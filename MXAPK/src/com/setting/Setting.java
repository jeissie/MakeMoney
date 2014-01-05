/**
 * 
 */
package com.setting;

import com.apkmanager.ApkManager;
import com.jesse.mxapk.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jie
 *
 */
public class Setting extends Activity {
	
	private TextView apk_manager;
	private ImageView wifi;
	SharedPreferences  user_preferences ;
	SharedPreferences.Editor editor ;
 	private boolean wifi_state ;
 	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		InitText();  //��ʼ������
		Wifi_State ();  //����wifi״̬
		
	}
	
	/*****��ʼ������*******/
	private void InitText(){
		apk_manager = (TextView) findViewById(R.id.apk_manager);
		
		apk_manager.setOnClickListener(buttonListener);
	}
	
	View.OnClickListener buttonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.apk_manager:
				Intent intent = new Intent(Setting.this,ApkManager.class);
				startActivity(intent);
			 break;
			}
		}
	};
	
	/******wifi״̬*******/
	public void Wifi_State (){
		user_preferences = getSharedPreferences("user_LOGO",MODE_PRIVATE); //�û����ü�¼
		editor = user_preferences.edit();
		wifi_state = user_preferences.getBoolean("only_WIFI", false);
		wifi = (ImageView) findViewById(R.id.wifi_set);
		
     	if(wifi_state == true){
     		wifi.setImageResource(R.drawable.on);
     	}else{
     		wifi.setImageResource(R.drawable.off);
     	}
     	
     	wifi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				wifi_state = !wifi_state;
				
				if(wifi_state == true){
					wifi.setImageResource(R.drawable.on);//��wifi����
					editor.putBoolean("only_WIFI",true);
					editor.commit();
				}else{
					wifi.setImageResource(R.drawable.off);//�ر�wifi����
					editor.putBoolean("only_WIFI",false);
					editor.commit();
				}
					
			}
		});
	}
}
