package kr.dangguel.domestictravel;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PolylineOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment map;
    EditText etMap;
    NaverMap naverMap;
    LatLng latLng;
    Marker marker;

    double[] mapLats;
    double[] mapLngs;
    String[] toDos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        if (intent.getDoubleExtra("lat", 0) != 0) {
            double lat = intent.getDoubleExtra("lat", 0);
            double lng = intent.getDoubleExtra("lng", 0);
            if (lat != 0 && lng != 0) {
                latLng = new LatLng(lat, lng);
            }
        } else if (intent.getDoubleArrayExtra("mapLats") != null) {
            latLng=null;
            mapLats = intent.getDoubleArrayExtra("mapLats");
            mapLngs = intent.getDoubleArrayExtra("mapLngs");
            toDos = intent.getStringArrayExtra("toDos");
        }
        etMap = findViewById(R.id.et_map);
        map = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);

        this.naverMap = naverMap;

        if (latLng != null) {
            naverMap.setCameraPosition(new CameraPosition(latLng, 15));
            marker = new Marker();
            marker.setPosition(latLng);
            marker.setMap(naverMap);
        }else if(latLng==null && getIntent().getDoubleArrayExtra("mapLats")!=null){
            LatLng[] latLngs = new LatLng[mapLats.length];
            Marker[] markers = new Marker[latLngs.length];
            for (int i = 0; i < latLngs.length; i++) {
                latLngs[i] = new LatLng(mapLats[i], mapLngs[i]);
                markers[i] = new Marker(latLngs[i]);
                markers[i].setCaptionText(toDos[i]);
                markers[i].setCaptionTextSize(20);
                markers[i].setCaptionColor(Color.BLUE);
                markers[i].setMap(naverMap);
            }
            PolylineOverlay polyline = new PolylineOverlay();
            ArrayList<LatLng> latLngArrayList = new ArrayList<>();
            for(int i=0; i<latLngs.length; i++){
                latLngArrayList.add(latLngs[i]);
            }
            polyline.setCoords(latLngArrayList);
            polyline.setWidth(10);
            polyline.setColor(Color.GREEN);
            polyline.setJoinType(PolylineOverlay.LineJoin.Round);
            polyline.setMap(naverMap);
            this.naverMap.setCameraPosition(new CameraPosition(latLngs[0], 13));
        }
    }

    public void chooseMap(View view) {
        if (marker != null) {
            marker.setMap(null);
            marker = null;
        }

        marker = new Marker();
        if (naverMap == null) {
            Log.e("abc", "널");
        }
        latLng = naverMap.getCameraPosition().target;
        marker.setPosition(latLng);
        marker.setMap(naverMap);

        Toast.makeText(this, "위치 설정 완료", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("lat", latLng.latitude);
        resultIntent.putExtra("lng", latLng.longitude);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void searchMap(View view) {
        String addr = etMap.getText().toString();
        CameraUpdate cameraUpdate;
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        try {
            List<Address> addresses = geocoder.getFromLocationName(addr, 3);
            StringBuffer buffer = new StringBuffer();
            for (Address t : addresses) {
                buffer.append(t.getLatitude() + " , " + t.getLongitude() + "\n");
            }
            latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());


            cameraUpdate = CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);

            Toast.makeText(this, etMap.getText().toString(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show();
        }
    }
}



