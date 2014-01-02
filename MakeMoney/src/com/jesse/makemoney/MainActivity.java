package com.jesse.makemoney;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jesse.fragment.FrameHome;
import com.jesse.slidingMenu.MenuFragment;
import com.jesse.slidingMenu.MenuFragment.SLMenuListOnItemClickListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends SlidingFragmentActivity implements SLMenuListOnItemClickListener{
	private SlidingMenu mSlidingMenu;  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("MakeMoney");
		setContentView(R.layout.activity_main);
		
		setBehindContentView(R.layout.frame_menu);  		// 默认左侧可滑动区域布局
		mSlidingMenu = getSlidingMenu(); 
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);		// 触摸模式
		mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);			// 设置为可左右都滑动
		
		mSlidingMenu.setSecondaryMenu(R.layout.frame_right_menu);		// 右侧可滑动区域
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.drawer_shadow);
		
		mSlidingMenu.setShadowDrawable(R.drawable.drawer_shadow);//设置阴影图片
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);		// 阴影宽度	
		
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);			// 阴影图片
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);		// 划开后的sliding宽度
		mSlidingMenu.setFadeDegree(0.35f);
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.left_menu, new MenuFragment());
		fragmentTransaction.commit();
		
		//使用左上方icon可点，这样在onOptionsItemSelected里面才可以监听到R.id.home
        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setLogo(R.drawable.ic_logo);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            toggle(); //动态判断自动关闭或开启SlidingMenu
//          getSlidingMenu().showMenu();//显示SlidingMenu
//          getSlidingMenu().showContent();//显示内容
            return true;
        case R.id.action_person:
            
        	if(mSlidingMenu.isSecondaryMenuShowing()){
        		mSlidingMenu.showContent();
        	}else{
        		mSlidingMenu.showSecondaryMenu();
        	}
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
        
    }
    
    @Override
	public void selectItem(int position, String title) {  
        // update the main content by replacing fragments    
        Fragment fragment = null;    
        switch (position) {    
        case 1:    
        	fragment = new FrameHome();
            break;    
        }    
        
        if (fragment != null) {    
            FragmentManager fragmentManager = getSupportFragmentManager();  
            fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();    
            // update selected item and title, then close the drawer    
            setTitle(title);  
            mSlidingMenu.showContent();  
        } else {    
            // error in creating fragment    
            Log.e("MainActivity", "Error in creating fragment");    
        }    
    }  
}
 