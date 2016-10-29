package com.seoul.greenstore.greenstore.Mypage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.R;
import com.seoul.greenstore.greenstore.Recycler.RecyclerAdapter;
import com.seoul.greenstore.greenstore.Recycler.Recycler_item;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.User.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X on 2016-09-29.
 */
public class MyPageFragment_store extends Fragment implements Server.ILoadResult{
    private static RecyclerAdapter recyclerAdapter;
    private static RecyclerView recyclerView;
    private static List<Recycler_item> data = new ArrayList<>();
    private static GridLayoutManager gridLayoutManager;
    private View view;

    public void setMyStoreLikeData(int id,String name,String addr,String photo){
        Recycler_item recycler_item = new Recycler_item();
        recycler_item.setId(id);
        recycler_item.setName(name);
        recycler_item.setAddr(addr);
        recycler_item.setImage(photo);
        data.add(recycler_item);
        if(recyclerAdapter!=null) recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.activity_mypage_store,null);

//        //좋아요 누른 공간
//        if(data.size()==0)
        getLikeItem();
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(data, getActivity());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }

    private void getLikeItem(){
        String[] gets = {Constants.GREEN_STORE_URL_APP_MYSTORElIKE + "/" + User.user.get(3), "GET"};
        Server server = new Server(getActivity(), this);
        server.execute(gets);
    }

    @Override
    public void customAddList(String result) {
        Log.v("test123","1111111111");
        data.clear();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                Recycler_item store = new Recycler_item();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                store.setId(Integer.parseInt(jsonObject.getString("sh_id")));
                store.setLike(Integer.parseInt(jsonObject.getString("sh_rcmn")));
                store.setUserLike(Integer.parseInt(jsonObject.getString("sh_like")));
                store.setName(jsonObject.getString("sh_name"));
                store.setAddr(jsonObject.getString("sh_addr"));
                store.setImage(jsonObject.getString("sh_photo"));
                store.setIndutyCode(Integer.parseInt(jsonObject.getString("induty_code_se")));
                data.add(store);
            }
            recyclerAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}