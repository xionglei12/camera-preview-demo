package com.fcbox.camerademo;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @创建者 000778
 * @创建时间 2018/10/12
 * @描述
 */

public class MyFragment extends Fragment {


	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.activity_main,container);
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}
}
