package com.jesse.gallery;

import android.graphics.Bitmap;
/**
 * @author jie
 *
 */
//BitMap与图片描述文字
public class BitMapGroup {
	private Bitmap mainGalleryBitmap = null;
	private String mainGalleryName = "";
	
	public BitMapGroup(Bitmap bitmap, String name) {
		mainGalleryBitmap = bitmap;
		mainGalleryName = name;
	}

	public BitMapGroup() {
		// TODO Auto-generated constructor stub
	}

	public Bitmap getMainGalleryBitmap() {
		return mainGalleryBitmap;
	}

	public void setMainGalleryBitmap(Bitmap mainGalleryBitmap) {
		this.mainGalleryBitmap = mainGalleryBitmap;
	}

	public String getMainGalleryName() {
		return mainGalleryName;
	}

	public void setMainGalleryName(String name) {
		this.mainGalleryName = name;
	}
	
}
