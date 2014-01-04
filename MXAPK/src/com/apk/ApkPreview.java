package com.apk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.apklistview.ApkListImageAndTextListAdapter;
import com.apklistview.ApkListImageAndText;
import com.apklistview.AsyncImageLoader.ImageCallback;
import com.apkmanager.ApkManager;
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
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.StaticLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
public class ApkPreview extends Activity {

	private ListView list1, list2, list3;
	private ViewPager paper_content;
	
	private List<View> listViews; // ҳ���б�
	private ImageView cursor, imagecomanger;// ����ͼƬ
	private TextView t1, t2, t3, t4;// ҳ��ͷ��
	private int offset = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���
	public static SlidingDrawer slidingDrawer_down; // ��������
	public static AutoCompleteTextView search_textview; // ������
	private Button search_button, main_button; // ������ť
	private TextView apk_item1, apk_item2, apk_item3, apk_item4, apk_item5,
			apk_item6, apk_item7, apk_item8, apk_item9, apk_item10, apk_item11;
	private Handler handler;
	private List<ApkListImageAndText> dataArray1 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray2 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray3 = new ArrayList<ApkListImageAndText>();
	private boolean flag1 = true;
	private boolean flag2 = true;
	private boolean flag3 = true;
	private boolean dialogFlag1 = true;
	private boolean dialogFlag2 = true;
	private boolean dialogFlag3 = true;
	private ApkListImageAndTextListAdapter adapter1;
	private ApkListImageAndTextListAdapter adapter2;
	private ApkListImageAndTextListAdapter adapter3;
	private ProgressDialog loginDialog;
	private String jumpUrl[] = new String[4];
	private boolean couldSend = true;
	String seatchString = null;
	boolean resulit;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apk_preview);
		if (savedInstanceState == null) {
			couldSend = true;
			InitImageView(); // ��ʼ������
			InitTextView(); // ��ʼ��ͷ��
			InitViewPager(); // ��ʼ��ѡ�
			InitSearch(); // ������ʼ��
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				int position = extras.getInt("position");
				paper_content.setCurrentItem(position);
			}
			Looper looper = Looper.myLooper();
			handler = new MessageHandler(looper);
			// �������ؼ�
			main_button = (Button) findViewById(R.id.main_button);
			main_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}

	}

	/****** ������ʼ�� ******/
	private void InitSearch() {
		slidingDrawer_down = (SlidingDrawer) findViewById(R.id.slidingDrawer);
		search_textview = (AutoCompleteTextView) findViewById(R.id.search_text);
		search_button = (Button) findViewById(R.id.search_button);

		search_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!search_textview.getText().toString().equals("")) {
					loginDialog = new ProgressDialog(ApkPreview.this);// ���ý�����
					loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // ����Բ��
					loginDialog.setMessage("���ڶ�ȡ");
					loginDialog.show();
					seatchString = search_textview.getText().toString();
					search();
				}
			}
		});
	}

	/******* ͷ���ʼ�� *******/
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);
		t4 = (TextView) findViewById(R.id.text4);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
		t4.setOnClickListener(new MyOnClickListener(3));
	}

	/******* ͷ����� *******/
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			paper_content.setCurrentItem(index);
		}
	};

	/***** ҳ�����ݳ�ʼ�� ******/
	private void InitViewPager() {
		paper_content = (ViewPager) findViewById(R.id.viewpager_content);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();

		listViews.add(mInflater.inflate(R.layout.apk_item, null));
		listViews.add(mInflater.inflate(R.layout.listview, null));
		listViews.add(mInflater.inflate(R.layout.listview, null));
		listViews.add(mInflater.inflate(R.layout.listview, null));

		paper_content.setAdapter(new MyPagerAdapter(listViews));
		paper_content.setCurrentItem(0); // ��ʼҳ����
		paper_content.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/****** ��ʼ������ ******/
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		offset = (screenW / 4 - bmpW) / 2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// ���ö�����ʼλ��

		loginDialog = new ProgressDialog(ApkPreview.this);// ���ý�����
		loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // ����Բ��
		loginDialog.setMessage("���ڶ�ȡ");
	}

	/***** ViewPager������ ******/
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

		// ��ȡ��ǰ���������
		@Override
		public int getCount() {
			return mListViews.size();
		}

		// ��ʼ��positionλ�õĽ���
		@SuppressLint("CutPasteId")
		@Override
		public Object instantiateItem(View arg0, int arg1) {

			if (arg1 < 4) {
				((ViewPager) arg0).addView(mListViews.get(arg1 % 4), 0);
			}
			// ����ҳ��1�ڵİ�ť�¼�
			if (arg1 == 0) {

				apk_item1 = (TextView) arg0.findViewById(R.id.apk_item1);
				apk_item1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(0);
					}
				});

				apk_item2 = (TextView) arg0.findViewById(R.id.apk_item2);
				apk_item2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(1);
					}
				});
				apk_item3 = (TextView) arg0.findViewById(R.id.apk_item3);
				apk_item3.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(2);
					}
				});
				apk_item4 = (TextView) arg0.findViewById(R.id.apk_item4);
				apk_item4.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(3);
					}
				});
				apk_item5 = (TextView) arg0.findViewById(R.id.apk_item5);
				apk_item5.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(4);
					}
				});
				apk_item6 = (TextView) arg0.findViewById(R.id.apk_item6);
				apk_item6.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(5);
					}
				});
				apk_item7 = (TextView) arg0.findViewById(R.id.apk_item7);
				apk_item7.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(6);
					}
				});
				apk_item8 = (TextView) arg0.findViewById(R.id.apk_item8);
				apk_item8.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(7);
					}
				});
				apk_item9 = (TextView) arg0.findViewById(R.id.apk_item9);
				apk_item9.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(8);
					}
				});
				apk_item10 = (TextView) arg0.findViewById(R.id.apk_item10);
				apk_item10.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(9);
					}
				});
				apk_item11 = (TextView) arg0.findViewById(R.id.apk_item11);
				apk_item11.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						closeDrawer();
						intentjump(10);
					}
				});

				imagecomanger = (ImageView) arg0
						.findViewById(R.id.imagecomanger);
				imagecomanger.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						closeDrawer();
						Intent intent = new Intent(ApkPreview.this,
								ApkManager.class);
						startActivity(intent);
					}
				});

			}
			if (arg1 == 1 && flag1) {
				list1 = (ListView) findViewById(R.id.listView);
				loading(1, "http://www.mxapk.com/list/38");
				list1.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter1, arg2);
					}

				});

				flag1 = false;
			}
			if (arg1 == 2 && flag2) {
				list2 = (ListView) findViewById(R.id.listView);
				loading(2, "http://www.mxapk.com/list/39");
				list2.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						startApkContent(adapter2, arg2);

					}

				});
				flag2 = false;
			}
			if (arg1 == 3 && flag3) {
				list3 = (ListView) findViewById(R.id.listView);
				loading(3, "http://www.mxapk.com/list/37");
				list3.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						startApkContent(adapter3, arg2);
					}

				});
				flag3 = false;
			}
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

	/******* apkItem������ *******/
	View.OnClickListener itemOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			closeDrawer();
			intentjump(v.getId());
		}

	};

	/******** ҳ���л����� ********/
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����
		int three = one * 3;

		// ��ǰѡ��ҳ�涥������������
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
				if (dialogFlag1)
					loginDialog.show();
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
				if (dialogFlag2)
					loginDialog.show();
				break;

			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				}
				if (dialogFlag3)
					loginDialog.show();
				break;
			}

			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			closeDrawer();
		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/******* ��תҳ�� *******/
	public void intentjump(int i) {
		Intent intent = new Intent(ApkPreview.this, ApkAll.class);
		intent.putExtra("item", i);
		startActivity(intent);
	}

	public interface ProgressBarCallback {
		public void gone();
	}
	
	/******** ���ظ��� ******/
	private void addPageMore(ListView list, final int position) {
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
	                
	                final Handler handlers = new Handler() {
	        			public void handleMessage(Message messages) {
	        				switch (messages.what) {
	        				case 1:
	        					adapter1.count += ApkListViewInfo.getLength();
	        					adapter1.notifyDataSetChanged();
	        					if(jumpUrl[1] == null) {
	        						moreTextView.setVisibility(View.GONE);//ȥ����ʾ"���ظ���"
	        		                loadProgressBar.setVisibility(View.GONE);//ȥ��������ʾ"
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//�ٴ���ʾ"���ظ���"
	        		                loadProgressBar.setVisibility(View.GONE); //�ٴ����ء���������
	        					}
	        					break;
	        				case 2:
	        					adapter2.count += ApkListViewInfo.getLength();
	        					adapter2.notifyDataSetChanged();
	        					if(jumpUrl[2]== null ) {
	        						 moreTextView.setVisibility(View.GONE);//�ٴ���ʾ"���ظ���"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//�ٴ���ʾ"���ظ���"
	        		                loadProgressBar.setVisibility(View.GONE); //�ٴ����ء���������
	        					}
	        					break;
	        				
	        				case 3:
	        					adapter3.count += ApkListViewInfo.getLength();
	        					adapter3.notifyDataSetChanged();
	        					if(jumpUrl[3] == null ) {
	        						 moreTextView.setVisibility(View.GONE);//�ٴ���ʾ"���ظ���"
	        		                loadProgressBar.setVisibility(View.GONE);
	        					}else {
	        		                moreTextView.setVisibility(View.VISIBLE);//�ٴ���ʾ"���ظ���"
	        		                loadProgressBar.setVisibility(View.GONE); //�ٴ����ء���������
	        					}
	        					break;

	        				case 100:
	        					ShowExceptionDialog.showNetDialog(ApkPreview.this,
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
	        						}
	        						i++;
	        					}
	        					jumpUrl[position] = ApkListViewInfo.getJumpUrl();
	        				
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
	
	/*******��������*******/
	private void loading(final int posetion, final String url) {

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
						}
						i++;
					}
					jumpUrl[posetion] = ApkListViewInfo.getJumpUrl();
					
					Message msg = new Message();
					msg.what = posetion;
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
	
	/******* ����UI���� ******/
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
				adapter1 = new ApkListImageAndTextListAdapter(ApkPreview.this,dataArray1, list1);
				list1.setAdapter(adapter1);
			
				break;

			case 2:
				loginDialog.cancel();
				dialogFlag2 = false;
				if (jumpUrl[2]!= null ) {
					addPageMore(list2, 2);
				}
				adapter2 = new ApkListImageAndTextListAdapter(ApkPreview.this,dataArray2, list2);
				list2.setAdapter(adapter2);

				break;

			case 3:
				loginDialog.dismiss();
				dialogFlag3 = false;
				if ( jumpUrl[3] != null ) {
					addPageMore(list3, 3);
				}
				adapter3 = new ApkListImageAndTextListAdapter(ApkPreview.this,dataArray3, list3);
				list3.setAdapter(adapter3);

				break;
				
			case 100:
				if(couldSend) {
				ShowExceptionDialog.showNetDialog(ApkPreview.this,
						"���������쳣�������������ӻ��Ժ����ԣ�");
				}
				break;
			}
		}
	}

	/*******����******/
	public void search(){
		final Handler handler = new Handler() {
			public void handleMessage(Message messages) {
				switch (messages.what) {
				case 1:
					loginDialog.cancel();
					if (resulit) {
						Intent intent = new Intent(ApkPreview.this, SearchResult.class);
						intent.putExtra("name", seatchString);
						Log.v("seatchString",seatchString);
						startActivity(intent);
					} else {
						ShowExceptionDialog.showNetDialog(ApkPreview.this, "��Ǹ�������������");
					}
					break;
				case 2:
					loginDialog.cancel();
					ShowExceptionDialog.showNetDialog(ApkPreview.this, "�����쳣��");
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
	
	/******* ��ת����ϸҳ�� ********/
	public void startApkContent(ApkListImageAndTextListAdapter adapter,
			int posetion) {
		ApkListImageAndText imageAndText = adapter.getItem(posetion);
		Intent intent = new Intent(ApkPreview.this, ApkContent.class);
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

	@SuppressLint("HandlerLeak")
	public void closeDrawer() {
		/***** �رճ��� ******/
		if (slidingDrawer_down.isOpened()) {
			slidingDrawer_down.animateClose();
		}
		/******* ���ؼ��� *****/
		InputMethodManager keyboard = (InputMethodManager) getSystemService(ApkPreview.INPUT_METHOD_SERVICE);
		keyboard.hideSoftInputFromWindow(search_textview.getWindowToken(), 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && slidingDrawer_down.isOpened()) {
			closeDrawer();

		} else if (keyCode == KeyEvent.KEYCODE_BACK
				&& !slidingDrawer_down.isOpened()) {
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
