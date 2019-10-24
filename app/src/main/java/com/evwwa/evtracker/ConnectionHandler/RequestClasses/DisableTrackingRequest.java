package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by fahim on 3/22/18.
 */

public class DisableTrackingRequest extends Request {

    private String disable_user_email;

    public DisableTrackingRequest(Context context){
        super(context);
    }

    public String getDisable_user_email() {
        return disable_user_email;
    }

    public void setDisable_user_email(String disable_user_email) {
        this.disable_user_email = disable_user_email;
    }

    @Override
    public String getOptCode() {
        return "disabletracking";
    }

    @Override
    public String getJSON() {
        try {
            JSONObject objx = new JSONObject();
            objx.put("dtusername", this.getDisable_user_email());
            return objx.toString();
        }catch (Exception e){
            return "";
        }
    }
}
