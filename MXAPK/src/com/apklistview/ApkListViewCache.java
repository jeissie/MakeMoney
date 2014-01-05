package com.apklistview;

import com.jesse.mxapk.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @author jie
 *
 */
public class ApkListViewCache {

	 private View baseView;
	 private ImageView imageUrl,image_star;
	 private TextView apk_zh_name,apk_en_name;
	 private TextView time;
	 private TextView apk_size;
	 private TextView downloads;
	 
	 public ApkListViewCache(View baseView) {
		this.baseView = baseView;
	 }
	 
	 public TextView getApk_zh_name() {
		 if (apk_zh_name== null) {
			 apk_zh_name = (TextView) baseView.findViewById(R.id.apk_zh_name);
		 }
		 return apk_zh_name;
	}
	 
	 public TextView getApk_en_name() {
		 if (apk_en_name== null) {
			 apk_en_name = (TextView) baseView.findViewById(R.id.apk_en_name);
		 }
		 return apk_en_name;
	}
	 
	 public TextView getApk_size() {
		 if (apk_size== null) {
			 apk_size = (TextView) baseView.findViewById(R.id.apk_size);
		 }
		 return apk_size ;
	}
	 
	 public TextView getTime() {
		 if (time== null) {
			 time = (TextView) baseView.findViewById(R.id.issue_time);
		 }
		 return time;
	}
	 public TextView getDownloads() {
		 if (downloads== null) {
			 downloads = (TextView) baseView.findViewById(R.id.downloads);
		 }
		 return downloads;
	}
	 public ImageView getImageView_icon() {
		 if (imageUrl== null) {
			 imageUrl = (ImageView) baseView.findViewById(R.id.apk_icon);
		 }
		 return imageUrl;
	}
	 
	 public ImageView getImageView_star(){
		 if(image_star == null){
			 image_star = (ImageView) baseView.findViewById(R.id.ratingBar);
		 }
		 return image_star;
	 }
}
