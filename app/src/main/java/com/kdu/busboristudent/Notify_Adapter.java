package com.kdu.busboristudent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kdu.busbori.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Notify_Adapter extends RecyclerView.Adapter<Notify_Adapter.ViewHolder> {
    private List<Notify_DataItem> Notify_dataList;
    private View.OnClickListener onItemClickListener;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Set<String> notifyList;
    private Context context;
    public Notify_Adapter(List<Notify_DataItem> Notify_dataList, Context context) {
        this.Notify_dataList = Notify_dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listnotify_item_layout, parent, false);
        return new ViewHolder(view);
    }
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notify_DataItem item = Notify_dataList.get(position);
        holder.textView.setText(item.getDestination()+ "[" +item.getType() + "]");
        holder.textView2.setText(item.getTime() + " 배차 수 : " + item.getButtonText());
        holder.notifyButton.setChecked(item.isNotify());
    }

    @Override
    public int getItemCount() {
        return Notify_dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textView2;
        public LinearLayout itemLayout;
        public ToggleButton notifyButton;
        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            textView2 = itemView.findViewById(R.id.textview2);
            itemLayout = itemView.findViewById(R.id.item_layout);
            notifyButton = itemView.findViewById(R.id.notify_toggle_button);

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

            notifyButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MutatingSharedPrefs")
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Notify_DataItem clickedItem = Notify_dataList.get(position);
                        String destination = clickedItem.getDestination();
                        String type = clickedItem.getType();
                        String time = clickedItem.getTime();
                        String combine = destination + type + time;
                        boolean isNotify = clickedItem.isNotify();

                        if (notifyButton.isChecked() && !isNotify) {
                            notifyList.add(combine);
                            clickedItem.setNotify(true);
                        } else if (!notifyButton.isChecked() && isNotify) {
                            notifyList.remove(combine);
                            clickedItem.setNotify(false);
                        }
                        editor = sharedPreferences.edit();
                        editor.clear();
                        editor.putStringSet("notify", notifyList);
                        editor.apply();
                    }
                }
            });
        }
    }
    public void setOnClickListener(View.OnClickListener listener) {
        onItemClickListener = listener;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateAdapterData(List<Notify_DataItem> newdataList, Context context) {
        this.Notify_dataList = newdataList;
        this.context = context;
        Notify_dataList.sort(new Comparator<Notify_DataItem>() {
            @Override
            public int compare(Notify_DataItem dataList1, Notify_DataItem dataList2) {
                return dataList1.getAssort().compareTo(dataList2.getAssort());
            }
        });
        sharedPreferences = context.getSharedPreferences("notify", Context.MODE_PRIVATE);
        notifyList = sharedPreferences.getStringSet("notify", new LinkedHashSet<>());
        for (Notify_DataItem item : Notify_dataList) {
            String destination = item.getDestination();
            String type = item.getType();
            String time = item.getTime();
            String combine = destination + type + time;
            boolean isNotify = notifyList.contains(combine);
            item.setNotify(isNotify);
        }
        notifyDataSetChanged();
    }
}