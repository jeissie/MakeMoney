package com.news;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.apk.ApkAll;
import com.exceptiondialog.ShowExceptionDialog;
import com.getimageandtext.NewsPreviewInfo;
import com.jesse.mxapk.R;
import com.jsoup.JsoupNewsPreview;
import com.newslistview.NewsListImageAndTextListAdapter;
import com.newslistview.NewsListImageAndText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
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

/**
 * @author jie
 * 
 */
public class NewsPreview extends Activity {

	private ListView industry_news, apk_news, head_news, skill, rom, website_news;
	private ViewPager paper_content;
	private List<View> listViews; // 页面列表
	private Button mainButton;
	private ImageView cursor;// 动画图片
	private TextView t1, t2, t3, t4, t5, t6;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private Handler handler;
	private List<NewsListImageAndText> dataArray1 = new ArrayList<NewsListImageAndText>();
	private List<NewsListImageAndText> dataArray2 = new ArrayList<NewsListImageAndText>();
	private List<NewsListImageAndText> dataArray3 = new ArrayList<NewsListImageAndText>();
	private List<NewsListImageAndText> dataArray4 = new ArrayList<NewsListImageAndText>();
	private List<NewsListImageAndText> dataArray5 = new ArrayList<NewsListImageAndText>();
	private List<NewsListImageAndText> dataArray6 = new ArrayList<NewsListImageAndText>();
	private boolean flag1 = true;
	private boolean flag2 = true;
	private boolean flag3 = true;
	private boolean flag4 = true;
	private boolean flag5 = true;
	private boolean flag6 = true;
	
	private boolean dialogFlag1 = true;
	private boolean dialogFlag2 = true;
	private boolean dialogFlag3 = true;
	private boolean dialogFlag4 = true;
	private boolean dialogFlag5 = true;
	private boolean dialogFlag6 = true;
	
	private NewsListImageAndTextListAdapter adapter1;
	private NewsListImageAndTextListAdapter adapter2;
	private NewsListImageAndTextListAdapter adapter3;
	private NewsListImageAndTextListAdapter adapter4;
	private NewsListImageAndTextListAdapter adapter5;
	private NewsListImageAndTextListAdapter adapter6;
	private ProgressDialog loginDialog;
  
