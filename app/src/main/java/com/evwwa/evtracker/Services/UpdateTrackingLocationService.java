package com.evwwa.evtracker.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.TrackingDataResponse;
import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.Utils.AppConstants;
import com.evwwa.evtracker.Utils.TaskUtils;

/**
 * Created by fahim on 27/5/18.
 */
public class UpdateTrackingLocationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    checkAndUpdateTrackingLocation();
                    try {
                        Thread.sleep(AppConstants.UPDATE_TRACKING_LOCATION_SERVICE_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void checkAndUpdateTrackingLocation(){
        ServerRequestHandler.TrackingData(getApplicationContext(), new ServerInterface() {
            @Override
            public void onResponse(Response res) {
                TrackingDataResponse rsp = (TrackingDataResponse)res;
                if(rsp.getResultCode()== AppConstants.RESPONSE_OK){
                    TaskUtils.saveTrackingDataToPref(getApplicationContext(),rsp);
                    sendBroadcast();
                }
            }
        });
    }

    private void sendBroadcast(){
        Intent intent = new Intent(AppConstants.UPDATE_TRACKING_DATA_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
