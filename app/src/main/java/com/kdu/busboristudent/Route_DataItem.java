package com.kdu.busboristudent;

import com.google.android.gms.maps.model.LatLng;

public class Route_DataItem {
    private String stationId;
    private String stationName;
    private LatLng latLng;
    private String turnYn;
    private String destination;
    private boolean isFavorite;
    public Route_DataItem(String stationId, String stationName, LatLng latLng, String turnYn, String destination) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.latLng = latLng;
        this.turnYn = turnYn;
        this.destination = destination;
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
    public String getturnYn() {
        return  turnYn;
    }
    public String getDestination() {
        return  destination;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

}