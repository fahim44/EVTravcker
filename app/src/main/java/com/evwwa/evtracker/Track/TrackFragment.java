package com.evwwa.evtracker.Track;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.Interfaces.AlertDialogInterface;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.R;
import com.evwwa.evtracker.Utils.AppConstants;
import com.evwwa.evtracker.Utils.TaskUtils;
import com.evwwa.evtracker.Utils.UserPrefernces;
import com.evwwa.evtracker.Utils.ViewUtils;

/**
 * Created by fahim on 3/8/18.
 */

public class TrackFragment extends Fragment implements View.OnClickListener {

    private ImageButton btn_cancel;
    private TextView tv;
    private ProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_track, container, false);

        btn_cancel = fragmentView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        tv = fragmentView.findViewById(R.id.tv_email);

        if(!TaskUtils.isEmpty(UserPrefernces.getTrackingUser(getContext()))){
            tv.setText(UserPrefernces.getTrackingUser(getContext()));
            btn_cancel.setVisibility(View.VISIBLE);
        }
        else {
            btn_cancel.setVisibility(View.GONE);
        }

        return fragmentView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_fragment_track,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_track_add){
            ViewUtils.show_track_email_alert_dialog(getActivity(), new ServerInterface() {
                @Override
                public void onResponse(Response res) {

                    Toast.makeText(getContext(),getString(R.string.track_req_send),Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_cancel){
            ViewUtils.showAlertDialog(getContext(),"",getString(R.string.stop_tracking_message)+ UserPrefernces.getTrackingUser(getContext()) + " ?",true,true,getString(R.string.yes),getString(R.string.no), new AlertDialogInterface() {
                @Override
                public void OnPositiveButtonClick() {
                    dialog = ProgressDialog.show(getContext(),"",getString(R.string.wait),true);

                    ServerRequestHandler.DisableTracking(getContext(), UserPrefernces.getTrackingUser(getContext()), new ServerInterface() {
                        @Override
                        public void onResponse(Response res) {
                            if(dialog != null){
                                dialog.cancel();
                            }

                            if(res.getResultCode()== AppConstants.RESPONSE_OK){
                                UserPrefernces.setTrackingUser(getContext(),"");
                                tv.setText("");
                                btn_cancel.setVisibility(View.GONE);

                                Toast.makeText(getContext(),getString(R.string.tracking_stopped),Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getContext(),getString(R.string.error_message),Toast.LENGTH_LONG).show();

                                /*Intent intent = new Intent(AppConstants.UPDATE_VIEW_BROADCAST);
                                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);*/

                            }
                        }
                    });
                }

                @Override
                public void OnNegativeButtonClick() {
                }
            });
        }
    }
}
