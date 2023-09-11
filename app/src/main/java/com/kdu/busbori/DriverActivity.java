package com.kdu.busbori;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class DriverActivity extends AppCompatActivity {
    private ToggleButton toggleButton;
    private boolean isServiceRunning = false;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        toggleButton = findViewById(R.id.toggle_gps);
        toggleButton.setChecked(isServiceRunning);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService();
                } else {
                    stopService();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isServiceRunning = sharedPreferences.getBoolean("serviceRunning", false);
        toggleButton.setChecked(isServiceRunning);
    }

    private void startService() {
        if (checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (checkBackgroundPermission()) {
                    if (!gpsrequest()) {
                        Intent serviceIntent = new Intent(DriverActivity.this, GPSservice.class);
                        startForegroundService(serviceIntent);
                        isServiceRunning = true;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("serviceRunning", isServiceRunning);
                        editor.apply();
                    }
                } else {
                    requestBackgroundPermission();
                    toggleButton.setChecked(false);
                }
            } else {
                Intent serviceIntent = new Intent(DriverActivity.this, GPSservice.class);
                startForegroundService(serviceIntent);

                isServiceRunning = true;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("serviceRunning", isServiceRunning);
                editor.apply();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermission();
                toggleButton.setChecked(false);
            }
        }
    }
    private boolean gpsrequest() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
            }
        });

        ((Task<?>) task).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                toggleButton.setChecked(false);
                if (e instanceof ResolvableApiException) {
                    Log.d(TAG, "OnFailure");
                    try {
                        ((ResolvableApiException) e).startResolutionForResult(
                                DriverActivity.this,
                                100
                        );
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.d(TAG, sendEx.getMessage().toString());
                    }
                }
            }
        });
        return false;
    }
    private void stopService() {
        Intent serviceIntent = new Intent(DriverActivity.this, GPSservice.class);
        stopService(serviceIntent);

        isServiceRunning = false;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("serviceRunning", isServiceRunning);
        editor.apply();
    }
    private boolean checkBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int backgroundPermissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            return backgroundPermissionCheck == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestBackgroundPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2);
        Toast.makeText(this, "백그라운드 위치 사용을 위해 권한 항상 허용, 정확한 위치 사용을 체크해 주시길 바랍니다.", Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }
}