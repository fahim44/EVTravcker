package com.evwwa.evtracker.ConnectionHandler.ResponseClasses;

import com.evwwa.evtracker.Utils.AppConstants;

/**
 * Created by fahim on 2/11/18.
 */

public abstract class Response {

    private int resultCode = AppConstants.RESPONSE_FAILED;

    public int getResultCode(){
        return resultCode;
    }

    public  void setResultCode(int i){
        resultCode = i;
    }

    public abstract void setJSON(String st);
}
