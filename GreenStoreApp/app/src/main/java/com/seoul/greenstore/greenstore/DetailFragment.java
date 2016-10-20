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
import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements Server.ILoadResult, View.OnClickListener, net.daum.mf.map.api.MapView.OpenAPIKeyAuthenticationResultListener,
        MapView.MapViewEventListener,
        MapView.CurrentLocationEventListener,
        MapView.POIItemEventListener {
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


    //play item
    private List<Play> playList;

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
    private MapView mapView = null;
    private MapPOIItem marker = null;
    private ViewGroup mapViewContainer = null;

    //좋아요 플래그
    private int likeFlag = 0;

    //intent 기능 관련
    private static final int PLAY_ACTIVITY = 0;
    private static final int RESULT_OK = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("fragmenttest","1111111");
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
        Log.v("fragmenttest","333333");
        String[] gets = {Constants.GREEN_STORE_URL_APP_DETAIL + position, "GET"};
        Server server = new Server(getActivity(), this);
        server.execute(gets);
    }

    @Override
    public void customAddList(String result) {
        if (!result.isEmpty()) {
            try {
                int playNum = 0;

                JSONObject jsonRootObject = new JSONObject(result);
                playList = new ArrayList<>();

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray playJson = jsonRootObject.optJSONArray("playList");
                JSONArray storeJson = jsonRootObject.optJSONArray("storeList");

                playNum = playJson.length();

                //menu & price
                menu = new ArrayList<String>();
                price = new ArrayList<String>();

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < playJson.length(); i++) {

                    JSONObject jsonObject = playJson.getJSONObject(i);
                    Play play = new Play();

                    Location playLoc = new Location("");
                    playLoc.setLatitude(Double.valueOf(jsonObject.optString("mapx")));
                    playLoc.setLongitude(Double.valueOf(jsonObject.optString("mapy")));

                    play.setPlayAddr(jsonObject.optString("addr1").toString());
                    play.setPlayPhoto(jsonObject.optString("firstimage").toString());
                    play.setPlayTitle(jsonObject.optString("title").toString());
                    play.setPlayLoc(playLoc);
                    playList.add(play);
                }

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
                    }
                    if (Integer.parseInt(jsonObject.getString("price")) != 0) {
                        menu.add(i, jsonObject.getString("menu"));
                        price.add(i, jsonObject.getString("price"));
                    }
                }
                //store 부분 아이템 생성
                settingStoreDetailFragment();

                //play 부분 아이템 생성
                settingPlayDetailFragment();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                Drawable drawable = getResources().getDrawable(R.drawable.noimage);
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
                    mapViewContainer.removeAllViews();
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    startActivityForResult(intent,PLAY_ACTIVITY);
                }
            });
        }
    }

    public void startActivityForResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PLAY_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Log.v("resultSetTest","12313123");
//                    if (data.getStringArrayListExtra("userData") != null) {
//                        facebookUserData = data.getStringArrayListExtra("userData");
//                        User.user = facebookUserData;
//                        profileImage = (ImageView) naviView.findViewById(R.id.profileImage);
//                        userIdView = (TextView) naviView.findViewById(R.id.userId);
//                        userIdView.setText(facebookUserData.get(1));
//                        Picasso.with(getApplicationContext()).load(User.user.get(2)).fit().into(profileImage);
//                    } else {
//                        kakaoUserData = data.getStringArrayListExtra("kakaoData");
//                        User.user = kakaoUserData;
//
//                        userIdView = (TextView) naviView.findViewById(R.id.userId);
//                        userIdView.setText(kakaoUserData.get(1));
//                    }
//                    menu.setTitle("Logout");
//                    //사용자 조회
//                    memberLookup();
                }
                break;
        }
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
        detailPride.setText(pride);
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

        if (User.userStoreLike != null) {
            for (int i = 0; i < User.userStoreLike.size(); ++i) {
                Log.v("userlike", "" + User.userStoreLike.get(i));
                if (User.userStoreLike.get(i).equals(String.valueOf(position))) {
                    Drawable drawable = getResources().getDrawable(
                            R.drawable.likestar);
                    clickLikeButton.setBackgroundDrawable(drawable);
                    likeFlag = 1;
                }
            }
        }

        //daum map
        //스토어 주소로 좌표값 생성
        loc = findGeoPoint();

        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);

        //다음이 제공하는 MapView객체 생성 및 API Key 설정
        mapView = new MapView(getActivity());
        mapView.setDaumMapApiKey(MapKeys.daumMapKey);
        mapView.setOpenAPIKeyAuthenticationResultListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setPOIItemEventListener(this);

        //중심점을 해당 스토어로 변경
        mapViewContainer.addView(mapView);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clickLikeButton:
                likeCheck();
                break;
            case R.id.clickLikeText:
                likeCheck();
                break;
        }
    }


    private void likeCheck() {
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
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(loc.getLatitude(), loc.getLongitude()), true);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView arg0, MapPOIItem arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem poItem, MapPoint mapPoint) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPOIItemSelected(MapView arg0, MapPOIItem arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView arg0, float arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCurrentLocationUpdate(MapView arg0, MapPoint arg1, float arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView arg0) {
        // TODO Auto-generated method stub

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
        Log.v("test", "@2");
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(loc.getLatitude(), loc.getLongitude()), 1, true);

        //marker
        marker = new MapPOIItem();
        marker.setItemName(name);
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(loc.getLatitude(), loc.getLongitude()));
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
        // Move To
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(loc.getLatitude(), loc.getLongitude()), true);
        // Zoom To
        mapView.setZoomLevel(1, true);

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView arg0, int arg1,
                                                        String arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }
}
