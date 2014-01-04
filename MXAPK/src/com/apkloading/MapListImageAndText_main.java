package com.apkloading;

import android.graphics.drawable.Drawable;

public class MapListImageAndText_main {

	private String apk_zh_name; // 中文名
	private String apk_version; // 英文名
	private String time; // 发布时间
	private String apk_size; // 软件大小
	private Drawable apk_icon;// 软件图标

	public MapListImageAndText_main(Drawable apk_icon, String apk_zh_name, String apk_version,
			String time, String apk_size) {
		this.apk_zh_name = apk_zh_name;
		this.time = time;
		this.apk_size = apk_size;
		this.apk_icon = apk_icon;
		this.apk_version = apk_version;

	}

	public Drawable getApk_icon() {
		return apk_icon;
	}

	public String getApk_zh_name() {
		return apk_zh_name;
	}

	public String getApk_version() {
		return apk_version;
	}

	public String getTime() {
		return time;
	}

	public String getApk_size() {
		return apk_size;
	}
}
