package com.jesse.gallery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jesse.makemoney.R;
import com.jesse.model.GetImageOnMain;
import com.jesse.net.DownloadComplege;
import com.jesse.net.DownloadImageComplete;
import com.jesse.net.NetService;
import com.jesse.util.ContextUtil;
import com.jesse.util.MakeMoneyDefine;
import com.jesse.util.MyView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

@SuppressLint("SdCardPath")
public class Util {
	private Context m_context;
	public static List<BitMapGroup> getContent(Context context)
			throws FileNotFoundException, IOException {
		List<BitMapGroup> objects = new ArrayList<BitMapGroup>();
			/***** 读取文件 ******/
		if (GetImageOnMain.getInstance().getBitMapGroup().size() <= 0) { // 文件还未开始下载或者下载正在下载中
			objects = loadedFileInNative(context);
		} else {
			MyView.message("reloading ImageInNative ");
			objects = GetImageOnMain.getInstance().getBitMapGroup();
		}
		return objects;
	} 
	
	public static List<BitMapGroup> loadedFileInNative(Context context)  {
		List<BitMapGroup> objects = new ArrayList<BitMapGroup>();
		/***** 读取缓存文件 ******/
		// TODO 读取数量应从网络获得
		for (int i = 0; i < 5; i++) {
			MyView.message("loadingImageInNative " + i);
			String fileName = MakeMoneyDefine.galleryFilePatch + "main" + i + ".jpg";
			if (new File(Environment.getExternalStorageDirectory() + fileName) != null) {
				BitMapGroup accp = new BitMapGroup();
				File temp = new File(
						Environment.getExternalStorageDirectory() + fileName);
				Bitmap bitmap = null;
				try {
					bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(temp));
				} catch (FileNotFoundException e) {
					//TODO 下载图片文件
					MyView.Error("downloading ImageFile");
					NetService.getInstance().requireService(context, "");
					e.printStackTrace();
					continue;
				} catch (IOException e) {
					e.printStackTrace();
				}
				accp.setMainGalleryBitmap(bitmap);
				objects.add(accp);
			} else {
				//TODO 下载图片文件
				MyView.message("downloading ImageFile");
				NetService.getInstance().requireService(context, "");
				continue;
			}
		} 
		return objects;
	}
}
