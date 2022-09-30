package com.payment.payneed.views.package_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.payment.payneed.R;
import com.payment.payneed.app.AppManager;
import com.payment.payneed.app.Constants;
import com.payment.payneed.commission.CommissionList;
import com.payment.payneed.network.PackageManagerNetworkCall;
import com.payment.payneed.network.RequestResponseLis;
import com.payment.payneed.network.VolleyNetworkCall;
import com.payment.payneed.utill.ParseApiData;
import com.payment.payneed.utill.Print;
import com.payment.payneed.utill.SharedPrefs;
import com.payment.payneed.views.package_manager.pkgmodel.ChargeCommissionModel;
import com.payment.payneed.views.package_manager.pkgmodel.ModifiedCommissionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommissionChargeListActivity extends AppCompatActivity implements RequestResponseLis, ComissionChargeAdapter.OnCommissionClickListener {

    RecyclerView chargeCommissionRecycler;
    ComissionChargeAdapter adapter;
    ArrayList<ChargeCommissionModel> list;
    String schemeId, title, status;
    TextView toolbar;
    Button updateBtn, closeBtn;
    ArrayList<ModifiedCommissionModel> modifiedList;
    private String apiCallType = "volleyNetworkCall";
    int REQUEST_CODE = 0;
    TextView noDataFoundTxt;

    private void init() {
        Bundle bundle = getIntent().getExtras();
        schemeId = bundle.getString("id");
        title = bundle.getString("title");
        status = bundle.getString("status");

        toolbar = findViewById(R.id.toolbar);
        updateBtn = findViewById(R.id.updateBtn);
        closeBtn = findViewById(R.id.closeBtn);
        noDataFoundTxt = findViewById(R.id.noDataFoundTxt);
        toolbar.setText(title + " Commission");

        chargeCommissionRecycler = findViewById(R.id.chargeCommissionRecycler);

        updateBtn.setOnClickListener(v -> {
            modifiedList = new ArrayList<>();
            for (ChargeCommissionModel data : list) {
                if (data.getModified()) {
                    modifiedList.add(new ModifiedCommissionModel(data.getSlab(), data.getType(), data.getValue()));
                }
            }
            REQUEST_CODE = 1;
            apiCallType = "packageManagerNetworkCall";
            networkCallUsingVolleyApi(Constants.URL.UPDATE_COMMISSION, true);
        });
        closeBtn.setOnClickListener(v -> {
            finish();
        });

    }

    private Map<String, Object> updateCommissionParam() {
        Map<String, Object> map = new HashMap<>();
        try {
            JSONArray modifiedArrayList = new JSONArray(new Gson().toJson(modifiedList));
            map.put("actiontype", "packagecommission");
            map.put("scheme_id", schemeId);
            map.put("user_id", SharedPrefs.getValue(this, SharedPrefs.USER_ID));
            map.put("data", modifiedArrayList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_charge);
        REQUEST_CODE = 0;
        init();
        networkCallUsingVolleyApi(Constants.URL.GET_PACKAGE_COMMISSION, true);

    }


    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {

            if (apiCallType.equalsIgnoreCase("packageManagerNetworkCall")) {
                new PackageManagerNetworkCall(this, this, url, 1, updateCommissionParam()).netWorkCall();
            } else {
                new VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
            }
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }


    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", SharedPrefs.getValue(this, SharedPrefs.USER_ID));
        map.put("actiontype", "package");
        map.put("id", schemeId);
        map.put("status", status);
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("On Success " + JSonResponse);
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            if (REQUEST_CODE == 0) {
                if (jsonObject.getString("statuscode").equalsIgnoreCase("TXN")) {
                    list = new ArrayList<>();
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONObject commissionObject = dataObject.getJSONObject("commission");
                    JSONArray array = new JSONArray();

                    if (title.equalsIgnoreCase("mobile recharge")) {
                        array = commissionObject.getJSONArray("mobile");
                    } else if (title.equalsIgnoreCase("dth recharge")) {
                        array = commissionObject.getJSONArray("dth");
                    } else if (title.equalsIgnoreCase("Electricity Bill")) {
                        array = commissionObject.getJSONArray("electricity");
                    } else if (title.equalsIgnoreCase("Pancard")) {
                        array = commissionObject.getJSONArray("pancard");
                    } else if (title.equalsIgnoreCase("Aeps")) {
                        array = commissionObject.getJSONArray("aeps");
                    } else if (title.equalsIgnoreCase("Money Transfer")) {
                        array = commissionObject.getJSONArray("dmt");
                    } else if (title.equalsIgnoreCase("Aadhaar")) {
                        array = commissionObject.getJSONArray("aadharpay");
                    }

                    list.addAll(ParseApiData.parseJsonArray(array, "ChargeCommissionModel"));
                    if (list.size()>0){

                        initRecycler();
                    }else{
                        updateBtn.setVisibility(View.GONE);
                        closeBtn.setVisibility(View.GONE);
                        noDataFoundTxt.setVisibility(View.VISIBLE);
                    }

                }
            }
            if (REQUEST_CODE == 1) {
                if (jsonObject.getString("statuscode").equalsIgnoreCase("TXN")) {
                    confirmationPopup(jsonObject.getString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailRequest(String msg) {
        Print.P("On Error " + msg);
    }

    private void initRecycler() {
        adapter = new ComissionChargeAdapter(this, list, this);
        chargeCommissionRecycler.setAdapter(adapter);
    }

    @Override
    public void updateCommission(int position) {


    }


    private void confirmationPopup(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.confirm_dialog, null);
        builder.setView(customLayout);
        Button viewCommissionBtn = customLayout.findViewById(R.id.viewCommissionBtn);
        TextView confirmTxt = customLayout.findViewById(R.id.confirmTxt);
        confirmTxt.setText(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        viewCommissionBtn.setOnClickListener(v -> {
            alertDialog.dismiss();
            Bundle bundle = new Bundle();
            bundle.putString("schemeId", schemeId);
            Intent intent = new Intent(this, CommissionList.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

        });

    }

}