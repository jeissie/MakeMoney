package com.jesse.manager;

import com.jesse.model.UserData;

public class InitData {
	public void initDeviceId() {
		//TODO get in native
		UserData.getInstance().setDeviceId("");
		//TODO set in native
	}
	
	public void initUserData() {
		//TODO get in net or share
		UserData.getInstance().setUserName("");
	}
	
}
