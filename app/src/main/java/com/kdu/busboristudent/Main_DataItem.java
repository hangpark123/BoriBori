package com.kdu.busboristudent;

import com.google.android.gms.maps.model.LatLng;

public class Main_DataItem {
    private String stationId;
    private String stationName;
    private String predictData;
    private String destination;
    public Main_DataItem(String stationId, String stationName, String predictData, String destination) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.predictData = predictData;
        this.destination = destination;
    }

    public String getStationId() {
        return stationId;
    }
    public String getStationName() {
        return stationName;
    }
    public String getPredictData() {
        return predictData;
    }
    public String getDestination() { return  destination; }
}