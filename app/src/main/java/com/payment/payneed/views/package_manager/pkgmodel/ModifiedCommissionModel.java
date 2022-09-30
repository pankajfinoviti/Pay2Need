package com.payment.payneed.views.package_manager.pkgmodel;

public class ModifiedCommissionModel {
    private String slab;
    private String type;
    private String value;

    public ModifiedCommissionModel(String slab, String type, String value) {
        this.slab = slab;
        this.type = type;
        this.value = value;
    }

    public String getSlab() {
        return slab;
    }

    public void setSlab(String slab) {
        this.slab = slab;
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
}
