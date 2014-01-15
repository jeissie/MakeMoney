package com.jesse.fragment;

import com.jesse.dao.SLMenuListOnItemClickListenerDao;
import com.jesse.makemoney.R;
import com.jesse.makemoney.R.id;
import com.jesse.makemoney.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FrameItems extends Fragment {
	private SLMenuListOnItemClickListenerDao mCallback;
	private LayoutInflater inflater;
	private View rootView;
	private ListView itemsList;
	// 首次绘制用户界面的回调方法，必须返回要创建的Fragments 视图UI
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		rootView = inflater.inflate(R.layout.frame_items, container, false);
		setViewResource();
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		try {
			mCallback = (SLMenuListOnItemClickListenerDao) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}

	private void setViewResource() {
		itemsList = (ListView) rootView.findViewById(R.id.left_menu);  
	}
}
