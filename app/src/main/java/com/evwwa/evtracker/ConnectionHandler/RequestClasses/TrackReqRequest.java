package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by fahim on 3/8/18.
 */

public class TrackReqRequest extends Request {
    private String requested_userName;

    public TrackReqRequest(Context context) {
        super(context);
    }

    public String getRequested_userName() {
        return requested_userName;
    }

    public void setRequested_userName(String requested_userName) {
        this.requested_userName = requested_userName;
    }

    @Override
    public String getOptCode() {
        return "trackrequest";
    }

    @Override
    public String getJSON() {
        try {
            JSONObject objx = new JSONObject();
            objx.put("rusername", this.getRequested_userName());
            return objx.toString();
        }catch (Exception e){
            return "";
        }
    }
}
