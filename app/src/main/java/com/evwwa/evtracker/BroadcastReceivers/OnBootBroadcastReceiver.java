package com.evwwa.evtracker.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.evwwa.evtracker.Utils.TaskUtils;

/**
 * Created by aiubian on 2/26/18.
 */

public class OnBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TaskUtils.checkAndStartBackgroundAlarm(context);
    }
}
