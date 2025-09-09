package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore;

import com.google.gson.annotations.SerializedName;

public class MatchResult{

	@SerializedName("Exact_match")
	private String exactMatch;

	public String getExactMatch(){
		return exactMatch;
	}
}