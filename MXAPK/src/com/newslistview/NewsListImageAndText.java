package com.newslistview;

/**
 * @author jie
 *
 */
public class NewsListImageAndText {

	 private String imageUrl;
	 private String news_title; //中文名
	 private String time;        //发布时间
	 private String glance;
	 private String comment;
	 private String newsUrl;
	 
	 public NewsListImageAndText(String imageUrl, String news_title, String time, String glance,
			 String comment,String newsUrl) {
		 this.imageUrl = imageUrl;
		 this.news_title = news_title;
		 this.time = time;
		 this.glance = glance;
		 this.comment = comment;
		 this.newsUrl = newsUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getNews_title() {
		return news_title;
	}

	public String getTime() {
		return time;
	}

	public String getGlance() {
		return glance;
	}
	
	public String getComment() {
		return comment;
	}
	public String getNewUrl(){
		return newsUrl;
	}
}


