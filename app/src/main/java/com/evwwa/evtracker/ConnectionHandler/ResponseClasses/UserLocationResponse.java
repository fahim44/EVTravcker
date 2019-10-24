package com.evwwa.evtracker.ConnectionHandler.ResponseClasses;

import com.evwwa.evtracker.Utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fahim on 3/8/18.
 */

public class UserLocationResponse extends Response {
    @Override
    public void setJSON(String st) {
        try {
            JSONObject x = new JSONObject(st);
            this.setResultCode(x.getInt("result"));
        } catch (JSONException var4) {
            this.setResultCode(AppConstants.RESPONSE_FAILED_TO_CONVERT_FPOM_STRING_TO_JSON); //failed to convert
        }
    }
}
