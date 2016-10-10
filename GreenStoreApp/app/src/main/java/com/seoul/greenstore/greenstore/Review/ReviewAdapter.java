package com.seoul.greenstore.greenstore.Review;

/**
 * Created by X on 2016-09-06.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.seoul.greenstore.greenstore.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
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
        this.items = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        private TextView storeName;

        public ViewHolder(View itemView) {
            super(itemView);
            storeName = (TextView) itemView.findViewById(R.id.storeName_review);
            profile = (ImageView) itemView.findViewById(R.id.profileImage_review);
            userId = (TextView) itemView.findViewById(R.id.userId_review);
            date_review = (TextView) itemView.findViewById(R.id.date_review);
            like_image = (ImageButton) itemView.findViewById(R.id.like_image);
            like_number = (TextView) itemView.findViewById(R.id.like_number);
            content_review = (TextView) itemView.findViewById(R.id.content_review);
            reviewImage1 = (ImageView) itemView.findViewById(R.id.reviewImage1);
            reviewImage2 = (ImageView) itemView.findViewById(R.id.reviewImage2);
            reviewImage3 = (ImageView) itemView.findViewById(R.id.reviewImage3);
        }

    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_review,null);

   /*
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

*/
//        holder.cardview_review = (CardView) v.findViewById(R.id.cardview_review);
//        v.setTag(holder);
//        Log.e("tes13", "112");*/
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review_item review_item = items.get(position);
//        holder = (ViewHolder) v.getTag();
//        Recycler_item store = items.get(position);
//        Log.e("storename",store.getName());
//        holder.userId.setText(review_item.get());

        Date from = review_item.getRdate();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String to = transFormat.format(from);

        holder.date_review.setText(to);
        holder.storeName.setText(review_item.getStoreName());
        holder.content_review.setText(review_item.getRcontents());
        Picasso.with(context).load(review_item.getImage()).fit().centerInside().into(holder.profile);
    }


    @Override
    public int getItemCount() {
        return this.items.size();
    }

}
