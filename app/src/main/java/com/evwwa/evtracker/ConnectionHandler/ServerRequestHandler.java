package com.evwwa.evtracker.ConnectionHandler;

import android.content.Context;

import com.evwwa.evtracker.ConnectionHandler.RequestClasses.AcceptTrackReqRequest;
import com.evwwa.evtracker.ConnectionHandler.RequestClasses.DisableTrackingRequest;
import com.evwwa.evtracker.ConnectionHandler.RequestClasses.FCMIdUpdateRequest;
import com.evwwa.evtracker.ConnectionHandler.RequestClasses.ForgetPasswordRequest;
import com.evwwa.evtracker.ConnectionHandler.RequestClasses.LoginRequest;
import com.evwwa.evtracker.ConnectionHandler.RequestClasses.SignUpRequest;
import com.evwwa.evtracker.ConnectionHandler.RequestClasses.TrackReqRequest;
import com.evwwa.evtracker.ConnectionHandler.RequestClasses.TrackingDataRequest;
import com.evwwa.evtracker.ConnectionHandler.RequestClasses.UserLocationRequest;
import com.evwwa.evtracker.ConnectionHandler.RequestClasses.WhoTrackMeRequest;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.AcceptTrackReqResponse;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.DisableTrackingResponse;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.FCMUpdateResponse;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.ForgetPasswordResponse;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.LoginResponse;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.SignUpResponse;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.TrackReqResponse;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.TrackingDataResponse;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.UserLocationResponse;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.WhoTrackMeResponse;
import com.evwwa.evtracker.Interfaces.ServerInterface;

/**
 * Created by fahim on 2/12/18.
 */

public class ServerRequestHandler {

    public static void SignUp(Context context, String email, String pass, String fullName, ServerInterface serverInterface){
        SignUpRequest req = new SignUpRequest(context);
        SignUpResponse rsp = new SignUpResponse();

        req.setUserName(email);
        req.setPassword(pass);
        req.setFullName(fullName);

        new ServerHandlerAsyncTask(context,req,rsp,serverInterface).execute();
    }

    public static void Login(Context context, String email, String pass, ServerInterface serverInterface){
        LoginRequest req = new LoginRequest(context);
        LoginResponse rsp = new LoginResponse();

        req.setUserName(email);
        req.setPassword(pass);

        new ServerHandlerAsyncTask(context,req,rsp,serverInterface).execute();
    }

    public static void UpdateFCMId(Context context, String id, ServerInterface serverInterface){
        FCMIdUpdateRequest req = new FCMIdUpdateRequest(context);
        FCMUpdateResponse rsp = new FCMUpdateResponse();

        req.setFcmId(id);

        new ServerHandlerAsyncTask(context,req,rsp,serverInterface).execute();
    }

    public static void TrackRequest(Context context, String request_userName, ServerInterface serverInterface){
        TrackReqRequest req = new TrackReqRequest(context);
        TrackReqResponse rsp = new TrackReqResponse();

        req.setRequested_userName(request_userName);

        new ServerHandlerAsyncTask(context,req,rsp,serverInterface).execute();
    }

    public static void AcceptTrackRequest(Context context, String accepted_userName, ServerInterface serverInterface){
        AcceptTrackReqRequest req = new AcceptTrackReqRequest(context);
        AcceptTrackReqResponse rsp = new AcceptTrackReqResponse();

        req.setAccepted_userName(accepted_userName);

        new ServerHandlerAsyncTask(context,req,rsp,serverInterface).execute();
    }

    public static void TrackingData(Context context,ServerInterface serverInterface){
        TrackingDataRequest req = new TrackingDataRequest(context);
        TrackingDataResponse rsp = new TrackingDataResponse();

        new ServerHandlerAsyncTask(context,req,rsp,serverInterface).execute();
    }

    public static void UserLocation(Context context,String latitude,String longitude, String dateTime, ServerInterface serverInterface){
        UserLocationRequest req = new UserLocationRequest(context);
        UserLocationResponse rsp = new UserLocationResponse();

        req.setLatitude(latitude);
        req.setLongitude(longitude);
        req.setDateTime(dateTime);

        new ServerHandlerAsyncTask(context,req,rsp,serverInterface).execute();
    }

    public static void WhoTrackMe(Context context,ServerInterface serverInterface){
        WhoTrackMeRequest req = new WhoTrackMeRequest(context);
        WhoTrackMeResponse rsp = new WhoTrackMeResponse();

        new ServerHandlerAsyncTask(context,req,rsp,serverInterface).execute();
    }

    public static void DisableTracking(Context context, String disable_track_email, ServerInterface serverInterface){
        DisableTrackingRequest req = new DisableTrackingRequest(context);
        DisableTrackingResponse rsp = new DisableTrackingResponse();

        req.setDisable_user_email(disable_track_email);

        new ServerHandlerAsyncTask(context,req,rsp,serverInterface).execute();
    }

    public static void ForgetPassword(Context context, String email, ServerInterface serverInterface){
        ForgetPasswordRequest req = new ForgetPasswordRequest(context);
        ForgetPasswordResponse rsp = new ForgetPasswordResponse();

        req.setEmail(email);

        new ServerHandlerAsyncTask(context,req,rsp,serverInterface).execute();
    }
}
