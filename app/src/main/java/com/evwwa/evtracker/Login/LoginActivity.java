package com.evwwa.evtracker.Login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.LoginResponse;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.ForgotPassword.ForgotPasswordActivity;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.Main.MainActivity;
import com.evwwa.evtracker.R;
import com.evwwa.evtracker.Utils.AppConstants;
import com.evwwa.evtracker.Utils.TaskUtils;
import com.evwwa.evtracker.Utils.UserPrefernces;
import com.evwwa.evtracker.Utils.ViewUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_email, et_pass;
    private CardView btn_login;
    private ProgressDialog dialog;

    private TextView tv_forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        tv_forgetPassword = findViewById(R.id.tv_forgetPassword);
        tv_forgetPassword.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_login){
            if(validInput()){
                login();
            }
        }
        else if(view.getId()==R.id.tv_forgetPassword){
            ViewUtils.navigateToActivity(this, ForgotPasswordActivity.class);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void login(){
        dialog = ProgressDialog.show(this,"","login, Please wait...",true);

        ServerRequestHandler.Login(this, et_email.getText().toString(), et_pass.getText().toString(), new ServerInterface() {
            @Override
            public void onResponse(Response res) {
                if(dialog != null){
                    dialog.cancel();
                }
                LoginResponse response = (LoginResponse) res;
                if(response.getResultCode()== AppConstants.RESPONSE_OK){
                    saveDataToPref(response);
                    TaskUtils.setFCMIdToServer(getApplicationContext());
                    ViewUtils.navigateToActivity(LoginActivity.this,MainActivity.class);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Login failed. Please try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveDataToPref(LoginResponse response){
        UserPrefernces.setAccessToken(LoginActivity.this,response.getAccessToken());
        UserPrefernces.setEmail(LoginActivity.this,response.getUserName());
        UserPrefernces.setFullName(LoginActivity.this,response.getFullName());
    }


    private boolean validInput(){
        if(!TaskUtils.isEmpty(et_email.getText().toString()) && !TaskUtils.isEmpty(et_pass.getText().toString())){
            if(TaskUtils.isValidEmail(et_email.getText().toString())){
                return true;
            }
            else {
                Toast.makeText(this,"Please Fill Up with valid email address",Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else {
            Toast.makeText(this,"Please Fill Up all fields with valid information",Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
