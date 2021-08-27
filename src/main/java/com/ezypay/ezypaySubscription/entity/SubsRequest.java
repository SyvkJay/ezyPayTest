package com.ezypay.ezypaySubscription.entity;

public class SubsRequest {

	private String amount;
	private String subscriptionType;
	private String dayWeekMonth;
	private String startDate;
	private String endDate;
	
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
	public String getDayWeekMonth() {
		return dayWeekMonth;
	}
	public void setDayWeekMonth(String dayWeekMonth) {
		this.dayWeekMonth = dayWeekMonth;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
