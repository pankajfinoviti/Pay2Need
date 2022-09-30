package com.payment.payneed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.payment.payneed.adapter.HeaderAdapter;
import com.payment.payneed.adapter.HomeAdapter;
import com.payment.payneed.adapter.MyCustomPagerAdapter;
import com.payment.payneed.app.AppManager;
import com.payment.payneed.app.Constants;
import com.payment.payneed.commission.CommissionList;
import com.payment.payneed.dataModel.MainLocalData;
import com.payment.payneed.model.ActivityListModel;
import com.payment.payneed.network.RequestResponseLis;
import com.payment.payneed.network.VolleyNetworkCall;
import com.payment.payneed.permission.AppPermissions;
import com.payment.payneed.permission.PermissionHandler;
import com.payment.payneed.utill.AepsAppHelper;
import com.payment.payneed.utill.MarqueeTextView;
import com.payment.payneed.utill.MyUtil;
import com.payment.payneed.utill.Print;
import com.payment.payneed.utill.RecyclerTouchListener;
import com.payment.payneed.utill.SharedPrefs;
import com.payment.payneed.views.AllServices;
import com.payment.payneed.views.BankAccountPage;
import com.payment.payneed.views.ProfilePage;
import com.payment.payneed.views.SplashScreen;
import com.payment.payneed.views.allservices_search.AllServicesSearch;
import com.payment.payneed.views.billpayment.RechargeAndBillPayment;
import com.payment.payneed.views.member.MemberListAll;
import com.payment.payneed.views.mhagram_aeps_matm.HandlerActivity;
import com.payment.payneed.views.moneytransfer.DMTSearchAccount;
import com.payment.payneed.views.package_manager.PackageManagerListActivity;
import com.payment.payneed.views.pancards.PanCard;
import com.payment.payneed.views.reports.AEPSTransaction;
import com.payment.payneed.views.reports.AllReports;
import com.payment.payneed.views.reports.BillRechargeTransaction;
import com.payment.payneed.views.reports.DMTTransactionReport;
import com.payment.payneed.views.reports.adapter.HorizontalReportListAdapter;
import com.payment.payneed.views.settings.Settings;
import com.payment.payneed.views.walletsection.AepsMatmWalletReqest;
import com.payment.payneed.views.walletsection.WalletFundRequest;
import com.payment.payneed.views.walletsection.WalletOptions;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.egram.aepslib.DashboardActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements RequestResponseLis, View.OnClickListener {
    private static final int REQUEST_CODE = 101;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private RecyclerView rvHome, rvReport, rvPayServicesList, headerList;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private RelativeLayout shimmer;
    private View secAeps, secMain, secMatmWallet;
    private TextView tvAepsBal, tvMainBal, tvMatamBal;
    private TextView tvAepsBalLbl, tvMainBalLbl, tvMatmBalLbl;
    private Button btnMainWallet, btnMatmWallet, btnAepsWallet;
    private Context context;
    private TextView tvMatmBalance, tvMainBalance, tvAepsBalance, tvWalletSec;
    private CarouselView rvSlider;
    private ShimmerFrameLayout shimmerAeps,shimmerMain;
    private LinearLayout secAddMoney, tvWalletTitleCon, secAepsWallet, secMatm;

    private void init() {
        context = this;
        rvSlider = findViewById(R.id.rvSlider);
        tvWalletTitleCon = findViewById(R.id.tvWalletTitleCon);
        secAepsWallet = findViewById(R.id.secAepsWallet);
        secMatm = findViewById(R.id.secMatm);
        secAddMoney = findViewById(R.id.secReload);
        headerList = findViewById(R.id.headerList);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        rvHome = findViewById(R.id.rvHome);
        rvPayServicesList = findViewById(R.id.rvPayServicesList);
        rvReport = findViewById(R.id.rvReport);
        shimmer = findViewById(R.id.shimmer);
        tvMatmBalance = findViewById(R.id.tvMatmBalance);
        tvMainBalance = findViewById(R.id.tvMainBalance);
        tvAepsBalance = findViewById(R.id.tvAepsBalance);
        tvWalletSec = findViewById(R.id.tvWalletSec);

        secAeps = findViewById(R.id.secAeps);
        tvAepsBal = secAeps.findViewById(R.id.tvAmount);
        btnAepsWallet = secAeps.findViewById(R.id.btnAddMoney);
        tvAepsBalLbl = secAeps.findViewById(R.id.tvLbl);
        shimmerAeps = secAeps.findViewById(R.id.tvAmountHolder);
        tvAepsBalLbl.setText("Aeps Balance");
        btnAepsWallet.setText("Payout");

        secMain = findViewById(R.id.secMain);
        tvMainBal = secMain.findViewById(R.id.tvAmount);
        btnMainWallet = secMain.findViewById(R.id.btnAddMoney);
        tvMainBalLbl = secMain.findViewById(R.id.tvLbl);
        shimmerMain = secMain.findViewById(R.id.tvAmountHolder);
        tvMainBalLbl.setText("Main Balance");


        new AepsAppHelper(this).checkForUpdates();
        setUpNavigationView();
        imageSliderInit();
        gridInit();
        topHeaderList();
        gridInitReport();
        gridInitSlider();
        initBalance();
        initMarquee();

        secAddMoney.setOnClickListener(v -> {
            shimmer.setVisibility(View.VISIBLE);
            networkCallUsingVolleyApi(Constants.URL.BALANCE_UPDATE, false);
        });

        tvWalletSec.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WalletOptions.class)));
        tvWalletTitleCon.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, WalletFundRequest.class);
            startActivity(i);
        });


        btnMainWallet.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, WalletFundRequest.class);
            startActivity(i);
        });

        btnAepsWallet.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AepsMatmWalletReqest.class);
            i.putExtra("activity_type", "aeps");
            startActivity(i);
        });

        secAepsWallet.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AepsMatmWalletReqest.class);
            i.putExtra("activity_type", "aeps");
            startActivity(i);
        });

        secMatm.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AepsMatmWalletReqest.class);
            i.putExtra("activity_type", "matm");
            startActivity(i);
        });

        TextView etSearch = findViewById(R.id.etSearch);
        etSearch.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AllServicesSearch.class)));
        appLockHandler();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,

                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE};
        AppPermissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {

            }
        });
        init();
    }

    private void appLockHandler() {
        String isLockEnable = SharedPrefs.getValue(this, SharedPrefs.IS_ALLOWED_APP_LOCK);
        if (MyUtil.isNN(isLockEnable)) {
            Intent intent = new Intent(this, EnterPinActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawer.closeDrawers();
            if (menuItem.getItemId() == R.id.report) {
                startActivity(new Intent(MainActivity.this, AllReports.class));
            } else if (menuItem.getItemId() == R.id.member) {
                slugPopup();
            } else if (menuItem.getItemId() == R.id.settle) {
                startActivity(new Intent(MainActivity.this, WalletOptions.class));
            } else if (menuItem.getItemId() == R.id.profile) {
                startActivity(new Intent(MainActivity.this, ProfilePage.class));
            } else if (menuItem.getItemId() == R.id.bank) {
                startActivity(new Intent(MainActivity.this, BankAccountPage.class));
            } else if (menuItem.getItemId() == R.id.settings) {
                startActivity(new Intent(MainActivity.this, Settings.class));
            } else if (menuItem.getItemId() == R.id.commission) {
                Bundle bundle = new Bundle();
                bundle.putString("schemeId", "getCommission");
                Intent intent = new Intent(this, CommissionList.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(context, "Available Soon", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.syncState();
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ImageView imgProfile = headerView.findViewById(R.id.imgProfile);
        TextView tvNavName = headerView.findViewById(R.id.tvNavName);
        TextView tvNavEmail = headerView.findViewById(R.id.tvNavEmail);
        String name = SharedPrefs.getValue(this, SharedPrefs.USER_NAME);
        tvNavName.setText(name);
        String email = SharedPrefs.getValue(this, SharedPrefs.USER_EMAIL);
        String useID = SharedPrefs.getValue(this, SharedPrefs.USER_ID);
        email += "\nuser id : " + useID;
        tvNavEmail.setText(email);
        Button btnLogout = headerView.findViewById(R.id.btnLogout);
        imgProfile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfilePage.class)));
        btnLogout.setOnClickListener(v -> AppManager.getInstance().logoutFromServer(this, false));
    }

    private void gridInitReport() {
        LinearLayoutManager nearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvReport.setLayoutManager(nearLayoutManager);
        rvReport.setItemAnimator(new DefaultItemAnimator());
        HorizontalReportListAdapter reportAdapter = new HorizontalReportListAdapter(MainActivity.this,
                MainLocalData.getReportGridRecord());
        rvReport.setAdapter(reportAdapter);
        rvReport.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                rvReport, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent i = new Intent(MainActivity.this, AEPSTransaction.class);
                        i.putExtra("title", "AEPS Transactions");
                        i.putExtra("type", "aepsstatement");
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(MainActivity.this, BillRechargeTransaction.class);
                        i.putExtra("title", "Recharge Statement");
                        i.putExtra("type", "rechargestatement");
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(MainActivity.this, BillRechargeTransaction.class);
                        i.putExtra("title", "Bill Payment Statement");
                        i.putExtra("type", "billpaystatement");
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(MainActivity.this, DMTTransactionReport.class);
                        i.putExtra("title", "DMT Statement");
                        i.putExtra("type", "dmtstatement");
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(MainActivity.this, AllReports.class);
                        i.putExtra("title", "All Reports");
                        startActivity(i);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void gridInit() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 4);
        rvHome.setLayoutManager(layoutManager);
        rvHome.setItemAnimator(new DefaultItemAnimator());
        HomeAdapter homeAdapter = new HomeAdapter(MainActivity.this, MainLocalData.getHomeGridRecord());
        rvHome.setAdapter(homeAdapter);
        rvHome.addOnItemTouchListener(new RecyclerTouchListener(this,
                rvHome, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i;
                if (position == 7) {
                    i = new Intent(MainActivity.this, AllServices.class);
                } else {
                    i = new Intent(MainActivity.this, RechargeAndBillPayment.class);
                    i.putExtra("position", String.valueOf(position));
                }
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void topHeaderList() {
        LinearLayoutManager nearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        headerList.setLayoutManager(nearLayoutManager);
        headerList.setItemAnimator(new DefaultItemAnimator());
        HeaderAdapter homeAdapter = new HeaderAdapter(MainActivity.this, MainLocalData.getMoneyTransferGrid(this));
        headerList.setAdapter(homeAdapter);
        headerList.addOnItemTouchListener(new RecyclerTouchListener(this,
                headerList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i;
                ActivityListModel model = MainLocalData.getMoneyTransferGrid(MainActivity.this).get(position);
                switch (model.getTxt1()) {
                    case "AEPS":
                       new AepsAppHelper(MainActivity.this).aepsInitiate();
                        break;
                    case "MATM":
                        i = new Intent(MainActivity.this, HandlerActivity.class);
                        i.putExtra("appUserId", SharedPrefs.getValue(MainActivity.this, SharedPrefs.USER_ID));
                        i.putExtra("type", "matm");
                        startActivityForResult(i, 5000);
                        break;
                    case "DMT":
                        startActivity(new Intent(context, DMTSearchAccount.class));
                        break;
                    case "PHONE":
                        i = new Intent(context, RechargeAndBillPayment.class);
                        i.putExtra("position", "0");
                        startActivity(i);
                        break;
                    case "DTH":
                        i = new Intent(context, RechargeAndBillPayment.class);
                        i.putExtra("position", "1");
                        startActivity(i);
                        break;

                    case "Package":
                        i = new Intent(context, PackageManagerListActivity.class);
                        startActivity(i);
                        break;
                    case "Pancard":
                        i = new Intent(context, PanCard.class);
                        i.putExtra("position", "0");
                        startActivity(i);
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void imageSliderInit() {
        String[] images = Constants.images;
        List<String> stringList = Arrays.asList(images);
        String is_avail_array = SharedPrefs.getValue(this, SharedPrefs.BANNERARRAY);
        if (MyUtil.isNN(is_avail_array))
            try {
                stringList = new ArrayList<>();
                JSONArray bannersJson = new JSONArray(is_avail_array);
                for (int i = 0; i < bannersJson.length(); i++) {
                    JSONObject obj = bannersJson.getJSONObject(i);
                    String url = Constants.URL.BASE_URL + "/public/" + obj.getString("value");
                    stringList.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        viewPager = findViewById(R.id.viewPager);
        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, stringList);
        viewPager.setAdapter(myCustomPagerAdapter);
        CircleIndicator circleIndicator = findViewById(R.id.circle);
        circleIndicator.setViewPager(viewPager);
        NUM_PAGES = images.length;
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
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
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        shimmerMain.setVisibility(View.GONE);
        shimmerAeps.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            JSONObject userObject = new JSONObject(jsonObject.getString("data"));
            if (userObject.has("mainwallet"))
                SharedPrefs.setValue(context, SharedPrefs.MAIN_WALLET, userObject.getString("mainwallet"));
            else
                SharedPrefs.setValue(context, SharedPrefs.MAIN_WALLET, userObject.getString("balance"));
            if (userObject.has("microatmbalance")) {
                SharedPrefs.setValue(context, SharedPrefs.MICRO_ATM_BALANCE, userObject.getString("microatmbalance"));
            } else {
                SharedPrefs.setValue(context, SharedPrefs.MICRO_ATM_BALANCE, "NO");
            }
            SharedPrefs.setValue(context, SharedPrefs.APES_BALANCE, userObject.getString("aepsbalance"));

            if (userObject.has("forceUpdate"))
                SharedPrefs.setValue(context, SharedPrefs.IS_FORCE_UPDATE_ENABLE, userObject.getString("forceUpdate"));
            else
                SharedPrefs.setValue(context, SharedPrefs.IS_FORCE_UPDATE_ENABLE, "");

            if (userObject.has("versioncode"))
                SharedPrefs.setValue(context, SharedPrefs.APP_VERSION_CODE, userObject.getString("versioncode"));
            else
                SharedPrefs.setValue(context, SharedPrefs.APP_VERSION_CODE, "");

            initBalance();
            isUpdateAvailable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBalance() {

        tvMainBal.setText(MyUtil.formatWithRupee(this, SharedPrefs.getValue(context, SharedPrefs.MAIN_WALLET)));
        tvAepsBal.setText(MyUtil.formatWithRupee(this, SharedPrefs.getValue(context, SharedPrefs.APES_BALANCE)));
        tvMatmBalance.setText("Fund Request");
        String mBal = SharedPrefs.getValue(context, SharedPrefs.MICRO_ATM_BALANCE);
        if (mBal != null && !mBal.equalsIgnoreCase("NO") && mBal.length() > 0)
            tvMatmBalance.setText(MyUtil.formatWithRupee(this, mBal));
    }

    @Override
    public void onFailRequest(String msg) {
        shimmer.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shimmer != null) {
             shimmerMain.setVisibility(View.VISIBLE);
            shimmerAeps.setVisibility(View.VISIBLE);
            networkCallUsingVolleyApi(Constants.URL.BALANCE_UPDATE, false);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.secDMT:
                startActivity(new Intent(context, DMTSearchAccount.class));
                break;
            case R.id.secPhone:
                Intent i = new Intent(this, RechargeAndBillPayment.class);
                i.putExtra("position", "0");
                startActivity(i);
                break;
        }
    }

    private void gridInitSlider() {
        try {
            rvSlider.setPageCount(Constants.imagesBottom.length);
            rvSlider.setImageListener(imageListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            MyUtil.setGlideImage(Constants.imagesBottom[position], imageView, context);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE:
                    if (resultCode == EnterPinActivity.RESULT_BACK_PRESSED) {
                        finishAffinity();
                        startActivity(new Intent(this, SplashScreen.class));
                    }
                    break;
                case 5000:
                    String msg = data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message");
                    if (MyUtil.isNN(msg)) {
                        Print.P(msg);
                        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AlertDialog alert;

    private void confirmPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you really want to exit?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> finish())
                .setNegativeButton(android.R.string.no, (dialog, whichButton) -> alert.dismiss());
        alert = builder.create();
        alert.show();
    }

    private void isUpdateAvailable() {
        if (alert != null && alert.isShowing()) alert.dismiss();
        int appVCode = BuildConfig.VERSION_CODE;
        String serverVersionCode = SharedPrefs.getValue(this, SharedPrefs.APP_VERSION_CODE);
        String isForceUpdate = SharedPrefs.getValue(this, SharedPrefs.IS_FORCE_UPDATE_ENABLE);
        if (MyUtil.isNN(serverVersionCode)) {
            try {
                int code = Integer.parseInt(serverVersionCode);
                if (code > appVCode) {
                    AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this,
                            R.style.ThemeOverlay_App_MaterialAlertDialog)
                            .setTitle("Update")
                            .setMessage("A new version is available please update you application")
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                dialog.dismiss();
                                openPlayStore();
                            });
                    if (!isForceUpdate.equalsIgnoreCase("yes")) {
                        builder.setNegativeButton("Cancel", (dialog, whichButton) -> alert.dismiss());
                    }
                    alert = builder.create();
                    alert.setCancelable(false);
                    alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openPlayStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @Override
    public void onBackPressed() {
        confirmPopup();
    }

    private void initMarquee() {
        String t = "";
        findViewById(R.id.secNews).setVisibility(View.VISIBLE);
        MarqueeTextView tvMarquee = findViewById(R.id.tvMarquee);
        t = SharedPrefs.getValue(this, SharedPrefs.NEWS);
        if (!MyUtil.isNN(t) || t.equalsIgnoreCase("no"))
            t = "Welcome to " + getString(R.string.app_name);
        tvMarquee.setText(t);
    }

    private void slugPopup() {
        String slug = SharedPrefs.getValue(this, SharedPrefs.ROLE_SLUG);
        if (MyUtil.isNN(slug) && (slug.equalsIgnoreCase("md") || slug.equalsIgnoreCase("distributor"))) {
            if (slug.equalsIgnoreCase("distributor")) {
                Intent i = new Intent(context, MemberListAll.class);
                i.putExtra("type", "retailer");
                startActivity(i);
            } else {
                final CharSequence[] choice = {"distributor", "retailer"};
                AlertDialog.Builder alert = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog);
                alert.setTitle("Please select role");
                alert.setSingleChoiceItems(choice, -1, (dialog, which) -> {
                    String txt = choice[which].toString();
                    dialog.dismiss();
                    Intent i = new Intent(context, MemberListAll.class);
                    i.putExtra("type", txt);
                    startActivity(i);
                });
                alert.show();
            }
        } else {
            Toast.makeText(context, "You don't have permission for this option", Toast.LENGTH_SHORT).show();
        }
    }
}