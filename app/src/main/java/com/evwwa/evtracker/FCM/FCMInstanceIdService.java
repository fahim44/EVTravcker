package com.evwwa.evtracker.FCM;

import com.evwwa.evtracker.Utils.TaskUtils;
import com.evwwa.evtracker.Utils.UserPrefernces;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by fahim on 2/28/18.
 */

public class FCMInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       // Log.d("FCM", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);

        UserPrefernces.setFCMId(getApplicationContext(),refreshedToken);

        TaskUtils.setFCMIdToServer(getApplicationContext());
    }
}
