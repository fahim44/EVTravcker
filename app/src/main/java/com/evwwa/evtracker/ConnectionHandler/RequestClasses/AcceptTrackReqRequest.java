package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by fahim on 3/8/18.
 */

public class AcceptTrackReqRequest extends Request {
    private String accepted_userName;

    public AcceptTrackReqRequest(Context context) {
        super(context);
    }

    public String getAccepted_userName() {
        return accepted_userName;
    }

    public void setAccepted_userName(String accepted_userName) {
        this.accepted_userName = accepted_userName;
    }

    @Override
    public String getOptCode() {
        return "trackaccept";
    }

    @Override
    public String getJSON() {
        try {
            JSONObject objx = new JSONObject();
            objx.put("atusername", this.getAccepted_userName());
            return objx.toString();
        }catch (Exception e){
            return "";
        }
    }
}
