package com.payment.payneed.views.signup.model;

import com.google.gson.annotations.SerializedName;

public class RolePojo{

	@SerializedName("fees")
	private String fees;

	@SerializedName("usertype")
	private String usertype;

	public void setFees(String fees){
		this.fees = fees;
	}

	public String getFees(){
		return fees;
	}

	public void setUsertype(String usertype){
		this.usertype = usertype;
	}

	public String getUsertype(){
		return usertype;
	}
}