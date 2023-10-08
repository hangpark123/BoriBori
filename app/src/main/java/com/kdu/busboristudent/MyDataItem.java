package com.kdu.busboristudent;

import com.google.android.gms.maps.model.LatLng;

public class MyDataItem {

    private String stationId;
    private String stationName;
    private LatLng latLng;

    public MyDataItem(String stationId, String stationName, LatLng latLng) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.latLng = latLng;
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public LatLng getLatLng() {
        return latLng;
    }

}