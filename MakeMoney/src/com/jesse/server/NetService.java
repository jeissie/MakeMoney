package com.jesse.server;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.jesse.makemoney.R;
import com.jesse.model.GetImageOnMain;
import com.jesse.model.UserData;
import com.jesse.util.ContextUtil;

/**
 * @author jesse
 */
public class NetService {
	
	public static void requireService(Context context, String requireDo) {
		//TODO download require then use callBack
		
		//TODO This is test. Then you should get the source from net. Use the source length to make the object!
		for (int i = 0; i < 5; i++) {
			GetImageOnMain.getInstance().initBitMapGroup(BitmapFactory.decodeResource(context.getResources(), R.drawable.bitmap1), "TestImage_" + i, "" + i);
		}
	}
}
