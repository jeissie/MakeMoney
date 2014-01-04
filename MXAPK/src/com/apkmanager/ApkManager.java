/**
 * 
 */
package com.apkmanager;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import com.apk.ApkContent;
import com.apkloading.MapListApkAdapter_content1;
import com.apkloading.MapListImageAndText_main;
import com.jesse.mxapk.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jie
 * 
 */
public class ApkManager extends Activity {

	private ListView apk_installed, apk_upversion, apk_downloaded;
	private ViewPager paper_content;
	private List<View> listViews; // 页面列表
	private Button backButton;
	private ImageView cursor;// 动画图片
	private TextView t1, t2, t3;
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private long codesize = 0; // 应用程序大小
	private List<MapListImageAndText_main> dataArray; // listview内容列表
	private ProgressDialog loginDialog; // 进度条
	private GetInfo getInfo; // AsyncTask
	private List<PackageInfo> packages; // apk信息列表
	private PackageInfo packageInfo;
	private int apkList_position[][];  //对齐listview位置
	public static String apk_info[][];  //apk包名列表
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apk_manager);
		InitImageView(); // 初始化动画
		InitTextView(); // 初始化头标
		InitViewPager(); // 初始化选项卡
		dataArray = new ArrayList<MapListImageAndText_main>(); // listview内容列表
		loginDialog = new ProgressDialog(ApkManager.this);// 设置进度条
		getInfo = new GetInfo();// AsyncTask读取信息
		packages = getPackageManager().getInstalledPackages(0);// apk信息列表
		packageInfo = new PackageInfo();
		getInfo.execute("getInfo");// 添加列表项内容
		
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	/******* 头标初始化 *******/
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
	}

	/******* 头标监听 *******/
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			paper_content.setCurrentItem(index);
		}
	};

	/***** 页卡内容初始化 ******/
	private void InitViewPager() {
		paper_content = (ViewPager) findViewById(R.id.viewpager_content);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		for (int i = 0; i < 3; i++) {
			listViews.add(mInflater.inflate(R.layout.listview, null));
		}
		paper_content.setAdapter(new MyPagerAdapter(listViews));
		paper_content.setCurrentItem(0); // 初始页面编号
		paper_content.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/****** 初始化动画 ******/
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/***** ViewPager适配器 ******/
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		// 获取当前窗体界面数
		@Override
		public int getCount() {
			return mListViews.size();
		}

		// 初始化position位置的界面
		@Override
		public Object instantiateItem(View arg0, int arg1) {

			if (arg1 < 3) {
				((ViewPager) arg0).addView(mListViews.get(arg1 % 3), 0);
			}
			// 测试页卡1内的按钮事件
			if (arg1 == 0) {
				apk_installed = (ListView) findViewById(R.id.listView);
				
				final PackageManager pm = ApkManager.this.getPackageManager();
				
				apk_installed.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
						
						Toast.makeText(ApkManager.this, "item"+arg2, Toast.LENGTH_SHORT).show();
						packageInfo = packages.get(apkList_position[arg2][0]);
						Drawable apk_icon = packageInfo.applicationInfo
								.loadIcon(getPackageManager());
						// Apk名称
						final String apk_zh_name = (String) packageInfo.applicationInfo
								.loadLabel(getPackageManager());
						// Apk版本名称
						String apk_version = packageInfo.versionName;
						// Apk安装时间
						long systime = packageInfo.firstInstallTime;
						String time = (String) DateFormat.format("yyyy-MM-dd",
								systime);
						// Apk大小
						try {
							queryPacakgeSize(packageInfo.packageName);
							packageInfo = pm.getPackageInfo(packageInfo.packageName,
									PackageManager.GET_PERMISSIONS);
						} catch (Exception e) {
							e.printStackTrace();
						}
						String apk_size = Formatter.formatFileSize(
								ApkManager.this, codesize); // Apk大小
						
						//读取应用权限
						String[] permission = packageInfo.requestedPermissions;
						//弹出自定义对话框
						final Dialog dialog = new Dialog(ApkManager.this);

						dialog.setContentView(R.layout.apk_info_dialog);
						dialog.setTitle("基本信息");

						ImageView icon = (ImageView) dialog.findViewById(R.id.apk_icon);
						icon.setImageDrawable(apk_icon);
						TextView name = (TextView) dialog.findViewById(R.id.apk_name_single);
						name.setText(apk_zh_name);
						TextView version = (TextView) dialog.findViewById(R.id.version);
						version.setText(apk_version);
						TextView size = (TextView) dialog.findViewById(R.id.size);
						size.setText(apk_size);
						TextView install_time = (TextView) dialog.findViewById(R.id.install_time);
						install_time.setText(time);
						TextView permissions = (TextView) dialog.findViewById(R.id.permission);
						String permission_content = "";
						if (permission != null) {
							for (int i = 0; i < permission.length; i++) {
								permission_content = permission_content + permission[i] + "\n";
							}
							permissions.setText(permission_content);
						} else{
							permissions.setText("无");
						}

						Button open = (Button) dialog
								.findViewById(R.id.open_button);
						open.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								//启动应用
								Intent intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
								startActivity(intent);
								onBackPressed();
								dialog.cancel();
							}
						});
						
						Button more = (Button) dialog
								.findViewById(R.id.more_button);
						more.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// 跳转至应用相信信息
								/*Intent intent = new Intent(ApkManager.this,ApkContent.class);
								startActivity(intent);
								dialog.cancel();*/
							}
						});
						
						dialog.show();
					}
				});

			}
			if (arg1 == 1) {

			}
			if (arg1 == 2) {

			}
			return mListViews.get(arg1 % 3);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/******** 页面切换监听 ********/
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		// 当前选中页面顶部滑动条动画
		public void onPageSelected(int arg0) {
			Animation animation = null;

			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(200);
			cursor.startAnimation(animation);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private class GetInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onCancelled() {

			super.onCancelled();
		}

		@SuppressWarnings("static-access")
		@Override
		protected Object doInBackground(Object... params) {

			int amuont = 0;
			apkList_position = new int[packages.size()][1];
			apk_info = new String[packages.size()][1];
			for (int i = 0; i < packages.size(); i++) {
				packageInfo = packages.get(i);
				
				// 获取非系统应用
				if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0) {
					
					apkList_position[amuont][0] = i;//对齐listview
					apk_info [amuont][0] = packageInfo.packageName;//保存包名，对齐listview
					// Apk的icon
					Drawable apk_icon = packageInfo.applicationInfo
							.loadIcon(getPackageManager());
					// Apk名称
					String apk_zh_name = (String) packageInfo.applicationInfo
							.loadLabel(getPackageManager());
					// Apk版本名称
					String apk_version = packageInfo.versionName;
					// Apk安装时间
					long systime = packageInfo.firstInstallTime;
					String time = (String) DateFormat.format("yyyy-MM-dd",
							systime);
					// Apk大小
					try {
						queryPacakgeSize(packageInfo.packageName);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String apk_size = Formatter.formatFileSize(
							ApkManager.this, codesize); // Apk大小

					// 填充数据行
					MapListImageAndText_main test = new MapListImageAndText_main(
							apk_icon, apk_zh_name, apk_version, time, apk_size);
					dataArray.add(test);
					amuont++;
				}
			}
			return null;
		}

		// 更新主线程
		@Override
		protected void onPostExecute(Object result) {

			MapListApkAdapter_content1 adapter = new MapListApkAdapter_content1(
					ApkManager.this, dataArray, apk_installed);
			apk_installed.setAdapter(adapter);
			loginDialog.cancel();
			super.onPostExecute(result);

		}

		// 设置预处理进度条
		@Override
		protected void onPreExecute() {

			loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置圆形
			loginDialog.setMessage("正在读取");
			loginDialog.setCancelable(true); // 退回键取消
			loginDialog.show();
			super.onPreExecute();

		}

	}

	public void queryPacakgeSize(String pkgName) throws Exception {
		if (pkgName != null) {
			// 使用放射机制得到PackageManager类的隐藏函数getPackageSizeInfo
			PackageManager pm = getPackageManager(); // 得到pm对象
			try {
				// 通过反射机制获得该隐藏函数
				Method getPackageSizeInfo = pm.getClass().getDeclaredMethod(
						"getPackageSizeInfo", String.class,
						IPackageStatsObserver.class);
				// 调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数
				getPackageSizeInfo.invoke(pm, pkgName, new PkgSizeObserver());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw ex; // 抛出异常
			}
		}
	}

	// aidl文件形成的Bindler机制服务类
	public class PkgSizeObserver extends IPackageStatsObserver.Stub {
		/***
		 * 回调函数，
		 * 
		 * @param pStatus
		 *            ,返回数据封装在PackageStats对象中
		 * @param succeeded
		 *            代表回调成功
		 */
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			codesize = pStats.codeSize; // 应用程序大小
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			getInfo.onCancelled();
			loginDialog.cancel();
			onBackPressed();
		}
		return false;
	}
	
	public String apk_position(int position){	
		packageInfo = packages.get(apkList_position[position][0]);
		return packageInfo.packageName;
	}
	
	//安装或卸载应用后，重新读取数据
	@Override
	protected void onRestart() {
		this.onCreate(null)	;
		}
}
