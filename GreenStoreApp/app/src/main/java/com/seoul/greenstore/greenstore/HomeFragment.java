package com.seoul.greenstore.greenstore;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.Commons.Constants;
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
public class HomeFragment extends Fragment implements Server.ILoadResult, AdapterView.OnItemSelectedListener, View.OnClickListener{

    private RecyclerAdapter adapter;
    private List<Recycler_item> data = new ArrayList<Recycler_item>();
    private Button searchBtn;
    private Spinner locationSpinner;
    private Spinner typeSpinner1;
    private Spinner typeSpinner2;
    private Spinner likeSpinner;
    private String[] spinnerData = new String[2];

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
        locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        typeSpinner1 = (Spinner) view.findViewById(R.id.typeSpinner1);
        typeSpinner2 = (Spinner) view.findViewById(R.id.typeSpinner2);
        likeSpinner = (Spinner) view.findViewById(R.id.likeSpinner);

        //구, 업종, 좋아요와 관련된 스피너 등록 시 필요한 파라미터 전송
        Spinners spinner = new Spinners(getActivity(),locationSpinner,typeSpinner1,typeSpinner2,likeSpinner);

        typeSpinner1.setOnItemSelectedListener(this);
        typeSpinner2.setOnItemSelectedListener(this);
        likeSpinner.setOnItemSelectedListener(this);
        locationSpinner.setOnItemSelectedListener(this);

        searchBtn = (Button)view.findViewById(R.id.search_main);
        searchBtn.setOnClickListener(this);
        Log.d("home","end");
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
//                sortLike(id);
                break;
            case R.id.typeSpinner1:
                spinnerData[0] = parent.getItemAtPosition(position).toString();
                break;
            case R.id.locationSpinner:
                spinnerData[1] = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.search_main){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            Fragment fragment = new SearchResultFragment();

            Bundle bundle = new Bundle();
            bundle.putStringArray("spinnerData",spinnerData);
            fragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.llContents, fragment);
            fragmentTransaction.addToBackStack("HomeFragment");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onStart() {
        super.onStart();

        if(data.size()==0){
            String[] gets = {Constants.GREEN_STORE_URL_APP, "GET"};
            Server server = new Server(getActivity(),this);
            server.execute(gets);
        }
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

        Log.v("id??",""+check);

        List<Recycler_item> tempData = new ArrayList<>();

        // 반복문을 돌면서 사용자가 선택한 카테고리(id)와 data에 들어있는 indutyCode가 같은것을 찾아서 tempData에 참조시킴.
        // 만약 선택된 값이 8이면 8이상인 데이터를 모두 기타 서비스업으로 취급함
        if(check==0) {
            tempData = data;
        }
//        if(check>8){
//            for (int i = 0; i < data.size(); ++i) {
//                if (data.get(i).getIndutyCode() >= check) {
//                    System.out.println("match id!");
//                    tempData.add(data.get(i));
//                }
//            }
//        }else {
            for (int i = 0; i < data.size(); ++i) {
                if (check == data.get(i).getIndutyCode()) {
                    System.out.println("match id!");
                    tempData.add(data.get(i));
                }
            }
//        }

        if(check!=0 && tempData.size()==0){
            Toast.makeText(getActivity(),"데이터가 없습니다.",Toast.LENGTH_SHORT).show();
        }

        adapter = new RecyclerAdapter(getActivity(),tempData);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}


