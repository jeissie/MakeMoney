package com.newslistview;

import com.jesse.mxapk.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jie
 *
 */
public class NewsListViewCache {

	 private View baseView;
	 private ImageView imageUrl;
	 private TextView news_title;
	 private TextView time;
	 private TextView glance;
	 private TextView comment;
	 
	 public NewsListViewCache(View baseView) {
		this.baseView = baseView;
	 }
	 
	 public TextView getNews_title() {
		 if (news_title== null) {
			 news_title = (TextView) baseView.findViewById(R.id.news_title);
		 }
		 return news_title;
	}

	 public TextView getTime() {
		 if (time== null) {
			 time = (TextView) baseView.findViewById(R.id.news_time);
		 }
		 return time;
	}

	 public ImageView getImageView() {
		 if (imageUrl== null) {
			 imageUrl = (ImageView) baseView.findViewById(R.id.news_image);
		 }
		 return imageUrl;
	}
	 
	 public TextView getGlance() {
		 if (glance== null) {
			 glance = (TextView) baseView.findViewById(R.id.glance);
		 }
		 return glance;
	}
	 
	 public TextView getComment() {
		 if (comment== null) {
			 comment = (TextView) baseView.findViewById(R.id.comment);
		 }
		 return comment;
	}
}
