package com.payment.payneed.network;

public interface RequestResponseLis {
    void onSuccessRequest(String JSonResponse);

    void onFailRequest(String msg);
}