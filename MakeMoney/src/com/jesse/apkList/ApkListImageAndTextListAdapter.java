package com.jesse.apkList;

import java.util.List;

import com.jesse.apkList.AsyncImageLoader.ImageCallback;
import com.jesse.makemoney.R;

import android.app.Activity;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author jie
 * 
 */
public class ApkListImageAndTextListAdapter extends ArrayAdapter<ApkListImageAndText> {

	private ListView listView;
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
		count = 10;  //TODO get the length in net
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final Activity activity = (Activity) getContext();
		ImageView imageView_icon = null;
		ImageView imageView_star = null;

		// 实例化listview
		View rowView = convertView;
		ApkListViewCache viewCache;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listview_item, null);
			viewCache = new ApkListViewCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (ApkListViewCache) rowView.getTag();
		}
		final ApkListImageAndText imageAndText = getItem(position);

		TextView apk_zh_name = viewCache.getApk_zh_name();
		apk_zh_name.setText(imageAndText.getApk_zh_name());
		
		TextView apk_money = viewCache.getApk_money();
		apk_money.setText(imageAndText.getMoeny());
		
//		TextView apk_size = viewCache.getApk_size();
//		apk_size.setText(imageAndText.getApk_size());
		
		RatingBar ratingBar = viewCache.getRatingBar();
		ratingBar.setNumStars(imageAndText.getStar());
		
		ImageButton download_button = viewCache.getDownloadsBtn();
		download_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Download or open apk
				
			}
		});

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
				imageView_icon.setImageResource(R.drawable.downloading);
			} else {
				imageView_icon.setImageDrawable(cachedImage);
			}
		} else {
			imageView_icon = viewCache.getImageView_icon();
			imageView_icon.setImageResource(R.drawable.downloading); 
		}

		return rowView;
	}
	
}
