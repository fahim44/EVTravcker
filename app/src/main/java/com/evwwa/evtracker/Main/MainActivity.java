package com.evwwa.evtracker.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.Interfaces.AlertDialogInterface;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.Launcher.LauncherActivity;
import com.evwwa.evtracker.Maps.MapsFragment;
import com.evwwa.evtracker.R;
import com.evwwa.evtracker.Services.UpdateTrackingLocationService;
import com.evwwa.evtracker.Status.StatusFragment;
import com.evwwa.evtracker.Track.TrackFragment;
import com.evwwa.evtracker.Utils.AppConstants;
import com.evwwa.evtracker.Utils.TaskUtils;
import com.evwwa.evtracker.Utils.UserPrefernces;
import com.evwwa.evtracker.Utils.ViewUtils;
import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tv_fullname, tv_email;
    private GoogleMap mMap;

    private NavigationView navigationView;
    private DrawerLayout drawer;

    private Intent updateTrackingLocationService = null;

    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        handleIntentData();

        setUpBroadcastReceiver();

        initializeDrawerHeader();

        ViewUtils.NavigateToFragmentClearPopUp(this,new MapsFragment());

        TaskUtils.checkAndStartBackgroundAlarm(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTrackingLocationService = new Intent(this, UpdateTrackingLocationService.class);
        startService(updateTrackingLocationService);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(updateTrackingLocationService != null) {
            stopService(updateTrackingLocationService);
            updateTrackingLocationService = null;
        }
    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(mainActivity_broadcastReceiver);
        TaskUtils.resetFullTrackingDataToNotAvailable(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            ViewUtils.exitApp(this);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id== R.id.nav_map){
            ViewUtils.NavigateToFragmentClearPopUp(this,new MapsFragment());
        }
        else if(id == R.id.nav_status){
            ViewUtils.NavigateToFragmentClearPopUp(this,new StatusFragment());
        }
        else if(id == R.id.nav_track){
            ViewUtils.NavigateToFragmentClearPopUp(this,new TrackFragment());
        }
        else if(id == R.id.nav_team){
            ViewUtils.NavigateToFragmentClearPopUp(this,new TrackFragment());
        }
        else if(id==R.id.nav_logout){
            ViewUtils.showAlertDialog(this,"",getString(R.string.logout_message),true,true,getString(R.string.yes),getString(R.string.no), new AlertDialogInterface() {
                @Override
                public void OnPositiveButtonClick() {
                    logout();
                }
                @Override
                public void OnNegativeButtonClick() {
                }
            });

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        UserPrefernces.clearAllData(this);
        ViewUtils.navigateToActivity(this, LauncherActivity.class);
    }

    private void initializeDrawerHeader(){
        View headerLayout = navigationView.getHeaderView(0);
        tv_fullname = headerLayout.findViewById(R.id.tv_fullname);
        tv_email = headerLayout.findViewById(R.id.tv_email);
        tv_fullname.setText(UserPrefernces.getFullName(this));
        tv_email.setText(UserPrefernces.getEmail(this));
    }


    private void setUpBroadcastReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.FCM_TO_MAINACTIVITY);
        intentFilter.addAction(AppConstants.UPDATE_VIEW_BROADCAST);
        localBroadcastManager.registerReceiver(mainActivity_broadcastReceiver,intentFilter);
    }

    private void handleIntentData(){
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            try{
                String message = intent.getStringExtra(AppConstants.MESSAGE);
                String user = intent.getStringExtra(AppConstants.USER);
                AppConstants.FCM_TYPE type = AppConstants.FCM_TYPE.values()[intent.getIntExtra(AppConstants.TYPE,0)];
                showTrackAlertDialog(message,user,type);
            }catch (Exception e){
            }
        }
    }

    private void showTrackAlertDialog(String message, final String user, final AppConstants.FCM_TYPE type){
        String posText=getString(R.string.ok),negText="";
        boolean negButton=false;
        if(type == AppConstants.FCM_TYPE.TRACK_REQUEST) {
            posText = getString(R.string.accept);
            negText = getString(R.string.decline);
            negButton = true;
        }

        ViewUtils.showAlertDialog(this, "", message, negButton, false, posText, negText, new AlertDialogInterface() {
            @Override
            public void OnPositiveButtonClick() {
                if(type == AppConstants.FCM_TYPE.TRACK_REQUEST){
                    ServerRequestHandler.AcceptTrackRequest(MainActivity.this, user, new ServerInterface() {
                        @Override
                        public void onResponse(Response res) {
                            UserPrefernces.setBossUser(MainActivity.this,user);
                            updateView();
                        }
                    });
                }
                else {
                    UserPrefernces.setTrackingUser(MainActivity.this,user);
                    updateView();
                }
            }

            @Override
            public void OnNegativeButtonClick() {

            }
        });

    }

    private void updateView(){
        if(navigationView.getMenu().findItem(R.id.nav_map).isChecked()){
            ViewUtils.NavigateToFragmentClearPopUp(this,new MapsFragment());
        }
        else if(navigationView.getMenu().findItem(R.id.nav_status).isChecked()){
            ViewUtils.NavigateToFragmentClearPopUp(this,new StatusFragment());
        }
        else if(navigationView.getMenu().findItem(R.id.nav_track).isChecked()){
            ViewUtils.NavigateToFragmentClearPopUp(this,new TrackFragment());
        }
    }

    private BroadcastReceiver mainActivity_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppConstants.FCM_TO_MAINACTIVITY)) {
                String message = intent.getStringExtra(AppConstants.MESSAGE);
                String user = intent.getStringExtra(AppConstants.USER);
                AppConstants.FCM_TYPE type = AppConstants.FCM_TYPE.values()[intent.getIntExtra(AppConstants.TYPE,0)];
                showTrackAlertDialog(message,user,type);
            }
            else if (intent.getAction().equals(AppConstants.UPDATE_VIEW_BROADCAST)) {
                updateView();
            }

        }
    };

}
