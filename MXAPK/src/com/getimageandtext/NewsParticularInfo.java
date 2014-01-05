package com.getimageandtext;

import java.util.ArrayList;
import java.util.List;

public class NewsParticularInfo {
	private static String newsHtml;
	private static List<String> imageUrl = new ArrayList<String>();
	
	
	public static List<String> getImageUrl() {
		return imageUrl;
	}

	public static void setImageUrl(List<String> imageUrl) {
		NewsParticularInfo.imageUrl = imageUrl;
	}

	public static String getNewsHtml() {
		return newsHtml;
	}

	public static void setNewsHtml(String newsHtml) {
		NewsParticularInfo.newsHtml = newsHtml;
	}
	
	

}
