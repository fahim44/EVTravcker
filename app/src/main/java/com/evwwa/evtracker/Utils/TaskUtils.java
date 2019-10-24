package com.evwwa.evtracker.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.evwwa.evtracker.BroadcastReceivers.UpdateLocationAlarmManagerBroadcastReceiver;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.TrackingDataResponse;
import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.Services.UpdateLocationJobService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by aiubian on 2/8/18.
 */

public class TaskUtils {

    public static void checkAndApplyPermission(Activity activity) {
        if(!isPermissionAllowed(activity)){
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.WAKE_LOCK },
                    AppConstants.PERMISSION_REQUEST_CODE);
        }

        check_auto_start_permission(activity);

        startBatterySaverService(activity);
    }


    public static boolean isPermissionAllowed(Activity activity) {
        return ((ContextCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_NETWORK_STATE)==PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_WIFI_STATE)==PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(activity,Manifest.permission.INTERNET)==PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(activity,Manifest.permission.WAKE_LOCK)==PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                && isBatterySavverServiceDisable(activity));
    }

    private static void check_auto_start_permission(Activity activity){
        if(!UserPrefernces.getautobootpref(activity)){
            int brandno = get_brand_for_autostart();
            if(brandno > 0){
                ViewUtils.show_autostart_alert(activity,brandno);
            }
            UserPrefernces.setautobootpref(activity);
        }
    }

    private static int get_brand_for_autostart() {
        /* 1 = xiaomi
        2 = letv
        3 = huawie
         */
        if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
            return 1;
        } else if (Build.BRAND.equalsIgnoreCase("Letv")) {
            return 2;
        } else if (Build.BRAND.equalsIgnoreCase("Honor")) {
            return 3;
        }
        return 0;
    }

    public static void start_autostart_activity(Context context, int brand){
        try {
            Intent intent = new Intent();
            if (brand == 1)
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            else if (brand == 2)
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            else if (brand == 3)
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));

            if (isCallableIntent(context, intent))
                context.startActivity(intent);
            else
                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        }catch (Exception e){
            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        }
    }

    private static boolean isCallableIntent(Context context,Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static boolean isInternetAvailable(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    public static boolean isEmpty(String s){
        String st = s.trim();
        st = st.replace(" ","");
        if(st==null)
            return true;
        else if(st.equalsIgnoreCase("null"))
            return true;
        else if(st.equalsIgnoreCase(""))
            return true;
        return st.isEmpty();
    }

    public static boolean isValidEmail(String target) {
        return !isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void checkAndStartBackgroundAlarm(Context context){
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(!isJobServiceOn(context))
                scheduleJob(context);
        }
        else {
            if(!isAlarmServiceOn(context))
                scheduleAlarm(context);
        }*/
        //if(!isAlarmServiceOn(context))
            scheduleAlarm(context);
    }

    private static void scheduleAlarm(Context context){
        Intent intent = new Intent(context, UpdateLocationAlarmManagerBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, AppConstants.ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AppConstants.ALARM_INTERVAL_IN_MILLISECOND, pendingIntent);
    }

    private static boolean isAlarmServiceOn( Context context ){
        Intent intent = new Intent(context, UpdateLocationAlarmManagerBroadcastReceiver.class);
        return  (PendingIntent.getBroadcast(context, AppConstants.ALARM_ID,
                intent,
                PendingIntent.FLAG_NO_CREATE) != null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, UpdateLocationJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(AppConstants.ALARM_ID, serviceComponent);
        builder.setMinimumLatency(AppConstants.ALARM_INTERVAL_IN_MILLISECOND); // wait at least
        builder.setOverrideDeadline(AppConstants.ALARM_INTERVAL_IN_MILLISECOND); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresDeviceIdle(false); // device should be idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        builder.setPersisted(true);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());

        /*JobScheduler jobScheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(context, UpdateLocationJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName).setPeriodic(10 * 10000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setRequiresCharging(false)
                .setMinimumLatency(10 * 1000)
                .setRequiresDeviceIdle(false)
                .build();
        jobScheduler.schedule(jobInfo);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static boolean isJobServiceOn( Context context ) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        boolean hasBeenScheduled = false ;

        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == AppConstants.ALARM_ID ) {
                hasBeenScheduled = true ;
                break ;
            }
        }

        return hasBeenScheduled ;
    }

    /*synchronized private void stopAlarmManager(Activity activity) {
        if(Build.VERSION.SDK_INT>=21){
            JobScheduler jobScheduler = (JobScheduler) Application.getInstance().getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.cancelAll();
        }else {
            Intent myIntent = new Intent(activity, AlarmManagerForPlannedRoute.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }*/


    public static void getCurrentLocation(Context context, Activity activity, OnSuccessListener<Location> onSuccessListener){
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, onSuccessListener);
        }
        catch (SecurityException e){
        }
        catch (Exception e){}
    }

    public static void setFCMIdToServer(Context context){
        if(!isEmpty(UserPrefernces.getAccessToken(context)) && !isEmpty(UserPrefernces.getFCMId(context))){
            ServerRequestHandler.UpdateFCMId(context, UserPrefernces.getFCMId(context), new ServerInterface() {
                @Override
                public void onResponse(Response res) {
               //     Log.d("FCM","FCMId to server: result code:" + Integer.toString(res.getResultCode()));
                }
            });
        }
    }

    public static boolean isAppOnForeground(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public static String getCurrentTimeInString(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        return dateFormat.format(date);
    }

    private static void startBatterySaverService(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                context.startActivity(intent);
            }

        }
    }

    private static boolean isBatterySavverServiceDisable(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                return false;
            }

        }
        return true;
    }

    public static String formatTimeIn12HourClock(String time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.US);
            Date date = sdf.parse(time);
            sdf = new SimpleDateFormat("hh:mm a, dd/MM/yyyy",Locale.US);
            return sdf.format(date);
        }catch (ParseException e) {
            return "";
        }
    }


    public static void saveTrackingDataToPref(Context context,TrackingDataResponse rsp){
        UserPrefernces.setTrackingUser(context,rsp.getEmail());
        UserPrefernces.setTrackingUserLatitude(context,Double.toString(rsp.getLatitude()));
        UserPrefernces.setTrackingUserLongitude(context,Double.toString(rsp.getLongitude()));
        UserPrefernces.setTrackingUserTime(context,rsp.getDateTime());
        UserPrefernces.setTrackingUserName(context,rsp.getFullName());
    }

    public static TrackingDataResponse getTrackingDataFromPref(Context context){
        TrackingDataResponse rsp = new TrackingDataResponse();
        rsp.setEmail(UserPrefernces.getTrackingUser(context));
        rsp.setFullName(UserPrefernces.getTrackingUserName(context));
        rsp.setDateTime(UserPrefernces.getTrackingUserTime(context));
        rsp.setLatitude(Double.parseDouble(UserPrefernces.getTrackingUserLatitude(context)));
        rsp.setLongitude(Double.parseDouble(UserPrefernces.getTrackingUserLongitude(context)));
        return rsp;
    }

    public static boolean isFullTrackingDataAvailable(Context context){
        return !isEmpty(UserPrefernces.getTrackingUserLatitude(context));
    }

    public static void resetFullTrackingDataToNotAvailable(Context context){
        UserPrefernces.setTrackingUserLatitude(context,"");
    }

}
