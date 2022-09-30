package com.payment.payneed.views.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.payneed.R;
import com.payment.payneed.app.AppManager;
import com.payment.payneed.app.Constants;
import com.payment.payneed.network.RequestResponseLis;
import com.payment.payneed.network.VolleyNetworkCall;
import com.payment.payneed.network.VolleyPostNetworkCall;
import com.payment.payneed.utill.AppHandler;
import com.payment.payneed.utill.MyUtil;
import com.payment.payneed.views.signup.model.SignupModel;
import com.payment.payneed.views.signup.model.SlugModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignupPageOne extends AppCompatActivity implements RequestResponseLis {
    private EditText etName;
    private EditText etMobile;
    private EditText etEmail;
    private EditText etShop;
    private EditText etPan;
    private String MOBILE_NUMBER;
    private Context context;
    ProgressDialog dialog;
    String slug = "";
    SignupModel model;
    private Spinner spnrSlug;
    private Button btnSignup;
    private String RolePrice = "";
    ArrayList<SlugModel> slugList;

    private void init() {
        context = SignupPageOne.this;
        etName = findViewById(R.id.etName);
        spnrSlug = findViewById(R.id.spnrSlug);
        btnSignup = findViewById(R.id.btnSubmit);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etShop = findViewById(R.id.etShop);
        etPan = findViewById(R.id.etPan);
        MOBILE_NUMBER = getIntent().getStringExtra("mobile");
        etMobile.setText(MOBILE_NUMBER);
        if (MOBILE_NUMBER != null && MOBILE_NUMBER.length() > 0) {
            etMobile.setFocusable(false);
        }
        model = new SignupModel();

        getRole();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_one_activity);

        init();

        btnSignup.setOnClickListener(v -> {
            if (AppManager.isOnline(SignupPageOne.this)) {
                setForm();
                if (!isValid()) return;

                Intent i = new Intent(this, SignupPageTwo.class);
                i.putExtra("data", model);
                i.putExtra("slug", slug);
                startActivity(i);
            } else {
                Toast.makeText(SignupPageOne.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

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
        if (!AppManager.isEmailValid(model.getEmail())) {
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

        return true;
    }

    private boolean isNN(String str) {
        boolean flag = false;
        if (str != null && str.length() > 0 && !str.equalsIgnoreCase("null")) {
            flag = true;
        }
        return flag;
    }

    public Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    private void networkCallUsingVolleyApi(String url, Map<String, String> param) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param, true).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private void setForm() {
        model.setName(etName.getText().toString());
        model.setMobile(etMobile.getText().toString());
        model.setEmail(etEmail.getText().toString());
        model.setShop(etShop.getText().toString());
        model.setPan(etPan.getText().toString());
        model.setSlug(slug);
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {

    }


    private void getRole() {

        new VolleyPostNetworkCall(new VolleyPostNetworkCall.RequestResponseLis() {
            @Override
            public void onSuccessRequest(String JSonResponse) {
                try {
                    JSONObject jsonObject =new JSONObject(JSonResponse);
                    JSONArray array  = jsonObject.getJSONArray("data");
                    intSlug(array);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailRequest(String msg) {

            }
        }, this, Constants.URL.GET_ROLE).netWorkCall(roleParam());


    }


    private void intSlug(JSONArray array) {
        slugList = new ArrayList();
        slugList.addAll(AppHandler.parseSlugList(array));
        slug = slugList.get(0).getSlug().toLowerCase(Locale.ROOT);
        ArrayList<String> list = new ArrayList();
        for (SlugModel data : slugList) {
            list.add(data.getName());
        }


        ArrayAdapter stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnrSlug.setAdapter(stateAdapter);

        spnrSlug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    slug = slugList.get(i).getSlug().toLowerCase(Locale.ROOT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private  Map<String, String> roleParam() {
        HashMap<String, String> map = new HashMap();
        map.put("type","register");
        return map;
    }



}
