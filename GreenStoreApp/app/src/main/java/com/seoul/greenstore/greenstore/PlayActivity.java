package com.seoul.greenstore.greenstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.seoul.greenstore.greenstore.MapView.MapViewItem;
import com.squareup.picasso.Picasso;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

/**
 * Created by X on 2016-10-11.
 */
public class PlayActivity extends Activity implements MapView.MapViewEventListener {
    private Intent intent;
    private MapPOIItem marker;

    private String title;
    private String addr;
    private String photo;
    private String tel;
    private double lat;
    private double longitude;

    private TextView playName;
    private TextView playAddr;
    private TextView playTel;
    private ImageView playPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play);
        this.setFinishOnTouchOutside(false);

        intent = getIntent();
        title = intent.getExtras().getString("title");
        addr = intent.getExtras().getString("addr");
        photo = intent.getExtras().getString("photo");
        lat = intent.getExtras().getDouble("lat");
        longitude = intent.getExtras().getDouble("longitude");
        tel = intent.getExtras().getString("tel");

        playName = (TextView)findViewById(R.id.play_name);
        playAddr = (TextView)findViewById(R.id.play_addr);
        playTel = (TextView)findViewById(R.id.play_tel);
        playPhoto = (ImageView)findViewById(R.id.play_image);

        if(title.equals("null")) title = "이름 정보가 없습니다.";
        if(addr.equals("null")) addr = "주소 정보가 없습니다.";
        if(tel.equals("null")) tel = "번호 정보가 없습니다.";

        playAddr.setText(addr);
        playName.setText(title);
        playTel.setText(tel);

        if(!photo.equals("null")) Picasso.with(this).load(photo).resize(1500,400).into(playPhoto);

        //지도 띄우기
        MapViewItem.mapViewContainer = (ViewGroup) findViewById(R.id.play_map_view);
        MapViewItem.mapView = new MapView(this);
        MapViewItem.mapView.setMapViewEventListener(this);
        MapViewItem.mapViewContainer.addView(MapViewItem.mapView);
    }

    //implements MapViewEventListener
    @Override
    public void onMapViewInitialized(MapView mapView) {
        //지도이동
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(lat,longitude), 1, true);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(lat,longitude);
        mapView.setMapCenterPoint(mapPoint, true);


        //마커생성
        marker = new MapPOIItem();
        marker.setItemName(title);
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(lat,longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        //마커추가
        mapView.addPOIItem(marker);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }


}
