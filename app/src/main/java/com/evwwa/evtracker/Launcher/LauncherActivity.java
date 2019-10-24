package com.evwwa.evtracker.Launcher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.evwwa.evtracker.Login.LoginActivity;
import com.evwwa.evtracker.Main.MainActivity;
import com.evwwa.evtracker.R;
import com.evwwa.evtracker.SignUp.SignupActivity;
import com.evwwa.evtracker.Utils.TaskUtils;
import com.evwwa.evtracker.Utils.UserPrefernces;
import com.evwwa.evtracker.Utils.ViewUtils;

/**
 * Created by aiubian on 2/8/18.
 */

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView btn_login,btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            /*for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("tap", "Key: " + key + " Value: " + value);
            }*/
        }

        ViewUtils.changeStatusBarColor(this);


        TaskUtils.checkAndApplyPermission(this);

        if(TaskUtils.isPermissionAllowed(this) && !TaskUtils.isEmpty(UserPrefernces.getAccessToken(this))){
            ViewUtils.navigateToActivity(this,MainActivity.class);
        }

        setContentView(R.layout.activity_launcher);

        btn_login =  findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        ViewUtils.exitApp(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(!TaskUtils.isPermissionAllowed(this)){
            Toast.makeText(this,"Permissions are required!!!",Toast.LENGTH_LONG).show();
            TaskUtils.checkAndApplyPermission(this);
        }
        else if(!TaskUtils.isEmpty(UserPrefernces.getAccessToken(this))){
            ViewUtils.navigateToActivity(this,MainActivity.class);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_login){
            ViewUtils.navigateToActivity(this,LoginActivity.class);
        }
        else if(view.getId()==R.id.btn_signup){
            ViewUtils.navigateToActivity(this,SignupActivity.class);
        }
        else if(view.getId()==R.id.tv_privacy_policy){
            ViewUtils.openPrivacyPolicyLink(this);
        }
    }


}
