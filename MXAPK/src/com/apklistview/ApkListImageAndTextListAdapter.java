package com.apklistview;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.apklistview.AsyncImageLoader.ImageCallback;
import com.context.ContextUtil;
import com.getimageandtext.ApkListViewInfo;
import com.getimageandtext.DownloadApk;
import com.jesse.mxapk.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jie
 * 
 */
public class ApkListImageAndTextListAdapter extends ArrayAdapter<ApkListImageAndText> {

	private ListView listView;
	private Button download_button;
	private AsyncImageLoader asyncImageLoader;
	private int star = 0;
	private String apkName;
	public int count = 0;
	
	@Override
    public int getCount() {
        return count;
    }
	
	public ApkListImageAndTextListAdapter(Activity activity,
			List<ApkListImageAndText> imageAndTexts, ListView listView) {
		super(activity, 0, imageAndTexts);
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
		count = ApkListViewInfo.getLength();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final Activity activity = (Activity) getContext();
		ImageView imageView_icon = null;
		ImageView imageView_star = null;

		// 填充行listview
		View rowView = convertView;
		ApkListViewCache viewCache;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.apk_content, null);
			viewCache = new ApkListViewCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (ApkListViewCache) rowView.getTag();
		}
		final ApkListImageAndText imageAndText = getItem(position);

		TextView apk_zh_name = viewCache.getApk_zh_name();
		apk_zh_name.setText(imageAndText.getApk_zh_name());

		TextView apk_en_name = viewCache.getApk_en_name();
		apk_en_name.setText(imageAndText.getApk_en_name());

		TextView time = viewCache.getTime();
		time.setText(imageAndText.getTime());

		TextView apk_size = viewCache.getApk_size();
		apk_size.setText(imageAndText.getApk_size());

		TextView downloads = viewCache.getDownloads();
		downloads.setText(imageAndText.getDownloads());

		download_button = (Button) rowView.findViewById(R.id.download_apk);
		download_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String url = imageAndText.getUrl();
				apkName = imageAndText.getApk_zh_name()+ imageAndText.getApk_en_name();
				DownloadApk.downLoadApk(apkName, url);

				Toast.makeText(ContextUtil.getInstance(), apkName + "准备下载",
						Toast.LENGTH_SHORT).show();
			}
		});

		// 读取图片信息并填充
		String imageUrl = imageAndText.getImageUrl();
		if (!imageUrl.equals("null")) {
			imageView_icon = viewCache.getImageView_icon();
			imageView_icon.setTag(imageUrl);
			Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl,
					new ImageCallback() {
						public void imageLoaded(Drawable imageDrawable,
								String imageUrl) {
							ImageView imageViewByTag = (ImageView) listView
									.findViewWithTag(imageUrl);
							if (imageViewByTag != null) {
								imageViewByTag.setImageDrawable(imageDrawable);
							}
						}
					});

			if (cachedImage == null) {
				imageView_icon.setImageResource(R.drawable.icon);
			} else {
				imageView_icon.setImageDrawable(cachedImage);
			}
		} else {
			imageView_icon = viewCache.getImageView_icon();
			imageView_icon.setImageResource(R.drawable.icon);// 此处设置无图片
		}

		star = imageAndText.getStar();
		switch (star) {
		case 1:
			imageView_star = viewCache.getImageView_star();
			imageView_star.setImageResource(R.drawable.star_1);
			break;
		case 2:
			imageView_star = viewCache.getImageView_star();
			imageView_star.setImageResource(R.drawable.star_2);
			break;
		case 3:
			imageView_star = viewCache.getImageView_star();
			imageView_star.setImageResource(R.drawable.star_3);
			break;
		case 4:
			imageView_star = viewCache.getImageView_star();
			imageView_star.setImageResource(R.drawable.star_4);
			break;
		case 5:
			imageView_star = viewCache.getImageView_star();
			imageView_star.setImageResource(R.drawable.star_5);
			break;
		default:
			imageView_star = viewCache.getImageView_star();
			imageView_star.setImageResource(R.drawable.star_0);
			break;
		}

		return rowView;
	}
	
}
