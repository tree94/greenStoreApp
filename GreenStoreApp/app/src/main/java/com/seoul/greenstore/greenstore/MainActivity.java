package com.seoul.greenstore.greenstore;

import android.content.Intent;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.Recycler.RecyclerAdapter;
import com.seoul.greenstore.greenstore.Recycler.Recycler_item;
import com.seoul.greenstore.greenstore.Server.Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    ListView listView;
//    ListViewAdapter adapter;

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);


        //메인 페이지 스피너 등록
        Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        Spinner typeSpinner1 = (Spinner) findViewById(R.id.typeSpinner1);
        Spinner typeSpinner2 = (Spinner) findViewById(R.id.typeSpinner2);
        Spinner likeSpinner = (Spinner) findViewById(R.id.likeSpinner);

        //구와 관련된 스피너 등록
        List<String> locList = new ArrayList<String>();
        locList.add("강남구");
        locList.add("강동구");
        locList.add("강북구");
        locList.add("강서구");
        locList.add("관악구");
        locList.add("광진구");
        locList.add("구로구");
        locList.add("금천구");
        locList.add("노원구");
        locList.add("도봉구");
        locList.add("동대문구");
        locList.add("동작구");
        locList.add("마포구");
        locList.add("서대문구");
        locList.add("서초구");
        locList.add("성동구");
        locList.add("성북구");
        locList.add("송파구");
        locList.add("양천구");
        locList.add("영등포구");
        locList.add("용산구");
        locList.add("은평구");
        locList.add("종로구");
        locList.add("중구");
        locList.add("중랑구");

        ArrayAdapter<String> locationSpinnerApater = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locList);
        locationSpinnerApater.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        locationSpinner.setAdapter(locationSpinnerApater);

        //업종과 관련된 스피너 등록
        List<String> secList = new ArrayList<String>();
        secList.add("한식");
        secList.add("중식");
        secList.add("경양식,일식");
        secList.add("기타외식업");
        secList.add("이,미용업");
        secList.add("목욕업");
        secList.add("세탁업");
        secList.add("숙박업");
        secList.add("기타서비스업종");


        ArrayAdapter<String> typeSpinner1Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, secList);
        typeSpinner1Adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typeSpinner1.setAdapter(typeSpinner1Adapter);
        typeSpinner2.setAdapter(typeSpinner1Adapter); //스피너 Spinner 1,2 내용 똑같아서 그냥 같이붙임


        List<String> descList = new ArrayList<String>();
        descList.add("서울시 추천 순 정렬");
        descList.add("사용자 좋아요 순 정렬");


        ArrayAdapter<String> likeSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, descList);
        likeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        likeSpinner.setAdapter(likeSpinnerAdapter);


        recyclerView.setLayoutManager(layoutManager);

        /* 메일보내기 버튼
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
//        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //navigationView 등록!
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //서버에 데이터 요청
        String[] gets = {"/","GET"};
        Server server = new Server(this);
        server.execute(gets);

//        adapter = new RecyclerAdapter();
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //메인페이지 recyclerView에 나올 아이템 ArrayList에 추가
        //클릭하면 하는 행동(현재는 Toast메시지출력)은 RecyclerAdapter.java에서

//        String a = "미니언";
//        List<Recycler_item> items = new ArrayList<>();
//        Recycler_item[] item = new Recycler_item[10];
//        item[0] = new Recycler_item(R.drawable.a, "망우찜쌈밥\n중랑구 용마산로 490");
//        item[1] = new Recycler_item(R.drawable.b, "목우촌부추삼겹살\n관악구 남현1길 68-19");
//        item[2] = new Recycler_item(R.drawable.c, "#3");
//        item[3] = new Recycler_item(R.drawable.d, "#4");
//        item[4] = new Recycler_item(R.drawable.e, "#5");
//        item[5] = new Recycler_item(R.drawable.a, "#6");
//        item[6] = new Recycler_item(R.drawable.b, "#7");
//        item[7] = new Recycler_item(R.drawable.c, "#8");
//        item[8] = new Recycler_item(R.drawable.d, "#9");
//        item[9] = new Recycler_item(R.drawable.e, "#10");
//
//        for (int i = 0; i < 10; i++) items.add(item[i]);
//
//        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_main));
    }

    // 서버 요청 결과 값을 adapter를 이용해 recyclerView와 연결
    public void addList(String result) {
        try {
            List<Recycler_item> data = new ArrayList<Recycler_item>();
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                Recycler_item store = new Recycler_item();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                store.setId(Integer.parseInt(jsonObject.getString("sh_id")));
                store.setLike(Integer.parseInt(jsonObject.getString("sh_rcmn")));
                store.setName(jsonObject.getString("sh_name"));
                store.setAddr(jsonObject.getString("sh_addr"));
                store.setImage(jsonObject.getString("sh_photo"));
                data.add(store);
            }
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            adapter = new RecyclerAdapter(MainActivity.this,data);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //툴바 메뉴에서 옵션 아이템 선택했을 때
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        EditText searchString = (EditText) findViewById(R.id.searchString);
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_search:
                Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
                searchString.setVisibility(View.VISIBLE);
                String getEdit = searchString.getText().toString();
                if(getEdit.getBytes().length>0){
                    Intent intent = new Intent(this, SearchResultActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(MainActivity.this, "카메라를 선택했습니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
