package com.jesse.obj;

public abstract class ApkItem {
	 protected String imageUrl;    		 //IconURL
	 protected String apk_zh_name; 		 //软件名称
	 protected String apk_size;    		 //软件大小
	 protected String downloadUrl;       //下载地址
	 protected int star;			     //软件评级
	 protected int moeny;				 //返利数量
	 protected boolean installed;		 //是否有安装过
	 
	 public ApkItem(String imageUrl, String apk_zh_name, String apk_size, String downloadUrl, int star, int money, boolean installed) {
		 this.imageUrl = imageUrl;
		 this.apk_zh_name = apk_zh_name;
		 this.apk_size = apk_size;
		 this.downloadUrl = downloadUrl;
		 this.star = star;
		 this.moeny = moeny;
		 this.installed = installed;
	 } 
	 
	 public void downloadApk () {};
	 public void showDetail () {};
	 public void open () {};
}
