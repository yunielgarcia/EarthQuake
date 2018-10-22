package com.example.android.quakereport;

import java.text.DecimalFormat;

class EarthQuake {

    private Double mMagnitude;
    private String mLocation;
    private String mDate;
    private String mUrl;

    EarthQuake(Double mMagnitude, String mLocation, String mDate, String mUrl) {
        this.mMagnitude = mMagnitude;
        this.mLocation = mLocation;
        this.mDate = mDate;
        this.mUrl = mUrl;
    }

    String getmMagnitudeFormatted() {
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(mMagnitude);
        return output;
    }

    public Double getmMagnitude() {
        return mMagnitude;
    }

    String getmDate() {
        return mDate;
    }

    String getmLocation() {
        return mLocation;
    }

    public String getmUrl() {
        return mUrl;
    }
}
