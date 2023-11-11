package com.kdu.busboristudent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;
import com.kdu.busbori.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Route_Adapter extends RecyclerView.Adapter<Route_Adapter.ViewHolder> {
    private List<Route_DataItem> Route_dataList;
    private Map<String, LatLng> stationLatLngMap;
    private String busStationId;
    private int selectedItem = -1;
    private String Bus;
    private String busKey;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Set<String> favoriteStations;
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private View.OnClickListener onItemClickListener;
    public Route_Adapter(List<Route_DataItem> Route_dataList, String busStationId, RecyclerView recyclerView, TabLayout tabLayout, String Bus, Context context) {
        this.Route_dataList = Route_dataList;
        this.busStationId = busStationId;
        this.recyclerView = recyclerView;
        this.tabLayout = tabLayout;
        this.Bus = Bus;
        this.context = context;

        stationLatLngMap = new HashMap<>();
        for (Route_DataItem item : Route_dataList) {
            stationLatLngMap.put(item.getStationId(), item.getLatLng());
        }

        busKey = Bus + "_favorite";
        sharedPreferences = context.getSharedPreferences(busKey, Context.MODE_PRIVATE);
        favoriteStations = sharedPreferences.getStringSet(busKey, new LinkedHashSet<>());
        List<String> stationIds = new ArrayList<>();
        List<String> destinations = new ArrayList<>();

        for (String combinedValue : favoriteStations) {
            String[] parts = combinedValue.split("\\|");
            for (int i = 0; i < parts.length; i += 2) {
                if (i + 1 < parts.length) {
                    stationIds.add(parts[i]);
                    destinations.add(parts[i + 1]);
                }
            }
        }
        for (Route_DataItem item : Route_dataList) {
            String stationId = item.getStationId();
            boolean isFavorite = stationIds.contains(stationId);
            item.setFavorite(isFavorite);
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
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Route_dataList = Route_dataList.stream().distinct().collect(Collectors.toList());
        Route_DataItem item = Route_dataList.get(position);
        holder.textView.setText(item.getStationName());
        holder.textView2.setText(item.getDestination());
        if (busStationId != null && busStationId.equals(item.getStationId())) {
            holder.busIcon.setVisibility(View.VISIBLE);
        } else {
            holder.busIcon.setVisibility(View.GONE);
        }
        if (position == selectedItem) {
            holder.itemLayout.setBackgroundResource(R.drawable.item_click_background);
        }
        holder.starButton.setChecked(item.isFavorite());
    }

    @Override
    public int getItemCount() {
        return Route_dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Route_DataItem item = Route_dataList.get(position);
        String turnYn = item.getturnYn();
        if (position == 0) {
            return 0;
        } else if (position == getItemCount() - 1) {
            return 1;
        } else if ("Y".equals(turnYn)) {
            return 2;
        }
        return 3;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textView2;
        public LinearLayout itemLayout;
        public ImageView busIcon;
        public ToggleButton starButton;

        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            textView2 = itemView.findViewById(R.id.textview2);
            itemLayout = itemView.findViewById(R.id.item_layout);
            busIcon = itemView.findViewById(R.id.visiblebusicon);
            starButton = itemView.findViewById(R.id.favorite_toggle_button);

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
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        onItemClickListener.onClick(v);
                    }
                }
            });
            starButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MutatingSharedPrefs")
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Route_DataItem clickedItem = Route_dataList.get(position);
                        String stationId = clickedItem.getStationId();
                        String destination = clickedItem.getDestination();
                        String combine = stationId + "|" + destination;
                        boolean isFavorite = clickedItem.isFavorite();

                        if (starButton.isChecked() && !isFavorite) {
                            favoriteStations.add(combine);
                            clickedItem.setFavorite(true);
                        } else if (!starButton.isChecked() && isFavorite) {
                            favoriteStations.remove(combine);
                            clickedItem.setFavorite(false);
                        }
                        editor = sharedPreferences.edit();
                        editor.clear();
                        editor.putStringSet(busKey, favoriteStations);
                        editor.apply();
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
    @SuppressLint("NotifyDataSetChanged")
    public void updateBusStationId(String newBusStationId) {
        busStationId = newBusStationId;
        notifyDataSetChanged();
    }
    public void setOnClickListener(View.OnClickListener listener) {
        onItemClickListener = listener;
    }
}