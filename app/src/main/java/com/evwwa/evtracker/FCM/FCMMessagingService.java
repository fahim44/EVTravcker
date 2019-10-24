package com.evwwa.evtracker.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.evwwa.evtracker.Main.MainActivity;
import com.evwwa.evtracker.R;
import com.evwwa.evtracker.Utils.AppConstants;
import com.evwwa.evtracker.Utils.TaskUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by fahim on 2/28/18.
 */

public class FCMMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
     //   Log.d("FCM", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
        //    Log.d("FCM", "Message data payload: " + remoteMessage.getData());

            String message = remoteMessage.getData().get("body");
            String user = "";
            AppConstants.FCM_TYPE type = AppConstants.FCM_TYPE.TRACK_REQUEST;
            if(message.contains(" wants to track you.")){
                user = message.replace(" wants to track you.","");
                type = AppConstants.FCM_TYPE.TRACK_REQUEST;
            }
            else if(message.contains("You are now tracking ")){
                user = message.replace("You are now tracking ","");
                type = AppConstants.FCM_TYPE.TRACK_ACCEPT;
            }

            boolean foreground = TaskUtils.isAppOnForeground(getApplicationContext());

            if(foreground){
                Intent intent = new Intent(AppConstants.FCM_TO_MAINACTIVITY);
                intent.putExtra(AppConstants.MESSAGE,message);
                intent.putExtra(AppConstants.USER,user);
                intent.putExtra(AppConstants.TYPE,type.ordinal());
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
            else {
                sendNotification(message,user,type);
            }

            /*if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/

        }

        // Check if message contains a notification payload.
       /* if (remoteMessage.getNotification() != null) {
            Log.d("FCM", "Message Notification Body: " + remoteMessage.getNotification().getBody());


        }*/

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }


    private void sendNotification(String messageBody, String user, AppConstants.FCM_TYPE type) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(AppConstants.MESSAGE,messageBody);
        intent.putExtra(AppConstants.USER,user);
        intent.putExtra(AppConstants.TYPE,type.ordinal());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "aa";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }



}
