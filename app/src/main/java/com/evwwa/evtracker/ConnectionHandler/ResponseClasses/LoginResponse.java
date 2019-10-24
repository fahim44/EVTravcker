package com.evwwa.evtracker.ConnectionHandler.ResponseClasses;

import com.evwwa.evtracker.Utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fahim on 2/13/18.
 */

public class LoginResponse extends Response {

    private String accessToken,userName,fullName;

    public String getAccessToken() {
        return accessToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    private void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public void setJSON(String st) {
        try {
            JSONObject x = new JSONObject(st);
            this.setResultCode(x.getInt("result"));
            this.setAccessToken(x.getString("accesstoken"));
            this.setUserName(x.getString("username"));
            this.setFullName(x.getString("fullname"));
        } catch (JSONException var4) {
            this.setResultCode(AppConstants.RESPONSE_FAILED_TO_CONVERT_FPOM_STRING_TO_JSON); //failed to convert
        }
    }
}
