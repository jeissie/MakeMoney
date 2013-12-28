package com.loading;

import java.io.File;

import com.getimageandtext.GetImageOnMain;
import com.jesse.mxapk.MxApkActivity;
import com.jesse.mxapk.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @author jie
 * 
 */
public class MxOpenLogo extends Activity {
	/** Called when the activity is first created. */
	@SuppressLint({ "SdCardPath" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.open_logo);
		creatFile();//建立文件夹
		
		/******* 获取当前屏幕宽度 *******/
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		GetImageOnMain getImage = new GetImageOnMain();
		getImage.setScreenWidth(screenWidth);
		
		// 当前线程睡眠2秒
		Handler x = new Handler();
		x.postDelayed(new Runnable() {
			public void run() {
				Intent i = new Intent(MxOpenLogo.this, MxApkActivity.class);// 跳转主界面
				startActivity(i);
				overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);// 淡入淡出效果
				MxOpenLogo.this.finish();
			}
		}, 2000);

	}
	/****** 建立文件夹 *******/
	@SuppressLint("SdCardPath")
	private void creatFile(){
		
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			if(freeSpaceOnSd() <= 5){
				Toast.makeText(MxOpenLogo.this, "请清理SD卡", Toast.LENGTH_LONG).show();
				finish();
				System.exit(0);
			} else{
				File file = new File("/sdcard/MxApk/cache");
				if (!file.exists()) {
					file.mkdirs();// 创建文件夹
				} 
			}
		} else {
			Toast.makeText(MxOpenLogo.this, "SD卡不可用", Toast.LENGTH_LONG).show();
			finish();
			System.exit(0);
		}

		
	}
	/******获取SD剩余空间*****/
	private int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / (1024*1024);
		return (int) sdFreeMB;
	}
}
