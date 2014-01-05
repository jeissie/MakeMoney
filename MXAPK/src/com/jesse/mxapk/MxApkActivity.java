package com.jesse.mxapk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.apk.ApkContent;
import com.apk.ApkPreview;
import com.exceptiondialog.ShowExceptionDialog;
import com.gallery.AptechObject;
import com.gallery.MyGallery;
import com.gallery.PageAdapter;
import com.gallery.Util;
import com.getimageandtext.GetImageOnMain;
import com.getimageandtext.GetInfoOnMain;
import com.jsoup.JsoupMain;
import com.jsoup.JsoupSearch;
import com.news.NewsPreview;
import com.search.SearchResult;
import com.setting.Setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author jeissie
 * 主页面
 */
@SuppressLint("HandlerLeak")
public class MxApkActivity extends Activity {

	private MyGallery gallery;
	private ImageView setting;
	private FrameLayout popular_items_View1, popular_items_View2,
			popular_items_View3;
	private TextView Table_L_text1_apk_name, Table_L_text2_apk_name,
			Table_L_text3_apk_name, Table_L_text4_apk_name,
			Table_L_text5_apk_name, Table_L_text6_apk_name,
			Table_L_text7_apk_name, Table_L_text8_apk_name,
			Table_L_text9_apk_name, Table_L_text10_apk_name;
	private TextView gameName1, gameName2, gameName3, gameName4, gameName5,
			gameName6, gameName7, gameName8, gameName9, gameName10;
	private TextView downloadTimes1, downloadTimes2, downloadTimes3,
			downloadTimes4, downloadTimes5, downloadTimes6, downloadTimes7,
			downloadTimes8, downloadTimes9, downloadTimes10;
	private TextView gameDownload1, gameDownload2, gameDownload3,
			gameDownload4, gameDownload5, gameDownload6, gameDownload7,
			gameDownload8, gameDownload9, gameDownload10;
	private TextView galleryName;
	private Button apk_Button, news_Button, search_button;
	public static AutoCompleteTextView search_textview;
	public static SlidingDrawer slidingDrawer_down;
	private LinearLayout linearLayout, table1, table2, table3, table4, table5,
			table6, table7, table8, table9, table10, table11, table12, table13,
			table14, table15, table16, table17, table18, table19, table20;
	private ImageView app1, app2, app3, app4, app5, app6, app7, app8, app9,
			app10;
	private ImageView game1, game2, game3, game4, game5, game6, game7, game8,
			game9, game10;
	private ProgressBar progressBar;
	private List<AptechObject> objects;
	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	private static Handler handler;
	private boolean ifSetDot = true;
	private JsoupMain jsoup = new JsoupMain();
	private boolean couldSend = true;
	String[] zhName = GetInfoOnMain.getZhName();
	String[] times = GetInfoOnMain.getApkDownloadTimes();
	String[] gameNames = GetInfoOnMain.getGameName();
	String[] gameTimes = GetInfoOnMain.getGameDownloadTimes();
	String seatchString = null;
	private ProgressDialog loginDialog;
	boolean resulit;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initButton(); // 按钮初始化
		initImage_text(); // 正中图片与文字
		couldSend = true;
		Looper looper = Looper.myLooper();
		handler = new MessageHandler(looper);
		try {
			initGallery();
		} catch (FileNotFoundException e) {
			if (couldSend) {
				progressBar.setVisibility(View.GONE);
				ShowExceptionDialog.showNetDialog(MxApkActivity.this,
						"本地读取异常，请检查网络连接！");
			}
			e.printStackTrace();
		} catch (IOException e) {
			if (couldSend) {
				progressBar.setVisibility(View.GONE);
				ShowExceptionDialog.showNetDialog(MxApkActivity.this,
						"本地读取异常，请检查网络连接！");
			}
			e.printStackTrace();
		}// 读取本地gallery资源

