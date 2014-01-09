package com.jesse.apkList;

import com.jesse.makemoney.R;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author jie
 * 
 */
public class ApkListViewCache {

	private View baseView;
	private ImageView imageIcon;
	private TextView apk_zh_name;
	private TextView apk_money;
	private ImageButton downloads;
	private RatingBar ratingBar;
	
	public ApkListViewCache(View baseView) {
		this.baseView = baseView;
	}

	public TextView getApk_zh_name() {
		if (apk_zh_name == null) {
			apk_zh_name = (TextView) baseView.findViewById(R.id.apkName);
		}
		return apk_zh_name;
	}

	public TextView getApk_money() {
		if (apk_money == null) {
			apk_money = (TextView) baseView.findViewById(R.id.moeny);
		}
		return apk_money;
	}

	public ImageButton getDownloadsBtn() {
		if (downloads == null) {
			downloads = (ImageButton) baseView.findViewById(R.id.button);
		}
		return downloads;
	}

	public ImageView getImageView_icon() {
		if (imageIcon == null) {
			imageIcon = (ImageView) baseView.findViewById(R.id.apkIcon);
		}
		return imageIcon;
	}
	
	public RatingBar getRatingBar() {
		if (ratingBar == null) {
			ratingBar = (RatingBar) baseView.findViewById(R.id.star);
		}
		return ratingBar;
	}

}
