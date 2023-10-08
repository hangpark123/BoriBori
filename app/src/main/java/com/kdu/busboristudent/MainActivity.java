package com.kdu.busboristudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kdu.busbori.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Schedulebutton = findViewById(R.id.button_schedule);
        Button Campusbutton = findViewById(R.id.button_cam);
        Button bus701button = findViewById(R.id.button_701);
        Button bus21button = findViewById(R.id.button_21);
        Button bus733button = findViewById(R.id.button_733);
        Schedulebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new Fragment_schedule();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Campusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new Fragment_maps();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        bus701button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new Yangjumaps_701();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        bus21button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new Yangjumaps_21();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        bus733button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new Yangjumaps_733();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if (currentFragment instanceof Yangjumaps_701) {
            Yangjumaps_701 yangjumaps_701 = (Yangjumaps_701) currentFragment;
            if (yangjumaps_701.isSlidingPanelExpanded()) {
                yangjumaps_701.collapseSlidingPanel();
            } else {
                super.onBackPressed();
            }
        } else if (currentFragment instanceof Yangjumaps_733) {
            Yangjumaps_733 yangjumaps_733 = (Yangjumaps_733) currentFragment;
            if (yangjumaps_733.isSlidingPanelExpanded()) {
                yangjumaps_733.collapseSlidingPanel();
            } else {
                super.onBackPressed();
            }
        } else if (currentFragment instanceof Yangjumaps_21) {
            Yangjumaps_21 yangjumaps_21 = (Yangjumaps_21) currentFragment;
            if (yangjumaps_21.isSlidingPanelExpanded()) {
                yangjumaps_21.collapseSlidingPanel();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}