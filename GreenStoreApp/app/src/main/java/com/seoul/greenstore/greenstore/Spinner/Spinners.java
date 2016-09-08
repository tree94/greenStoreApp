package com.seoul.greenstore.greenstore.Spinner;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 2016-09-08.
 */
public class Spinners{

    private Context context;
    private Spinner locationSpinner;
    private Spinner typeSpinner1;
    private Spinner typeSpinner2;
    private Spinner likeSpinner;

    public Spinners(Context context,Spinner locationSpinner,Spinner typeSpinner1,Spinner typeSpinner2,Spinner likeSpinner) {
        this.context = context;
        this.locationSpinner = locationSpinner;
        this.typeSpinner1 = typeSpinner1;
        this.typeSpinner2 = typeSpinner2;
        this.likeSpinner = likeSpinner;

        //구와 관련된 스피너 등록
        setLocationSpinner();
        //업종과 관련된 스피너 등록
        setTypeSpinner();
        //좋아요와 관련되 스피너 등록
        setLikeSpinner();
    }

    //지역구 스피너 등록 메소드
    public void setLocationSpinner(){
        List<String> locList = Arrays.asList("강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구","도봉구","동대문구","동작구",
                "마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구","중구","중랑구");

        ArrayAdapter<String> locationSpinnerApater = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, locList);
        locationSpinnerApater.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        locationSpinner.setAdapter(locationSpinnerApater);
    }

    //업종 스피너 등록 메소드
    public void setTypeSpinner(){
        List<String> secList = Arrays.asList("한식","중식","경양식,일식","기타 외식업","이,미용업","목욕업","세탁업","숙박업","기타서비스업종");

        ArrayAdapter<String> typeSpinner1Adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, secList);
        typeSpinner1Adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typeSpinner1.setAdapter(typeSpinner1Adapter);
        typeSpinner2.setAdapter(typeSpinner1Adapter); //스피너 Spinner 1,2 내용 똑같아서 그냥 같이붙임
    }

    //좋아요 스피너 등록
    public void setLikeSpinner(){
        List<String> descList = Arrays.asList("서울시 추천 순","사용자 추천 순");

        ArrayAdapter<String> likeSpinnerAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, descList);
        likeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        likeSpinner.setAdapter(likeSpinnerAdapter);
    }



}
