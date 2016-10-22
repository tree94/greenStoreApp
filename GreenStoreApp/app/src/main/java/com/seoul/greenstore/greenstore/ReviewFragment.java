package com.seoul.greenstore.greenstore;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.seoul.greenstore.greenstore.Review.ReviewAdapter;
import com.seoul.greenstore.greenstore.Review.Review_item;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.Spinner.Spinners;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by X on 2016-09-08.
 */
public class ReviewFragment extends Fragment implements Server.ILoadResult, AdapterView.OnItemSelectedListener {
    private ReviewAdapter adapter;
    private View view;
    private List<Review_item> data = new ArrayList<Review_item>();
    private RecyclerView recyclerView = null;
    private Button sortCate;
    private String[] spinnerData = new String[2];


    private int sh_id;
    private String sh_addr;
    private String sh_name;

    Bundle bundle = null;

    String loc = "";
    String type = "";

    public static ReviewFragment newInstance() {
        ReviewFragment fragment = new ReviewFragment();
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
        Log.d("coffee", "Review onstart IN");

        if (bundle == null) {
            String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_ALL, "GET"};
            Server server = new Server(getActivity(), this);
            server.execute(gets);
        } else {
            String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_ONE_STORE + sh_id, "GET"};
            Server server = new Server(getActivity(), this);
            server.execute(gets);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_review, null);
        View view2 = inflater.inflate(R.layout.cardview_review, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);

        bundle = this.getArguments();
        if(bundle!=null) {
            sh_id = bundle.getInt("sh_id");
            sh_addr = bundle.getString("sh_addr");
            sh_name = bundle.getString("sh_name");
        }

        sortCate = (Button) view.findViewById(R.id.sortCate);
//        like_image = (ImageButton) view.findViewById(R.id.like_image);
//        cardview = (CardView) view.findViewById(R.id.cardview_review);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerReview);
        Spinner locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner_r);
        Spinner typeSpinner1 = (Spinner) view.findViewById(R.id.typeSpinner1_r);
        Spinners spinner = new Spinners(getActivity(), locationSpinner, typeSpinner1);
//        storeName_review = (TextView) view.findViewById(R.id.storeName_review);

//        review_setting = (Button) view.findViewById(R.id.review_setting);

        adapter = new ReviewAdapter(getActivity(), data);
        recyclerView.setHasFixedSize(true);
        Log.i("ADAPTER", adapter.toString());


        locationSpinner.setOnItemSelectedListener(this);
        typeSpinner1.setOnItemSelectedListener(this);


        //리뷰를 카테고리별로
        sortCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCate(spinnerData);
            }

        });


        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerView.smoothScrollToPosition(0);
                Toast.makeText(getActivity(), "FAB 누름", Toast.LENGTH_SHORT).show();
            }
        });

// Inflate the layout for this fragment
        return view;
    }

    private void sortCate(String[] spinnerData) {

        try {
            loc = URLEncoder.encode(spinnerData[0], "UTF-8");
            type = URLEncoder.encode(spinnerData[1], "UTF-8");
            if (loc.equals("지역 선택"))
                loc = "전체";
            if (type.equals("업종 선택"))
                type = "전체";
            Log.d("hot8", loc + "/" + type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 카테고리 검색
        String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_CATESEARCH + loc + "/" + type, "GET"};
        Log.d("hot6", "URL" + Constants.GREEN_STORE_URL_APP_REVIEW_CATESEARCH + loc + "/" + type);
        Server server = new Server(getActivity(), this);
        server.execute(gets);


    }


    @Override
    public void customAddList(String result) {
        data.clear();
        Log.d("coffee", "Review customAddList IN");
        try {
            JSONArray jsonArray = new JSONArray(result);
            if(result==null)
                Toast.makeText(getActivity(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < jsonArray.length(); i++) {
                Review_item review = new Review_item();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                review.setStoreName(jsonObject.getString("sh_name"));
                review.setRkey(Integer.parseInt(jsonObject.getString("rkey")));
                review.setMkey(Integer.parseInt(jsonObject.getString("mkey")));
//                review.setSh_id(Integer.parseInt(jsonObject.getString("sh_id")));
                review.setRcontents(jsonObject.getString("rcontent"));
                review.setRelike(Integer.parseInt(jsonObject.getString("relike")));
                review.setSh_addr(jsonObject.getString("sh_addr"));
                review.setInduty(Integer.parseInt(jsonObject.getString("induty_CODE_SE")));
                review.setInduty_name(jsonObject.getString("induty_CODE_SE_NAME"));

                //String으로 받는 ndate를 Date로 바꾼 후 실행
                Date from = new Date(Long.valueOf(jsonObject.getString("rdate")));
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String temp = transFormat.format(from);
                Date to = transFormat.parse(temp);
                review.setRdate(to);
                Log.e("dateformat", "" + transFormat.format(from));


                data.add(review);
            }

            adapter.notifyDataSetChanged();

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
            layoutManager.scrollToPositionWithOffset(0, 0);
            recyclerView.scrollToPosition(0);

            Log.d("hot8", "Review notifyDataSetChanged IN");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.locationSpinner_r:
                spinnerData[0] = parent.getItemAtPosition(position).toString();
                break;
            case R.id.typeSpinner1_r:
                spinnerData[1] = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}