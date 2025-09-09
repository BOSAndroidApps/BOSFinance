package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore;

import com.google.gson.annotations.SerializedName;

public class SCORE{

	@SerializedName("BureauScore")
	private String bureauScore;

	@SerializedName("BureauScoreConfidLevel")
	private Object bureauScoreConfidLevel;

	public String getBureauScore(){
		return bureauScore;
	}

	public Object getBureauScoreConfidLevel(){
		return bureauScoreConfidLevel;
	}
}