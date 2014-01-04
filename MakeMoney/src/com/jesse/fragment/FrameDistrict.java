package com.jesse.fragment;

import com.jesse.makemoney.R;
import com.jesse.slidingMenu.MenuFragment.SLMenuListOnItemClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FrameDistrict extends Fragment {
	private SLMenuListOnItemClickListener mCallback; 
	
	// 首次绘制用户界面的回调方法，必须返回要创建的Fragments 视图UI
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        return inflater.inflate(R.layout.frame_district, container, false);  
    } 

	@Override
	public void onAttach(Activity activity) {
		try {
			mCallback = (SLMenuListOnItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}
}