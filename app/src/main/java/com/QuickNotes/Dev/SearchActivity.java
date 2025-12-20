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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class SearchActivity extends AppCompatActivity {
	
	private HashMap<String, Object> noteValues = new HashMap<>();
	private String currentCategory = "";
	SearchRecyclerAdapter adapter;
	
	private ArrayList<HashMap<String, Object>> notesListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> searchListMap = new ArrayList<>();
	
	private LinearLayout toolbarBg;
	private RecyclerView searchRecycler;
	private CalendarView calendarview1;
	private ImageView toolbarBackImage;
	private EditText toolbarSearchEditText;
	private ImageView toolbarClearImage;
	
	private SharedPreferences Sp;
	private Intent intent = new Intent();
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
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.search);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		MobileAds.initialize(this);
		
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		toolbarBg = findViewById(R.id.toolbarBg);
		searchRecycler = findViewById(R.id.searchRecycler);
		calendarview1 = findViewById(R.id.calendarview1);
		toolbarBackImage = findViewById(R.id.toolbarBackImage);
		toolbarSearchEditText = findViewById(R.id.toolbarSearchEditText);
		toolbarClearImage = findViewById(R.id.toolbarClearImage);
		Sp = getSharedPreferences("QuickNotesData", Activity.MODE_PRIVATE);
		
		calendarview1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView _param1, int _param2, int _param3, int _param4) {
				final int _year = _param2;
				final int _month = _param3;
				final int _day = _param4;
				
			}
		});
		
		toolbarBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});
		
		toolbarSearchEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				if (_charSeq.equals("")) {
					_OnCreateGetFromSQLite();
					toolbarClearImage.setVisibility(View.GONE);
				} else {
					try { 
						searchListMap.clear(); 
						if (_charSeq.length() == 0) { 
							searchListMap.addAll(notesListMap); 
						} else { 
							double searchRecyclerval00 = 0; 
							double searchRecyclerval0 = 0; 
							String searchRecyclerval2 = ""; 
							searchRecyclerval0 = notesListMap.size(); 
							searchRecyclerval00 = searchRecyclerval0 - 1; 
							for (int searchRecyclerval1 = 0; searchRecyclerval1 < (int)(searchRecyclerval0); searchRecyclerval1++) { 
								searchRecyclerval2 = notesListMap.get((int)searchRecyclerval00).get("note").toString(); 
								if (searchRecyclerval2.toLowerCase().contains(_charSeq.toLowerCase())) { 
									searchListMap.add(notesListMap.get((int)searchRecyclerval00)); 
								} 
								searchRecyclerval00--; 
							} 
						}
					} catch (Exception e) {}; 
					
					searchRecycler.setAdapter(new SearchRecyclerAdapter(searchListMap)); 
					searchRecycler.setHasFixedSize(true);
					toolbarClearImage.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		toolbarClearImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				toolbarSearchEditText.setText("");
			}
		});
	}
	
	private void initializeLogic() {
		_OnCreateSetSQLite();
		_SetLayoutManager();
		_OnCreateGetFromSQLite();
	}
	
	@Override
	public void onBackPressed() {
		finishAfterTransition();
	}
	public void _SetLayoutManager() {
		if (Sp.contains("recyclerMode")) {
			switch(Sp.getString("recyclerMode", "")) {
				case "normal": {
					LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
					searchRecycler.setLayoutManager(layoutManager);
					break;
				}
				case "staggered": {
					androidx.recyclerview.widget.StaggeredGridLayoutManager slmsearchRecycler = new androidx.recyclerview.widget.StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
					searchRecycler.setLayoutManager(slmsearchRecycler);
					break;
				}
			}
		} else {
			LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
			searchRecycler.setLayoutManager(layoutManager);
			Sp.edit().putString("recyclerMode", "normal").commit();
		}
	}
	
	
	public void _OnCreateSetSQLite() {
		Database = SQLiteDatabase.openOrCreateDatabase((SearchActivity.this).getDatabasePath("QuickNotes" + ".db").getPath(), null);
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
			notesListMap = Database_retrieveMultiple("notes", "id, title, note, tags, isFavorite, category, state, sortTime, createTime, editTime, trashTime, archiveTime, isReminder, reminderTime".split(", "), "state" + " = ?", new String[]{"Normal"});
			SketchwareUtil.sortListMap(notesListMap, "sortTime", false, false);
			adapter = new SearchRecyclerAdapter(notesListMap);
			searchRecycler.setAdapter(adapter);
		}catch(Exception e){
			
		}
	}
	
	public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public SearchRecyclerAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
					noteValues = _data.get((int)_position);
					intent.setClass(getApplicationContext(), NoteeditActivity.class);
					intent.putExtra("noteMap", new Gson().toJson(noteValues));
					intent.putExtra("type", "edit");
					startActivity(intent);
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