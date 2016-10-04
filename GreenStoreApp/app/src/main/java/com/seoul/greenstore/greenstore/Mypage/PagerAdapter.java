package com.seoul.greenstore.greenstore.Mypage;

/**
 * Created by X on 2016-09-30.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MyPageFragment_store tab1 = new MyPageFragment_store();
                return tab1;
            case 1:
                MyPageFragment_review tab2 = new MyPageFragment_review();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}