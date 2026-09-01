package com.yorker.fanzania.views.screens.tournament.wallet;

import com.google.gson.annotations.SerializedName;

public class WalletClaimResponse{

	@SerializedName("ClaimId")
	private int claimId;

	@SerializedName("ClaimAmount")
	private int claimAmount;

	@SerializedName("Vouchar")
	private String vouchar;

	@SerializedName("Bundle")
	private String bundle;

	@SerializedName("ClaimDate")
	private String claimDate;

	@SerializedName("TotalClaimAmount")
	private int totalClaimAmount;

	@SerializedName("TotalOutstandingAmount")
	private int totalOutstandingAmount;

	@SerializedName("MinAmountToWithDraw")
	private int minAmountToWithDraw;

	public int getTotalCash() {
		return totalCash;
	}

	@SerializedName("TotalCash")
	private int totalCash;

	@SerializedName("WalletAmount")
	private int WalletAmount;

	@SerializedName("RecentTopUpAmount")
	private int RecentTopUpAmount;
	@SerializedName("RecentTopUpDate")
	private String RecentTopUpDate;



	public int getMinAmountToWithDraw() {
		return minAmountToWithDraw;
	}

	public int getTotalOutstandingAmount() {
		return totalOutstandingAmount;
	}

	public int getClaimId(){
		return claimId;
	}

	public int getClaimAmount(){
		return claimAmount;
	}

	public String getVouchar(){
		return vouchar;
	}

	public String getBundle(){
		return bundle;
	}

	public String getClaimDate(){
		return claimDate;
	}

	public int getTotalClaimAmount(){
		return totalClaimAmount;
	}

	public int getWalletAmount() {
		return WalletAmount;
	}

	public void setWalletAmount(int walletAmount) {
		WalletAmount = walletAmount;
	}

	public int getRecentTopUpAmount() {
		return RecentTopUpAmount;
	}

	public void setRecentTopUpAmount(int recentTopUpAmount) {
		RecentTopUpAmount = recentTopUpAmount;
	}

	public String getRecentTopUpDate() {
		return RecentTopUpDate;
	}

	public void setRecentTopUpDate(String recentTopUpDate) {
		RecentTopUpDate = recentTopUpDate;
	}

	@Override
	public String toString() {
		return "WalletClaimResponse{" +
				"claimId=" + claimId +
				", claimAmount=" + claimAmount +
				", vouchar='" + vouchar + '\'' +
				", bundle='" + bundle + '\'' +
				", claimDate='" + claimDate + '\'' +
				", totalClaimAmount=" + totalClaimAmount +
				", totalOutstandingAmount=" + totalOutstandingAmount +
				", minAmountToWithDraw=" + minAmountToWithDraw +
				", totalCash=" + totalCash +
				", WalletAmount=" + WalletAmount +
				", RecentTopUpAmount=" + RecentTopUpAmount +
				", RecentTopUpDate='" + RecentTopUpDate + '\'' +
				'}';
	}
}