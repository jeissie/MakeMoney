package com.jesse.customLayout;

import com.jesse.makemoney.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RightMenuFragmentItem extends LinearLayout {

	private ImageView imageView;
	private TextView textView;
	private LinearLayout linearLayout;
	
	public RightMenuFragmentItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public RightMenuFragmentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.frame_right_menu_item, this);
        imageView = (ImageView) findViewById(R.id.image);
        textView = (TextView)findViewById(R.id.text);
        linearLayout = (LinearLayout) findViewById(R.id.frame_menu_background);
    }

	public void setImage(int resId) {
		imageView.setImageResource(resId);
	}
	
	public void setText(String text) {
		textView.setText(text);
	}
	
}
