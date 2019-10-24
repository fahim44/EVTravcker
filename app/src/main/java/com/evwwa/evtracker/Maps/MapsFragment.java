package com.evwwa.evtracker.Maps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.TrackingDataResponse;
import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.R;
import com.evwwa.evtracker.Timer.BataTime;
import com.evwwa.evtracker.Timer.BataTimeCallback;
import com.evwwa.evtracker.Utils.AppConstants;
import com.evwwa.evtracker.Utils.TaskUtils;
import com.evwwa.evtracker.Utils.UserPrefernces;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

/**
 * Created by aiubian on 2/8/18.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;

    private MapView mapView;

    private double lat =0, lng=0;

    private FusedLocationProviderClient mFusedLocationClient;

    private Marker marker_tracked = null, marker_currentLoc = null;

    private boolean is_map_fagmentView_showing = false;

    LocalBroadcastManager localBroadcastManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.UPDATE_TRACKING_DATA_BROADCAST);
        localBroadcastManager.registerReceiver(broadcastReceiver_service_to_fragment,intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_maps, container, false);
        mapView = fragmentView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());



        return fragmentView;
    }




    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        is_map_fagmentView_showing = true;

        updateMapData();

      //  runUpdateViewTimer();
    }

    @Override
    public void onPause() {
        is_map_fagmentView_showing = false;
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver_service_to_fragment);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private void updateMapData(){
        if(mMap != null && is_map_fagmentView_showing) {
            getUserCurrentLocation();
            checkAndUpdateTrackingLocation();
        }
    }

    private void runUpdateViewTimer(){
        if(is_map_fagmentView_showing){
            new BataTime(1000 * 60).start(new BataTimeCallback() {
                @Override
                public void onUpdate(int elapsed) {
                }

                @Override
                public void onComplete() {
                    updateMapData();
                    runUpdateViewTimer();
                }
            });
        }
    }

///////////////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setTrafficEnabled(true);
        mMap.setInfoWindowAdapter(new EvwwaInfoWindowAdapter(getActivity()));

        updateMapData();

    }


////////////////////////////////
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

/////////////////////////
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


///////////////////////////////////
    private void getUserCurrentLocation(){
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

                                if(marker_currentLoc!= null){
                                    try {
                                        marker_currentLoc.remove();
                                    }catch (Exception e){}
                                }


                                marker_currentLoc =  mMap.addMarker(new MarkerOptions()
                                        .position(loc)
                                        .title(getContext().getString(R.string.you_are_here))
                                        .snippet(getStreetAddress(location.getLatitude(),location.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation)));
                                marker_currentLoc.showInfoWindow();

                                if(TaskUtils.isEmpty(UserPrefernces.getTrackingUser(getContext()))) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                                }
                            }
                        }
                    });
        }
        catch (SecurityException e){
        }
        catch (Exception e){}
    }

    private void checkAndUpdateTrackingLocation(){
        if(!TaskUtils.isFullTrackingDataAvailable(getContext())) {
            ServerRequestHandler.TrackingData(getContext(), new ServerInterface() {
                @Override
                public void onResponse(Response res) {
                    TrackingDataResponse rsp = (TrackingDataResponse) res;
                    if (rsp.getResultCode() == AppConstants.RESPONSE_OK) {
                        TaskUtils.saveTrackingDataToPref(getContext(),rsp);
                        updateTrackedMarker(rsp.getLatitude(),rsp.getLongitude(),rsp.getFullName(),rsp.getDateTime());
                    }
                }
            });
        }
        else {
            TrackingDataResponse rsp = TaskUtils.getTrackingDataFromPref(getContext());
            updateTrackedMarker(rsp.getLatitude(),rsp.getLongitude(),rsp.getFullName(),rsp.getDateTime());
        }
    }

    private void updateTrackedMarker(double latitude,double longitude,String name,String time){
        LatLng loc = new LatLng(latitude, longitude);
        if (marker_tracked != null) {
            try {
                marker_tracked.remove();
            } catch (Exception e) {
            }
        }

        String snippetMessage = TaskUtils.formatTimeIn12HourClock(time) + "\n\n" + getStreetAddress(latitude, longitude);

        marker_tracked = mMap.addMarker(new MarkerOptions().position(loc)
                .title(name)
                .snippet(snippetMessage));
        marker_tracked.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
    }


    private String getStreetAddress(Double lat, Double lon){
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getContext(), Locale.getDefault());

            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            StringBuilder stringBuilder = new StringBuilder("");

            if(addresses.get(0).getFeatureName() != null){
                stringBuilder.append(addresses.get(0).getFeatureName() + "\n");
            }

            stringBuilder.append("Address:\t");
            for(int i=0;i<=addresses.get(0).getMaxAddressLineIndex();i++){
                stringBuilder.append(addresses.get(0).getAddressLine(i));
            }

            stringBuilder.append("\nCity:\t");
            stringBuilder.append(addresses.get(0).getLocality());

            stringBuilder.append("\nState:\t");
            stringBuilder.append(addresses.get(0).getAdminArea());

            stringBuilder.append("\nCountry:\t");
            stringBuilder.append(addresses.get(0).getCountryName());

            stringBuilder.append("\nPostal Code:\t");
            stringBuilder.append(addresses.get(0).getPostalCode());

            return stringBuilder.toString();
        }catch (Exception e){
            return "";
        }
    }


    private BroadcastReceiver broadcastReceiver_service_to_fragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppConstants.UPDATE_TRACKING_DATA_BROADCAST)) {
                updateMapData();
            }
        }
    };


}
