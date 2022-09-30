package com.payment.payneed.views.package_manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.payment.payneed.R;
import com.payment.payneed.app.AppManager;
import com.payment.payneed.app.Constants;
import com.payment.payneed.commission.CommissionList;
import com.payment.payneed.network.RequestResponseLis;
import com.payment.payneed.network.VolleyNetworkCall;
import com.payment.payneed.utill.AppHandler;
import com.payment.payneed.utill.ParseApiData;
import com.payment.payneed.utill.Print;
import com.payment.payneed.utill.SharedPrefs;
import com.payment.payneed.views.package_manager.pkgmodel.CommissionModel;
import com.payment.payneed.views.package_manager.pkgmodel.PackageComListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PackageManagerListActivity extends AppCompatActivity implements RequestResponseLis, PackageManagerAdapter.OnPackageListClickListener, ComissionPopupAdapter.OnCommissionClickListener {

    RecyclerView packageManagerRecycler;
    PackageManagerAdapter adapter;
    ArrayList<PackageComListModel> list;
    Map<String, String> map;
    String Rtype = "get";
    EditText dialogNameEt;
    SwitchCompat dialogStatus;
    String id = "", name, status;
    FloatingActionButton addPackageBtn;
    ArrayList<CommissionModel> commissionList;
    ArrayList<CommissionModel> chargeList;
    AlertDialog commissionDialog;
    String schemeId;


    private void init() {
        packageManagerRecycler = findViewById(R.id.packageManagerRecycler);
        addPackageBtn = findViewById(R.id.addPackageBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager_list);

        init();
        networkCallUsingVolleyApi(Constants.URL.PACKAGE_LIST_COMMISSION, true, param());

        addPackageBtn.setOnClickListener(v -> {
            Rtype = "add";
            PackageComListModel model = new PackageComListModel();
            openDialog(model);
        });
    }


    private void networkCallUsingVolleyApi(String url, boolean isLoad, Map<String, String> param) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param, isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }


    private Map<String, String> param() {
        map = new HashMap<>();
        map.put("user_id", SharedPrefs.getValue(this, SharedPrefs.USER_ID));
        if (Rtype.equalsIgnoreCase("get")) {
            map.put("actiontype", "packagecommission");
        }
        if (Rtype.equalsIgnoreCase("update")) {
            map.put("actiontype", "package");
            if (!id.isEmpty())
                map.put("id", id);
            if (!name.equalsIgnoreCase(dialogNameEt.getText().toString().trim()))
                map.put("name", dialogNameEt.getText().toString().trim());
            if (dialogStatus.isChecked()) {
                map.put("status", "1");
            } else {
                map.put("status", "0");
            }
        }
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("On Success " + JSonResponse);

        try {
            if (Rtype.equalsIgnoreCase("get")) {
                JSONObject jsonObject = new JSONObject(JSonResponse);
                if (AppHandler.getStatus(JSonResponse).equalsIgnoreCase("TXN")) {
                    list = new ArrayList<>();
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    list.addAll(ParseApiData.parseJsonArray(dataArray, "PackageComListModel"));
                    adapter = new PackageManagerAdapter(this, list, this);
                    packageManagerRecycler.setAdapter(adapter);
                }
            }

            if (Rtype.equalsIgnoreCase("update")) {
                Rtype = "get";
                networkCallUsingVolleyApi(Constants.URL.PACKAGE_LIST_COMMISSION, true, param());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P("On Failure " + msg);
    }

    @Override
    public void onEditBtnClicked(int position, PackageComListModel model) {
        openDialog(model);
    }


    @Override
    public void onViewCommissionClicked(int position, String schemeId) {
        Bundle bundle = new Bundle();
        bundle.putString("schemeId", schemeId);
        Intent intent = new Intent(this, CommissionList.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onChargeBtnClicked(int position, PackageComListModel model) {
        schemeId = model.getScheme();
        status = model.getStatus();
        openCommissionDialog();
    }


    private void openDialog(PackageComListModel data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.edit_package_dialog, viewGroup, false);
        builder.setView(dialogView);
        dialogNameEt = dialogView.findViewById(R.id.nameEt);
        dialogStatus = dialogView.findViewById(R.id.updateStatus);
        Button updateBtn = dialogView.findViewById(R.id.updateBtn);
        TextView title = dialogView.findViewById(R.id.title);
        TextView tvStatus = dialogView.findViewById(R.id.tvStatus);
        ImageView closeBtn = dialogView.findViewById(R.id.closeBtn);

        if (Rtype.equalsIgnoreCase("add")) {
            updateBtn.setText("Create");
            title.setText("Create Package");
            dialogStatus.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
        }
        dialogNameEt.setText(data.getName());
        schemeId = data.getScheme();
        id = data.getId();
        name = data.getName();
        status = data.getStatus();
        if (data.getStatus().equalsIgnoreCase("1")) {
            dialogStatus.setChecked(true);
        }else {
            dialogStatus.setChecked(false);
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        closeBtn.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        updateBtn.setOnClickListener(v -> {
            Rtype = "update";
            networkCallUsingVolleyApi(Constants.URL.UPDATE_PACKAGE, true, param());
            alertDialog.dismiss();
        });
    }

    private void openCommissionDialog() {
        ComissionPopupAdapter adapter1, adatper2;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.commission_dialog, viewGroup, false);
        builder.setView(dialogView);

        RecyclerView commissionRecycler = dialogView.findViewById(R.id.commissionRecycler);
        RecyclerView chargeRecycler = dialogView.findViewById(R.id.chargeRecycler);

        commissionList = new ArrayList<>();
        commissionList.add(new CommissionModel("Mobile Recharge"));
        commissionList.add(new CommissionModel("Dth Recharge"));
        commissionList.add(new CommissionModel("Electricity Bill"));
        commissionList.add(new CommissionModel("Pancard"));
        commissionList.add(new CommissionModel("Aeps"));
        initRecycler(commissionList, commissionRecycler);


        chargeList = new ArrayList<>();
        chargeList.add(new CommissionModel("Money Transfer"));
        chargeList.add(new CommissionModel("Aadhaar"));
        initRecycler(chargeList, chargeRecycler);


        commissionDialog = builder.create();
        commissionDialog.show();
    }

    private void initRecycler(ArrayList<CommissionModel> list, RecyclerView recyclerView) {
        ComissionPopupAdapter adapter = new ComissionPopupAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onCommissionItemClicked(int position, String title) {
        commissionDialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("id", schemeId);
        bundle.putString("status", status);
        bundle.putString("title", title);
        Intent intent = new Intent(this, CommissionChargeListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}