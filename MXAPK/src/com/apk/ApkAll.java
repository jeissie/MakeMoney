package com.apk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.apklistview.ApkListImageAndTextListAdapter;
import com.apklistview.ApkListImageAndText;
import com.exceptiondialog.ShowExceptionDialog;
import com.getimageandtext.ApkListViewInfo;
import com.jesse.mxapk.MxApkActivity;
import com.jesse.mxapk.R;
import com.jsoup.JsoupApkListView;
import com.jsoup.JsoupSearch;
import com.search.SearchResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * @author jie
 *
 */
@SuppressLint("CutPasteId")
public class ApkAll extends Activity {

	private ViewPager paper_content;
	private List<View> listViews; // 页面列表
	private TextView apk_all_title;
	public static SlidingDrawer slidingDrawer_down; // 搜索抽屉
	public static AutoCompleteTextView search_textview; // 搜索框
	private Button search_button,apk_preview_button; // 搜索按钮
	private ListView list1, list2, list3, list4, list5, list6, list7, list8,
			list9, list10, list11;
	private ImageView left,right;
	private int item ;
	private Handler handler;
	private List<ApkListImageAndText> dataArray1 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray2 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray3 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray4 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray5 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray6 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray7 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray8 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray9 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray10 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray11 = new ArrayList<ApkListImageAndText>();
	private ApkListImageAndTextListAdapter adapter1;
	private ApkListImageAndTextListAdapter adapter2;
	private ApkListImageAndTextListAdapter adapter3;
	private ApkListImageAndTextListAdapter adapter4;
	private ApkListImageAndTextListAdapter adapter5;
	private ApkListImageAndTextListAdapter adapter6;
	private ApkListImageAndTextListAdapter adapter7;
	private ApkListImageAndTextListAdapter adapter8;
	private ApkListImageAndTextListAdapter adapter9;
	private ApkListImageAndTextListAdapter adapter10;
	private ApkListImageAndTextListAdapter adapter11;
	
	private boolean flag1 = true;
	private boolean flag2 = true;
	private boolean flag3 = true;
	private boolean flag4 = true;
	private boolean flag5 = true;
	private boolean flag6 = true;
	private boolean flag7 = true;
	private boolean flag8 = true;
	private boolean flag9 = true;
	private boolean flag10 = true;
	private boolean flag11 = true;
	
	private boolean dialogFlag1 = true;
	private boolean dialogFlag2 = true;
	private boolean dialogFlag3 = true;
	private boolean dialogFlag4 = true;
	private boolean dialogFlag5 = true;
	private boolean dialogFlag6 = true;
	private boolean dialogFlag7 = true;
	private boolean dialogFlag8 = true;
	private boolean dialogFlag9 = true;
	private boolean dialogFlag10 = true;
	private boolean dialogFlag11 = true;