	private boolean couldSend = true;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_preview);
		couldSend = true;
		InitImageView(); // 初始化动画
		InitTextView(); // 初始化头标
		InitViewPager(); // 初始化选项卡

		mainButton = (Button) findViewById(R.id.main_button);
		mainButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});
		Looper looper = Looper.myLooper();
		handler = new MessageHandler(looper);
	}

	/******* 头标初始化 *******/
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);
		t4 = (TextView) findViewById(R.id.text4);
		t5 = (TextView) findViewById(R.id.text5);
		t6 = (TextView) findViewById(R.id.text6);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
		t4.setOnClickListener(new MyOnClickListener(3));
		t5.setOnClickListener(new MyOnClickListener(4));
		t6.setOnClickListener(new MyOnClickListener(5));
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

		for (int i = 0; i < 6; i++) {
			listViews.add(mInflater.inflate(R.layout.listview, null));
		}
		paper_content.setAdapter(new MyPagerAdapter(listViews));
		paper_content.setCurrentItem(0); // 初始页面编号
		loginDialog.show();
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
		offset = (screenW / 6 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
		
		loginDialog = new ProgressDialog(NewsPreview.this);// 设置进度条
		loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置圆形
		loginDialog.setCancelable(true); 
		loginDialog.setMessage("正在读取");
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

			if (arg1 < 6) {
				((ViewPager) arg0).addView(mListViews.get(arg1 % 6), 0);
			}
			// 测试页卡1内的按钮事件
			if (arg1 == 0 && flag1) {
				industry_news = (ListView) findViewById(R.id.listView);
				loading(1, "http://www.mxapk.com/news/list/11");

				industry_news.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startNewsContent(adapter1, arg2);
					}

				});
				flag1 = false;
			}

			if (arg1 == 1 && flag2) {
				apk_news = (ListView) findViewById(R.id.listView);
				loading(2, "http://www.mxapk.com/news/list/12");

				apk_news.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startNewsContent(adapter2, arg2);
					}

				});
				flag2 = false;
			}
			if (arg1 == 2 && flag3) {
				head_news = (ListView) findViewById(R.id.listView);
				loading(3, "http://www.mxapk.com/news/list/13");

				head_news.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startNewsContent(adapter3, arg2);
					}

				});
				flag3 = false;
			}
			if (arg1 == 3 && flag4) {
				skill = (ListView) findViewById(R.id.listView);

				loading(4, "http://www.mxapk.com/news/list/14");
				skill.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startNewsContent(adapter4, arg2);
					}

				});
				flag4 = false;
			}
			if (arg1 == 4 && flag5) {
				rom = (ListView) findViewById(R.id.listView);
				loading(5, "http://www.mxapk.com/news/list/42");
				rom.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						startNewsContent(adapter5, arg2);

					}

				});
				flag5 = false;
			}
			if (arg1 == 5 && flag6) {
				website_news = (ListView) findViewById(R.id.listView);

				loading(6, "http://www.mxapk.com/news/list/15");
				website_news.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startNewsContent(adapter6, arg2);
					}

				});
				flag6 = false;
			}
			return mListViews.get(arg1 % 6);
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
		int four = one * 4;
		int five = one * 5;
		int six = one * 6;

		// 当前选中页面顶部滑动条动画
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
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, 0, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, 0, 0, 0);
				} 
				if(dialogFlag1)
					loginDialog.show();
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, three, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, four, 0, 0);
				} 
				if(dialogFlag2)
					loginDialog.show();
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, two, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, two, 0, 0);
				}
				if(dialogFlag3)
					loginDialog.show();
				break;

			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, three, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, three, 0, 0);
				} 
				if(dialogFlag4)
					loginDialog.show();
				break;

			case 4:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, four, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, four, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, four, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, four, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, four, 0, 0);
				} 
				if(dialogFlag5)
					loginDialog.show();
				break;

			case 5:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, five, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, five, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, five, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, five, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, five, 0, 0);
				} 
				if(dialogFlag6)
					loginDialog.show();
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

	public void startNewsContent(NewsListImageAndTextListAdapter adapter,
			int position) {
		NewsListImageAndText imageAndText = adapter.getItem(position);
		Intent intent = new Intent(NewsPreview.this, NewsContent.class);
		intent.putExtra("url", imageAndText.getNewUrl());
		intent.putExtra("title", imageAndText.getNews_title());
		intent.putExtra("time", imageAndText.getTime());
		intent.putExtra("comment",imageAndText.getComment());
		intent.putExtra("glance", imageAndText.getGlance());
		startActivity(intent);
	}

	public void loading(final int posetion, final String url) {
		Log.v("posetion", posetion + "");
		Log.v("url", url);
		new Thread() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				String icon;
				String newsName;
				String newsTime;
				String glance;
				String comment;
				String newsUrl;
				try {
					JsoupNewsPreview jsoup = new JsoupNewsPreview();
					jsoup.getInfoOnListView(url);
					int i = 0;
					while (i != NewsPreviewInfo.getLength()) {
						icon = NewsPreviewInfo.getNewsimg(i);
						newsName = NewsPreviewInfo.getNewsName(i);
						newsTime = NewsPreviewInfo.getTime(i);
						glance = NewsPreviewInfo.getGlance(i);
						comment = NewsPreviewInfo.getComment(i);
						newsUrl = NewsPreviewInfo.getNewsUrl(i);
						NewsListImageAndText test = new NewsListImageAndText(
								icon, newsName, newsTime, glance, comment,
								newsUrl);
						switch (posetion) {
						case 1:
							
							dataArray1.add(test);
							break;
						case 2:
							dataArray2.add(test);
							break;
						case 3:
							dataArray3.add(test);
							break;
						case 4:
							dataArray4.add(test);
							break;
						case 5:
							dataArray5.add(test);
							break;
						case 6:
							dataArray6.add(test);
							break;
						}
						i++;
					}
					Message msg = new Message();
					msg.what = posetion;
					handler.sendMessage(msg);
				} catch (IOException e) {
					Message msg = new Message();
					msg.what = 100;
					handler.sendMessage(msg);
					
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
				loginDialog.cancel();
				dialogFlag1 = false;
				adapter1 = new NewsListImageAndTextListAdapter(
						NewsPreview.this, dataArray1, industry_news);
				industry_news.setAdapter(adapter1);
				break;

			case 2:
				loginDialog.cancel();
				dialogFlag2 = false;
				adapter2 = new NewsListImageAndTextListAdapter(
						NewsPreview.this, dataArray2, apk_news);
				apk_news.setAdapter(adapter2);
				break;

			case 3:
				loginDialog.cancel();
				dialogFlag3 = false;
				adapter3 = new NewsListImageAndTextListAdapter(
						NewsPreview.this, dataArray3, head_news);
				head_news.setAdapter(adapter3);
				break;

			case 4:
				loginDialog.cancel();
				dialogFlag4 = false;
				adapter4 = new NewsListImageAndTextListAdapter(
						NewsPreview.this, dataArray4, skill);
				skill.setAdapter(adapter4);
				break;

			case 5:
				loginDialog.cancel();
				dialogFlag5 = false;
				adapter5 = new NewsListImageAndTextListAdapter(
						NewsPreview.this, dataArray5, rom);
				rom.setAdapter(adapter5);
				break;

			case 6:
				loginDialog.cancel();
				dialogFlag6 = false;
				adapter6 = new NewsListImageAndTextListAdapter(
						NewsPreview.this, dataArray6, website_news);
				website_news.setAdapter(adapter6);
				break;
				
			case 100:
				if(couldSend) {
				ShowExceptionDialog.showNetDialog(NewsPreview.this, "网络连接异常，请检查网络连接或稍后再试！");
				}
				break;
			}
		}
	}
	
	@Override
	protected void onPause() {
		couldSend = false;
		super.onPause();
	}
}
