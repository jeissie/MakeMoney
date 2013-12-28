package com.news;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exceptiondialog.ShowExceptionDialog;
import com.getimageandtext.NewsParticularInfo;
import com.jesse.mxapk.R;
import com.jsoup.JsoupNewsParticular;

/**
 * @author jie
 * 
 */
public class NewsContent extends Activity {

	// private WebView newWebView;
	private ProgressBar progressBar;
	private Button backButton;
	private TextView textContent, articleTitle, articleIssueTime,
			articleComment, articleGlance;
	private String url, title, time, comment, galnce;
	private String htmlString;
	private Handler handler;
	private Spanned htmlSpan;
	private boolean couldSend = true;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_particular);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			url = extras.getString("url");
			title = extras.getString("title");
			time = extras.getString("time");
			comment = extras.getString("comment");
			galnce = extras.getString("glance");
		}
		couldSend = true;
		Looper looper = Looper.myLooper();
		handler = new MessageHandler(looper);
		initCompont();// 初始化组件
		loading(url);// 下载并填充信息

	}

	// 初始化组件
	private void initCompont() {
		textContent = (TextView) findViewById(R.id.webView1);
		articleTitle = (TextView) findViewById(R.id.articleTitle);
		articleIssueTime = (TextView) findViewById(R.id.articleIssueTime);
		articleComment = (TextView) findViewById(R.id.comment);
		articleGlance = (TextView) findViewById(R.id.glance);

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		backButton = (Button) findViewById(R.id.back_button);

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	// 读取文字内容
	public void loading(final String url) {

		articleTitle.setText(title);
		articleIssueTime.setText(time);
		articleComment.setText(comment);
		articleGlance.setText(galnce);
		new Thread() {
			@Override
			public void run() {

				try {
					JsoupNewsParticular jsoup = new JsoupNewsParticular();
					jsoup.getNewsParticular(url);
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

	// 读取图片
	public void loadingImage() {

		new Thread() {
			@Override
			public void run() {
				htmlString = NewsParticularInfo.getNewsHtml();
				htmlSpan = Html.fromHtml(htmlString, imgGetter2, null);
				Message msg = new Message();
				msg.what = 2;
				handler.sendMessage(msg);
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
				htmlString = NewsParticularInfo.getNewsHtml();
				textContent
						.setText(Html.fromHtml(htmlString, imgGetter1, null));
				progressBar.setVisibility(View.GONE);
				loadingImage();// 读取图片
				break;

			case 2:
				textContent.setText(htmlSpan);
				break;

			case 100:
				if(couldSend) {
				ShowExceptionDialog.showNetDialog(NewsContent.this, "网络连接异常，请检查网络连接或稍后再试！");
				}
				break;
			}
		}
	}

	final ImageGetter imgGetter1 = new Html.ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = null;

			return drawable;
		}
	};

	final ImageGetter imgGetter2 = new Html.ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			if (!source.startsWith("http://")) {
				source = "http://www.mxapk.com" + source;
			}
			Drawable drawable = null;
			URL url;
			InputStream inputStream = null;

			try {
				url = new URL(source);
				inputStream = (InputStream) url.getContent();
			} catch (MalformedURLException e) {
				if(couldSend) {
				ShowExceptionDialog.showNetDialog(NewsContent.this, "对不起，该文章网址错误或已经删除！");
				}
				e.printStackTrace();
			} catch (IOException e) {
				if(couldSend) {
				ShowExceptionDialog.showNetDialog(NewsContent.this, "网络连接异常，请检查网络连接或稍后再试！");
				}
				e.printStackTrace();
			}
			drawable = Drawable.createFromStream(inputStream, "src");
			if (drawable != null)
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight());
			return drawable;
		}
	};

	@Override
	protected void onPause() {
		couldSend = false;
		super.onPause();
	}
	
}
