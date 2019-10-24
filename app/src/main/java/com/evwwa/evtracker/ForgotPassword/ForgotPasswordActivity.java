package com.evwwa.evtracker.ForgotPassword;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.evwwa.evtracker.ConnectionHandler.ResponseClasses.Response;
import com.evwwa.evtracker.ConnectionHandler.ServerRequestHandler;
import com.evwwa.evtracker.Interfaces.ServerInterface;
import com.evwwa.evtracker.R;
import com.evwwa.evtracker.Utils.AppConstants;
import com.evwwa.evtracker.Utils.TaskUtils;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_email;
    private CardView btn_ok;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        et_email = findViewById(R.id.et_email);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_ok){
            if(validInput()){
                forgetPassword();
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

    private void forgetPassword(){
        dialog = ProgressDialog.show(this,"","Please wait...",true);

        ServerRequestHandler.ForgetPassword(this, et_email.getText().toString(), new ServerInterface() {
            @Override
            public void onResponse(Response res) {
                if(dialog != null){
                    dialog.cancel();
                }
                if(res.getResultCode()== AppConstants.RESPONSE_OK){
                    Toast.makeText(ForgotPasswordActivity.this,"Please check your email",Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this,"Please try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validInput(){
        if(!TaskUtils.isEmpty(et_email.getText().toString())){
            if(TaskUtils.isValidEmail(et_email.getText().toString())){
                return true;
            }
        }
        Toast.makeText(this,"Please Fill Up with valid email address",Toast.LENGTH_LONG).show();
        return false;
    }
}
