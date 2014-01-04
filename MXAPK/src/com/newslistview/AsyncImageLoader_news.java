package com.newslistview;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.exceptiondialog.ShowExceptionDialog;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

/**
 * @author jie
 * 
 */
public class AsyncImageLoader_news {

	private HashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader_news() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				switch (message.what) {
				case 0:
					imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
					break;

				}

			}
		};

		// 下载图片
		new Thread() {
			@Override
			public void run() {
				Drawable drawable;
				try {
					drawable = loadImageFromUrl(imageUrl);
					imageCache.put(imageUrl, new SoftReference<Drawable>(
							drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
				
					e.printStackTrace();
				}

			}
		}.start();
		return null;
	}

	// 根据url下载图片
	public static Drawable loadImageFromUrl(String url) throws IOException {
		URL m;
		InputStream i = null;
		m = new URL(url);
		i = (InputStream) m.getContent();
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
}
