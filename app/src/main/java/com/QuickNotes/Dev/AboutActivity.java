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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class AboutActivity extends AppCompatActivity {
	
	private HashMap<String, Object> licenseMap = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> licensesListMap = new ArrayList<>();
	private ArrayList<String> licensesListString = new ArrayList<>();
	
	private LinearLayout toolbarBg;
	private LinearLayout topBg;
	private LinearLayout licensesBg;
	private ImageView toolbarBackImage;
	private LinearLayout toolbarTextBg;
	private ImageView toolbarChannelImage;
	private ImageView toolbarShareAppImage;
	private TextView toolbarTitle;
	private TextView toolbarSubtitle;
	private TextView appNameText;
	private TextView wizzyAppsText;
	private TextView versionText;
	private LinearLayout licensesUpBg;
	private ListView licensesList;
	private MaterialButton privacyPolicyButton;
	private TextView licensesText;
	private ImageView licensesImage;
	
	private Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.about);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		MobileAds.initialize(this);
		
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		toolbarBg = findViewById(R.id.toolbarBg);
		topBg = findViewById(R.id.topBg);
		licensesBg = findViewById(R.id.licensesBg);
		toolbarBackImage = findViewById(R.id.toolbarBackImage);
		toolbarTextBg = findViewById(R.id.toolbarTextBg);
		toolbarChannelImage = findViewById(R.id.toolbarChannelImage);
		toolbarShareAppImage = findViewById(R.id.toolbarShareAppImage);
		toolbarTitle = findViewById(R.id.toolbarTitle);
		toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
		appNameText = findViewById(R.id.appNameText);
		wizzyAppsText = findViewById(R.id.wizzyAppsText);
		versionText = findViewById(R.id.versionText);
		licensesUpBg = findViewById(R.id.licensesUpBg);
		licensesList = findViewById(R.id.licensesList);
		privacyPolicyButton = findViewById(R.id.privacyPolicyButton);
		licensesText = findViewById(R.id.licensesText);
		licensesImage = findViewById(R.id.licensesImage);
		
		toolbarBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});
		
		toolbarChannelImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("https://t.me/WizzyTRsApps"));
				startActivity(intent);
			}
		});
		
		toolbarShareAppImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent i = new Intent(android.content.Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.QuickNotes.Dev"); startActivity(Intent.createChooser(i,"Quick Notes"));
			}
		});
		
		licensesUpBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (licensesList.getVisibility() == View.GONE) {
					licensesList.setVisibility(View.VISIBLE);
					licensesImage.setVisibility(View.GONE);
					licensesImage.setImageResource(R.drawable.arrow_drop_up_72dp_black);
					licensesImage.setVisibility(View.VISIBLE);
				} else {
					licensesList.setVisibility(View.GONE);
					licensesImage.setVisibility(View.GONE);
					licensesImage.setImageResource(R.drawable.arrow_drop_down_72dp_black);
					licensesImage.setVisibility(View.VISIBLE);
				}
			}
		});
		
		privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), PrivacypolicyActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
	private void initializeLogic() {
		try{
			versionText.setText(getString(R.string.version).concat(" ".concat(getPackageManager().getPackageInfo(getPackageName(), 0).versionName.toString())));
		}catch(Exception e){
			versionText.setVisibility(View.GONE);
		}
		licensesListString.add("Gson (Apache 2.0) ");
		licensesListString.add("TapTargetView (Apache 2.0)");
		licensesListString.add("BlurView (Apache 2.0)");
		licensesListString.add("Material Components (Apache 2.0)");
		licensesListString.add("Lottie (Apache 2.0)");
		licensesList.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, licensesListString));
		((BaseAdapter)licensesList.getAdapter()).notifyDataSetChanged();
		wizzyAppsText.setText("©".concat(getString(R.string.w2025WizzyAppsAllRightsReserved)));
		_RippleAndTooltips();
	}
	
	public void _RippleAndTooltips() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable toolbarBackImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarBackImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarBackImageBG.setCornerRadius((float)50);
			toolbarBackImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarBackImage.setBackground(toolbarBackImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarBackImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarBackImageMask.setCornerRadius((float)50);
			toolbarBackImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarBackImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarBackImageMask // mask
			);
			
			toolbarBackImage.setForeground(toolbarBackImageRE);
			toolbarBackImage.setClickable(true);
			toolbarBackImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarBackImageGG = new android.graphics.drawable.GradientDrawable();
			toolbarBackImageGG.setColor(Color.TRANSPARENT);
			toolbarBackImageGG.setCornerRadius((float)50);
			toolbarBackImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarBackImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarBackImageGG,
			null
			);
			toolbarBackImage.setBackground(toolbarBackImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable toolbarShareAppImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarShareAppImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarShareAppImageBG.setCornerRadius((float)50);
			toolbarShareAppImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarShareAppImage.setBackground(toolbarShareAppImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarShareAppImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarShareAppImageMask.setCornerRadius((float)50);
			toolbarShareAppImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarShareAppImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarShareAppImageMask // mask
			);
			
			toolbarShareAppImage.setForeground(toolbarShareAppImageRE);
			toolbarShareAppImage.setClickable(true);
			toolbarShareAppImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarShareAppImageGG = new android.graphics.drawable.GradientDrawable();
			toolbarShareAppImageGG.setColor(Color.TRANSPARENT);
			toolbarShareAppImageGG.setCornerRadius((float)50);
			toolbarShareAppImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarShareAppImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarShareAppImageGG,
			null
			);
			toolbarShareAppImage.setBackground(toolbarShareAppImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable toolbarChannelImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarChannelImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarChannelImageBG.setCornerRadius((float)50);
			toolbarChannelImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarChannelImage.setBackground(toolbarChannelImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarChannelImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarChannelImageMask.setCornerRadius((float)50);
			toolbarChannelImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarChannelImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarChannelImageMask // mask
			);
			
			toolbarChannelImage.setForeground(toolbarChannelImageRE);
			toolbarChannelImage.setClickable(true);
			toolbarChannelImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarChannelImageGG = new android.graphics.drawable.GradientDrawable();
			toolbarChannelImageGG.setColor(Color.TRANSPARENT);
			toolbarChannelImageGG.setCornerRadius((float)50);
			toolbarChannelImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarChannelImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarChannelImageGG,
			null
			);
			toolbarChannelImage.setBackground(toolbarChannelImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable licensesUpBgBG = new android.graphics.drawable.GradientDrawable();
			licensesUpBgBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			licensesUpBgBG.setCornerRadius((float)50);
			licensesUpBgBG.setStroke((int)0, Color.TRANSPARENT);
			licensesUpBg.setBackground(licensesUpBgBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable licensesUpBgMask = new android.graphics.drawable.GradientDrawable();
			licensesUpBgMask.setCornerRadius((float)50);
			licensesUpBgMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable licensesUpBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			licensesUpBgMask // mask
			);
			
			licensesUpBg.setForeground(licensesUpBgRE);
			licensesUpBg.setClickable(true);
			licensesUpBg.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable licensesUpBgGG = new android.graphics.drawable.GradientDrawable();
			licensesUpBgGG.setColor(Color.TRANSPARENT);
			licensesUpBgGG.setCornerRadius((float)50);
			licensesUpBgGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable licensesUpBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			licensesUpBgGG,
			null
			);
			licensesUpBg.setBackground(licensesUpBgRE);
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarBackImage.setTooltipText(getString(R.string.goBack));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarShareAppImage.setTooltipText(getString(R.string.shareApp));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarChannelImage.setTooltipText(getString(R.string.joinOurChannel));
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