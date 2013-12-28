package com.jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.getimageandtext.GetInfoOnMain;
import com.getimageandtext.GetImageOnMain;
import com.getimageandtext.GetInfoOnMain;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Xml.Encoding;

/**
 * @author jie ����Ļ�е���Դ��ȡ������
 */
public class JsoupMain {

	Document doc;

	/****** ��ȡGalleryͼƬ������ ******/
	public void getInfoOnGallery() throws IOException {
		List<String> imgSrcs = new ArrayList<String>();// ��ַ��Դ
		List<String> imgName = new ArrayList<String>();// ����
		List<String> imgUrl = new ArrayList<String>();// ��תҳ��ַ

		doc = Jsoup.connect("http://www.mxapk.com/").get();
		Elements slideInfo = doc.getElementsByClass("slidesImg");
		for (Element info : slideInfo.select("img")) {
			String img = info.attr("src");
			if (img != null && img.trim().length() > 0) {
				imgSrcs.add("http://www.mxapk.com" + img);
			}
		}
		Elements slideApkName = doc.getElementsByClass("slidesImg"); // ָ����ҳ����ͼƬ��
		for (Element info : slideApkName) {
			Elements eachInfo = info.getElementsByTag("span");
			for (Element names : eachInfo) {
				String name = names.text();
				imgName.add(name);
			}
		}
		
		for (int i = 0; i < imgName.size(); i++) {
			GetImageOnMain.setMainGalleryName(i, imgName.get(i));
		}
		
		for (int i = 0; i < imgSrcs.size(); i++) {
			URL url = new URL((String) imgSrcs.get(i));
			URLConnection uc = url.openConnection();
			InputStream in = uc.getInputStream();
			Bitmap map = BitmapFactory.decodeStream(in);
			GetImageOnMain.setMain_galleryBitmap(i, map); // �洢ͼƬ��Դ
			in.close();
		}
	}

	/******* �б�Ӧ��ͼ�� ******/
	public void getApkIconInfo() throws IOException {
		List<String> apkIcon = new ArrayList<String>();// ͼ���ַ��Դ

		doc = Jsoup.connect("http://www.mxapk.com/list/1").get();
		Elements slideApkInfo = doc.getElementsByClass("tabcenter");
		Elements slideApkIcon = slideApkInfo.select("img");
		for (Element info : slideApkIcon) {
			String img = info.attr("src");
			if (img != null && img.trim().length() > 0) {
				apkIcon.add("http://www.mxapk.com" + img);
				Log.v("url", img);
			}
		}

		for (int i = 0; i < 10; i++) {
			URL url = new URL((String) apkIcon.get(i));
			URLConnection uc = url.openConnection();
			InputStream in = uc.getInputStream();
			Bitmap map = BitmapFactory.decodeStream(in);
			GetInfoOnMain.setApkIcon(i, map);
			in.close();
			Log.v("apkIcon.size", apkIcon.size() + "");
		}
	}

