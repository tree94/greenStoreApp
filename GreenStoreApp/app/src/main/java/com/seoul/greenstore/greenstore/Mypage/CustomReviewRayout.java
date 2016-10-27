package com.seoul.greenstore.greenstore.Mypage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seoul.greenstore.greenstore.R;

/**
 * Created by user on 2016-10-22.
 */
public class CustomReviewRayout extends LinearLayout {
    ImageView reviewPhoto;
    TextView reviewName;
    TextView reviewDate;
    TextView reviewCount;
    TextView reviewContent;
    TextView reviewStoreName;

    public CustomReviewRayout(Context context) {
        super(context);
        initView();

    }

    public CustomReviewRayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CustomReviewRayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);
    }


    private void initView() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.review_item, this, false);
        addView(v);

        reviewPhoto = (ImageView) findViewById(R.id.reviewPhoto);
        reviewStoreName = (TextView) findViewById(R.id.reviewStoreName);
        reviewName = (TextView) findViewById(R.id.reviewName);
        reviewDate = (TextView) findViewById(R.id.reviewDate);
        reviewCount = (TextView) findViewById(R.id.reviewCount);
        reviewContent = (TextView) findViewById(R.id.reviewContent);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.reviewItem);

        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.reviewItem, defStyle, 0);
        setTypeArray(typedArray);

    }


    private void setTypeArray(TypedArray typedArray) {
//        int bg_resID = typedArray.getResourceId(R.styleable.LoginButton_bg, R.drawable.login_naver_bg);
//        bg.setBackgroundResource(bg_resID);

//        int symbol_resID = typedArray.getResourceId(R.styleable.LoginButton_symbol, R.drawable.login_naver_symbol);
//        symbol.setImageResource(symbol_resID);

//        int textColor = typedArray.getColor(R.styleable.LoginButton_textColor, 0);
//        text.setTextColor(textColor);

        reviewStoreName.setText(typedArray.getString(R.styleable.reviewItem_reviewStoreName));
        reviewName.setText(typedArray.getString(R.styleable.reviewItem_reviewName));
        reviewDate.setText(typedArray.getString(R.styleable.reviewItem_reviewDate));
        reviewCount.setText(typedArray.getString(R.styleable.reviewItem_reviewCount));
        reviewContent.setText(typedArray.getString(R.styleable.reviewItem_reviewContent));
        typedArray.recycle();

    }


//    void setBg(int bg_resID) {
//
//        bg.setBackgroundResource(bg_resID);
//    }
//
//    void setSymbol(int symbol_resID) {
//        symbol.setImageResource(symbol_resID);
//    }
//
//    void setTextColor(int color) {
//
//        text.setTextColor(color);
//    }

//    void setText(String text_string) {
//        reviewName.setText(text_string);
//    }
//
//    void setText(int text_resID) {
//        reviewName.setText(text_resID);
//    }

}
