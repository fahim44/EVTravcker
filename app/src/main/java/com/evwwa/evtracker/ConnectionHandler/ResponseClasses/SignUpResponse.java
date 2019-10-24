package com.evwwa.evtracker.ConnectionHandler.ResponseClasses;

import com.evwwa.evtracker.Utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fahim on 2/12/18.
 */

public class SignUpResponse extends Response {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    private void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void setJSON(String st) {
        try {
            JSONObject x = new JSONObject(st);
            this.setResultCode(x.getInt("result"));
            this.setAccessToken(x.getString("accesstoken"));
        } catch (JSONException var4) {
            this.setResultCode(AppConstants.RESPONSE_FAILED_TO_CONVERT_FPOM_STRING_TO_JSON); //failed to convert
        }
    }
}
