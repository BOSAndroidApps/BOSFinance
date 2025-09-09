package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CAPS{

	@SerializedName("CAPS_Summary")
	private CAPSSummary cAPSSummary;

	public CAPSSummary getCAPSSummary(){
		return cAPSSummary;
	}
}