package com.seoul.greenstore.greenstore.Commons;

/**
 * Created by user on 2016-09-10.
 */
public class Constants {

        public static final String BASIC_URL = "http://192.168.100.253:8080/";
//    public static final String BASIC_URL = "http://172.30.49.177:8080/";

//    public static final String BASIC_URL = "http://192.168.13.36:8080/";
//    public static final String BASIC_URL = "http://172.30.49.177:8080/";



//    public static final String GREEN_STORE_URL_BASIC = BASIC_URL + "greenStore/";
    public static final String GREEN_STORE_URL_APP = BASIC_URL + "app/";

    //detail
    public static final String GREEN_STORE_URL_APP_DETAIL = GREEN_STORE_URL_APP + "detail/";

    //search
    public static final String GREEN_STORE_URL_APP_SEARCH = GREEN_STORE_URL_APP + "search/";
    public static final String GREEN_STORE_URL_APP_CATESEARCH = GREEN_STORE_URL_APP + "cateSearch/";

    //meber
    public static final String GREEN_STORE_URL_APP_MEBERLOOKUP = GREEN_STORE_URL_APP + "memberLookup/";

    //notice
    public static final String GREEN_STORE_URL_APP_NOTICE = GREEN_STORE_URL_APP + "notice/list";

    //STS에 review/one 이 스토어 9018로 지정되어있음. 현재 스토어 sh_id받아와서 넣어주는걸로 코드변경해야함.
    public static final String GREEN_STORE_URL_APP_REVIEW_ONE = GREEN_STORE_URL_APP + "review/one";
    public static final String GREEN_STORE_URL_APP_REVIEW_ALL = GREEN_STORE_URL_APP + "review/listAll";

    public static final String GREEN_STORE_URL_APP_REVIEW_WRITE = GREEN_STORE_URL_APP + "review/insert";
    public static final String GREEN_STORE_URL_APP_REVIEW_DELETE = GREEN_STORE_URL_APP + "review/delete";

    public static final String GREEN_STORE_URL_APP_REVIEW_LIKE = GREEN_STORE_URL_APP+"review/like";



}