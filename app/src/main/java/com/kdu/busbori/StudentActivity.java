package com.kdu.busbori;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class StudentActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        navigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, new Fragment_schedule())
                .commit();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.nav_schedule){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new Fragment_schedule())
                            .commit();
                } else if(itemId == R.id.nav_maps){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new Fragment_maps())
                            .commit();
                }
                return true;
            }
        });
    }
}