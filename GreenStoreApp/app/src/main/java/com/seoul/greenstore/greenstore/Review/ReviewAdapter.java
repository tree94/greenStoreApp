package com.seoul.greenstore.greenstore.Review;

/**
 * Created by X on 2016-09-06.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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
import com.seoul.greenstore.greenstore.Mypage.MyPageFragment_review;
import com.seoul.greenstore.greenstore.R;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.User.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> implements Server.ILoadResult {
    private Context context;

    private Activity acti;
    private LayoutInflater inflater;
    private List<Review_item> items = Collections.emptyList();
    private int currentPos = 0;
    private Review_item current;
    private ViewHolder holder;
    private View v;
    private Button btnSetting;

    //리뷰 좋아요 중복 방지 리스트
    private static List<Integer> likeList = new ArrayList<>(Arrays.asList(0));

    public ReviewAdapter() {
    }

    public ReviewAdapter(Context context, List<Review_item> data) {
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
//            profile = (ImageView) itemView.findViewById(R.id.profileImage_review);
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
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_review, null);

        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Review_item review_item = items.get(position);
        Date from = review_item.getRdate();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String to = transFormat.format(from);

        holder.date_review.setText(to);
        holder.storeName.setText(review_item.getStoreName());
        holder.content_review.setText(review_item.getRcontents());
        holder.like_number.setText(String.valueOf(review_item.getRelike()));
        holder.userId.setText(review_item.getMname());

//        if (review_item.getImage()!=null){
//            Log.v("imageTest",""+review_item.getImage());
//            Picasso.with(context).load(review_item.getImage()).fit().centerInside().into(holder.profile);
//        }

        if (User.user != null) {
            if (User.userReviewLike != null) {
                for (int i = 0; i < User.userReviewLike.size(); ++i) {
                    Log.v("userlike", "" + User.userReviewLike.get(i) + " / " + review_item.getRkey());
                    if (User.userReviewLike.get(i).equals(String.valueOf(review_item.getRkey()))) {
                        holder.like_image.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.heart));
                    }
                }
            }
        }

        if (review_item.getSh_addr() != null) {
            String addr = review_item.getSh_addr();
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

//                Log.d("min", addr);

            }
            //res = res.substring(0, res.length() - 2);   // 맨 뒤 글자 잘라내기
//            System.out.println(res);
        }

//        Picasso.with(context).load(review_item.getImage()).fit().centerInside().into(holder.profile);

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

        holder.like_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.user == null) {
                    Toast.makeText(context.getApplicationContext(), "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.v("likeListTest", "" + likeList.size());
                    if(likeList.contains(review_item.getRkey())){
                        Toast.makeText(acti, "이미 좋아요 하셨습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        //서버로 좋아요 +1
                        String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEWLIKE + "?mkey=" + User.user.get(3) + "&rkey=" + review_item.getRkey(), "GET"};
                        Server server = new Server(acti, ReviewAdapter.this);
                        server.execute(gets);

                        if (User.userReviewLike.size() > 0)
                            User.userReviewLike.put(User.userReviewLike.size(), String.valueOf(review_item.getRkey()));
                        else
                            User.userReviewLike.put(0, String.valueOf(review_item.getRkey()));

                        //내가 좋아요한 스토어에 데이터 저장.
                        MyPageFragment_review myPageFragment_review = new MyPageFragment_review();
                        myPageFragment_review.setMyReviewLikeData(review_item.getMname(), review_item.getDateTime(), review_item.getRelike(), review_item.getRcontents());

                        holder.like_image.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.heart));
                        review_item.setRelike(review_item.getRelike()+1);
                        holder.like_number.setText(String.valueOf(review_item.getRelike()));
                        likeList.add(review_item.getRkey());
                    }
                }
            }
        });

    }

    private void ShowDialog(final Review_item review_item) {

        LayoutInflater dialog = LayoutInflater.from(context);
        final View dialogLayout = dialog.inflate(R.layout.review_custom_dialog, null);
        final Dialog myDialog = new Dialog(context);


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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

                if(User.user!=null && review_item.getMkey()==Integer.parseInt(User.user.get(3))) {
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
                }else{
                    Toast.makeText(context, "자신의 리뷰만 수정할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User.user != null && Integer.parseInt(User.user.get(3))==review_item.getMkey()) {
                    String[] gets = {Constants.GREEN_STORE_URL_APP_REVIEW_DELETE + "?rkey=" + review_item.getRkey(), "GET"};
                    Log.d("review_item", review_item.getRkey() + "");
                    Server server = new Server(acti, ReviewAdapter.this);
                    server.execute(gets);
                    Toast.makeText(acti, "리뷰가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    myDialog.cancel();
                }else{
                    Toast.makeText(context, "자신의 리뷰만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
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
