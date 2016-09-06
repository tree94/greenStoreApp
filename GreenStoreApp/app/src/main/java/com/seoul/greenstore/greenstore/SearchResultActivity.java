package com.seoul.greenstore.greenstore;

/**
 * Created by X on 2016-09-06.
 */

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.seoul.greenstore.greenstore.Recycler.RecyclerAdapter;
import com.seoul.greenstore.greenstore.Recycler.Recycler_item;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String a = "미니언";
        List<Recycler_item> items = new ArrayList<>();
        Recycler_item[] item = new Recycler_item[10];
        item[0] = new Recycler_item(R.drawable.a, "망우찜쌈밥\n중랑구 용마산로 490");
        item[1] = new Recycler_item(R.drawable.b, "목우촌부추삼겹살\n관악구 남현1길 68-19");
        item[2] = new Recycler_item(R.drawable.c, "#3");
        item[3] = new Recycler_item(R.drawable.d, "#4");
        item[4] = new Recycler_item(R.drawable.e, "#5");
        item[5] = new Recycler_item(R.drawable.a, "#6");
        item[6] = new Recycler_item(R.drawable.b, "#7");
        item[7] = new Recycler_item(R.drawable.c, "#8");
        item[8] = new Recycler_item(R.drawable.d, "#9");
        item[9] = new Recycler_item(R.drawable.e, "#10");

        for (int i = 0; i < 10; i++) items.add(item[i]);

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_search_result));

        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
