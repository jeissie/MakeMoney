package com.jesse.apkList;

import com.jesse.obj.ApkItem;

/**
 * @author jie
 *
 */
public class ApkListImageAndText extends ApkItem{
	 


	public ApkListImageAndText(String imageUrl, String apk_zh_name,
			String apk_size, String downloadUrl, int star, int money,
			boolean installed) {
		super(imageUrl, apk_zh_name, apk_size, downloadUrl, star, money, installed);
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public String getApk_zh_name() {
		return this.apk_zh_name;
	}
	

	public String getApk_size() {
		return this.apk_size;
	}
	
	public String getDownloadUrl() {
		return this.downloadUrl;
	}
	
	public int getStar(){
		return this.star;
	}
	
	public int getMoeny() {
		return this.moeny;
	}

}