	private ProgressDialog loginDialog;
	private String jumpUrl[] = new String[12]; //下一页地址
	private boolean couldSend = true;
	String seatchString = null;
	boolean resulit;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apk_all);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			item = extras.getInt("item");
		}
		couldSend = true;
		InitViewPager(); // 初始化选项卡
		InitSearch(); // 搜索初始化
		InitTextView(); // 初始化头标
		apk_preview_button = (Button) findViewById(R.id.apk_preview_button);
		apk_preview_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		Looper looper = Looper.myLooper();
		handler = new MessageHandler(looper);
	}

	/****** 搜索初始化 ******/
	private void InitSearch() {
		slidingDrawer_down = (SlidingDrawer) findViewById(R.id.slidingDrawer);
		search_textview = (AutoCompleteTextView) findViewById(R.id.search_text);
		search_button = (Button) findViewById(R.id.search_button);
		search_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!search_textview.getText().toString().equals("")) {
					loginDialog = new ProgressDialog(ApkAll.this);// 设置进度条
					loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置圆形
					loginDialog.setMessage("正在读取");
					loginDialog.show();
					seatchString = search_textview.getText().toString();
					search();
				}
			}
		});
	}

	/***** 页卡内容初始化 ******/
	private void InitViewPager() {
		paper_content = (ViewPager) findViewById(R.id.viewpager_content);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();

		for (int i = 0; i < 11; i++) {
			listViews.add(mInflater.inflate(R.layout.listview, null));
		}
		paper_content.setAdapter(new MyPagerAdapter(listViews));
		paper_content.setCurrentItem(item);
		paper_content.setOnPageChangeListener(new MyOnPageChangeListener());
		
		loginDialog = new ProgressDialog(ApkAll.this);// 设置进度条
		loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置圆形
		loginDialog.setMessage("正在读取");
		loginDialog.show();
	}

	/******* 头标初始化 *******/
	private void InitTextView() {
		apk_all_title = (TextView) findViewById(R.id.apk_all_title);
		left = (ImageView) findViewById(R.id.left);
		right = (ImageView) findViewById(R.id.right);
		switch (item) {
		case 0:
			apk_all_title.setText("系统安全");
			left.setVisibility(View.GONE);
			break;
		case 1:
			apk_all_title.setText("新闻阅读");
			break;
		case 2:
			apk_all_title.setText("地图导航");
			break;
		case 3:
			apk_all_title.setText("快捷工具");
			break;
		case 4:
			apk_all_title.setText("办公理财");
			break;
		case 5:
			apk_all_title.setText("社交应用");
			break;
		case 6:
			apk_all_title.setText("生活购物");
			break;
		case 7:
			apk_all_title.setText("主题美化");
			break;
		case 8:
			apk_all_title.setText("图像处理");
			break;
		case 9:
			apk_all_title.setText("音乐视频");
			break;
		case 10:
			apk_all_title.setText("实用工具");
			right.setVisibility(View.GONE);
			break;

		}
	}
	/***** ViewPager适配器 ******/
	@SuppressLint("CutPasteId")
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
		@SuppressLint("CutPasteId")
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			
			if (arg1 < 11) {
				((ViewPager) arg0).addView(mListViews.get(arg1 % 11), 0);
			}

			// 测试页卡1内的按钮事件
			if (arg1 == 0 && flag1) {
				
				list1 = (ListView) findViewById(R.id.listView);
				loading(1,"http://www.mxapk.com/list/17");
				list1.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						startApkContent(adapter1, arg2);
					}

				});
			}
			if (arg1 == 1 && flag2) {

				list2 = (ListView) findViewById(R.id.listView);
				loading(2,"http://www.mxapk.com/list/33");

				list2.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter2, arg2);
					}

				});
			}
			if (arg1 == 2 && flag3) {			
				list3 = (ListView) findViewById(R.id.listView);
				loading(3,"http://www.mxapk.com/list/18");

				list3.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter3, arg2);
					}

				});
			}
			if (arg1 == 3 && flag4) {

				list4 = (ListView) findViewById(R.id.listView);
				loading(4,"http://www.mxapk.com/list/6");
				list4.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter4, arg2);
					}

				});
			}
			if (arg1 == 4 && flag5) {

				list5 = (ListView) findViewById(R.id.listView);
				loading(5,"http://www.mxapk.com/list/20");

				list5.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter5, arg2);
					}

				});
			}
			if (arg1 == 5 && flag6) {

				list6 = (ListView) findViewById(R.id.listView);
				loading(6,"http://www.mxapk.com/list/16");

				list6.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter6, arg2);
					}

				});
			}
			if (arg1 == 6 && flag7) {
				list7 = (ListView) findViewById(R.id.listView);
				loading(7,"http://www.mxapk.com/list/8");

				list7.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter7, arg2);
					}

				});
			}
			if (arg1 == 7 && flag8) {

				list8 = (ListView) findViewById(R.id.listView);
				loading(8,"http://www.mxapk.com/list/7");
				
				list8.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter8, arg2);
					}

				});
			}
			if (arg1 == 8 && flag9) {

				list9 = (ListView) findViewById(R.id.listView);
				loading(9,"http://www.mxapk.com/list/40");

				list9.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter9, arg2);
					}

				});
			}
			if (arg1 == 9 && flag10) {

				list10 = (ListView) findViewById(R.id.listView);
				loading(10,"http://www.mxapk.com/list/19");
				list10.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter10, arg2);
					}

				});
			}

			if (arg1 == 10 && flag11) {

				list11= (ListView) findViewById(R.id.listView);
				loading(11,"http://www.mxapk.com/list/41");
				list11.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter11, arg2);
					}

				});
			}
			return mListViews.get(arg1 % 11);
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

		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				apk_all_title.setText("系统安全");
				left.setVisibility(View.GONE);
				if(dialogFlag1)
					loginDialog.show();
				break;
			case 1:
				apk_all_title.setText("新闻阅读");
				left.setVisibility(View.VISIBLE);
				if(dialogFlag2)
					loginDialog.show();
				break;
			case 2:
				apk_all_title.setText("地图导航");
				if(dialogFlag3)
					loginDialog.show();
				break;
			case 3:
				apk_all_title.setText("快捷工具");
				if(dialogFlag4)
					loginDialog.show();
				break;
			case 4:
				apk_all_title.setText("办公理财");
				if(dialogFlag5)
					loginDialog.show();
				break;
			case 5:
				apk_all_title.setText("社交应用");
				if(dialogFlag6)
					loginDialog.show();
				break;
			case 6:
				apk_all_title.setText("生活购物");
				if(dialogFlag7)
					loginDialog.show();
				break;
			case 7:
				apk_all_title.setText("主题美化");
				if(dialogFlag8)
					loginDialog.show();
				break;
			case 8:
				apk_all_title.setText("图像处理");
				if(dialogFlag9)
					loginDialog.show();
				break;
			case 9:
				apk_all_title.setText("音乐视频");
				right.setVisibility(View.VISIBLE);
				if(dialogFlag10)
					loginDialog.show();
				break;
			case 10:
				apk_all_title.setText("实用工具");
				right.setVisibility(View.GONE);
				if(dialogFlag11)
					loginDialog.show();
				break;
			}

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			closeDrawer();
		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/******** 加载更多 ******/
	private void addPageMore(ListView list, final int position) {
		final TextView moreTextView;// 查看更多
		final LinearLayout loadProgressBar;// 正在加载进度条
		View view = LayoutInflater.from(this).inflate(R.layout.list_page_load,null);
		moreTextView = (TextView) view.findViewById(R.id.more_id);
		loadProgressBar = (LinearLayout) view.findViewById(R.id.load_id);
		moreTextView.setOnClickListener(new Button.OnClickListener() {
			 @Override
	            public void onClick(View v) {    
	                moreTextView.setVisibility(View.GONE);//隐藏"加载更多"
	                loadProgressBar.setVisibility(View.VISIBLE);//显示进度条
	                
	                final Handler handlers = new Handler() {
	        			public void handleMessage(Message messages) {
	        				switch (messages.what) {
	        				case 1:
	        					adapter1.count += ApkListViewInfo.getLength();
	        					adapter1.notifyDataSetChanged();
	        					if(jumpUrl[1] == null) {
	        						moreTextView.setVisibility(View.GONE);//去除显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);//去除进度显示"
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        				case 2:
	        					adapter2.count += ApkListViewInfo.getLength();
	        					adapter2.notifyDataSetChanged();
	        					if(jumpUrl[2]== null ) {
	        						 moreTextView.setVisibility(View.GONE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        				
	        				case 3:
	        					adapter3.count += ApkListViewInfo.getLength();
	        					adapter3.notifyDataSetChanged();
	        					if(jumpUrl[3] == null ) {
	        						 moreTextView.setVisibility(View.GONE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        					
	        				case 4:
	        					adapter4.count += ApkListViewInfo.getLength();
	        					adapter4.notifyDataSetChanged();
	        					if(jumpUrl[4] == null ) {
	        						 moreTextView.setVisibility(View.GONE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        					
	        				case 5:
	        					adapter5.count += ApkListViewInfo.getLength();
	        					adapter5.notifyDataSetChanged();
	        					if(jumpUrl[5] == null ) {
	        						 moreTextView.setVisibility(View.GONE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        					
	        				case 6:
	        					adapter6.count += ApkListViewInfo.getLength();
	        					adapter6.notifyDataSetChanged();
	        					if(jumpUrl[6] == null ) {
	        						 moreTextView.setVisibility(View.GONE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        					
	        				case 7:
	        					adapter7.count += ApkListViewInfo.getLength();
	        					adapter7.notifyDataSetChanged();
	        					if(jumpUrl[7] == null ) {
	        						 moreTextView.setVisibility(View.GONE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        					
	        				case 8:
	        					adapter8.count += ApkListViewInfo.getLength();
	        					adapter8.notifyDataSetChanged();
	        					if(jumpUrl[8] == null ) {
	        						 moreTextView.setVisibility(View.GONE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        					
	        				case 9:
	        					adapter9.count += ApkListViewInfo.getLength();
	        					adapter9.notifyDataSetChanged();
	        					if(jumpUrl[9] == null ) {
	        						 moreTextView.setVisibility(View.GONE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        					
	        				case 10:
	        					adapter10.count += ApkListViewInfo.getLength();
	        					adapter10.notifyDataSetChanged();
	        					if(jumpUrl[10] == null ) {
	        						 moreTextView.setVisibility(View.GONE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        					
	        				case 11:
	        					adapter11.count += ApkListViewInfo.getLength();
	        					adapter11.notifyDataSetChanged();
	        					if(jumpUrl[11] == null ) {
	        						 moreTextView.setVisibility(View.GONE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        					
	        					

	        				case 100:
	        					if(couldSend) {
	        					ShowExceptionDialog.showNetDialog(ApkAll.this,
	        							"网络连接异常，请检查网络连接或稍后再试！");
	        					}
	        					break;
	        				}

	        			}
	        		};
	        		
	                new Thread() {
	                	
	        			@Override
	        			public void run() {
	        				
	        				String icon;   //图标
	        				String apkName; //名称
	        				String apkVersion; //版本信息
	        				String apkTime;  //发布时间
	        				String apkSize;  //软件大小
	        				String apkDowmloadTimes; //下载次数
	        				String apkUrl;  //下载地址
	        				String apkJumpUrl;  //详细页面地址
	        				int apkStat;
	        				try {
	        					JsoupApkListView jsoup = new JsoupApkListView();
	        					jsoup.getInfoOnListView(jumpUrl[position]);
	        					int i = 0;
	        					while (i != ApkListViewInfo.getLength()) {
	        						icon = ApkListViewInfo.getApkIcon(i);
	        						apkName = ApkListViewInfo.getApkZhName(i);
	        						apkDowmloadTimes = ApkListViewInfo.getApkDownloadTimes(i);
	        						apkVersion = ApkListViewInfo.getApkVersion(i);
	        						apkTime = ApkListViewInfo.getApkTime(i);
	        						apkSize = ApkListViewInfo.getApkSize(i);
	        						apkUrl = ApkListViewInfo.getApkInstallUrl(i);
	        						apkJumpUrl = ApkListViewInfo.getApkUrl(i);
	        						apkStat = 3;
	        						ApkListImageAndText test = new ApkListImageAndText(
	        								icon, apkName, apkVersion, apkTime, apkSize,
	        								apkDowmloadTimes, apkStat, apkUrl, apkJumpUrl);
	        						
	        						switch (position) {
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
	    							case 7:
	    							
	    								dataArray7.add(test);
	    								break;
	    							case 8:
	    								
	    								dataArray8.add(test);
	    								break;
	    							case 9:
	    							
	    								dataArray9.add(test);
	    								break;
	    							case 10:
	    							
	    								dataArray10.add(test);
	    								break;
	    							case 11:
	    						
	    								dataArray11.add(test);
	    								break;
	        						}
	        						i++;
	        					}
	        					jumpUrl[position] = ApkListViewInfo.getJumpUrl();
	        					Log.v("jumpurllodingMore", ApkListViewInfo.getJumpUrl()+"");
	        					Message msg = new Message();
	        					msg.what = position;
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
		});
		
		list.addFooterView(view);
	
	
	}
	
	public void loading(final int position, final String url) {	
		
			new Thread() {
				@Override
				public void run() {
					
					String icon;
					String apkName;
					String apkVersion;
					String apkTime;
					String apkSize;
					String apkDowmloadTimes;
					String apkUrl;
					String apkJumpUrl;
					int apkStat;
					try {
						JsoupApkListView jsoup = new JsoupApkListView();
						jsoup.getInfoOnListView(url);
						int i = 0;
						while(i != ApkListViewInfo.getLength() ){
							icon = ApkListViewInfo.getApkIcon(i);
							apkName = ApkListViewInfo.getApkZhName(i);
							apkDowmloadTimes = ApkListViewInfo.getApkDownloadTimes(i);
							apkVersion = ApkListViewInfo.getApkVersion(i);
							apkTime = ApkListViewInfo.getApkTime(i);
							apkSize = ApkListViewInfo.getApkSize(i);
							apkUrl = ApkListViewInfo.getApkInstallUrl(i);
							apkJumpUrl = ApkListViewInfo.getApkUrl(i);
							apkStat = 3;
							ApkListImageAndText test = new ApkListImageAndText(
									icon, apkName, apkVersion, apkTime, apkSize,apkDowmloadTimes, apkStat, apkUrl, apkJumpUrl);
							switch (position) {
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
							case 7:
							
								dataArray7.add(test);
								break;
							case 8:
								
								dataArray8.add(test);
								break;
							case 9:
							
								dataArray9.add(test);
								break;
							case 10:
							
								dataArray10.add(test);
								break;
							case 11:
						
								dataArray11.add(test);
								break;
							}
							
							i++;
						}
						jumpUrl[position] = ApkListViewInfo.getJumpUrl();
						Message msg = new Message();
						msg.what = position;
						handler.sendMessage(msg);
					} catch (IOException e) {
						Message msg = new Message();
						msg.what = 100;
						handler.sendMessage(msg);
						
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
				loginDialog.cancel();
				dialogFlag1 = false;
				if ( jumpUrl[1] != null ) {
					addPageMore(list1, 1);
				}
				adapter1 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray1, list1);
				list1.setAdapter(adapter1);
				break;
			
			case 2:
				loginDialog.cancel();
				dialogFlag2 = false;
				if ( jumpUrl[2] != null ) {
					addPageMore(list2, 2);
				}
				adapter2 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray2, list2);
				list2.setAdapter(adapter2);
				break;
				
			case 3:
				loginDialog.cancel();
				dialogFlag3 = false;
				if ( jumpUrl[3] != null ) {
					addPageMore(list3, 3);
				}
				adapter3 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray3, list3);
				list3.setAdapter(adapter3);
				break;
			case 4:
				loginDialog.cancel();
				dialogFlag4 = false;
				if ( jumpUrl[4] != null ) {
					addPageMore(list4, 4);
				}
				adapter4 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray4, list4);
				list4.setAdapter(adapter4);
				break;
				
			case 5:
				loginDialog.cancel();
				dialogFlag5 = false;
				if ( jumpUrl[5] != null ) {
					addPageMore(list5, 5);
				}
				adapter5 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray5, list5);
				list5.setAdapter(adapter5);
				break;
				
			case 6:
				loginDialog.cancel();
				dialogFlag6 = false;
				if ( jumpUrl[6] != null ) {
					addPageMore(list6, 6);
				}
				adapter6 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray6, list6);
				list6.setAdapter(adapter6);
				break;
				
			case 7:
				loginDialog.cancel();
				dialogFlag7 = false;
				if ( jumpUrl[7] != null ) {
					addPageMore(list7, 7);
				}
				adapter7 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray7, list7);
				list7.setAdapter(adapter7);
				break;
				
			case 8:
				loginDialog.cancel();
				dialogFlag8 = false;
				if ( jumpUrl[8] != null ) {
					addPageMore(list8, 8);
				}
				 adapter8 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray8, list8);
				list8.setAdapter(adapter8);
				break;				
				
			case 9:
				loginDialog.cancel();
				dialogFlag9 = false;
				if ( jumpUrl[9] != null ) {
					addPageMore(list9, 9);
				}
				adapter9 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray9, list9);
				list9.setAdapter(adapter9);
				break;
				
			case 10:
				loginDialog.cancel();
				dialogFlag10 = false;
				if ( jumpUrl[10] != null ) {
					addPageMore(list10, 10);
				}
				adapter10 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray10, list10);
				list10.setAdapter(adapter10);
				break;
				
			case 11:
				loginDialog.cancel();
				dialogFlag11 = false;
				if ( jumpUrl[11] != null ) {
					addPageMore(list11, 11);
				}
				adapter11 = new ApkListImageAndTextListAdapter(
						ApkAll.this, dataArray11, list11);
				list11.setAdapter(adapter11);
				break;
				
			case 100:
				if(couldSend) {
				ShowExceptionDialog.showNetDialog(ApkAll.this, "网络连接异常，请检查网络连接或稍后再试！");
				}
				break;
			}
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
						Intent intent = new Intent(ApkAll.this, SearchResult.class);
						intent.putExtra("name", seatchString);
						Log.v("seatchString",seatchString);
						startActivity(intent);
					} else {
						ShowExceptionDialog.showNetDialog(ApkAll.this, "抱歉，无搜索结果！");
					}
					break;
				case 2:
					loginDialog.cancel();
					ShowExceptionDialog.showNetDialog(ApkAll.this, "网络异常！");
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
		/***** 关闭抽屉 ******/
		if (slidingDrawer_down.isOpened()) {
			slidingDrawer_down.animateClose();
		}
		/******* 隐藏键盘 *****/
		InputMethodManager keyboard = (InputMethodManager) getSystemService(ApkAll.INPUT_METHOD_SERVICE);
		keyboard.hideSoftInputFromWindow(search_textview.getWindowToken(), 0);
	}
	
	/******* 跳转至详细页面 ********/
	public void startApkContent(ApkListImageAndTextListAdapter adapter,
			int posetion) {
		ApkListImageAndText imageAndText = adapter.getItem(posetion);
		Intent intent = new Intent(ApkAll.this, ApkContent.class);
		intent.putExtra("apkUrl", imageAndText.getJumpUrl());
		intent.putExtra("apkName", imageAndText.getApk_zh_name());
		intent.putExtra("version", imageAndText.getApk_en_name());
		intent.putExtra("apkSize", imageAndText.getApk_size());
		intent.putExtra("apkDownloadTimes", imageAndText.getDownloads());
		intent.putExtra("apkTime", imageAndText.getTime());
		intent.putExtra("apkDownloadUrl", imageAndText.getUrl());
		intent.putExtra("star", imageAndText.getStar());
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && slidingDrawer_down.isOpened()) {
			closeDrawer();
			
		} else if(keyCode == KeyEvent.KEYCODE_BACK && !slidingDrawer_down.isOpened() ) {
			onBackPressed();
		}
		return false;
	}
	
	@Override
	protected void onPause() {
		couldSend = false;
		super.onPause();
	}
}
