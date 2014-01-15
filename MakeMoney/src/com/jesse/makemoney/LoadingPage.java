package com.jesse.makemoney;

import java.io.File;

import com.jesse.manager.InitData;
import com.jesse.util.MyView;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class LoadingPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_page);
		
		/******* 获取屏幕宽度 *******/
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		MyView.setScreenWidth(screenWidth);
		creatFile();
		
		/******初始化用户数据******/
		InitData initData = new InitData();
		initData.initDeviceId();
		initData.initUserData();
		
		Handler x = new Handler();
		x.postDelayed(new Runnable() {
			public void run() {
				Intent i = new Intent(LoadingPage.this, MainActivity.class);
				startActivity(i);
				overridePendingTransition(R.drawable.out_alpha, R.drawable.enter_alpha);
				LoadingPage.this.finish();
			}
		}, 2000);
	}

	/****** SD卡文件夹检测创建 *******/
	@SuppressLint("SdCardPath")
	private void creatFile(){
		
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) { 
			if(freeSpaceOnSd() <= 5){
				Toast.makeText(LoadingPage.this, "对不去，SD卡已满，请清理SD卡后再次进入软件！", Toast.LENGTH_LONG).show();
				finish();
				System.exit(0);
			} else{
				File file = new File("/sdcard/MakeMoney/cache");
				if (!file.exists()) {
					file.mkdirs();
				} 
			}
		} else {
			Toast.makeText(LoadingPage.this, "SD卡不可用！", Toast.LENGTH_LONG).show();
			finish();
			System.exit(0);
		}

		
	}
	/****** 获取SD卡剩余空间 *****/
	private int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / (1024*1024);
		return (int) sdFreeMB;
	}
}
