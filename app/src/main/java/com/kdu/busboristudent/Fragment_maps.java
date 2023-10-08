package com.kdu.busboristudent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kdu.busbori.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Fragment_maps extends Fragment {
    private GoogleMap googleMap;
    private LatLng Dobong = new LatLng(37.687332, 127.043672);
    private LatLng Yangju = new LatLng(37.773208, 127.045193);
    private LatLng Deokhyeon = new LatLng(37.798859, 127.081819);
    private LatLng Campus = new LatLng(37.810158, 127.071145);
    private LatLng Route = new LatLng(37.757432, 127.055045);
    private Map<String, Marker> markers = new HashMap<>();
    String deviceid;
    String time;
    LatLng latLng;
    Thread thread;
    boolean isrunning = true;
    BasicAWSCredentials credentials = new BasicAWSCredentials("AKIATR4KVIIS74DNZLGD", "KAjjuxbXJE6n1lyeYTuBfikvjtUq8BkQhQ6BUQOB");
    AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;

                    MarkerOptions dobongMarkerOptions = new MarkerOptions();
                    dobongMarkerOptions.position(Dobong);
                    dobongMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dobong));
                    dobongMarkerOptions.title("도봉산");
                    dobongMarkerOptions.snippet("도봉산");
                    googleMap.addMarker(dobongMarkerOptions);

                    MarkerOptions yangjuMarkerOptions = new MarkerOptions();
                    yangjuMarkerOptions.position(Yangju);
                    yangjuMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.yangju));
                    yangjuMarkerOptions.title("양주");
                    yangjuMarkerOptions.snippet("양주");
                    googleMap.addMarker(yangjuMarkerOptions);

                    MarkerOptions deokhyeonMarkerOptions = new MarkerOptions();
                    deokhyeonMarkerOptions.position(Deokhyeon);
                    deokhyeonMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.deokhyeon));
                    deokhyeonMarkerOptions.title("덕현초교");
                    deokhyeonMarkerOptions.snippet("덕현초교");
                    googleMap.addMarker(deokhyeonMarkerOptions);

                    MarkerOptions campusMarkerOptions = new MarkerOptions();
                    campusMarkerOptions.position(Campus);
                    campusMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.campus));
                    campusMarkerOptions.title("학교");
                    campusMarkerOptions.snippet("학교");
                    googleMap.addMarker(campusMarkerOptions);


                    MarkerOptions routeMarkerOptions = new MarkerOptions();
                    routeMarkerOptions.position(Route);

                    moveCameraToLocation(Route, 12);
                }
            });
        }

        Button dobongButton = view.findViewById(R.id.button_dobong);
        Button yangjuButton = view.findViewById(R.id.button_yangju);
        Button deokhyeonButton = view.findViewById(R.id.button_deokhyeon);
        Button campusButton = view.findViewById(R.id.button_campus);
        Button routeButton = view.findViewById(R.id.button_route);

        dobongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToLocation(Dobong, 16);
            }
        });

        yangjuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToLocation(Yangju, 16);
            }
        });

        deokhyeonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToLocation(Deokhyeon, 16);
            }
        });

        campusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToLocation(Campus, 16);
            }
        });

        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToLocation(Route, 12);
            }
        });

        return view;
    }
    public void onStart(){
        super.onStart();
        isrunning = true;
        client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        thread = new Thread(() -> {
            while (isrunning) {
                ScanRequest request = new ScanRequest()
                        .withTableName("Borigps");
                ScanResult result = client.scan(request);
                List<Map<String, AttributeValue>> items = result.getItems();

                if (!(thread.isInterrupted())) {
                    getActivity().runOnUiThread(() -> {
                        for (Map<String, AttributeValue> item : items) {
                            deviceid = Objects.requireNonNull(item.get("deviceid")).getS();
                            time = Objects.requireNonNull(item.get("time")).getS();
                            String latitudeStr = item.get("latitude") != null ? Objects.requireNonNull(item.get("latitude")).getS() : null;
                            String longitudeStr = item.get("longitude") != null ? Objects.requireNonNull(item.get("longitude")).getS() : null;

                            if (latitudeStr != null && longitudeStr != null) {
                                double latitude = Double.parseDouble(latitudeStr);
                                double longitude = Double.parseDouble(longitudeStr);
                                latLng = new LatLng(latitude, longitude);

                                Marker existingMarker = markers.get(deviceid);

                                if (latLng != null && deviceid != null && time != null) {
                                    if (existingMarker != null) {
                                        existingMarker.setPosition(latLng);
                                    } else {
                                        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                                        markerOptions.title(time);
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.schoolbusicon));
                                        Marker newMarker = googleMap.addMarker(markerOptions);
                                        markers.put(deviceid, newMarker);
                                    }
                                }
                            }
                        }
                    });
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    @Override
    public void onStop() {
        super.onStop();
        isrunning = false;
        thread.interrupt();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isrunning = false;
        thread.interrupt();
    }
    private void moveCameraToLocation(LatLng location, float zoom) {
        if (googleMap != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
        }
    }
}