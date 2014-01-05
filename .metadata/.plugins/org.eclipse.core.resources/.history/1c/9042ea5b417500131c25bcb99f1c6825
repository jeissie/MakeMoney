package com.gallery;

import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.jesse.mxapk.R;

import android.content.Context;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
/**
 * @author jie
 *
 */
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
			//获取子布局界面
			view = inflater.inflate(R.layout.gallery_image, null);
			AptechObject object = objects.get(position % objects.size());
			//获取子布局界面控件
			ImageView objphoto = (ImageView) view.findViewById(R.id.gallery_image);
			//为各控件赋值
			objphoto.setImageBitmap(object.getObjphoto());
		}
		return view;
	}

}
