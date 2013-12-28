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
 * @author jie ����Ļ�е�Gallery����Դ��ȡ������
 */
@SuppressLint("SdCardPath")
public class GetImageOnMain {

	private static Bitmap[] mainGalleryBitmap = new Bitmap[5];// ����ĻͼƬ
	private static String[] mainGalleryName = new String[5];

	private static int screenWidth;// ��ǰ��Ļ����

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

		// ���ͼƬ�Ŀ���
		int width = bm.getWidth();
		int height = bm.getHeight();
		// ������Ҫ�Ĵ�С
		double newWidth1 = screenWidth;
		double newHeight1 = screenWidth / 2.24;
		// �������ű���
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// �õ��µ�ͼƬ
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