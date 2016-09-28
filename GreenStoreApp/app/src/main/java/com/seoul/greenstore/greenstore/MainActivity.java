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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private MenuItem menu;
    private NavigationView naviView;
    private ActionBarDrawerToggle drawerToggle;
    private Fragment fragment = null;
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
    public static final Stack<Fragment> mStack = new Stack<>();
    public static String strCommon;


    Class fragmentClass = HomeFragment.class;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("startAcitivit","12312344");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        naviView = (NavigationView) findViewById(R.id.nvView);
        searchTextView = (TextView) findViewById(R.id.searchTextView);
        setupDrawerContent(naviView);

        backPressCloseHandler = new BackPressCloseHandler(this);

//        Intent intent = getIntent();
//        kakaoUserData = intent.getStringExtra("kakaoData");

        getSupportFragmentManager().beginTransaction().replace(R.id.llContents, new HomeFragment()).commit();
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
        switch (requestCode){
            case LOGIN_ACTIVITY:
                if(resultCode==RESULT_OK){
                    if(data.getStringArrayListExtra("userData")!=null) {
                        facebookUserData = data.getStringArrayListExtra("userData");
                        profileImage = (ImageView) naviView.findViewById(R.id.profileImage);
                        userIdView = (TextView) naviView.findViewById(R.id.userId);
                        userIdView.setText(facebookUserData.get(1));
                        Picasso.with(getApplicationContext()).load(facebookUserData.get(5)).fit().into(profileImage);
                    }
                    else{
                        kakaoUserData = data.getStringArrayListExtra("kakaoData");
                        try{
                            Log.v("kakaouserData",kakaoUserData.get(0));
                        }catch (Exception e){}
                        userIdView = (TextView) naviView.findViewById(R.id.userId);
                        userIdView.setText(kakaoUserData.get(0));
                    }
                    Log.v("test123","!2345555");
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
                        finish();
                    }else{
                        Log.v("kakaologout","logout");
                        UserManagement.requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Log.v("loginCheck","checkechk");
                            }
                        });
                    }
                    profileImage.setImageResource(R.drawable.circle);
                    userIdView.setText("로그인하세요");
                    menuItem.setTitle("Login");
                }

                break;
            case R.id.nav_Mypage:
                fragmentClass = ImageFragment.class;
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

        if(fragmentClass!=null)
        backStateName = fragmentClass.getClass().getName();

        if (!nowFragment.getClass().equals(fragmentClass) && fragmentClass!=null) { //fragment not in back stack, create it.
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

        backPressCloseHandler.onBackPressed();
    }


}