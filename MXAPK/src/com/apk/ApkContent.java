package com.apk;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exceptiondialog.ShowExceptionDialog;
import com.getimageandtext.ApkContentInfo;
import com.getimageandtext.DownloadApk;
import com.jesse.mxapk.R;
import com.jsoup.JsoupApkContent;
import com.report.Report;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jie 此页面是展示单个Apk的详细页面
 */
@SuppressLint("HandlerLeak")
public class ApkContent extends Activity {

	private Button comment_button, download_button, more_button, back_button;
	private TextView apk_name, apk_times, apk_size, apk_version, apk_author,
			apk_publish, t1, t2, t3, t4, download_image;
	private Gallery apk_content_gallery;
	private ImageView apk_star, apkicon;
	private ViewPager paper_content;
	private List<View> listViews; // 页面列表
	private ImageView cursor;// 动画图片
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private int ONE = Menu.FIRST;
	private int TWO = Menu.FIRST + 1;
	private int THREE = Menu.FIRST + 2;
	private int FOUR = Menu.FIRST + 3;
	private String apkUrl, apkName, version, apkSize, apkDownloadTimes,
			apkTime, apkDownloadUrl;
	private int star;
	private Handler handlers;
	private ProgressDialog loginDialog;
	private List<Drawable> mps;
	private boolean couldSend = true;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.apk_content_view);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			apkUrl = extras.getString("apkUrl"); // 页面地址
			apkName = extras.getString("apkName");// 软件下载地址
			version = extras.getString("version");// 软件版本
			apkSize = extras.getString("apkSize");// 软件体积
			apkDownloadTimes = extras.getString("apkDownloadTimes");// 下载次数
			apkTime = extras.getString("apkTime");// 发布时间
			star = extras.getInt("star");
			apkDownloadUrl = extras.getString("apkDownloadUrl");// 下载地址
		}
		loginDialog = new ProgressDialog(this);// 设置进度条
		couldSend = true;
		InitButton(); // 按键初始化
		InitTextview();// 文字初始化
		InitImageView(); // 初始化动画
		InitTextView_title(); // 初始化头标
		InitViewPager(); // 初始化页面
		Star(star);
		loadingIcon(apkUrl);
		loadingGallery(apkUrl);

		apkicon = (ImageView) findViewById(R.id.apk_icon);

		Looper looper = Looper.myLooper();
		handlers = new MessageHandler(looper);

	}

	/***** 按键初始化 ******/
	private void InitButton() {
		comment_button = (Button) findViewById(R.id.comment_button);
		download_button = (Button) findViewById(R.id.download_button);
		more_button = (Button) findViewById(R.id.more_button);
		back_button = (Button) findViewById(R.id.back_button);
		download_image = (TextView) findViewById(R.id.download_image);
		
		comment_button.setOnClickListener(buttonListener);
		download_button.setOnClickListener(buttonListener);
		more_button.setOnClickListener(buttonListener);
		back_button.setOnClickListener(buttonListener);
		download_image.setOnClickListener(buttonListener);
	}

	/***** 按键监听 ******/
	View.OnClickListener buttonListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.back_button:
				onBackPressed();
				break;

			case R.id.comment_button:
				Intent intent = new Intent(ApkContent.this, Report.class);
				startActivity(intent);
				break;
			case R.id.download_button:
			case R.id.download_image:
				DownloadApk.downLoadApk(apkName,apkDownloadUrl);
				Toast.makeText(ApkContent.this, apkName+"准备下载", Toast.LENGTH_SHORT).show();
				break;
			case R.id.more_button:
				openOptionsMenu(); // 打开选项菜单
				break;
			}

		}
	};

	private void Star(int amount) {
		apk_star = (ImageView) findViewById(R.id.apk_star);
		switch (amount) {
		case 1:
			apk_star.setImageResource(R.drawable.star_1);
			break;
		case 2:
			apk_star.setImageResource(R.drawable.star_2);
			break;
		case 3:
			apk_star.setImageResource(R.drawable.star_3);
			break;
		case 4:
			apk_star.setImageResource(R.drawable.star_4);
			break;
		case 5:
			apk_star.setImageResource(R.drawable.star_5);
			break;
		default:
			apk_star.setImageResource(R.drawable.star_0);
			break;
		}
	}

	/****** 文字初始化 *******/
	private void InitTextview() {
		apk_name = (TextView) findViewById(R.id.apk_name);
		apk_times = (TextView) findViewById(R.id.apk_times);
		apk_size = (TextView) findViewById(R.id.apk_size);
		apk_version = (TextView) findViewById(R.id.apk_version);
		apk_author = (TextView) findViewById(R.id.apk_author);
		apk_publish = (TextView) findViewById(R.id.apk_publish);

		apk_name.setText(apkName);
		apk_times.setText(apkDownloadTimes);
		apk_size.setText(apkSize);
		apk_version.setText(version);
		apk_publish.setText(apkTime);
	}

	/******* 头标初始化 *******/
	private void InitTextView_title() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);
		t4 = (TextView) findViewById(R.id.text4);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
		t4.setOnClickListener(new MyOnClickListener(3));
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

	/***** Gallery初始化 *******/
	private void Gallery() {

		apk_content_gallery = (Gallery) findViewById(R.id.apk_content_gallerys);
		apk_content_gallery.setAdapter(new ImageAdapter(this));
		apk_content_gallery
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
	}

	/***** Gallery适配器 *******/
	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			return mps.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView image = new ImageView(mContext);
			image.setImageDrawable(mps.get(position));
			image.setAdjustViewBounds(true);
			image.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT));
			image.setScaleType(ImageView.ScaleType.FIT_CENTER);
			return image;
			
		}
	}

	/****** 初始化动画 ******/
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 4 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/***** 页卡内容初始化 ******/
	private void InitViewPager() {
		paper_content = (ViewPager) findViewById(R.id.viewpager_content);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();

		listViews.add(mInflater.inflate(R.layout.apk_particular_1, null));
		listViews.add(mInflater.inflate(R.layout.apk_particular_2, null));
		listViews.add(mInflater.inflate(R.layout.apk_particular_3, null));
		listViews.add(mInflater.inflate(R.layout.apk_particular_4, null));

		paper_content.setAdapter(new MyPagerAdapter(listViews));
		paper_content.setCurrentItem(0);
		paper_content.setOnPageChangeListener(new MyOnPageChangeListener());
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
			if (arg1 < 4) {
				((ViewPager) arg0).addView(mListViews.get(arg1 % 4), 0);
			}
			/*
			 * if (arg1 == 0) { text1 = (TextView)
			 * arg0.findViewById(R.id.text_content); text1.setText("测试1"); } if
			 * (arg1 == 1) { text1 = (TextView)
			 * arg0.findViewById(R.id.text_content); text1.setText("测试2"); } if
			 * (arg1 == 2) { text1 = (TextView)
			 * arg0.findViewById(R.id.text_content); text1.setText("测试3"); } if
			 * (arg1 == 3) { text1 = (TextView)
			 * arg0.findViewById(R.id.text_content); text1.setText("测试4"); }
			 */

			return mListViews.get(arg1 % 4);
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
		int three = one * 3;

		public void onPageSelected(int arg0) {

			Animation animation = null;

			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				}

				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}

				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}

				break;

			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				}

				break;
			}

			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/****** 创建Menu菜单 ******/
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ONE, 0, "分享此应用");// 设置文字与图标
		menu.add(0, TWO, 0, "举报");
		menu.add(0, THREE, 0, "顶一下");
		menu.add(0, FOUR, 0, "踩一下");
		return super.onCreateOptionsMenu(menu);
	}

	/***** Menu监听 ******/
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Toast.makeText(this, "已分享", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Intent intent = new Intent(ApkContent.this, Report.class);
			startActivity(intent);
			break;
		case 3:
			Toast.makeText(this, "+1", Toast.LENGTH_SHORT).show();
			break;
		case 4:
			Toast.makeText(this, "-1", Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);

	}

	public void loadingIcon(final String url) {
		Log.v("url", url);
		loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置圆形
		loginDialog.setMessage("正在读取");
		loginDialog.setCancelable(true); 
		loginDialog.show();
		new Thread() {
			@Override
			public void run() {
				try {
					JsoupApkContent jsoup = new JsoupApkContent();
					jsoup.getApkContentInfo(url);
					Message msg = new Message();
					msg.what = 1;
					handlers.sendMessage(msg);
				} catch (IOException e) {
					Message msg = new Message();
					msg.what = 100;
					handlers.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void loadingGallery(final String url) {
		new Thread() {
			@Override
			public void run() {
				try {
					JsoupApkContent jsoup = new JsoupApkContent();
					jsoup.getApkContentGallery(apkUrl);		
					Message msg = new Message();
					msg.what = 2;
					handlers.sendMessage(msg);
				} catch (IOException e) {
					Message msg = new Message();
					msg.what = 101;
					handlers.sendMessage(msg);
					
					e.printStackTrace();
				}
			}
		}.start();
	}

	/******* 更改UI界面 ******/
	@SuppressLint("HandlerLeak")
	class MessageHandler extends Handler {
		public MessageHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				apkicon.setImageBitmap(ApkContentInfo.getApkIcon());
				break;
			case 2:
				mps = ApkContentInfo.getApkGallery();
				
				loginDialog.cancel();
				Gallery();
				break;
				
			case 100:
			case 101:
				if(couldSend) {
				ShowExceptionDialog.showNetDialog(ApkContent.this, "网络连接异常，请检查网络连接或稍后再试！");
				}
				break;
			}
		}
	}

	@Override
	protected void onPause() {
		couldSend = false;
		ApkContentInfo.releaseGallery();
		super.onPause();
	}
}
