package com.kdu.busboristudent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.kdu.busbori.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Yangjumaps_733 extends Fragment {
    private static final String CACHE_KEY = "busRouteData_733";
    private static final String SERVICE_KEY = "7d5zmWvwEZpaRX9CIT1%2B4B4zWcsM5VHsA9gkQJ3fJEiv8%2BpuOnHZY9zwprseP3wmK0XbPoDKQl%2BUGKNBi419Ag%3D%3D";
    private static final String ROUTE_ID733 = "241451015";
    private static final String ROUTE_ENDPOINT = "https://apis.data.go.kr/6410000/busrouteservice/getBusRouteStationList";
    private static final String LOCATION_ENDPOINT = "https://apis.data.go.kr/6410000/buslocationservice/getBusLocationList";
    private static final String PREDICT_ENDPOINT = "https://openapi.gbis.go.kr/ws/rest/busarrivalservice";
    private String predict_flag;
    private String predict_locationNo;
    private String predict_time;
    private String predict_busNo;
    private String predict_stationId;
    private RecyclerView recyclerView;
    private MyAdapter adapter_733;
    private List<MyDataItem> dataList = new ArrayList<>();
    private GoogleMap googleMap;
    private LatLng middle = new LatLng(37.813357, 127.063539);
    private Map<String, Marker> markers = new HashMap<>();
    private String busStationId;
    private Marker busMarker;
    private Thread locationThread_733;
    boolean isrunning = true;
    private SlidingUpPanelLayout sliding_layout;
    private TabLayout tabLayout;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yangjumaps_733, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_733);
        recyclerView = view.findViewById(R.id.recyclerview_733);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sliding_layout = view.findViewById(R.id.slidingUpPanelLayout_733);
        tabLayout = view.findViewById(R.id.slidetab_733);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;
                    if (adapter_733 == null) {
                        adapter_733 = new MyAdapter(dataList, busStationId, predict_flag, predict_locationNo, predict_time, predict_busNo, predict_stationId,
                                googleMap, recyclerView, sliding_layout, markers, tabLayout);
                    }
                    for (MyDataItem dataItem : dataList) {
                        LatLng latLng = dataItem.getLatLng();
                        String stationName = dataItem.getStationName();
                        String stationId = dataItem.getStationId();
                        Marker existingMarker = markers.get(stationId);
                        if (latLng != null) {
                            if (existingMarker != null) {
                                existingMarker.setTitle(stationName);
                                existingMarker.setPosition(latLng);
                            } else {
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                                markerOptions.title(stationName);
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.stationicon));
                                Marker newMarker = googleMap.addMarker(markerOptions);
                                markers.put(stationId, newMarker);
                            }
                        }map.animateCamera(CameraUpdateFactory.newLatLngZoom(middle, 14), 250, null);
                        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                marker.showInfoWindow();
                                LatLng markerLatLng = marker.getPosition();
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 16), 250, null);

                                return true;
                            }
                        });
                    }
                    recyclerView.setAdapter(adapter_733);
                }
            });
        }
        Button schedulebutton_733 = view.findViewById(R.id.schedulebutton_733);
        schedulebutton_733.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new Schedule_733();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        FloatingActionButton location_refreshButton = view.findViewById(R.id.location_refreshButton_733);
        location_refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isrunning = false;
                locationThread_733.interrupt();
                isrunning = true;
                buslocation();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(middle).zoom(14).bearing(0).build()), 250, null);
            }
        });
        return view;
    }
    private void buspredict(String selectedstaionId){
        Thread predictThread_733 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder predict_urlBuilder = new StringBuilder(PREDICT_ENDPOINT);
                    predict_urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + SERVICE_KEY);
                    predict_urlBuilder.append("&" + URLEncoder.encode("stationId", "UTF-8") + "=" + selectedstaionId);
                    predict_urlBuilder.append("&" + URLEncoder.encode("routeId", "UTF-8") + "=" + ROUTE_ID733);
                    URL predict_url = new URL(predict_urlBuilder.toString());
                    HttpsURLConnection predict_conn = (HttpsURLConnection) predict_url.openConnection();
                    predict_conn.setRequestMethod("GET");
                    predict_conn.setRequestProperty("Content-type", "application/xml");

                    StringBuilder predict_sb = new StringBuilder();
                    try (BufferedReader predict_rd = new BufferedReader(new InputStreamReader(predict_conn.getInputStream()))) {
                        String predict_line;
                        while ((predict_line = predict_rd.readLine()) != null) {
                            predict_sb.append(predict_line);
                        }
                    }
                    DocumentBuilderFactory predict_factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder predict_builder = predict_factory.newDocumentBuilder();
                    Document predict_document = predict_builder.parse(new InputSource(new StringReader(predict_sb.toString())));

                    NodeList predict_nodeList = predict_document.getElementsByTagName("busArrivalItem");
                    for (int i = 0; i < predict_nodeList.getLength(); i++) {
                        Element predict_element = (Element) predict_nodeList.item(i);
                        predict_flag = predict_element.getElementsByTagName("FLAG").item(0).getTextContent();
                        predict_locationNo = predict_element.getElementsByTagName("locationNo1").item(0).getTextContent();
                        predict_time = predict_element.getElementsByTagName("predictTime1").item(0).getTextContent();
                        predict_busNo = predict_element.getElementsByTagName("plateNo1").item(0).getTextContent();
                        predict_stationId = predict_element.getElementsByTagName("stationId").item(0).getTextContent();

                        predict_conn.disconnect();
                    }
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }
            }
        });
        predictThread_733.start();
        Log.e("Boribus", "predict_733 스레드 시작");
    }
    private void busroute() {
        Thread routeThread_733 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder route_urlBuilder = new StringBuilder(ROUTE_ENDPOINT);
                    route_urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + SERVICE_KEY);
                    route_urlBuilder.append("&" + URLEncoder.encode("routeId", "UTF-8") + "=" + ROUTE_ID733);
                    URL route_url = new URL(route_urlBuilder.toString());
                    HttpsURLConnection route_conn = (HttpsURLConnection) route_url.openConnection();
                    route_conn.setRequestMethod("GET");
                    route_conn.setRequestProperty("Content-type", "application/xml");

                    StringBuilder route_sb = new StringBuilder();
                    try (BufferedReader route_rd = new BufferedReader(new InputStreamReader(route_conn.getInputStream()))) {
                        String route_line;
                        while ((route_line = route_rd.readLine()) != null) {
                            route_sb.append(route_line);
                        }
                    }

                    DocumentBuilderFactory route_factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder route_builder = route_factory.newDocumentBuilder();
                    Document route_document = route_builder.parse(new InputSource(new StringReader(route_sb.toString())));

                    NodeList route_nodeList = route_document.getElementsByTagName("busRouteStationList");
                    for (int i = 0; i < route_nodeList.getLength(); i++) {
                        Element route_element = (Element) route_nodeList.item(i);
                        String route_stationId = route_element.getElementsByTagName("stationId").item(0).getTextContent();
                        String route_stationName = route_element.getElementsByTagName("stationName").item(0).getTextContent();
                        double route_latitude = Double.parseDouble(route_element.getElementsByTagName("y").item(0).getTextContent());
                        double route_longitude = Double.parseDouble(route_element.getElementsByTagName("x").item(0).getTextContent());

                        if (!(locationThread_733.isInterrupted())) {
                            requireActivity().runOnUiThread(() -> {
                                LatLng route_latlng = new LatLng(route_latitude, route_longitude);
                                MyDataItem dataItem = new MyDataItem(route_stationId, route_stationName, route_latlng);
                                dataList.add(dataItem);
                                saveDataToCache(route_sb.toString());
                            });
                        }
                        route_conn.disconnect();
                    }
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }
            }
        });
        routeThread_733.start();
        Log.e("Boribus", "Route_733 스레드 시작");
    }

    private void buslocation() {
        locationThread_733 = new Thread(() -> {
            while (isrunning) {
                try {
                    StringBuilder location_urlBuilder = new StringBuilder(LOCATION_ENDPOINT);
                    location_urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + SERVICE_KEY);
                    location_urlBuilder.append("&" + URLEncoder.encode("routeId", "UTF-8") + "=" + ROUTE_ID733);
                    URL location_url = new URL(location_urlBuilder.toString());
                    HttpsURLConnection location_conn = (HttpsURLConnection) location_url.openConnection();
                    location_conn.setRequestMethod("GET");
                    location_conn.setRequestProperty("Content-type", "application/xml");

                    StringBuilder location_sb = new StringBuilder();
                    try (BufferedReader location_rd = new BufferedReader(new InputStreamReader(location_conn.getInputStream()))) {
                        String location_line;
                        while ((location_line = location_rd.readLine()) != null) {
                            location_sb.append(location_line);
                        }
                    }
                    Log.e("Boribus", String.valueOf(location_sb));
                    DocumentBuilderFactory location_factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder location_builder = location_factory.newDocumentBuilder();
                    Document location_document = location_builder.parse(new InputSource(new StringReader(location_sb.toString())));

                    NodeList location_nodeList = location_document.getElementsByTagName("stationId");
                    for (int i = 0; i < location_nodeList.getLength(); i++) {
                        Element location_element = (Element) location_nodeList.item(i);
                        busStationId = location_element.getTextContent();
                        Log.e("Boribus", busStationId);
                    }
                    if (!(locationThread_733.isInterrupted())) {
                        requireActivity().runOnUiThread(() -> {
                            if (busStationId != null) {
                                for (int i = 0; i < dataList.size(); i++) {
                                    MyDataItem item = dataList.get(i);
                                    String stationId = item.getStationId();
                                    String stationName = item.getStationName();
                                    if (stationId != null && stationId.equals(busStationId)) {
                                        LatLng latLng = adapter_733.getStationLatLng().get(stationId);
                                        if (latLng != null) {
                                            if (busMarker == null) {
                                                busMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title("현위치 : " + stationName).icon(BitmapDescriptorFactory.fromResource(R.drawable.yangjubusicon_25)));
                                                Objects.requireNonNull(busMarker).showInfoWindow();
                                                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), 250, null);
                                            } else {
                                                busMarker.setTitle("현위치 : " + stationName);
                                                busMarker.setPosition(latLng);
                                                busMarker.showInfoWindow();
                                                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), 250, null);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        });
                    }
                    location_conn.disconnect();
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }); locationThread_733.start();
        Log.e("Boribus", "Location_733 스레드 시작");
    }
    private void saveDataToCache(String data) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("cache_733", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CACHE_KEY, data);
        editor.apply();
    }
    private String loadCachedData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("cache_733", Context.MODE_PRIVATE);
        return sharedPreferences.getString(CACHE_KEY, null);
    }
    private void loadDataFromCache() {
        String cachedXmlData = loadCachedData();

        if (cachedXmlData != null && !cachedXmlData.isEmpty()) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(cachedXmlData));
                Document document = builder.parse(is);

                NodeList nodeList = document.getElementsByTagName("busRouteStationList");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    String cache_stationId = element.getElementsByTagName("stationId").item(0).getTextContent();
                    String cache_stationName = element.getElementsByTagName("stationName").item(0).getTextContent();
                    double cache_latitude = Double.parseDouble(element.getElementsByTagName("y").item(0).getTextContent());
                    double cache_longitude = Double.parseDouble(element.getElementsByTagName("x").item(0).getTextContent());
                    LatLng cache_latLng = new LatLng(cache_latitude, cache_longitude);

                    MyDataItem dataItem = new MyDataItem(cache_stationId, cache_stationName, cache_latLng);
                    dataList.add(dataItem);
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isSlidingPanelExpanded() {
        return sliding_layout != null && sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED;
    }

    public void collapseSlidingPanel() {
        if (sliding_layout != null) {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        String cachedData = loadCachedData();
        if (cachedData != null && !cachedData.isEmpty()) {
            loadDataFromCache();
        } else {
            busroute();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isrunning = true;
        buslocation();
    }

    @Override
    public void onStop() {
        super.onStop();
        isrunning = false;
        locationThread_733.interrupt();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isrunning = false;
        locationThread_733.interrupt();
    }
}