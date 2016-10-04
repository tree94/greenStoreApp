package com.seoul.greenstore.greenstore.Review;

/**
 * Created by X on 2016-09-06.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.seoul.greenstore.greenstore.R;

import java.util.Collections;
import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private TextView storeName;
    private List<Review_item> items = Collections.emptyList();
    private int currentPos = 0;
    private Review_item current;
    private ViewHolder holder;
    private View v;

    public ReviewAdapter() {
    }

    public ReviewAdapter(Context context, List<Review_item> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.items = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_review, null);
        holder = new ViewHolder(v);

        holder.store = (TextView) v.findViewById(R.id.storeName_review);
        holder.profile = (ImageView) v.findViewById(R.id.profileImage_review);
        holder.userId = (TextView) v.findViewById(R.id.userId_review);
        holder.date_review = (TextView) v.findViewById(R.id.date_review);
        holder.like_image = (ImageButton) v.findViewById(R.id.like_image);
        holder.like_number = (TextView) v.findViewById(R.id.like_number);
        holder.content_review = (TextView) v.findViewById(R.id.content_review);
        holder.reviewImage1 = (ImageView) v.findViewById(R.id.reviewImage1);
        holder.reviewImage2 = (ImageView) v.findViewById(R.id.reviewImage2);
        holder.reviewImage3 = (ImageView) v.findViewById(R.id.reviewImage3);


        holder.cardview_review = (CardView) v.findViewById(R.id.cardview_review);
        v.setTag(holder);
        Log.e("tes13", "112");
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder = (ViewHolder) v.getTag();
        Review_item item = items.get(position);
//        Log.e("storename", store.getName());
//        holder.name.setText(Html.fromHtml(store.getName()));
//        holder.addr.setText(Html.fromHtml(store.getAddr()));
//        System.out.println(store.getImage() + " image~~");
//        Picasso.with(context).load(store.getImage()).fit().centerInside().into(holder.imageView);
//        final Recycler_item item = items.get(position);
//        Recycler_item current = items.get(position);
//        holder.name.setText(current.getName());
//        holder.addr.setText(current.getAddr());
//        holder.imageURL = current.getImage();

//        Picasso.with(context).load(current.getImage()).into(holder.imageView);


//        new DownloadAsyncTask().execute(holder);

//        holder.cardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, item.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


/*
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }*/

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    //    extends RecyclerView.ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profile;
        private TextView userId;
        private TextView date_review;
        private ImageButton like_image;
        private TextView like_number;
        private TextView content_review;
        private ImageView reviewImage1;
        private ImageView reviewImage2;
        private ImageView reviewImage3;
        private CardView cardview_review;
        private TextView store;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
