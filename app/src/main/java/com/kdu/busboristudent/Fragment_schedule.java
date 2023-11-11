package com.kdu.busboristudent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.kdu.busbori.R;

public class Fragment_schedule extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getChildFragmentManager().beginTransaction()
                .add(R.id.frame, new Schedule_school())
                .commit();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        Fragment schedule_school = new Schedule_school();
        Fragment schedule_701 = new Schedule_701();
        Fragment schedule_733 = new Schedule_733();
        Fragment schedule_21 = new Schedule_21();

        TabLayout tabs = view.findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0){

                    selected = schedule_school;

                }else if (position == 1){

                    selected = schedule_701;

                }else if (position == 2){

                    selected = schedule_733;

                }else if (position == 3){

                    selected = schedule_21;
                }

                getChildFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}