package com.jesse.util;

import android.util.Log;

public class MyView {
	private static int screenWidth = 0;

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static void setScreenWidth(int screenWidth) {
		MyView.screenWidth = screenWidth;
	}
	
	public static void Error(String text) {
		Log.v("MakeMoeny Error !!!", text);
	}
	
	public static void message(String text) {
		Log.v("MakeMoeny Message !!!", text);
	}
}
