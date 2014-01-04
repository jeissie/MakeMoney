package com.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.context.ContextUtil;
import com.getimageandtext.NewsPreviewInfo;

import android.R.string;
import android.util.Log;
import android.widget.Toast;

public class JsoupNewsPreview {

	Document doc;

	public void getInfoOnListView(String url) throws IOException {
		List<String> icon = new ArrayList<String>();// 新闻图标
		List<String> newsName = new ArrayList<String>();// 中文名称
		List<String> time = new ArrayList<String>();// 发布时间
		List<String> glance = new ArrayList<String>();// 浏览次数
		List<String> comment = new ArrayList<String>();// 评论次数
		List<String> jumpUrl = new ArrayList<String>();// 跳转地址

			doc = Jsoup.connect(url).get();
	
		
		Elements newsInfo = doc.getElementsByClass("item-panel");
		Elements newsIcon = newsInfo.select("img");
		for (Element info : newsIcon) {
			String img = info.attr("src");

			if (img != null && img.trim().length() > 0) {
				if (img.startsWith("http://")) {
					icon.add(img);
				} else {
					icon.add("http://www.mxapk.com" + img);
					Log.v("newsImg", img);
				}

			}
			if (img == null || img.trim().length() <= 0) {
				icon.add("empty");
			}
		}

		Elements newsTitle = newsInfo.select(".tit");
		for (Element info : newsTitle) {
			
				
				String name = info.text();
				newsName.add(name);
				Log.v("newsName", name);
			
		}
		newsTitle = newsTitle.select("a");
		for (Element info : newsTitle) {
			String newsUrl = info.attr("href");
			if (newsUrl != null && newsUrl.trim().length() > 0) {

				jumpUrl.add("http://www.mxapk.com" + newsUrl);
				Log.v("newsUrl", newsUrl);

			}
				
			}
		Elements newsTime = newsInfo.select(".timer");
		for (Element info : newsTime) {
			{
				String times = info.text();
				time.add(times);
				Log.v("newstime", times);
			}
		}

		Elements newsGlance = newsInfo.select(".view");
		for (Element info : newsGlance) {
			{
				String glances = info.text();
				glance.add(glances);
				Log.v("glance", glances);
			}
		}

		Elements newsComment = newsInfo.select(".cmts");
		for (Element info : newsComment) {
			{
				String comments = info.text();
				comment.add(comments);
				Log.v("comment", comments);
			}
		}

		for (int i = 0; i < newsName.size(); i++) {
			NewsPreviewInfo.setNewsimg(i, icon.get(i));
			NewsPreviewInfo.setTime(i, time.get(i));
			NewsPreviewInfo.setNewsName(i, newsName.get(i));
			NewsPreviewInfo.setComment(i, comment.get(i));
			NewsPreviewInfo.setGlance(i, glance.get(i));
			NewsPreviewInfo.setNewsUrl(i, jumpUrl.get(i));
		}
	}
}
