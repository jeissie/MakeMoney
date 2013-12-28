package com.apkloading;

import java.util.List;

import com.apkmanager.ApkManager;
import com.jesse.mxapk.R;

import android.app.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author jie
 * 软件管理第一个页面Adapter
 */
public class MapListApkAdapter_content1 extends
		ArrayAdapter<MapListImageAndText_main> {

	private ListView listView;
	private ImageView uninstall_button;

	public MapListApkAdapter_content1(Activity activity,
			List<MapListImageAndText_main> imageAndTexts, ListView listView) {
		super(activity, 0, imageAndTexts);
		this.listView = listView;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();

		// 填充行listview
		View rowView = convertView;
		MapListViewCache viewCache;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.apk_maneger_content_1, null);
			viewCache = new MapListViewCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (MapListViewCache) rowView.getTag();
		}
		
		MapListImageAndText_main imageAndText = getItem(position);
		
		// 填充信息
		ImageView imageView_icon = viewCache.getImageView_icon();
		imageView_icon.setImageDrawable(imageAndText.getApk_icon());
		
		 
		TextView apk_zh_name = viewCache.getApk_zh_name();
		apk_zh_name.setText(imageAndText.getApk_zh_name());

		TextView apk_version = viewCache.getApk_version();
		apk_version.setText(imageAndText.getApk_version());

		TextView time = viewCache.getTime();
		time.setText(imageAndText.getTime());

		TextView apk_size = viewCache.getApk_size();
		apk_size.setText(imageAndText.getApk_size());

		uninstall_button = (ImageView) rowView.findViewById(R.id.uninstall_button);
		uninstall_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri packageURI = Uri.parse("package:"+ApkManager.apk_info[position][0]);
				Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
				getContext().startActivity(uninstallIntent);		
			}
		});
		
		return rowView;
	}
}
