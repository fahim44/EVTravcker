package com.evwwa.evtracker.Maps;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.evwwa.evtracker.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by fahim on 3/27/18.
 */
public class EvwwaInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity activity;

    public EvwwaInfoWindowAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = activity.getLayoutInflater().inflate(R.layout.custom_info_contents,null);

        TextView tvTitle = (view.findViewById(R.id.title));
        tvTitle.setText(marker.getTitle());
        TextView tvSnippet = (view.findViewById(R.id.snippet));
        tvSnippet.setText(marker.getSnippet());

        return view;
    }
}
