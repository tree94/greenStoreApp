package com.seoul.greenstore.greenstore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

/**
 * Created by X on 2016-10-14.
 */
public class ServiceFragment extends Fragment {

    CarouselView carouselView;

    int[] images = {R.drawable.cheongyecheon, R.drawable.salon, R.drawable.korean};


    public ServiceFragment newInstance() {
        ServiceFragment fragment = new ServiceFragment();
        return fragment;
    }


    public ServiceFragment() {

// Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_service, container, false);
        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setPageCount(images.length);

        carouselView.setImageListener(imageListener);
// Inflate the layout for this fragment
        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(images[position]);
        }
    };

}

