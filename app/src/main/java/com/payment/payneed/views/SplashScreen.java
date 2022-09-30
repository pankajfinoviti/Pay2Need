package com.payment.payneed.views;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.payneed.MainActivity;
import com.payment.payneed.R;
import com.payment.payneed.permission.AppPermissions;
import com.payment.payneed.permission.PermissionHandler;
import com.payment.payneed.utill.MyUtil;
import com.payment.payneed.utill.PrefLoginManager;
import com.payment.payneed.utill.SharedPrefs;
import com.payment.payneed.utill.ThemeHelper;
import com.payment.payneed.views.auth.Login;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        String ui = SharedPrefs.getValue(this, SharedPrefs.APP_THEAM);
        if (MyUtil.isNN(ui))
            ThemeHelper.applyTheme(ui);

        startLoader(400);

    }

    Thread splashTimer;

    private void startLoader(final int maxTime) {
        splashTimer = new Thread() {
            public void run() {
                try {
                    int splashTime = 0;
                    int selector = 1;
                    while (splashTime < maxTime) {
                        Thread.sleep(700);
                        splashTime = splashTime + 100;
                    }

                    openActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        splashTimer.start();
    }

    private void openActivity() {
        PrefLoginManager pref = new PrefLoginManager(this);
        String status = pref.getFarmerLoginRes();
        if (status != null && status.length() > 0) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startLoginWithPerm();
        }
        finish();
    }

    private void startLoginWithPerm() {
        String[] permissions = {Manifest.permission.READ_PHONE_STATE};
        AppPermissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                startActivity(new Intent(SplashScreen.this, Login.class));
            }
        });
    }
}
