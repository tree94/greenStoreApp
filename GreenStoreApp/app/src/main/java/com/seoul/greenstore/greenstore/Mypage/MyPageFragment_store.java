package com.seoul.greenstore.greenstore.Mypage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seoul.greenstore.greenstore.R;

/**
 * Created by X on 2016-09-29.
 */
public class MyPageFragment_store extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_mypage_store, container, false);
    }
}