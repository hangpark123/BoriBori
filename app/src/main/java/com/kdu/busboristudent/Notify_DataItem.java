package com.kdu.busboristudent;

public class Notify_DataItem {
    private String assort;
    private String buttonText;
    private String destination;
    private String time;
    private String type;
    private boolean isNotify;
    public Notify_DataItem(String assort, String buttonText, String destination, String time, String type) {
        this.assort = assort;
        this.buttonText = buttonText;
        this.destination = destination;
        this.time = time;
        this.type = type;
    }

    public String getAssort() {
        return assort;
    }
    public String getButtonText() {
        return buttonText;
    }

    public String getDestination() {
        return destination;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
    public boolean isNotify() {
        return isNotify;
    }
    public void setNotify(boolean notify) {
        isNotify = notify;
    }
}