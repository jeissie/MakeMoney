package com.jesse.model;

import com.jesse.obj.UserInfo;

public class UserData extends UserInfo{
	public static UserData instances = null;
	
	private UserData() {}
	
	public static UserData getInstance() {
		if (instances == null) {
			instances = new UserData();
		}
		return instances;
	}
	
	public void setUserName(String userName) {
		this.userNameString = userName;
	}
	
	public void setDeviceId(String id) {
		this.deviceId = id;
	}
}
