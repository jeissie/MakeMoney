package com.jesse.gallery;

import java.util.List;

import com.jesse.makemoney.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PageAdapter extends BaseAdapter{

	private Context context;
	private List<AptechObject> objects;
	private LayoutInflater inflater;
	
	public PageAdapter(Context context , List<AptechObject> objects){
		this.context = context;
		this.objects = objects;
		inflater = LayoutInflater.from(context);
	}
	
	public int getCount() {

		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {
		return objects.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		if(view == null){
			view = inflater.inflate(R.layout.gallery_image, null);
			AptechObject object = objects.get(position % objects.size());
			ImageView objphoto = (ImageView) view.findViewById(R.id.gallery_image);
			objphoto.setImageBitmap(object.getObjphoto());
		}
		return view;
	}

}
