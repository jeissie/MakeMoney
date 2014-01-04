package com.search;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.apk.ApkContent;
import com.apklistview.ApkListImageAndText;
import com.apklistview.ApkListImageAndTextListAdapter;
import com.exceptiondialog.ShowExceptionDialog;
import com.getimageandtext.ApkListViewInfo;
import com.jesse.mxapk.MxApkActivity;
import com.jesse.mxapk.R;
import com.jsoup.JsoupApkListView;
import com.jsoup.JsoupSearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author jeissie
 * 搜索显示页面
 */
public class SearchResult extends Activity {

	private ListView list;
	private String searchString = null;
	private ProgressDialog loginDialog;
	private String next;
	private List<ApkListImageAndText> dataArray = new ArrayList<ApkListImageAndText>();
	private ApkListImageAndTextListAdapter adapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			searchString = extras.getString("name");
		}
		loginDialog = new ProgressDialog(SearchResult.this);// 设置进度条
		loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置圆形
		loginDialog.setMessage("正在读取");
		loginDialog.show();
		loading();
		
		list = (ListView) findViewById(R.id.searchResultList);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				startApkContent(adapter, arg2);

			}

		});
		
	}
	
	private void loading() {
		
		final Handler handlers = new Handler() {
			public void handleMessage(Message messages) {
				switch (messages.what) {
				case 1:
					loginDialog.cancel();
					if ( next != null ) {
						addPageMore(list);
					}
					adapter = new ApkListImageAndTextListAdapter(SearchResult.this,dataArray, list);
					list.setAdapter(adapter);
					break;
					
				case 100:
					loginDialog.cancel();
					ShowExceptionDialog.showNetDialog(SearchResult.this,
							"网络连接异常，请检查网络连接或稍后再试！");
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
 					searchString = URLEncoder.encode(searchString, "UTF-8");
 					Log.v("searchurlonresult", "http://www.mxapk.com/search/result?keywords="+searchString);
					jsoup.getInfoOnListView("http://www.mxapk.com/search/result?keywords="+searchString);
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
						dataArray.add(test); 
						i++;
					}
					next = ApkListViewInfo.getJumpUrl();
					Message msg = new Message();
					msg.what = 1;
					handlers.sendMessage(msg);
 				}catch (IOException e) {
					Message msg = new Message();
					msg.what = 100;
					handlers.sendMessage(msg);
					e.printStackTrace();
				}
 			}
		 }.start();
	}
	/******** 加载更多 ******/
	private void addPageMore(ListView list) {
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
	                
	                final Handler handler = new Handler() {
	        			public void handleMessage(Message messages) {
	        				switch (messages.what) {
	        				case 1:
	        					adapter.count += ApkListViewInfo.getLength();
	        					adapter.notifyDataSetChanged();
	        					if(next == null) {
	        						moreTextView.setVisibility(View.GONE);//去除显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE);//去除进度显示"
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//再次显示"加载更多"
	        		                loadProgressBar.setVisibility(View.GONE); //再次隐藏“进度条”
	        					}
	        					break;
	        				case 100:
	        					loginDialog.cancel();
	        					ShowExceptionDialog.showNetDialog(SearchResult.this,
	        							"网络连接异常，请检查网络连接或稍后再试！");
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
	        					jsoup.getInfoOnListView(next);
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
	        							dataArray.add(test);
	        						i++;
	        					}
	        					next = ApkListViewInfo.getJumpUrl();
	        					Message msg = new Message();
	        					msg.what = 1;
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
		});
		
		list.addFooterView(view);
	}

	
	/******* 跳转至详细页面 ********/
	public void startApkContent(ApkListImageAndTextListAdapter adapter,
			int posetion) {
		ApkListImageAndText imageAndText = adapter.getItem(posetion);
		Intent intent = new Intent(SearchResult.this, ApkContent.class);
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
}
