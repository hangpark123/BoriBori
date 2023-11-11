package com.kdu.busboristudent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Yangjumaps_701 extends Fragment {
    private static final String CACHE_KEY = "busRouteData_701";
    private static final String SERVICE_KEY = "7d5zmWvwEZpaRX9CIT1%2B4B4zWcsM5VHsA9gkQJ3fJEiv8%2BpuOnHZY9zwprseP3wmK0XbPoDKQl%2BUGKNBi419Ag%3D%3D";
    private static final String ROUTE_ID701 = "235000091";
    private static final String ROUTE_ENDPOINT = "https://apis.data.go.kr/6410000/busrouteservice/getBusRouteStationList";
    private String predict_locationNo;
    private String predict_time;
    private RecyclerView recyclerView;
    private Route_Adapter adapter_701;
    private List<Route_DataItem> Route_dataList = new ArrayList<>();
    private GoogleMap googleMap;
    private LatLng middle = new LatLng(37.795385, 127.057775);
    private Map<String, Marker> markers = new HashMap<>();
    private String busStationId;
    private String busEnd;
    private String busNo;
    private TextView endtext;
    private TextView nametext;
    private TextView timetext;
    private Thread locationThread_701;
    private Thread routeThread_701;
    private Thread predictThread_701;
    boolean isrunning = true;
    private SlidingUpPanelLayout sliding_layout;
    private TabLayout tabLayout;
    FrameLayout progressBar;
    private int selectedItem = -1;
    private String selectedstationName;
    BasicAWSCredentials credentials = new BasicAWSCredentials("AKIATR4KVIIS74DNZLGD", "KAjjuxbXJE6n1lyeYTuBfikvjtUq8BkQhQ6BUQOB");
    AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
    private String Bus = "701";
    private Context savecontext;

    public Yangjumaps_701() {
    }

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged", "ResourceType"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yangjumaps_701, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_701);
        recyclerView = view.findViewById(R.id.recyclerview_701);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sliding_layout = view.findViewById(R.id.slidingUpPanelLayout_701);
        tabLayout = view.findViewById(R.id.slidetab_701);
        endtext = view.findViewById(R.id.textViewNow_701);
        nametext = view.findViewById(R.id.textViewName_701);
        timetext = view.findViewById(R.id.textViewTime_701);
        progressBar = view.findViewById(R.id.progressBar_701);
        progressBar.setVisibility(View.VISIBLE);
        if (adapter_701 == null) {
            adapter_701 = new Route_Adapter(Route_dataList, busStationId, recyclerView, tabLayout, Bus, savecontext);
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;
                    for (Route_DataItem dataItem : Route_dataList) {
                        LatLng latLng = dataItem.getLatLng();
                        String stationName = dataItem.getStationName();
                        String stationId = dataItem.getStationId();
                        String destination = dataItem.getDestination();
                        Marker existingMarker = markers.get(stationId);
                        if (latLng != null) {
                            if (existingMarker != null) {
                                existingMarker.setTitle(stationName);
                                existingMarker.setPosition(latLng);
                            } else {
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                                markerOptions.title(stationName+"["+ destination +"]");
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busstationicon_50));
                                Marker newMarker = googleMap.addMarker(markerOptions);
                                markers.put(stationId, newMarker);
                            }
                        }map.animateCamera(CameraUpdateFactory.newLatLngZoom(middle, 13.5F), 250, null);
                        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                String stationId = null;
                                for (Map.Entry<String, Marker> entry : markers.entrySet()) {
                                    if (entry.getValue().equals(marker)) {
                                        stationId = entry.getKey();
                                        break;
                                    }
                                }
                                marker.showInfoWindow();
                                LatLng markerLatLng = marker.getPosition();
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 16), 250, null);
                                nametext.setText(marker.getTitle());

                                buspredict(stationId);
                                return true;
                            }
                        });
                    }
                    recyclerView.setAdapter(adapter_701);
                }
            });
        }
        ImageButton schedulebutton_701 = view.findViewById(R.id.schedulebutton_701);
        schedulebutton_701.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new Schedule_701();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        FloatingActionButton location_refreshButton = view.findViewById(R.id.location_refreshButton_701);
        location_refreshButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                isrunning = false;
                locationThread_701.interrupt();
                isrunning = true;
                buslocation();
                nametext.setText("701");
                timetext.setText("");
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(middle).zoom(13.5F).bearing(0).build()), 250, null);
            }
        });

        adapter_701.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);
                if (position != RecyclerView.NO_POSITION) {
                    selectedItem = position;
                    Route_DataItem clickedItem = Route_dataList.get(position);
                    String stationId = clickedItem.getStationId();
                    String destination = clickedItem.getDestination();
                    buspredict(stationId);
                    selectedstationName = clickedItem.getStationName();
                    LatLng LatLng = adapter_701.getStationLatLng().get(stationId);
                    Marker existingMarker = markers.get(stationId);
                    nametext.setText(selectedstationName+ "[" + destination + "]");
                    if (LatLng != null) {
                        if (existingMarker != null) {
                            existingMarker.showInfoWindow();
                        }
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 16), 250, null);
                        if (sliding_layout != null) {
                            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                    }
                }
            }
        });
        return view;
    }
    private void buspredict(String stationId){
        client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        predictThread_701 = new Thread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                QueryRequest request = new QueryRequest()
                        .withTableName("Bus701")
                        .withKeyConditionExpression("stationId = :id")
                        .withExpressionAttributeValues(Collections.singletonMap(":id", new AttributeValue(stationId)));
                QueryResult result = client.query(request);
                List<Map<String, AttributeValue>> items = result.getItems();

                if (!items.isEmpty()) {
                    Map<String, AttributeValue> item = items.get(0);
                    AttributeValue locationNo1Attribute = item.get("locationNo1");
                    AttributeValue predictTime1Attribute = item.get("predictTime1");

                    if (locationNo1Attribute != null && predictTime1Attribute != null) {
                        predict_locationNo = locationNo1Attribute.getS();
                        predict_time = predictTime1Attribute.getS();
                    }
                }

                if (!(predictThread_701.isInterrupted())) {
                    requireActivity().runOnUiThread(() -> {
                        if (predict_time != null && predict_locationNo != null) {
                            if (predict_time.equals("X") && predict_locationNo.equals("X")) {
                                if (endtext.getText().equals("운행 정보 없음")){
                                    timetext.setText("");
                                } else {
                                    timetext.setText("도착 정보 없음");
                                    timetext.setTextColor(Color.parseColor("#585858"));
                                }
                            } else if (predict_time.equals("1")){
                                timetext.setText("곧 도착(" + predict_locationNo + "정류장)");
                                timetext.setTextColor(Color.parseColor("#DF0101"));
                            } else {
                                timetext.setText(predict_time + "분(" + predict_locationNo + "정류장)");
                                timetext.setTextColor(Color.parseColor("#66CC00"));
                            }
                        }
                    });
                }
            }
        });
        predictThread_701.start();
    }
    private void busroute() {
        routeThread_701 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder route_urlBuilder = new StringBuilder(ROUTE_ENDPOINT);
                    route_urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + SERVICE_KEY);
                    route_urlBuilder.append("&" + URLEncoder.encode("routeId", "UTF-8") + "=" + ROUTE_ID701);
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

                    int desint = 0;
                    NodeList route_nodeList = route_document.getElementsByTagName("busRouteStationList");
                    for (int i = 0; i < route_nodeList.getLength(); i++) {
                        Element route_element = (Element) route_nodeList.item(i);
                        String route_stationId = route_element.getElementsByTagName("stationId").item(0).getTextContent();
                        String route_stationName = route_element.getElementsByTagName("stationName").item(0).getTextContent();
                        double route_latitude = Double.parseDouble(route_element.getElementsByTagName("y").item(0).getTextContent());
                        double route_longitude = Double.parseDouble(route_element.getElementsByTagName("x").item(0).getTextContent());
                        String route_turnYn = route_element.getElementsByTagName("turnYn").item(0).getTextContent();
                        String destination = "";

                        if (route_turnYn.equals("N") && desint == 0) {
                            destination = "양주역 방면";
                        } else if (route_turnYn.equals("Y")) {
                            destination = "회차";
                            desint = 1;
                        } else if (route_turnYn.equals("N")) {
                            destination = "경동대학교 방면";
                        }

                        if (!(routeThread_701.isInterrupted())) {
                            final String finalDestination = destination;
                            requireActivity().runOnUiThread(() -> {
                                LatLng route_latlng = new LatLng(route_latitude, route_longitude);
                                Route_DataItem dataItem = new Route_DataItem(route_stationId, route_stationName, route_latlng, route_turnYn, finalDestination);
                                Route_dataList.add(dataItem);
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
        routeThread_701.start();
    }
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void buslocation() {
        client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        locationThread_701 = new Thread(() -> {
            while (isrunning) {
                try {
                    ScanRequest request = new ScanRequest()
                            .withTableName("BusLocation701")
                            .withProjectionExpression("stationId, endBus, plateNo");
                    ScanResult result = client.scan(request);
                    List<Map<String, AttributeValue>> items = result.getItems();

                    for (Map<String, AttributeValue> item : items) {
                        busStationId = Objects.requireNonNull(item.get("stationId")).getS();
                        busEnd = Objects.requireNonNull(item.get("endBus")).getS();
                        busNo = Objects.requireNonNull(item.get("plateNo")).getS();
                    }
                    if (!(locationThread_701.isInterrupted())) {
                        requireActivity().runOnUiThread(() -> {
                            if (busEnd != null && busEnd.equals("0")) {
                                endtext.setText(busNo + " 운행 중");
                                endtext.setTextColor(Color.parseColor("#009999"));
                                progressBar.setVisibility(View.GONE);
                            } else if (busEnd != null && busEnd.equals("1")) {
                                endtext.setText(busNo + " 막차");
                                progressBar.setVisibility(View.GONE);
                                endtext.setTextColor(Color.parseColor("#DF0101"));
                            } else {
                                endtext.setText("운행 정보 없음");
                                progressBar.setVisibility(View.GONE);
                            }
                            if (busStationId != null) {
                                adapter_701.updateBusStationId(busStationId);

                                for (Map.Entry<String, Marker> entry : markers.entrySet()) {
                                    Marker marker = entry.getValue();
                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.busstationicon_50));
                                }

                                for (int i = 0; i < Route_dataList.size(); i++) {
                                    Route_DataItem item = Route_dataList.get(i);
                                    String stationName = item.getStationName();
                                    String stationId = item.getStationId();
                                    String destination = item.getDestination();

                                    if (stationId != null && stationId.equals(busStationId)) {
                                        LatLng latLng = adapter_701.getStationLatLng().get(stationId);

                                        if (latLng != null) {
                                            Marker specialMarker = markers.get(stationId);
                                            if (specialMarker != null) {
                                                specialMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yangjubusicon_25));

                                                specialMarker.setTitle("현위치 : " + stationName + "[" + destination + "]");
                                                specialMarker.setPosition(latLng);
                                                specialMarker.showInfoWindow();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        });
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } catch (AmazonClientException e) {
                    e.printStackTrace();
                    isrunning = false;
                    locationThread_701.interrupt();
                    if (locationThread_701.isInterrupted() && !locationThread_701.isAlive()) {
                        isrunning = true;
                        locationThread_701.start();
                    }
                }
            }
        });
        locationThread_701.start();
    }
    private void saveDataToCache(String data) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("cache_701", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CACHE_KEY, data);
        editor.apply();
    }
    private String loadCachedData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("cache_701", Context.MODE_PRIVATE);
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

                int desint = 0;
                NodeList nodeList = document.getElementsByTagName("busRouteStationList");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    String cache_stationId = element.getElementsByTagName("stationId").item(0).getTextContent();
                    String cache_stationName = element.getElementsByTagName("stationName").item(0).getTextContent();
                    double cache_latitude = Double.parseDouble(element.getElementsByTagName("y").item(0).getTextContent());
                    double cache_longitude = Double.parseDouble(element.getElementsByTagName("x").item(0).getTextContent());
                    LatLng cache_latLng = new LatLng(cache_latitude, cache_longitude);
                    String cache_turnYn = element.getElementsByTagName("turnYn").item(0).getTextContent();
                    String destination = "";

                    if (cache_turnYn.equals("N") && desint == 0) {
                        destination = "양주역 방면";
                    } else if (cache_turnYn.equals("Y")) {
                        destination = "회차";
                        desint = 1;
                    } else if (cache_turnYn.equals("N")) {
                        destination = "경동대학교 방면";
                    }
                    final String finalDestination = destination;
                    Route_DataItem dataItem = new Route_DataItem(cache_stationId, cache_stationName, cache_latLng, cache_turnYn, finalDestination);
                    Route_dataList.add(dataItem);
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
    public void performMarkerClick(String stationId) {
        Marker marker = markers.get(stationId);

        if (marker != null) {
            marker.showInfoWindow();
            LatLng markerLatLng = marker.getPosition();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 16), 250, null);
            nametext.setText(marker.getTitle());
            buspredict(stationId);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        savecontext = context;
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
        locationThread_701.interrupt();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isrunning = false;
        locationThread_701.interrupt();
    }
}