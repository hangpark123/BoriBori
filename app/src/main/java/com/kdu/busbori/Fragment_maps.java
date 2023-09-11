package com.kdu.busbori;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_maps extends Fragment {
    private GoogleMap googleMap;
    private LatLng Dobong = new LatLng(37.687332, 127.043672);
    private LatLng Yangju = new LatLng(37.810158, 127.071145);
    private LatLng Route = new LatLng(37.757432, 127.055045);
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

                    MarkerOptions dobgusanMarkerOptions = new MarkerOptions();
                    dobgusanMarkerOptions.position(Dobong);
                    dobgusanMarkerOptions.title("도봉산");
                    dobgusanMarkerOptions.snippet("도봉산 셔틀버스");
                    googleMap.addMarker(dobgusanMarkerOptions);

                    MarkerOptions yangjuMarkerOptions = new MarkerOptions();
                    yangjuMarkerOptions.position(Yangju);
                    yangjuMarkerOptions.title("양주");
                    yangjuMarkerOptions.snippet("양주 셔틀버스");
                    googleMap.addMarker(yangjuMarkerOptions);


                    MarkerOptions routeMarkerOptions = new MarkerOptions();
                    routeMarkerOptions.position(Route);

                    moveCameraToLocation(Route, 12);
                }
            });
        }

        Button dobongButton = view.findViewById(R.id.button_dobong);
        Button yangjuButton = view.findViewById(R.id.button_yangju);
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

        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToLocation(Route, 12);
            }
        });

        return view;
    }

    private void moveCameraToLocation(LatLng location, float zoom) {
        if (googleMap != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
        }
    }
}