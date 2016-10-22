package com.seoul.greenstore.greenstore;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seoul.greenstore.greenstore.Mypage.PagerAdapter;

/**
 * Created by X on 2016-09-30.
 */
public class MypageFragment extends Fragment {
    private static View view;
    private static ViewPager viewPager;
    private static TabLayout tabLayout;
    private static PagerAdapter adapter;

    public MypageFragment newInstance() {
        MypageFragment fragment = new MypageFragment();
        return fragment;
    }

    public MypageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null)
        view = inflater.inflate(R.layout.activity_mypage, null);

        if(tabLayout==null) {
            tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText("좋아요 누른 공간"));
            tabLayout.addTab(tabLayout.newTab().setText("좋아요 누른 리뷰"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }


        if(adapter==null)
        adapter = new PagerAdapter(getFragmentManager(), tabLayout.getTabCount());

        if(viewPager==null) {
            viewPager = (ViewPager) view.findViewById(R.id.pager);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("pauseddddd", "11111");
    }
}
