package com.seoul.greenstore.greenstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.seoul.greenstore.greenstore.Mypage.PagerAdapter;

/**
 * Created by X on 2016-09-30.
 */
public class MypageFragment extends AppCompatActivity {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView naviView;
    private ActionBarDrawerToggle drawerToggle;
    private SearchView searchView;
    private MenuItem searchItem;
    private TextView searchTextView;
    private String backStateName = null;
    private Fragment fragment = null;
    private static final int LOGIN_ACTIVITY = 0;

    Class fragmentClass = HomeFragment.class;
    FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        naviView = (NavigationView) findViewById(R.id.nvView);
        searchTextView = (TextView) findViewById(R.id.searchTextView);
        setupDrawerContent(naviView);

//        getSupportFragmentManager().beginTransaction().replace(R.id.llContents, new HomeFragment()).commit();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("좋아요 누른 공간"));
        tabLayout.addTab(tabLayout.newTab().setText("좋아요 누른 리뷰"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

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

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        switch (menuItem.getItemId()) {
            case R.id.nav_Home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_Login:
                fragmentClass = null;
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_ACTIVITY);
                break;
            case R.id.nav_Mypage:
                Intent intent2 = new Intent(this, MypageFragment.class);
                startActivityForResult(intent2, 1);
                break;
            case R.id.nav_Notice:
                fragmentClass = NoticeFragment.class;
                break;
            case R.id.nav_Service:
                fragmentClass = ReviewFragment.class;
                break;

            default:
                fragmentClass = ImageFragment.class;
        }

        if (fragmentClass != null) {
            System.out.println("??????");
            try {
                fragment = (Fragment) fragmentClass.newInstance();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Insert the fragment by replacing any existing fragment


        fragmentManager = getSupportFragmentManager();
        Fragment nowFragment = fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 1);

        if (fragmentClass != null)
            backStateName = fragmentClass.getClass().getName();

        if (!nowFragment.getClass().equals(fragmentClass) && fragmentClass != null) { //fragment not in back stack, create it.
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.llContents, fragment);
            transaction.addToBackStack(backStateName);
            transaction.commit();
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        if (menuItem.getTitle() != "Login")
            setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawer.closeDrawers();
    }

}