		getInfo();// 启动线程下载图片
		getAppInfo();// 线程下载Icon名称
		getAppIcon();// 启动线程下载20个apk信息
		initApkTable();
		initSearch(); // 搜索初始化

	}

	/********* 按钮初始化 **********/
	private void initButton() {
		apk_Button = (Button) findViewById(R.id.apk_button); // 中部左键
		news_Button = (Button) findViewById(R.id.news_button);// 中部右键
		search_button = (Button) findViewById(R.id.search_button);// 搜素按钮

		apk_Button.setOnClickListener(buttonOnClickListener); // apk按键监听
		news_Button.setOnClickListener(buttonOnClickListener); // 新闻按钮监听
		search_button.setOnClickListener(buttonOnClickListener);// 搜索按钮监听
	}

	/********* 按钮监听 *********/
	View.OnClickListener buttonOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.apk_button:
				closeDrawer();
				Intent intent1 = new Intent(MxApkActivity.this,
						ApkPreview.class);
				startActivity(intent1);
				break;
			case R.id.news_button:
				closeDrawer();
				Intent intent2 = new Intent(MxApkActivity.this,
						NewsPreview.class);
				startActivity(intent2);
				break;
			case R.id.search_button:
				if(!search_textview.getText().toString().equals("")) {
					loginDialog = new ProgressDialog(MxApkActivity.this);// 设置进度条
					loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置圆形
					loginDialog.setMessage("正在读取");
					loginDialog.show();
					seatchString = search_textview.getText().toString();
					search();
				}
				break;
			default:
				break;
			}

		}
	};

	/******* 搜索初始化 *********/
	private void initSearch() {
		slidingDrawer_down = (SlidingDrawer) findViewById(R.id.slidingDrawer); // 抽屉
		search_textview = (AutoCompleteTextView) findViewById(R.id.search_text);// 搜索栏
	}
	
	/****** 图片与文字 ******/
	private void initImage_text() {
		popular_items_View1 = (FrameLayout) findViewById(R.id.popular_items_View1);
		popular_items_View2 = (FrameLayout) findViewById(R.id.popular_items_View2);
		popular_items_View3 = (FrameLayout) findViewById(R.id.popular_items_View3);

		galleryName = (TextView) findViewById(R.id.gallery_name);

		setting = (ImageView) findViewById(R.id.setting); // 设置

		popular_items_View1.setOnClickListener(new pic_ClickListener());
		popular_items_View2.setOnClickListener(new pic_ClickListener());
		popular_items_View3.setOnClickListener(new pic_ClickListener());

		setting.setOnClickListener(new pic_ClickListener());

	}

	/********** 图片与文字监听 *************/
	class pic_ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.popular_items_View1:
				closeDrawer();
				startApkContent(3, 1);
				break;

			case R.id.popular_items_View2:
				closeDrawer();
				startApkContent(3, 2);
				break;
			case R.id.popular_items_View3:
				closeDrawer();
				startApkContent(3, 3);
				break;

			case R.id.setting:
				Intent intent = new Intent(MxApkActivity.this, Setting.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}

	}

	/****** Gallery初始化 *******/
	private void initGallery() throws FileNotFoundException, IOException {
		gallery = (MyGallery) findViewById(R.id.main_gallery); // 主屏
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		// 点击监听器
		gallery.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});

		// 动态添加选择点
		linearLayout = (LinearLayout) findViewById(R.id.gallery_conunt);

		objects = Util.getContent(this);
		if (objects != null) {
			int count = objects.size();
			if (count > 0) {
				PageAdapter adapter = new PageAdapter(this, objects);
				gallery.setAdapter(adapter);
				gallery.setOnItemSelectedListener(selectedListener);// 选择监听器
				setDot(count);
				ifSetDot = false;
			}
		}
	}

	/******* 设置图片滑动引导点 ********/
	private void setDot(int count) {
		for (int i = 0; i < count; i++) {
			ImageView imageView = new ImageView(this);
			if (i == 0) // 第一个默认点为亮点
			{
				imageView.setBackgroundResource(R.drawable.selected_dot);
			} else {
				imageView.setBackgroundResource(R.drawable.round);
			}
			imageView.setId(i); // 设置点的id从0开始
			linearLayout.addView(imageView, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

		}
	}

	private void clearDot() {
		int i = 0;
		if (objects != null) {
			while (i < objects.size()) {
				ImageView image = (ImageView) linearLayout.findViewById((i)
						% objects.size());
				if (image != null) {
					image.setBackgroundResource(R.drawable.round);
				}
				i++;
			}
		}

	}

	/******** gallery 选择监听器 ********/
	private OnItemSelectedListener selectedListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// 显示Apk名字
			if (GetImageOnMain.getMainGalleryName(arg2 % objects.size()) != null) {
				galleryName.setText(GetImageOnMain.getMainGalleryName(arg2
						% objects.size()));
			}
			// 获取选中linearLayout中imageview的id
			ImageView imageView = (ImageView) linearLayout.findViewById(arg2
					% objects.size());
			// 设置背景为亮点
			imageView.setBackgroundResource(R.drawable.selected_dot);
			// 获取当前亮点的左右点
			ImageView imageLeft = (ImageView) linearLayout
					.findViewById((arg2 - 1) % objects.size());
			ImageView imageRight = (ImageView) linearLayout
					.findViewById((arg2 + 1) % objects.size());
			// 边界判断
			if (imageLeft != null) {
				imageLeft.setBackgroundResource(R.drawable.round);
			}
			if (imageRight != null) {
				imageRight.setBackgroundResource(R.drawable.round);
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};

	private void initApkTable() {
		table1 = (LinearLayout) findViewById(R.id.table1);
		table2 = (LinearLayout) findViewById(R.id.table2);
		table3 = (LinearLayout) findViewById(R.id.table3);
		table4 = (LinearLayout) findViewById(R.id.table4);
		table5 = (LinearLayout) findViewById(R.id.table5);
		table6 = (LinearLayout) findViewById(R.id.table6);
		table7 = (LinearLayout) findViewById(R.id.table7);
		table8 = (LinearLayout) findViewById(R.id.table8);
		table9 = (LinearLayout) findViewById(R.id.table9);
		table10 = (LinearLayout) findViewById(R.id.table10);
		table11 = (LinearLayout) findViewById(R.id.table11);
		table12 = (LinearLayout) findViewById(R.id.table12);
		table13 = (LinearLayout) findViewById(R.id.table13);
		table14 = (LinearLayout) findViewById(R.id.table14);
		table15 = (LinearLayout) findViewById(R.id.table15);
		table16 = (LinearLayout) findViewById(R.id.table16);
		table17 = (LinearLayout) findViewById(R.id.table17);
		table18 = (LinearLayout) findViewById(R.id.table18);
		table19 = (LinearLayout) findViewById(R.id.table19);
		table20 = (LinearLayout) findViewById(R.id.table20);

		table1.setOnClickListener(new apkTableListener());
		table2.setOnClickListener(new apkTableListener());
		table3.setOnClickListener(new apkTableListener());
		table4.setOnClickListener(new apkTableListener());
		table5.setOnClickListener(new apkTableListener());
		table6.setOnClickListener(new apkTableListener());
		table7.setOnClickListener(new apkTableListener());
		table8.setOnClickListener(new apkTableListener());
		table9.setOnClickListener(new apkTableListener());
		table10.setOnClickListener(new apkTableListener());
		table11.setOnClickListener(new apkTableListener());
		table12.setOnClickListener(new apkTableListener());
		table13.setOnClickListener(new apkTableListener());
		table14.setOnClickListener(new apkTableListener());
		table15.setOnClickListener(new apkTableListener());
		table16.setOnClickListener(new apkTableListener());
		table17.setOnClickListener(new apkTableListener());
		table18.setOnClickListener(new apkTableListener());
		table19.setOnClickListener(new apkTableListener());
		table20.setOnClickListener(new apkTableListener());

		app1 = (ImageView) findViewById(R.id.Table_L_image1);
		app2 = (ImageView) findViewById(R.id.Table_L_image2);
		app3 = (ImageView) findViewById(R.id.Table_L_image3);
		app4 = (ImageView) findViewById(R.id.Table_L_image4);
		app5 = (ImageView) findViewById(R.id.Table_L_image5);
		app6 = (ImageView) findViewById(R.id.Table_L_image6);
		app7 = (ImageView) findViewById(R.id.Table_L_image7);
		app8 = (ImageView) findViewById(R.id.Table_L_image8);
		app9 = (ImageView) findViewById(R.id.Table_L_image9);
		app10 = (ImageView) findViewById(R.id.Table_L_image10);

		game1 = (ImageView) findViewById(R.id.Table_R_image1);
		game2 = (ImageView) findViewById(R.id.Table_R_image2);
		game3 = (ImageView) findViewById(R.id.Table_R_image3);
		game4 = (ImageView) findViewById(R.id.Table_R_image4);
		game5 = (ImageView) findViewById(R.id.Table_R_image5);
		game6 = (ImageView) findViewById(R.id.Table_R_image6);
		game7 = (ImageView) findViewById(R.id.Table_R_image7);
		game8 = (ImageView) findViewById(R.id.Table_R_image8);
		game9 = (ImageView) findViewById(R.id.Table_R_image9);
		game10 = (ImageView) findViewById(R.id.Table_R_image10);

		Table_L_text1_apk_name = (TextView) findViewById(R.id.Table_L_text1_apk_name);
		Table_L_text2_apk_name = (TextView) findViewById(R.id.Table_L_text2_apk_name);
		Table_L_text3_apk_name = (TextView) findViewById(R.id.Table_L_text3_apk_name);
		Table_L_text4_apk_name = (TextView) findViewById(R.id.Table_L_text4_apk_name);
		Table_L_text5_apk_name = (TextView) findViewById(R.id.Table_L_text5_apk_name);
		Table_L_text6_apk_name = (TextView) findViewById(R.id.Table_L_text6_apk_name);
		Table_L_text7_apk_name = (TextView) findViewById(R.id.Table_L_text7_apk_name);
		Table_L_text8_apk_name = (TextView) findViewById(R.id.Table_L_text8_apk_name);
		Table_L_text9_apk_name = (TextView) findViewById(R.id.Table_L_text9_apk_name);
		Table_L_text10_apk_name = (TextView) findViewById(R.id.Table_L_text10_apk_name);

		gameName1 = (TextView) findViewById(R.id.Table_R_text1_apk_name);
		gameName2 = (TextView) findViewById(R.id.Table_R_text2_apk_name);
		gameName3 = (TextView) findViewById(R.id.Table_R_text3_apk_name);
		gameName4 = (TextView) findViewById(R.id.Table_R_text4_apk_name);
		gameName5 = (TextView) findViewById(R.id.Table_R_text5_apk_name);
		gameName6 = (TextView) findViewById(R.id.Table_R_text6_apk_name);
		gameName7 = (TextView) findViewById(R.id.Table_R_text7_apk_name);
		gameName8 = (TextView) findViewById(R.id.Table_R_text8_apk_name);
		gameName9 = (TextView) findViewById(R.id.Table_R_text9_apk_name);
		gameName10 = (TextView) findViewById(R.id.Table_R_text10_apk_name);

		downloadTimes1 = (TextView) findViewById(R.id.Table_L_text1_downloads);
		downloadTimes2 = (TextView) findViewById(R.id.Table_L_text2_downloads);
		downloadTimes3 = (TextView) findViewById(R.id.Table_L_text3_downloads);
		downloadTimes4 = (TextView) findViewById(R.id.Table_L_text4_downloads);
		downloadTimes5 = (TextView) findViewById(R.id.Table_L_text5_downloads);
		downloadTimes6 = (TextView) findViewById(R.id.Table_L_text6_downloads);
		downloadTimes7 = (TextView) findViewById(R.id.Table_L_text7_downloads);
		downloadTimes8 = (TextView) findViewById(R.id.Table_L_text8_downloads);
		downloadTimes9 = (TextView) findViewById(R.id.Table_L_text9_downloads);
		downloadTimes10 = (TextView) findViewById(R.id.Table_L_text10_downloads);

		gameDownload1 = (TextView) findViewById(R.id.Table_R_text1_downloads);
		gameDownload2 = (TextView) findViewById(R.id.Table_R_text2_downloads);
		gameDownload3 = (TextView) findViewById(R.id.Table_R_text3_downloads);
		gameDownload4 = (TextView) findViewById(R.id.Table_R_text4_downloads);
		gameDownload5 = (TextView) findViewById(R.id.Table_R_text5_downloads);
		gameDownload6 = (TextView) findViewById(R.id.Table_R_text6_downloads);
		gameDownload7 = (TextView) findViewById(R.id.Table_R_text7_downloads);
		gameDownload8 = (TextView) findViewById(R.id.Table_R_text8_downloads);
		gameDownload9 = (TextView) findViewById(R.id.Table_R_text9_downloads);
		gameDownload10 = (TextView) findViewById(R.id.Table_R_text10_downloads);
	}

	/****** 读取Gallery图片线程 *****/
	private void getInfo() {
		new Thread() {
			@Override
			public void run() {
				try {
					jsoup.getInfoOnGallery(); // 获取网络图片资源
					sendMsg(1);
				} catch (IOException e) {
					sendMsg(100);
					e.printStackTrace();
				}
			}

		}.start();

	}

	/****** 读取Apk信息线程 *****/
	private void getAppInfo() {
		new Thread() {
			@Override
			public void run() {
				try {
					jsoup.getApkInfo();
					jsoup.getGameInfo();
					sendMsg(3);
				} catch (IOException e) {
					sendMsg(101);
					e.printStackTrace();
				}

			}
		}.start();
	}

	/****** 读取ApkIcon图片线程 *****/
	private void getAppIcon() {
		new Thread() {
			@Override
			public void run() {
				try {
					jsoup.getApkIconInfo();
					jsoup.getGameIconInfo();
					sendMsg(2);
				} catch (IOException e) {
					sendMsg(102);
					e.printStackTrace();
				}

			}
		}.start();
	}

	private static void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler.sendMessage(msg);
	}

	/******* 更改UI界面 ******/
	class MessageHandler extends Handler {
		public MessageHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				progressBar.setVisibility(View.GONE);
				clearDot();// 清楚引导点
				objects = null;
				try {
					objects = Util.getContent(MxApkActivity.this);// 将网络新获取资源显示在gallery上
				} catch (FileNotFoundException e) {
					if (couldSend) {
						ShowExceptionDialog.showNetDialog(MxApkActivity.this,
								"本地读取异常，请检查网络连接！");
					}
					e.printStackTrace();
				} catch (IOException e) {
					if (couldSend) {
						ShowExceptionDialog.showNetDialog(MxApkActivity.this,
								"网络连接，请检查网络连接！");
					}
					e.printStackTrace();
				}
				if (objects != null) {
					int count = objects.size();
					if (count > 0) {
						PageAdapter adapter = new PageAdapter(
								MxApkActivity.this, objects);
						gallery.setAdapter(adapter);
						gallery.setOnItemSelectedListener(selectedListener);// 选择监听器
						if (ifSetDot)
							setDot(count);
					}
				}
				break;

			case 2:
				setAppIcon();
				setGameIcon();
				break;
			case 3:
				setAppInfo();
				setGameInfo();
				break;

			case 100:
			case 101:
			case 102:
				if (couldSend) {
					loginDialog.cancel();
					ShowExceptionDialog.showNetDialog(MxApkActivity.this,
							":(，网络异常，请检查网络连接！");
				}
				break;
			}
		}
	}

	private void setAppIcon() {
		Bitmap[] icon = GetInfoOnMain.getApkIcon();
		if (icon != null && icon.length >= 10) {
			app1.setImageBitmap(icon[0]);
			app2.setImageBitmap(icon[1]);
			app3.setImageBitmap(icon[2]);
			app4.setImageBitmap(icon[3]);
			app5.setImageBitmap(icon[4]);
			app6.setImageBitmap(icon[5]);
			app7.setImageBitmap(icon[6]);
			app8.setImageBitmap(icon[7]);
			app9.setImageBitmap(icon[8]);
			app10.setImageBitmap(icon[9]);
		}
		icon = null;
	}

	private void setGameIcon() {
		Bitmap[] gameIcon = GetInfoOnMain.getGameIcon();
		if (gameIcon != null && gameIcon.length >= 10) {
			game1.setImageBitmap(gameIcon[0]);
			game2.setImageBitmap(gameIcon[1]);
			game3.setImageBitmap(gameIcon[2]);
			game4.setImageBitmap(gameIcon[3]);
			game5.setImageBitmap(gameIcon[4]);
			game6.setImageBitmap(gameIcon[5]);
			game7.setImageBitmap(gameIcon[6]);
			game8.setImageBitmap(gameIcon[7]);
			game9.setImageBitmap(gameIcon[8]);
			game10.setImageBitmap(gameIcon[9]);
		}
		gameIcon = null;
	}

	private void setAppInfo() {

		if (zhName != null && zhName.length >= 10) {
			Table_L_text1_apk_name.setText(zhName[0]);
			Table_L_text2_apk_name.setText(zhName[1]);
			Table_L_text3_apk_name.setText(zhName[2]);
			Table_L_text4_apk_name.setText(zhName[3]);
			Table_L_text5_apk_name.setText(zhName[4]);
			Table_L_text6_apk_name.setText(zhName[5]);
			Table_L_text7_apk_name.setText(zhName[6]);
			Table_L_text8_apk_name.setText(zhName[7]);
			Table_L_text9_apk_name.setText(zhName[8]);
			Table_L_text10_apk_name.setText(zhName[9]);
		}

		if (times != null && times.length >= 10) {
			downloadTimes1.setText(times[0]);
			downloadTimes2.setText(times[1]);
			downloadTimes3.setText(times[2]);
			downloadTimes4.setText(times[3]);
			downloadTimes5.setText(times[4]);
			downloadTimes6.setText(times[5]);
			downloadTimes7.setText(times[6]);
			downloadTimes8.setText(times[7]);
			downloadTimes9.setText(times[8]);
			downloadTimes10.setText(times[9]);
		}

	}

	private void setGameInfo() {

		if (gameNames != null && gameNames.length >= 10) {
			gameName1.setText(gameNames[0]);
			gameName2.setText(gameNames[1]);
			gameName3.setText(gameNames[2]);
			gameName4.setText(gameNames[3]);
			gameName5.setText(gameNames[4]);
			gameName6.setText(gameNames[5]);
			gameName7.setText(gameNames[6]);
			gameName8.setText(gameNames[7]);
			gameName9.setText(gameNames[8]);
			gameName10.setText(gameNames[9]);
		}

		if (gameTimes != null && gameTimes.length >= 10) {
			gameDownload1.setText(gameTimes[0]);
			gameDownload2.setText(gameTimes[1]);
			gameDownload3.setText(gameTimes[2]);
			gameDownload4.setText(gameTimes[3]);
			gameDownload5.setText(gameTimes[4]);
			gameDownload6.setText(gameTimes[5]);
			gameDownload7.setText(gameTimes[6]);
			gameDownload8.setText(gameTimes[7]);
			gameDownload9.setText(gameTimes[8]);
			gameDownload10.setText(gameTimes[9]);
		}

	}

	class apkTableListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.table1:
				startApkContent(1, 0);
				break;
			case R.id.table2:
				startApkContent(1, 1);
				break;
			case R.id.table3:
				startApkContent(1, 2);
				break;
			case R.id.table4:
				startApkContent(1, 3);
				break;
			case R.id.table5:
				startApkContent(1, 4);
				break;
			case R.id.table6:
				startApkContent(1, 5);
				break;
			case R.id.table7:
				startApkContent(1, 6);
				break;
			case R.id.table8:
				startApkContent(1, 7);
				break;
			case R.id.table9:
				startApkContent(1, 8);
				break;
			case R.id.table10:
				startApkContent(1, 9);
				break;
			case R.id.table11:
				startApkContent(2, 0);
				break;
			case R.id.table12:
				startApkContent(2, 1);
				break;
			case R.id.table13:
				startApkContent(2, 2);
				break;
			case R.id.table14:
				startApkContent(2, 3);
				break;
			case R.id.table15:
				startApkContent(2, 4);
				break;
			case R.id.table16:
				startApkContent(2, 5);
				break;
			case R.id.table17:
				startApkContent(2, 6);
				break;
			case R.id.table18:
				startApkContent(2, 7);
				break;
			case R.id.table19:
				startApkContent(2, 8);
				break;
			case R.id.table20:
				startApkContent(2, 9);
				break;

			}
		}

	}

	/******* activity跳转 ******/
	private void startApkContent(int i, int position) {

		switch (i) {
		case 1:
			if (times[9] != null) {
				Intent intent = new Intent(MxApkActivity.this, ApkContent.class);
				intent.putExtra("apkUrl", GetInfoOnMain.getApkUrl(position));
				intent.putExtra("apkName", zhName[position]);
				intent.putExtra("version",
						GetInfoOnMain.getApkVersion(position));
				intent.putExtra("apkSize", GetInfoOnMain.getApkSize(position));
				intent.putExtra("apkDownloadTimes", times[position]);
				intent.putExtra("apkTime", GetInfoOnMain.getApkTime(position));
				intent.putExtra("apkDownloadUrl",
						GetInfoOnMain.getApkInstallUrl(position));
				intent.putExtra("star", 3);
				startActivity(intent);
			}

			break;
		case 2:
			if (gameTimes[9] != null) {
				Intent intent = new Intent(MxApkActivity.this, ApkContent.class);
				intent.putExtra("apkUrl", GetInfoOnMain.getGameUrl(position));
				intent.putExtra("apkName", gameNames[position]);
				intent.putExtra("version",
						GetInfoOnMain.getGameVersion(position));
				intent.putExtra("apkSize", GetInfoOnMain.getGameSize(position));
				intent.putExtra("apkDownloadTimes", gameTimes[position]);
				intent.putExtra("apkTime", GetInfoOnMain.getGameTime(position));
				intent.putExtra("apkDownloadUrl",
						GetInfoOnMain.getGameInstallUrl(position));
				intent.putExtra("star", 3);
				startActivity(intent);
			}
			break;

		case 3:
			Intent intent = new Intent(MxApkActivity.this, ApkPreview.class);
			intent.putExtra("position", position);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	
	/*******搜索******/
	public void search(){
		final Handler handler = new Handler() {
			public void handleMessage(Message messages) {
				switch (messages.what) {
				case 1:
					loginDialog.cancel();
					if (resulit) {
						Intent intent = new Intent(MxApkActivity.this, SearchResult.class);
						intent.putExtra("name", seatchString);
						Log.v("seatchString",seatchString);
						startActivity(intent);
					} else {
						ShowExceptionDialog.showNetDialog(MxApkActivity.this, "抱歉，无搜索结果！");
					}
					break;
				case 2:
					loginDialog.cancel();
					ShowExceptionDialog.showNetDialog(MxApkActivity.this, "网络异常！");
					break;
				}
			}
		};
		
		new Thread() {

			@Override
			public void run() {

				try {
					final JsoupSearch search = new JsoupSearch();
					resulit = search.search(seatchString);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (IOException e) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
				
				

			}
		}.start();
	}

	public void closeDrawer() {
		// 关闭抽屉
		if (slidingDrawer_down.isOpened()) {
			slidingDrawer_down.animateClose();
		}
		// 隐藏键盘
		InputMethodManager keyboard = (InputMethodManager) getSystemService(MxApkActivity.INPUT_METHOD_SERVICE);
		keyboard.hideSoftInputFromWindow(search_textview.getWindowToken(), 0);
	}

	// 返回键重写
	Timer tExit = new Timer(); // 计时器
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && slidingDrawer_down.isOpened()) {
			closeDrawer();
		}

		if (keyCode == KeyEvent.KEYCODE_BACK && !slidingDrawer_down.isOpened()) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "再按一次后退键退出应用程序", Toast.LENGTH_SHORT)
						.show();
				if (!hasTask) {
					tExit.schedule(task, 2000);
				}
			} else {
				finish();
				System.exit(0);
			}
		}
		return false;
	}

	/****** 取消gallery的timer *****/
	@Override
	protected void onDestroy() {
		gallery.destroy();
	}

	/****** Back后停止主界面动画 *****/
	@Override
	protected void onPause() {
		MySurfaceView1.setFlag(false);
		MySurfaceView2.setFlag(false);
		MySurfaceView3.setFlag(false);
		couldSend = false;
		super.onPause();
	}

	/***** 重启当前activity时重启动画 ******/
	@Override
	protected void onRestart() {
		MySurfaceView1.setFlag(true);
		MySurfaceView2.setFlag(true);
		MySurfaceView3.setFlag(true);
		super.onRestart();
	}

	/****** 获取控件宽度 ******/
	/*
	 * private void getMAX_WIDTH() { ViewTreeObserver viewTreeObserver =
	 * layout_left .getViewTreeObserver(); // 获取控件宽度
	 * viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
	 * 
	 * @Override public boolean onPreDraw() { if (!hasMeasured) { window_width =
	 * getWindowManager().getDefaultDisplay() .getWidth();
	 * RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
	 * layout_left .getLayoutParams(); layoutParams.width = window_width;
	 * layout_left.setLayoutParams(layoutParams); MAX_WIDTH =
	 * layout_right.getWidth();
	 * 
	 * hasMeasured = true; } return true; } });
	 * 
	 * }
	 *//********** 屏幕手势 ***********/
	/*
	 * @Override public boolean onTouch(View v, MotionEvent event) { if
	 * (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
	 * RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
	 * layout_left .getLayoutParams(); // 缩回去 if (layoutParams.leftMargin <
	 * -window_width / 2) { new AsynMove().execute(-SPEED);
	 * Toast.makeText(MXAPKActivity.this, "缩回", Toast.LENGTH_SHORT).show(); }
	 * else { new AsynMove().execute(SPEED); } }
	 * 
	 * return mGestureDetector.onTouchEvent(event); }
	 * 
	 * class GestureListener implements OnGestureListener {
	 * 
	 * // 关闭主屏抽屉
	 * 
	 * @Override public boolean onDown(MotionEvent arg0) {
	 * 
	 * if (slidingDrawer_down.isOpened()) { slidingDrawer_down.animateClose(); }
	 * 
	 * mScrollX = 0; isScrolling = false;
	 * 
	 * return true; }
	 * 
	 * @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float
	 * arg2, float arg3) {
	 * 
	 * 
	 * // 向左滑 if (e1.getX() - e2.getX() > 100) {
	 * Toast.makeText(MXAPKActivity.this, "apkactivity",
	 * Toast.LENGTH_SHORT).show(); Intent intent = new
	 * Intent(MXAPKActivity.this, News_preview.class); startActivity(intent);
	 * overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left); } //
	 * 向右滑 if (e2.getX() - e1.getX() > 100) { Intent intent = new
	 * Intent(MXAPKActivity.this, Apk_preview.class); startActivity(intent);
	 * overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left); }
	 * 
	 * return false; }
	 * 
	 * @Override public void onLongPress(MotionEvent arg0) {
	 * 
	 * }
	 *//*** e1 是起点，e2是终点，如果distanceX=e1.x-e2.x>0说明向左滑动。反之亦如此. */
	/*
	 * @Override public boolean onScroll(MotionEvent arg0, MotionEvent arg1,
	 * float arg2, float arg3) {
	 * 
	 * isScrolling = true; mScrollX += arg2;// arg2:向左为正，右为负
	 * RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
	 * layout_left .getLayoutParams(); layoutParams.leftMargin -= mScrollX; if
	 * (layoutParams.leftMargin >= 0) { isScrolling = false;//
	 * 拖过头了不需要再执行AsynMove了 layoutParams.leftMargin = 0;
	 * 
	 * } else if (layoutParams.leftMargin <= -MAX_WIDTH) { //
	 * 拖过头了不需要再执行AsynMove了 isScrolling = false; layoutParams.leftMargin =
	 * -MAX_WIDTH; } layout_left.setLayoutParams(layoutParams); return false;
	 * 
	 * }
	 * 
	 * @Override public void onShowPress(MotionEvent e) { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * @Override public boolean onSingleTapUp(MotionEvent e) {
	 * 
	 * RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
	 * layout_left .getLayoutParams(); // 左移动 if (layoutParams.leftMargin >= 0)
	 * { new AsynMove().execute(-SPEED); } else { // 右移动 new
	 * AsynMove().execute(SPEED); }
	 * 
	 * return true;
	 * 
	 * } }
	 * 
	 * 
	 * 
	 * class AsynMove extends AsyncTask<Integer, Integer, Void> {
	 * 
	 * @Override protected Void doInBackground(Integer... params) { int times =
	 * 0; if (MAX_WIDTH % Math.abs(params[0]) == 0)// 整除 times = MAX_WIDTH /
	 * Math.abs(params[0]); else times = MAX_WIDTH / Math.abs(params[0]) + 1;//
	 * 有余数
	 * 
	 * for (int i = 0; i < times; i++) { publishProgress(params[0]); try {
	 * Thread.sleep(Math.abs(params[0])); } catch (InterruptedException e) {
	 * e.printStackTrace(); } }
	 * 
	 * return null; }
	 *//**
	 * update UI
	 */
	/*
	 * @Override protected void onProgressUpdate(Integer... values) {
	 * RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
	 * layout_left .getLayoutParams(); // 右移动 if (values[0] > 0) {
	 * layoutParams.leftMargin = Math.min(layoutParams.leftMargin + values[0],
	 * 0);
	 * 
	 * } else { // 左移动 layoutParams.leftMargin =
	 * Math.max(layoutParams.leftMargin + values[0], -MAX_WIDTH);
	 * 
	 * } layout_left.setLayoutParams(layoutParams);
	 * 
	 * }
	 * 
	 * }
	 */
}