package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by aiubian on 3/8/18.
 */

public class FCMIdUpdateRequest extends Request {
    private String fcmId;

    public FCMIdUpdateRequest(Context context) {
        super(context);
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    @Override
    public String getOptCode() {
        return "fcmupdate";
    }

    @Override
    public String getJSON() {
        try {
            JSONObject objx = new JSONObject();
            objx.put("fcm", this.getFcmId());
            return objx.toString();
        }catch (Exception e){
            return "";
        }
    }
}
