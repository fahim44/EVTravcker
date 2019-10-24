package com.evwwa.evtracker.Interfaces;

import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;

/**
 * Created by fahim on 2/11/18.
 */

public interface ServerInterface {

    void onResponse(Response res);
}
