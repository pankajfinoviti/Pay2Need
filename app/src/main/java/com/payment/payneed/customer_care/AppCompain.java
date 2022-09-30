package com.payment.payneed.customer_care;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.payment.payneed.R;
import com.payment.payneed.app.AppManager;
import com.payment.payneed.app.Constants;
import com.payment.payneed.network.RequestResponseLis;
import com.payment.payneed.network.VolleyNetworkCall;
import com.payment.payneed.utill.AppHandler;
import com.payment.payneed.utill.MyUtil;
import com.payment.payneed.utill.Print;

import java.util.HashMap;
import java.util.Map;

public class AppCompain extends AppCompatActivity implements RequestResponseLis {
    private EditText etSubject, etDes;
    private String PRODUCT, TRAN_ID, STATUS;
    private Button btnProceed;

    private void init() {
        etSubject = findViewById(R.id.etSubject);
        etDes = findViewById(R.id.etDes);
        btnProceed = findViewById(R.id.btnProceed);
        PRODUCT = getIntent().getStringExtra("product");
        TRAN_ID = getIntent().getStringExtra("tranId");
        STATUS = getIntent().getStringExtra("status");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.transparentStatusBar(this);
        setContentView(R.layout.activity_complain);
        init();

        btnProceed.setOnClickListener(v -> {
            if (etSubject.getText().toString().length() > 0) {
                if (MyUtil.isNN(TRAN_ID)) {
                    networkCallUsingVolleyApi(Constants.URL.COMPLAIN, param());
                }else {
                    networkCallUsingVolleyApi(Constants.URL.CONTACT_SUPPORT, paramSupport());
                }
            } else {
                etSubject.setError("Please enter subject");
            }
        });
    }

    private void networkCallUsingVolleyApi(String url, Map<String, String> map) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), true).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("subject", etSubject.getText().toString());
        if (MyUtil.isNN_ET(etDes))
            map.put("description", etDes.getText().toString());
        if (MyUtil.isNN(TRAN_ID))
            map.put("transaction_id", TRAN_ID);
        if (MyUtil.isNN(PRODUCT))
            map.put("product", PRODUCT);
        if (MyUtil.isNN(STATUS))
            map.put("status", STATUS);
        return map;
    }

    private Map<String, String> paramSupport() {
        Map<String, String> map = new HashMap<>();
        map.put("title", etSubject.getText().toString());
        if (MyUtil.isNN_ET(etDes))
            map.put("description", etDes.getText().toString());
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        String message = AppHandler.getMessage(JSonResponse);
        popUp(message);
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }

    private void popUp(String msg) {
        AlertDialog alert;
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this,
                R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Response")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    dialog.dismiss();
                    Constants.IS_RELOAD_REQUEST = true;
                    finish();
                });
        alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

}
