package com.kdu.busbori;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class Fragment_schedule extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getChildFragmentManager().beginTransaction()
                .add(R.id.frame, new Schedule_dobong())
                .commit();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        Fragment schedule_dobong = new Schedule_dobong();
        Fragment schedule_yangju = new Schedule_yangju();
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

                    selected = schedule_dobong;

                }else if (position == 1){

                    selected = schedule_yangju;

                }else if (position == 2){

                    selected = schedule_701;

                }else if (position == 3){

                    selected = schedule_733;
                }else if (position == 4){

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

        /*navigationView = view.findViewById(R.id.schedule_bottomNavigationView);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.menu_dobong){
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.schedule_fragment, new Schedule_dobong())
                            .commit();
                } else if(itemId == R.id.menu_yangjoo){
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.schedule_fragment, new Schedule_yangju())
                            .commit();
                } else if(itemId == R.id.menu_701){
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.schedule_fragment, new Schedule_701())
                            .commit();
                } else if(itemId == R.id.menu_733){
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.schedule_fragment, new Schedule_733())
                            .commit();
                } else if(itemId == R.id.menu_21){
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.schedule_fragment, new Schedule_21())
                            .commit();
                }
                return true;
            }
        });

        return view;*/
    }
}