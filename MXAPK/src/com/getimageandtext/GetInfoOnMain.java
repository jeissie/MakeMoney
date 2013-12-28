package com.getimageandtext;

import android.R.integer;
import android.graphics.Bitmap;

public class GetInfoOnMain {

	private static String[] apkZhName = new String[10];
	private static String[] apkDownloadTimes = new String[10];
	private static Bitmap[] apkIcon = new Bitmap[10];
	private static String[] apkVersion = new String[10]; // 版本
	private static String[] apkSize = new String[10]; // 软件大小
	private static String[] apkTime = new String[10]; // 发布时间
	private static String[] apkInstallUrl = new String[10];// 下载地址
	private static String[] apkUrl = new String[10]; // 详细页面跳转地址
	private int[] apkStar = new int[10]; // 软件评级

	private static String[] gameName = new String[10];
	private static String[] gameDownloadTimes = new String[10];
	private static Bitmap[] gameIcon = new Bitmap[10];
	private static String[] gameVersion = new String[10]; // 版本
	private static String[] gameSize = new String[10]; // 软件大小
	private static String[] gameTime = new String[10]; // 发布时间
	private static String[] gameInstallUrl = new String[10];// 下载地址
	private static String[] gameUrl = new String[10]; // 详细页面跳转地址
	private int[] gameStar = new int[10]; // 软件评级

	/********* 应用 **********/
	public static String getApkVersion(int i) {
		return apkVersion[i];
	}

	public static void setApkVersion(int i, String apkVersion) {
		GetInfoOnMain.apkVersion[i] = apkVersion;
	}

	public static String getApkSize(int i) {
		return apkSize[i];
	}

	public static void setApkSize(int i, String apkSize) {
		GetInfoOnMain.apkSize[i] = apkSize;
	}

	public static String getApkTime(int i) {
		return apkTime[i];
	}

	public static void setApkTime(int i, String apkTime) {
		GetInfoOnMain.apkTime[i] = apkTime;
	}

	public static String getApkInstallUrl(int i) {
		return apkInstallUrl[i];
	}

	public static void setApkInstallUrl(int i, String apkInstallUrl) {
		GetInfoOnMain.apkInstallUrl[i] = apkInstallUrl;
	}

	public int[] getApkStar() {
		return apkStar;
	}

	public void setApkStar(int[] apkStar) {
		this.apkStar = apkStar;
	}

	public static String[] getApkDownloadTimes() {
		return apkDownloadTimes;
	}

	public static void setApkDownloadTimes(int i, String apkDownloadTimes) {
		GetInfoOnMain.apkDownloadTimes[i] = apkDownloadTimes;
	}

	public static String getApkUrl(int i) {
		return apkUrl[i];
	}

	public static void setApkUrl(int i, String apkUrl) {
		GetInfoOnMain.apkUrl[i] = apkUrl;
	}

	public static Bitmap[] getApkIcon() {
		return apkIcon;
	}

	public static void setApkIcon(int i, Bitmap apkIcon) {
		GetInfoOnMain.apkIcon[i] = apkIcon;
	}

	public static void setZhName(int i, String apkName) {
		GetInfoOnMain.apkZhName[i] = apkName;
	}

	public static String[] getZhName() {
		return apkZhName;
	}

	/********* 游戏 ***********/

	public static String[] getGameName() {
		return gameName;
	}

	public static String getGameVersion(int i) {
		return gameVersion[i];
	}

	public static void setGameVersion(int i, String gameVersion) {
		GetInfoOnMain.gameVersion[i] = gameVersion;
	}

	public static String getGameSize(int i) {
		return gameSize[i];
	}

	public static void setGameSize(int i, String gameSize) {
		GetInfoOnMain.gameSize[i] = gameSize;
	}

	public static String getGameTime(int i) {
		return gameTime[i];
	}

	public static void setGameTime(int i, String gameTime) {
		GetInfoOnMain.gameTime[i] = gameTime;
	}

	public static String getGameInstallUrl(int i) {
		return gameInstallUrl[i];
	}

	public static void setGameInstallUrl(int i, String gameInstallUrl) {
		GetInfoOnMain.gameInstallUrl[i] = gameInstallUrl;
	}

	public int[] getGameStar() {
		return gameStar;
	}

	public void setGameStar(int[] gameStar) {
		this.gameStar = gameStar;
	}

	public static void setGameName(int i, String gameName) {
		GetInfoOnMain.gameName[i] = gameName;
	}

	public static String[] getGameDownloadTimes() {
		return gameDownloadTimes;
	}

	public static void setGameDownloadTimes(int i, String gameDownloadTimes) {
		GetInfoOnMain.gameDownloadTimes[i] = gameDownloadTimes;
	}

	public static String getGameUrl(int i) {
		return gameUrl[i];
	}

	public static void setGameUrl(int i, String gameUrl) {
		GetInfoOnMain.gameUrl[i] = gameUrl;
	}

	public static Bitmap[] getGameIcon() {
		return gameIcon;
	}

	public static void setGameIcon(int i, Bitmap gameIcon) {
		GetInfoOnMain.gameIcon[i] = gameIcon;
	}
}
