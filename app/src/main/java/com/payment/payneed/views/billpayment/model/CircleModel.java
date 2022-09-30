package com.payment.payneed.views.billpayment.model;

import com.google.gson.annotations.SerializedName;

public class CircleModel {

	@SerializedName(value = "name",alternate = {"maha_circle_name"})
	private String name;

	@SerializedName("id")
	private String id;
@SerializedName("circle_code")
	private String circle_code;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCircle_code() {
		return circle_code;
	}

	public void setCircle_code(String circle_code) {
		this.circle_code = circle_code;
	}
}