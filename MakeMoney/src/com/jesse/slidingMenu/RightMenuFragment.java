package com.jesse.slidingMenu;

import com.jesse.makemoney.R;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RightMenuFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// 棣栨缁樺埗鐢ㄦ埛鐣岄潰鐨勫洖璋冩柟娉曪紝蹇呴』杩斿洖瑕佸垱寤虹殑Fragments 瑙嗗浘UI
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frame_right_menu, null);
		// setViewResource(rootView);
		return rootView;
	}

	// 褰揂ctivity涓殑onCreate鏂规硶鎵ц瀹屽悗璋冪敤銆�
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	// activity涓巉ragment涔嬮棿鐨勯�淇�
	@Override
	public void onAttach(Activity activity) {
		try {
			// mCallback = (SLMenuListOnItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}

}