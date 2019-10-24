package com.evwwa.evtracker.SignUp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.SignUpResponse;
import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.Main.MainActivity;
import com.evwwa.evtracker.R;
import com.evwwa.evtracker.Utils.AppConstants;
import com.evwwa.evtracker.Utils.TaskUtils;
import com.evwwa.evtracker.Utils.UserPrefernces;
import com.evwwa.evtracker.Utils.ViewUtils;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_email,et_pass,et_confirmPass,et_fullName;
    private CardView btn_signUp;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_confirmPass = findViewById(R.id.et_confirm_pass);
        et_fullName = findViewById(R.id.et_full_name);
        btn_signUp = findViewById(R.id.btn_signup);
        btn_signUp.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_signup){
            if(validInput()){
                signUp();
            }

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

    private void signUp(){
        dialog = ProgressDialog.show(this,"","signing up, Please wait...",true);

        ServerRequestHandler.SignUp(this, et_email.getText().toString(), et_pass.getText().toString(), et_fullName.getText().toString(), new ServerInterface() {
            @Override
            public void onResponse(Response res) {
                if(dialog != null){
                    dialog.cancel();
                }
                SignUpResponse response = (SignUpResponse) res;
                if(response.getResultCode()== AppConstants.RESPONSE_OK){
                    saveDataToPref(response);
                    TaskUtils.setFCMIdToServer(getApplicationContext());
                    ViewUtils.navigateToActivity(SignupActivity.this,MainActivity.class);
                }
                else {
                    Toast.makeText(SignupActivity.this,"Sign Up failed. Please try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveDataToPref(SignUpResponse response){
        UserPrefernces.setAccessToken(SignupActivity.this,response.getAccessToken());
        UserPrefernces.setEmail(SignupActivity.this,et_email.getText().toString());
        UserPrefernces.setFullName(SignupActivity.this,et_fullName.getText().toString());
    }


    private boolean validInput(){
        if(!TaskUtils.isEmpty(et_email.getText().toString()) && !TaskUtils.isEmpty(et_pass.getText().toString()) && !TaskUtils.isEmpty(et_confirmPass.getText().toString()) && !TaskUtils.isEmpty(et_fullName.getText().toString())){
            if(TaskUtils.isValidEmail(et_email.getText().toString())){
                if(et_pass.getText().toString().equals(et_confirmPass.getText().toString())){
                    return true;
                }
                else {
                    Toast.makeText(this,"Passwords don't match",Toast.LENGTH_LONG).show();
                    return false;
                }
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
