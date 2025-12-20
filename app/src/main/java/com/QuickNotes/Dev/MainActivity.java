package com.QuickNotes.Dev;

import android.Manifest;
import android.animation.*;
import android.animation.ObjectAnimator;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.splashscreen.*;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.airbnb.lottie.*;
import com.getkeepsafe.taptargetview.*;
import com.google.android.gms.*;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import eightbitlab.com.blurview.*;
import eightbitlab.com.blurview.BlurView;
import java.io.*;
import java.io.File;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.ads.FullScreenContentCallback;

public class MainActivity extends AppCompatActivity {
	
	public final int REQ_CD_CAMERA = 101;
	public final int REQ_CD_FİLEPİCKER = 102;
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private String _reward_ad_unit_id;
	
	private MaterialToolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private FloatingActionButton _fab;
	private DrawerLayout _drawer;
	private boolean wait = false;
	private boolean sortfavortites = false;
	private boolean waitBeforeTouch = false;
	private double pos = 0;
	private double listLength = 0;
	private double listNumber = 0;
	private boolean allCategory = false;
	private String currentCategory = "";
	private HashMap<String, Object> noteMap = new HashMap<>();
	private HashMap<String, Object> noteValues = new HashMap<>();
	private boolean multiSelectMode = false;
	private double multiNumber = 0;
	private HashMap<String, Object> categoryMap = new HashMap<>();
	public MainRecyclerAdapter adapter;
	private boolean keepSplash = false;
	private boolean oncreate = false;
	private String language = "";
	private boolean selectionMode = false;
	
	private ArrayList<HashMap<String, Object>> trashNotesList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> notesListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> searchNotesListMap = new ArrayList<>();
	private ArrayList<String> categoryNames = new ArrayList<>();
	private ArrayList<Double> selectedItems = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> firebaseListMap = new ArrayList<>();
	
	private LinearLayout mainBg;
	private RelativeLayout mainRelativeBg;
	private AdView banner1;
	private NestedScrollView mainVscroll;
	private BlurView toolbarBlur;
	private LinearLayout mainBigBg;
	private LinearLayout welcomeBg;
	private LinearLayout categoryMainBg;
	private TextView categoryText;
	private LinearLayout divider;
	private RecyclerView mainRecycler;
	private LottieAnimationView emptyLottie;
	private TextView welcomeText;
	private TextView dailyText;
	private HorizontalScrollView categoryScrollBg;
	private BlurView editCategoryBlurBg;
	private LinearLayout categoryBg;
	private MaterialButton categoryAllButton;
	private MaterialButton category1Button;
	private MaterialButton category2Button;
	private MaterialButton category3Button;
	private MaterialButton category4Button;
	private MaterialButton category5Button;
	private MaterialButton category6Button;
	private MaterialButton category7Button;
	private MaterialButton category8Button;
	private ImageView editCategoryNamesImage;
	private LinearLayout toolbarBigBg;
	private LinearLayout toolbarBg;
	private LinearLayout searchBg;
	private LinearLayout optionsBg;
	private ImageView toolbarDrawerImage;
	private LinearLayout toolbarTextBg;
	private ImageView layoutImage;
	private ImageView showFavoritesImage;
	private ImageView showDateAndTimeImage;
	private ImageView toolbarSearchImage;
	private TextView toolbarTitle;
	private TextView toolbarSubtitle;
	private EditText searchedittext;
	private ImageView optionsCancel;
	private TextView optionsText;
	private ImageView optionsDelete;
	private ImageView optionsArchive;
	private ImageView optionsShare;
	private BlurView _drawer_blurViewBg;
	private LinearLayout _drawer_mainBg;
	private LinearLayout _drawer_appTitleBackBg;
	private NestedScrollView _drawer_vscrollBg;
	private LinearLayout _drawer_appTitleBg;
	private TextView _drawer_appTitleText;
	private LinearLayout _drawer_buttonsBg;
	private LinearLayout _drawer_ocrBg;
	private LinearLayout _drawer_trashBg;
	private LinearLayout _drawer_archiveBg;
	private LinearLayout _drawer_calendarBg;
	private LinearLayout _drawer_settingsBg;
	private LinearLayout _drawer_helpBg;
	private LinearLayout _drawer_aboutBg;
	private LinearLayout _drawer_versionBg;
	private ImageView _drawer_ocrImage;
	private LinearLayout _drawer_ocrTextBg;
	private TextView _drawer_ocrText;
	private LinearLayout _drawer_ocrOptionsBg;
	private TextView _drawer_ocrGalleryText;
	private TextView _drawer_ocrCameraText;
	private ImageView _drawer_trashImage;
	private TextView _drawer_trashText;
	private ImageView _drawer_archiveImage;
	private TextView _drawer_archiveText;
	private ImageView _drawer_calendarImage;
	private TextView _drawer_calendarText;
	private ImageView _drawer_settingsImage;
	private TextView _drawer_settingsText;
	private ImageView _drawer_helpImage;
	private TextView _drawer_helpText;
	private ImageView _drawer_aboutImage;
	private TextView _drawer_aboutText;
	private TextView _drawer_installedText;
	private TextView _drawer_newVersionText;
	private TextView _drawer_versionInfoText;
	
	private ObjectAnimator ObjectAnimator1 = new ObjectAnimator();
	private TimerTask Timer;
	private ObjectAnimator ObjectAnimator2 = new ObjectAnimator();
	private ObjectAnimator ObjectAnimator3 = new ObjectAnimator();
	private Intent intent = new Intent();
	private ObjectAnimator ObjectAnimator4 = new ObjectAnimator();
	private SharedPreferences Sp;
	private Calendar Calendar1 = Calendar.getInstance();
	private SQLiteDatabase Database;
	private void Database_createTable(String _tableName, HashMap<String, Object> _columns) throws SQLException {
		StringBuilder _createQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		_createQuery.append(_tableName).append(" (");
		for (String _column : _columns.keySet()) {
			_createQuery.append(_column).append(" ").append(_columns.get(_column)).append(", ");
		}
		_createQuery.setLength(_createQuery.length() - 2);
		_createQuery.append(");");
		Database.execSQL(_createQuery.toString());
	}
	
	private void Database_insert(String _tableName, HashMap<String, Object> _data) throws SQLException {
		StringBuilder _keys = new StringBuilder();
		StringBuilder _values = new StringBuilder();
		String[] _bindArgs = new String[_data.size()];
		int _i = 0;
		for (String _key : _data.keySet()) {
			_keys.append(_key).append(", ");
			_values.append("?, ");
			_bindArgs[_i++] = _data.get(_key).toString();
		}
		_keys.setLength(_keys.length() - 2);
		_values.setLength(_values.length() - 2);
		String _insertQuery = "INSERT INTO " + _tableName + " (" + _keys.toString() + ") VALUES (" + _values.toString() + ");";
		Database.execSQL(_insertQuery, _bindArgs);
	}
	
	private void Database_update(String _tableName, HashMap<String, Object> _data, String _whereClause, String[] _whereArgs) throws SQLException {
		StringBuilder _setClause = new StringBuilder();
		String[] _bindArgs = new String[_data.size() + _whereArgs.length];
		int _i = 0;
		for (String _key : _data.keySet()) {
			_setClause.append(_key).append(" = ?, ");
			_bindArgs[_i++] = _data.get(_key).toString();
		}
		_setClause.setLength(_setClause.length() - 2);
		System.arraycopy(_whereArgs, 0, _bindArgs, _i, _whereArgs.length);
		String _updateQuery = "UPDATE " + _tableName + " SET " + _setClause.toString() + " WHERE " + _whereClause;
		Database.execSQL(_updateQuery, _bindArgs);
	}
	
	private HashMap<String, Object> Database_retrieveOne(String _tableName, String[] _columns, String _whereClause, String[] _whereArgs) {
		Cursor _cursor = null;
		HashMap<String, Object> _result = new HashMap<>();
		try {
			_cursor = Database.query(_tableName, _columns, _whereClause, _whereArgs, null, null, null);
			if (_cursor != null && _cursor.moveToFirst()) {
				for (String _column : _columns) {
					int _index = _cursor.getColumnIndex(_column);
					_result.put(_column, _cursor.getString(_index));
				}
			}
		} catch (SQLException _e) {
			throw new SQLiteException("Error retrieving entry", _e);
		} finally {
			if (_cursor != null) {
				_cursor.close();
			}
		}
		return _result;
	}
	
