package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by fahim on 2/13/18.
 */

public class LoginRequest extends Request {

    private String userName,password;

    public LoginRequest(Context context) {
        super(context);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getOptCode() {
        return "login";
    }

    @Override
    public String getJSON() {
        try {
            JSONObject objx = new JSONObject();
            objx.put("username", this.getUserName());
            objx.put("password", this.getPassword());
            return objx.toString();
        }catch (Exception e){
            return "";
        }
    }
}
