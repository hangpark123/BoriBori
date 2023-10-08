package com.kdu.busboristudent;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.tabs.TabLayout;
import com.kdu.busbori.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<MyDataItem> dataList;
    private Map<String, LatLng> stationLatLngMap;
    private Map<String, Marker> markers = new HashMap<>();
    private String busStationId;
    private String predict_flag;
    private String predict_locationNo;
    private String predict_time;
    private String predict_busNo;
    private String predict_stationId;
    private int selectedItem = -1;
    private GoogleMap googleMap;
    private RecyclerView recyclerView;
    private SlidingUpPanelLayout sliding;
    private TabLayout tabLayout;
    public MyAdapter(List<MyDataItem> dataList, String busStationId, String predict_flag, String predict_locationNo, String predict_time, String predict_busNo, String predict_stationId, GoogleMap googleMap, RecyclerView recyclerView, SlidingUpPanelLayout sliding, Map markers, TabLayout tabLayout) {
        this.dataList = dataList;
        this.busStationId = busStationId;
        this.predict_flag = predict_flag;
        this.predict_locationNo = predict_locationNo;
        this.predict_time = predict_time;
        this.predict_busNo = predict_busNo;
        this.predict_stationId = predict_stationId;
        this.googleMap = googleMap;
        this.recyclerView = recyclerView;
        this.sliding = sliding;
        this.markers = markers;
        this.tabLayout = tabLayout;

        stationLatLngMap = new HashMap<>();
        for (MyDataItem item : dataList) {

            stationLatLngMap.put(item.getStationId(), item.getLatLng());
        }

    }
    public Map<String, LatLng> getStationLatLng() {
        return stationLatLngMap;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        switch (viewType) {
            case 0:
                layoutResId = R.layout.list_item_layout_first;
                break;
            case 1:
                layoutResId = R.layout.list_item_layout_last;
                break;
            case 2:
                layoutResId = R.layout.list_item_layout_middle;
                break;
            default:
                layoutResId = R.layout.list_item_layout;
                break;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);

        return new ViewHolder(view);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        dataList = dataList.stream().distinct().collect(Collectors.toList());
        MyDataItem item = dataList.get(position);
        holder.textView.setText(item.getStationName());
        holder.textView2.setText(item.getStationId());
        if (busStationId != null && busStationId.equals(item.getStationId())) {
            holder.busIcon.setVisibility(View.VISIBLE);
        } else {
            holder.busIcon.setVisibility(View.GONE);
        }
        if (position == selectedItem) {
            holder.itemLayout.setBackgroundResource(R.drawable.item_click_background);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MyDataItem item = dataList.get(position);
        String stationId = item.getStationId();
        if (position == 0) {
            return 0;
        } else if (position == getItemCount() - 1) {
            return 1;
        } else if ("235001220".equals(stationId) || "235000658".equals(stationId) || "235000242".equals(stationId)) {
            return 2;
        }
        return 3;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textView2;
        public LinearLayout itemLayout;
        public ImageView busIcon;

        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            textView2 = itemView.findViewById(R.id.textview2);
            itemLayout = itemView.findViewById(R.id.item_layout);
            busIcon = itemView.findViewById(R.id.visiblebusicon);
            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_START;
                }
            };
            itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        selectedItem = position;
                        notifyDataSetChanged();
                        MyDataItem clickedItem = dataList.get(position);
                        String stationId = clickedItem.getStationId();
                        LatLng LatLng = getStationLatLng().get(stationId);
                        Marker existingMarker = markers.get(stationId);

                        if (LatLng != null) {
                            if (existingMarker != null) {
                                existingMarker.showInfoWindow();
                            }
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 16), 250, null);
                            if (sliding != null) {
                                sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }
                        }
                    }
                }
            });
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();

                    if(position == 0) {
                        recyclerView.smoothScrollToPosition(0);
                        Log.e("brr", String.valueOf(tabLayout.getTag()));
                    } else if (position == 1) {
                        if (tabLayout.getTag().equals("701")){
                            smoothScroller.setTargetPosition(18);
                            Objects.requireNonNull(recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);
                        } else if (tabLayout.getTag().equals("21")){
                            smoothScroller.setTargetPosition(16);
                            Objects.requireNonNull(recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);
                        } else if (tabLayout.getTag().equals("733")){
                            smoothScroller.setTargetPosition(14);
                            Objects.requireNonNull(recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);
                        }
                    }
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
                @SuppressLint("ResourceType")
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    int position = tab.getPosition();

                    if(position == 0) {
                        recyclerView.smoothScrollToPosition(0);
                    } else if (position == 1) {
                        if (tabLayout.getTag().equals("701")){
                            smoothScroller.setTargetPosition(18);
                            Objects.requireNonNull(recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);
                        } else if (tabLayout.getTag().equals("21")){
                            smoothScroller.setTargetPosition(16);
                            Objects.requireNonNull(recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);
                        } else if (tabLayout.getTag().equals("733")){
                            smoothScroller.setTargetPosition(14);
                            Objects.requireNonNull(recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);
                        }
                    }
                }
            });
        }
    }
    private void moveCameraToLocation(LatLng location, float zoom) {
        if (googleMap != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
        }
    }
}