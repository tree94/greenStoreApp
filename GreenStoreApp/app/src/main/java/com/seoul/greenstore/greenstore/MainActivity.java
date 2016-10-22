package com.seoul.greenstore.greenstore;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.User.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements Server.ILoadResult {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private MenuItem menu;
    private NavigationView naviView;
    private NavigationView tempNaviView;
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


    //Class fragmentClass = HomeFragment.class;
    public static Class fragmentClass = HomeFragment.class;
    public static FragmentManager fragmentManager = null;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        searchTextView = (TextView) findViewById(R.id.searchTextView);

        naviView = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(naviView);


        backPressCloseHandler = new BackPressCloseHandler(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.llContents, new HomeFragment()).commit();

        fragmentManager = getSupportFragmentManager();


        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



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
                        User.user = facebookUserData;
                        profileImage = (ImageView) naviView.findViewById(R.id.profileImage);
                        userIdView = (TextView) naviView.findViewById(R.id.userId);
                        userIdView.setText(facebookUserData.get(1));
                        Picasso.with(getApplicationContext()).load(User.user.get(2)).fit().into(profileImage);
                    } else {
                        kakaoUserData = data.getStringArrayListExtra("kakaoData");
                        User.user = kakaoUserData;

                        userIdView = (TextView) naviView.findViewById(R.id.userId);
                        userIdView.setText(kakaoUserData.get(1));
                    }
                    menu.setTitle("Logout");
                    //사용자 조회
                    memberLookup();

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    Fragment fragment = new HomeFragment();

                    fragmentTransaction.replace(R.id.llContents, fragment);
                    fragmentTransaction.addToBackStack(fm.findFragmentById(R.id.llContents).toString());
                    fragmentTransaction.commit();
                }
                break;
        }
    }

    //사용자 조회 메소드
    private void memberLookup() {
        String[] gets = {Constants.GREEN_STORE_URL_APP_MEBERLOOKUP, "POST", "memberLookup", "", "", ""};
        gets[3] = User.user.get(0);
        gets[4] = User.user.get(1);
        gets[5] = User.user.get(2);

        Server server = new Server(MainActivity.this, this);
        server.execute(gets);
    }

    @Override
    protected void onPause(){
        super.onPause();
        userReset();
    }

    private void userReset(){
        User.user = null;
        User.userStoreLike = null;
        User.userReviewLike = null;
    }

    @Override
    public void customAddList(String result) {
        //사용자가 좋아요 한 정보들을 hashMap 컬렉션에 저장
        try {
            JSONArray jsonArray = new JSONArray(result);
            Map<Integer, String> tempStoreMap = new HashMap<>();
            Map<Integer, String> tempReviewMap = new HashMap<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (i == 0)
                    User.user.add(3, jsonObject.getString("mkey"));
                tempReviewMap.put(i, jsonObject.getString("rkey"));
                tempStoreMap.put(i, jsonObject.getString("sh_id"));
            }
            User.userReviewLike = tempReviewMap;
            User.userStoreLike = tempStoreMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                if (facebookUserData == null && kakaoUserData == null) {
                    fragmentClass = null;
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_ACTIVITY);
                } else {
                    profileImage = (ImageView) findViewById(R.id.profileImage);
                    if (facebookUserData != null) {
                        LoginManager.getInstance().logOut();
                        facebookUserData = null;
                    } else {
                        Log.v("kakaologout", "logout");
                        UserManagement.requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Log.v("loginCheck", "checkechk");
                            }
                        });
                        kakaoUserData = null;
                    }
                    User.user = null;
                    User.userStoreLike = null;
                    User.userReviewLike = null;
                    profileImage.setImageResource(R.drawable.circle);
                    userIdView.setText("로그인하세요");
                    menuItem.setTitle("Login");

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_Mypage:
                if(User.user!=null)
                    fragmentClass = MypageFragment.class;
                else
                    Toast.makeText(this,"로그인을 해주세요.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_Notice:
                fragmentClass = NoticeFragment.class;
                break;
            case R.id.nav_Service:
                fragmentClass = ServiceFragment.class;
//                Intent intent = new Intent(this, PlayActivity.class);
//                startActivity(intent);
                break;
            case R.id.nav_Review:
                fragmentClass = ReviewFragment.class;
                break;

            default:
                fragmentClass = ImageFragment.class;
        }

        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Insert the fragment by replacing any existing fragment


        fragmentManager = getSupportFragmentManager();
        Fragment nowFragment = fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 1);


        if (fragmentClass != null) {
            backStateName = fragmentClass.getClass().getName();
//            if (!nowFragment.getClass().equals(fragmentClass)) { //fragment not in back stack, create it.
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.llContents, fragment);
            transaction.addToBackStack(backStateName);
            transaction.commit();

        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);

        // Set action bar title
        // Close the navigation drawer
        drawer.closeDrawers();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main,menu);
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);

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

            if (fragmentClass != null)
            backStateName = fragmentClass.getClass().getName();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.llContents, SearchResultFragment.newInstance());
            transaction.addToBackStack("");
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
*//*
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_search:
                break;*/
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to
    // override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public static void changeFragment(String strNewFragment) {
        switch (strNewFragment) {
            case "Review":
                fragmentClass = ReviewFragment.class;
                break;

        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //fragment not in back stack, create it.
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.llContents, fragment);
        transaction.addToBackStack(fragmentClass.getClass().getName());
        transaction.commit();


        //fragmentManager.beginTransaction().replace(R.id.llContents, new ReviewWriteFragment()).commit();

    }

}


