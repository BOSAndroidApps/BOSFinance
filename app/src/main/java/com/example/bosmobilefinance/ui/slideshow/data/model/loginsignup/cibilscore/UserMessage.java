package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore;

import com.google.gson.annotations.SerializedName;

public class UserMessage{

	@SerializedName("UserMessageText")
	private String userMessageText;

	public String getUserMessageText(){
		return userMessageText;
	}
}