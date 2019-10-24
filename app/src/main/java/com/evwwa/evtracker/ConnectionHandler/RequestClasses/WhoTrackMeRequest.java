package com.evwwa.evtracker.ConnectionHandler.RequestClasses;

import android.content.Context;

/**
 * Created by fahim on 3/21/18.
 */

public class WhoTrackMeRequest extends Request {

    public WhoTrackMeRequest(Context context){
        super(context);
    }

    @Override
    public String getOptCode() {
        return "whotrackme";
    }

    @Override
    public String getJSON() {
        return "";
    }
}
