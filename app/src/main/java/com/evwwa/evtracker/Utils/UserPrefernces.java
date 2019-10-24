package com.evwwa.evtracker.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fahim on 2/12/18.
 */

public class UserPrefernces {

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(
                AppConstants.PREFERENCE, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getAutoBootSharedPreferences(Context context) {
        return context.getSharedPreferences(
                AppConstants.AUTOBOOT_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static String getAccessToken(Context context) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);
        return userSharedPreferences.getString(AppConstants.ACCESS_TOKEN, "");
    }

    public static void setAccessToken(Context context, String aToken) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(AppConstants.ACCESS_TOKEN, aToken);
        editor.apply();
    }


    public static String getEmail(Context context) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);
        return userSharedPreferences.getString(AppConstants.USER_EMAIL, "");
    }

    public static void setEmail(Context context, String aToken) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(AppConstants.USER_EMAIL, aToken);
        editor.apply();
    }

    public static String getFullName(Context context) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);
        return userSharedPreferences.getString(AppConstants.USER_FULLNAME, "");
    }

    public static void setFullName(Context context, String aToken) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(AppConstants.USER_FULLNAME, aToken);
        editor.apply();
    }

    public static void clearAllData(Context context){
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void setautobootpref(Context context) {
        SharedPreferences sharedPreferences = getAutoBootSharedPreferences(context);
        sharedPreferences.edit().putBoolean(AppConstants.AUTOBOOT, true).apply();
    }

    public static boolean getautobootpref(Context context) {
        SharedPreferences sharedPreferences = getAutoBootSharedPreferences(context);
        return sharedPreferences.getBoolean(AppConstants.AUTOBOOT, false);

    }


    public static String getFCMId(Context context) {
        SharedPreferences userSharedPreferences = getAutoBootSharedPreferences(context);
        return userSharedPreferences.getString(AppConstants.FCM_ID, "");
    }

    public static void setFCMId(Context context, String id) {
        SharedPreferences userSharedPreferences = getAutoBootSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(AppConstants.FCM_ID, id);
        editor.apply();
    }

    public static String getTrackingUser(Context context) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);
        return userSharedPreferences.getString(AppConstants.TRACKING_USER, "");
    }

    public static void setTrackingUser(Context context, String user) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(AppConstants.TRACKING_USER, user);
        editor.apply();
    }

    public static String getTrackingUserLatitude(Context context) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);
        return userSharedPreferences.getString(AppConstants.TRACKING_USER_LATITUDE, "");
    }


    public static void setTrackingUserLatitude(Context context, String lat) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(AppConstants.TRACKING_USER_LATITUDE, lat);
        editor.apply();
    }

    public static String getTrackingUserLongitude(Context context) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);
        return userSharedPreferences.getString(AppConstants.TRACKING_USER_LONGITUDE, "");
    }

    public static void setTrackingUserLongitude(Context context, String lon) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(AppConstants.TRACKING_USER_LONGITUDE, lon);
        editor.apply();
    }

    public static String getTrackingUserTime(Context context) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);
        return userSharedPreferences.getString(AppConstants.TRACKING_USER_TIME, "");
    }

    public static void setTrackingUserTime(Context context, String time) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(AppConstants.TRACKING_USER_TIME, time);
        editor.apply();
    }

    public static String getTrackingUserName(Context context) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);
        return userSharedPreferences.getString(AppConstants.TRACKING_USER_NAME, "");
    }

    public static void setTrackingUserName(Context context, String name) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(AppConstants.TRACKING_USER_NAME, name);
        editor.apply();
    }

    public static String getBossUser(Context context) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);
        return userSharedPreferences.getString(AppConstants.BOSS_USER, "");
    }

    public static void setBossUser(Context context, String user) {
        SharedPreferences userSharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(AppConstants.BOSS_USER, user);
        editor.apply();
    }


}
