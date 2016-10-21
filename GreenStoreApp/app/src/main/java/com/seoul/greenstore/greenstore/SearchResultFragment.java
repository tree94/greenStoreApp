package com.seoul.greenstore.greenstore;

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
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.Recycler.RecyclerAdapter;
import com.seoul.greenstore.greenstore.Recycler.Recycler_item;
import com.seoul.greenstore.greenstore.Server.Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by X on 2016-09-08.
 */
public class SearchResultFragment extends Fragment implements Server.ILoadResult{
    private RecyclerAdapter adapter;
    private View view;
    private List<Recycler_item> data = new ArrayList<Recycler_item>();
    private RecyclerView recyclerView = null;
    private TextView textView;
    private String[] searchList = new String[2];
    private String encodeStr;


    public static SearchResultFragment newInstance(){
        SearchResultFragment fragment = new SearchResultFragment();
        return fragment;
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
        if(data.size() == 0) {
            //서버에 보낼 query를 UTF-8로 인코딩
            if (MainActivity.strCommon != null) {

                //내용 검색
                try {
                    encodeStr = URLEncoder.encode(MainActivity.strCommon, "UTF-8");
                    MainActivity.strCommon = null;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String[] gets = {Constants.GREEN_STORE_URL_APP_SEARCH + encodeStr, "GET"};
                Log.d("hot6", "URL" + Constants.GREEN_STORE_URL_APP_SEARCH + encodeStr);
                Server server = new Server(getActivity(), this);
                server.execute(gets);
            } else {
                //내용 검색
                String loc = "";
                String type = "";
                try {
                    type = URLEncoder.encode(searchList[0], "UTF-8");
                    loc = URLEncoder.encode(searchList[1], "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // 카테고리 검색
                String[] gets = {Constants.GREEN_STORE_URL_APP_CATESEARCH + loc + "/" + type, "GET"};
                Log.d("hot6", "URL" + Constants.GREEN_STORE_URL_APP_CATESEARCH + loc + "/" + type);
                Server server = new Server(getActivity(), this);
                server.execute(gets);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, null);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSearch);
        textView = (TextView) view.findViewById(R.id.searchTextView);
        if(MainActivity.strCommon!=null)
            textView.setText("'"+MainActivity.strCommon+"'"+" 로 검색한 결과입니다.");
        else{
            Bundle bundle = this.getArguments();

            if(bundle != null) {
                searchList = bundle.getStringArray("spinnerData");
                if (searchList[1].equals("지역 선택"))
                    searchList[1] = "전체";
                if (searchList[0].equals("업종 선택"))
                    searchList[0] = "전체";
                textView.setText("'" + searchList[1] + "' 지역의 '" + searchList[0] + "' 카테고리로 검색한 결과입니다.");
            }else {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                Fragment fragment = new HomeFragment();
                fragmentTransaction.replace(R.id.llContents, fragment);
                fragmentTransaction.commit();
            }
        }

        adapter = new RecyclerAdapter(data,getActivity(), recyclerView);

        Log.i("ADAPTER", adapter.toString());

        recyclerView.setAdapter(adapter);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
// Inflate the layout for this fragment
        return view;
    }

    public void btnSearch(int location, int type){

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
                if(data.size()==0){
                    Toast.makeText(getContext(),"겸색 결과가 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

}