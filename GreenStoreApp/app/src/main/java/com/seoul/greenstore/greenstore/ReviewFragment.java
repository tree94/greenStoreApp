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
import android.widget.TextView;
import android.widget.Toast;

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
    private String[] spinnerData = new String[2];

    //review item
    private TextView storeName_review;
    private TextView userId_review;
    private TextView date_review;
    private TextView like_number;
    private TextView content_review;


    private int sh_id;
    private String sh_addr;
    private String sh_name;

    Bundle bundle = null;


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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);

        bundle = this.getArguments();
        if (bundle != null) {
            sh_id = bundle.getInt("sh_id");
            sh_addr = bundle.getString("sh_addr");
            sh_name = bundle.getString("sh_name");
        }

        sortCate = (Button) view.findViewById(R.id.sortCate);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerReview);
        Spinner locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner_r);
        Spinner typeSpinner1 = (Spinner) view.findViewById(R.id.typeSpinner1_r);
        Spinners spinner = new Spinners(getActivity(), locationSpinner, typeSpinner1);

        adapter = new ReviewAdapter(getActivity(), data);
        recyclerView.setHasFixedSize(true);
        Log.i("ADAPTER", adapter.toString());


        locationSpinner.setOnItemSelectedListener(this);
        typeSpinner1.setOnItemSelectedListener(this);


        //리뷰를 카테고리별로
        sortCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortData(spinnerData);
            }

        });


        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private void sortData(String[] spinnerData) {
        Log.v("spinnerTest",""+spinnerData[0] + " / "+spinnerData[1]);
        List<Review_item> tempData = new ArrayList<>();
        if (spinnerData[0].equals("지역 선택") && spinnerData[1].equals("업종 선택")) {
            tempData = data;
        } else {
            if (spinnerData[0].equals("지역 선택") && !spinnerData[1].equals("업종 선택")) {
                for (Review_item item : data) {
                    if (spinnerData[1].equals(item.getInduty_name()))
                        tempData.add(item);
                }
            } else if (spinnerData[1].equals("업종 선택") && !spinnerData[0].equals("지역 선택")) {
                for (Review_item item : data) {
                    if (formatAddr(item.getSh_addr()).equals(spinnerData[0]+" "))
                        tempData.add(item);
                }
            } else {
                for (Review_item item : data) {
                    if (formatAddr(item.getSh_addr()).equals(spinnerData[0]) && item.getInduty_name().equals(spinnerData[1]))
                        tempData.add(item);
                }
            }
        }
        adapter = new ReviewAdapter(getActivity(), tempData);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private String formatAddr(String addr) {
        String res = "";
        boolean flag = false;
        for (int i = 0; i < addr.length(); i++) {
            char c = addr.charAt(i);
            if (flag == true) {
                res += c;   //String으로 변환필요하면 해주기
            }
            if (flag == true && c == ' ') {
                break;
            }
            if (c == ' ') {
                flag = true;
            }
        }
        return res;
    }


    @Override
    public void customAddList(String result) {
        data.clear();
        try {
            JSONArray jsonArray = new JSONArray(result);
            if (result == null)
                Toast.makeText(getActivity(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < jsonArray.length(); i++) {
                Review_item review = new Review_item();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                review.setStoreName(jsonObject.getString("sh_name"));
                review.setMname(jsonObject.getString("mname"));
                review.setRkey(Integer.parseInt(jsonObject.getString("rkey")));
                review.setMkey(Integer.parseInt(jsonObject.getString("mkey")));
                review.setRcontents(jsonObject.getString("rcontent"));
                review.setRelike(Integer.parseInt(jsonObject.getString("relike")));
                review.setSh_addr(jsonObject.getString("sh_addr"));
                review.setInduty(Integer.parseInt(jsonObject.getString("induty_CODE_SE")));
                review.setInduty_name(jsonObject.getString("induty_CODE_SE_NAME"));
                review.setImage(jsonObject.getString("mphoto"));

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