	/******* �б�Ӧ������ *******/
	public void getApkInfo() throws IOException {
		List<String> apkZhName = new ArrayList<String>();// ��������
		List<String> apkVersion = new ArrayList<String>();// apk�汾
		List<String> apkSize = new ArrayList<String>();// �����С
		List<String> apkTime = new ArrayList<String>();// ����ʱ��
		List<String> apkDownloadTimes = new ArrayList<String>();// ���ش���
		List<String> apkInstallUrl = new ArrayList<String>();// ���ص�ַ
		List<String> apkStar = new ArrayList<String>(); // �������
		List<String> apkUrl = new ArrayList<String>();

		doc = Jsoup.connect("http://www.mxapk.com/list/1").get();
		Elements slideApkInfo = doc.getElementsByClass("tabcenter");
		Elements slideApkZhName = slideApkInfo.select(".info");
		for (Element info : slideApkZhName) {
			Elements nameinfos = info.select(".title");
			for (Element names : nameinfos) {
				String name = names.text();
				apkZhName.add(name);
			}
		}

		for (Element downlaodTimes : slideApkZhName) {

			String timesInfo = downlaodTimes.text().toString();
			String regex = "\\b\\d*\\w*\\s*\\b \\b������\\b";
			Pattern p = Pattern.compile(regex);
			Log.v("info", timesInfo);
			Matcher m = p.matcher(timesInfo);
			while (m.find()) {
				String times = null;
				times = m.group();
				Log.v("times", times);
				apkDownloadTimes.add(times);
			}

		}
		Elements apkUrls = slideApkZhName.select("a");
		for (Element infoUrl : apkUrls) {
			String jumpUrl = infoUrl.attr("href");
			if (jumpUrl != null && jumpUrl.trim().length() > 0) {
				apkUrl.add("http://www.mxapk.com" + jumpUrl);
				Log.v("url", jumpUrl);
			}
		}

		for (Element downlaodTimes : slideApkZhName) {

			String timesInfo = downlaodTimes.text().toString();
			String regex;
			Pattern p;
			Matcher m;

			// �汾��Ϣ
			regex = "\\([^@]*\\)";
			p = Pattern.compile(regex);
			m = p.matcher(timesInfo);
			while (m.find()) {
				String version = null;
				version = m.group();
				Log.v("times", version);
				apkVersion.add(version);
			}
			// �����С
			regex = "\\d+(.\\d+)?GB|\\d+(.\\d+)?MB|\\d+(.\\d+)?KB|\\d+(.\\d+)?B";
			p = Pattern.compile(regex);
			m = p.matcher(timesInfo);
			while (m.find()) {
				String apksize = null;
				apksize = m.group();
				Log.v("times", apksize);
				apkSize.add(apksize);
			}
		}
		Elements downloadUrl = slideApkInfo.select(".xzbtn");
		for (Element info : downloadUrl) {
			String dUrl = info.attr("href");
			if (dUrl != null && dUrl.trim().length() > 0) {
				apkInstallUrl.add("http://www.mxapk.com" + dUrl);
				Log.v("durl", dUrl);
			}
		}
		// ����ʱ��
		Elements time = slideApkInfo.select(".icon");
		for (Element info : time) {
			apkTime.add(info.text());

		}

		for (int i = 0; i < 10; i++) {
			GetInfoOnMain.setZhName(i, apkZhName.get(i));
			GetInfoOnMain.setApkDownloadTimes(i, apkDownloadTimes.get(i));
			GetInfoOnMain.setApkVersion(i, apkVersion.get(i));
			GetInfoOnMain.setApkSize(i, apkSize.get(i));
			GetInfoOnMain.setApkTime(i, apkTime.get(i));
			GetInfoOnMain.setApkInstallUrl(i, apkInstallUrl.get(i));
			GetInfoOnMain.setApkUrl(i, apkUrl.get(i));
		}
	}

	/******* ��ϷӦ��ͼ�� ******/
	public void getGameIconInfo() throws IOException {
		List<String> gameIcon = new ArrayList<String>();// ͼ���ַ��Դ

		doc = Jsoup.connect("http://www.mxapk.com/list/2").get();
		Elements slideGameInfo = doc.getElementsByClass("tabcenter");
		Elements slideGameIcon = slideGameInfo.select("img");
		for (Element info : slideGameIcon) {
			String img = info.attr("src");
			if (img != null && img.trim().length() > 0) {
				gameIcon.add("http://www.mxapk.com" + img);
				Log.v("url", img);
			}
		}

		for (int i = 0; i < 10; i++) {
			URL url = new URL((String) gameIcon.get(i));
			URLConnection uc = url.openConnection();
			InputStream in = uc.getInputStream();
			Bitmap map = BitmapFactory.decodeStream(in);
			GetInfoOnMain.setGameIcon(i, map);
			in.close();
			Log.v("gameIcon.size", gameIcon.size() + "");
		}
	}

