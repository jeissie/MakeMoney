package com.gallery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.context.ContextUtil;
import com.getimageandtext.GetImageOnMain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * @author jie
 * 
 */
@SuppressLint("SdCardPath")
public class Util {

	/* 创建数据源 */
	@SuppressWarnings("unused")
	public static List<AptechObject> getContent(Context context)
			throws FileNotFoundException, IOException {

		List<AptechObject> objects = new ArrayList<AptechObject>();

		File file = new File("/sdcard/MxApk/cache");
		if (!file.exists()) {
			return null;
		} else {
			/***** 读取本地缓存图片 ******/
			if (GetImageOnMain.getMain_galleryBitmap(4) == null) { // 网络获取的图片没有或者不完全则读取本地图片

				if (new File(Environment.getExternalStorageDirectory()
						+ "/MxApk/cache/main4.jpg") != null) {
					for (int i = 0; i < 5; i++) {
						AptechObject accp = new AptechObject();
						String fileName = "/MxApk/cache/main" + i + ".jpg";
						File temp = new File(
								Environment.getExternalStorageDirectory()
										+ fileName);
						if (temp != null) {
							Bitmap bitmap = MediaStore.Images.Media.getBitmap(
									ContextUtil.getInstance()
											.getContentResolver(), Uri
											.fromFile(temp));
							accp.setObjphoto(bitmap);
							objects.add(accp);
						} else {
							objects = null;
						}
					}
					return objects;
				} else {
					/****** 获取网络图片 ******/
					AptechObject accp1 = new AptechObject();
					Bitmap bitmap1 = GetImageOnMain.getMain_galleryBitmap(0);
					if (bitmap1 != null) {
						accp1.setObjphoto(bitmap1);
						objects.add(accp1);
					}
					AptechObject accp2 = new AptechObject();
					Bitmap bitmap2 = GetImageOnMain.getMain_galleryBitmap(1);
					if (bitmap2 != null) {
						accp2.setObjphoto(bitmap2);
						objects.add(accp2);
					}
					AptechObject accp3 = new AptechObject();
					Bitmap bitmap3 = GetImageOnMain.getMain_galleryBitmap(2);
					if (bitmap3 != null) {
						accp3.setObjphoto(bitmap3);
						objects.add(accp3);
					}
					AptechObject accp4 = new AptechObject();
					Bitmap bitmap4 = GetImageOnMain.getMain_galleryBitmap(3);
					if (bitmap4 != null) {
						accp4.setObjphoto(bitmap4);
						objects.add(accp4);
					}
					AptechObject accp5 = new AptechObject();
					Bitmap bitmap5 = GetImageOnMain.getMain_galleryBitmap(4);
					if (bitmap5 != null) {
						accp5.setObjphoto(bitmap5);
						objects.add(accp5);
					}
					GetImageOnMain.clearBitmap();
					return objects;
				}

			} else {
				/****** 获取网络图片 ******/
				AptechObject accp1 = new AptechObject();
				Bitmap bitmap1 = GetImageOnMain.getMain_galleryBitmap(0);
				if (bitmap1 != null) {
					accp1.setObjphoto(bitmap1);
					objects.add(accp1);
				}
				AptechObject accp2 = new AptechObject();
				Bitmap bitmap2 = GetImageOnMain.getMain_galleryBitmap(1);
				if (bitmap2 != null) {
					accp2.setObjphoto(bitmap2);
					objects.add(accp2);
				}
				AptechObject accp3 = new AptechObject();
				Bitmap bitmap3 = GetImageOnMain.getMain_galleryBitmap(2);
				if (bitmap3 != null) {
					accp3.setObjphoto(bitmap3);
					objects.add(accp3);
				}
				AptechObject accp4 = new AptechObject();
				Bitmap bitmap4 = GetImageOnMain.getMain_galleryBitmap(3);
				if (bitmap4 != null) {
					accp4.setObjphoto(bitmap4);
					objects.add(accp4);
				}
				AptechObject accp5 = new AptechObject();
				Bitmap bitmap5 = GetImageOnMain.getMain_galleryBitmap(4);
				if (bitmap5 != null) {
					accp5.setObjphoto(bitmap5);
					objects.add(accp5);
				}
				GetImageOnMain.clearBitmap();
				return objects;
			}
		} // Bitmap bitmap1 =
			// BitmapFactory.decodeResource(context.getResources(),
			// R.drawable.main_gallery_view1);

	}
}
