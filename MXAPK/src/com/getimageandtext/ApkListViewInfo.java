package com.getimageandtext;

import android.R.integer;

public class ApkListViewInfo {
	private static String[] apkZhName = new String[10];  //������
	private static String[] apkVersion = new String[10];  //�汾
 	private static String[] apkSize = new String[10];    //�����С
	private static String[] apkTime = new String[10];    //����ʱ��
	private static String[] apkDownloadTimes = new String[10];  //���ش���
	private static String[] apkInstallUrl = new String[10];//���ص�ַ
	private static String[] apkUrl = new String[10];  //��ϸҳ����ת��ַ
	private static String[] apkIcon = new String[10]; //���ICON
	private int[] apkStar = new int[10];             //�������
	private static String jumpUrl = null;             //��һҳ��ַ
	
	
	public static String getApkZhName(int i) {
		return apkZhName[i];
	}
	public static void setApkZhName(int i, String apkZhName) {
		ApkListViewInfo.apkZhName[i] = apkZhName;
	}
	public static String getApkVersion(int i ) {
		return apkVersion[i];
	}
	public static void setApkVersion(int i, String apkEnName) {
		ApkListViewInfo.apkVersion[i] = apkEnName;
	}
	public static String getApkSize(int i) {
		return apkSize[i];
	}
	public static void setApkSize(int i, String apkSize) {
		ApkListViewInfo.apkSize[i] = apkSize;
	}
	public static String getApkTime(int i) {
		return apkTime[i];
	}
	public static void setApkTime(int i,String apkTime) {
		ApkListViewInfo.apkTime[i] = apkTime;
	}
	public static String getApkDownloadTimes(int i) {
		return apkDownloadTimes[i];
	}
	public static void setApkDownloadTimes(int i, String apkDownloadTimes) {
		ApkListViewInfo.apkDownloadTimes[i] = apkDownloadTimes;
	}
	public static String getApkUrl(int i) {
		return apkUrl[i];
	}
	public static void setApkUrl(int i, String apkUrl) {
		ApkListViewInfo.apkUrl[i] = apkUrl;
	}
	public static String getApkIcon(int i) {
		return apkIcon[i];
	}
	public static void setApkIcon(int i, String apkIcon) {
		ApkListViewInfo.apkIcon[i] = apkIcon;
	}
	public static int getLength(){
		return  apkIcon.length;
	}
	public static String getJumpUrl() {
		return jumpUrl;
	}
	public static void setJumpUrl(String junpUrl) {
		ApkListViewInfo.jumpUrl = junpUrl;
	}
	public int[] getApkStar() {
		return apkStar;
	}
	public void setApkStar(int[] apkStar) {
		this.apkStar = apkStar;
	}
	public static String getApkInstallUrl(int i) {
		return apkInstallUrl[i];
	}
	public static void setApkInstallUrl(int i, String apkInstallUrl) {
		ApkListViewInfo.apkInstallUrl[i] = apkInstallUrl;
	}
	
	
}
