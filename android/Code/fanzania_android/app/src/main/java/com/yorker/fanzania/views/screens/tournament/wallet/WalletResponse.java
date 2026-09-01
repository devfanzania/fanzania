package com.yorker.fanzania.views.screens.tournament.wallet;

import com.google.gson.annotations.SerializedName;

public class WalletResponse{

	@SerializedName("WalletPoints")
	private int walletPoints;

	@SerializedName("AttainTierByDate")
	private String attainTierByDate;

	@SerializedName("TierStartDate")
	private String tierStartDate;

	@SerializedName("AttainTier")
	private String attainTier;

	@SerializedName("RetainTier")
	private String retainTier;

	@SerializedName("RetainTierByDate")
	private String retainTierByDate;

	@SerializedName("Name")
	private String name;

	@SerializedName("UserTier")
	private String userTier;

	@SerializedName("TierExpiryDate")
	private String tierExpiryDate;

	@SerializedName("TournamentTotal")
	private int tournamentTotal;

	@SerializedName("SignUpDate")
	private String signUpDate;

	@SerializedName("RetainTierByPoints")
	private int retainTierByPoints;

	@SerializedName("MatchContestTotal")
	private int matchContestTotal;

	@SerializedName("AttainTierByPoints")
	private int attainTierByPoints;

	public int getWalletPoints(){
		return walletPoints;
	}

	public String getAttainTierByDate(){
		return attainTierByDate;
	}

	public String getTierStartDate(){
		return tierStartDate;
	}

	public String getAttainTier(){
		return attainTier;
	}

	public String getRetainTier(){
		return retainTier;
	}

	public String getRetainTierByDate(){
		return retainTierByDate;
	}

	public String getName(){
		return name;
	}

	public String getUserTier(){
		return userTier;
	}

	public String getTierExpiryDate(){
		return tierExpiryDate;
	}

	public int getTournamentTotal(){
		return tournamentTotal;
	}

	public String getSignUpDate(){
		return signUpDate;
	}

	public int getRetainTierByPoints(){
		return retainTierByPoints;
	}

	public int getMatchContestTotal(){
		return matchContestTotal;
	}

	public int getAttainTierByPoints(){
		return attainTierByPoints;
	}

	@Override
 	public String toString(){
		return 
			"WalletResponse{" + 
			"walletPoints = '" + walletPoints + '\'' + 
			",attainTierByDate = '" + attainTierByDate + '\'' + 
			",tierStartDate = '" + tierStartDate + '\'' + 
			",attainTier = '" + attainTier + '\'' + 
			",retainTier = '" + retainTier + '\'' + 
			",retainTierByDate = '" + retainTierByDate + '\'' + 
			",name = '" + name + '\'' + 
			",userTier = '" + userTier + '\'' + 
			",tierExpiryDate = '" + tierExpiryDate + '\'' + 
			",tournamentTotal = '" + tournamentTotal + '\'' + 
			",signUpDate = '" + signUpDate + '\'' + 
			",retainTierByPoints = '" + retainTierByPoints + '\'' + 
			",matchContestTotal = '" + matchContestTotal + '\'' + 
			",attainTierByPoints = '" + attainTierByPoints + '\'' + 
			"}";
		}
}