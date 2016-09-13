package com.seoul.greenstore.greenstore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by X on 2016-09-08.
 */
public class ImageFragment extends Fragment {

    public ImageFragment newInstance(){
        ImageFragment fragment = new ImageFragment();
        return fragment;
    }


    public ImageFragment() {

// Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

}