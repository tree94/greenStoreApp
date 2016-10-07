package com.seoul.greenstore.greenstore;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.seoul.greenstore.greenstore.Commons.BackPressCloseHandler;
import com.seoul.greenstore.greenstore.Review.ReviewWriteFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private MenuItem menu;
    private NavigationView naviView;
    private ActionBarDrawerToggle drawerToggle;
    //private Fragment fragment = null;
public static Fragment fragment = null;
    private SearchView searchView;
    private MenuItem searchItem;
    private TextView searchTextView;
    private ImageView profileImage;
    private TextView userIdView;
    private ArrayList<String> facebookUserData = null;
    private ArrayList<String> kakaoUserData = null;
    private MenuItem loginItem;
    private String backStateName = null;
    private static BackPressCloseHandler backPressCloseHandler;
    private static final int LOGIN_ACTIVITY = 0;
    private static final int MY_PAGE = 1;
    public static final Stack<Fragment> mStack = new Stack<>();
    public static String strCommon;


//    Class fragmentClass = HomeFragment.class;
public static Class fragmentClass = HomeFragment.class;
public static FragmentManager fragmentManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        naviView = (NavigationView) findViewById(R.id.nvView);
        searchTextView = (TextView) findViewById(R.id.searchTextView);
        setupDrawerContent(naviView);

        backPressCloseHandler = new BackPressCloseHandler(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.llContents, new HomeFragment()).commit();

        fragmentManager = getSupportFragmentManager();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.getStringArrayListExtra("userData") != null) {
                        facebookUserData = data.getStringArrayListExtra("userData");
                        profileImage = (ImageView) naviView.findViewById(R.id.profileImage);
                        userIdView = (TextView) naviView.findViewById(R.id.userId);
                        userIdView.setText(facebookUserData.get(1));
                        Picasso.with(getApplicationContext()).load(facebookUserData.get(5)).fit().into(profileImage);
                    } else {
                        kakaoUserData = data.getStringArrayListExtra("kakaoData");
                        userIdView = (TextView) naviView.findViewById(R.id.userId);
                        userIdView.setText(kakaoUserData.get(0));
                    }
                    menu.setTitle("Logout");
                }break;
        }
    }

    //Drawer에서 항목을 선택했을 때 전환해줄 fragment 선택
    public void selectDrawerItem(final MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        this.menu = menuItem;
        switch (menuItem.getItemId()) {
            case R.id.nav_Home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_Login:
                if(facebookUserData==null && kakaoUserData==null) {
                    fragmentClass = null;
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_ACTIVITY);
                }
                else{
                    profileImage = (ImageView)findViewById(R.id.profileImage);
                    if(facebookUserData!=null){
                        LoginManager.getInstance().logOut();
                        facebookUserData=null;
                    }else{
                        Log.v("kakaologout","logout");
                        UserManagement.requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Log.v("loginCheck","checkechk");
                            }
                        });
                        kakaoUserData=null;
                    }
                    profileImage.setImageResource(R.drawable.circle);
                    userIdView.setText("로그인하세요");
                    menuItem.setTitle("Login");
                }
                break;
            case R.id.nav_Mypage:
                fragmentClass = null;
                Intent intent2 = new Intent(this, MypageFragment.class);
//                startActivityForResult(intent2,MY_PAGE);
                startActivity(intent2);
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


//        fragmentManager = getSupportFragmentManager();
        Fragment nowFragment = fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 1);

        if(fragmentClass!=null)
        backStateName = fragmentClass.getClass().getName();

        if (fragmentClass!=null && !nowFragment.getClass().equals(fragmentClass)) { //fragment not in back stack, create it.
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.llContents, fragment);
            transaction.addToBackStack(backStateName);
            transaction.commit();
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        if(menuItem.getTitle()!="Login")
            setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawer.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main,menu);
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            searchItem = menu.findItem(R.id.action_search);
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("음식이나 지역을 입력하세요.");
            searchView.setOnQueryTextListener(queryTextListener);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            if (null != searchManager) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            }
            searchView.setIconifiedByDefault(true);

        }
        return true;
    }

    private OnQueryTextListener queryTextListener = new OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {

            strCommon = query;
            if(fragmentClass!=null)
            backStateName = fragmentClass.getClass().getName();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.llContents, SearchResultFragment.newInstance());
            transaction.addToBackStack(backStateName);
            transaction.commit();


            setTitle("검색결과");
            searchView.setQuery("", false);
            searchView.setIconified(true);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // TODO Auto-generated method stub
            return false;
        }
    };


    //툴바 메뉴에서 옵션 아이템 선택했을 때
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
     /*       case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                break;
*/
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_search:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to
    // override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        Log.d("___", "fragment.getClass().toString() : " + fragment.getClass().toString());
//        Log.d("___", "HomeFragment.class.toString() : " + HomeFragment.class.toString());
        Log.d("___", "size : " + fragmentManager.getFragments().size());

        //Fragment nowFragment = fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 1);
        if( fragment!=null) {
            if( fragmentManager.getFragments().size() <= 1 ) // HOW TO : 지금 프래그먼트가 HomeFragment인지 검사 ???    // fragment.getClass().equals(HomeFragment.class) )
                backPressCloseHandler.onBackPressed();
        } else {
            Log.d("___", "fragment is null");
        }
    }

    public static void changeFragment(String strNewFragment) {
        if(strNewFragment.equals("fragment1")) {
            //Log.d("___","newFragment : fragment1");
            //Log.d("___","fragmentManager.getFragments().size() : " + fragmentManager.getFragments().size() );

            Fragment nowFragment = fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 1);

            fragmentClass = ReviewWriteFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch(Exception e) { e.printStackTrace(); }

            if (fragmentClass!=null && !nowFragment.getClass().equals(fragmentClass)) { //fragment not in back stack, create it.
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.llContents, fragment);
                transaction.addToBackStack(fragmentClass.getClass().getName());
                transaction.commit();
            }

            //fragmentManager.beginTransaction().replace(R.id.llContents, new ReviewWriteFragment()).commit();

        }

    }


}