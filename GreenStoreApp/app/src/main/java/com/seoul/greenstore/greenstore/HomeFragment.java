package com.seoul.greenstore.greenstore;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.seoul.greenstore.greenstore.Recycler.RecyclerAdapter;
import com.seoul.greenstore.greenstore.Recycler.Recycler_item;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.Spinner.Spinners;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X on 2016-09-08.
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment implements Server.ILoadResult,AdapterView.OnItemSelectedListener{

    private RecyclerAdapter adapter;
    private List<Recycler_item> data = new ArrayList<Recycler_item>();

    private View view = null;
    RecyclerView recyclerView = null;

    public static HomeFragment newInstance() {
        HomeFragment homeFrag = new HomeFragment();
        return homeFrag;
    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("home","home");
        view = inflater.inflate(R.layout.activity_home, null);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapter(getActivity(), data);

        Log.i("ADAPTER", adapter.toString());

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //메인 페이지 스피너 등록
        Spinner locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        Spinner typeSpinner1 = (Spinner) view.findViewById(R.id.typeSpinner1);
        Spinner typeSpinner2 = (Spinner) view.findViewById(R.id.typeSpinner2);
        Spinner likeSpinner = (Spinner) view.findViewById(R.id.likeSpinner);

        //구, 업종, 좋아요와 관련된 스피너 등록 시 필요한 파라미터 전송
        Spinners spinner = new Spinners(getActivity(),locationSpinner,typeSpinner1,typeSpinner2,likeSpinner);


        typeSpinner2.setOnItemSelectedListener(this);
        likeSpinner.setOnItemSelectedListener(this);

        return view;
    }

    //업종과 관련된 스피너 선택 시
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.typeSpinner2:
                sortCategory(id);
                break;
            case R.id.likeSpinner:
                sortLike(id);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onStart() {
        super.onStart();
        String[] gets = {"/", "GET"};
        Server server = new Server(getActivity(),this);
        server.execute(gets);
    }

    public void addList(String result) {
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

    @Override
    public void customAddList(String result) {
        addList(result);
    }

    // 선택된 카테고리에 해당하는 정보들만 추출하여 화면에 출력
    public void sortCategory(long id){
        int check = (int)id;
        List<Recycler_item> tempData = new ArrayList<>();

        // 반복문을 돌면서 사용자가 선택한 카테고리(id)와 data에 들어있는 indutyCode가 같은것을 찾아서 tempData에 참조시킴.
        // 만약 선택된 값이 8이면 8이상인 데이터를 모두 기타 서비스업으로 취급함
        if(check==0) {
            tempData = data;
        }
        if(check>8){
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
        adapter = new RecyclerAdapter(getActivity(),tempData);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void sortLike(long id){
        switch ((int)id){
            case 0:

                break;
            case 1:
                break;
        }
    }
}

