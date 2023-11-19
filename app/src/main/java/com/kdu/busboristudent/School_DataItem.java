package com.kdu.busboristudent;

import com.google.android.gms.maps.model.LatLng;

public class School_DataItem {
    private String deviceId;
    private String destination;
    private String type;
    private String time;
    private String turnYn;
    private LatLng LatLng;
    private String Run;
    private String distance;
    private String distance_now;
    public School_DataItem(String deviceId, String destination, String type, String time, String turnYn, LatLng LatLng, String Run, String distance, String distance_now) {
        this.deviceId = deviceId;
        this.destination = destination;
        this.type = type;
        this.time = time;
        this.turnYn = turnYn;
        this.LatLng = LatLng;
        this.Run = Run;
        this.distance = distance;
        this.distance_now = distance_now;
    }

    public String getDeviceId() {
        return deviceId;
    }
    public String getDestination() {
        return  destination;
    }
    public String getType() {
        return  type;
    }
    public String getTime() {
        return time;
    }
    public String getTurnYn() {
        return  turnYn;
    }
    public LatLng getLatLng() {
        return LatLng;
    }
    public String getRun() {
        return Run;
    }
    public String getDistance() {
        return distance;
    }
    public String getDistance_now() {
        return distance_now;
    }
}