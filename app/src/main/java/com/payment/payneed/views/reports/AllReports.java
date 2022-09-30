package com.payment.payneed.views.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.payneed.R;
import com.payment.payneed.dataModel.MainLocalData;
import com.payment.payneed.utill.RecyclerTouchListener;
import com.payment.payneed.utill.SharedPrefs;
import com.payment.payneed.views.pancards.PanCardStatement;
import com.payment.payneed.views.reports.adapter.VerticalReportListAdapter;

public class AllReports extends AppCompatActivity {

    private String TITLE = "";
    private RecyclerView rvReport;

    private void init() {
        TITLE = getIntent().getStringExtra("title");
        rvReport = findViewById(R.id.reportList);
        initToolbar();
    }

    private void initToolbar() {
        TextView tvTitle = findViewById(R.id.toolbarTitle);
        //tvTitle.setText(TITLE);
        ImageView imgBack = findViewById(R.id.imgClose);
        imgBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_report_list);
        init();
        gridInitReport();

    }
    String matm;
    private void gridInitReport() {
        LinearLayoutManager nearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReport.setLayoutManager(nearLayoutManager);
        rvReport.setItemAnimator(new DefaultItemAnimator());
        matm = SharedPrefs.getValue(this, SharedPrefs.MICRO_ATM_BALANCE);
        VerticalReportListAdapter reportAdapter = new VerticalReportListAdapter(AllReports.this, MainLocalData.getReportAllGridRecord(matm));
        rvReport.setAdapter(reportAdapter);
        rvReport.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                rvReport, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String v = MainLocalData.getReportAllGridRecord(matm).get(position).getTxt1();
                Intent intent= null;

                switch (v) {
                    case "Aeps Statement":
                        intent = new Intent(AllReports.this, AEPSTransaction.class);
                        intent.putExtra("title", "AEPS Transactions");
                        intent.putExtra("type", "aepsstatement");
                        startActivity(intent);
                        break;
                    case "Billpay Statement":
                        intent = new Intent(AllReports.this, BillRechargeTransaction.class);
                        intent.putExtra("title", "Billpay Statement");
                        intent.putExtra("type", "billpaystatement");
                        startActivity(intent);
                        break;
                    case "Money Transfer Statement":
                        intent = new Intent(AllReports.this, DMTTransactionReport.class);
                        intent.putExtra("type", "dmtstatement");
                        intent.putExtra("title", "DMT Statement");
                        startActivity(intent);
                        break;
                    case "Recharge Statement":
                        intent = new Intent(AllReports.this, BillRechargeTransaction.class);
                        intent.putExtra("type", "rechargestatement");
                        intent.putExtra("title", "Recharge Statement");
                        startActivity(intent);
                        break;
                    case "MATM Statement":
                        intent = new Intent(AllReports.this, AEPSTransaction.class);
                        intent.putExtra("type", "matmstatement");
                        intent.putExtra("title", "MATM Statement");
                        startActivity(intent);
                        break;
                    case "Main Wallet":
                        intent = new Intent(AllReports.this, WalletStatement.class);
                        intent.putExtra("type", "accountstatement");
                        intent.putExtra("title", "Main Wallet");
                        startActivity(intent);
                        break;
                    case "Aeps Wallet":
                        intent = new Intent(AllReports.this, AepsWalletStatement.class);
                        intent.putExtra("type", "awalletstatement");
                        intent.putExtra("title", "Aeps Wallet");
                        startActivity(intent);
                        break;
                    case "Matm Wallet":
                        intent = new Intent(AllReports.this, AepsWalletStatement.class);
                        intent.putExtra("type", "matmwalletstatement");
                        intent.putExtra("title", "MATM Wallet");
                        startActivity(intent);
                        break;
                    case "Wallet Fund Request":
                        intent = new Intent(AllReports.this, MainWalletFundReqStatement.class);
                        intent.putExtra("type", "fundrequest");
                        intent.putExtra("title", "Wallet Fund Request");
                        startActivity(intent);
                        break;
                    case "AEPS Fund Request":
                        intent = new Intent(AllReports.this, AEPSFundRequest.class);
                        intent.putExtra("type", "aepsfundrequest");
                        intent.putExtra("title", "AEPS Fund Request");
                        startActivity(intent);
                        break;

                    case "Matm Fund Request":
                        intent = new Intent(AllReports.this, AEPSFundRequest.class);
                        intent.putExtra("type", "matmfundrequest");
                        intent.putExtra("title", "MATM Settlement Report");
                        startActivity(intent);
                        break;
                    case "Pancard Statement":
                        intent = new Intent(AllReports.this, PanCardStatement.class);
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

}
