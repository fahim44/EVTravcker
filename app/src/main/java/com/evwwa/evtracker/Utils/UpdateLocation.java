package com.evwwa.evtracker.Utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.Interfaces.ServerInterface;

/**
 * Created by fahim on 2/26/18.
 */

public class UpdateLocation implements LocationListener {

    Context context;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude,longitude;
    LocationManager locationManager;
    Location location;
    long notify_interval = 1000;

    public UpdateLocation(Context context){
     this.context = context;
    }

    public void getAndUpdateLocation(){
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnable && !isNetworkEnable) {

            } else {



                if (isGPSEnable) {
                    getGPSLocation();
                    if(location==null && isNetworkEnable){
                        getNetworkLocation();
                        if(location!=null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            updateLocation(location);
                        }
                    }
                    else if(location!=null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        updateLocation(location);
                    }
                    /*location = null;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            updateLocation(location);
                        }
                    }*/
                }

                else if (isNetworkEnable) {
                    getNetworkLocation();
                    if(location!=null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        updateLocation(location);
                    }
                    /*location = null;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            updateLocation(location);
                        }
                    }*/

                }

                locationManager.removeUpdates(this);
            }
        }catch (SecurityException e){}
        catch (Exception e){}

    }

    private Location getNetworkLocation() throws SecurityException{
        location = null;
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                return location;
            }
        }
        return null;
    }

    private Location getGPSLocation() throws SecurityException{
        location = null;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                return location;
            }
        }
        return null;
    }

    private void updateLocation(Location loc){
       // Toast.makeText(context,"lat="+ Double.toString(loc.getLatitude()) + " long="+ Double.toString(loc.getLongitude()),Toast.LENGTH_LONG).show();
        //Log.d("locationzz","lat="+ Double.toString(loc.getLatitude()) + " long="+ Double.toString(loc.getLongitude()));
        if(!TaskUtils.isEmpty(UserPrefernces.getAccessToken(context))) {
            ServerRequestHandler.UserLocation(context, Double.toString(loc.getLatitude()), Double.toString(loc.getLongitude()), TaskUtils.getCurrentTimeInString(), new ServerInterface() {
                @Override
                public void onResponse(Response res) {
                  //  Log.d("locationzz", Integer.toString(res.getResultCode()));
                }
            });
        }
    }

    ///////////////////////////////
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
