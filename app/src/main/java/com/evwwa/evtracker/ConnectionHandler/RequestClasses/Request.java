package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

import com.evwwa.evtracker.Utils.AppConstants;
import com.evwwa.evtracker.Utils.UserPrefernces;

/**
 * Created by fahim on 2/11/18.
 */

public abstract class Request {

    public Request(Context context){
       setAccessToken(UserPrefernces.getAccessToken(context));
    }

    private String accessToken = "";

    public abstract String getOptCode();

    public abstract String getJSON();

    public String getURL(){
        return AppConstants.SERVER_URL;
    }

    public  String getAccessToken(){
        return accessToken;
    }

    public void setAccessToken(String token) {
        accessToken = token;
    }
}