	/******* ��ϷӦ������ *******/
	public void getGameInfo() throws IOException {
		List<String> GameZhName = new ArrayList<String>();// ��������
		List<String> gameDownloadTimes = new ArrayList<String>();
		List<String> gameVersion = new ArrayList<String>();// apk�汾
		List<String> gameSize = new ArrayList<String>();// �����С
		List<String> gameTime = new ArrayList<String>();// ����ʱ��
		List<String> gameInstallUrl = new ArrayList<String>();// ���ص�ַ
		List<String> gameStar = new ArrayList<String>(); // �������
		List<String> gameUrl = new ArrayList<String>();
		doc = Jsoup.connect("http://www.mxapk.com/list/2").get();
		Elements slideGameInfo = doc.getElementsByClass("tabcenter");
		Elements slideGameZhName = slideGameInfo.select(".info");
		for (Element info : slideGameZhName) {
			Elements nameinfos = info.select(".title");
			for (Element names : nameinfos) {
				String name = names.text();
				GameZhName.add(name);
			}
		}

		for (Element downlaodTimes : slideGameZhName) {

			String timesInfo = downlaodTimes.text().toString();
			String regex = "\\b\\d*\\w*\\s*\\b \\b������\\b";
			Pattern p = Pattern.compile(regex);
			Log.v("info", timesInfo);
			Matcher m = p.matcher(timesInfo);
			while (m.find()) {
				String times = null;
				times = m.group();
				Log.v("times", times);
				gameDownloadTimes.add(times);
			}

		}

		Elements apkUrls = slideGameZhName.select("a");
		for (Element infoUrl : apkUrls) {
			String jumpUrl = infoUrl.attr("href");
			if (jumpUrl != null && jumpUrl.trim().length() > 0) {
				gameUrl.add("http://www.mxapk.com" + jumpUrl);
				Log.v("url", jumpUrl);
			}
		}

		for (Element downlaodTimes : slideGameZhName) {

			String timesInfo = downlaodTimes.text().toString();
			String regex;
			Pattern p;
			Matcher m;

			// �汾��Ϣ
			regex = "\\([^@]*\\)";
			p = Pattern.compile(regex);
			m = p.matcher(timesInfo);
			while (m.find()) {
				String version = null;
				version = m.group();
				Log.v("times", version);
				gameVersion.add(version);
			}
			// �����С
			regex = "\\d+(.\\d+)?GB|\\d+(.\\d+)?MB|\\d+(.\\d+)?KB|\\d+(.\\d+)?B";
			p = Pattern.compile(regex);
			m = p.matcher(timesInfo);
			while (m.find()) {
				String apksize = null;
				apksize = m.group();
				Log.v("times", apksize);
				gameSize.add(apksize);
			}
		}
		Elements downloadUrl = slideGameInfo.select(".xzbtn");
		for (Element info : downloadUrl) {
			String dUrl = info.attr("href");
			if (dUrl != null && dUrl.trim().length() > 0) {
				gameInstallUrl.add("http://www.mxapk.com" + dUrl);
				Log.v("durl", dUrl);
			}
		}
		// ����ʱ��
		Elements time = slideGameInfo.select(".icon");
		for (Element info : time) {
			gameTime.add(info.text());

		}
		for (int i = 0; i < 10; i++) {
			GetInfoOnMain.setGameName(i, GameZhName.get(i));
			GetInfoOnMain.setGameDownloadTimes(i, gameDownloadTimes.get(i));
			GetInfoOnMain.setGameVersion(i, gameVersion.get(i));
			GetInfoOnMain.setGameSize(i, gameSize.get(i));
			GetInfoOnMain.setGameTime(i, gameTime.get(i));
			GetInfoOnMain.setGameInstallUrl(i, gameInstallUrl.get(i));
			GetInfoOnMain.setGameUrl(i, gameUrl.get(i));
		}
	}
}
