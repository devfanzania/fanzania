package com.yorker.fanzania.views.screens.notification;

import com.google.gson.annotations.SerializedName;

public class NotificationResponse{

	@SerializedName("Title")
	private String title;

	@SerializedName("UpdateDate")
	private String updateDate;

	@SerializedName("Active")
	private boolean active;

	@SerializedName("NotificationId")
	private int notificationId;

	@SerializedName("Message")
	private String message;

	@SerializedName("UserId")
	private int userId;

	@SerializedName("InsertDate")
	private String insertDate;

	@SerializedName("MessageType")
	private int messageType;

	public String getTitle() {
		return title;
	}

	public String getUpdateDate(){
		return updateDate;
	}

	public boolean isActive(){
		return active;
	}

	public int getNotificationId(){
		return notificationId;
	}

	public String getMessage(){
		return message;
	}

	public int getUserId(){
		return userId;
	}

	public String getInsertDate(){
		return insertDate;
	}

	public int getMessageType(){
		return messageType;
	}
}