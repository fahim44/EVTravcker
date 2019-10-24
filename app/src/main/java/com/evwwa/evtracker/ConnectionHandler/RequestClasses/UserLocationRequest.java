package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by fahim on 3/8/18.
 */

public class UserLocationRequest extends Request {

    private String latitude,longitude,dateTime;

    public UserLocationRequest(Context context){
        super(context);
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getOptCode() {
        return "userlocation";
    }

    @Override
    public String getJSON() {
        try {
            JSONObject objx = new JSONObject();
            objx.put("lat", this.getLatitude());
            objx.put("long", this.getLongitude());
            objx.put("datetime", this.getDateTime());
            return objx.toString();
        }catch (Exception e){
            return "";
        }
    }
}
