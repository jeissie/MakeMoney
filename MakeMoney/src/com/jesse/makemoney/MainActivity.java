package com.jesse.makemoney;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jesse.dao.SLMenuListOnItemClickListenerDao;
import com.jesse.fragment.FrameDistrict;
import com.jesse.fragment.FrameHome;
import com.jesse.fragment.FrameItems;
import com.jesse.slidingMenu.MenuFragment;
import com.jesse.slidingMenu.RightMenuFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity implements SLMenuListOnItemClickListenerDao , OnClickListener{
	private SlidingMenu mSlidingMenu;  
	private ImageButton framMenuButton;
	private ImageButton userInfoButton;
	private TextView titleName;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("MakeMoney");
		setContentView(R.layout.activity_main);
		//----------------------------------
		//Title
		//----------------------------------
		framMenuButton = (ImageButton) findViewById(R.id.frame_menu);
		userInfoButton = (ImageButton) findViewById(R.id.user_info);
		titleName = (TextView) findViewById(R.id.title);
		framMenuButton.setOnClickListener(this);
		userInfoButton.setOnClickListener(this);
		
		//----------------------------------
		//SlidingMenu	
		//----------------------------------
		setBehindContentView(R.layout.frame_menu);  		// 榛樿宸︿晶鍙粦鍔ㄥ尯鍩熷竷灞�
		mSlidingMenu = getSlidingMenu(); 
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);		// 瑙︽懜妯″紡
		mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);			// 璁剧疆涓哄彲宸﹀彸閮芥粦鍔�
		
		mSlidingMenu.setSecondaryMenu(R.layout.frame_right_menu_init);		// 鍙充晶鍙粦鍔ㄥ尯鍩�
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.drawer_shadow);
		
		mSlidingMenu.setShadowDrawable(R.drawable.drawer_shadow);//璁剧疆闃村奖鍥剧墖
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);		// 闃村奖瀹藉害	
		
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);			// 闃村奖鍥剧墖
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);		// 鍒掑紑鍚庣殑sliding瀹藉害
		mSlidingMenu.setFadeDegree(0.35f);
		
		//----------------------------------
		//FragmentControler	
		//----------------------------------
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.left_menu, new MenuFragment());
		fragmentTransaction.replace(R.id.right_menu_initid, new RightMenuFragment());
		fragmentTransaction.commit();

		selectItem(1, "MakeMoney");
		
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.frame_menu:
			toggle(); //鍔ㄦ�鍒ゆ柇鑷姩鍏抽棴鎴栧紑鍚疭lidingMenu
//          getSlidingMenu().showMenu();//鏄剧ずSlidingMenu
//          getSlidingMenu().showContent();//鏄剧ず鍐呭
			break;
			
		case R.id.user_info:
			if(mSlidingMenu.isSecondaryMenuShowing()){
        		mSlidingMenu.showContent();
        	}else{
        		mSlidingMenu.showSecondaryMenu();
        	}
			break;
		}
		
	}  
    
    @Override
	public void selectItem(int position, String title) {  
        Fragment fragment = null;    
        switch (position) {    
	        case 1:    
	        	fragment = new FrameHome();
	            break;  
	        case 2:
	        	fragment = new FrameDistrict();
	        	break;
	        case 3:
	        	fragment = new FrameItems();
	        	break;
        }    
        
        if (fragment != null) {    
            FragmentManager fragmentManager = getSupportFragmentManager();  
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);  
            fragmentTransaction.commit();    
            titleName.setText(title);
            mSlidingMenu.showContent();  
        } else {    
            // error in creating fragment    
            Log.e("MainActivity", "Error in creating fragment");    
        }    
    }
}
 