	private ArrayList<HashMap<String, Object>> Database_retrieveMultiple(String _tableName, String[] _columns, String _whereClause, String[] _whereArgs) {
		ArrayList<HashMap<String, Object>> _results = new ArrayList<>();
		Cursor _cursor = null;
		try {
			_cursor = Database.query(_tableName, _columns, _whereClause, _whereArgs, null, null, null);
			if (_cursor != null && _cursor.moveToFirst()) {
				do {
					HashMap<String, Object> _row = new HashMap<>();
					for (String _column : _columns) {
						int _index = _cursor.getColumnIndex(_column);
						_row.put(_column, _cursor.getString(_index));
					}
					_results.add(_row);
				} while (_cursor.moveToNext());
			}
		} catch (SQLException _e) {
			throw new SQLiteException("Error retrieving entries", _e);
		} finally {
			if (_cursor != null) {
				_cursor.close();
			}
		}
		return _results;
	}
	private Intent Camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	private File _file_Camera;
	private Intent FilePicker = new Intent(Intent.ACTION_GET_CONTENT);
	private RewardedAd rewardedAd;
	private OnUserEarnedRewardListener _rewardedAd_on_user_earned_reward_listener;
	private RewardedAdLoadCallback _rewardedAd_rewarded_ad_load_callback;
	private FullScreenContentCallback _rewardedAd_full_screen_content_callback;
	private DatabaseReference firebase = _firebase.getReference("appData");
	private ChildEventListener _firebase_child_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		SplashScreen.installSplashScreen(this);
super.onCreate(_savedInstanceState);
SharedPreferences Sp = getSharedPreferences("QuickNotesData", MODE_PRIVATE);
if (Sp.contains("language")) {
if (Sp.getString("language", "").equals("device")) {
Locale locale = Resources.getSystem().getConfiguration().locale; // cihaz dili
Locale.setDefault(locale);

Configuration config = new Configuration();
config.setLocale(locale);

getResources().updateConfiguration(config, getResources().getDisplayMetrics());
} else {
Locale locale = new Locale(Sp.getString("language", ""));
Locale.setDefault(locale);
Configuration config = new Configuration();
config.setLocale(locale);
getResources().updateConfiguration(config, getResources().getDisplayMetrics());
}
} else {
Locale locale = Resources.getSystem().getConfiguration().locale; // cihaz dili
Locale.setDefault(locale);

Configuration config = new Configuration();
config.setLocale(locale);

getResources().updateConfiguration(config, getResources().getDisplayMetrics());
}
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		MobileAds.initialize(this);
		_reward_ad_unit_id = "ca-app-pub-4066032612915131/8755979453";
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
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
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		_fab = findViewById(R.id._fab);
		_drawer = findViewById(R.id._drawer);
		ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(MainActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
		_drawer.addDrawerListener(_toggle);
		_toggle.syncState();
		
		LinearLayout _nav_view = findViewById(R.id._nav_view);
		
		mainBg = findViewById(R.id.mainBg);
		mainRelativeBg = findViewById(R.id.mainRelativeBg);
		banner1 = findViewById(R.id.banner1);
		mainVscroll = findViewById(R.id.mainVscroll);
		toolbarBlur = findViewById(R.id.toolbarBlur);
		mainBigBg = findViewById(R.id.mainBigBg);
		welcomeBg = findViewById(R.id.welcomeBg);
		categoryMainBg = findViewById(R.id.categoryMainBg);
		categoryText = findViewById(R.id.categoryText);
		divider = findViewById(R.id.divider);
		mainRecycler = findViewById(R.id.mainRecycler);
		emptyLottie = findViewById(R.id.emptyLottie);
		welcomeText = findViewById(R.id.welcomeText);
		dailyText = findViewById(R.id.dailyText);
		categoryScrollBg = findViewById(R.id.categoryScrollBg);
		editCategoryBlurBg = findViewById(R.id.editCategoryBlurBg);
		categoryBg = findViewById(R.id.categoryBg);
		categoryAllButton = findViewById(R.id.categoryAllButton);
		category1Button = findViewById(R.id.category1Button);
		category2Button = findViewById(R.id.category2Button);
		category3Button = findViewById(R.id.category3Button);
		category4Button = findViewById(R.id.category4Button);
		category5Button = findViewById(R.id.category5Button);
		category6Button = findViewById(R.id.category6Button);
		category7Button = findViewById(R.id.category7Button);
		category8Button = findViewById(R.id.category8Button);
		editCategoryNamesImage = findViewById(R.id.editCategoryNamesImage);
		toolbarBigBg = findViewById(R.id.toolbarBigBg);
		toolbarBg = findViewById(R.id.toolbarBg);
		searchBg = findViewById(R.id.searchBg);
		optionsBg = findViewById(R.id.optionsBg);
		toolbarDrawerImage = findViewById(R.id.toolbarDrawerImage);
		toolbarTextBg = findViewById(R.id.toolbarTextBg);
		layoutImage = findViewById(R.id.layoutImage);
		showFavoritesImage = findViewById(R.id.showFavoritesImage);
		showDateAndTimeImage = findViewById(R.id.showDateAndTimeImage);
		toolbarSearchImage = findViewById(R.id.toolbarSearchImage);
		toolbarTitle = findViewById(R.id.toolbarTitle);
		toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
		searchedittext = findViewById(R.id.searchedittext);
		optionsCancel = findViewById(R.id.optionsCancel);
		optionsText = findViewById(R.id.optionsText);
		optionsDelete = findViewById(R.id.optionsDelete);
		optionsArchive = findViewById(R.id.optionsArchive);
		optionsShare = findViewById(R.id.optionsShare);
		_drawer_blurViewBg = _nav_view.findViewById(R.id.blurViewBg);
		_drawer_mainBg = _nav_view.findViewById(R.id.mainBg);
		_drawer_appTitleBackBg = _nav_view.findViewById(R.id.appTitleBackBg);
		_drawer_vscrollBg = _nav_view.findViewById(R.id.vscrollBg);
		_drawer_appTitleBg = _nav_view.findViewById(R.id.appTitleBg);
		_drawer_appTitleText = _nav_view.findViewById(R.id.appTitleText);
		_drawer_buttonsBg = _nav_view.findViewById(R.id.buttonsBg);
		_drawer_ocrBg = _nav_view.findViewById(R.id.ocrBg);
		_drawer_trashBg = _nav_view.findViewById(R.id.trashBg);
		_drawer_archiveBg = _nav_view.findViewById(R.id.archiveBg);
		_drawer_calendarBg = _nav_view.findViewById(R.id.calendarBg);
		_drawer_settingsBg = _nav_view.findViewById(R.id.settingsBg);
		_drawer_helpBg = _nav_view.findViewById(R.id.helpBg);
		_drawer_aboutBg = _nav_view.findViewById(R.id.aboutBg);
		_drawer_versionBg = _nav_view.findViewById(R.id.versionBg);
		_drawer_ocrImage = _nav_view.findViewById(R.id.ocrImage);
		_drawer_ocrTextBg = _nav_view.findViewById(R.id.ocrTextBg);
		_drawer_ocrText = _nav_view.findViewById(R.id.ocrText);
		_drawer_ocrOptionsBg = _nav_view.findViewById(R.id.ocrOptionsBg);
		_drawer_ocrGalleryText = _nav_view.findViewById(R.id.ocrGalleryText);
		_drawer_ocrCameraText = _nav_view.findViewById(R.id.ocrCameraText);
		_drawer_trashImage = _nav_view.findViewById(R.id.trashImage);
		_drawer_trashText = _nav_view.findViewById(R.id.trashText);
		_drawer_archiveImage = _nav_view.findViewById(R.id.archiveImage);
		_drawer_archiveText = _nav_view.findViewById(R.id.archiveText);
		_drawer_calendarImage = _nav_view.findViewById(R.id.calendarImage);
		_drawer_calendarText = _nav_view.findViewById(R.id.calendarText);
		_drawer_settingsImage = _nav_view.findViewById(R.id.settingsImage);
		_drawer_settingsText = _nav_view.findViewById(R.id.settingsText);
		_drawer_helpImage = _nav_view.findViewById(R.id.helpImage);
		_drawer_helpText = _nav_view.findViewById(R.id.helpText);
		_drawer_aboutImage = _nav_view.findViewById(R.id.aboutImage);
		_drawer_aboutText = _nav_view.findViewById(R.id.aboutText);
		_drawer_installedText = _nav_view.findViewById(R.id.installedText);
		_drawer_newVersionText = _nav_view.findViewById(R.id.newVersionText);
		_drawer_versionInfoText = _nav_view.findViewById(R.id.versionInfoText);
		Sp = getSharedPreferences("QuickNotesData", Activity.MODE_PRIVATE);
		_file_Camera = FileUtil.createNewPictureFile(getApplicationContext());
		Uri _uri_Camera;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			_uri_Camera = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", _file_Camera);
		} else {
			_uri_Camera = Uri.fromFile(_file_Camera);
		}
		Camera.putExtra(MediaStore.EXTRA_OUTPUT, _uri_Camera);
		Camera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		FilePicker.setType("image/*");
		FilePicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		welcomeText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				ObjectAnimator animator1 = ObjectAnimator.ofFloat(welcomeText, "rotation", 0f, 15f, -15f, 10f, -10f, 5f, -5f, 0f);
				animator1.setDuration(2000);
				animator1.setInterpolator(new AccelerateDecelerateInterpolator());
				animator1.start();
			}
		});
		
		categoryAllButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				currentCategory = "NoCategory";
				_OnCreateGetFromSQLite();
				categoryText.setText(categoryAllButton.getText().toString().concat(" ".concat(getString(R.string.notes))));
				_SetCurrentCategoryClick(currentCategory);
			}
		});
		
		category1Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				currentCategory = "Category1";
				_OnCreateGetFromSQLite();
				categoryText.setText(category1Button.getText().toString().concat(" ".concat(getString(R.string.notes))));
				_SetCurrentCategoryClick(currentCategory);
			}
		});
		
		category2Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				currentCategory = "Category2";
				_OnCreateGetFromSQLite();
				categoryText.setText(category2Button.getText().toString().concat(" ".concat(getString(R.string.notes))));
				_SetCurrentCategoryClick(currentCategory);
			}
		});
		
		category3Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				currentCategory = "Category3";
				_OnCreateGetFromSQLite();
				categoryText.setText(category3Button.getText().toString().concat(" ".concat(getString(R.string.notes))));
				_SetCurrentCategoryClick(currentCategory);
			}
		});
		
		category4Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				currentCategory = "Category4";
				_OnCreateGetFromSQLite();
				categoryText.setText(category4Button.getText().toString().concat(" ".concat(getString(R.string.notes))));
				_SetCurrentCategoryClick(currentCategory);
			}
		});
		
		category5Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				currentCategory = "Category5";
				_OnCreateGetFromSQLite();
				categoryText.setText(category5Button.getText().toString().concat(" ".concat(getString(R.string.notes))));
				_SetCurrentCategoryClick(currentCategory);
			}
		});
		
		category6Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				currentCategory = "Category6";
				_OnCreateGetFromSQLite();
				categoryText.setText(category6Button.getText().toString().concat(" ".concat(getString(R.string.notes))));
				_SetCurrentCategoryClick(currentCategory);
			}
		});
		
		category7Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				currentCategory = "Category7";
				_OnCreateGetFromSQLite();
				categoryText.setText(category7Button.getText().toString().concat(" ".concat(getString(R.string.notes))));
				_SetCurrentCategoryClick(currentCategory);
			}
		});
		
		category8Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				currentCategory = "Category8";
				_OnCreateGetFromSQLite();
				categoryText.setText(category8Button.getText().toString().concat(" ".concat(getString(R.string.notes))));
				_SetCurrentCategoryClick(currentCategory);
			}
		});
		
		editCategoryNamesImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), CategorynameeditActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				View _fabt = findViewById(R.id._fab);
				_fabt.setTransitionName("logo");  
				
				
				
				ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
				MainActivity.this,
				_fabt,
				"logo"
				);
				
				startActivity(intent, options.toBundle());
			}
		});
		
		toolbarDrawerImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_drawer.openDrawer(GravityCompat.START);
			}
		});
		
		layoutImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final Handler wait = new Handler(Looper.getMainLooper());
				final Runnable rwait  = new Runnable() {
					@Override
					public void run() {
						if (Sp.contains("recyclerMode")) {
							if (Sp.getString("recyclerMode", "").equals("normal")) {
								androidx.recyclerview.widget.StaggeredGridLayoutManager slmmainRecycler = new androidx.recyclerview.widget.StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
								mainRecycler.setLayoutManager(slmmainRecycler);
								Sp.edit().putString("recyclerMode", "staggered").commit();
								layoutImage.setImageResource(R.drawable.grid_view_72dp_black);
							} else {
								LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
								mainRecycler.setLayoutManager(layoutManager);
								Sp.edit().putString("recyclerMode", "normal").commit();
								layoutImage.setImageResource(R.drawable.view_agenda_72dp_black);
							}
						} else {
							LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
							mainRecycler.setLayoutManager(layoutManager);
							Sp.edit().putString("recyclerMode", "normal").commit();
							layoutImage.setImageResource(R.drawable.view_agenda_72dp_black);
						}
						_OnCreateGetFromSQLite();;
					}
				};
				wait.postDelayed(rwait, 50);
			}
		});
		
		showFavoritesImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final Handler wait = new Handler(Looper.getMainLooper());
				final Runnable rwait  = new Runnable() {
					@Override
					public void run() {
						if (sortfavortites) {
							showFavoritesImage.setImageResource(R.drawable.star_72dp_black);
							showFavoritesImage.setColorFilter(getResources().getColor(R.color.colorOnPrimary));
							sortfavortites = false;
							_OnCreateGetFromSQLite();
						} else {
							showFavoritesImage.setImageResource(R.drawable.star_72dp_black_filled);
							showFavoritesImage.setColorFilter(0xFFFFEB3B);
							sortfavortites = true;
							SketchwareUtil.sortListMap(notesListMap, "isFavorite", true, false);
						}
						adapter = new MainRecyclerAdapter(notesListMap);
						mainRecycler.setAdapter(adapter);;
					}
				};
				wait.postDelayed(rwait, 50);
			}
		});
		
		showDateAndTimeImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Sp.getString("hideTime", "").equals("1")) {
					Sp.edit().putString("hideTime", "0").commit();
					showDateAndTimeImage.setImageResource(R.drawable.event_available_72dp_black);
				} else {
					Sp.edit().putString("hideTime", "1").commit();
					showDateAndTimeImage.setImageResource(R.drawable.event_busy_72dp_black);
				}
				final Handler wait = new Handler(Looper.getMainLooper());
				final Runnable rwait  = new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();;
					}
				};
				wait.postDelayed(rwait, 50);
			}
		});
		
		toolbarSearchImage.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				intent.setClass(getApplicationContext(), SearchActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				View toolbarSearchImaget = findViewById(R.id.toolbarSearchImage);
				toolbarSearchImaget.setTransitionName("search");  
				
				
				
				ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
				MainActivity.this,
				toolbarSearchImaget,
				"search"
				);
				
				startActivity(intent, options.toBundle());
				return true;
			}
		});
		
		toolbarSearchImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (searchBg.getVisibility() == View.GONE) {
					if (sortfavortites) {
						showFavoritesImage.setImageResource(R.drawable.star_72dp_black);
						showFavoritesImage.setColorFilter(getResources().getColor(R.color.colorOnPrimary));
						sortfavortites = false;
					}
					if (selectionMode) {
						selectedItems.clear();
						optionsBg.setVisibility(View.GONE);
						selectionMode = false;
						_OnCreateGetFromSQLite();
					}
					searchBg.setVisibility(View.VISIBLE);
					searchedittext.setEnabled(true);
					showDateAndTimeImage.setVisibility(View.GONE);
					showFavoritesImage.setVisibility(View.GONE);
					layoutImage.setVisibility(View.GONE);
					toolbarSearchImage.setVisibility(View.VISIBLE);
				} else {
					searchedittext.setText("");
					searchBg.setVisibility(View.GONE);
					searchedittext.setEnabled(false);
					showDateAndTimeImage.setVisibility(View.VISIBLE);
					showFavoritesImage.setVisibility(View.VISIBLE);
					layoutImage.setVisibility(View.VISIBLE);
					_OnCreateGetFromSQLite();
				}
			}
		});
		
		searchedittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				try { 
					searchNotesListMap.clear(); 
					if (_charSeq.length() == 0) { 
						searchNotesListMap.addAll(notesListMap); 
					} else { 
						double mainRecyclerval00 = 0; 
						double mainRecyclerval0 = 0; 
						String mainRecyclerval2 = ""; 
						mainRecyclerval0 = notesListMap.size(); 
						mainRecyclerval00 = mainRecyclerval0 - 1; 
						for (int mainRecyclerval1 = 0; mainRecyclerval1 < (int)(mainRecyclerval0); mainRecyclerval1++) { 
							mainRecyclerval2 = notesListMap.get((int)mainRecyclerval00).get("note").toString(); 
							if (mainRecyclerval2.toLowerCase().contains(_charSeq.toLowerCase())) { 
								searchNotesListMap.add(notesListMap.get((int)mainRecyclerval00)); 
							} 
							mainRecyclerval00--; 
						} 
					}
				} catch (Exception e) {}; 
				
				mainRecycler.setAdapter(new MainRecyclerAdapter(searchNotesListMap)); 
				mainRecycler.setHasFixedSize(true);
				if (searchedittext.getText().toString().equals("")) {
					adapter = new MainRecyclerAdapter(notesListMap);
					mainRecycler.setAdapter(adapter);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		optionsCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				selectionMode = false;
				selectedItems.clear();
				optionsBg.setVisibility(View.GONE);
				_OnCreateGetFromSQLite();
				showDateAndTimeImage.setVisibility(View.VISIBLE);
				showFavoritesImage.setVisibility(View.VISIBLE);
				layoutImage.setVisibility(View.VISIBLE);
				toolbarSearchImage.setVisibility(View.VISIBLE);
			}
		});
		
		optionsDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final Handler wait = new Handler(Looper.getMainLooper());
				final Runnable rwait  = new Runnable() {
					@Override
					public void run() {
						while(selectedItems.size() > 0) {
							Calendar1 = Calendar.getInstance();
							noteValues = Database_retrieveOne("notes", "id, title, note, tags, isFavorite, category, state, sortTime, createTime, editTime, trashTime, archiveTime, isReminder, reminderTime".split(", "), "id" + " = ?", new String[]{String.valueOf((long)(selectedItems.get((int)(0)).doubleValue()))});
							noteValues.put("state", "Trash");
							noteValues.put("trashTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
							Database_update("notes", noteValues, "id" + " = ?", new String[]{noteValues.get("id").toString()});
							selectedItems.remove((int)(0));
						}
						selectionMode = false;
						optionsBg.setVisibility(View.GONE);
						SketchwareUtil.showMessage(getApplicationContext(), "Trash Done");
						_OnCreateGetFromSQLite();
						showDateAndTimeImage.setVisibility(View.VISIBLE);
						showFavoritesImage.setVisibility(View.VISIBLE);
						layoutImage.setVisibility(View.VISIBLE);
						toolbarSearchImage.setVisibility(View.VISIBLE);;
					}
				};
				wait.postDelayed(rwait, 50);
			}
		});
		
		optionsArchive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final Handler wait = new Handler(Looper.getMainLooper());
				final Runnable rwait  = new Runnable() {
					@Override
					public void run() {
						while(selectedItems.size() > 0) {
							Calendar1 = Calendar.getInstance();
							noteValues = Database_retrieveOne("notes", "id, title, note, tags, isFavorite, category, state, sortTime, createTime, editTime, trashTime, archiveTime, isReminder, reminderTime".split(", "), "id" + " = ?", new String[]{String.valueOf((long)(selectedItems.get((int)(0)).doubleValue()))});
							noteValues.put("state", "Archive");
							noteValues.put("archiveTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
							Database_update("notes", noteValues, "id" + " = ?", new String[]{noteValues.get("id").toString()});
							selectedItems.remove((int)(0));
						}
						selectionMode = false;
						optionsBg.setVisibility(View.GONE);
						SketchwareUtil.showMessage(getApplicationContext(), "Archive Done");
						_OnCreateGetFromSQLite();
						showDateAndTimeImage.setVisibility(View.VISIBLE);
						showFavoritesImage.setVisibility(View.VISIBLE);
						layoutImage.setVisibility(View.VISIBLE);
						toolbarSearchImage.setVisibility(View.VISIBLE);;
					}
				};
				wait.postDelayed(rwait, 50);
			}
		});
		
		optionsShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final Handler wait = new Handler(Looper.getMainLooper());
				final Runnable rwait  = new Runnable() {
					@Override
					public void run() {
						if (selectedItems.size() == 1) {
							noteValues = Database_retrieveOne("notes", "id, title, note, tags, isFavorite, category, state, sortTime, createTime, editTime, trashTime, archiveTime, isReminder, reminderTime".split(", "), "id" + " = ?", new String[]{String.valueOf((long)(selectedItems.get((int)(0)).doubleValue()))});
							if (noteValues.containsKey("title") && noteValues.containsKey("note")) {
								if (!noteValues.get("title").toString().equals("") && !noteValues.get("note").toString().equals("")) {
									Intent i = new Intent(android.content.Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(android.content.Intent.EXTRA_TEXT, noteValues.get("title").toString().concat("\n\n".concat(noteValues.get("note").toString()))); startActivity(Intent.createChooser(i,"Share Note"));
								} else {
									if (!noteValues.get("title").toString().equals("")) {
										Intent i = new Intent(android.content.Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(android.content.Intent.EXTRA_TEXT, noteValues.get("title").toString()); startActivity(Intent.createChooser(i,"Share Note"));
									}
									if (!noteValues.get("note").toString().equals("")) {
										Intent i = new Intent(android.content.Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(android.content.Intent.EXTRA_TEXT, noteValues.get("note").toString()); startActivity(Intent.createChooser(i,"Shate Note"));
									}
								}
							} else {
								SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.error));
							}
						}
						selectedItems.clear();
						selectionMode = false;
						optionsBg.setVisibility(View.GONE);
						_OnCreateGetFromSQLite();
						showDateAndTimeImage.setVisibility(View.VISIBLE);
						showFavoritesImage.setVisibility(View.VISIBLE);
						layoutImage.setVisibility(View.VISIBLE);
						toolbarSearchImage.setVisibility(View.VISIBLE);;
					}
				};
				wait.postDelayed(rwait, 50);
			}
		});
		
		_fab.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				
				return true;
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), NoteeditActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("type", "new");
				intent.putExtra("category", currentCategory);
				View _fabt = findViewById(R.id._fab);
				_fabt.setTransitionName("logo");  
				
				
				
				ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
				MainActivity.this,
				_fabt,
				"logo"
				);
				
				startActivity(intent, options.toBundle());
			}
		});
		
		_firebase_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		firebase.addChildEventListener(_firebase_child_listener);
		
		_drawer_ocrBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (_drawer_ocrOptionsBg.getVisibility() == View.GONE) {
					_drawer_ocrOptionsBg.setVisibility(View.VISIBLE);
				} else {
					_drawer_ocrOptionsBg.setVisibility(View.GONE);
				}
			}
		});
		
		_drawer_trashBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), TrashActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		_drawer_archiveBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), ArchiveActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		_drawer_calendarBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), CalendarActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		_drawer_settingsBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), SettingActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		_drawer_helpBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), HelpActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		_drawer_aboutBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), AboutActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		_drawer_ocrGalleryText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				FilePicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
				startActivityForResult(FilePicker, REQ_CD_FİLEPİCKER);
				_drawer_ocrOptionsBg.setVisibility(View.GONE);
			}
		});
		
		_drawer_ocrCameraText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(Camera, REQ_CD_CAMERA);
				_drawer_ocrOptionsBg.setVisibility(View.GONE);
			}
		});
	}
	
	private void initializeLogic() {
		if (Sp.contains("language")) {
			if (Sp.getString("language", "").equals("device")) {
				Locale locale = Resources.getSystem().getConfiguration().locale; 
				Locale.setDefault(locale);
				
				Configuration config = getResources().getConfiguration();
				config.setLocale(locale);
				getResources().updateConfiguration(config, getResources().getDisplayMetrics());
			} else {
				
				Locale locale = new Locale(Sp.getString("language", "")); // TinyDB’den çekilebilir
				Locale.setDefault(locale);
				Configuration config = getResources().getConfiguration();
				config.setLocale(locale);
				getResources().updateConfiguration(config, getResources().getDisplayMetrics());
				
			}
		} else {
			Locale locale = Resources.getSystem().getConfiguration().locale; 
			Locale.setDefault(locale);
			
			Configuration config = getResources().getConfiguration();
			config.setLocale(locale);
			getResources().updateConfiguration(config, getResources().getDisplayMetrics());
		}
		
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_CAMERA:
			if (_resultCode == Activity.RESULT_OK) {
				String _filePath = _file_Camera.getAbsolutePath();
				
				intent.setClass(getApplicationContext(), OcrActivity.class);
				intent.putExtra("filePath", _filePath);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			else {
				
			}
			break;
			
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
				intent.setClass(getApplicationContext(), OcrActivity.class);
				intent.putExtra("filePath", _filePath.get((int)(0)));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	
	@Override 
	public void onConfigurationChanged(Configuration newConfig) 
	{ 
		super.onConfigurationChanged(newConfig);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			welcomeBg.setLayoutParams(new LinearLayout.LayoutParams((int) (android.widget.LinearLayout.LayoutParams.MATCH_PARENT),(int) (250)));
		}
		else
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			welcomeBg.setLayoutParams(new LinearLayout.LayoutParams((int) (android.widget.LinearLayout.LayoutParams.MATCH_PARENT),(int) (400)));
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (Sp.contains("reloadData")) {
			switch(Sp.getString("reloadData", "")) {
				case "note": {
					_OnCreateGetFromSQLite();
					break;
				}
				case "imported": {
					categoryAllButton.performClick();
					final Handler importDelay = new Handler(Looper.getMainLooper());
					final Runnable rimportDelay  = new Runnable() {
						@Override
						public void run() {
							_OnCreateGetFromSQLite();
							_GetCategoryNamesFromSQLite();;
						}
					};
					importDelay.postDelayed(rimportDelay, 50);
					break;
				}
				case "category": {
					_GetCategoryNamesFromSQLite();
					break;
				}
				case "tutorial": {
					if (_drawer.isDrawerOpen(GravityCompat.START)) {
						_drawer.closeDrawer(GravityCompat.START);
					}
					TapTargetSequence sequence = new TapTargetSequence(MainActivity.this)
					.targets(
					TapTarget.forView(editCategoryNamesImage, getString(R.string.customizeCategoryNames), getString(R.string.fromHereYouCanCustomizeCategoryNames))
					.outerCircleColorInt(getResources().getColor(R.color.colorPrimaryHint))
					.targetCircleColorInt(getResources().getColor(R.color.colorAccent))
					.titleTextColor(0xFFFFFFFF)
					.descriptionTextColor(0xFFFFFFFF)
					.drawShadow(true)
					.cancelable(false)
					.tintTarget(false)
					.targetRadius(50)
					.transparentTarget(true)
					
					,
					
					TapTarget.forView(showFavoritesImage, getString(R.string.showFavoriteNotes), getString(R.string.youCanViewYourFavoriteNotesHere))
					.outerCircleColorInt(getResources().getColor(R.color.colorPrimaryHint))
					.targetCircleColorInt(getResources().getColor(R.color.colorAccent))
					.titleTextColor(0xFFFFFFFF)
					.descriptionTextColor(0xFFFFFFFF)
					.drawShadow(true)
					.cancelable(false)
					.tintTarget(false)
					.targetRadius(50)
					.transparentTarget(true)
					
					,
					
					TapTarget.forView(showDateAndTimeImage, getString(R.string.showHideDateAndTime), getString(R.string.toggleToShowOrHideTheDateAndTimeOnYourNotes))
					.outerCircleColorInt(getResources().getColor(R.color.colorPrimaryHint))
					.targetCircleColorInt(getResources().getColor(R.color.colorAccent))
					.titleTextColor(0xFFFFFFFF)
					.descriptionTextColor(0xFFFFFFFF)
					.drawShadow(true)
					.cancelable(false)
					.tintTarget(false)
					.targetRadius(50)
					.transparentTarget(true)
					
					,
					
					TapTarget.forView(toolbarSearchImage, getString(R.string.searchInNotes), getString(R.string.fromHereYouCanQuicklySearchAndLocateYourNotes))
					.outerCircleColorInt(getResources().getColor(R.color.colorPrimaryHint))
					.targetCircleColorInt(getResources().getColor(R.color.colorAccent))
					.titleTextColor(0xFFFFFFFF)
					.descriptionTextColor(0xFFFFFFFF)
					.drawShadow(true)
					.cancelable(false)
					.tintTarget(false)
					.targetRadius(50)
					.transparentTarget(true)
					
					,
					
					TapTarget.forView(_fab, getString(R.string.addNewNotes), getString(R.string.createANewNoteByPressingThisButton))
					.outerCircleColorInt(getResources().getColor(R.color.colorPrimaryHint))
					.targetCircleColorInt(getResources().getColor(R.color.colorAccent))
					.titleTextColor(0xFFFFFFFF)
					.descriptionTextColor(0xFFFFFFFF)
					.drawShadow(true)
					.cancelable(false)
					.tintTarget(false)
					.targetRadius(50)
					.transparentTarget(true)
					
					)
					.listener(new TapTargetSequence.Listener() {
						@Override
						public void onSequenceFinish() {
							intent.setClass(getApplicationContext(), NoteeditActivity.class);
							intent.putExtra("type", "tutorial");
							intent.putExtra("category", currentCategory);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);;
						}
						
						@Override
						public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
							// Her bir adımda yapılacak işlem
							
						}
						
						@Override
						public void onSequenceCanceled(TapTarget lastTarget) {
							;
						}
						
					});
					sequence.start();
					break;
				}
			}
			Sp.edit().remove("reloadData").commit();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (!oncreate) {
			_AutoSaveLoad();
			try{
				getSupportActionBar().hide();
				_drawer.setScrimColor(android.graphics.Color.TRANSPARENT);
				toolbarBlur.addView(mainRelativeBg);
			}catch(Exception e){
				
			}
			_fab.setBackgroundColor(0xFFEC407A);
			_fab.setImageResource(R.drawable.add_2_72dp_black);
			int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
			if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
				// ACTION WHEN DARK MODE IS ON
				
			} else {
				// ACTION IF DARK MODE IS OFF
				getWindow().getDecorView().setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			};
			
			_OnCreateSetSQLite();
			_First();
			currentCategory = "NoCategory";
			_SetLayoutManager();
			_OnCreateGetFromSQLite();
			_GetCategoryNamesFromSQLite();
			_OnCreateRipple();
			if (Build.VERSION.SDK_INT > 30) {
				if (!Sp.contains("performanceMode")) {
					{
						View decorView = getWindow().getDecorView();
						ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
						Drawable windowBackground = decorView.getBackground();
						float blur1 = ((float)5);
						toolbarBlur.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
						toolbarBlur.setClipToOutline(true);
						toolbarBlur.setupWith(rootView, new RenderEffectBlur())
						.setBlurRadius(blur1)
						.setFrameClearDrawable(windowBackground) 
						.setBlurAutoUpdate(true);
						
						editCategoryBlurBg.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
						editCategoryBlurBg.setClipToOutline(true);
						editCategoryBlurBg.setupWith(rootView, new RenderEffectBlur())
						.setBlurRadius(blur1)
						.setFrameClearDrawable(windowBackground) 
						.setBlurAutoUpdate(true);
						
						float radius2 = TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()
						);
						
						// Üst ve alt renkleri direkt colors.xml'den al
						int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
						int colorPrimaryHalf = ContextCompat.getColor(this, R.color.colorPrimaryHalf);
						
						// Yukarıdan aşağı gradient
						GradientDrawable gd2 = new GradientDrawable(
						GradientDrawable.Orientation.TOP_BOTTOM,
						new int[]{ colorPrimary, colorPrimaryHalf }
						);
						
						// Alt köşeleri oval yap
						gd2.setCornerRadii(new float[]{
							0, 0,               // sol üst
							0, 0,               // sağ üst
							radius2, radius2,   // sağ alt
							radius2, radius2    // sol alt
						});
						
						toolbarBlur.setBackground(gd2);
					}
					{
						View decorView = getWindow().getDecorView();
						ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
						Drawable windowBackground = decorView.getBackground();
						float blur1 = ((float)15);
						_drawer_blurViewBg.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
						_drawer_blurViewBg.setClipToOutline(true);
						_drawer_blurViewBg.setupWith(rootView, new RenderEffectBlur())
						.setBlurRadius(blur1)
						.setFrameClearDrawable(windowBackground) 
						.setBlurAutoUpdate(true);
						
						android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
						gd1.setColor(Color.TRANSPARENT);
						gd1.setCornerRadii(new float[] {0,0,50,50,50,50,0,0});
						_drawer_blurViewBg.setBackground(gd1);
						float radius2 = TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()
						);
						
						// Üst ve alt renkleri direkt colors.xml'den al
						int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
						int colorPrimaryHalf = ContextCompat.getColor(this, R.color.colorPrimaryHalf);
						
						// Yukarıdan aşağı gradient
						GradientDrawable gd2 = new GradientDrawable(
						GradientDrawable.Orientation.TOP_BOTTOM,
						new int[]{ colorPrimary, colorPrimaryHalf }
						);
						
						// Alt köşeleri oval yap
						gd2.setCornerRadii(new float[]{
							0, 0,               // sol üst
							0, 0,               // sağ üst
							radius2, radius2,   // sağ alt
							radius2, radius2    // sol alt
						});
						
						_drawer_appTitleBackBg.setBackground(gd2);
					}
				} else {
					android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
					gd1.setColor(getResources().getColor(R.color.colorBackground));
					gd1.setCornerRadii(new float[] {0,0,50,50,50,50,0,0});
					_drawer_blurViewBg.setBackground(gd1);
					android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
					gd2.setColor(getResources().getColor(R.color.colorBackground));
					gd2.setCornerRadii(new float[] {0,0,0,0,50,50,50,50});
					toolbarBlur.setBackground(gd2);
				}
			} else {
				android.graphics.drawable.GradientDrawable gd1 = new android.graphics.drawable.GradientDrawable();
				gd1.setColor(getResources().getColor(R.color.colorBackground));
				gd1.setCornerRadii(new float[] {0,0,50,50,50,50,0,0});
				_drawer_blurViewBg.setBackground(gd1);
				android.graphics.drawable.GradientDrawable gd2 = new android.graphics.drawable.GradientDrawable();
				gd2.setColor(getResources().getColor(R.color.colorBackground));
				gd2.setCornerRadii(new float[] {0,0,0,0,50,50,50,50});
				toolbarBlur.setBackground(gd2);
			}
			if (Sp.getString("hidetime", "").equals("1")) {
				showDateAndTimeImage.setImageResource(R.drawable.event_busy_72dp_black);
			}
			{
				AdRequest adRequest = new AdRequest.Builder().build();
				banner1.loadAd(adRequest);
			}
			_fab.setOnTouchListener(new View.OnTouchListener() {
				float currentX = 0, currentY = 0;
				
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
						// Basınca hafif büyüt
						view.animate()
						.scaleX(1.2f)
						.scaleY(1.2f)
						.setDuration(300)
						.start();
						break;
						
						case MotionEvent.ACTION_MOVE:
						float dx = event.getX() - (view.getWidth() / 2f);
						float dy = event.getY() - (view.getHeight() / 2f);
						
						// Parmağın uzaklığı
						double length = Math.sqrt(dx * dx + dy * dy);
						
						// Maksimum kayma mesafesi (örnek 80dp)
						float max = 30 * view.getResources().getDisplayMetrics().density;
						
						// Yumuşatma faktörü: küçük hareketlerde hızlı, uzaklaştıkça yavaş
						float factor = (float) (1 - Math.exp(-length / 150));
						
						// Yeni hedef konum
						float offsetX = 0;
						float offsetY = 0;
						
						if (length > 0) {
							offsetX = (float) (dx / length * max * factor);
							offsetY = (float) (dy / length * max * factor);
						}
						
						// Yumuşak takip (0.3 → daha küçük yaparsan daha yumuşak olur)
						currentX = currentX + (offsetX - currentX) * 0.3f;
						currentY = currentY + (offsetY - currentY) * 0.3f;
						
						view.setTranslationX(currentX);
						view.setTranslationY(currentY);
						break;
						
						case MotionEvent.ACTION_UP:
						case MotionEvent.ACTION_CANCEL:
						// Normal haline dön
						view.animate()
						.translationX(0)
						.translationY(0)
						.scaleX(1f)
						.scaleY(1f)
						.setInterpolator(new OvershootInterpolator())
						.setDuration(250)
						.start();
						break;
					}
					return false; // false → ripple ve onClick çalışsın
				}
			});
			categoryText.setText(categoryAllButton.getText().toString().concat(" ".concat(getString(R.string.notes))));
			oncreate = true;
			firebase.addChildEventListener(_firebase_child_listener);
			firebase.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot _dataSnapshot) {
					firebaseListMap = new ArrayList<>();
					try {
						GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
						for (DataSnapshot _data : _dataSnapshot.getChildren()) {
							HashMap<String, Object> _map = _data.getValue(_ind);
							firebaseListMap.add(_map);
						}
					} catch (Exception _e) {
						_e.printStackTrace();
					}
					try{
						if (Double.parseDouble(getPackageManager().getPackageInfo(getPackageName(), 0).versionName.toString()) > Double.parseDouble(firebaseListMap.get((int)0).get("ver").toString())) {
							_drawer_versionBg.setVisibility(View.GONE);
						} else {
							_drawer_versionBg.setVisibility(View.VISIBLE);
							_drawer_versionBg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)5, 0xFFFFEB3B, Color.TRANSPARENT));
							_drawer_newVersionText.setText(getString(R.string.newVersion).concat(" ".concat(firebaseListMap.get((int)0).get("ver").toString())));
							_drawer_versionInfoText.setText(firebaseListMap.get((int)0).get("info").toString());
							if (firebaseListMap.get((int)0).get("ver").toString().equals(getPackageManager().getPackageInfo(getPackageName(), 0).versionName.toString())) {
								_drawer_versionBg.setEnabled(false);
								_drawer_versionBg.setAlpha((float)(0.7d));
								_drawer_installedText.setVisibility(View.VISIBLE);
							} else {
								SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.newVersionFound));
							}
						}
					}catch(Exception e){
						firebase.removeEventListener(_firebase_child_listener);
					}
					firebase.removeEventListener(_firebase_child_listener);
				}
				@Override
				public void onCancelled(DatabaseError _databaseError) {
				}
			});
			_drawerAnim();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (banner1 != null) {
			banner1.destroy();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (banner1 != null) {
			banner1.pause();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (_drawer.isDrawerOpen(GravityCompat.START)) {
			_drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	public void _OnCreateRipple() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable _drawer_settingsBgBG = new android.graphics.drawable.GradientDrawable();
			_drawer_settingsBgBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			_drawer_settingsBgBG.setCornerRadius((float)30);
			_drawer_settingsBgBG.setStroke((int)0, Color.TRANSPARENT);
			_drawer_settingsBg.setBackground(_drawer_settingsBgBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable _drawer_settingsBgMask = new android.graphics.drawable.GradientDrawable();
			_drawer_settingsBgMask.setCornerRadius((float)30);
			_drawer_settingsBgMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable _drawer_settingsBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			_drawer_settingsBgMask // mask
			);
			
			_drawer_settingsBg.setForeground(_drawer_settingsBgRE);
			_drawer_settingsBg.setClickable(true);
			_drawer_settingsBg.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable _drawer_settingsBgGG = new android.graphics.drawable.GradientDrawable();
			_drawer_settingsBgGG.setColor(Color.TRANSPARENT);
			_drawer_settingsBgGG.setCornerRadius((float)30);
			_drawer_settingsBgGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable _drawer_settingsBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			_drawer_settingsBgGG,
			null
			);
			_drawer_settingsBg.setBackground(_drawer_settingsBgRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable _drawer_trashBgBG = new android.graphics.drawable.GradientDrawable();
			_drawer_trashBgBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			_drawer_trashBgBG.setCornerRadius((float)30);
			_drawer_trashBgBG.setStroke((int)0, Color.TRANSPARENT);
			_drawer_trashBg.setBackground(_drawer_trashBgBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable _drawer_trashBgMask = new android.graphics.drawable.GradientDrawable();
			_drawer_trashBgMask.setCornerRadius((float)30);
			_drawer_trashBgMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable _drawer_trashBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			_drawer_trashBgMask // mask
			);
			
			_drawer_trashBg.setForeground(_drawer_trashBgRE);
			_drawer_trashBg.setClickable(true);
			_drawer_trashBg.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable _drawer_trashBgGG = new android.graphics.drawable.GradientDrawable();
			_drawer_trashBgGG.setColor(Color.TRANSPARENT);
			_drawer_trashBgGG.setCornerRadius((float)30);
			_drawer_trashBgGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable _drawer_trashBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			_drawer_trashBgGG,
			null
			);
			_drawer_trashBg.setBackground(_drawer_trashBgRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable _drawer_helpBgBG = new android.graphics.drawable.GradientDrawable();
			_drawer_helpBgBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			_drawer_helpBgBG.setCornerRadius((float)30);
			_drawer_helpBgBG.setStroke((int)0, Color.TRANSPARENT);
			_drawer_helpBg.setBackground(_drawer_helpBgBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable _drawer_helpBgMask = new android.graphics.drawable.GradientDrawable();
			_drawer_helpBgMask.setCornerRadius((float)30);
			_drawer_helpBgMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable _drawer_helpBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			_drawer_helpBgMask // mask
			);
			
			_drawer_helpBg.setForeground(_drawer_helpBgRE);
			_drawer_helpBg.setClickable(true);
			_drawer_helpBg.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable _drawer_helpBgGG = new android.graphics.drawable.GradientDrawable();
			_drawer_helpBgGG.setColor(Color.TRANSPARENT);
			_drawer_helpBgGG.setCornerRadius((float)30);
			_drawer_helpBgGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable _drawer_helpBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			_drawer_helpBgGG,
			null
			);
			_drawer_helpBg.setBackground(_drawer_helpBgRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable _drawer_aboutBgBG = new android.graphics.drawable.GradientDrawable();
			_drawer_aboutBgBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			_drawer_aboutBgBG.setCornerRadius((float)30);
			_drawer_aboutBgBG.setStroke((int)0, Color.TRANSPARENT);
			_drawer_aboutBg.setBackground(_drawer_aboutBgBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable _drawer_aboutBgMask = new android.graphics.drawable.GradientDrawable();
			_drawer_aboutBgMask.setCornerRadius((float)30);
			_drawer_aboutBgMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable _drawer_aboutBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			_drawer_aboutBgMask // mask
			);
			
			_drawer_aboutBg.setForeground(_drawer_aboutBgRE);
			_drawer_aboutBg.setClickable(true);
			_drawer_aboutBg.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable _drawer_aboutBgGG = new android.graphics.drawable.GradientDrawable();
			_drawer_aboutBgGG.setColor(Color.TRANSPARENT);
			_drawer_aboutBgGG.setCornerRadius((float)30);
			_drawer_aboutBgGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable _drawer_aboutBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			_drawer_aboutBgGG,
			null
			);
			_drawer_aboutBg.setBackground(_drawer_aboutBgRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable _drawer_ocrBgBG = new android.graphics.drawable.GradientDrawable();
			_drawer_ocrBgBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			_drawer_ocrBgBG.setCornerRadius((float)30);
			_drawer_ocrBgBG.setStroke((int)0, Color.TRANSPARENT);
			_drawer_ocrBg.setBackground(_drawer_ocrBgBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable _drawer_ocrBgMask = new android.graphics.drawable.GradientDrawable();
			_drawer_ocrBgMask.setCornerRadius((float)30);
			_drawer_ocrBgMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable _drawer_ocrBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			_drawer_ocrBgMask // mask
			);
			
			_drawer_ocrBg.setForeground(_drawer_ocrBgRE);
			_drawer_ocrBg.setClickable(true);
			_drawer_ocrBg.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable _drawer_ocrBgGG = new android.graphics.drawable.GradientDrawable();
			_drawer_ocrBgGG.setColor(Color.TRANSPARENT);
			_drawer_ocrBgGG.setCornerRadius((float)30);
			_drawer_ocrBgGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable _drawer_ocrBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			_drawer_ocrBgGG,
			null
			);
			_drawer_ocrBg.setBackground(_drawer_ocrBgRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable _drawer_archiveBgBG = new android.graphics.drawable.GradientDrawable();
			_drawer_archiveBgBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			_drawer_archiveBgBG.setCornerRadius((float)30);
			_drawer_archiveBgBG.setStroke((int)0, Color.TRANSPARENT);
			_drawer_archiveBg.setBackground(_drawer_archiveBgBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable _drawer_archiveBgMask = new android.graphics.drawable.GradientDrawable();
			_drawer_archiveBgMask.setCornerRadius((float)30);
			_drawer_archiveBgMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable _drawer_archiveBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			_drawer_archiveBgMask // mask
			);
			
			_drawer_archiveBg.setForeground(_drawer_archiveBgRE);
			_drawer_archiveBg.setClickable(true);
			_drawer_archiveBg.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable _drawer_archiveBgGG = new android.graphics.drawable.GradientDrawable();
			_drawer_archiveBgGG.setColor(Color.TRANSPARENT);
			_drawer_archiveBgGG.setCornerRadius((float)30);
			_drawer_archiveBgGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable _drawer_archiveBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			_drawer_archiveBgGG,
			null
			);
			_drawer_archiveBg.setBackground(_drawer_archiveBgRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable _drawer_calendarBgBG = new android.graphics.drawable.GradientDrawable();
			_drawer_calendarBgBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			_drawer_calendarBgBG.setCornerRadius((float)30);
			_drawer_calendarBgBG.setStroke((int)0, Color.TRANSPARENT);
			_drawer_calendarBg.setBackground(_drawer_calendarBgBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable _drawer_calendarBgMask = new android.graphics.drawable.GradientDrawable();
			_drawer_calendarBgMask.setCornerRadius((float)30);
			_drawer_calendarBgMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable _drawer_calendarBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			_drawer_calendarBgMask // mask
			);
			
			_drawer_calendarBg.setForeground(_drawer_calendarBgRE);
			_drawer_calendarBg.setClickable(true);
			_drawer_calendarBg.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable _drawer_calendarBgGG = new android.graphics.drawable.GradientDrawable();
			_drawer_calendarBgGG.setColor(Color.TRANSPARENT);
			_drawer_calendarBgGG.setCornerRadius((float)30);
			_drawer_calendarBgGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable _drawer_calendarBgRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			_drawer_calendarBgGG,
			null
			);
			_drawer_calendarBg.setBackground(_drawer_calendarBgRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable layoutImageBG = new android.graphics.drawable.GradientDrawable();
			layoutImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			layoutImageBG.setCornerRadius((float)50);
			layoutImageBG.setStroke((int)0, Color.TRANSPARENT);
			layoutImage.setBackground(layoutImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable layoutImageMask = new android.graphics.drawable.GradientDrawable();
			layoutImageMask.setCornerRadius((float)50);
			layoutImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable layoutImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			layoutImageMask // mask
			);
			
			layoutImage.setForeground(layoutImageRE);
			layoutImage.setClickable(true);
			layoutImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable layoutImageGG = new android.graphics.drawable.GradientDrawable();
			layoutImageGG.setColor(Color.TRANSPARENT);
			layoutImageGG.setCornerRadius((float)50);
			layoutImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable layoutImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			layoutImageGG,
			null
			);
			layoutImage.setBackground(layoutImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable showFavoritesImageBG = new android.graphics.drawable.GradientDrawable();
			showFavoritesImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			showFavoritesImageBG.setCornerRadius((float)50);
			showFavoritesImageBG.setStroke((int)0, Color.TRANSPARENT);
			showFavoritesImage.setBackground(showFavoritesImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable showFavoritesImageMask = new android.graphics.drawable.GradientDrawable();
			showFavoritesImageMask.setCornerRadius((float)50);
			showFavoritesImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable showFavoritesImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			showFavoritesImageMask // mask
			);
			
			showFavoritesImage.setForeground(showFavoritesImageRE);
			showFavoritesImage.setClickable(true);
			showFavoritesImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable showFavoritesImageGG = new android.graphics.drawable.GradientDrawable();
			showFavoritesImageGG.setColor(Color.TRANSPARENT);
			showFavoritesImageGG.setCornerRadius((float)50);
			showFavoritesImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable showFavoritesImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			showFavoritesImageGG,
			null
			);
			showFavoritesImage.setBackground(showFavoritesImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable toolbarSearchImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarSearchImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarSearchImageBG.setCornerRadius((float)50);
			toolbarSearchImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarSearchImage.setBackground(toolbarSearchImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarSearchImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarSearchImageMask.setCornerRadius((float)50);
			toolbarSearchImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarSearchImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarSearchImageMask // mask
			);
			
			toolbarSearchImage.setForeground(toolbarSearchImageRE);
			toolbarSearchImage.setClickable(true);
			toolbarSearchImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarSearchImageGG = new android.graphics.drawable.GradientDrawable();
			toolbarSearchImageGG.setColor(Color.TRANSPARENT);
			toolbarSearchImageGG.setCornerRadius((float)50);
			toolbarSearchImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarSearchImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarSearchImageGG,
			null
			);
			toolbarSearchImage.setBackground(toolbarSearchImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable showDateAndTimeImageBG = new android.graphics.drawable.GradientDrawable();
			showDateAndTimeImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			showDateAndTimeImageBG.setCornerRadius((float)50);
			showDateAndTimeImageBG.setStroke((int)0, Color.TRANSPARENT);
			showDateAndTimeImage.setBackground(showDateAndTimeImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable showDateAndTimeImageMask = new android.graphics.drawable.GradientDrawable();
			showDateAndTimeImageMask.setCornerRadius((float)50);
			showDateAndTimeImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable showDateAndTimeImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			showDateAndTimeImageMask // mask
			);
			
			showDateAndTimeImage.setForeground(showDateAndTimeImageRE);
			showDateAndTimeImage.setClickable(true);
			showDateAndTimeImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable showDateAndTimeImageGG = new android.graphics.drawable.GradientDrawable();
			showDateAndTimeImageGG.setColor(Color.TRANSPARENT);
			showDateAndTimeImageGG.setCornerRadius((float)50);
			showDateAndTimeImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable showDateAndTimeImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			showDateAndTimeImageGG,
			null
			);
			showDateAndTimeImage.setBackground(showDateAndTimeImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable toolbarDrawerImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarDrawerImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarDrawerImageBG.setCornerRadius((float)50);
			toolbarDrawerImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarDrawerImage.setBackground(toolbarDrawerImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarDrawerImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarDrawerImageMask.setCornerRadius((float)50);
			toolbarDrawerImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarDrawerImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarDrawerImageMask // mask
			);
			
			toolbarDrawerImage.setForeground(toolbarDrawerImageRE);
			toolbarDrawerImage.setClickable(true);
			toolbarDrawerImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarDrawerImageGG = new android.graphics.drawable.GradientDrawable();
			toolbarDrawerImageGG.setColor(Color.TRANSPARENT);
			toolbarDrawerImageGG.setCornerRadius((float)50);
			toolbarDrawerImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarDrawerImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarDrawerImageGG,
			null
			);
			toolbarDrawerImage.setBackground(toolbarDrawerImageRE);
		}
		// Tıklanacak View'i tanımla
		final View touch = findViewById(R.id.categoryAllButton); // örnek view id'si
		
		touch.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					view.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).start();
					break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
					break;
				}
				return false;
			}
		});
		// Tıklanacak View'i tanımla
		final View touch1 = findViewById(R.id.category1Button); // örnek view id'si
		
		touch1.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					view.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).start();
					break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
					break;
				}
				return false;
			}
		});
		// Tıklanacak View'i tanımla
		final View touch2 = findViewById(R.id.category2Button); // örnek view id'si
		
		touch2.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					view.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).start();
					break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
					break;
				}
				return false;
			}
		});
		// Tıklanacak View'i tanımla
		final View touch3 = findViewById(R.id.category3Button); // örnek view id'si
		
		touch3.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					view.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).start();
					break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
					break;
				}
				return false;
			}
		});
		// Tıklanacak View'i tanımla
		final View touch4 = findViewById(R.id.category4Button); // örnek view id'si
		
		touch4.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					view.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).start();
					break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
					break;
				}
				return false;
			}
		});
		// Tıklanacak View'i tanımla
		final View touch5 = findViewById(R.id.category5Button); // örnek view id'si
		
		touch5.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					view.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).start();
					break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
					break;
				}
				return false;
			}
		});
		// Tıklanacak View'i tanımla
		final View touch6 = findViewById(R.id.category6Button); // örnek view id'si
		
		touch6.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					view.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).start();
					break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
					break;
				}
				return false;
			}
		});
		// Tıklanacak View'i tanımla
		final View touch7 = findViewById(R.id.category7Button); // örnek view id'si
		
		touch7.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					view.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).start();
					break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
					break;
				}
				return false;
			}
		});
		// Tıklanacak View'i tanımla
		final View touch8 = findViewById(R.id.category8Button); // örnek view id'si
		
		touch8.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					view.animate().scaleX(0.93f).scaleY(0.93f).setDuration(80).start();
					break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
					break;
				}
				return false;
			}
		});
		final View touch9 = findViewById(R.id.editCategoryNamesImage); // örnek view id'si
		
		touch9.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					// Basınca hafif küçül
					view.animate()
					.scaleX(0.93f)
					.scaleY(0.93f)
					.setDuration(80)
					.start();
					break;
					
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					// Bırakınca overshoot ile geri büyüt
					view.animate()
					.scaleX(1f)
					.scaleY(1f)
					.setInterpolator(new OvershootInterpolator())
					.setDuration(200) // biraz daha uzun olursa zıplama net görünür
					.start();
					break;
				}
				return false; // false → ripple ve onClick çalışsın
			}
		});
		if (Build.VERSION.SDK_INT > 25) {
			_fab.setTooltipText(getString(R.string.newNote));
		}
		if (Build.VERSION.SDK_INT > 25) {
			editCategoryNamesImage.setTooltipText(getString(R.string.customizeCategoryNames));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarDrawerImage.setTooltipText(getString(R.string.sideMenu));
		}
		if (Build.VERSION.SDK_INT > 25) {
			showFavoritesImage.setTooltipText(getString(R.string.showFavorites));
		}
		if (Build.VERSION.SDK_INT > 25) {
			showDateAndTimeImage.setTooltipText(getString(R.string.showHideDateAndTime));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarSearchImage.setTooltipText(getString(R.string.search));
		}
	}
	
	
	public void _First() {
		if (!Sp.contains("firstStart")) {
			Sp.edit().putString("firstStart", "1").commit();
			Sp.edit().putString("hideTime", "0").commit();
			Sp.edit().putString("autoSave", "").commit();
			categoryMap = new HashMap<>();
			categoryMap.put("NoCategory", getString(R.string.all));
			categoryMap.put("Category1", getString(R.string.personal));
			categoryMap.put("Category2", getString(R.string.work));
			categoryMap.put("Category3", getString(R.string.school));
			categoryMap.put("Category4", getString(R.string.specialdays));
			categoryMap.put("Category5", "");
			categoryMap.put("Category6", "");
			categoryMap.put("Category7", "");
			categoryMap.put("Category8", "");
			Database_insert("categoryNames", categoryMap);
			TapTargetView.showFor(MainActivity.this,
			TapTarget.forView(findViewById(R.id._fab), "Write Your First Note", "Tap here to add a new note.")
			.outerCircleColorInt(getResources().getColor(R.color.colorPrimaryHint)) // Arka daire rengi
			.targetCircleColorInt(getResources().getColor(R.color.colorAccent)) // Hedefin ortasındaki daire rengi
			.titleTextSize(22)
			.titleTextColor(android.R.color.white)
			.descriptionTextSize(16)
			.descriptionTextColor(android.R.color.white)
			.textTypeface(Typeface.SANS_SERIF)
			.dimColor(android.R.color.black)
			.drawShadow(true)
			.cancelable(true)
			.tintTarget(false)
			.targetRadius(50)
			.transparentTarget(true),
			new TapTargetView.Listener() {
				@Override
				public void onTargetClick(TapTargetView view) {
					super.onTargetClick(view);
					_fab.performClick();;
					// Kullanıcı balona tıkladığında yapılacaklar
				}
			});
		}
	}
	
	
	public void _OnCreateSetSQLite() {
		Database = SQLiteDatabase.openOrCreateDatabase((MainActivity.this).getDatabasePath("QuickNotes" + ".db").getPath(), null);
		noteValues.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
		noteValues.put("title", "TEXT");
		noteValues.put("note", "TEXT NOT NULL");
		noteValues.put("tags", "TEXT");
		noteValues.put("isFavorite", "INTEGER DEFAULT 0");
		noteValues.put("category", "TEXT DEFAULT 'NoCategory'");
		noteValues.put("state", "TEXT DEFAULT 'Normal'");
		noteValues.put("sortTime", "INTEGER DEFAULT 0");
		noteValues.put("createTime", "INTEGER NOT NULL");
		noteValues.put("editTime", "INTEGER DEFAULT 0");
		noteValues.put("trashTime", "INTEGER DEFAULT 0");
		noteValues.put("archiveTime", "INTEGER DEFAULT 0");
		noteValues.put("isReminder", "INTEGER DEFAULT 0");
		noteValues.put("reminderTime", "INTEGER DEFAULT 0");
		Database_createTable("notes", noteValues);
		noteValues = new HashMap<>();
		noteValues.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
		noteValues.put("NoCategory", "TEXT");
		noteValues.put("Category1", "TEXT");
		noteValues.put("Category2", "TEXT");
		noteValues.put("Category3", "TEXT");
		noteValues.put("Category4", "TEXT");
		noteValues.put("Category5", "TEXT");
		noteValues.put("Category6", "TEXT");
		noteValues.put("Category7", "TEXT");
		noteValues.put("Category8", "TEXT");
		Database_createTable("categoryNames", noteValues);
	}
	
	
	public void _OnCreateGetFromSQLite() {
		try{
			if (currentCategory.equals("NoCategory")) {
				notesListMap = Database_retrieveMultiple("notes", "id, title, note, tags, isFavorite, category, state, sortTime, createTime, editTime, trashTime, archiveTime, isReminder, reminderTime".split(", "), "state" + " = ?", new String[]{"Normal"});
			} else {
				notesListMap = Database_retrieveMultiple(
				"notes",
				"id, title, note, tags, isFavorite, category, state, sortTime, createTime, editTime, trashTime, archiveTime, isReminder, reminderTime".split(", "),
				"category" + " = ? AND " + "state" + " = ?",
				new String[]{currentCategory, "Normal"}
				);
			}
			if (Sp.contains("sort")) {
				switch(Sp.getString("sort", "")) {
					default: {
						SketchwareUtil.sortListMap(notesListMap, "createTime", false, false);
						break;
					}
					case "create": {
						SketchwareUtil.sortListMap(notesListMap, "createTime", false, false);
						break;
					}
					case "edit": {
						SketchwareUtil.sortListMap(notesListMap, "sortTime", false, false);
						break;
					}
				}
			} else {
				Sp.edit().putString("sort", "create").commit();
				SketchwareUtil.sortListMap(notesListMap, "createTime", false, false);
			}
			adapter = new MainRecyclerAdapter(notesListMap);
			mainRecycler.setAdapter(adapter);
			if (notesListMap.size() == 0) {
				emptyLottie.setVisibility(View.VISIBLE);
			} else {
				emptyLottie.setVisibility(View.GONE);
			}
		}catch(Exception e){
			
		}
	}
	
	
	public void _GetCategoryNamesFromSQLite() {
		try{
			categoryMap = Database_retrieveOne("categoryNames", "NoCategory, Category1, Category2, Category3, Category4, Category5, Category6, Category7, Category8".split(", "), "id" + " = ?", new String[]{"1"});
			categoryAllButton.setText(getString(R.string.all));
			if (!categoryMap.get("Category1").toString().equals("")) {
				category1Button.setText(categoryMap.get("Category1").toString());
			} else {
				category1Button.setText(getString(R.string.unnamed).concat(" 1"));
			}
			if (!categoryMap.get("Category2").toString().equals("")) {
				category2Button.setText(categoryMap.get("Category2").toString());
			} else {
				category2Button.setText(getString(R.string.unnamed).concat(" 2"));
			}
			if (!categoryMap.get("Category3").toString().equals("")) {
				category3Button.setText(categoryMap.get("Category3").toString());
			} else {
				category3Button.setText(getString(R.string.unnamed).concat("  3"));
			}
			if (!categoryMap.get("Category4").toString().equals("")) {
				category4Button.setText(categoryMap.get("Category4").toString());
			} else {
				category4Button.setText(getString(R.string.unnamed).concat(" 4"));
			}
			if (!categoryMap.get("Category5").toString().equals("")) {
				category5Button.setText(categoryMap.get("Category5").toString());
			} else {
				category5Button.setText(getString(R.string.unnamed).concat(" 5"));
			}
			if (!categoryMap.get("Category6").toString().equals("")) {
				category6Button.setText(categoryMap.get("Category6").toString());
			} else {
				category6Button.setText(getString(R.string.unnamed).concat(" 6"));
			}
			if (!categoryMap.get("Category7").toString().equals("")) {
				category7Button.setText(categoryMap.get("Category7").toString());
			} else {
				category7Button.setText(getString(R.string.unnamed).concat(" 7"));
			}
			if (!categoryMap.get("Category8").toString().equals("")) {
				category8Button.setText(categoryMap.get("Category8").toString());
			} else {
				category8Button.setText(getString(R.string.unnamed).concat(" 8"));
			}
		}catch(Exception e){
			
		}
	}
	
	
	public void _AutoSaveLoad() {
		if (Sp.contains("autoSaveNote")) {
			intent.setClass(getApplicationContext(), NoteeditActivity.class);
			intent.putExtra("type", "autoSave");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
	
	
	public void _SetCurrentCategoryClick(final String _Category) {
		switch(_Category) {
			case "NoCategory": {
				categoryAllButton.setTextSize((int)18);
				category1Button.setTextSize((int)14);
				category2Button.setTextSize((int)14);
				category3Button.setTextSize((int)14);
				category4Button.setTextSize((int)14);
				category5Button.setTextSize((int)14);
				category6Button.setTextSize((int)14);
				category7Button.setTextSize((int)14);
				category8Button.setTextSize((int)14);
				break;
			}
			case "Category1": {
				categoryAllButton.setTextSize((int)14);
				category1Button.setTextSize((int)18);
				category2Button.setTextSize((int)14);
				category3Button.setTextSize((int)14);
				category4Button.setTextSize((int)14);
				category5Button.setTextSize((int)14);
				category6Button.setTextSize((int)14);
				category7Button.setTextSize((int)14);
				category8Button.setTextSize((int)14);
				break;
			}
			case "Category2": {
				categoryAllButton.setTextSize((int)14);
				category1Button.setTextSize((int)14);
				category2Button.setTextSize((int)18);
				category3Button.setTextSize((int)14);
				category4Button.setTextSize((int)14);
				category5Button.setTextSize((int)14);
				category6Button.setTextSize((int)14);
				category7Button.setTextSize((int)14);
				category8Button.setTextSize((int)14);
				break;
			}
			case "Category3": {
				categoryAllButton.setTextSize((int)14);
				category1Button.setTextSize((int)14);
				category2Button.setTextSize((int)14);
				category3Button.setTextSize((int)18);
				category4Button.setTextSize((int)14);
				category5Button.setTextSize((int)14);
				category6Button.setTextSize((int)14);
				category7Button.setTextSize((int)14);
				category8Button.setTextSize((int)14);
				break;
			}
			case "Category4": {
				categoryAllButton.setTextSize((int)14);
				category1Button.setTextSize((int)14);
				category2Button.setTextSize((int)14);
				category3Button.setTextSize((int)14);
				category4Button.setTextSize((int)18);
				category5Button.setTextSize((int)14);
				category6Button.setTextSize((int)14);
				category7Button.setTextSize((int)14);
				category8Button.setTextSize((int)14);
				break;
			}
			case "Category5": {
				categoryAllButton.setTextSize((int)14);
				category1Button.setTextSize((int)14);
				category2Button.setTextSize((int)14);
				category3Button.setTextSize((int)14);
				category4Button.setTextSize((int)14);
				category5Button.setTextSize((int)18);
				category6Button.setTextSize((int)14);
				category7Button.setTextSize((int)14);
				category8Button.setTextSize((int)14);
				break;
			}
			case "Category6": {
				categoryAllButton.setTextSize((int)14);
				category1Button.setTextSize((int)14);
				category2Button.setTextSize((int)14);
				category3Button.setTextSize((int)14);
				category4Button.setTextSize((int)14);
				category5Button.setTextSize((int)14);
				category6Button.setTextSize((int)18);
				category7Button.setTextSize((int)14);
				category8Button.setTextSize((int)14);
				break;
			}
			case "Category7": {
				categoryAllButton.setTextSize((int)14);
				category1Button.setTextSize((int)14);
				category2Button.setTextSize((int)14);
				category3Button.setTextSize((int)14);
				category4Button.setTextSize((int)14);
				category5Button.setTextSize((int)14);
				category6Button.setTextSize((int)14);
				category7Button.setTextSize((int)18);
				category8Button.setTextSize((int)14);
				break;
			}
			case "Category8": {
				categoryAllButton.setTextSize((int)14);
				category1Button.setTextSize((int)14);
				category2Button.setTextSize((int)14);
				category3Button.setTextSize((int)14);
				category4Button.setTextSize((int)14);
				category5Button.setTextSize((int)14);
				category6Button.setTextSize((int)14);
				category7Button.setTextSize((int)14);
				category8Button.setTextSize((int)18);
				break;
			}
		}
	}
	
	
	public void _SetLayoutManager() {
		if (Sp.contains("recyclerMode")) {
			switch(Sp.getString("recyclerMode", "")) {
				case "normal": {
					LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
					mainRecycler.setLayoutManager(layoutManager);
					layoutImage.setImageResource(R.drawable.view_agenda_72dp_black);
					break;
				}
				case "staggered": {
					androidx.recyclerview.widget.StaggeredGridLayoutManager slmmainRecycler = new androidx.recyclerview.widget.StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
					mainRecycler.setLayoutManager(slmmainRecycler);
					layoutImage.setImageResource(R.drawable.grid_view_72dp_black);
					break;
				}
			}
		} else {
			LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
			mainRecycler.setLayoutManager(layoutManager);
			Sp.edit().putString("recyclerMode", "normal").commit();
			layoutImage.setImageResource(R.drawable.view_agenda_72dp_black);
		}
	}
	
	
	public void _RecyclerSwitchRippleCategory(final View _view, final double _position, final ArrayList<HashMap<String, Object>> _listmap) {
		switch(_listmap.get((int)_position).get("category").toString()) {
			default: {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					// Arka plan: Şeffaf ya da dışarıdan verilen
					android.graphics.drawable.GradientDrawable _viewBG = new android.graphics.drawable.GradientDrawable();
					_viewBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
					_viewBG.setCornerRadius((float)30);
					_viewBG.setStroke((int)5, 0xFFF44336);
					_view.setBackground(_viewBG); // sadece arka plan
					
					// Ripple maskesi: sadece ripple efekti için görünmez tabaka
					android.graphics.drawable.GradientDrawable _viewMask = new android.graphics.drawable.GradientDrawable();
					_viewMask.setCornerRadius((float)30);
					_viewMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
					
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
					),
					null, // foreground ripple
					_viewMask // mask
					);
					
					_view.setForeground(_viewRE);
					_view.setClickable(true);
					_view.setFocusable(true);
				} else {
					// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
					android.graphics.drawable.GradientDrawable _viewGG = new android.graphics.drawable.GradientDrawable();
					_viewGG.setColor(Color.TRANSPARENT);
					_viewGG.setCornerRadius((float)30);
					_viewGG.setStroke((int) 5, 0xFFF44336);
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)}
					),
					_viewGG,
					null
					);
					_view.setBackground(_viewRE);
				}
				break;
			}
			case "Category1": {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					// Arka plan: Şeffaf ya da dışarıdan verilen
					android.graphics.drawable.GradientDrawable _viewBG = new android.graphics.drawable.GradientDrawable();
					_viewBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
					_viewBG.setCornerRadius((float)30);
					_viewBG.setStroke((int)5, 0xFF2196F3);
					_view.setBackground(_viewBG); // sadece arka plan
					
					// Ripple maskesi: sadece ripple efekti için görünmez tabaka
					android.graphics.drawable.GradientDrawable _viewMask = new android.graphics.drawable.GradientDrawable();
					_viewMask.setCornerRadius((float)30);
					_viewMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
					
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
					),
					null, // foreground ripple
					_viewMask // mask
					);
					
					_view.setForeground(_viewRE);
					_view.setClickable(true);
					_view.setFocusable(true);
				} else {
					// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
					android.graphics.drawable.GradientDrawable _viewGG = new android.graphics.drawable.GradientDrawable();
					_viewGG.setColor(Color.TRANSPARENT);
					_viewGG.setCornerRadius((float)30);
					_viewGG.setStroke((int) 5, 0xFF2196F3);
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)}
					),
					_viewGG,
					null
					);
					_view.setBackground(_viewRE);
				}
				break;
			}
			case "Category2": {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					// Arka plan: Şeffaf ya da dışarıdan verilen
					android.graphics.drawable.GradientDrawable _viewBG = new android.graphics.drawable.GradientDrawable();
					_viewBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
					_viewBG.setCornerRadius((float)30);
					_viewBG.setStroke((int)5, 0xFF8BC34A);
					_view.setBackground(_viewBG); // sadece arka plan
					
					// Ripple maskesi: sadece ripple efekti için görünmez tabaka
					android.graphics.drawable.GradientDrawable _viewMask = new android.graphics.drawable.GradientDrawable();
					_viewMask.setCornerRadius((float)30);
					_viewMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
					
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
					),
					null, // foreground ripple
					_viewMask // mask
					);
					
					_view.setForeground(_viewRE);
					_view.setClickable(true);
					_view.setFocusable(true);
				} else {
					// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
					android.graphics.drawable.GradientDrawable _viewGG = new android.graphics.drawable.GradientDrawable();
					_viewGG.setColor(Color.TRANSPARENT);
					_viewGG.setCornerRadius((float)30);
					_viewGG.setStroke((int) 5, 0xFF8BC34A);
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)}
					),
					_viewGG,
					null
					);
					_view.setBackground(_viewRE);
				}
				break;
			}
			case "Category3": {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					// Arka plan: Şeffaf ya da dışarıdan verilen
					android.graphics.drawable.GradientDrawable _viewBG = new android.graphics.drawable.GradientDrawable();
					_viewBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
					_viewBG.setCornerRadius((float)30);
					_viewBG.setStroke((int)5, 0xFFFFEB3B);
					_view.setBackground(_viewBG); // sadece arka plan
					
					// Ripple maskesi: sadece ripple efekti için görünmez tabaka
					android.graphics.drawable.GradientDrawable _viewMask = new android.graphics.drawable.GradientDrawable();
					_viewMask.setCornerRadius((float)30);
					_viewMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
					
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
					),
					null, // foreground ripple
					_viewMask // mask
					);
					
					_view.setForeground(_viewRE);
					_view.setClickable(true);
					_view.setFocusable(true);
				} else {
					// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
					android.graphics.drawable.GradientDrawable _viewGG = new android.graphics.drawable.GradientDrawable();
					_viewGG.setColor(Color.TRANSPARENT);
					_viewGG.setCornerRadius((float)30);
					_viewGG.setStroke((int) 5, 0xFFFFEB3B);
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)}
					),
					_viewGG,
					null
					);
					_view.setBackground(_viewRE);
				}
				break;
			}
			case "Category4": {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					// Arka plan: Şeffaf ya da dışarıdan verilen
					android.graphics.drawable.GradientDrawable _viewBG = new android.graphics.drawable.GradientDrawable();
					_viewBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
					_viewBG.setCornerRadius((float)30);
					_viewBG.setStroke((int)5, 0xFFFF9800);
					_view.setBackground(_viewBG); // sadece arka plan
					
					// Ripple maskesi: sadece ripple efekti için görünmez tabaka
					android.graphics.drawable.GradientDrawable _viewMask = new android.graphics.drawable.GradientDrawable();
					_viewMask.setCornerRadius((float)30);
					_viewMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
					
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
					),
					null, // foreground ripple
					_viewMask // mask
					);
					
					_view.setForeground(_viewRE);
					_view.setClickable(true);
					_view.setFocusable(true);
				} else {
					// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
					android.graphics.drawable.GradientDrawable _viewGG = new android.graphics.drawable.GradientDrawable();
					_viewGG.setColor(Color.TRANSPARENT);
					_viewGG.setCornerRadius((float)30);
					_viewGG.setStroke((int) 5, 0xFFFF9800);
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)}
					),
					_viewGG,
					null
					);
					_view.setBackground(_viewRE);
				}
				break;
			}
			case "Category5": {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					// Arka plan: Şeffaf ya da dışarıdan verilen
					android.graphics.drawable.GradientDrawable _viewBG = new android.graphics.drawable.GradientDrawable();
					_viewBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
					_viewBG.setCornerRadius((float)30);
					_viewBG.setStroke((int)5, 0xFF673AB7);
					_view.setBackground(_viewBG); // sadece arka plan
					
					// Ripple maskesi: sadece ripple efekti için görünmez tabaka
					android.graphics.drawable.GradientDrawable _viewMask = new android.graphics.drawable.GradientDrawable();
					_viewMask.setCornerRadius((float)30);
					_viewMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
					
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
					),
					null, // foreground ripple
					_viewMask // mask
					);
					
					_view.setForeground(_viewRE);
					_view.setClickable(true);
					_view.setFocusable(true);
				} else {
					// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
					android.graphics.drawable.GradientDrawable _viewGG = new android.graphics.drawable.GradientDrawable();
					_viewGG.setColor(Color.TRANSPARENT);
					_viewGG.setCornerRadius((float)30);
					_viewGG.setStroke((int) 5, 0xFF673AB7);
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)}
					),
					_viewGG,
					null
					);
					_view.setBackground(_viewRE);
				}
				break;
			}
			case "Category6": {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					// Arka plan: Şeffaf ya da dışarıdan verilen
					android.graphics.drawable.GradientDrawable _viewBG = new android.graphics.drawable.GradientDrawable();
					_viewBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
					_viewBG.setCornerRadius((float)30);
					_viewBG.setStroke((int)5, 0xFF00BCD4);
					_view.setBackground(_viewBG); // sadece arka plan
					
					// Ripple maskesi: sadece ripple efekti için görünmez tabaka
					android.graphics.drawable.GradientDrawable _viewMask = new android.graphics.drawable.GradientDrawable();
					_viewMask.setCornerRadius((float)30);
					_viewMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
					
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
					),
					null, // foreground ripple
					_viewMask // mask
					);
					
					_view.setForeground(_viewRE);
					_view.setClickable(true);
					_view.setFocusable(true);
				} else {
					// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
					android.graphics.drawable.GradientDrawable _viewGG = new android.graphics.drawable.GradientDrawable();
					_viewGG.setColor(Color.TRANSPARENT);
					_viewGG.setCornerRadius((float)30);
					_viewGG.setStroke((int) 5, 0xFF00BCD4);
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)}
					),
					_viewGG,
					null
					);
					_view.setBackground(_viewRE);
				}
				break;
			}
			case "Category7": {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					// Arka plan: Şeffaf ya da dışarıdan verilen
					android.graphics.drawable.GradientDrawable _viewBG = new android.graphics.drawable.GradientDrawable();
					_viewBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
					_viewBG.setCornerRadius((float)30);
					_viewBG.setStroke((int)5, 0xFFCDDC39);
					_view.setBackground(_viewBG); // sadece arka plan
					
					// Ripple maskesi: sadece ripple efekti için görünmez tabaka
					android.graphics.drawable.GradientDrawable _viewMask = new android.graphics.drawable.GradientDrawable();
					_viewMask.setCornerRadius((float)30);
					_viewMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
					
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
					),
					null, // foreground ripple
					_viewMask // mask
					);
					
					_view.setForeground(_viewRE);
					_view.setClickable(true);
					_view.setFocusable(true);
				} else {
					// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
					android.graphics.drawable.GradientDrawable _viewGG = new android.graphics.drawable.GradientDrawable();
					_viewGG.setColor(Color.TRANSPARENT);
					_viewGG.setCornerRadius((float)30);
					_viewGG.setStroke((int) 5, 0xFFCDDC39);
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)}
					),
					_viewGG,
					null
					);
					_view.setBackground(_viewRE);
				}
				break;
			}
			case "Category8": {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					// Arka plan: Şeffaf ya da dışarıdan verilen
					android.graphics.drawable.GradientDrawable _viewBG = new android.graphics.drawable.GradientDrawable();
					_viewBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
					_viewBG.setCornerRadius((float)30);
					_viewBG.setStroke((int)5, 0xFF009688);
					_view.setBackground(_viewBG); // sadece arka plan
					
					// Ripple maskesi: sadece ripple efekti için görünmez tabaka
					android.graphics.drawable.GradientDrawable _viewMask = new android.graphics.drawable.GradientDrawable();
					_viewMask.setCornerRadius((float)30);
					_viewMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
					
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
					),
					null, // foreground ripple
					_viewMask // mask
					);
					
					_view.setForeground(_viewRE);
					_view.setClickable(true);
					_view.setFocusable(true);
				} else {
					// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
					android.graphics.drawable.GradientDrawable _viewGG = new android.graphics.drawable.GradientDrawable();
					_viewGG.setColor(Color.TRANSPARENT);
					_viewGG.setCornerRadius((float)30);
					_viewGG.setStroke((int) 5, 0xFF009688);
					android.graphics.drawable.RippleDrawable _viewRE = new android.graphics.drawable.RippleDrawable(
					new android.content.res.ColorStateList(
					new int[][]{new int[]{}},
					new int[]{getResources().getColor(R.color.colorControlHighlight)}
					),
					_viewGG,
					null
					);
					_view.setBackground(_viewRE);
				}
				break;
			}
		}
	}
	
	
	public void _drawerAnim() {
		_drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
			
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				
				float scale = 1f + (slideOffset * 0.1f);
				
				// Sol üst sabit kalsın
				mainBg.setPivotX(0f);
				mainBg.setPivotY(0f);
				
				// Büyüme
				mainBg.setScaleX(scale);
				mainBg.setScaleY(scale);
			}
			
			@Override public void onDrawerOpened(View drawerView) {}
			@Override public void onDrawerClosed(View drawerView) {}
			@Override public void onDrawerStateChanged(int newState) {}
		});
	}
	
	public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public MainRecyclerAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.noterecylerview, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final LinearLayout mainbg = _view.findViewById(R.id.mainbg);
			final LinearLayout upbg = _view.findViewById(R.id.upbg);
			final LinearLayout divider = _view.findViewById(R.id.divider);
			final LinearLayout downbg = _view.findViewById(R.id.downbg);
			final LinearLayout notebg = _view.findViewById(R.id.notebg);
			final ImageView favimage = _view.findViewById(R.id.favimage);
			final TextView title = _view.findViewById(R.id.title);
			final TextView note = _view.findViewById(R.id.note);
			final ImageView timeimage = _view.findViewById(R.id.timeimage);
			final TextView timetext = _view.findViewById(R.id.timetext);
			final TextView daytext = _view.findViewById(R.id.daytext);
			final TextView datetext = _view.findViewById(R.id.datetext);
			final ImageView dateimage = _view.findViewById(R.id.dateimage);
			
			if (Sp.getString("hideTime", "").equals("1")) {
				if (downbg.getVisibility() == View.VISIBLE) {
					downbg.setVisibility(View.GONE);
					divider.setVisibility(View.INVISIBLE);
				}
			} else {
				if (downbg.getVisibility() == View.GONE) {
					downbg.setVisibility(View.VISIBLE);
					divider.setVisibility(View.VISIBLE);
				}
			}
			if (_data.get((int)_position).containsKey("title")) {
				if (!_data.get((int)_position).get("title").toString().equals("")) {
					title.setText(_data.get((int)_position).get("title").toString());
				} else {
					title.setVisibility(View.GONE);
				}
			}
			if (_data.get((int)_position).containsKey("note")) {
				if (!_data.get((int)_position).get("note").toString().equals("")) {
					note.setText(_data.get((int)_position).get("note").toString());
				} else {
					
				}
			}
			if (_data.get((int)_position).containsKey("editTime") && _data.get((int)_position).containsKey("createTime")) {
				if (_data.get((int)_position).get("editTime").toString().equals("0")) {
					Calendar1.setTimeInMillis((long)(Double.parseDouble(_data.get((int)_position).get("createTime").toString())));
					daytext.setText(new SimpleDateFormat("EEEE").format(Calendar1.getTime()));
					timetext.setText(new SimpleDateFormat("HH:mm").format(Calendar1.getTime()));
					datetext.setText(new SimpleDateFormat("dd.MM.y").format(Calendar1.getTime()));
				} else {
					Calendar1.setTimeInMillis((long)(Double.parseDouble(_data.get((int)_position).get("editTime").toString())));
					daytext.setText(new SimpleDateFormat("EEEE").format(Calendar1.getTime()));
					timetext.setText(new SimpleDateFormat("HH:mm").format(Calendar1.getTime()));
					datetext.setText(new SimpleDateFormat("dd.MM.y").format(Calendar1.getTime()));
				}
			}
			if (Sp.contains("recyclerMode")) {
				if (Sp.getString("recyclerMode", "").equals("staggered")) {
					daytext.setVisibility(View.GONE);
					timeimage.setVisibility(View.GONE);
					dateimage.setVisibility(View.GONE);
				}
			}
			_RecyclerSwitchRippleCategory(mainbg, _position, _data);
			if (_data.get((int)_position).containsKey("isFavorite")) {
				if (_data.get((int)_position).get("isFavorite").toString().equals("1")) {
					favimage.setImageResource(R.drawable.star_72dp_black_filled);
					favimage.setColorFilter(0xFFFFEB3B);
				} else {
					favimage.setImageResource(R.drawable.star_72dp_black);
					favimage.setColorFilter(getResources().getColor(R.color.colorOnPrimary));
				}
				favimage.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						noteValues = _data.get((int)_position);
						if (noteValues.get("isFavorite").toString().equals("1")) {
							favimage.setImageResource(R.drawable.star_72dp_black);
							favimage.setColorFilter(getResources().getColor(R.color.colorOnPrimary));
							noteValues.put("isFavorite", "0");
						} else {
							favimage.setImageResource(R.drawable.star_72dp_black_filled);
							favimage.setColorFilter(0xFFFFEB3B);
							noteValues.put("isFavorite", "1");
						}
						Database_update("notes", noteValues, "id" + " = ?", new String[]{noteValues.get("id").toString()});
					}
				});
			}
			mainbg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					if (selectionMode && searchBg.getVisibility() == View.GONE) {
						if (selectedItems.contains(Double.parseDouble(_data.get((int)_position).get("id").toString()))) {
							selectedItems.remove((int)(selectedItems.indexOf(Double.parseDouble(_data.get((int)_position).get("id").toString()))));
							_RecyclerSwitchRippleCategory(mainbg, _position, _data);
						} else {
							selectedItems.add(Double.valueOf(Double.parseDouble(_data.get((int)_position).get("id").toString())));
							mainbg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)30, getResources().getColor(R.color.colorAccent)));
						}
						if (!(selectedItems.size() == 1)) {
							optionsShare.setVisibility(View.GONE);
							optionsText.setText(String.valueOf((long)(selectedItems.size())).concat(" ".concat(getString(R.string.selected))));
						} else {
							optionsShare.setVisibility(View.VISIBLE);
							optionsText.setText(getString(R.string.options));
						}
						if (selectedItems.size() == 0) {
							selectionMode = false;
							optionsBg.setVisibility(View.GONE);
							showDateAndTimeImage.setVisibility(View.VISIBLE);
							showFavoritesImage.setVisibility(View.VISIBLE);
							layoutImage.setVisibility(View.VISIBLE);
							toolbarSearchImage.setVisibility(View.VISIBLE);
						}
					} else {
						if (searchBg.getVisibility() == View.VISIBLE) {
							toolbarSearchImage.performClick();
						}
						noteValues = _data.get((int)_position);
						intent.setClass(getApplicationContext(), NoteeditActivity.class);
						intent.putExtra("noteMap", new Gson().toJson(noteValues));
						intent.putExtra("type", "edit");
						View _fabt = findViewById(R.id._fab);
						_fabt.setTransitionName("logo");  
						
						
						
						ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
						MainActivity.this,
						_fabt,
						"logo"
						);
						
						startActivity(intent, options.toBundle());
					}
				}
			});
			mainbg.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View _view) {
					if (searchBg.getVisibility() == View.GONE) {
						if (selectionMode) {
							if (selectedItems.contains(Double.parseDouble(_data.get((int)_position).get("id").toString()))) {
								selectedItems.remove((int)(selectedItems.indexOf(Double.parseDouble(_data.get((int)_position).get("id").toString()))));
								_RecyclerSwitchRippleCategory(mainbg, _position, _data);
							} else {
								selectedItems.add(Double.valueOf(Double.parseDouble(_data.get((int)_position).get("id").toString())));
								mainbg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)30, getResources().getColor(R.color.colorAccent)));
							}
						} else {
							selectionMode = true;
							selectedItems.clear();
							selectedItems.add(Double.valueOf(Double.parseDouble(_data.get((int)_position).get("id").toString())));
							mainbg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)30, getResources().getColor(R.color.colorAccent)));
							optionsBg.setVisibility(View.VISIBLE);
							optionsShare.setVisibility(View.VISIBLE);
							showDateAndTimeImage.setVisibility(View.GONE);
							showFavoritesImage.setVisibility(View.GONE);
							layoutImage.setVisibility(View.GONE);
							toolbarSearchImage.setVisibility(View.GONE);
						}
						if (!(selectedItems.size() == 1)) {
							optionsShare.setVisibility(View.GONE);
							optionsText.setText(String.valueOf((long)(selectedItems.size())).concat(" ".concat(getString(R.string.selected))));
						} else {
							optionsShare.setVisibility(View.VISIBLE);
							optionsText.setText(getString(R.string.options));
						}
						if (selectedItems.size() == 0) {
							selectionMode = false;
							optionsBg.setVisibility(View.GONE);
							showDateAndTimeImage.setVisibility(View.VISIBLE);
							showFavoritesImage.setVisibility(View.VISIBLE);
							layoutImage.setVisibility(View.VISIBLE);
							toolbarSearchImage.setVisibility(View.VISIBLE);
						}
					}
					return true;
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