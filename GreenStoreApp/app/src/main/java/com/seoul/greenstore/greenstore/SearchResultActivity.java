/*
package com.seoul.greenstore.greenstore;

*/
/**
 * Created by X on 2016-09-06.
 *//*


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.seoul.greenstore.greenstore.Recycler.RecyclerAdapter;

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;

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

        Intent intent = new Intent(this.getIntent());
        String locationItem = intent.getStringExtra("locationItem");
        int typeId = intent.getIntExtra("typeId",0);

        System.out.println("location = "+locationItem);
        System.out.println("typeId = "+typeId);

        String[] gets = {"/"};
//        Server server = new Server(this);


    }

    // 서버 요청 결과 값을 adapter를 이용해 recyclerView와 연결
//    public void addList(String result) {
//        try {
//            List<Recycler_item> data = new ArrayList<Recycler_item>();
//            JSONArray jsonArray = new JSONArray(result);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                Recycler_item store = new Recycler_item();
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                store.setId(Integer.parseInt(jsonObject.getString("sh_id")));
//                store.setLike(Integer.parseInt(jsonObject.getString("sh_rcmn")));
//                store.setName(jsonObject.getString("sh_name"));
//                store.setAddr(jsonObject.getString("sh_addr"));
//                store.setImage(jsonObject.getString("sh_photo"));
//                data.add(store);
//            }
//            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//            adapter = new RecyclerAdapter(SearchResultActivity.this,data);
//            recyclerView.setAdapter(adapter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
*/
