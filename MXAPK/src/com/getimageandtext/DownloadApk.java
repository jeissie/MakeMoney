package com.getimageandtext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import com.context.ContextUtil;
import com.exceptiondialog.ShowExceptionDialog;
import com.jesse.mxapk.MxApkActivity;
import com.jesse.mxapk.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DownloadApk {
	private static int fileSize; // �ļ���С
	private static String apkName; // �ļ�����
	private static File file;
	private static int progress = 0; // ��������ݴ�С
	private static RemoteViews view = null;
	private static Notification notification;
	private static NotificationManager manager = null;
	private static PendingIntent pIntent = null;// ������ʾ
	private static int times = 0;
	private static Intent intent = new Intent();
	private static int notificationNum;
	public static void downLoadApk(String name, final String apkurl) {

		final String fileName = name + ".apk";
		apkName = name + ":";
		File tmpFile = new File("/sdcard/Download/APK"); // ����·��
		if (!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		file = new File("/sdcard/Download/APK/" + fileName);

		new Thread() {
			@Override
			public void run() {
				try {
					URL url = new URL(apkurl);
					try {
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.connect();
						fileSize = conn.getContentLength();// ������Ӧ��ȡ�ļ���С
						InputStream in = conn.getInputStream();
						FileOutputStream fos = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						progress = 0;
						if (conn.getResponseCode() >= 400) {
							Toast.makeText(ContextUtil.getInstance(), "���ӳ�ʱ",
									Toast.LENGTH_SHORT).show();
							sendMsg(3);
						} else {
							sendMsg(0);
							while (true) {
								if (in != null) {
									int numRead = in.read(buf);
									if (numRead <= 0) {
										break;
									} else {
										fos.write(buf, 0, numRead);
										progress += numRead;
										//���200�θ���һ�Σ����������ʱҲ����һ�Σ�����
										if (times == 200 || fileSize == file.length()) {
											sendMsg(1);
											times = 0;
										}
										times++;
									}

								} else {
									Toast.makeText(ContextUtil.getInstance(),
											"������ϣ�����ȴ������豸���ӡ�",
											Toast.LENGTH_SHORT);
									break;
								}
							}
						}

						conn.disconnect();
						fos.close();
						in.close();
						sendMsg(2);// ��װӦ��
					} catch (IOException e) {
						
						e.printStackTrace();

					}

				} catch (MalformedURLException e) {
					
					e.printStackTrace();

				}

			}

		}.start();

	}

	private static void showNotification() {
		manager = (NotificationManager) ContextUtil.getInstance()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notification = new Notification();
		notification.icon = R.drawable.icon; // ����֪ͨ��״̬����ʾ��ͼ��
		notification.tickerText = apkName + "��ʼ���أ�"; // ֪ͨʱ��״̬����ʾ����
		view = new RemoteViews(ContextUtil.getInstance().getPackageName(),
				R.layout.notification);
		view.setImageViewResource(R.id.notificationImage, R.drawable.icon);  //ͼ��
		view.setTextViewText(R.id.notificationTitle, apkName + "�����أ�");   //��ʼ������
		view.setTextViewText(R.id.notificationProgressText, progress  + "%");
		view.setProgressBar(R.id.notificationProgress, 100, progress, false);  //��ʼ��������
		notification.contentView = view;
		pIntent = PendingIntent.getActivity(ContextUtil.getInstance(), 0, intent, 0);
		notification.contentIntent = pIntent;
		Calendar c = Calendar.getInstance();
		notificationNum = c.get(Calendar.MINUTE)+c.get(Calendar.MILLISECOND);
		manager.notify(notificationNum, notification);
	}

	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// ����һ��Handler�����ڴ��������߳���UI��ͨѶ
			switch (msg.what) {
			case 0:
				showNotification();
				break;

			case 1:
				view.setTextViewText(R.id.notificationProgressText, file.length()/(1024) + "kb");
				view.setProgressBar(R.id.notificationProgress, 100, progress,
						false);
				manager.notify(notificationNum, notification);
				break;
			case 2:
				openFile(file);
				break;
			case 3:

				break;
			}
		}
	};

	private static void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler.sendMessage(msg);
	}

	private static void openFile(File file) {
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		ContextUtil.getInstance().startActivity(intent);

	}

}
