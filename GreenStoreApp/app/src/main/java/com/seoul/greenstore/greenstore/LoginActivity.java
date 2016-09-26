package com.seoul.greenstore.greenstore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

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
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by X on 2016-09-06.
 */

//final class BitmapTarget implements Target {
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//        LoginActivity.profileImage.setBackground(bitmapDrawable);
//
//        //imageViewTest
////        ImageView image = new ImageView(this);
////        image.setImageResource("");
////        setContentView(image);
////        ((ImageView) findViewById(R.id.imageView1))
////        bmImage.setImageBitmap(result);
////
//
//        //LoginActivity.imageViewTest.setImageBitmap(bitmap);
//        //LoginActivity.textViewTest.setBackground(bitmapDrawable);
//        Log.d("TAG", "onBitmapLoaded.");
//
//    }
//
//    @Override
//    public void onBitmapFailed(Drawable errorDrawable) {
//        Log.d("TAG", "FAILED");
//    }
//
//    @Override
//    public void onPrepareLoad(Drawable placeHolderDrawable) {
//        Log.d("TAG", "Prepare Load");
//    }
//}
public class LoginActivity extends Activity implements View.OnClickListener {

    private SessionCallback callback;      //kakao 콜백 선언
    private CallbackManager callbackManager; // facebook 콜백 선언
    private LoginButton loginButton;
    private AccessToken token;
    private ImageButton profileImage;
    private LayoutInflater layoutInflater;
    private View naviLayout;
    private Target target;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        token = AccessToken.getCurrentAccessToken();
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);

        //profileImage아이디를 다른 레이아웃으로부터 가져옴
        layoutInflater = getLayoutInflater();
        naviLayout = layoutInflater.inflate(R.layout.nav_header_main, null);



        profileImage = (ImageButton) naviLayout.findViewById(R.id.profileImage);

        Log.e("imageid", "" + profileImage);
        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);

        // facebook 매니저.
        callbackManager = CallbackManager.Factory.create();

        loginButton.setOnClickListener(this);

    }

    //    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.facebook_login_button) {
            if (token != null) {
                LoginManager.getInstance().logOut();
                finish();
            } else {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
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
                                    System.out.println(userId + " ~!!");
                                    //프로필 이미지
//                                    Target bitmapTarget = new BitmapTarget();
                                    try {
                                        String picUrl = user.getJSONObject("picture").getJSONObject("data").getString("url");

                                        Log.i("PICURL", picUrl);

                                       // Picasso.with(LoginActivity.this).load(picUrl).into(profileImage);
                                        new AsyncProfileTask().execute(picUrl);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
//                                    new GraphRequest(
//                                            AccessToken.getCurrentAccessToken(),
//                                            "/"+userId+"/picture",
//                                            null,
//                                            HttpMethod.GET,
//                                            new GraphRequest.Callback() {
//                                                public void onCompleted(GraphResponse response) {
//            /* handle the result */                 JSONObject res = response.getJSONObject();
//                                                    Log.i("res",""+res);
//                                                }
//                                            }
//                                    ).executeAsync();
                                   // new ProfileThread(userId).start();
//                                    Picasso.with(getApplicationContext()).load("http://graph.facebook.com/" + userId + "/picture").into(new Target() {
//                                        @Override
//                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//                                            profileImage.setBackground(bitmapDrawable);
//                                        }
//
//                                        @Override
//                                        public void onBitmapFailed(Drawable errorDrawable) {
//                                            Log.d("TAG", "FAILED");
//                                        }
//
//                                        @Override
//                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
//                                            Log.d("TAG", "Prepare Load");
//                                        }
//                                    });
                                    Log.i("TAG", "user: " + user.toString());
                                    Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
                                    setResult(RESULT_OK);
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
        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }



    private class AsyncProfileTask extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(MainActivity.profileImage != null)
             MainActivity.profileImage.setImageBitmap(bitmap);

        }
    }




}