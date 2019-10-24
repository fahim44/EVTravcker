package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by fahim on 2/12/18.
 */

public class SignUpRequest extends Request {
    private String userName,password,fullName;

    public SignUpRequest(Context context) {
        super(context);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String getOptCode() {
        return "register";
    }

    @Override
    public String getJSON() {
        try {
            JSONObject objx = new JSONObject();
            objx.put("username", this.getUserName());
            objx.put("password", this.getPassword());
            objx.put("fullname", this.getFullName());
            return objx.toString();
        }catch (Exception e){
            return "";
        }
    }
}
