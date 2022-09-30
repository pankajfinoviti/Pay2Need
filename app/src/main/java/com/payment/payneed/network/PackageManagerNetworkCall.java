package com.payment.payneed.network;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payment.payneed.R;
import com.payment.payneed.utill.Print;
import com.payment.payneed.utill.SharedPrefs;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PackageManagerNetworkCall {
    private RequestResponseLis listener;
    private Activity context;
    private String apiUrl;
    private int method = 0;
    Map<String, Object> map;
    Dialog dialog;
    String whichRequest="";
    private boolean isDialog = true;
    String requestBody = "";

    public PackageManagerNetworkCall(RequestResponseLis listener, Activity context, String apiUrl) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;

    }

    public PackageManagerNetworkCall(RequestResponseLis listener, Activity context, String apiUrl, boolean isDialog) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
        this.isDialog = isDialog;
    }

    public PackageManagerNetworkCall(RequestResponseLis listener, Activity context, String apiUrl, int method) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
        this.method = method;
    }

    public PackageManagerNetworkCall(RequestResponseLis listener, Activity context, String apiUrl, int method, Map<String, Object> map, boolean isDialog) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
        this.method = method;
        this.map = map;
        this.isDialog = isDialog;
    }

    public PackageManagerNetworkCall(RequestResponseLis listener, Activity context, String apiUrl, int method, Map<String, Object> map) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
        this.method = method;
        this.map = map;

    }

    public PackageManagerNetworkCall(RequestResponseLis listener, Activity context, String apiUrl, int method, Map<String, Object> map, String whichRequest) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
        this.method = method;
        this.map = map;
        this.whichRequest = whichRequest;

    }


    public PackageManagerNetworkCall(RequestResponseLis listener, Activity context, String apiUrl, int method, String whichRequest, Map<String, Object> objMap) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
        this.method = method;
        this.whichRequest = whichRequest;

    }


    public PackageManagerNetworkCall(RequestResponseLis listener, Activity context, String apiUrl, int method, Map<String, Object> map, String whichRequest, boolean isDialog) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
        this.method = method;
        this.map = map;
        this.whichRequest = whichRequest;
        this.isDialog = isDialog;
    }


    public void netWorkCall() {
        if (map != null && map.size() > 0){
            requestBody = new JSONObject(map).toString();
            Print.P("Url : "+apiUrl);
            Print.P("Params : "+requestBody);
        }

        if (isDialog){
            showLoader(context);
        }

        StringRequest sendRequest = new StringRequest(
                method,
                apiUrl,
                Response -> {

                    if (BuildConfig.DEBUG)
                        Print.P(" Volley Response:" + Response);
                    try {
                        listener.onSuccessRequest(Response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    closeLoader();
                }
                , volleyError -> {
            try {
                String responseBody = new String(volleyError.networkResponse.data, StandardCharsets.UTF_8);
                Log.e("ERROR", responseBody);
                listener.onFailRequest(responseBody);

                closeLoader();
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailRequest(volleyError.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN);
                Map<String, String> params = new HashMap<String, String>();
                if (token!=null && token.length()>0) {
                    params.put("Authorization","Bearer"+ " "+token);
                }
                params.put("Accept","application/json");
                params.put("Content-Type","application/json");

                return params;

            }
        };

        sendRequest.setRetryPolicy(new
                DefaultRetryPolicy(
                60000,
                3,
                2));

        if (BuildConfig.DEBUG) {
            try {
                Log.e("get - URL ", sendRequest.getUrl());
                Log.e("get - Headers", sendRequest.getHeaders().toString());
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }
        }

        sendRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sendRequest);
    }

    private void showLoader(Activity context) {
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.module_loader_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    private void closeLoader() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
