package com.QuickNotes.Dev;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.*;
import androidx.core.widget.NestedScrollView;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import eightbitlab.com.blurview.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class CalendarActivity extends AppCompatActivity {
	
	private HashMap<String, Object> noteValues = new HashMap<>();
	private String day = "";
	
	private ArrayList<HashMap<String, Object>> trashNotesListMap = new ArrayList<>();
	
	private LinearLayout toolbarBg;
	private NestedScrollView mainScroll;
	private AdView adview1;
	private ImageView toolbarBackImage;
	private LinearLayout toolbarTextBg;
	private ImageView toolbarCurrentDayImage;
	private TextView toolbarTitle;
	private TextView toolbarSubtitle;
	private LinearLayout mainBg;
	private CalendarView mainCalendar;
	private RecyclerView noteRecycler;
	
	private SharedPreferences Sp;
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
	private Calendar Calendar1 = Calendar.getInstance();
	private Intent intent = new Intent();
	private Calendar Calendar2 = Calendar.getInstance();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.calendar);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		MobileAds.initialize(this);
		
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		toolbarBg = findViewById(R.id.toolbarBg);
		mainScroll = findViewById(R.id.mainScroll);
		adview1 = findViewById(R.id.adview1);
		toolbarBackImage = findViewById(R.id.toolbarBackImage);
		toolbarTextBg = findViewById(R.id.toolbarTextBg);
		toolbarCurrentDayImage = findViewById(R.id.toolbarCurrentDayImage);
		toolbarTitle = findViewById(R.id.toolbarTitle);
		toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
		mainBg = findViewById(R.id.mainBg);
		mainCalendar = findViewById(R.id.mainCalendar);
		noteRecycler = findViewById(R.id.noteRecycler);
		Sp = getSharedPreferences("QuickNotesData", Activity.MODE_PRIVATE);
		
		toolbarBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});
		
		toolbarCurrentDayImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Calendar2 = Calendar.getInstance();
				mainCalendar.setDate((long)(Calendar2.getTimeInMillis()), true, true);
				day = new SimpleDateFormat("DDD").format(Calendar2.getTime());
				noteRecycler.setAdapter(new NoteRecyclerAdapter(trashNotesListMap));
				toolbarSubtitle.setText(new SimpleDateFormat("d MMMM y").format(Calendar2.getTime()));
			}
		});
		
		mainCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView _param1, int _param2, int _param3, int _param4) {
				final int _year = _param2;
				final int _month = _param3;
				final int _day = _param4;
				Calendar2.set(Calendar.YEAR, (int)(_year));
				Calendar2.set(Calendar.MONTH, (int)(_month));
				Calendar2.set(Calendar.DAY_OF_MONTH, (int)(_day));
				day = new SimpleDateFormat("DDD").format(Calendar2.getTime());
				noteRecycler.setAdapter(new NoteRecyclerAdapter(trashNotesListMap));
				toolbarSubtitle.setVisibility(View.VISIBLE);
				toolbarSubtitle.setText(new SimpleDateFormat("d MMMM y").format(Calendar2.getTime()));
			}
		});
	}
	
	private void initializeLogic() {
		Database = SQLiteDatabase.openOrCreateDatabase((CalendarActivity.this).getDatabasePath("QuickNotes" + ".db").getPath(), null);
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
		Calendar2 = Calendar.getInstance();
		day = new SimpleDateFormat("DDD").format(Calendar2.getTime());
		_GetDataFromSQLite();
		_OnCreateRipple();
		{
			AdRequest adRequest = new AdRequest.Builder().build();
			adview1.loadAd(adRequest);
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		_GetDataFromSQLite();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (adview1 != null) {
			adview1.destroy();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (adview1 != null) {
			adview1.pause();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (adview1 != null) {
			adview1.resume();
		}
	}
	public void _GetDataFromSQLite() {
		try{
			trashNotesListMap = Database_retrieveMultiple("notes", "id, title, note, tags, isFavorite, category, state, sortTime, createTime, editTime, trashTime, archiveTime, isReminder, reminderTime".split(", "), "state" + " = ?", new String[]{"Normal"});
			SketchwareUtil.sortListMap(trashNotesListMap, "sortTime", false, false);
			noteRecycler.setAdapter(new NoteRecyclerAdapter(trashNotesListMap));
			noteRecycler.setLayoutManager(new LinearLayoutManager(this));
		}catch(Exception e){
			
		}
	}
	
	
	public void _OnCreateRipple() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable toolbarBackImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarBackImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarBackImageBG.setCornerRadius((float)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f));
			toolbarBackImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarBackImage.setBackground(toolbarBackImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarBackImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarBackImageMask.setCornerRadius((float)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f));
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
			toolbarBackImageGG.setCornerRadius((float)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f));
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
			android.graphics.drawable.GradientDrawable toolbarCurrentDayImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarCurrentDayImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarCurrentDayImageBG.setCornerRadius((float)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f));
			toolbarCurrentDayImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarCurrentDayImage.setBackground(toolbarCurrentDayImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarCurrentDayImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarCurrentDayImageMask.setCornerRadius((float)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f));
			toolbarCurrentDayImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarCurrentDayImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarCurrentDayImageMask // mask
			);
			
			toolbarCurrentDayImage.setForeground(toolbarCurrentDayImageRE);
			toolbarCurrentDayImage.setClickable(true);
			toolbarCurrentDayImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarCurrentDayImageGG = new android.graphics.drawable.GradientDrawable();
			toolbarCurrentDayImageGG.setColor(Color.TRANSPARENT);
			toolbarCurrentDayImageGG.setCornerRadius((float)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f));
			toolbarCurrentDayImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarCurrentDayImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarCurrentDayImageGG,
			null
			);
			toolbarCurrentDayImage.setBackground(toolbarCurrentDayImageRE);
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarBackImage.setTooltipText(getString(R.string.goBack));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarCurrentDayImage.setTooltipText(getString(R.string.goToToday));
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
	
	public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public NoteRecyclerAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
			
			_RecyclerSwitchRippleCategory(mainbg, _position, _data);
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
						Sp.edit().putString("reloadData", "note").commit();
					}
				});
			}
			mainbg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					noteValues = _data.get((int)_position);
					intent.setClass(getApplicationContext(), NoteeditActivity.class);
					intent.putExtra("noteMap", new Gson().toJson(noteValues));
					intent.putExtra("type", "edit");
					startActivity(intent);
				}
			});
			Calendar1.setTimeInMillis((long)(Double.parseDouble(_data.get((int)_position).get("createTime").toString())));
			if (new SimpleDateFormat("DDD").format(Calendar1.getTime()).equals(day)) {
				mainbg.setVisibility(View.VISIBLE);
			} else {
				mainbg.setVisibility(View.GONE);
			}
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