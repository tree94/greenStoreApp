package com.seoul.greenstore.greenstore.Facebook;

import android.support.v4.app.Fragment;

import com.facebook.login.widget.LoginButton;

/**
 * Created by user on 2016-09-19.
 */
public class FacebookFragment extends Fragment {
    private LoginButton loginButton;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_facebook,container,false);
//        loginButton = (LoginButton)view.findViewById(R.id.facebook_login_button);
//        loginButton.setReadPermissions("email");
//        loginButton.setFragment(this);
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//
//        return view;
//    }
}
