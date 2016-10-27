package com.seoul.greenstore.greenstore.Mypage;

/**
 * Created by X on 2016-09-06.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.R;
import com.seoul.greenstore.greenstore.Review.ReviewUpdateFragment;
import com.seoul.greenstore.greenstore.Review.Review_item;
import com.seoul.greenstore.greenstore.Server.Server;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ViewHolder> implements Server.ILoadResult {
    private Context context;

    private Activity acti;
    private LayoutInflater inflater;
    private List<Review_item> items = Collections.emptyList();
    private int currentPos = 0;
    private Review_item current;
    private ViewHolder holder;
    private View v;
    private Button btnSetting;

    public MyReviewAdapter() {
    }

    public MyReviewAdapter(Context context, List<Review_item> data) {
        this.context = context;
        this.acti = (Activity) context;
        this.items = data;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profile;
        private TextView userId;
        private TextView date_review;
        private Button review_setting;
        private ImageButton like_image;
        private TextView like_number;
        private TextView content_review;
        private ImageView reviewImage1;
        private ImageView reviewImage2;
        private ImageView reviewImage3;
        private CardView cardview_review;
        private TextView storeName;
        private Button btnSetting;
        private String gu;

        public ViewHolder(View itemView) {
            super(itemView);

            btnSetting = (Button) itemView.findViewById(R.id.review_setting);
            storeName = (TextView) itemView.findViewById(R.id.storeName_review);
            profile = (ImageView) itemView.findViewById(R.id.profileImage_review);
            userId = (TextView) itemView.findViewById(R.id.userId_review);
            date_review = (TextView) itemView.findViewById(R.id.date_review);
            review_setting = (Button) itemView.findViewById(R.id.review_setting);
            like_image = (ImageButton) itemView.findViewById(R.id.like_image);
            like_number = (TextView) itemView.findViewById(R.id.like_number);
            content_review = (TextView) itemView.findViewById(R.id.content_review);
            reviewImage1 = (ImageView) itemView.findViewById(R.id.reviewImage1);
            reviewImage2 = (ImageView) itemView.findViewById(R.id.reviewImage2);
            reviewImage3 = (ImageView) itemView.findViewById(R.id.reviewImage3);
        }

    }

    @Override
    public MyReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_review, null);

        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review_item review_item = items.get(position);
        Date from = review_item.getRdate();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String to = transFormat.format(from);

        holder.date_review.setText(to);
        holder.storeName.setText(review_item.getStoreName());
        holder.content_review.setText(review_item.getRcontents());
        holder.like_number.setText(String.valueOf(review_item.getRelike()));

        if(review_item.getSh_addr()!=null) {
            String addr = review_item.getSh_addr();
            String res = "";
            boolean flag = false;
            for (int i=0; i < addr.length(); i++) {
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

                Log.d("min",addr);

            }
            //res = res.substring(0, res.length() - 2);   // 맨 뒤 글자 잘라내기
            System.out.println(res);
        }

        Picasso.with(context).load(review_item.getImage()).fit().centerInside().into(holder.profile);

        holder.review_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(review_item);
            }
        });
/*
        holder.like_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_LIKE, "POST","reviewDelete", "5"};
                Server server = new Server(acti, this);
                server.execute(gets);
            }
        });
        */


    }

    private void ShowDialog(final Review_item review_item) {

        LayoutInflater dialog = LayoutInflater.from(context);
        final View dialogLayout = dialog.inflate(R.layout.review_custom_dialog, null);
        final Dialog myDialog = new Dialog(context);


        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        Button btn_update = (Button) dialogLayout.findViewById(R.id.review_update);
        Button btn_delete = (Button) dialogLayout.findViewById(R.id.review_delete);
        final Button btn_nothing = (Button) dialogLayout.findViewById(R.id.review_nothing);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "업데이트를 클릭함", Toast.LENGTH_SHORT).show();


                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                Fragment fragment = new ReviewUpdateFragment();


                Bundle bundle = new Bundle();
                bundle.putString("review_Rcontents", review_item.getRcontents().toString());
                bundle.putString("review_Rkey", String.valueOf(review_item.getRkey()));
                bundle.putString("review_StoreName", review_item.getStoreName().toString());


                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.llContents, fragment);
                fragmentTransaction.addToBackStack(fm.findFragmentById(R.id.llContents).toString());
                fragmentTransaction.commit();
                myDialog.cancel();

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_DELETE + "?rkey=" + review_item.getRkey(), "GET"};
                Log.d("review_item", review_item.getRkey() + "");
                Server server = new Server(acti, MyReviewAdapter.this);
                server.execute(gets);
                Toast.makeText(acti, "리뷰가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }
        });

        btn_nothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });


    }
/*
    private String getAddr(String addr) {
        String input = addr;



        return res;
    }*/

    @Override
    public void customAddList(String result) {

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

}
