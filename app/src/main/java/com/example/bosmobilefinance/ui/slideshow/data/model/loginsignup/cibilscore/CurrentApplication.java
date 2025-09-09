package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CurrentApplication{

	@SerializedName("Current_Application_Details")
	private CurrentApplicationDetails currentApplicationDetails;

	public CurrentApplicationDetails getCurrentApplicationDetails(){
		return currentApplicationDetails;
	}
}