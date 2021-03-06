package com.example.mediraj.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediraj.R;
import com.example.mediraj.helper.ConnectionManager;
import com.example.mediraj.helper.Constant;
import com.example.mediraj.helper.DataManager;
import com.example.mediraj.helper.SessionManager;
import com.example.mediraj.model.UserData;
import com.example.mediraj.webapi.APiClient;
import com.example.mediraj.webapi.ApiInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.santalu.maskara.widget.MaskEditText;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getName();
    ApiInterface apiInterface;

    TextInputEditText pass;
    MaskEditText phone;
    MaterialButton loginBtn,signBtn;
    TextView forgotPass;
    String mobile=null,token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {

        //views
        phone = findViewById(R.id.userPhone);
        pass = findViewById(R.id.loginPass);
        loginBtn = findViewById(R.id.btnLogin);
        forgotPass = findViewById(R.id.forgotPass);
        signBtn = findViewById(R.id.signUpBtn);
        //api interface
        apiInterface = APiClient.getClient().create(ApiInterface.class);
        //listener for views
        loginBtn.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        signBtn.setOnClickListener(this);

        //token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    try {
                        token = task.getResult();
                        Log.e("device token",token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Are you sure you Want to exit?");
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        AlertDialog alert=alertDialog.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLogin) {
            validation();
        } else if (id == R.id.forgotPass) {
            startActivity(new Intent(this, ForgetPassActivity.class));
        } else if (id == R.id.signUpBtn) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        }
    }

    private void validation() {
        //extract phone number from field
        try {
            if (!phone.getText().toString().equals("") && phone.getText().length()==16) {
                String raw_phone = phone.getMasked().split(" ")[1];
                mobile = raw_phone.split("-")[0] + raw_phone.split("-")[1];
            }else {
                phone.setError(getString(R.string.userPhone_error_valid));
                phone.requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (phone.getText().toString().isEmpty()) {
            phone.setError(getString(R.string.userPhone_error));
            phone.requestFocus();
        }else if (mobile==null){
            phone.setError(getString(R.string.userPhone_error_valid));
            phone.requestFocus();
        }
        else if (mobile.length() !=11) {
            phone.setError(getString(R.string.userPhone_error_valid));
            phone.requestFocus();
        }else if (!mobile.startsWith("0")){
            phone.setError(getString(R.string.userPhone_error_number));
            phone.requestFocus();
        }
        else if (pass.getText().toString().isEmpty()) {
            pass.setError(getString(R.string.userPassword_error));
            pass.requestFocus();
        } else {
            if (ConnectionManager.connection(this)) {
                userLogin();
            }else {
                Toast.makeText(this,getString(R.string.internet_connect_msg),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void userLogin() {
        DataManager.getInstance().showProgressMessage(this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("mobile",phone.getText().toString());
        map.put("password",pass.getText().toString());
        map.put("token",token);

        Log.e(TAG,map.toString());

        Call<UserData> loginCall = apiInterface.userLogIn(Constant.AUTH,map);
        loginCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    UserData userData = response.body();
                    if (userData.response==200){
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e(TAG,"Login response : "+dataResponse);
                        SessionManager.writeString(LoginActivity.this,Constant.USER_INFO,dataResponse);
                        Toast.makeText(LoginActivity.this,userData.message,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, userData.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}