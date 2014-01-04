package com.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.getimageandtext.ApkListViewInfo;

import android.util.Log;

/**
 * @author jie
 * ����б��е���Ϣ����
 */
public class JsoupApkListView {

	Document doc;
	
	public void getInfoOnListView(String url) throws IOException {
		List<String> apkIcon = new ArrayList<String>();// ���ͼ��
		List<String> apkZhName = new ArrayList<String>();//��������
		List<String> apkVersion = new ArrayList<String>();//apk�汾
		List<String> apkSize = new ArrayList<String>();//�����С
		List<String> apkTime = new ArrayList<String>();//����ʱ��
		List<String> apkDownloadTimes = new ArrayList<String>();//���ش���
		List<String> apkInstallUrl = new ArrayList<String>();//���ص�ַ
		List<String> apkStar = new ArrayList<String>(); //�������
		List<String> apkUrl = new ArrayList<String>();   //��ϸҳ���ַ
		String jumpUrl = null;  //��ת��ַ
		
		doc = Jsoup.connect(url).get();
		Elements slideApkInfo = doc.getElementsByClass("tabcenter");
		//ͼ���ַ
		Elements slideApkIcon = slideApkInfo.select("img");
		for (Element info : slideApkIcon) {
			String img = info.attr("src");
			if (img != null && img.trim().length() > 0) {
				apkIcon.add("http://www.mxapk.com" + img);
				Log.v("url", img);
			}
		}
		//������
		Elements slideApkZhName = slideApkInfo.select(".info");
		for (Element info : slideApkZhName) {
			Elements nameinfos = info.select(".title");
			for (Element names : nameinfos) {
				String name = names.text();
				apkZhName.add(name);
			}
			
		}
		
		Elements apkUrls = slideApkZhName.select("a");
		for (Element infoUrl : apkUrls) {
			String content = infoUrl.attr("href");
			if (content!= null && content.trim().length() > 0) {
				apkUrl.add("http://www.mxapk.com" + content);
			}
		}
		
		//���й�˾�����ش���
		for(Element downlaodTimes : slideApkZhName) {
			
			String timesInfo = downlaodTimes.text().toString();
			//���ش���
			String regex = "\\b\\d*\\w*\\s*\\b \\b������\\b";
			Pattern p = Pattern.compile(regex);
					Log.v("info", timesInfo);
			Matcher m = p.matcher(timesInfo);
			while (m.find()){
				String times = null;
				times = m.group();
				Log.v("times", times);
				apkDownloadTimes.add(times);
			} 
			//�汾��Ϣ
			regex = "\\([^@]*\\)";
			p = Pattern.compile(regex);
			m = p.matcher(timesInfo);
			while (m.find()){
				String version = null;
				version = m.group();
				Log.v("times", version);
				apkVersion.add(version);
			} 
			//�����С
			regex = "\\d+(.\\d+)?GB|\\d+(.\\d+)?MB|\\d+(.\\d+)?KB|\\d+(.\\d+)?B";
			p = Pattern.compile(regex);
			m = p.matcher(timesInfo);
			while (m.find()){
				String apksize = null;
				apksize = m.group();
				Log.v("times", apksize);
				apkSize.add(apksize);
			}
		}
		Elements downloadUrl = slideApkInfo.select(".xzbtn");
		for(Element info : downloadUrl) {
			String dUrl = info.attr("href");
			if (dUrl != null && dUrl.trim().length() > 0) {
				apkInstallUrl.add("http://www.mxapk.com" + dUrl);
				Log.v("durl", dUrl);
			}
		}
		//����ʱ��
		Elements time = slideApkInfo.select(".icon");
		for (Element info : time) {
			apkTime.add(info.text());
			
		}

		Elements jumpClass = doc.getElementsByClass("page");
		jumpClass = jumpClass.select(".next");
		for(Element info : jumpClass) {
			String dUrl = info.attr("href");
			if (dUrl != null && dUrl.trim().length() > 0) {
				jumpUrl = "http://www.mxapk.com" + dUrl;
				Log.v("jumpurlOnJsoup",jumpUrl);
			}else {
				jumpUrl = null;
			}
		}
		int index = apkIcon.size() > 10 ? 10 : apkIcon.size();
		for (int i = 0; i < index; i++) {
			ApkListViewInfo.setApkIcon(i,apkIcon.get(i));
			ApkListViewInfo.setApkZhName(i, apkZhName.get(i));
			ApkListViewInfo.setApkDownloadTimes(i, apkDownloadTimes.get(i));
			ApkListViewInfo.setApkVersion(i, apkVersion.get(i));
			ApkListViewInfo.setApkSize(i,apkSize.get(i));
			ApkListViewInfo.setApkTime(i,apkTime.get(i));
			ApkListViewInfo.setApkInstallUrl(i, apkInstallUrl.get(i));
			ApkListViewInfo.setApkUrl(i, apkUrl.get(i));	
		}
		
		ApkListViewInfo.setJumpUrl(jumpUrl);
	}
}
