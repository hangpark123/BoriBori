package com.kdu.busboristudent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.kdu.busbori.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Fragment_favorite_701 extends Fragment {
    private Thread predict;
    private RecyclerView recyclerView;
    private Main_Adapter adapter_701;
    private List<Main_DataItem> Main_dataList = new ArrayList<>();
    BasicAWSCredentials credentials = new BasicAWSCredentials("AKIATR4KVIIS74DNZLGD", "KAjjuxbXJE6n1lyeYTuBfikvjtUq8BkQhQ6BUQOB");
    AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
    private int selectedItem = -1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_701, container, false);
        recyclerView = view.findViewById(R.id.favoritelist_701);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (adapter_701 == null) {
            adapter_701 = new Main_Adapter(Main_dataList);
        }

        TextView text701 = view.findViewById(R.id.text701);
        text701.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new Yangjumaps_701();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        adapter_701.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);
                if (position != RecyclerView.NO_POSITION) {
                    selectedItem = position;
                    Main_DataItem clickedItem = Main_dataList.get(position);
                    String stationId = clickedItem.getStationId();
                    Yangjumaps_701 yangjumapsFragment = new Yangjumaps_701();

                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.container, yangjumapsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    new Handler().postDelayed(() -> yangjumapsFragment.performMarkerClick(stationId), 200);
                }
            }
        });
        return view;
    }
    private void buspredict(String BusNo,String stationId, String destination){
        client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        predict = new Thread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                QueryRequest request = new QueryRequest()
                        .withTableName(BusNo)
                        .withKeyConditionExpression("stationId = :id")
                        .withExpressionAttributeValues(Collections.singletonMap(":id", new AttributeValue(stationId)));
                QueryResult result = client.query(request);
                List<Map<String, AttributeValue>> items = result.getItems();

                if (!items.isEmpty()) {
                    Map<String, AttributeValue> item = items.get(0);
                    AttributeValue stationNameAttribute = item.get("stationName");
                    AttributeValue locationNo1Attribute = item.get("locationNo1");
                    AttributeValue predictTime1Attribute = item.get("predictTime1");

                    if (locationNo1Attribute != null && predictTime1Attribute != null) {
                        String predict_stationName = stationNameAttribute.getS();
                        String predict_locationNo = locationNo1Attribute.getS();
                        String predict_time = predictTime1Attribute.getS();
                        requireActivity().runOnUiThread(() -> {
                            String newPredictData;
                            if (predict_time != null && predict_locationNo != null) {
                                if (predict_time.equals("X") && predict_locationNo.equals("X")) {
                                    newPredictData = "도착 정보 없음";
                                } else if (predict_time.equals("1")) {
                                    newPredictData = "곧 도착(" + predict_locationNo + "정류장)";
                                } else {
                                    newPredictData = predict_time + "분(" + predict_locationNo + "정류장)";
                                }
                                Main_DataItem dataItem = new Main_DataItem(stationId, predict_stationName, newPredictData, destination);
                                Main_dataList.add(dataItem);
                                recyclerView.setAdapter(adapter_701);
                            }
                        });
                    }
                }
            }
        });
        predict.start();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        SharedPreferences sharedPreferences_701 = requireActivity().getSharedPreferences("701_favorite", Context.MODE_PRIVATE);
        Set<String> favoriteStations_701 = (sharedPreferences_701.getStringSet("701_favorite", new LinkedHashSet<>()));
        List<String> stationIds = new ArrayList<>();
        List<String> destinations = new ArrayList<>();

        for (String combinedValue : favoriteStations_701) {
            String[] parts = combinedValue.split("\\|");
            for (int i = 0; i < parts.length; i += 2) {
                if (i + 1 < parts.length) {
                    stationIds.add(parts[i]);
                    destinations.add(parts[i + 1]);
                }
            }
        }
        String Bus_701 = "Bus701";
        for (int i = 0; i < stationIds.size(); i++) {
            String stationId = stationIds.get(i);
            String destination = destinations.get(i);
            buspredict(Bus_701, stationId, destination);
        }
    }
}