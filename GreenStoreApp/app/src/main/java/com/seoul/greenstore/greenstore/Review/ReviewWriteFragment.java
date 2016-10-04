package com.seoul.greenstore.greenstore.Review;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.R;
import com.seoul.greenstore.greenstore.Server.Server;

/**
 * Created by X on 2016-10-04.
 */
public class ReviewWriteFragment extends Fragment implements Server.ILoadResult{

    public static ReviewWriteFragment newInstance(){
        ReviewWriteFragment fragment = new ReviewWriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("coffee", "Review onstart IN");

        String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_WRITE, "GET"};
        Server server = new Server(getActivity(), this);
        server.execute(gets);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_review, null);

        return view;
    }


    @Override
    public void customAddList(String result) {

    }
}
