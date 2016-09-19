package com.seoul.greenstore.greenstore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seoul.greenstore.greenstore.Recycler.RecyclerAdapter;
import com.seoul.greenstore.greenstore.Recycler.Recycler_item;
import com.seoul.greenstore.greenstore.Server.Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X on 2016-09-08.
 */
public class SearchResultFragment extends Fragment implements Server.ILoadResult{
    private RecyclerAdapter adapter;
    private View view;
    private List<Recycler_item> data = new ArrayList<Recycler_item>();
    RecyclerView recyclerView = null;

    public SearchResultFragment newInstance(){
        SearchResultFragment fragment = new SearchResultFragment();
        return fragment;
    }

    public SearchResultFragment() {

// Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onStart() {
        super.onStart();
        String[] gets = {"/", "GET"};
        Server server = new Server(getActivity(),this);
        server.execute(gets);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, null);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSearch);
        adapter = new RecyclerAdapter(getActivity(), data);

        Log.i("ADAPTER", adapter.toString());

        recyclerView.setAdapter(adapter);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

// Inflate the layout for this fragment
        return view;
    }

    @Override
    public void customAddList(String result) {

            data.clear();
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
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

}