package com.ezypay.ezypaySubscription.entity;

import java.util.List;

public class SubsResponse {

	private String message;
	private String amount;
	private String subscriptionType;
	private List<String> invoiceDateList;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSubscriptionType() {
		return subscriptionType;
	}
	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}
	public List<String> getInvoiceDateList() {
		return invoiceDateList;
	}
	public void setInvoiceDateList(List<String> invoiceDateList) {
		this.invoiceDateList = invoiceDateList;
	}
	
}
