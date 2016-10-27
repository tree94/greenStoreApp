package com.seoul.greenstore.greenstore.User;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by user on 2016-10-10.
 */
public class User{
    //sharedPreferences
    private SharedPreferences sharedPreferences;

    //user.get(0) = 유저의 facebook id 또는 kakao id 고유 값
    //user.get(1) = 유저 name
    //user.get(2) = 유저 picUrl
    //user.get(3) = 유저의 우리가 부여한 id
    public static ArrayList<String> user;

    //좋아요 사용법.
    public static Map<Integer,String> userReviewLike;

    //반복문을 돌리면서 map.get(i) 해당 sh_id가 현재 sh_id랑 같은지 확인.
    public static Map<Integer,String> userStoreLike;

//    public List<User> getUser(){
//        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
//
//    }

    public static void userReset() {
        User.user = null;
        User.userStoreLike = null;
        User.userReviewLike = null;
    }
}
