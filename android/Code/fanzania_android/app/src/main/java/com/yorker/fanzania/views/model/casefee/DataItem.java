package com.yorker.fanzania.views.model.casefee;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("PGClientId")
	private String pGClientId;

	@SerializedName("PGClientSecret")
	private String pGClientSecret;

	@SerializedName("PGAuthKey")
	private String pGAuthKey;

	@SerializedName("Environment")
	private String environment;

	@SerializedName("PaymentGatewayName")
	private String paymentGatewayName;

	public void setPGClientId(String pGClientId){
		this.pGClientId = pGClientId;
	}

	public String getPGClientId(){
		return pGClientId;
	}

	public void setPGClientSecret(String pGClientSecret){
		this.pGClientSecret = pGClientSecret;
	}

	public String getPGClientSecret(){
		return pGClientSecret;
	}

	public void setPGAuthKey(String pGAuthKey){
		this.pGAuthKey = pGAuthKey;
	}

	public String getPGAuthKey(){
		return pGAuthKey;
	}

	public void setEnvironment(String environment){
		this.environment = environment;
	}

	public String getEnvironment(){
		return environment;
	}

	public void setPaymentGatewayName(String paymentGatewayName){
		this.paymentGatewayName = paymentGatewayName;
	}

	public String getPaymentGatewayName(){
		return paymentGatewayName;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"pGClientId = '" + pGClientId + '\'' + 
			",pGClientSecret = '" + pGClientSecret + '\'' + 
			",pGAuthKey = '" + pGAuthKey + '\'' + 
			",environment = '" + environment + '\'' + 
			",paymentGatewayName = '" + paymentGatewayName + '\'' + 
			"}";
		}
}