package com.seoul.greenstore.greenstore.Commons;

/**
 * Created by user on 2016-09-10.
 */
public class Constants {

//    public static final String BASIC_URL = "http://172.30.49.177:8080/";

//    public static final String BASIC_URL = "http://192.168.43.181:8080/";
//    public static final String BASIC_URL = "http://172.30.40.131:8080/";

//
    public static final String BASIC_URL = "http://192.168.100.253:8080/";
    public static final String GREEN_STORE_URL_BASIC = BASIC_URL;
    public static final String GREEN_STORE_URL_APP = GREEN_STORE_URL_BASIC + "app/";

    //detail
    public static final String GREEN_STORE_URL_APP_DETAIL = GREEN_STORE_URL_APP + "detail/";

    //search
    public static final String GREEN_STORE_URL_APP_SEARCH = GREEN_STORE_URL_APP + "search/";
    public static final String GREEN_STORE_URL_APP_CATESEARCH = GREEN_STORE_URL_APP + "cateSearch/";

    //meber
    public static final String GREEN_STORE_URL_APP_MEBERLOOKUP = GREEN_STORE_URL_APP + "memberLookup/";

    //notice
    public static final String GREEN_STORE_URL_APP_NOTICE = GREEN_STORE_URL_APP + "notice/list";

    //storeLike
    public static final  String GREEN_STORE_URL_APP_STORELIKE = GREEN_STORE_URL_APP + "storeLike/";

    //myStoreLike
    public static final  String GREEN_STORE_URL_APP_MYSTORElIKE = GREEN_STORE_URL_APP + "myStoreLike/";

    private static final String GREEN_STORE_URL_APP_REVIEW = GREEN_STORE_URL_APP+"review/";
    public static final String GREEN_STORE_URL_APP_REVIEW_ONE = GREEN_STORE_URL_APP_REVIEW + "one";
    public static final String GREEN_STORE_URL_APP_REVIEW_ONE_STORE = GREEN_STORE_URL_APP_REVIEW + "oneStore/";


    public static final String GREEN_STORE_URL_APP_REVIEW_ALL = GREEN_STORE_URL_APP_REVIEW + "listAll";
    public static final String GREEN_STORE_URL_APP_REVIEW_WRITE = GREEN_STORE_URL_APP_REVIEW + "insert";
    public static final String GREEN_STORE_URL_APP_REVIEW_UPDATE = GREEN_STORE_URL_APP_REVIEW + "update";
    public static final String GREEN_STORE_URL_APP_REVIEW_DELETE = GREEN_STORE_URL_APP_REVIEW + "delete";

    public static final String GREEN_STORE_URL_APP_REVIEW_CATESEARCH = GREEN_STORE_URL_APP_REVIEW + "appReviewCateSearch/";
    public static final String GREEN_STORE_URL_APP_REVIEW_LIKE = GREEN_STORE_URL_APP_REVIEW+"like";


}