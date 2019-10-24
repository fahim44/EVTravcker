package com.evwwa.evtracker.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.evwwa.evtracker.Utils.TaskUtils;
import com.evwwa.evtracker.Utils.UpdateLocation;

/**
 * Created by fahim on 2/15/18.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class UpdateLocationJobService extends JobService {


    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        new UpdateLocation(getApplicationContext()).getAndUpdateLocation();
        jobFinished(jobParameters,true);
        TaskUtils.checkAndStartBackgroundAlarm(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

}
