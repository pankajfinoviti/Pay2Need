package com.payment.payneed.commission.gsonModel;

import com.google.gson.annotations.SerializedName;

public class CommissionModel {
    @SerializedName("provider")
    private Provider provider;
    private String  keys;

    public Provider getProvider() {
        return provider;
    }



    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("value")
    private String value;

    @SerializedName("slab")
    private String slab;

    @SerializedName("scheme_id")
    private String schemeId;

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSlab() {
        return slab;
    }

    public void setSlab(String slab) {
        this.slab = slab;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }
}