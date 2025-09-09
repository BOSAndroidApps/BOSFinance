package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore;

import com.google.gson.annotations.SerializedName;

public class ResultJson{

	@SerializedName("INProfileResponse")
	private INProfileResponse iNProfileResponse;

	public INProfileResponse getINProfileResponse(){
		return iNProfileResponse;
	}
}