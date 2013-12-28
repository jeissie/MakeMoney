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
 * @author jie ��ҳ����չʾ����Apk����ϸҳ��
 */
@SuppressLint("HandlerLeak")
public class ApkContent extends Activity {

	private Button comment_button, download_button, more_button, back_button;
	private TextView apk_name, apk_times, apk_size, apk_version, apk_author,
			apk_publish, t1, t2, t3, t4, download_image;
	private Gallery apk_content_gallery;
	private ImageView apk_star, apkicon;
	private ViewPager paper_content;
	private List<View> listViews; // ҳ���б�
	private ImageView cursor;// ����ͼƬ
	private int offset = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���
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
			apkUrl = extras.getString("apkUrl"); // ҳ���ַ
			apkName = extras.getString("apkName");// ������ص�ַ
			version = extras.getString("version");// ����汾
			apkSize = extras.getString("apkSize");// ������
			apkDownloadTimes = extras.getString("apkDownloadTimes");// ���ش���
			apkTime = extras.getString("apkTime");// ����ʱ��
			star = extras.getInt("star");
			apkDownloadUrl = extras.getString("apkDownloadUrl");// ���ص�ַ
		}
		loginDialog = new ProgressDialog(this);// ���ý�����
		couldSend = true;
		InitButton(); // ������ʼ��
		InitTextview();// ���ֳ�ʼ��
		InitImageView(); // ��ʼ������
		InitTextView_title(); // ��ʼ��ͷ��
		InitViewPager(); // ��ʼ��ҳ��
		Star(star);
		loadingIcon(apkUrl);
		loadingGallery(apkUrl);

		apkicon = (ImageView) findViewById(R.id.apk_icon);

		Looper looper = Looper.myLooper();
		handlers = new MessageHandler(looper);

	}

	/***** ������ʼ�� ******/
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

	/***** �������� ******/
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
				Toast.makeText(ApkContent.this, apkName+"׼������", Toast.LENGTH_SHORT).show();
				break;
			case R.id.more_button:
				openOptionsMenu(); // ��ѡ��˵�
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

	/****** ���ֳ�ʼ�� *******/
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

	/******* ͷ���ʼ�� *******/
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

	/***** Gallery��ʼ�� *******/
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

	/***** Gallery������ *******/
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
	}

	/***** ҳ�����ݳ�ʼ�� ******/
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

	/***** ViewPager������ ******/
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
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			if (arg1 < 4) {
				((ViewPager) arg0).addView(mListViews.get(arg1 % 4), 0);
			}
			/*
			 * if (arg1 == 0) { text1 = (TextView)
			 * arg0.findViewById(R.id.text_content); text1.setText("����1"); } if
			 * (arg1 == 1) { text1 = (TextView)
			 * arg0.findViewById(R.id.text_content); text1.setText("����2"); } if
			 * (arg1 == 2) { text1 = (TextView)
			 * arg0.findViewById(R.id.text_content); text1.setText("����3"); } if
			 * (arg1 == 3) { text1 = (TextView)
			 * arg0.findViewById(R.id.text_content); text1.setText("����4"); }
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

	/******** ҳ���л����� ********/
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����
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
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			cursor.startAnimation(animation);

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/****** ����Menu�˵� ******/
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ONE, 0, "�����Ӧ��");// ����������ͼ��
		menu.add(0, TWO, 0, "�ٱ�");
		menu.add(0, THREE, 0, "��һ��");
		menu.add(0, FOUR, 0, "��һ��");
		return super.onCreateOptionsMenu(menu);
	}

	/***** Menu���� ******/
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Toast.makeText(this, "�ѷ���", Toast.LENGTH_SHORT).show();
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
		loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // ����Բ��
		loginDialog.setMessage("���ڶ�ȡ");
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
				ShowExceptionDialog.showNetDialog(ApkContent.this, "���������쳣�������������ӻ��Ժ����ԣ�");
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
