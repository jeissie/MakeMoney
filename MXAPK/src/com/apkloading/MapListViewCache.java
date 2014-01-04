package com.apkloading;

import com.jesse.mxapk.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MapListViewCache {

	private View baseView;
	private ImageView imageicon;
	private TextView apk_zh_name, apk_version;
	private TextView time;
	private TextView apk_size;
	
	public MapListViewCache(View baseView) {
		this.baseView = baseView;
	 }
	
	public ImageView getImageView_icon() {
		 if (imageicon== null) {
			 imageicon = (ImageView) baseView.findViewById(R.id.apk_icon);
		 }
		 return imageicon;
	}
	
	public TextView getApk_zh_name() {
		 if (apk_zh_name== null) {
			 apk_zh_name = (TextView) baseView.findViewById(R.id.apk_name);
		 }
		 return apk_zh_name;
	}
	
	public TextView getApk_version() {
		 if (apk_version== null) {
			 apk_version = (TextView) baseView.findViewById(R.id.apk_version);
		 }
		 return apk_version;
	}
	
	public TextView getApk_size() {
		 if (apk_size== null) {
			 apk_size = (TextView) baseView.findViewById(R.id.apk_size);
		 }
		 return apk_size ;
	}
	
	 public TextView getTime() {
		 if (time== null) {
			 time = (TextView) baseView.findViewById(R.id.apk_issue_time);
		 }
		 return time;
	}
}
