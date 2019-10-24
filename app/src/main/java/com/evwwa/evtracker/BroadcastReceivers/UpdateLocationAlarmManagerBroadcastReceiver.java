package com.evwwa.evtracker.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.evwwa.evtracker.Utils.UpdateLocation;

/**
 * Created by fahim on 2/26/18.
 */

public class UpdateLocationAlarmManagerBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new UpdateLocation(context).getAndUpdateLocation();
    }
}
