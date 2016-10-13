package com.seoul.greenstore.greenstore;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.Commons.MapKeys;
import com.seoul.greenstore.greenstore.Server.Server;
import com.seoul.greenstore.greenstore.User.User;
import com.squareup.picasso.Picasso;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements Server.ILoadResult, View.OnClickListener,MapView.OpenAPIKeyAuthenticationResultListener,
        MapView.MapViewEventListener,
        MapView.CurrentLocationEventListener,
        MapView.POIItemEventListener  {
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

    //daum map
    private Location loc;
    private MapView mapView = null;
    private MapPOIItem marker = null;

    //좋아요 플래그
    private int likeFlag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("comeCheck","1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("comeCheck","2");
        view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Inflate the layout for this fragment

        Bundle bundle = this.getArguments();
        position = bundle.getInt("position");
        Toast.makeText(getActivity().getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();
        return view;
    }

    private Location findGeoPoint(){
        loc = new Location("");
        Address address;
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
        try {
            List<Address> addressList = geocoder.getFromLocationName(addr, 1);
            if(addressList.size() > 0){
                address = addressList.get(0);
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                loc.setLatitude(lat);
                loc.setLongitude(lng);
            }
        }catch (IOException e){}
        return loc;
    }

    @Override
    public void onStart() {
        Log.v("comeCheck","3");
        super.onStart();
        String[] gets = {Constants.GREEN_STORE_URL_APP_DETAIL + position, "GET"};
        Server server = new Server(getActivity(), this);
        server.execute(gets);
    }

    @Override
    public void customAddList(String result) {
        try {
            menu = new ArrayList<String>();
            price = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                menu.add(i, jsonObject.getString("menu"));
                price.add(i, jsonObject.getString("price"));
            }
            settingDetailFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void settingDetailFragment() {
        Log.v("comeCheck","4");
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

        if (photo != null)
            Picasso.with(getActivity().getApplicationContext()).load(photo).fit().centerInside().into(detailPhoto);
        detailName.setText(name);
        detailLike.setText(String.valueOf(like));
        detailRcmn.setText(String.valueOf(rcmn));
        detailInfo.setText(info);
        detailPride.setText(pride);
        detailPhone.setText(phone);

        //메뉴와 가격 set해줌.
        String temp = "";
        for(String item : menu){ temp += item+"\n\n"; }
        detailMenu.setText(temp);

        temp = "";
        for (String item : price) { temp += item+"원\n\n"; }
        detailPrice.setText(String.valueOf(temp));

        clickLikeButton.setOnClickListener(this);
        clickLikeText.setOnClickListener(this);

        if(User.userStoreLike!=null) {
            for (int i = 0; i < User.userStoreLike.size(); ++i) {
                if(User.userStoreLike.get(i).equals(position)){
                    Drawable drawable = getResources().getDrawable(
                            R.drawable.star2);
                    clickLikeButton.setBackgroundDrawable(drawable);
                    likeFlag = 1;
                }
            }
        }

        //daum map
        //스토어 주소로 좌표값 생성
        loc = findGeoPoint();

        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        ViewGroup mapViewContainer = (ViewGroup)view.findViewById(R.id.map_view);

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
    public void onStop(){
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


    private void likeCheck(){
        if(User.user==null)
            Toast.makeText(getActivity().getApplicationContext(),"로그인을 해주세요.",Toast.LENGTH_SHORT).show();
        else{
            if(likeFlag==1)
                Toast.makeText(getActivity().getApplicationContext(),"이미 좋아요 하셨습니다.",Toast.LENGTH_SHORT).show();
            else{
                //서버로 좋아요 +1
                String[] gets = {Constants.GREEN_STORE_URL_APP_STORELIKE+"?mkey="+User.user.get(3)+"&sh_id="+position, "GET"};
                Server server = new Server(getActivity(),this);
                server.execute(gets);

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
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(loc.getLatitude(),loc.getLongitude()),true);
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
        Log.v("test","@2");
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(loc.getLatitude(),loc.getLongitude()), 1, true);

        //marker
        marker = new MapPOIItem();
        marker.setItemName(name);
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(loc.getLatitude(),loc.getLongitude()));
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
        Log.v("test","@3");
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
