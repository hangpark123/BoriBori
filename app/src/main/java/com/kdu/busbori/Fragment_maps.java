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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Fragment_maps extends Fragment {
    private GoogleMap googleMap;
    private LatLng Dobong = new LatLng(37.687332, 127.043672);
    private LatLng Yangju = new LatLng(37.773208, 127.045193);
    private LatLng Deokhyeon = new LatLng(37.798859, 127.081819);
    private LatLng Campus = new LatLng(37.810158, 127.071145);
    private LatLng Route = new LatLng(37.757432, 127.055045);
    private Map<String, Marker> markers = new HashMap<>();
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
                    dobgusanMarkerOptions.snippet("도봉산");
                    googleMap.addMarker(dobgusanMarkerOptions);

                    MarkerOptions yangjuMarkerOptions = new MarkerOptions();
                    yangjuMarkerOptions.position(Yangju);
                    yangjuMarkerOptions.title("양주");
                    yangjuMarkerOptions.snippet("양주");
                    googleMap.addMarker(yangjuMarkerOptions);

                    MarkerOptions deokhyeonMarkerOptions = new MarkerOptions();
                    deokhyeonMarkerOptions.position(Deokhyeon);
                    deokhyeonMarkerOptions.title("덕현초교");
                    deokhyeonMarkerOptions.snippet("덕현초교");
                    googleMap.addMarker(deokhyeonMarkerOptions);

                    MarkerOptions campusMarkerOptions = new MarkerOptions();
                    campusMarkerOptions.position(Campus);
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

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("BoriGPS");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {
                    DocumentSnapshot document = documentChange.getDocument();
                    String documentId = document.getId();
                    double latitude = document.getDouble("latitude");
                    double longitude = document.getDouble("longitude");
                    String time = document.getString("time");
                    LatLng latLng = new LatLng(latitude, longitude);

                    Marker existingMarker = markers.get(documentId);

                    if (existingMarker != null) {
                        existingMarker.setPosition(latLng);
                    } else {
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                        markerOptions.title(time);
                        Marker newMarker = googleMap.addMarker(markerOptions);
                        markers.put(documentId, newMarker);
                    }
                }
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