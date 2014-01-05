package com.getimageandtext;

import android.R.integer;

public class NewsPreviewInfo {

	private static String[] newsName = new String[20];
	private static String[] newsimg= new String[20];
	private static String[] time = new String[20];
	private static String[] glance = new String[20];
	private static String[] comment = new String[20];
	private static String[] newsUrl = new String[20];
	
	public static String getNewsUrl(int i) {
		return newsUrl[i];
	}
	public static void setNewsUrl(int i, String newsUrl) {
		NewsPreviewInfo.newsUrl[i] = newsUrl;
	}
	public static String getNewsName(int i) {
		return newsName[i];
	}
	public static void setNewsName(int i, String newsName) {
		NewsPreviewInfo.newsName[i] = newsName;
	}
	public static String getNewsimg(int i) {
		return newsimg[i];
	}
	public static void setNewsimg(int i, String newsimg) {
		NewsPreviewInfo.newsimg[i] = newsimg;
	}
	public static String getTime(int i) {
		return time[i];
	}
	public static void setTime(int i, String time) {
		NewsPreviewInfo.time[i] = time;
	}
	public static String getGlance(int i) {
		return glance[i];
	}
	public static void setGlance(int i, String glance) {
		NewsPreviewInfo.glance[i] = glance;
	}
	public static String getComment(int i) {
		return comment[i];
	}
	public static void setComment(int i, String comment) {
		NewsPreviewInfo.comment[i] = comment;
	}
	public static int getLength(){
		return  newsName.length;
	}
}
