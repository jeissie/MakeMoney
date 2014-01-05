/**
 * 
 */
package com.report;

import com.jesse.mxapk.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author jie
 * 
 */
public class Report extends Activity {

	private Button cancel,send;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		
		cancel = (Button) findViewById(R.id.cancel);
		send = (Button) findViewById(R.id.send);
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();	
			}
		});
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(Report.this, "ÒÑ·¢ËÍ", Toast.LENGTH_SHORT).show();
				onBackPressed();
			}
		});
	}
}
