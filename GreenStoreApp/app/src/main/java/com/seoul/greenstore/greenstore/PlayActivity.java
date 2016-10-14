package com.seoul.greenstore.greenstore;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import com.seoul.greenstore.greenstore.Commons.MapKeys;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

/**
 * Created by X on 2016-10-11.
 */
public class PlayActivity extends Activity implements MapView.MapViewEventListener {


    private String findAddress = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play);
        this.setFinishOnTouchOutside(false);

        //지도 띄우기
        MapView mapView = new MapView((this));
        mapView.setDaumMapApiKey(MapKeys.daumMapKey);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);


        mapView.setMapViewEventListener(this);

        //주소를 위도/경도로 바꾸기기

    }


    //implements MapViewEventListener
    @Override
    public void onMapViewInitialized(MapView mapView) {

        //지도이동
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633);
        mapView.setMapCenterPoint(mapPoint, true);


        //마커생성
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        //마커추가
        mapView.addPOIItem(marker);


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
