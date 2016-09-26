package com.seoul.greenstore.greenstore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.seoul.greenstore.greenstore.Review.ReviewAdapter;
import com.seoul.greenstore.greenstore.Review.Review_item;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.Spinner.Spinners;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X on 2016-09-08.
 */
public class ReviewFragment extends Fragment implements Server.ILoadResult{
    private ReviewAdapter adapter;
    private View view;
    private List<Review_item> data = new ArrayList<Review_item>();
    private RecyclerView recyclerView = null;
    private TextView textView;


    public static ReviewFragment newInstance(){
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerReview);
        Spinner locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        Spinner typeSpinner1 = (Spinner) view.findViewById(R.id.typeSpinner1);
        Spinners spinner = new Spinners(getActivity(),locationSpinner,typeSpinner1);

        adapter = new ReviewAdapter(getActivity(), data);

        Log.i("ADAPTER", adapter.toString());

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
// Inflate the layout for this fragment
        return view;
    }

    @Override
    public void customAddList(String result) {
       /* data.clear();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                Review_item review = new Review_item();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                review.setRkey(Integer.parseInt(jsonObject.getString("rkey")));
                review.setMkey(Integer.parseInt(jsonObject.getString("mkey")));
                review.setSh_id(Integer.parseInt(jsonObject.getString("sh_id")));
                review.setRcontents(jsonObject.getString("rcontents"));
                review.setRelike(Integer.parseInt(jsonObject.getString("relike")));

                String from = jsonObject.getString("rdate");
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date to = transFormat.parse(from);
                review.setRdate(to);


                data.add(review);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}