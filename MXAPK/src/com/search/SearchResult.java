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
 * ������ʾҳ��
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
		loginDialog = new ProgressDialog(SearchResult.this);// ���ý�����
		loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // ����Բ��
		loginDialog.setMessage("���ڶ�ȡ");
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
							"���������쳣�������������ӻ��Ժ����ԣ�");
					break;
				
				}
			}
		};
		
		 new Thread() {	
 			@Override
 			public void run() {
 				String icon;   //ͼ��
				String apkName; //����
				String apkVersion; //�汾��Ϣ
				String apkTime;  //����ʱ��
				String apkSize;  //�����С
				String apkDowmloadTimes; //���ش���
				String apkUrl;  //���ص�ַ
				String apkJumpUrl;  //��ϸҳ���ַ
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
	/******** ���ظ��� ******/
	private void addPageMore(ListView list) {
		final TextView moreTextView;// �鿴����
		final LinearLayout loadProgressBar;// ���ڼ��ؽ�����
		View view = LayoutInflater.from(this).inflate(R.layout.list_page_load,null);
		moreTextView = (TextView) view.findViewById(R.id.more_id);
		loadProgressBar = (LinearLayout) view.findViewById(R.id.load_id);
		moreTextView.setOnClickListener(new Button.OnClickListener() {
			 @Override
	            public void onClick(View v) {    
	                moreTextView.setVisibility(View.GONE);//����"���ظ���"
	                loadProgressBar.setVisibility(View.VISIBLE);//��ʾ������
	                
	                final Handler handler = new Handler() {
	        			public void handleMessage(Message messages) {
	        				switch (messages.what) {
	        				case 1:
	        					adapter.count += ApkListViewInfo.getLength();
	        					adapter.notifyDataSetChanged();
	        					if(next == null) {
	        						moreTextView.setVisibility(View.GONE);//ȥ����ʾ"���ظ���"
	        		                loadProgressBar.setVisibility(View.GONE);//ȥ��������ʾ"
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//�ٴ���ʾ"���ظ���"
	        		                loadProgressBar.setVisibility(View.GONE); //�ٴ����ء���������
	        					}
	        					break;
	        				case 100:
	        					loginDialog.cancel();
	        					ShowExceptionDialog.showNetDialog(SearchResult.this,
	        							"���������쳣�������������ӻ��Ժ����ԣ�");
	        					break;
	        				}

	        			}
	        		};
	        				
	        		new Thread() {   	
	        			@Override
	        			public void run() {
	        				String icon;   //ͼ��
	        				String apkName; //����
	        				String apkVersion; //�汾��Ϣ
	        				String apkTime;  //����ʱ��
	        				String apkSize;  //�����С
	        				String apkDowmloadTimes; //���ش���
	        				String apkUrl;  //���ص�ַ
	        				String apkJumpUrl;  //��ϸҳ���ַ
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

	
	/******* ��ת����ϸҳ�� ********/
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
