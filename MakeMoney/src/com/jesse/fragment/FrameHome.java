package com.jesse.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.jesse.gallery.BitMapGroup;
import com.jesse.gallery.MyGallery;
import com.jesse.gallery.PageAdapter;
import com.jesse.gallery.Util;
import com.jesse.makemoney.R;
import com.jesse.model.GetImageOnMain;
import com.jesse.slidingMenu.MenuFragment.SLMenuListOnItemClickListener;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;

public class FrameHome extends Fragment {
	private SLMenuListOnItemClickListener mCallback; 
	private MyGallery gallery;
	private LinearLayout linearLayout;
	private List<BitMapGroup> objects;
	private TextView galleryName;
	private boolean ifSetDot = true;
	
	// 首次绘制用户界面的回调方法，必须返回要创建的Fragments 视图UI
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		View rootView = inflater.inflate(R.layout.frame_home, container, false);  
		setViewResource(rootView);
        return rootView;
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
	
	private void setViewResource(View rootView) {  
		gallery = (MyGallery) rootView.findViewById(R.id.main_gallery);
		linearLayout = (LinearLayout) rootView.findViewById(R.id.gallery_conunt);
		try {
			initGallery();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/****** Gallery*******/
	private void initGallery() throws FileNotFoundException, IOException {
		
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});

		objects = Util.getContent(getActivity());
		if (objects != null) {
			int count = objects.size();
			if (count > 0) {
				PageAdapter adapter = new PageAdapter(getActivity(), objects);
				gallery.setAdapter(adapter);
				gallery.setOnItemSelectedListener(selectedListener);
				setDot(count);
				ifSetDot = false;
			} 
		} else {
			Log.v("MakeMoney", "objects is null");
		}
	}

	/******* 动点 ********/
	private void setDot(int count) {
		for (int i = 0; i < count; i++) {
			ImageView imageView = new ImageView(getActivity());
			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.selected_dot);
			} else {
				imageView.setBackgroundResource(R.drawable.round);
			}
			imageView.setId(i);
			linearLayout.addView(imageView, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

		}
	}

	private void clearDot() {
		int i = 0;
		if (objects != null) {
			while (i < objects.size()) {
				ImageView image = (ImageView) linearLayout.findViewById((i)
						% objects.size());
				if (image != null) {
					image.setBackgroundResource(R.drawable.round);
				}
				i++;
			}
		}

	}

	/******** gallery ********/
	private OnItemSelectedListener selectedListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
//			if (GetImageOnMain.getInstance().getBitMapGroup().get(arg2 % objects.size()).getMainGalleryName() != null) {
//				galleryName.setText(GetImageOnMain.getInstance().getBitMapGroup().get(arg2 % objects.size()).getMainGalleryName());
//			}
			
			ImageView imageView = (ImageView) linearLayout.findViewById(arg2
					% objects.size());
		
			imageView.setBackgroundResource(R.drawable.selected_dot);
	
			ImageView imageLeft = (ImageView) linearLayout
					.findViewById((arg2 - 1) % objects.size());
			ImageView imageRight = (ImageView) linearLayout
					.findViewById((arg2 + 1) % objects.size());
	
			if (imageLeft != null) {
				imageLeft.setBackgroundResource(R.drawable.round);
			}
			if (imageRight != null) {
				imageRight.setBackgroundResource(R.drawable.round);
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};
}
