package com.payment.payneed.views.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.payneed.R;
import com.payment.payneed.app.AppManager;
import com.payment.payneed.app.Constants;
import com.payment.payneed.network.RequestResponseLis;
import com.payment.payneed.network.VolleyNetworkCall;
import com.payment.payneed.utill.AppHandler;
import com.payment.payneed.utill.MyUtil;
import com.payment.payneed.utill.Print;
import com.payment.payneed.views.otpview.OTPValidateAuth;
import com.payment.payneed.views.otpview.OTPValidateForgetPassword;
import com.payment.payneed.views.signup.SignupPageOne;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements RequestResponseLis {
    private EditText etUser, etPassword;
    private TextView tvForgot,tvSignup;
    private Context context;
    private Button btnLogin, imgPass;
    private int REQUEST_TYPE = 0;
    private boolean isPassword = true;

    private void init() {
        context = Login.this;
        etUser = findViewById(R.id.etUser);
//        imgPass = findViewById(R.id.imgPass);
        tvForgot = findViewById(R.id.tvForgot);
        btnLogin = findViewById(R.id.btnLogin);
        etPassword = findViewById(R.id.etPassword);
        tvSignup = findViewById(R.id.tvSignup);
    }
/*
    private void passwordVisibility() {
        if (isPassword) {
            isPassword = false;
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            imgPass.setImageDrawable(getDrawable(R.drawable.password_view));
        } else {
            isPassword = true;
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgPass.setImageDrawable(getDrawable(R.drawable.password_off));
        }*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.transparentStatusBar(this);
        setContentView(R.layout.login_design_one);
        init();

        btnLogin.setOnClickListener(v -> {
            if (isValid()) {
                REQUEST_TYPE = 0;
                networkCallUsingVolleyApi(Constants.URL.LOGIN, true);
            }
        });

        tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupPageOne.class));
        });

        tvForgot.setOnClickListener(v -> {
            if (isValidForgot()) {
                REQUEST_TYPE = 1;
                networkCallUsingVolleyApi(Constants.URL.PASSWORD_RESET_REQ, true);
            }
        });
//        imgPass.setOnClickListener(v -> passwordVisibility());
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", etUser.getText().toString());
        if (REQUEST_TYPE == 0)
            map.put("password", etPassword.getText().toString());
        return map;
    }

    private boolean isValid() {
        if (!MyUtil.isNN(etUser.getText().toString())) {
            Toast.makeText(this, "Please input user id", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(etPassword.getText().toString())) {
            Toast.makeText(this, "Please input password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidForgot() {
        if (!MyUtil.isNN(etUser.getText().toString())) {
            Toast.makeText(this, "Please input user id", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onSuccessRequest(String res) {
        try {
            String status = AppHandler.getStatus(res);
            String msg = AppHandler.getMessage(res);
            if (REQUEST_TYPE == 1) {
                Intent i = new Intent(this, OTPValidateForgetPassword.class);
                i.putExtra("USER_ID", etUser.getText().toString());
                startActivity(i);
            } else {
                if (status.equals("TXN")) {
                    AppHandler.parseAuthRes(etUser.getText().toString(), res, this);
                } else if (status.equalsIgnoreCase("OTP")) {
                    Intent i = new Intent(this, OTPValidateAuth.class);
                    i.putExtra("USER_ID", etUser.getText().toString());
                    i.putExtra("PASSWORD", etPassword.getText().toString());
                    i.putExtra("MSG", msg);
                    startActivity(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P("" + msg);
    }
}
