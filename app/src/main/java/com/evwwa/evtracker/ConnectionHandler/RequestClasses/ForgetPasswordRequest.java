package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by fahim on 3/27/18.
 */

public class ForgetPasswordRequest extends Request {
    private String email;


    public ForgetPasswordRequest(Context context){
        super(context);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getOptCode() {
        return "forgetpassword";
    }

    @Override
    public String getJSON() {
        try {
            JSONObject objx = new JSONObject();
            objx.put("email", this.getEmail());
            return objx.toString();
        }catch (Exception e){
            return "";
        }
    }
}
