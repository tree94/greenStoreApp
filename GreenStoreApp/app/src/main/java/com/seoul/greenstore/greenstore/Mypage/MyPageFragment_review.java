package com.seoul.greenstore.greenstore.Mypage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.R;
import com.seoul.greenstore.greenstore.Review.Review_item;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.User.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by X on 2016-09-30.
 */
public class MyPageFragment_review extends Fragment implements Server.ILoadResult {
    private RecyclerView recyclerView = null;
    private static MyReviewAdapter reviewAdapter;
    private static List<Review_item> data = new ArrayList<>();
    private FloatingActionButton fab;
    private View view;

    public void setMyReviewLikeData(String mname,String rdate,int relike,String rcontent){
        Review_item recycler_item = new Review_item();
        recycler_item.setMname(mname);
        recycler_item.setDateTime(rdate);
        recycler_item.setRelike(relike);
        recycler_item.setRcontents(rcontent);
        data.add(recycler_item);
        if(reviewAdapter!=null) reviewAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mypage_review, null);
        getlikeItem();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        recyclerView = (RecyclerView) view.findViewById(R.id.myRecyclerReview);
        reviewAdapter = new MyReviewAdapter(getActivity(), data);
        recyclerView.setAdapter(reviewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
//        fab = (FloatingActionButton) view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                recyclerView.smoothScrollToPosition(0);
//                Toast.makeText(getActivity(), "FAB 누름", Toast.LENGTH_SHORT).show();
//            }
//        });
//        getlikeItem();
        return view;
    }

    @Override
    public void customAddList(String result) {
        data.clear();
        DateFormat format1 = DateFormat.getDateInstance(DateFormat.FULL);
        Log.d("coffee", "// " + result);
        try {
            JSONArray jsonArray = new JSONArray(result);
            if (result == null) {
                Toast.makeText(getActivity(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
            }
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
//                Date from = new Date(Long.valueOf(jsonObject.getString("rdate")));
//                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String temp = transFormat.format(from);
//                Date to = transFormat.parse(temp);

                review.setDateTime(String.valueOf(format1.format(new Date(Long.valueOf(jsonObject.getString("rdate"))))));
//                Log.e("dateformat", "" + transFormat.format(from));


                data.add(review);
            }

            reviewAdapter.notifyDataSetChanged();

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
            layoutManager.scrollToPositionWithOffset(0, 0);
            recyclerView.scrollToPosition(0);

            Log.d("hot8", "Review notifyDataSetChanged IN");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getlikeItem(){
        String[] gets = {Constants.GREEN_STORE_URL_APP_MYREVIEWlIKE + "/" + User.user.get(3), "GET"};
        Server server = new Server(getActivity(), this);
        server.execute(gets);
    }
}

//    private void settingDetailItem() {
//        for (Review_item item : data) {
//            ImageView reviewPhoto = (ImageView) view.findViewById(R.id.reviewPhoto);
//            TextView reviewName = (TextView) view.findViewById(R.id.reviewName);
//            TextView reviewDate = (TextView) view.findViewById(R.id.reviewDate);
//            TextView reviewCount = (TextView) view.findViewById(R.id.reviewCount);
//            TextView reviewContent = (TextView) view.findViewById(R.id.reviewContent);
//
//            if (!item.getReviewPhoto().equals(""))
//                Picasso.with(view.getContext()).load(item.getReviewPhoto()).resize(100, 200).into(reviewPhoto);
//
//            reviewName.setText(item.getReviewName());
//            DateFormat format1 = DateFormat.getDateInstance(DateFormat.FULL);
//            reviewDate.setText(String.valueOf(format1.format(item.getReviewTime())));
//            reviewCount.setText(String.valueOf(item.getReviewCount()));
//            reviewContent.setText(item.getReviewContent());
//        }
//    }




