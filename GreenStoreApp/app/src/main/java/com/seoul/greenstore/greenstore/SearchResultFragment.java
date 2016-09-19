package com.seoul.greenstore.greenstore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by X on 2016-09-08.
 */
public class SearchResultFragment extends Fragment {

    public SearchResultFragment newInstance(){
        SearchResultFragment fragment = new SearchResultFragment();
        return fragment;
    }


    public SearchResultFragment() {

// Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_search, container, false);
    }

}