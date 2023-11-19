package com.kdu.busboristudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.kdu.busbori.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {
    private long backBtnTime = 0;
    private CircleIndicator3 indicator;
    private ViewPager2 viewPager2;
    private PagerAdapter viewPager2Adapter;
    private int currentViewPagerPage = 0;
    BasicAWSCredentials credentials = new BasicAWSCredentials("AKIATR4KVIIS74DNZLGD", "KAjjuxbXJE6n1lyeYTuBfikvjtUq8BkQhQ6BUQOB");
    AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(MainActivity.this, NotifyService.class);
        startForegroundService(serviceIntent);
        Button Schedulebutton = findViewById(R.id.button_schedule);
        Button Campusbutton = findViewById(R.id.button_cam);
        Button bus701button = findViewById(R.id.button_701);
        Button bus21button = findViewById(R.id.button_21);
        Button bus733button = findViewById(R.id.button_733);
        viewPager2Adapter = new PagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2 = findViewById(R.id.pager);
        viewPager2.setAdapter(viewPager2Adapter);
        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager2);

        ImageButton notify = findViewById(R.id.notify);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(Fragment_notify.class.getSimpleName());

                if (fragment == null) {
                    fragment = new Fragment_notify();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment, Fragment_notify.class.getSimpleName());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
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
            currentViewPagerPage = 0;
            Yangjumaps_701 yangjumaps_701 = (Yangjumaps_701) currentFragment;
            if (yangjumaps_701.isSlidingPanelExpanded()) {
                yangjumaps_701.collapseSlidingPanel();
            } else {
                super.onBackPressed();
                viewPager2.setAdapter(viewPager2Adapter);
                viewPager2.setCurrentItem(currentViewPagerPage);
            }
        } else if (currentFragment instanceof Yangjumaps_733) {
            currentViewPagerPage = 2;
            Yangjumaps_733 yangjumaps_733 = (Yangjumaps_733) currentFragment;
            if (yangjumaps_733.isSlidingPanelExpanded()) {
                yangjumaps_733.collapseSlidingPanel();
            } else {
                super.onBackPressed();
                viewPager2.setAdapter(viewPager2Adapter);
                viewPager2.setCurrentItem(currentViewPagerPage);
            }
        } else if (currentFragment instanceof Yangjumaps_21) {
            currentViewPagerPage = 1;
            Yangjumaps_21 yangjumaps_21 = (Yangjumaps_21) currentFragment;
            if (yangjumaps_21.isSlidingPanelExpanded()) {
                yangjumaps_21.collapseSlidingPanel();
            } else {
                super.onBackPressed();
                viewPager2.setAdapter(viewPager2Adapter);
                viewPager2.setCurrentItem(currentViewPagerPage);
            }
        } else if (currentFragment instanceof Fragment_schedule) {
            super.onBackPressed();
        } else if (currentFragment instanceof Fragment_maps) {
            super.onBackPressed();
        } else if (currentFragment instanceof Schedule_701) {
            super.onBackPressed();
        } else if (currentFragment instanceof Schedule_21) {
            super.onBackPressed();
        } else if (currentFragment instanceof Schedule_733) {
            super.onBackPressed();
        } else if (currentFragment instanceof Schedule_school) {
            super.onBackPressed();
        } else if (currentFragment instanceof Fragment_notify) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("경동셔틀").setMessage("경동셔틀 앱을 종료하시겠습니까?")
                    .setPositiveButton("종료", (dialogInterface, i) -> finish())
                    .setNegativeButton("취소", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create()
                    .show();
        }
    }
}