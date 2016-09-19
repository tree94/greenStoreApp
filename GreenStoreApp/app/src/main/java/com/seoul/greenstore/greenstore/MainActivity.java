package com.seoul.greenstore.greenstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Stack;

public class MainActivity extends AppCompatActivity  {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView naviView;
    private ActionBarDrawerToggle drawerToggle;
    Fragment fragment = null;
    Class fragmentClass = HomeFragment.class;
    SearchView searchView = null;
    FragmentManager fragmentManager;

    public static final Stack<Fragment> mStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();
        //searchView를 통해 검색할 때 행할 옵션 리스너

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment fragment = new Fragment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.activity_search, fragment );
                fragmentTransaction.commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        naviView = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(naviView);

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

    //Drawer에서 항목을 선택했을 때 전환해줄 fragment 선택
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = null;
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_third_fragment:
                fragmentClass = SearchResultFragment.class;
                break;
            case R.id.nav_fourth_fragment:
                fragmentClass = ImageFragment.class;
                break;
            case R.id.nav_fifth_fragment:
                fragmentClass = ImageFragment.class;
                break;

            default:
                fragmentClass = ImageFragment.class;
        }

        if(fragmentClass!=null) {
            System.out.println("??????");
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Insert the fragment by replacing any existing fragment
        fragmentManager = getSupportFragmentManager();
        Fragment nowFragment = fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1);

        if(!nowFragment.getClass().equals(fragmentClass))
            if(fragment!=null) fragmentManager.beginTransaction().replace(R.id.llContents, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawer.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Inflate the menu; this adds items to the action bar if it is present.

//        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    //툴바 메뉴에서 옵션 아이템 선택했을 때
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                break;

            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_search:

                Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
               /* fragmentClass = NoticeFragment.class;
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment nowFragment = fragmentManager.getFragments().get(fragmentManager.getFragments().size()-1);

                if(!nowFragment.getClass().equals(fragmentClass))
                    if(fragment!=null) fragmentManager.beginTransaction().replace(R.id.llContents, fragment).commit();
*/
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}