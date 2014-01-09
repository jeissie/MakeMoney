package com.jesse.customLayout;

import com.jesse.dao.ListItemUnitDao;
import com.jesse.makemoney.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ListViewItemUnit extends LinearLayout implements OnClickListener{

	private ListItemUnitDao itemUnitDao;
	private ImageView apkIcon, button;
	private TextView apkName, moeny;
	private RatingBar star;
	private LinearLayout linearLayout;
	private int level = 0;
	
	public ListViewItemUnit(Context context) {
		super(context);
	}

	public ListViewItemUnit(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listview_item_unit, this);
        apkIcon = (ImageView) findViewById(R.id.apkIcon);
        button = (ImageView) findViewById(R.id.button);
        button.setOnClickListener(this);
        apkName = (TextView)findViewById(R.id.apkName);
        moeny = (TextView)findViewById(R.id.moeny);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayoutListItemUnit);
    }
	
	@SuppressLint("ResourceAsColor")
	public void setDistrictColor(int num) {
		//TODO use userdefine to define the different district color
		linearLayout.setBackgroundColor(R.color.light_gray);
	}

	public void setListener(ListItemUnitDao itemUnitDao) {
		this.itemUnitDao = itemUnitDao;
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			case R.id.button:
				itemUnitDao.buttonListener();
				break;
			case R.id.apkIcon:
				itemUnitDao.iconListener();
				break;
		}
	}
}
