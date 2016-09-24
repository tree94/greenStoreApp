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
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.seoul.greenstore.greenstore.Kakao.KakaoSignupActivity;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by X on 2016-09-06.
 */
public class LoginActivity  extends Activity implements View.OnClickListener {

    private SessionCallback callback;      //kakao 콜백 선언
    private CallbackManager callbackManager; // facebook 콜백 선언
    private LoginButton loginButton;
    private AccessToken token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        token = AccessToken.getCurrentAccessToken();
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);

        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);

        // facebook 매니저.
        callbackManager = CallbackManager.Factory.create();

        loginButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.facebook_login_button) {
            if (token != null) {
                LoginManager.getInstance().logOut();
                finish();
            } else {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(final LoginResult result) {
                    GraphRequest request;
                    request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject user, GraphResponse response) {
                            if (response.getError() != null) {
                            } else {
                                Log.i("TAG", "user: " + user.toString());
                                Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday");
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
            if(exception != null) {
                Log.e("exception",exception.toString());

            }
            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }

    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}