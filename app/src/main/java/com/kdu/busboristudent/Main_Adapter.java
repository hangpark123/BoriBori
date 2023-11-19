package com.kdu.busboristudent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Main_Adapter extends RecyclerView.Adapter<Main_Adapter.ViewHolder> {
    private List<Main_DataItem> Main_dataList;
    private View.OnClickListener onItemClickListener;
    public Main_Adapter(List<Main_DataItem> Main_dataList) {
        this.Main_dataList = Main_dataList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listfavorit_item_layout, parent, false);
        return new ViewHolder(view);
    }
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Main_DataItem item = Main_dataList.get(position);
        holder.textView.setText(item.getStationName()+"["+item.getDestination()+"]");
        if (item.getPredictData().equals("도착 정보 없음")){
            holder.textView2.setText(item.getPredictData());
            holder.textView2.setTextColor(Color.parseColor("#585858"));
        } else if (item.getPredictData().contains("곧 도착")){
            holder.textView2.setText(item.getPredictData());
            holder.textView2.setTextColor(Color.parseColor("#DF0101"));
        } else {
            holder.textView2.setText(item.getPredictData());
            holder.textView2.setTextColor(Color.parseColor("#66CC00"));
        }
    }

    @Override
    public int getItemCount() {
        return Main_dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textView2;
        public LinearLayout itemLayout;

        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            textView2 = itemView.findViewById(R.id.textview2);
            itemLayout = itemView.findViewById(R.id.item_layout);


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

        }
    }
    public void setOnClickListener(View.OnClickListener listener) {
        onItemClickListener = listener;
    }
}