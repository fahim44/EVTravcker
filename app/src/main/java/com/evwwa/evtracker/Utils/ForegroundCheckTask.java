package com.evwwa.evtracker.Utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by aiubian on 3/8/18.
 */

public class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Context... params) {
        final Context context = params[0];
        return isAppOnForeground(context);
    }

    private boolean isAppOnForeground(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}
