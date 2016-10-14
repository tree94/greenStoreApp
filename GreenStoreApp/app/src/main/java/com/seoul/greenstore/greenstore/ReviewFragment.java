package com.seoul.greenstore.greenstore;

import android.os.Bundle;
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

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.Review.ReviewAdapter;
import com.seoul.greenstore.greenstore.Review.Review_item;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.Spinner.Spinners;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private Button btnWrite; // 글쓰기 버튼을 일단 만들어놓음.
    private String[] spinnerData = new String[2];
    private int flag = 0;


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

        String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_ALL, "GET"};

        Server server = new Server(getActivity(), this);
        server.execute(gets);
        Log.d("coffee", "IN");

//        String[] gets = {Constants.GREEN_STORE_URL_APP_SEARCH + encodeStr, "GET"};
//        Log.d("hot6", "URL" + Constants.GREEN_STORE_URL_APP_SEARCH + encodeStr);
//        Server server = new Server(getActivity(), this);
//        server.execute(gets);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_review, null);
        View view2 = inflater.inflate(R.layout.cardview_review, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);


        sortCate = (Button) view.findViewById(R.id.sortCate);
//        like_image = (ImageButton) view.findViewById(R.id.like_image);
//        cardview = (CardView) view.findViewById(R.id.cardview_review);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerReview);
        Spinner locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner_r);
        Spinner typeSpinner1 = (Spinner) view.findViewById(R.id.typeSpinner1_r);
        Spinners spinner = new Spinners(getActivity(), locationSpinner, typeSpinner1);
//        storeName_review = (TextView) view.findViewById(R.id.storeName_review);
        btnWrite = (Button) view.findViewById(R.id.review_write);
//        review_setting = (Button) view.findViewById(R.id.review_setting);

        adapter = new ReviewAdapter(getActivity(), data);
        recyclerView.setHasFixedSize(true);
        Log.i("ADAPTER", adapter.toString());

        typeSpinner1.setOnItemSelectedListener(this);
        locationSpinner.setOnItemSelectedListener(this);

/*       like_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_LIKE, "GET", "reviewDelete", "5"};
                Server server = new Server(getActivity(), this);
                server.execute(gets);*//*
                Toast.makeText(getActivity(), "하트 이미지를 누름", Toast.LENGTH_SHORT).show();

            }
        });*/

        //글쓰기 버튼. 상세 페이지로 갈 것임
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Activty 의 메소드 호출
                MainActivity.changeFragment("fragment1");
            }
        });

        sortCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCate(spinnerData);
            }
        });


        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
// Inflate the layout for this fragment
        return view;


    }

    private void sortCate(String data[]) {


    }


    @Override
    public void customAddList(String result) {
        data.clear();
        Log.d("coffee", "Review customAddList IN");
        try {
            JSONArray jsonArray = new JSONArray(result);
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

            Log.d("coffee", "Review notifyDataSetChanged IN");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.locationSpinner_r:
                spinnerData[0] = parent.getItemAtPosition(position).toString();
                if (spinnerData[0].equals("지역 선택")) ;
                spinnerData[0] = "전체";
                break;
            case R.id.typeSpinner1_r:
                spinnerData[1] = parent.getItemAtPosition(position).toString();
                if (spinnerData[1].equals("업종 선택"))
                    spinnerData[1] = "전체";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}