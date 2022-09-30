package com.payment.payneed.commission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payment.payneed.R;
import com.payment.payneed.app.AppManager;
import com.payment.payneed.app.Constants;
import com.payment.payneed.commission.gsonModel.CommissionModel;
import com.payment.payneed.network.RequestResponseLis;
import com.payment.payneed.network.VolleyNetworkCall;
import com.payment.payneed.utill.Print;
import com.payment.payneed.utill.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommissionList extends AppCompatActivity implements RequestResponseLis {
    ArrayList<String> keyList;
    ListView listView;
    private AlertDialog loaderDialog;
    private CommissionServiceAdapter adapter;
    String scheme_id;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commition_list);
        Constants.commissionDataList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        scheme_id = bundle.getString("schemeId");
        Print.P("Slug "+SharedPrefs.getValue(this, SharedPrefs.ROLE_SLUG));


        if (scheme_id.equalsIgnoreCase("getCommission")){
            url = Constants.URL.GET_COMMISSION;
        }else {
            url = Constants.URL.GET_PACKAGE_COMMISSION;
        }


        initListView();

        networkCallUsingVolleyApi(url, true);
    }

    private void initListView() {
        keyList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        adapter = new CommissionServiceAdapter(this, keyList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CommissionList.this, CommissionDataList.class);
                intent.putExtra("key", keyList.get(i));
                startActivity(intent);
            }
        });
    }

    private void closeLoader() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
        }
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1,param(),isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        if (!scheme_id.equalsIgnoreCase("getCommission")) {
            map.put("actiontype", "commission");
            map.put("user_id", SharedPrefs.getValue(this, SharedPrefs.USER_ID));
            map.put("scheme_id", scheme_id);
        }
        return map;
    }


    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("Success "+JSonResponse);
        Constants.commissionDataList.clear();
        keyList.clear();
        Gson gson = new GsonBuilder().create();
        closeLoader();
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONObject commissionObj = dataObject.getJSONObject("commission");
            Iterator<String> keys = commissionObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray keyArray = commissionObj.getJSONArray(key);
                for (int i = 0; i < keyArray.length(); i++) {
                    JSONObject obj = keyArray.getJSONObject(i);
                    CommissionModel model = gson.fromJson(obj.toString(), CommissionModel.class);
                    model.setKeys(key);
                    Constants.commissionDataList.add(model);
                }
                keyList.add(key);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        Toast.makeText(this, "Error: " + msg, Toast.LENGTH_SHORT).show();
    }
}