package com.seoul.greenstore.greenstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.Commons.MapKeys;
import com.seoul.greenstore.greenstore.Dto.Play;
import com.seoul.greenstore.greenstore.Dto.Review;
import com.seoul.greenstore.greenstore.MapView.MapViewItem;
import com.seoul.greenstore.greenstore.Mypage.MyPageFragment_store;
import com.seoul.greenstore.greenstore.Review.ReviewWriteFragment;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.User.User;
import com.squareup.picasso.Picasso;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailFragment extends Fragment implements Server.ILoadResult, View.OnClickListener, MapView.MapViewEventListener {
    // TODO: Rename parameter arguments, choose names that match

    private View view;
    private int position;   //데이터 아이디
    private String photo;   //이미지 경로
    private String name;    //스토어 이름
    private int rcmn;       //서울시 좋아요
    private int like;       //사용자 좋아요
    private String phone;      //스토어 전화번호
    private String addr;    //스토어 위치
    private String info;    //스토어 기타 정보
    private List<String> menu;    //스토어 메뉴
    private List<String> price;      //스토어 메뉴 가격
    private String pride;   //스토어 자랑거리
    private Double pointX;
    private Double pointY;

    private List<Play> playList;

    //review itemList
    private List<Review> reviewList;

    //android item
    private ImageView detailPhoto;
    private TextView detailName;
    private TextView detailLike;
    private TextView detailRcmn;
    private ImageButton clickLikeButton;
    private TextView clickLikeText;
    private TextView detailInfo;
    private TextView detailPride;
    private TextView detailMenu;
    private TextView detailPrice;
    private TextView detailPhone;
    private TextView detailAddr;
    private RelativeLayout rlWrite;
    private RelativeLayout rlMore;


    //daum map
    private Location loc;
    private MapPOIItem marker = null;

    //좋아요 플래그
    private int likeFlag = 0;

    //intent 기능 관련
    private static final int PLAY_ACTIVITY = 0;
    private static final int RESULT_OK = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("fragmenttest","222222");
        view = inflater.inflate(R.layout.activity_detail, container, false);


        // Inflate the layout for this fragment

        Bundle bundle = this.getArguments();
        position = bundle.getInt("position");

        Toast.makeText(getActivity().getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();

        rlWrite = (RelativeLayout) view.findViewById(R.id.review_write);
        rlMore = (RelativeLayout) view.findViewById(R.id.more);
        rlWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                Fragment fragment = new ReviewWriteFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("sh_id",position);
                bundle.putString("sh_name",name);
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.llContents, fragment);
                fragmentTransaction.addToBackStack(fm.findFragmentById(R.id.llContents).toString());
                fragmentTransaction.commit();
            }
        });

        rlMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                Fragment fragment = new ReviewFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("sh_id",position);
                bundle.putString("sh_name",name);
                bundle.putString("sh_addr",addr);
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.llContents, fragment);
                fragmentTransaction.addToBackStack(fm.findFragmentById(R.id.llContents).toString());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private Location findGeoPoint() {
        loc = new Location("");
        Address address;
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
        try {
            List<Address> addressList = geocoder.getFromLocationName(addr, 1);
            if (addressList.size() > 0) {
                address = addressList.get(0);
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                loc.setLatitude(lat);
                loc.setLongitude(lng);
            }
        } catch (IOException e) {
        }
        return loc;
    }

    @Override
    public void onStart() {
        super.onStart();
        String[] gets = {Constants.GREEN_STORE_URL_APP_DETAIL + position, "GET"};
        Server server = new Server(getActivity(), this);
        server.execute(gets);
    }

    @Override
    public void customAddList(String result) {
        if (!result.isEmpty()) {
            try {
                int playNum = 0;
                int reviewNum = 0;

                JSONObject jsonRootObject = new JSONObject(result);
                playList = new ArrayList<>();
                reviewList = new ArrayList<>();

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray playJson = jsonRootObject.optJSONArray("playList");
                JSONArray storeJson = jsonRootObject.optJSONArray("storeList");
                JSONArray reviewJson = jsonRootObject.optJSONArray("reviewList");

                if (playJson != null) playNum = playJson.length();
                if (reviewJson != null) reviewNum = reviewJson.length();

                //menu & price
                menu = new ArrayList<String>();
                price = new ArrayList<String>();

                //주변 놀거리 리스트
                for (int i = 0; i < playNum; i++) {
                    JSONObject jsonObject = playJson.getJSONObject(i);
                    Play play = new Play();

                    Location playLoc = new Location("");
                    playLoc.setLatitude(Double.valueOf(jsonObject.optString("mapy")));
                    playLoc.setLongitude(Double.valueOf(jsonObject.optString("mapx")));

                    play.setPlayAddr(jsonObject.optString("addr1").toString());
                    play.setPlayPhoto(jsonObject.optString("firstimage").toString());
                    play.setPlayTitle(jsonObject.optString("title").toString());
                    play.setPlayTel(jsonObject.optString("tel").toString());
                    play.setPlayLoc(playLoc);
                    playList.add(play);
                }

                //스토어 상세 정보
                for (int i = 0; i < storeJson.length(); ++i) {
                    JSONObject jsonObject = storeJson.getJSONObject(i);
                    if (i == 0) {
                        name = jsonObject.getString("sh_name");
                        rcmn = Integer.parseInt(jsonObject.getString("sh_rcmn"));
                        like = Integer.parseInt(jsonObject.getString("sh_like"));
                        phone = jsonObject.getString("sh_phone");
                        addr = jsonObject.getString("sh_addr");
                        info = jsonObject.getString("sh_info");
                        pride = jsonObject.getString("sh_pride");
                        photo = jsonObject.getString("sh_photo");
                        if(!jsonObject.getString("pointX").equals("null")) {
                            pointX = jsonObject.getDouble("pointX");
                            pointY = jsonObject.getDouble("pointY");
                        }
                    }
                    if (Integer.parseInt(jsonObject.getString("price")) != 0) {
                        menu.add(i, jsonObject.getString("menu"));
                        price.add(i, jsonObject.getString("price"));
                    }
                }

                //리뷰 정보
                for (int i = 0; i < reviewNum; ++i) {
                    JSONObject jsonObject = reviewJson.getJSONObject(i);
                    Review review = new Review();
                    review.setReviewName(jsonObject.getString("mname"));
                    review.setReviewPhoto(jsonObject.getString("mphoto"));
                    review.setReviewCount(Integer.parseInt(jsonObject.getString("relike")));
                    review.setReviewContent(jsonObject.getString("rcontent"));

                    //date
                    try {
                        Date from = new Date(Long.valueOf(jsonObject.getString("rdate")));
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String temp = transFormat.format(from);
                        Date to = transFormat.parse(temp);
                        review.setReviewTime(to);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    reviewList.add(review);
                }

                //store 부분 아이템 생성
                settingStoreDetailFragment();

                //play 부분 아이템 생성
                settingPlayDetailFragment();

                //review 부분 아이템 생성
                if(reviewNum>0) settingReviewDetailFragment();
                else{
                    view.findViewById(R.id.reviewMain).setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void settingReviewDetailFragment() {
        for (Review item : reviewList) {
            ImageView reviewPhoto = (ImageView) view.findViewById(R.id.reviewPhoto);
            TextView reviewName = (TextView) view.findViewById(R.id.reviewName);
            TextView reviewDate = (TextView) view.findViewById(R.id.reviewDate);
            TextView reviewCount = (TextView) view.findViewById(R.id.reviewCount);
            TextView reviewContent = (TextView) view.findViewById(R.id.reviewContent);

            if (!item.getReviewPhoto().equals(""))
                Picasso.with(view.getContext()).load(item.getReviewPhoto()).resize(100, 200).into(reviewPhoto);

            reviewName.setText(item.getReviewName());
            DateFormat format1 = DateFormat.getDateInstance(DateFormat.FULL);
            reviewDate.setText(String.valueOf(format1.format(item.getReviewTime())));
            reviewCount.setText(String.valueOf(item.getReviewCount()));
            reviewContent.setText(item.getReviewContent());
        }
    }

    private void settingPlayDetailFragment() {
        LinearLayout parentLinear = (LinearLayout) view.findViewById(R.id.playScroll);

        for (final Play item : playList) {

            //주소나 제목이 null인 것 변경
            if (item.getPlayTitle().equals("null"))
                item.setPlayTitle("이름이 없습니다.");
            else if (item.getPlayAddr().equals("null"))
                item.setPlayAddr("주소가 없습니다.");

            //주변 놀거리 xml 생성
            LinearLayout linearLayout = new LinearLayout(view.getContext());
            linearLayout.setId(item.hashCode());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(params);

            ImageView imageView = new ImageView(view.getContext());
            params.setMargins(10, 10, 20, 10);
            imageView.setMinimumHeight(600);
            imageView.setMinimumWidth(500);
            imageView.setMaxHeight(600);
            imageView.setMaxWidth(500);
            imageView.setLayoutParams(params);

            if (!item.getPlayPhoto().equals("null")) {
                Picasso.with(view.getContext()).load(item.getPlayPhoto()).resize(600, 700).into(imageView);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.seoul);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 600, 700, true));
                imageView.setBackgroundDrawable(d);
            }
            linearLayout.addView(imageView);

            TextView textView = new TextView(view.getContext());

            textView.setLayoutParams(params);
            textView.setText(item.getPlayTitle());
            textView.setTextSize(10);
            linearLayout.addView(textView);

            textView = new TextView(view.getContext());
            textView.setLayoutParams(params);
            textView.setText(item.getPlayAddr());
            textView.setTextSize(10);
            linearLayout.addView(textView);

            parentLinear.addView(linearLayout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MapViewItem.mapViewContainer.removeView(MapViewItem.mapView);
                    MapViewItem.mapView = null;
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    intent.putExtra("title", item.getPlayTitle());
                    intent.putExtra("addr", item.getPlayAddr());
                    intent.putExtra("photo", item.getPlayPhoto());
                    intent.putExtra("lat", item.getPlayLoc().getLatitude());
                    intent.putExtra("longitude", item.getPlayLoc().getLongitude());
                    intent.putExtra("tel", item.getPlayTel());
                    startActivityForResult(intent, PLAY_ACTIVITY);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        likeCheck();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MapViewItem.mapViewContainer.removeView(MapViewItem.mapView);
        MapViewItem.mapView = null;
        settingMap();
    }

    private void settingStoreDetailFragment() {
        detailPhoto = (ImageView) view.findViewById(R.id.detailPhoto);
        detailName = (TextView) view.findViewById(R.id.detailName);
        detailLike = (TextView) view.findViewById(R.id.detailLike);
        detailRcmn = (TextView) view.findViewById(R.id.detailRcmn);
        clickLikeButton = (ImageButton) view.findViewById(R.id.clickLikeButton);
        clickLikeText = (TextView) view.findViewById(R.id.clickLikeText);
        detailInfo = (TextView) view.findViewById(R.id.detailInfo);
        detailPride = (TextView) view.findViewById(R.id.detailPride);
        detailMenu = (TextView) view.findViewById(R.id.detailMenu);
        detailPrice = (TextView) view.findViewById(R.id.detailPrice);
        detailPhone = (TextView) view.findViewById(R.id.detailPhone);
        detailAddr = (TextView) view.findViewById(R.id.detailAddr);

        if (photo != null)
            Picasso.with(getActivity().getApplicationContext()).load(photo).fit().centerInside().into(detailPhoto);
        detailName.setText(name);
        detailLike.setText(String.valueOf(like));
        detailRcmn.setText(String.valueOf(rcmn));
        detailInfo.setText(info);

        if(pride != null) detailPride.setText(pride);
        else detailPride.setText("자랑거리 데이터가 없습니다.");

        detailPhone.setText(phone);
        detailAddr.setText(addr);

        //메뉴와 가격 set해줌.
        String temp = "";
        for (String item : menu) {
            temp += item + "\n\n";
        }
        detailMenu.setText(temp);

        temp = "";
        for (String item : price) {
            temp += item + "원\n\n";
        }
        detailPrice.setText(String.valueOf(temp));


        clickLikeButton.setOnClickListener(this);
        clickLikeText.setOnClickListener(this);


        //좋아요 했는지 체크
        likeCheck();
        //다음 지도 초기화
        settingMap();
    }

    private void likeCheck() {
        if (User.userStoreLike != null) {
            for (int i = 0; i < User.userStoreLike.size(); ++i) {
                Log.v("userlike", "" + User.userStoreLike.get(i));
                if (User.userStoreLike.get(i).equals(String.valueOf(position))) {
                    Drawable drawable = getResources().getDrawable(R.drawable.likestar);
                    if (clickLikeButton == null)
                        clickLikeButton = (ImageButton) view.findViewById(R.id.clickLikeButton);
                    clickLikeButton.setBackgroundDrawable(drawable);
                    likeFlag = 1;
                }
            }
        }
    }

    private void settingMap() {
        MapViewItem.mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);

        loc = findGeoPoint();

        //다음이 제공하는 MapView객체 생성 및 API Key 설정
        MapViewItem.mapView = new MapView(getActivity());

        MapViewItem.mapView.setDaumMapApiKey(MapKeys.daumMapKey);
        MapViewItem.mapView.setMapViewEventListener(this);

        //중심점을 해당 스토어로 변경
        MapViewItem.mapViewContainer.addView(MapViewItem.mapView);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clickLikeButton:
                likeClickCheck();
                break;
            case R.id.clickLikeText:
                likeClickCheck();
                break;
        }
    }


    private void likeClickCheck() {
        if (User.user == null)
            Toast.makeText(getActivity().getApplicationContext(), "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
        else {
            if (likeFlag == 1)
                Toast.makeText(getActivity().getApplicationContext(), "이미 좋아요 하셨습니다.", Toast.LENGTH_SHORT).show();
            else {
                //서버로 좋아요 +1
                String[] gets = {Constants.GREEN_STORE_URL_APP_STORELIKE + "?mkey=" + User.user.get(3) + "&sh_id=" + position, "GET"};
                Server server = new Server(getActivity(), this);
                server.execute(gets);

                if (User.userStoreLike.size() > 0)
                    User.userStoreLike.put(User.userStoreLike.size(), String.valueOf(position));
                else
                    User.userStoreLike.put(0, String.valueOf(position));

                //내가 좋아요한 스토어에 데이터 저장.
                MyPageFragment_store myPageFragment_store = new MyPageFragment_store();
                myPageFragment_store.setMyStoreLikeData(position,name,addr,photo);

                Drawable drawable = getResources().getDrawable(
                        R.drawable.likestar);
                clickLikeButton.setBackgroundDrawable(drawable);
                detailLike.setText(String.valueOf(++like));
                likeFlag = 1;
            }
        }
    }

    // 이 아래는 맵에 관한 메소드들임.
    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint MapPoint) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        // TODO Auto-generated method stub
        // Move and Zoom to
        Log.v("test", "@2 " + loc.getLatitude() + " / " + loc.getLongitude());
        Log.v("test", "@3 " + pointX + " / " + pointY);

        settingZoomMarkMap(mapView);
    }

    private void settingZoomMarkMap(MapView mapView){
        if(loc.getLatitude()!=0) {
            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(loc.getLatitude(), loc.getLongitude()), 1, true);
            Log.v("locTEst","22");
        }else if(pointX!=null) {
            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(pointY, pointX), 1, true);
            Log.v("locTEst","33");
        }else{
            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(0,0), 10, true);
            Toast.makeText(getActivity().getApplicationContext(),"주소 데이터 오류",Toast.LENGTH_SHORT).show();
            Log.v("locTEst","44");
        }

        //marker
        marker = new MapPOIItem();
        marker.setItemName(name);
        marker.setTag(0);
        if(loc.getLatitude()!=0)
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(loc.getLatitude(), loc.getLongitude()));
        else if(pointX!=null)
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(pointY, pointX));
        else{
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(loc.getLatitude(), loc.getLongitude()));
        }

        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint MapPoint) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint MapPoint) {
        // TODO Auto-generated method stub
        Log.v("test", "@3");
        settingZoomMarkMap(mapView);
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }
}
