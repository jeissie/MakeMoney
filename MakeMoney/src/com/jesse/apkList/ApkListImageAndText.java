package com.jesse.apkList;

import com.jesse.obj.ApkItem;

/**
 * @author jie
 *
 */
public class ApkListImageAndText extends ApkItem{
	 

	public ApkListImageAndText(String imageUrl, String apk_zh_name,
			String apk_size, String downloadUrl, int star) {
		super(imageUrl, apk_zh_name, apk_size, downloadUrl, star);
		// TODO Auto-generated constructor stub
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getApk_zh_name() {
		return apk_zh_name;
	}
	

	public String getApk_size() {
		return apk_size;
	}
	
	public String getDownloadUrl() {
		return downloadUrl;
	}
	
	public int getStar(){
		return star;
	}

}


