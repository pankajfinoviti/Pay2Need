package com.payment.payneed.views.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.payneed.R;
import com.payment.payneed.app.AppManager;
import com.payment.payneed.app.Constants;
import com.payment.payneed.network.RequestResponseLis;
import com.payment.payneed.network.VolleyNetworkCall;
import com.payment.payneed.utill.AppHandler;
import com.payment.payneed.utill.Print;
import com.payment.payneed.views.auth.Login;
import com.payment.payneed.views.signup.model.SignupModel;
import com.paysprint.onboardinglib.appvitals.AppConstants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignupPageTwo extends AppCompatActivity implements RequestResponseLis {
    private static int REQUEST_TYPE = 0;
    SignupModel model;
    private Button btnSignup;
    private EditText etAadhar;
    private EditText etState;
    private EditText etCity;
    private EditText etAddress;
    private EditText etPincode;
    private Context context;
    private int RequestType = 0;
    private AlertDialog loaderDialog;

    private void init() {
        context = SignupPageTwo.this;
        etPincode = findViewById(R.id.etPincode);
        etAadhar = findViewById(R.id.etAadhar);
        etState = findViewById(R.id.etState);
        etCity = findViewById(R.id.etCity);
        etAddress = findViewById(R.id.etAddress);
        btnSignup = findViewById(R.id.btnSubmit);
        model = getIntent().getParcelableExtra("data");

        setInitial();
    }

    private void setForm() {
        model.setPincode(etPincode.getText().toString());
        model.setAadhar(etAadhar.getText().toString());
        model.setState(etState.getText().toString());
        model.setCity(etCity.getText().toString());
        model.setAddress(etAddress.getText().toString());
    }

    private void setInitial() {
        try {
            if (isNN(model.getPincode())) etPincode.setText(model.getPincode());
            if (isNN(model.getAadhar())) etAadhar.setText(model.getAadhar());
            if (isNN(model.getState())) etState.setText(model.getState());
            if (isNN(model.getCity())) etCity.setText(model.getCity());
            if (isNN(model.getAddress())) etAddress.setText(model.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        if (!isNN(model.getName())) {
            Toast.makeText(context, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isNN(model.getMobile()) && model.getMobile().length() != 10) {
            Toast.makeText(context, "Please input valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isNN(model.getEmail())) {
            Toast.makeText(context, "Email id is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isEmailValid(model.getEmail())) {
            Toast.makeText(this, "Please input valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isNN(model.getShop())) {
            Toast.makeText(context, "Shop Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isNN(model.getPan())) {
            Toast.makeText(context, "Pancard Number is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isNN(model.getSlug())) {
            Toast.makeText(context, "Role Selection is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isNN(model.getAadhar()) && model.getAadhar().length() != 12) {
            Toast.makeText(context, "Please input valid aadhaar number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isNN(model.getState())) {
            Toast.makeText(context, "State Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isNN(model.getCity())) {
            Toast.makeText(context, "City is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isNN(model.getAddress())) {
            Toast.makeText(context, "Address is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isNN(model.getPincode()) && model.getPincode().length() != 6) {
            Toast.makeText(context, "Please input valid pincode", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isNN(String str) {
        boolean flag = false;
        if (str != null && str.length() > 0 && !str.equalsIgnoreCase("null")) {
            flag = true;
        }
        return flag;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity_page_two);
        init();
        btnSignup.setOnClickListener(v -> {
            setForm();
            if (AppManager.isOnline(SignupPageTwo.this)) {
                if (isValid()) {
                    RequestType = 2;
                    networkCallUsingVolleyApi(Constants.URL.USER_SIGNUP, param());
                }
            } else {
                Toast.makeText(SignupPageTwo.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void networkCallUsingVolleyApi(String url, Map<String, String> param) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param, true).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private void closeLoader() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
        }
    }

    public Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("name", model.getName());
        map.put("ispaymentenable", "1");
        map.put("mobile", model.getMobile());
        map.put("email", model.getEmail());
        map.put("shopname", model.getShop());
        map.put("pancard", model.getPan());
        map.put("aadharcard", model.getAadhar());
        map.put("state", model.getState());
        map.put("city", model.getCity());
        map.put("address", model.getCity());
        map.put("pincode", model.getPincode());
        map.put("slug", "retailer");
        Print.P("PARAM : " + new JSONObject(map));
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        closeLoader();
        String msg = AppHandler.getMessage(JSonResponse);
        confirmPopup(msg);
    }

    private void confirmPopup(String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "OK",
                (dialog, id) -> {
                    dialog.cancel();
                    Intent i = new Intent(context, Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(i);
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

}
