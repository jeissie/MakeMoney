package com.getimageandtext;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ApkContentInfo {
	private static Bitmap apkIcon ;// Ö÷ÆÁÄ»Í¼Æ¬
	private static List<Drawable> apkGallery = new ArrayList<Drawable>();//Gallery


	public static List<Drawable> getApkGallery() {
		return apkGallery;
	}

	public static void setApkGallery(Drawable apkGallery) {
		ApkContentInfo.apkGallery.add(apkGallery) ;
	}

	public static Bitmap getApkIcon() {
		return apkIcon;
	}

	public static void setApkIcon(Bitmap apkIcons) {
		ApkContentInfo.apkIcon = apkIcons;
	}
	
	public static void releaseGallery(){
		apkGallery.clear();
	}
}
