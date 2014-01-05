package com.jsoup;

import java.io.IOException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class JsoupSearch {

	Document doc;
	
	public boolean search(String content) throws IOException {
	
		doc = Jsoup.connect("http://www.mxapk.com/search/result?keywords=" + URLEncoder.encode(content, "UTF-8")).get();
		Elements slideApkInfo = doc.getElementsByClass("tabcenter");
		//Í¼±êµØÖ·
		Elements slideApkIcon = slideApkInfo.select("img");
		for (Element info : slideApkIcon) {
			String img = info.attr("src");
			if (img != null && img.trim().length() > 0) {
				return true;
			}
		}
		return false;
	}
}
