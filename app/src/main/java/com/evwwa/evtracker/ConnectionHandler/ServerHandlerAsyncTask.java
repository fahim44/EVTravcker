/*
999 -> failed to retrieve
888 -> failed to json to string convert
 */


package com.evwwa.evtracker.ConnectionHandler;

import android.content.Context;
import android.os.AsyncTask;

import com.evwwa.evtracker.ConnectionHandler.RequestClasses.Request;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.Utils.AppConstants;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by fahim on 2/11/18.
 */

public class ServerHandlerAsyncTask extends AsyncTask<Void,Void,Response> {

    ServerInterface serverInterface;
    Request req;
    Response rsp;
    Context context;

    public ServerHandlerAsyncTask(Context context,Request req,Response rsp, ServerInterface serverInterface){
        this.context = context;
        this.req = req;
        this.rsp = rsp;
        this.serverInterface = serverInterface;
    }

    @Override
    protected Response doInBackground(Void... voids) {
       // if(TaskUtils.isInternetAvailable(context)){
           // Log.d("locationzz", "doinstart");
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mt = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(mt, req.getJSON());
                okhttp3.Request request = (new okhttp3.Request.Builder()).url(req.getURL()).addHeader("optcode", req.getOptCode()).addHeader("atoken", req.getAccessToken()).post(body).build();
                okhttp3.Response response = client.newCall(request).execute();
                rsp.setJSON(response.body().string());
            //    Log.d("locationzz", "doinend");
                return rsp;
            } catch (Exception e) {
                return null;
            }
      //  }


      //  cancel(true);
     //   return null;
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if(response != null){
            serverInterface.onResponse(response);
        }
        else {
            rsp.setResultCode(AppConstants.RESPONSE_FAILED_TO_RETRIEVE);  // failed to retrieve result
            serverInterface.onResponse(rsp);
        }
    }
}
