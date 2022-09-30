package com.payment.payneed.views.billpayment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.payment.payneed.R;
import com.payment.payneed.app.AppManager;
import com.payment.payneed.app.Constants;
import com.payment.payneed.databinding.SearchWithListActivityBinding;
import com.payment.payneed.network.RequestResponseLis;
import com.payment.payneed.network.VolleyNetworkCall;
import com.payment.payneed.utill.AppHandler;
import com.payment.payneed.utill.GRAdapter;
import com.payment.payneed.utill.MyUtil;
import com.payment.payneed.utill.Print;
import com.payment.payneed.views.billpayment.model.CircleModel;
import com.payment.payneed.views.browseplan.ViewPlans;
import com.payment.payneed.views.invoice.ReportInvoice;
import com.payment.payneed.views.invoice.model.InvoiceModel;
import com.payment.payneed.views.otpview.OTPPinReset;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobileRechargeAmountInput extends AppCompatActivity implements RequestResponseLis {
    public Context context;
    Dialog dialog;
    private TextView tvMobile;
    private TextView tvMobileCarrier, tvCircle;
    private TextView tvPlans, tvGenPin;
    private EditText etAmount, etTPin;
    private AppCompatButton btnProceed;
    private ImageView icBack, imgProvider;
    private String MOBILE = "", CARRIER_INFO = "", CARRIER_ID = "", URL = "";
    private String CircleId = "";
    private String Circle = "Select Circle";

    private void init() {
        context = MobileRechargeAmountInput.this;
        tvMobile = findViewById(R.id.tvMobile);
        etTPin = findViewById(R.id.etTPin);
        tvGenPin = findViewById(R.id.tvGenPin);
        imgProvider = findViewById(R.id.imgProvider);
        tvMobileCarrier = findViewById(R.id.tvMobileCarrier);
        tvPlans = findViewById(R.id.tvPlans);
        etAmount = findViewById(R.id.etAmount);
        btnProceed = findViewById(R.id.btnProceed);
        icBack = findViewById(R.id.icBack);
        tvCircle = findViewById(R.id.tvCircle);
        icBack.setOnClickListener(v -> finish());


        MOBILE = getIntent().getStringExtra("mobile");
        CARRIER_INFO = getIntent().getStringExtra("provider_name");
        URL = getIntent().getStringExtra("provider_image");
        CARRIER_ID = getIntent().getStringExtra("provider_id");

        tvMobile.setText(MOBILE);
        tvCircle.setText(Circle);
        tvMobileCarrier.setText(CARRIER_INFO);
        MyUtil.setProviderImage(URL, imgProvider, CARRIER_INFO, this, "mobile");

        tvCircle.setOnClickListener(v -> {
            getCircle(Constants.URL.GET_CIRCLE);
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.mobile_amount_enter_activity);
        init();


        tvPlans.setOnClickListener(v -> {
            if (tvCircle.getText().toString().equalsIgnoreCase("Select Circle")) {
                Toast.makeText(context, "Select Circle", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(MobileRechargeAmountInput.this, ViewPlans.class);
            i.putExtra("provider_id", CARRIER_ID);
            i.putExtra("provider_name", CARRIER_INFO);
            i.putExtra("mobile", MOBILE);
            i.putExtra("circle", Circle);
            i.putExtra("type", "mobile");
            startActivityForResult(i, 121);
        });

        btnProceed.setOnClickListener(v -> {
            EditText[] data = {etAmount, etTPin};
            if (AppHandler.editTextRequiredValidation(data)) {

                showLoader(this);

            }
        });

        tvGenPin.setOnClickListener(v -> startActivity(new Intent(this, OTPPinReset.class)));
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            Print.P("PARAM : " + new JSONObject(param()).toString());
            Print.P("URL : " + url);
            new VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("provider_id", CARRIER_ID);
        map.put("amount", etAmount.getText().toString());
        map.put("number", MOBILE);
        map.put("circle", Circle);
        map.put("pin", etTPin.getText().toString());
        return map;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 121:
                    if (data != null) {
                        String amount = data.getStringExtra("amount");
                        etAmount.setText(amount);
                        etAmount.setSelection(amount.length());
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            String message = jsonObject.getString("message");
            String txnId = jsonObject.getString("txnid");
            String rrn = jsonObject.getString("rrn");
            Constants.INVOICE_DATA = new ArrayList<>();
            Constants.INVOICE_DATA.add(new InvoiceModel("Txn Id", txnId));
            Constants.INVOICE_DATA.add(new InvoiceModel("RRN No", rrn));
            Constants.INVOICE_DATA.add(new InvoiceModel("Mobile Number", MOBILE));
            Constants.INVOICE_DATA.add(new InvoiceModel("Date", Constants.SHOWING_DATE_FORMAT.format(new Date())));
            Constants.INVOICE_DATA.add(new InvoiceModel("Provider ID", CARRIER_ID));
            Constants.INVOICE_DATA.add(new InvoiceModel("Provider Name", CARRIER_INFO));
            Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", "Bill Payment"));
            Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(this, etAmount.getText().toString())));
            Constants.INVOICE_DATA.add(new InvoiceModel("Status", "success"));
            Intent i = new Intent(this, ReportInvoice.class);
            i.putExtra("status", "success");
            i.putExtra("remark", "" + message);
            startActivity(i);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }

    @SuppressLint("SetTextI18n")
    private void showLoader(Activity context) {
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pay_confirmation_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        ImageView imgProvider = dialog.findViewById(R.id.imgProvider);
        MyUtil.setProviderImage(URL, imgProvider, CARRIER_INFO, this, "mobile");
        TextView tvMobile = dialog.findViewById(R.id.tvMobile);
        tvMobile.setText(MOBILE);
        TextView tvOperator = dialog.findViewById(R.id.tvOperator);
        String showData = CARRIER_INFO + "\n" + "Amount : " + etAmount.getText().toString();
        tvOperator.setText(showData);
        ImageView imgEdit = dialog.findViewById(R.id.imgEdit);
        Button cancelBtn = dialog.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(v -> closeLoader());
        imgEdit.setOnClickListener(v -> {
            closeLoader();
            finish();
        });
        Button confirmButton = dialog.findViewById(R.id.btnConfirm);
        confirmButton.setOnClickListener(v -> {
            closeLoader();
            networkCallUsingVolleyApi(Constants.URL.MOBILE_RECHARGE_PAY, true);
        });

        dialog.show();
    }

    private void closeLoader() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    private void getCircle(String url) {
        new VolleyNetworkCall(new RequestResponseLis() {
            @Override
            public void onSuccessRequest(String JSonResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(JSonResponse);
                    JSONArray jSONArray = jSONObject.getJSONArray("circles");
                    initCircleRv(new ArrayList<>(AppHandler.parseCicle(jSONArray)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailRequest(String msg) {

            }
        }, this, url, 1, param(), true).netWorkCall();
    }

    private void initCircleRv(List<CircleModel> data) {
        final List<CircleModel> dataList = new ArrayList<>(data);
        final List<CircleModel> dataListAll = new ArrayList<>(data);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        SearchWithListActivityBinding accLisBinding = SearchWithListActivityBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(accLisBinding.getRoot());
        bottomSheetDialog.show();
        GRAdapter<CircleModel> circleAdapter = new GRAdapter<>(dataList,
                R.layout.operator_bank_row, (itemView, item, position) -> {
            TextView tvTitle = itemView.findViewById(R.id.tvTitle);
            ImageView thumbnail = itemView.findViewById(R.id.thumbnail);
            thumbnail.setVisibility(View.GONE);
            tvTitle.setText(item.getName());
            itemView.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                Circle = item.getName();
//                CircleId = item.getCircle_code();
                tvCircle.setText("Circle - " + Circle);
            });
        });
        accLisBinding.listView.setLayoutManager(new LinearLayoutManager(this));
        accLisBinding.listView.setAdapter(circleAdapter);
        accLisBinding.icBack.setOnClickListener(v -> bottomSheetDialog.dismiss());
        accLisBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                dataList.clear();
                for (CircleModel d : dataListAll) {
                    if (d.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        dataList.add(d);
                    }
                }
                circleAdapter.notifyDataSetChanged();
            }
        });
    }

}
