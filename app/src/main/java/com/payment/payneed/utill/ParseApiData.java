package com.payment.payneed.utill;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.payment.payneed.views.package_manager.pkgmodel.ChargeCommissionModel;
import com.payment.payneed.views.package_manager.pkgmodel.PackageComListModel;


import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ParseApiData {

    public static <T> List parseJsonArray(JSONArray jsonArray, String modelName) {
        List<Class<T>> data = new ArrayList<>();
        try {
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), getType(modelName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Type getType(String modelName) {
        Type type;
        if ("PackageComListModel".equals(modelName)) {
            type = new TypeToken<List<PackageComListModel>>() {
            }.getType();
        }else   if ("ChargeCommissionModel".equals(modelName)) {
            type = new TypeToken<List<ChargeCommissionModel>>() {
            }.getType();
        }  else throw new IllegalStateException("NO Module Found for parsing data " + modelName);

        return type;
    }

}
