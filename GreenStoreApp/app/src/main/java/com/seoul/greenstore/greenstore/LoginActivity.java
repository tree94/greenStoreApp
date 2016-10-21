package com.seoul.greenstore.greenstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by X on 2016-09-06.
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    private SessionCallback callback;      //kakao 콜백 선언
    private CallbackManager callbackManager; // facebook 콜백 선언

    private LoginButton FacebookLoginButton;
    private com.kakao.usermgmt.LoginButton kakaoLoginButton;
    private AccessToken token;
    private Intent intent;
    private Bundle extra;
    private String picUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        intent = new Intent();

        token = AccessToken.getCurrentAccessToken();

        //kakao callback
        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);

        FacebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        kakaoLoginButton = (com.kakao.usermgmt.LoginButton) findViewById(R.id.com_kakao_login);

        // facebook 매니저.
        callbackManager = CallbackManager.Factory.create();

        FacebookLoginButton.setOnClickListener(this);
        kakaoLoginButton.setOnClickListener(this);
    }

    //    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.facebook_login_button) {
            if (token != null) {
                LoginManager.getInstance().logOut();
                finish();
            } else {
//                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(final LoginResult result) {
                        GraphRequest request;
                        request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user, GraphResponse response) {
                                if (response.getError() != null) {
                                } else {
                                    String userId = user.optString("id");
                                    String userName = user.optString("name");
                                    String userEmail = user.optString("email");
                                    String gender = user.optString("gender");
                                    String birth = user.optString("birthday");

                                    try {
                                        picUrl = user.getJSONObject("picture").getJSONObject("data").getString("url");
                                        Log.i("PICURL", picUrl);
//                                        setResult(RESULT_OK,intent);
//                                        finish();
                                        // Picasso.with(LoginActivity.this).load(picUrl).into(profileImage);
//                                        new AsyncProfileTask().execute(picUrl);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("TAG", "user: " + user.toString());
                                    Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());

                                    ArrayList<String> userData = new ArrayList<String>(
                                            Arrays.asList(userId, userName, picUrl, userEmail, gender, birth)
                                    );
                                    extra = new Bundle();
                                    extra.putStringArrayList("userData", userData);
                                    intent.putExtras(extra);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,picture,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("test", "Error: " + error);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        finish();
                    }
                });
            }
        }
    }

    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환

                Log.d("UserProfile", userProfile.toString());
                redirectMainActivity(userProfile); // 로그인 성공시 MainActivity로
//                kakaoProfileImage.setImageResource(userProfile); 에러나서 관둠..오빠가하세요 캬캬캬

            }
        });
    }

    private void redirectMainActivity(UserProfile userProfile) {
        Log.v("userprofile", "profile1");
        String userId = String.valueOf(userProfile.getId());
        String nickname = userProfile.getNickname();
        String profile = userProfile.getProfileImagePath();
        ArrayList<String> userData = new ArrayList<String>(
                Arrays.asList(userId, nickname, profile)
        );

        extra = new Bundle();
        extra.putStringArrayList("kakaoData", userData);
        intent.putExtras(extra);
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.e("exception", exception.toString());
            }
            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }


    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        requestMe();
    }
}