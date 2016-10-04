package com.seoul.greenstore.greenstore.Kakao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
<<<<<<< HEAD
import android.widget.ImageView;
import android.widget.Toast;
=======
>>>>>>> 01c2a85bcbc7de8d119180b2e7aad9ba8e32a234

import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.util.helper.log.Logger;import com.kakao.usermgmt.response.model.UserProfile;

import com.seoul.greenstore.greenstore.LoginActivity;

public class KakaoSignupActivity extends Activity{

    private Intent intent;
    private Bundle extra;

    /**
     *
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

<<<<<<< HEAD
    ImageView kakaoProfileImage;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kakaoProfileImage = (ImageView) findViewById(R.id.profileImage);
        requestMe();
=======
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        kakaoProfileImage = (ImageView) findViewById(R.id.profileImage);
        intent = new Intent();
        extra = new Bundle();
        Log.v("test123?","123123");
//        requestMe();
>>>>>>> 01c2a85bcbc7de8d119180b2e7aad9ba8e32a234
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
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
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
<<<<<<< HEAD
                Log.d("UserProfile", userProfile.toString());


                Toast.makeText(KakaoSignupActivity.this, "로그인성공", Toast.LENGTH_SHORT).show();
                redirectMainActivity(); // 로그인 성공시 MainActivity로
=======
                Log.d("UserProfile" , userProfile.toString());
//                kakaoProfileImage.setImageResource(userProfile); 에러나서 관둠..오빠가하세요 캬캬캬
                redirectMainActivity(userProfile); // 로그인 성공시 MainActivity로
>>>>>>> 01c2a85bcbc7de8d119180b2e7aad9ba8e32a234
            }
        });
    }

<<<<<<< HEAD
    private void redirectMainActivity() {
        startActivity(new Intent(this, MainActivity.class));

        setTitle("GreenStore");
=======
    private void redirectMainActivity(UserProfile userProfile) {
        extra.putString("kakaoData",userProfile.toString());
        intent.putExtras(extra);
        setResult(RESULT_OK,intent);
>>>>>>> 01c2a85bcbc7de8d119180b2e7aad9ba8e32a234
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
