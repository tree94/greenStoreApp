package com.seoul.greenstore.greenstore;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.squareup.picasso.Picasso;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements Server.ILoadResult, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match

    private View view;
    private int position;   //데이터 아이디
    private String photo;   //이미지 경로
    private String name;    //스토어 이름
    private int rcmn;       //서울시 좋아요
    private int like;       //사용자 좋아요
    private String addr;    //스토어 위치
    private String info;    //스토어 기타 정보
    private List<String> menu;    //스토어 메뉴
    private List<Integer> price;      //스토어 메뉴 가격
    private String pride;   //스토어 자랑거리

    //android item
    private ImageView detailPhoto;
    private TextView detailName;
    private TextView detailLike;
    private TextView detailRcmn;
    private ImageButton clickLikeButton;
    private TextView clickLikeText;

    //daum map
    private Location loc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail, container, false);


        // Inflate the layout for this fragment

        Bundle bundle = this.getArguments();
        position = bundle.getInt("position");

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
        super.onStart();
        String[] gets = {Constants.GREEN_STORE_URL_APP_DETAIL + position, "GET"};
        Server server = new Server(getActivity(), this);
        server.execute(gets);
    }

    @Override
    public void customAddList(String result) {
        try {
            menu = new ArrayList<String>();
            price = new ArrayList<Integer>();
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (i == 0) {
                    name = jsonObject.getString("sh_name");
                    rcmn = Integer.parseInt(jsonObject.getString("sh_rcmn"));
                    like = Integer.parseInt(jsonObject.getString("sh_like"));
                    addr = jsonObject.getString("sh_addr");
                    info = jsonObject.getString("sh_info");
                    pride = jsonObject.getString("sh_pride");
                    photo = jsonObject.getString("sh_photo");
                }
                menu.add(i, jsonObject.getString("menu"));
                price.add(i, Integer.parseInt(jsonObject.getString("price")));
            }
            settingDetailFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void settingDetailFragment() {
        detailPhoto = (ImageView) view.findViewById(R.id.detailPhoto);
        detailName = (TextView) view.findViewById(R.id.detailName);
        detailLike = (TextView) view.findViewById(R.id.detailLike);
        detailRcmn = (TextView) view.findViewById(R.id.detailRcmn);
        clickLikeButton = (ImageButton) view.findViewById(R.id.clickLikeButton);
        clickLikeText = (TextView) view.findViewById(R.id.clickLikeText);

        if (photo != null)
            Picasso.with(getActivity().getApplicationContext()).load(photo).fit().centerInside().into(detailPhoto);
        detailName.setText(name);
        detailLike.setText(String.valueOf(like));
        detailRcmn.setText(String.valueOf(rcmn));

        clickLikeButton.setOnClickListener(this);
        clickLikeText.setOnClickListener(this);

        //daum map
        //스토어 주소로 좌표값 생성
        loc = findGeoPoint();

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(loc.getLatitude(),loc.getLongitude());

        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        ViewGroup mapViewContainer = (ViewGroup)view.findViewById(R.id.map_view);

        //다음이 제공하는 MapView객체 생성 및 API Key 설정
        MapView mapView = new MapView(getActivity());
        mapView.setDaumMapApiKey(MapKeys.daumMapKey);


        //중심점을 해당 스토어로 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(loc.getLatitude(),loc.getLongitude()), true);

        mapViewContainer.addView(mapView);

        //marker
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(name);
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
    }

    @Override
    public void onStop(){
        super.onStop();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clickLikeButton:
                Toast.makeText(getActivity().getApplicationContext(), "button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.clickLikeText:
                Toast.makeText(getActivity().getApplicationContext(), "text", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
