package com.jesse.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jesse.apkList.ApkListImageAndText;
import com.jesse.apkList.ApkListImageAndTextListAdapter;
import com.jesse.dao.SLMenuListOnItemClickListenerDao;
import com.jesse.gallery.MyGallery;
import com.jesse.makemoney.R;
import com.jesse.util.ContextUtil;
import com.jesse.util.MyView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FrameDistrict extends Fragment {
	private SLMenuListOnItemClickListenerDao mCallback;
	private View rootView;
	private LayoutInflater inflater;
	private PagerTitleStrip pagerTitleStrip;// viewpager的标题
	private PagerTabStrip pagerTabStrip;// 一个viewpager的指示器，效果就是一个横的粗的下划线
	private ListView view1, view2, view3; // 需要滑动的页卡
	private ViewPager paper_content;
	private List<ApkListImageAndText> dataArray1 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray2 = new ArrayList<ApkListImageAndText>();
	private List<ApkListImageAndText> dataArray3 = new ArrayList<ApkListImageAndText>();
	private ApkListImageAndTextListAdapter adapter1;
	private ApkListImageAndTextListAdapter adapter2;
	private ApkListImageAndTextListAdapter adapter3;
	private Activity m_activity;
	private Handler handler;
	private List<List<ApkListImageAndText>> resourceList = new ArrayList<List<ApkListImageAndText>>();
	private List<ApkListImageAndTextListAdapter> adapterList = new ArrayList<ApkListImageAndTextListAdapter>();
	private List<ListView> listPage = new ArrayList<ListView>();	// every page in ViewPager
 	// 首次绘制用户界面的回调方法，必须返回要创建的Fragments 视图UI
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		rootView = inflater.inflate(R.layout.frame_district, container, false);
		setViewResource();
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		m_activity= activity;
		try {
			mCallback = (SLMenuListOnItemClickListenerDao) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}

	private void setViewResource() {
		pagerTabStrip = (PagerTabStrip) rootView.findViewById(R.id.pagertab);
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.blue));
		pagerTabStrip.setDrawFullUnderline(false);
		pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.white));
		pagerTabStrip.setTextSpacing(50);
		InitViewPager();
		paper_content.setCurrentItem(1);
		Looper looper = Looper.myLooper();
		handler = new MessageHandler(looper);
	}

	private void InitViewPager() {
		paper_content = (ViewPager) rootView.findViewById(R.id.viewpager);
		List<View> listViews = new ArrayList<View>();	// view container
		List<String> everyPageTitle = new ArrayList<String>();	
		
		listViews.add(inflater.inflate(R.layout.listview, null));
		listViews.add(inflater.inflate(R.layout.listview, null));
		listViews.add(inflater.inflate(R.layout.listview, null));

		listPage.add(view1);
		listPage.add(view2);
		listPage.add(view3);
		
		resourceList.add(dataArray1);
		resourceList.add(dataArray2);
		resourceList.add(dataArray3);
		
		adapterList.add(adapter1);
		adapterList.add(adapter2);
		adapterList.add(adapter3);
		
		everyPageTitle.add("Title 1");
		everyPageTitle.add("Title 2");
		everyPageTitle.add("Title 3");
		
		paper_content.setAdapter(new MyPagerAdapter(listViews, listPage, everyPageTitle));
		paper_content.setCurrentItem(0);
		// paper_content.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public class MyPagerAdapter extends PagerAdapter {
		private List<View> mListViews;
		private List<ListView> mListPage;
		private List<String> mEveryPageTitle;

		public MyPagerAdapter(List<View> listViews, List<ListView> listPage, List<String> everyPageTitle) {
			this.mListViews = listViews;
			this.mListPage = listPage;
			this.mEveryPageTitle = everyPageTitle;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListViews.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return (mEveryPageTitle.size() > position) ? mEveryPageTitle.get(position) : "";  
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == (arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		// 根据postion来实例化item
		public Object instantiateItem(View arg0, int position) {
			if (position < 4) {
				((ViewPager) arg0).addView(
						mListViews.get(position % mListViews.size()), 0);
			}
			ListView everyView = mListPage.get(position);
			everyView = (ListView) rootView.findViewById(R.id.listView);
			listPage.remove(position);
			listPage.add(position, everyView);
			loadingList(position);
			everyView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

				}

			});
			return position;
		}
	}
	
	public void loadingList(final int posetion){
		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					ApkListImageAndText test = new ApkListImageAndText("null", "TestApk", "50", "", i % 5, i % 10, false);
					
					switch (posetion) {
					case 1:
						dataArray1.add(test);
						break;
					case 2:
						dataArray2.add(test);
						break;
					case 3:
						dataArray3.add(test);
						break;
					}
				}
				Message msg = new Message();
				msg.what = posetion;
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	class MessageHandler extends Handler {
		public MessageHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			int position = msg.what;
			ApkListImageAndTextListAdapter adapter = adapterList.get(position);
			adapter = new ApkListImageAndTextListAdapter(m_activity, resourceList.get(position), listPage.get(position));
				adapterList.remove(position);
				adapterList.add(position, adapter);
				listPage.get(position).setAdapter(adapterList.get(position));
				
			MyView.message(position);
		}
	}
}
