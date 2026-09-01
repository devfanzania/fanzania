package com.yorker.fanzania.views.model.casefee;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CasefeeResponse{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("httpStatus")
	private String httpStatus;

	@SerializedName("statusMessage")
	private String statusMessage;

	@SerializedName("httpStatusDescription")
	private String httpStatusDescription;

	@SerializedName("status")
	private String status;

	@SerializedName("httpStatusCode")
	private int httpStatusCode;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setHttpStatus(String httpStatus){
		this.httpStatus = httpStatus;
	}

	public String getHttpStatus(){
		return httpStatus;
	}

	public void setStatusMessage(String statusMessage){
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage(){
		return statusMessage;
	}

	public void setHttpStatusDescription(String httpStatusDescription){
		this.httpStatusDescription = httpStatusDescription;
	}

	public String getHttpStatusDescription(){
		return httpStatusDescription;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setHttpStatusCode(int httpStatusCode){
		this.httpStatusCode = httpStatusCode;
	}

	public int getHttpStatusCode(){
		return httpStatusCode;
	}

	@Override
 	public String toString(){
		return 
			"CasefeeResponse{" + 
			"data = '" + data + '\'' + 
			",httpStatus = '" + httpStatus + '\'' + 
			",statusMessage = '" + statusMessage + '\'' + 
			",httpStatusDescription = '" + httpStatusDescription + '\'' + 
			",status = '" + status + '\'' + 
			",httpStatusCode = '" + httpStatusCode + '\'' + 
			"}";
		}
}