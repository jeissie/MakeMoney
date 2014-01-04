package com.jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.getimageandtext.ApkContentInfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class JsoupApkContent {
	Document doc;

	public void getApkContentInfo(String url) throws IOException {
		String apkIcon = null;// 软件图标
		
		
		doc = Jsoup.connect(url).get();
		Elements slideApkInfo = doc.getElementsByClass("left_box_03");
		Elements slideApkIcon = slideApkInfo.select(".soft_pic");
		for (Element info : slideApkIcon) {
			String img = info.attr("src");
			if (img != null && img.trim().length() > 0) {
				apkIcon = "http://www.mxapk.com" + img;
				Log.v("url", img);
			}
		}

			URL urls = new URL(apkIcon);
			URLConnection uc = urls.openConnection();
			InputStream in = uc.getInputStream();
			Bitmap map = BitmapFactory.decodeStream(in);
			ApkContentInfo.setApkIcon(map); // 存储图片资源
			in.close();
		
	}
	public void getApkContentGallery(String url) throws IOException{
		List<String> apkGallery = new ArrayList<String>();
		doc = Jsoup.connect(url).get();
		Elements slideApkGallery = doc.getElementsByClass("piclist");
		slideApkGallery = slideApkGallery.select("a");
		int i = 0;
		for (Element info : slideApkGallery) {
			if (i != 5) {
				String img = info.attr("href");
				if (img != null && img.trim().length() > 0) {
					Log.v("apkGallery", img);
					apkGallery.add("http://www.mxapk.com" + img);
				}
			} else {
				break;
			}
			i++;
		}
		
		for(int j = 0; j<apkGallery.size(); j++) {
			URL m;
			InputStream s = null;
			try {
				m = new URL(apkGallery.get(j));
				s = (InputStream) m.getContent();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Drawable d = Drawable.createFromStream(s, "src");
			ApkContentInfo.setApkGallery(d);
		}
	}
}
