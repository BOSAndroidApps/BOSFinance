package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CAISSummary{

	@SerializedName("Total_Outstanding_Balance")
	private TotalOutstandingBalance totalOutstandingBalance;

	@SerializedName("Credit_Account")
	private CreditAccount creditAccount;

	public TotalOutstandingBalance getTotalOutstandingBalance(){
		return totalOutstandingBalance;
	}

	public CreditAccount getCreditAccount(){
		return creditAccount;
	}
}