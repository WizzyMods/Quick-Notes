package com.QuickNotes.Dev;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.*;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.getkeepsafe.taptargetview.*;
import com.google.android.gms.*;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import eightbitlab.com.blurview.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class HelpActivity extends AppCompatActivity {
	
	private HashMap<String, Object> helpMap = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> helpListMap = new ArrayList<>();
	
	private LinearLayout toolbarBg;
	private RecyclerView helpRecycler;
	private ImageView toolbarBackImage;
	private LinearLayout toolbarTextBg;
	private TextView toolbarTitle;
	private TextView toolbarSubtitle;
	
	private Intent intent = new Intent();
	private SharedPreferences Sp;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.help);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		MobileAds.initialize(this);
		
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		toolbarBg = findViewById(R.id.toolbarBg);
		helpRecycler = findViewById(R.id.helpRecycler);
		toolbarBackImage = findViewById(R.id.toolbarBackImage);
		toolbarTextBg = findViewById(R.id.toolbarTextBg);
		toolbarTitle = findViewById(R.id.toolbarTitle);
		toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
		Sp = getSharedPreferences("QuickNotesData", Activity.MODE_PRIVATE);
		
		toolbarBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});
	}
	
	private void initializeLogic() {
		helpMap = new HashMap<>();
		helpMap.put("question", getString(R.string.howToUseTheApp));
		helpMap.put("answer", getString(R.string.quickNotesIsUserFriendlyAndSimpleAppTutorialWillTeachBasicUsageOfTheAppYouCanStartTutorialByPressingButtonBelow));
		helpMap.put("tutorial", "");
		helpListMap.add(helpMap);
		helpRecycler.setAdapter(new HelpRecyclerAdapter(helpListMap));
		helpRecycler.setLayoutManager(new LinearLayoutManager(this));
	}
	
	public class HelpRecyclerAdapter extends RecyclerView.Adapter<HelpRecyclerAdapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public HelpRecyclerAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.helprecycler, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final LinearLayout helpMainBg = _view.findViewById(R.id.helpMainBg);
			final LinearLayout helpTitleBg = _view.findViewById(R.id.helpTitleBg);
			final ScrollView helpAnswerScroll = _view.findViewById(R.id.helpAnswerScroll);
			final TextView titleText = _view.findViewById(R.id.titleText);
			final ImageView helpAnswerImage = _view.findViewById(R.id.helpAnswerImage);
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final TextView helpAnswerText = _view.findViewById(R.id.helpAnswerText);
			final com.google.android.material.button.MaterialButton helpButton = _view.findViewById(R.id.helpButton);
			
			titleText.setText(_data.get((int)_position).get("question").toString());
			helpAnswerText.setText(_data.get((int)_position).get("answer").toString());
			if (_data.get((int)_position).containsKey("tutorial")) {
				helpButton.setVisibility(View.VISIBLE);
				helpButton.setText(getString(R.string.startTutorial));
				helpButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						Sp.edit().putString("reloadData", "tutorial").commit();
						finish();
					}
				});
			}
			helpAnswerImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					if (helpAnswerScroll.getVisibility() == View.GONE) {
						helpAnswerScroll.setVisibility(View.VISIBLE);
						helpAnswerImage.setImageResource(R.drawable.arrow_drop_up_72dp_black);
					} else {
						helpAnswerScroll.setVisibility(View.GONE);
						helpAnswerImage.setImageResource(R.drawable.arrow_drop_down_72dp_black);
					}
				}
			});
		}
		
		@Override
		public int getItemCount() {
			return _data.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
		}
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