package com.seoul.greenstore.greenstore.Mypage;

/**
 * Created by X on 2016-09-30.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private static Fragment tab1;
    private static Fragment tab2;
    private static int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab1 = new MyPageFragment_store();
                return tab1;
            case 1:
                tab2 = new MyPageFragment_review();
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