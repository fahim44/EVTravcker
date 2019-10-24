package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

/**
 * Created by aiubian on 3/8/18.
 */

public class TrackingDataRequest extends Request {

    public TrackingDataRequest(Context context){
        super(context);
    }

    @Override
    public String getOptCode() {
        return "trackingdata";
    }

    @Override
    public String getJSON() {
        return "";
    }
}
