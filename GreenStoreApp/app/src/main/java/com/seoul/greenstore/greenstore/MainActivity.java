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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.Recycler.RecyclerAdapter;
import com.seoul.greenstore.greenstore.Recycler.Recycler_item;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.Spinner.Spinners;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private boolean flag = false;
    private List<Recycler_item> data = new ArrayList<Recycler_item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);


        //메인 페이지 스피너 등록
        Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        Spinner typeSpinner1 = (Spinner) findViewById(R.id.typeSpinner1);
        Spinner typeSpinner2 = (Spinner) findViewById(R.id.typeSpinner2);
        Spinner likeSpinner = (Spinner) findViewById(R.id.likeSpinner);

        //구, 업종, 좋아요와 관련된 스피너 등록 시 필요한 파라미터 전송
        Spinners spinner = new Spinners(MainActivity.this,locationSpinner,typeSpinner1,typeSpinner2,likeSpinner);

        //업종과 관련된 스피너 선택 시
        typeSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(flag){
                    System.out.println("??ididid > "+ id);
                    sortCategory(id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "not selected", Toast.LENGTH_SHORT).show();
            }
        });



        //좋아요와 관련된 스피너 선택 시
        likeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.print("??"+ flag);
                if(flag){
                    System.out.println("?!?!?!?!??!??!!!");
                    RecyclerAdapter ra = new RecyclerAdapter();
                    ra.sort();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "not selected", Toast.LENGTH_SHORT).show();
            }
        });

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

    }

    // 서버 요청 결과 값을 adapter를 이용해 recyclerView와 연결
    public void addList(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                Recycler_item store = new Recycler_item();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                store.setId(Integer.parseInt(jsonObject.getString("sh_id")));
                store.setLike(Integer.parseInt(jsonObject.getString("sh_rcmn")));
                store.setName(jsonObject.getString("sh_name"));
                store.setAddr(jsonObject.getString("sh_addr"));
                store.setImage(jsonObject.getString("sh_photo"));
                store.setIndutyCode(Integer.parseInt(jsonObject.getString("induty_code_se")));
                data.add(store);
            }
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            adapter = new RecyclerAdapter(MainActivity.this,data);
            recyclerView.setAdapter(adapter);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 선택된 카테고리에 해당하는 정보들만 추출하여 화면에 출력
    public void sortCategory(long id){
        List<Recycler_item> tempData = new ArrayList<Recycler_item>();

        int check = (int)id + 1;
        // 반복문을 돌면서 사용자가 선택한 카테고리(id)와 data에 들어있는 indutyCode가 같은것을 찾아서 tempData에 참조시킴.
        // 만약 선택된 값이 9이면 9이상인 데이터를 모두 기타 서비스업으로 취급함
        if(check==9){
            for (int i = 0; i < data.size(); ++i) {
                if (data.get(i).getIndutyCode() >= check) {
                    System.out.println("match id!");
                    tempData.add(data.get(i));
                }
            }
        }else {
            for (int i = 0; i < data.size(); ++i) {
                if (check == data.get(i).getIndutyCode()) {
                    System.out.println("match id!");
                    tempData.add(data.get(i));
                }
            }
        }
        adapter = new RecyclerAdapter(MainActivity.this,tempData);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
            Intent intent = new Intent(this, NoticeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
