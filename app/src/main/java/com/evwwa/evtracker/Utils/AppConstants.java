package com.evwwa.evtracker.Utils;

/**
 * Created by fahim on 2/11/18.
 */

public class AppConstants {

    public static final String SERVER_URL = "base_url/corporate_track/index.php";

    public static final String PRIVACY_POLICY_URL = "base_url/app/support/EVTrack.html";

    public static final String PREFERENCE = "evtracker_pref";
    public static final String AUTOBOOT_PREFERENCE = "evtracker_autoboot_pref";

    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_FULLNAME = "user_full_name";

    public static final String FCM_ID = "fcm_id";

    public static final String TRACKING_USER = "TRACKING_USER"; // CURRENT USER TRACKING THIS USER
    public static final String BOSS_USER = "BOSS_USER";  // WHO IS TRACKING CURRENT USER


    public static final String TRACKING_USER_LATITUDE = "TRACKING_USER_LATITUDE"; // CURRENT USER TRACKING THIS USER
    public static final String TRACKING_USER_LONGITUDE = "TRACKING_USER_LONGITUDE"; // CURRENT USER TRACKING THIS USER
    public static final String TRACKING_USER_TIME = "TRACKING_USER_TIME"; // CURRENT USER TRACKING THIS USER
    public static final String TRACKING_USER_NAME = "TRACKING_USER_NAME"; // CURRENT USER TRACKING THIS USER

    public static final String AUTOBOOT = "auto_boot";

    public static final int PERMISSION_REQUEST_CODE = 4;

    public static final int RESPONSE_OK = 1;
    public static final int RESPONSE_FAILED = 0;
    public static final int RESPONSE_FAILED_TO_RETRIEVE = 999;
    public static final int RESPONSE_FAILED_TO_CONVERT_FPOM_STRING_TO_JSON = 888;

    public static final int ALARM_ID = 234324243;
    public static final long ALARM_INTERVAL_IN_MILLISECOND = (1000 * 60 * 5);


    public static final String FCM_TO_MAINACTIVITY = "base_url.FCM_TO_MAINACTIVITY";
    public static final String UPDATE_VIEW_BROADCAST = "base_url.UPDATE_VIEW_BROADCAST";

    public static final String UPDATE_TRACKING_DATA_BROADCAST = "base_url.UPDATE_TRACKING_DATA_BROADCAST";

    public static final String MESSAGE = "message";
    public static final String USER = "user";
    public static final String TYPE = "type";

    public static final long UPDATE_TRACKING_LOCATION_SERVICE_INTERVAL = (1000 * 60);

    public enum FCM_TYPE {TRACK_REQUEST, TRACK_ACCEPT}
}
