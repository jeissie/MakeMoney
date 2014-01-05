package com.getimageandtext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.context.ContextUtil;
import com.news.NewsPreview;

import android.R.integer;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;

/**
 * @author jie 主屏幕中的Gallery的资源获取和下载
 */
@SuppressLint("SdCardPath")
public class GetImageOnMain {

	private static Bitmap[] mainGalleryBitmap = new Bitmap[5];// 主屏幕图片
	private static String[] mainGalleryName = new String[5];

	private static int screenWidth;// 当前屏幕宽度

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		GetImageOnMain.screenWidth = screenWidth;
	}

	public static Bitmap getMain_galleryBitmap(int i) {
		return mainGalleryBitmap[i];
	}

	public static void setMain_galleryBitmap(int i, Bitmap main_galleryBitmap) {

		if (main_galleryBitmap != null) {
			GetImageOnMain.mainGalleryBitmap[i] = scaleMainImg(main_galleryBitmap);
			inputImageToFileCache(1, i, GetImageOnMain.mainGalleryBitmap[i]);
		}
	}

	public static void clearBitmap() {
		GetImageOnMain.mainGalleryBitmap = null;
	}

	public static String getMainGalleryName(int i) {
		return mainGalleryName[i];
	}

	public static void setMainGalleryName(int i, String mainGalleryName) {
		GetImageOnMain.mainGalleryName[i] = mainGalleryName;
	}

	private static Bitmap scaleMainImg(Bitmap bm) {

		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		double newWidth1 = screenWidth;
		double newHeight1 = screenWidth / 2.24;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
		return newbm;
	}

	private static void inputImageToFileCache(int params, int position,
			Bitmap image) {
		File file = new File("/sdcard/MxApk/cache");
		if (!file.exists()) {
			file.mkdir();
		}
		File imageFile = new File(file, "main" + position + ".jpg");
		try {
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			image.compress(CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
