package com.QuickNotes.Dev;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class SettingActivity extends AppCompatActivity {
	
	public final int REQ_CD_FİLEPİCKER = 101;
	
	private Timer _timer = new Timer();
	
	private HashMap<String, Object> exportMap = new HashMap<>();
	private String importPathString = "";
	
	private ArrayList<HashMap<String, Object>> spinnerLangListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> sortSpinnerListMap = new ArrayList<>();
	
	private LinearLayout toolbarBg;
	private ScrollView settingsScroll;
	private ImageView toolbarBackImage;
	private LinearLayout toolbarTextBg;
	private ImageView toolbarResetImage;
	private TextView toolbarTitle;
	private TextView toolbarSubtitle;
	private LinearLayout settingsBg;
	private LinearLayout backupTitleBg;
	private LinearLayout backupBg;
	private LinearLayout themeTitleBg;
	private LinearLayout themeBg;
	private TextView backupTitleText;
	private MaterialButton exportButton;
	private MaterialButton importButton;
	private TextView themeTitleText;
	private Switch disableAnimationsSwitch;
	private Spinner languageSpinner;
	private Spinner sortSpinner;
	private Switch autoSavingSwitch;
	private TextView autoSaveText;
	
	private SharedPreferences Sp;
	private Calendar Calendar1 = Calendar.getInstance();
	private Intent FilePicker = new Intent(Intent.ACTION_GET_CONTENT);
	private Intent intent = new Intent();
	private TimerTask wait;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.setting);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		MobileAds.initialize(this);
		
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		toolbarBg = findViewById(R.id.toolbarBg);
		settingsScroll = findViewById(R.id.settingsScroll);
		toolbarBackImage = findViewById(R.id.toolbarBackImage);
		toolbarTextBg = findViewById(R.id.toolbarTextBg);
		toolbarResetImage = findViewById(R.id.toolbarResetImage);
		toolbarTitle = findViewById(R.id.toolbarTitle);
		toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
		settingsBg = findViewById(R.id.settingsBg);
		backupTitleBg = findViewById(R.id.backupTitleBg);
		backupBg = findViewById(R.id.backupBg);
		themeTitleBg = findViewById(R.id.themeTitleBg);
		themeBg = findViewById(R.id.themeBg);
		backupTitleText = findViewById(R.id.backupTitleText);
		exportButton = findViewById(R.id.exportButton);
		importButton = findViewById(R.id.importButton);
		themeTitleText = findViewById(R.id.themeTitleText);
		disableAnimationsSwitch = findViewById(R.id.disableAnimationsSwitch);
		languageSpinner = findViewById(R.id.languageSpinner);
		sortSpinner = findViewById(R.id.sortSpinner);
		autoSavingSwitch = findViewById(R.id.autoSavingSwitch);
		autoSaveText = findViewById(R.id.autoSaveText);
		Sp = getSharedPreferences("QuickNotesData", Activity.MODE_PRIVATE);
		FilePicker.setType("*/*");
		FilePicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		toolbarBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});
		
		toolbarResetImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).create();
				View inflate = getLayoutInflater().inflate(R.layout.alert_dialog_backup,null); 
				dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				dialog.setView(inflate);
				dialog.setCancelable(true);
				final BlurView dialogBlurBg = (BlurView) inflate.findViewById(R.id.dialogBlurBg);
				if (Build.VERSION.SDK_INT > 30) {
					View decorView = getWindow().getDecorView();
					ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
					Drawable windowBackground = decorView.getBackground();
					float blurBg = ((float)5);
					dialogBlurBg.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
					dialogBlurBg.setClipToOutline(true);
					dialogBlurBg.setupWith(rootView, new RenderEffectBlur())
					.setBlurRadius(blurBg)
					.setFrameClearDrawable(windowBackground) 
					.setBlurAutoUpdate(true);
					
					final Choreographer blurc = Choreographer.getInstance();
					
					final Choreographer.FrameCallback rblurc = new Choreographer.FrameCallback() {
						@Override
						public void doFrame(long frameTimeNanos) {
							dialogBlurBg.invalidate();;
							blurc.postFrameCallback(this); // Sorun burada çözüldü
						}
					};
					
					blurc.postFrameCallback(rblurc); // Başlat
				} else {
					dialogBlurBg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f), getResources().getColor(R.color.colorPrimary)));
				}
				LinearLayout alertDialogBg = (LinearLayout) inflate.findViewById(R.id.alertDialogBg);
				alertDialogBg.setVisibility(View.VISIBLE);
				final TextView titleAlertDialogText = (TextView) inflate.findViewById(R.id.titleAlertDialogText);
				titleAlertDialogText.setText(getString(R.string.deleteAllNotes));
				final TextView infoAlertDialogText = (TextView) inflate.findViewById(R.id.infoAlertDialogText);
				infoAlertDialogText.setText(getString(R.string.areYouSureYouWantToDeleteAllNotesThisActionCannotBeUndoneToConfirmLongClickTheConfirmButton));
				final Button cancelAlertDialogButton = (Button) inflate.findViewById(R.id.cancelAlertDialogButton);
				final Button okAlertDialogButton = (Button) inflate.findViewById(R.id.okAlertDialogButton);
				okAlertDialogButton.setBackgroundTintList(ColorStateList.valueOf(0xFFE53935));
				cancelAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						final Handler wait = new Handler(Looper.getMainLooper());
						final Runnable rwait  = new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();;
							}
						};
						wait.postDelayed(rwait, 50);
					}
				});
				okAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.longClick));
					}
				});
				okAlertDialogButton.setOnLongClickListener(new View.OnLongClickListener(){
					@Override
					public boolean onLongClick(View _view){
						File databases = getDatabasePath("QuickNotes.db");
						FileUtil.deleteFile(databases.getAbsolutePath());
						Sp.edit().remove("firstStart").commit();
						final Handler wait = new Handler(Looper.getMainLooper());
						final Runnable rwait  = new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.allNotesDeletedSuccessfully));
								intent.setClass(getApplicationContext(), MainActivity.class);
								startActivity(intent);
								finishAffinity();;
							}
						};
						wait.postDelayed(rwait, 100);
						return false;
					}
				});
				//dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				dialog.getWindow().setDimAmount(0.2f);
				dialog.show();
			}
		});
		
		exportButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).create();
				View inflate = getLayoutInflater().inflate(R.layout.alert_dialog_backup,null); 
				dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				dialog.setView(inflate);
				dialog.setCancelable(true);
				final BlurView dialogBlurBg = (BlurView) inflate.findViewById(R.id.dialogBlurBg);
				if (Build.VERSION.SDK_INT > 30) {
					View decorView = getWindow().getDecorView();
					ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
					Drawable windowBackground = decorView.getBackground();
					float blurBg = ((float)5);
					dialogBlurBg.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
					dialogBlurBg.setClipToOutline(true);
					dialogBlurBg.setupWith(rootView, new RenderEffectBlur())
					.setBlurRadius(blurBg)
					.setFrameClearDrawable(windowBackground) 
					.setBlurAutoUpdate(true);
					
					final Choreographer blurc = Choreographer.getInstance();
					
					final Choreographer.FrameCallback rblurc = new Choreographer.FrameCallback() {
						@Override
						public void doFrame(long frameTimeNanos) {
							dialogBlurBg.invalidate();;
							blurc.postFrameCallback(this); // Sorun burada çözüldü
						}
					};
					
					blurc.postFrameCallback(rblurc); // Başlat
				} else {
					dialogBlurBg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f), getResources().getColor(R.color.colorPrimary)));
				}
				LinearLayout alertDialogBg = (LinearLayout) inflate.findViewById(R.id.alertDialogBg);
				alertDialogBg.setVisibility(View.VISIBLE);
				final TextView titleAlertDialogText = (TextView) inflate.findViewById(R.id.titleAlertDialogText);
				titleAlertDialogText.setText(getString(R.string.exportBackup));
				final TextView infoAlertDialogText = (TextView) inflate.findViewById(R.id.infoAlertDialogText);
				infoAlertDialogText.setText(getString(R.string.exportYourNotesTo));
				final Button cancelAlertDialogButton = (Button) inflate.findViewById(R.id.cancelAlertDialogButton);
				final Button neutralAlertDialogButton = (Button) inflate.findViewById(R.id.neutralAlertDialogButton);
				final Button okAlertDialogButton = (Button) inflate.findViewById(R.id.okAlertDialogButton);
				cancelAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						final Handler wait = new Handler(Looper.getMainLooper());
						final Runnable rwait  = new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();;
							}
						};
						wait.postDelayed(rwait, 50);
					}
				});
				neutralAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						final Handler wait = new Handler(Looper.getMainLooper());
						final Runnable rwait  = new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();;
							}
						};
						wait.postDelayed(rwait, 50);
					}
				});
				okAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						final Handler wait = new Handler(Looper.getMainLooper());
						final Runnable rwait  = new Runnable() {
							@Override
							public void run() {
								Calendar1 = Calendar.getInstance();
								File databases = getDatabasePath("QuickNotes.db");
								FileUtil.copyFile(databases.getAbsolutePath(), FileUtil.getPublicDir(Environment.DIRECTORY_DOWNLOADS).concat("/QuickNotesBackup".concat("/QuickNotesBackup_".concat(new SimpleDateFormat("HHmm_ddMMyy").format(Calendar1.getTime()).concat(".db")))));
								SketchwareUtil.showMessage(getApplicationContext(), "Download".concat("/QuickNotesBackup".concat("/QuickNotesBackup_".concat(new SimpleDateFormat("HHmm_ddMMyy").format(Calendar1.getTime()).concat(".db")))));
								dialog.dismiss();;
							}
						};
						wait.postDelayed(rwait, 50);
					}
				});
				//dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				dialog.getWindow().setDimAmount(0.2f);
				dialog.show();
			}
		});
		
		importButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				//FilePicker.addCategory(Intent.CATEGORY_OPENABLE);
				FilePicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // TEK SEÇİM
				startActivityForResult(FilePicker, REQ_CD_FİLEPİCKER);
				SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.onlySelectFilesWithThedbExtension));
			}
		});
	}
	
	private void initializeLogic() {
		int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
		if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
			// ACTION WHEN DARK MODE IS ON
			
		} else {
			// ACTION IF DARK MODE IS OFF
			getWindow().getDecorView().setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		};
		
		if (Build.VERSION.SDK_INT > 30) {
			if (Sp.contains("performanceMode")) {
				disableAnimationsSwitch.setChecked(true);
			}
		} else {
			disableAnimationsSwitch.setChecked(true);
			disableAnimationsSwitch.setEnabled(false);
		}
		if (!Sp.contains("autoSave")) {
			autoSavingSwitch.setChecked(false);
			autoSaveText.setVisibility(View.VISIBLE);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", getString(R.string.device));
			spinnerLangListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", "English");
			spinnerLangListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", "Türkçe (Turkish)");
			spinnerLangListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", "العربية (Arabic)");
			spinnerLangListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", "Русский (Russian)");
			spinnerLangListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", "Español (Spanish)");
			spinnerLangListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", "Deutsch (German)");
			spinnerLangListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", "Français (French)");
			spinnerLangListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", "हिंदी (Hindi)");
			spinnerLangListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", "Português (Brazil)");
			spinnerLangListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("language", "简体中文 (Simplified Chinese)");
			spinnerLangListMap.add(_item);
		}
		languageSpinner.setAdapter(new LanguageSpinnerAdapter(spinnerLangListMap));
		if (Sp.contains("language")) {
			switch(Sp.getString("language", "")) {
				case "device": {
					languageSpinner.setSelection((int)(0));
					break;
				}
				case "en": {
					languageSpinner.setSelection((int)(1));
					break;
				}
				case "tr": {
					languageSpinner.setSelection((int)(2));
					break;
				}
				case "ar": {
					languageSpinner.setSelection((int)(3));
					break;
				}
				case "ru": {
					languageSpinner.setSelection((int)(4));
					break;
				}
				case "es": {
					languageSpinner.setSelection((int)(5));
					break;
				}
				case "de": {
					languageSpinner.setSelection((int)(6));
					break;
				}
				case "fr": {
					languageSpinner.setSelection((int)(7));
					break;
				}
				case "hi": {
					languageSpinner.setSelection((int)(8));
					break;
				}
				case "pt": {
					languageSpinner.setSelection((int)(9));
					break;
				}
				case "zh": {
					languageSpinner.setSelection((int)(10));
					break;
				}
			}
		} else {
			Sp.edit().putString("language", "device").commit();
			languageSpinner.setSelection((int)(0));
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("sort", getString(R.string.createDate));
			sortSpinnerListMap.add(_item);
		}
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("sort", getString(R.string.editDate));
			sortSpinnerListMap.add(_item);
		}
		sortSpinner.setAdapter(new SortSpinnerAdapter(sortSpinnerListMap));
		if (Sp.contains("sort")) {
			switch(Sp.getString("sort", "")) {
				case "create": {
					sortSpinner.setSelection((int)(0));
					break;
				}
				case "edit": {
					sortSpinner.setSelection((int)(1));
					break;
				}
			}
		}
		_RippleAndTooltips();
		_SwitchListeners();
		backupBg.setVisibility(View.VISIBLE);
		themeBg.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_FİLEPİCKER:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				importPathString = _filePath.get((int)(0));
				if (importPathString.endsWith(".db")) {
					final AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).create();
					View inflate = getLayoutInflater().inflate(R.layout.alert_dialog_backup,null); 
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setView(inflate);
					dialog.setCancelable(true);
					final BlurView dialogBlurBg = (BlurView) inflate.findViewById(R.id.dialogBlurBg);
					if (Build.VERSION.SDK_INT > 30) {
						View decorView = getWindow().getDecorView();
						ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
						Drawable windowBackground = decorView.getBackground();
						float blurBg = ((float)5);
						dialogBlurBg.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
						dialogBlurBg.setClipToOutline(true);
						dialogBlurBg.setupWith(rootView, new RenderEffectBlur())
						.setBlurRadius(blurBg)
						.setFrameClearDrawable(windowBackground) 
						.setBlurAutoUpdate(true);
						
						final Choreographer blurc = Choreographer.getInstance();
						
						final Choreographer.FrameCallback rblurc = new Choreographer.FrameCallback() {
							@Override
							public void doFrame(long frameTimeNanos) {
								dialogBlurBg.invalidate();;
								blurc.postFrameCallback(this); // Sorun burada çözüldü
							}
						};
						
						blurc.postFrameCallback(rblurc); // Başlat
					} else {
						dialogBlurBg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f), getResources().getColor(R.color.colorPrimary)));
					}
					LinearLayout alertDialogBg = (LinearLayout) inflate.findViewById(R.id.alertDialogBg);
					alertDialogBg.setVisibility(View.VISIBLE);
					final TextView titleAlertDialogText = (TextView) inflate.findViewById(R.id.titleAlertDialogText);
					titleAlertDialogText.setText(getString(R.string.importBackup));
					final TextView infoAlertDialogText = (TextView) inflate.findViewById(R.id.infoAlertDialogText);
					infoAlertDialogText.setText(getString(R.string.doYouReallyWantToImportThisBackup).concat("\n".concat(Uri.parse(importPathString).getLastPathSegment())));
					final Button cancelAlertDialogButton = (Button) inflate.findViewById(R.id.cancelAlertDialogButton);
					final Button neutralAlertDialogButton = (Button) inflate.findViewById(R.id.neutralAlertDialogButton);
					neutralAlertDialogButton.setVisibility(View.INVISIBLE);
					final Button okAlertDialogButton = (Button) inflate.findViewById(R.id.okAlertDialogButton);
					okAlertDialogButton.setText(getString(R.string.Import));
					cancelAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
							final Handler wait = new Handler(Looper.getMainLooper());
							final Runnable rwait  = new Runnable() {
								@Override
								public void run() {
									dialog.dismiss();;
								}
							};
							wait.postDelayed(rwait, 50);
						}
					});
					okAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
							final Handler wait = new Handler(Looper.getMainLooper());
							final Runnable rwait  = new Runnable() {
								@Override
								public void run() {
									File databases = getDatabasePath("QuickNotes.db");
									FileUtil.copyFile(importPathString, databases.getAbsolutePath());
									Sp.edit().putString("reloadData", "imported").commit();
									SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.successfullyImported));;
								}
							};
							wait.postDelayed(rwait, 50);
							dialog.dismiss();
						}
					});
					//dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
					dialog.getWindow().setDimAmount(0.2f);
					dialog.show();
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.incompatibleFileOnlyUsedbFiles));
				}
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		finish(); overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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
			android.graphics.drawable.GradientDrawable toolbarResetImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarResetImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarResetImageBG.setCornerRadius((float)50);
			toolbarResetImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarResetImage.setBackground(toolbarResetImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarResetImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarResetImageMask.setCornerRadius((float)50);
			toolbarResetImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarResetImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarResetImageMask // mask
			);
			
			toolbarResetImage.setForeground(toolbarResetImageRE);
			toolbarResetImage.setClickable(true);
			toolbarResetImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarResetImageGG = new android.graphics.drawable.GradientDrawable();
			toolbarResetImageGG.setColor(Color.TRANSPARENT);
			toolbarResetImageGG.setCornerRadius((float)50);
			toolbarResetImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarResetImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarResetImageGG,
			null
			);
			toolbarResetImage.setBackground(toolbarResetImageRE);
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarBackImage.setTooltipText(getString(R.string.goBack));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarResetImage.setTooltipText(getString(R.string.deleteAllNotes));
		}
	}
	
	
	public void _SwitchListeners() {
		disableAnimationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean disableAnimationsSwitchnnbbm = _param2;
				if (disableAnimationsSwitch.isChecked()) {
					Sp.edit().putString("performanceMode", "").commit();
				} else {
					Sp.edit().remove("performanceMode").commit();
				}
				final AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).create();
				View inflate = getLayoutInflater().inflate(R.layout.alert_dialog_backup,null); 
				dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				dialog.setView(inflate);
				dialog.setCancelable(true);
				final BlurView dialogBlurBg = (BlurView) inflate.findViewById(R.id.dialogBlurBg);
				if (Build.VERSION.SDK_INT > 30) {
					View decorView = getWindow().getDecorView();
					ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
					Drawable windowBackground = decorView.getBackground();
					float blurBg = ((float)5);
					dialogBlurBg.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
					dialogBlurBg.setClipToOutline(true);
					dialogBlurBg.setupWith(rootView, new RenderEffectBlur())
					.setBlurRadius(blurBg)
					.setFrameClearDrawable(windowBackground) 
					.setBlurAutoUpdate(true);
					
					final Choreographer blurc = Choreographer.getInstance();
					
					final Choreographer.FrameCallback rblurc = new Choreographer.FrameCallback() {
						@Override
						public void doFrame(long frameTimeNanos) {
							dialogBlurBg.invalidate();;
							blurc.postFrameCallback(this); // Sorun burada çözüldü
						}
					};
					
					blurc.postFrameCallback(rblurc); // Başlat
				} else {
					dialogBlurBg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f), getResources().getColor(R.color.colorPrimary)));
				}
				LinearLayout alertDialogBg = (LinearLayout) inflate.findViewById(R.id.alertDialogBg);
				alertDialogBg.setVisibility(View.VISIBLE);
				final TextView titleAlertDialogText = (TextView) inflate.findViewById(R.id.titleAlertDialogText);
				titleAlertDialogText.setText(getString(R.string.caution));
				final TextView infoAlertDialogText = (TextView) inflate.findViewById(R.id.infoAlertDialogText);
				infoAlertDialogText.setText(getString(R.string.quickNotesMustBeRestartedForTheChangesToTakeEffect));
				final Button cancelAlertDialogButton = (Button) inflate.findViewById(R.id.cancelAlertDialogButton);
				final Button neutralAlertDialogButton = (Button) inflate.findViewById(R.id.neutralAlertDialogButton);
				final Button okAlertDialogButton = (Button) inflate.findViewById(R.id.okAlertDialogButton);
				cancelAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						final Handler wait = new Handler(Looper.getMainLooper());
						final Runnable rwait  = new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();;
							}
						};
						wait.postDelayed(rwait, 50);
					}
				});
				neutralAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						final Handler wait = new Handler(Looper.getMainLooper());
						final Runnable rwait  = new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();;
							}
						};
						wait.postDelayed(rwait, 50);
					}
				});
				okAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						final Handler wait = new Handler(Looper.getMainLooper());
						final Runnable rwait  = new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								intent.setClass(getApplicationContext(), MainActivity.class);
								startActivity(intent);
								finishAffinity();;
							}
						};
						wait.postDelayed(rwait, 50);
					}
				});
				//dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				dialog.getWindow().setDimAmount(0.2f);
				dialog.show();
			}
		});
		autoSavingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean autoSavingSwitchnnbbm = _param2;
				if (autoSavingSwitch.isChecked()) {
					Sp.edit().putString("autoSave", "").commit();
					autoSaveText.setVisibility(View.GONE);
				} else {
					Sp.edit().remove("autoSave").commit();
					autoSaveText.setVisibility(View.VISIBLE);
				}
			}
		});
		wait = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
								final int languageSpinnernnbb = _param3;
								switch((int)languageSpinner.getSelectedItemPosition()) {
									case ((int)0): {
										Sp.edit().putString("language", "device").commit();
										break;
									}
									case ((int)1): {
										Sp.edit().putString("language", "en").commit();
										break;
									}
									case ((int)2): {
										Sp.edit().putString("language", "tr").commit();
										break;
									}
									case ((int)3): {
										Sp.edit().putString("language", "ar").commit();
										break;
									}
									case ((int)4): {
										Sp.edit().putString("language", "ru").commit();
										break;
									}
									case ((int)5): {
										Sp.edit().putString("language", "es").commit();
										break;
									}
									case ((int)6): {
										Sp.edit().putString("language", "de").commit();
										break;
									}
									case ((int)7): {
										Sp.edit().putString("language", "fr").commit();
										break;
									}
									case ((int)8): {
										Sp.edit().putString("language", "hi").commit();
										break;
									}
									case ((int)9): {
										Sp.edit().putString("language", "pt").commit();
										break;
									}
									case ((int)10): {
										Sp.edit().putString("language", "zh").commit();
										break;
									}
								}
								intent.setClass(getApplicationContext(), MainActivity.class);
								startActivity(intent);
								finishAffinity();
							}
							
							@Override
							public void onNothingSelected(AdapterView<?> _param1) {
								
							}
						});
						sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
								final int sortSpinnernnbb = _param3;
								switch((int)sortSpinner.getSelectedItemPosition()) {
									case ((int)0): {
										Sp.edit().putString("sort", "create").commit();
										break;
									}
									case ((int)1): {
										Sp.edit().putString("sort", "edit").commit();
										break;
									}
								}
								intent.setClass(getApplicationContext(), MainActivity.class);
								startActivity(intent);
								finishAffinity();
							}
							
							@Override
							public void onNothingSelected(AdapterView<?> _param1) {
								
							}
						});
					}
				});
			}
		};
		_timer.schedule(wait, (int)(300));
	}
	
	public class LanguageSpinnerAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public LanguageSpinnerAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.spinner, null);
			}
			
			final LinearLayout spinnerBg = _view.findViewById(R.id.spinnerBg);
			final TextView spinnerText = _view.findViewById(R.id.spinnerText);
			
			if (_data.get((int)_position).containsKey("language")) {
				spinnerText.setText(_data.get((int)_position).get("language").toString());
				spinnerBg.setVisibility(View.VISIBLE);
			} else {
				spinnerBg.setVisibility(View.GONE);
			}
			
			return _view;
		}
	}
	
	public class SortSpinnerAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public SortSpinnerAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.spinner, null);
			}
			
			final LinearLayout spinnerBg = _view.findViewById(R.id.spinnerBg);
			final TextView spinnerText = _view.findViewById(R.id.spinnerText);
			
			if (_data.get((int)_position).containsKey("sort")) {
				spinnerText.setText(_data.get((int)_position).get("sort").toString());
			}
			
			return _view;
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