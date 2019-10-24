package com.evwwa.evtracker.ConnectionHandler.ResponseClasses;

import com.evwwa.evtracker.Utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aiubian on 3/8/18.
 */

public class TrackingDataResponse extends Response {
    Double latitude,longitude;
    String fullName,email, dateTime;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public void setJSON(String st) {
        try {
            JSONObject x = new JSONObject(st);
            this.setResultCode(x.getInt("result"));
            this.setLatitude(x.getDouble("latitude"));
            this.setLongitude(x.getDouble("longitude"));
            this.setFullName(x.getString("fullname"));
            this.setEmail(x.getString("email"));
            this.setDateTime(x.getString("datetime"));
        } catch (JSONException var4) {
            this.setResultCode(AppConstants.RESPONSE_FAILED_TO_CONVERT_FPOM_STRING_TO_JSON); //failed to convert
        }
    }
}
