package com.yorker.fanzania.views.screens.tournament.wallet;

import com.google.gson.annotations.SerializedName;

public class WalletRewardsResponse{

	@SerializedName("RewardAmount")
	private int rewardAmount;

	@SerializedName("Details")
	private String details;

	@SerializedName("TotalRewardAmount")
	private int totalRewardAmount;

	@SerializedName("RewardDate")
	private String rewardDate;

	@SerializedName("RewardType")
	private String rewardType;

	@SerializedName("RewardId")
	private int rewardId;

	public int getRewardAmount(){
		return rewardAmount;
	}

	public String getDetails(){
		return details;
	}

	public int getTotalRewardAmount(){
		return totalRewardAmount;
	}

	public String getRewardDate(){
		return rewardDate;
	}

	public String getRewardType(){
		return rewardType;
	}

	public int getRewardId(){
		return rewardId;
	}
}