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
	private List<View> listViews; // ҳ���б�
	private Button backButton;
	private ImageView cursor;// ����ͼƬ
	private TextView t1, t2, t3;
	private int offset = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���
	private long codesize = 0; // Ӧ�ó����С
	private List<MapListImageAndText_main> dataArray; // listview�����б�
	private ProgressDialog loginDialog; // ������
	private GetInfo getInfo; // AsyncTask
	private List<PackageInfo> packages; // apk��Ϣ�б�
	private PackageInfo packageInfo;
	private int apkList_position[][];  //����listviewλ��
	public static String apk_info[][];  //apk�����б�
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apk_manager);
		InitImageView(); // ��ʼ������
		InitTextView(); // ��ʼ��ͷ��
		InitViewPager(); // ��ʼ��ѡ�
		dataArray = new ArrayList<MapListImageAndText_main>(); // listview�����б�
		loginDialog = new ProgressDialog(ApkManager.this);// ���ý�����
		getInfo = new GetInfo();// AsyncTask��ȡ��Ϣ
		packages = getPackageManager().getInstalledPackages(0);// apk��Ϣ�б�
		packageInfo = new PackageInfo();
		getInfo.execute("getInfo");// ����б�������
		
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	/******* ͷ���ʼ�� *******/
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
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
		for (int i = 0; i < 3; i++) {
			listViews.add(mInflater.inflate(R.layout.listview, null));
		}
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
		offset = (screenW / 3 - bmpW) / 2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// ���ö�����ʼλ��
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

			if (arg1 < 3) {
				((ViewPager) arg0).addView(mListViews.get(arg1 % 3), 0);
			}
			// ����ҳ��1�ڵİ�ť�¼�
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
						// Apk����
						final String apk_zh_name = (String) packageInfo.applicationInfo
								.loadLabel(getPackageManager());
						// Apk�汾����
						String apk_version = packageInfo.versionName;
						// Apk��װʱ��
						long systime = packageInfo.firstInstallTime;
						String time = (String) DateFormat.format("yyyy-MM-dd",
								systime);
						// Apk��С
						try {
							queryPacakgeSize(packageInfo.packageName);
							packageInfo = pm.getPackageInfo(packageInfo.packageName,
									PackageManager.GET_PERMISSIONS);
						} catch (Exception e) {
							e.printStackTrace();
						}
						String apk_size = Formatter.formatFileSize(
								ApkManager.this, codesize); // Apk��С
						
						//��ȡӦ��Ȩ��
						String[] permission = packageInfo.requestedPermissions;
						//�����Զ���Ի���
						final Dialog dialog = new Dialog(ApkManager.this);

						dialog.setContentView(R.layout.apk_info_dialog);
						dialog.setTitle("������Ϣ");

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
							permissions.setText("��");
						}

						Button open = (Button) dialog
								.findViewById(R.id.open_button);
						open.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								//����Ӧ��
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
								// ��ת��Ӧ��������Ϣ
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

	/******** ҳ���л����� ********/
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

		// ��ǰѡ��ҳ�涥������������
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
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
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
				
				// ��ȡ��ϵͳӦ��
				if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0) {
					
					apkList_position[amuont][0] = i;//����listview
					apk_info [amuont][0] = packageInfo.packageName;//�������������listview
					// Apk��icon
					Drawable apk_icon = packageInfo.applicationInfo
							.loadIcon(getPackageManager());
					// Apk����
					String apk_zh_name = (String) packageInfo.applicationInfo
							.loadLabel(getPackageManager());
					// Apk�汾����
					String apk_version = packageInfo.versionName;
					// Apk��װʱ��
					long systime = packageInfo.firstInstallTime;
					String time = (String) DateFormat.format("yyyy-MM-dd",
							systime);
					// Apk��С
					try {
						queryPacakgeSize(packageInfo.packageName);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String apk_size = Formatter.formatFileSize(
							ApkManager.this, codesize); // Apk��С

					// ���������
					MapListImageAndText_main test = new MapListImageAndText_main(
							apk_icon, apk_zh_name, apk_version, time, apk_size);
					dataArray.add(test);
					amuont++;
				}
			}
			return null;
		}

		// �������߳�
		@Override
		protected void onPostExecute(Object result) {

			MapListApkAdapter_content1 adapter = new MapListApkAdapter_content1(
					ApkManager.this, dataArray, apk_installed);
			apk_installed.setAdapter(adapter);
			loginDialog.cancel();
			super.onPostExecute(result);

		}

		// ����Ԥ���������
		@Override
		protected void onPreExecute() {

			loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // ����Բ��
			loginDialog.setMessage("���ڶ�ȡ");
			loginDialog.setCancelable(true); // �˻ؼ�ȡ��
			loginDialog.show();
			super.onPreExecute();

		}

	}

	public void queryPacakgeSize(String pkgName) throws Exception {
		if (pkgName != null) {
			// ʹ�÷�����Ƶõ�PackageManager������غ���getPackageSizeInfo
			PackageManager pm = getPackageManager(); // �õ�pm����
			try {
				// ͨ��������ƻ�ø����غ���
				Method getPackageSizeInfo = pm.getClass().getDeclaredMethod(
						"getPackageSizeInfo", String.class,
						IPackageStatsObserver.class);
				// ���øú��������Ҹ��������� ��������������ɺ��ص�PkgSizeObserver��ĺ���
				getPackageSizeInfo.invoke(pm, pkgName, new PkgSizeObserver());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw ex; // �׳��쳣
			}
		}
	}

	// aidl�ļ��γɵ�Bindler���Ʒ�����
	public class PkgSizeObserver extends IPackageStatsObserver.Stub {
		/***
		 * �ص�������
		 * 
		 * @param pStatus
		 *            ,�������ݷ�װ��PackageStats������
		 * @param succeeded
		 *            ����ص��ɹ�
		 */
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			codesize = pStats.codeSize; // Ӧ�ó����С
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
	
	//��װ��ж��Ӧ�ú����¶�ȡ����
	@Override
	protected void onRestart() {
		this.onCreate(null)	;
		}
}
