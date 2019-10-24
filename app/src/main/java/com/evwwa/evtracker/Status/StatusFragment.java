package com.evwwa.evtracker.Status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.WhoTrackMeResponse;
import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.R;
import com.evwwa.evtracker.Utils.AppConstants;
import com.evwwa.evtracker.Utils.TaskUtils;
import com.evwwa.evtracker.Utils.UserPrefernces;

/**
 * Created by fahim on 3/8/18.
 */

public class StatusFragment extends Fragment {

    TextView tv= null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWhoTrackMeInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_status, container, false);

        tv = fragmentView.findViewById(R.id.tv_email);

        if(!TaskUtils.isEmpty(UserPrefernces.getBossUser(getContext()))){
            tv.setText(UserPrefernces.getBossUser(getContext()));
        }
        return fragmentView;
    }

    private void getWhoTrackMeInfo(){
        ServerRequestHandler.WhoTrackMe(getContext(), new ServerInterface() {
            @Override
            public void onResponse(Response res) {
                WhoTrackMeResponse rsp = (WhoTrackMeResponse) res;
                if(rsp.getResultCode()== AppConstants.RESPONSE_OK){
                    UserPrefernces.setBossUser(getContext(),rsp.getEmail());
                    if(tv != null){
                        tv.setText(UserPrefernces.getBossUser(getContext()));
                    }
                }
            }
        });
    }

}
