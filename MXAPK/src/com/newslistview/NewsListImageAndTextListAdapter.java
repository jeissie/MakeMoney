package com.newslistview;

import java.util.List;

import com.jesse.mxapk.R;
import com.newslistview.AsyncImageLoader_news.ImageCallback;



import android.app.Activity;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author jie
 *
 */
public class NewsListImageAndTextListAdapter extends
		ArrayAdapter<NewsListImageAndText> {

	private ListView listView;
	private AsyncImageLoader_news asyncImageLoader;
	public NewsListImageAndTextListAdapter(Activity activity,
			List<NewsListImageAndText> imageAndTexts, ListView listView) {
		super(activity, 0, imageAndTexts);
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader_news();
	}
	
     public View getView(final int position, View convertView, ViewGroup parent) {
		 Activity activity = (Activity) getContext();
		 ImageView imageView = null;
		 
		 // 填充行listview
		 View rowView = convertView;
		 NewsListViewCache viewCache;
		 if (rowView == null) {
		             LayoutInflater inflater = activity.getLayoutInflater();
		               rowView = inflater.inflate(R.layout.news_content, null);
		                viewCache = new NewsListViewCache(rowView);
			                rowView.setTag(viewCache);
		           } else {
		              viewCache = (NewsListViewCache) rowView.getTag();
		           }
		 NewsListImageAndText imageAndText = getItem(position);
		 
		 		 
		 //读取图片信息并填充
		 String imageUrl = imageAndText.getImageUrl();
		 Log.v("newsimageurl", imageUrl);
		 if(!imageUrl.equals("empty"))
		 {
			 imageView = viewCache.getImageView();
			 imageView.setTag(imageUrl);
			 Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() 
			 {
				 public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					 ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
					 if (imageViewByTag != null) {
						 imageViewByTag.setImageDrawable(imageDrawable);
					 }
				 }
			 });
			 
			 if (cachedImage == null) {
				 imageView.setImageResource(R.drawable.icon);
			 }else{
				 imageView.setImageDrawable(cachedImage);
			 }
		 }
		 else {
			 imageView = viewCache.getImageView();
			 imageView.setImageResource(R.drawable.icon);//此处设置无图片
		}
		 
		 
		 
		 TextView news_title = viewCache.getNews_title();
		 news_title.setText(imageAndText.getNews_title());

		 TextView time = viewCache.getTime();
		 time.setText(imageAndText.getTime());

		 TextView glance = viewCache.getGlance();
		 glance.setText(imageAndText.getGlance());
		 
		 TextView comment = viewCache.getComment();
		 comment.setText(imageAndText.getComment());

		 return rowView;
     }
}
