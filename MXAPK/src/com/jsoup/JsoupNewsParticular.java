package com.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.getimageandtext.NewsParticularInfo;


public class JsoupNewsParticular {

	Document doc;

	public void getNewsParticular(String url) throws IOException {
		List<String> images = new ArrayList<String>();// 新闻中的图片地址
		
		doc = Jsoup.connect(url).get();
		String newsInfo = doc.getElementsByClass("article-content").html();//新闻文字内容
		Log.v("newsifno",newsInfo);
		NewsParticularInfo.setNewsHtml(newsInfo);
		
		Elements imageInfo = doc.getElementsByClass("article-content");
		Elements icon = imageInfo.select("img");
		for (Element info : icon) {
			String img = info.attr("src");
			if (img != null && img.trim().length() > 0) {
				if (img.startsWith("http://")) {
					images.add(img);
				} else {
					images.add("http://www.mxapk.com" + img);
				}

			}
		}
		
		NewsParticularInfo.setImageUrl(images);
	}
}
