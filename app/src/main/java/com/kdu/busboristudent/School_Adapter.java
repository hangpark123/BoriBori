package com.kdu.busboristudent;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kdu.busbori.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class School_Adapter extends RecyclerView.Adapter<School_Adapter.ViewHolder> {
    private List<School_DataItem> School_dataList;
    private View.OnClickListener onItemClickListener;
    public School_Adapter(List<School_DataItem> School_dataList) {
        this.School_dataList = School_dataList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId = 0;
        switch (viewType) {
            case 0:
                layoutResId = R.layout.list_goingbus_item_layout_y;
                break;
            case 1:
                layoutResId = R.layout.list_goingbus_item_layout_n;
                break;
            case 2:
                layoutResId = R.layout.list_dropbus_item_layout_y;
                break;
            case 3:
                layoutResId = R.layout.list_dropbus_item_layout_n;
                break;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);

        return new ViewHolder(view);
    }
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        School_dataList = School_dataList.stream().distinct().collect(Collectors.toList());
        School_DataItem item = School_dataList.get(position);
        String destination = item.getDestination();
        String type = item.getType();
        String time = item.getTime();
        int max = Integer.parseInt(item.getDistance());
        int distance_now = Integer.parseInt(item.getDistance_now());

        holder.textView1.setText(type);
        holder.textView1_2.setText(destination);
        holder.textView1_3.setText(time);
        if (type.equals("등교")){
            holder.textView1.setTextColor(Color.parseColor("#009999"));
            holder.textView2.setText(destination);
            holder.textView3.setText("학교");
        }else {
            holder.textView1.setTextColor(Color.parseColor("#DF0101"));
            holder.textView2.setText("학교");
            holder.textView3.setText(destination);
        }
        holder.seekBar.setMax(max);
        holder.seekBar.setProgress(Math.min(distance_now, max));
    }

    @Override
    public int getItemCount() {
        return School_dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        School_DataItem item = School_dataList.get(position);
        String type = item.getType();
        String turnYn = item.getTurnYn();
        if (type.equals("등교")) {
            if (turnYn.equals("Y")){
                return 0;
            } else {
                return 1;
            }
        } else if (type.equals("하교")) {
            if (turnYn.equals("Y")){
                return 2;
            } else {
                return 3;
            }
        }
        return 4;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView1_2;
        public TextView textView1_3;
        public TextView textView2;
        public TextView textView3;
        public LinearLayout itemLayout;
        public SeekBar seekBar;

        @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textview1);
            textView1_2 = itemView.findViewById(R.id.textview1_2);
            textView1_3 = itemView.findViewById(R.id.textview1_3);
            textView2 = itemView.findViewById(R.id.textview2);
            textView3 = itemView.findViewById(R.id.textview3);
            itemLayout = itemView.findViewById(R.id.item_layout);
            seekBar = itemView.findViewById(R.id.seekBar);

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
            seekBar.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
    }
    public void setOnClickListener(View.OnClickListener listener) {
        onItemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateAdapterData(List<School_DataItem> newdataList) {
        this.School_dataList = newdataList;
        notifyDataSetChanged();
    }
}