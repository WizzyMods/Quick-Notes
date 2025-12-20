package com.QuickNotes.Dev;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.*;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.getkeepsafe.taptargetview.*;
import com.google.android.gms.*;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.*;
import com.google.firebase.FirebaseApp;
import eightbitlab.com.blurview.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class CrashActivity extends AppCompatActivity {
	
	private LinearLayout mainBg;
	private TextView titleText;
	private TextView logText;
	private LinearLayout buttonsBg;
	private MaterialButton showLogBg;
	private MaterialButton closeBg;
	private MaterialButton restartBg;
	
	private Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.crash);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		MobileAds.initialize(this);
		
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		mainBg = findViewById(R.id.mainBg);
		titleText = findViewById(R.id.titleText);
		logText = findViewById(R.id.logText);
		buttonsBg = findViewById(R.id.buttonsBg);
		showLogBg = findViewById(R.id.showLogBg);
		closeBg = findViewById(R.id.closeBg);
		restartBg = findViewById(R.id.restartBg);
		
		showLogBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				logText.setVisibility(View.VISIBLE);
				showLogBg.setEnabled(false);
			}
		});
		
		closeBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finishAffinity();
			}
		});
		
		restartBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finishAffinity();
			}
		});
	}
	
	private void initializeLogic() {
		if (getIntent().hasExtra("error")) {
			logText.setText(getIntent().getStringExtra("error"));
		}
	}
	
	@Override
	public void onBackPressed() {
		finishAffinity();
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}