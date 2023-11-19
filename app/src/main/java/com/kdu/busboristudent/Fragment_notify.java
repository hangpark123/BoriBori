package com.kdu.busboristudent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.kdu.busbori.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Fragment_notify extends Fragment {
    private RecyclerView recyclerView;
    private Notify_Adapter adapter;
    private Thread scanthread;
    private List<Notify_DataItem> Notify_dataList = new ArrayList<>();
    private BasicAWSCredentials credentials = new BasicAWSCredentials("AKIATR4KVIIS74DNZLGD", "KAjjuxbXJE6n1lyeYTuBfikvjtUq8BkQhQ6BUQOB");
    private AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
    private int selectedItem = -1;
    private Context savecontext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
        recyclerView = view.findViewById(R.id.NotifyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (adapter == null) {
            adapter = new Notify_Adapter(Notify_dataList, savecontext);
        }
        recyclerView.setAdapter(adapter);
        return view;
    }
    private void BusData() {
        client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        scanthread = new Thread(() -> {
            ScanRequest request = new ScanRequest()
                    .withTableName("Busbutton");
            ScanResult result = client.scan(request);
            List<Map<String, AttributeValue>> items = result.getItems();
            requireActivity().runOnUiThread(() -> {
                Notify_dataList.clear();
                for (Map<String, AttributeValue> item : items) {
                    String assort = item.get("assort") != null ? item.get("assort").getS() : null;
                    String buttonText = item.get("buttonText") != null ? item.get("buttonText").getS() : null;
                    String destination = item.get("destination") != null ? item.get("destination").getS() : null;
                    String time = item.get("time") != null ? item.get("time").getS() : null;
                    String type = item.get("type") != null ? item.get("type").getS() : null;

                    if (assort != null && buttonText != null && destination != null && time != null && type != null) {
                        Notify_DataItem dataItem = new Notify_DataItem(assort, buttonText, destination, time, type);
                        Notify_dataList.add(dataItem);
                    }
                    adapter.updateAdapterData(Notify_dataList, savecontext);
                }
            });
        });
        scanthread.start();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        savecontext = context;
        BusData();
    }
    @SuppressLint({"DefaultLocale", "NotifyDataSetChanged"})
    public void onStart(){
        super.onStart();
    }
}