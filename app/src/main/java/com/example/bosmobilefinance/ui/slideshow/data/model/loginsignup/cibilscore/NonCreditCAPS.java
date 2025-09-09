package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore;

import com.google.gson.annotations.SerializedName;

public class NonCreditCAPS{

	@SerializedName("NonCreditCAPS_Summary")
	private NonCreditCAPSSummary nonCreditCAPSSummary;

	public NonCreditCAPSSummary getNonCreditCAPSSummary(){
		return nonCreditCAPSSummary;
	}
}