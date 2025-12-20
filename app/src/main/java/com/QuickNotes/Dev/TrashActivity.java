package com.QuickNotes.Dev;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
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
import android.os.*;
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
import androidx.core.widget.NestedScrollView;
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
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import eightbitlab.com.blurview.*;
import java.io.*;
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import androidx.transition.TransitionManager;
import androidx.transition.AutoTransition;

public class TrashActivity extends AppCompatActivity {
	
	private HashMap<String, Object> noteMap = new HashMap<>();
	private HashMap<String, Object> noteValues = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> notesListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> trashNotesListMap = new ArrayList<>();
	
	private LinearLayout toolbarBg;
	private NestedScrollView mainScroll;
	private ImageView toolbarBackImage;
	private LinearLayout toolbarTextBg;
	private ImageView toolbarClearTrashImage;
	private TextView toolbarTitle;
	private TextView toolbarSubtitle;
	private LinearLayout mainBg;
	private LinearLayout trashTitleBg;
	private TextView trashInfoText;
	private LinearLayout divider;
	private RecyclerView mainRecycler;
	private LottieAnimationView emptyLottie;
	private TextView trashTitleText;
	
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
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.trash);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		MobileAds.initialize(this);
		
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		toolbarBg = findViewById(R.id.toolbarBg);
		mainScroll = findViewById(R.id.mainScroll);
		toolbarBackImage = findViewById(R.id.toolbarBackImage);
		toolbarTextBg = findViewById(R.id.toolbarTextBg);
		toolbarClearTrashImage = findViewById(R.id.toolbarClearTrashImage);
		toolbarTitle = findViewById(R.id.toolbarTitle);
		toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
		mainBg = findViewById(R.id.mainBg);
		trashTitleBg = findViewById(R.id.trashTitleBg);
		trashInfoText = findViewById(R.id.trashInfoText);
		divider = findViewById(R.id.divider);
		mainRecycler = findViewById(R.id.mainRecycler);
		emptyLottie = findViewById(R.id.emptyLottie);
		trashTitleText = findViewById(R.id.trashTitleText);
		Sp = getSharedPreferences("QuickNotesData", Activity.MODE_PRIVATE);
		
		toolbarBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});
		
		toolbarClearTrashImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final AlertDialog dialog = new AlertDialog.Builder(TrashActivity.this).create();
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
				titleAlertDialogText.setText(getString(R.string.clearTrash));
				final TextView infoAlertDialogText = (TextView) inflate.findViewById(R.id.infoAlertDialogText);
				infoAlertDialogText.setText(getString(R.string.doYouWantToClearTrashFolderThisOperationCantBeUndone));
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
				okAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
						final Handler wait = new Handler(Looper.getMainLooper());
						final Runnable rwait  = new Runnable() {
							@Override
							public void run() {
								Database.delete("notes", "state" + " = ?", new String[]{"Trash"});
								_GetDataFromSQLite();
								SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.allTrashedNotesDeleted));
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
	}
	
	private void initializeLogic() {
		Database = SQLiteDatabase.openOrCreateDatabase((TrashActivity.this).getDatabasePath("QuickNotes" + ".db").getPath(), null);
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
		_GetDataFromSQLite();
		_RippleAndTooltips();
	}
	
	public void _GetDataFromSQLite() {
		try{
			trashNotesListMap = Database_retrieveMultiple("notes", "id, title, note, tags, isFavorite, category, state, sortTime, createTime, editTime, trashTime, archiveTime, isReminder, reminderTime".split(", "), "state" + " = ?", new String[]{"Trash"});
			SketchwareUtil.sortListMap(trashNotesListMap, "trashTime", false, false);
			mainRecycler.setAdapter(new MainRecyclerAdapter(trashNotesListMap));
			if (trashNotesListMap.size() == 0) {
				emptyLottie.setVisibility(View.VISIBLE);
			} else {
				emptyLottie.setVisibility(View.GONE);
			}
		}catch(Exception e){
			
		}
	}
	
	
	public void _TransitionManager(final View _view, final double _duration) {
		LinearLayout viewgroup =(LinearLayout) _view;
		
		android.transition.AutoTransition autoTransition = new android.transition.AutoTransition(); autoTransition.setDuration((long)_duration); android.transition.TransitionManager.beginDelayedTransition(viewgroup, autoTransition);
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
			android.graphics.drawable.GradientDrawable toolbarClearTrashImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarClearTrashImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarClearTrashImageBG.setCornerRadius((float)50);
			toolbarClearTrashImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarClearTrashImage.setBackground(toolbarClearTrashImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarClearTrashImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarClearTrashImageMask.setCornerRadius((float)50);
			toolbarClearTrashImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarClearTrashImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarClearTrashImageMask // mask
			);
			
			toolbarClearTrashImage.setForeground(toolbarClearTrashImageRE);
			toolbarClearTrashImage.setClickable(true);
			toolbarClearTrashImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarClearTrashImageGG = new android.graphics.drawable.GradientDrawable();
			toolbarClearTrashImageGG.setColor(Color.TRANSPARENT);
			toolbarClearTrashImageGG.setCornerRadius((float)50);
			toolbarClearTrashImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarClearTrashImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarClearTrashImageGG,
			null
			);
			toolbarClearTrashImage.setBackground(toolbarClearTrashImageRE);
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarBackImage.setTooltipText(getString(R.string.goBack));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarClearTrashImage.setTooltipText(getString(R.string.clearTrash));
		}
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
			
			downbg.setVisibility(View.VISIBLE);
			divider.setVisibility(View.VISIBLE);
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
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				// Arka plan: Şeffaf ya da dışarıdan verilen
				android.graphics.drawable.GradientDrawable mainbgBG = new android.graphics.drawable.GradientDrawable();
				mainbgBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
				mainbgBG.setCornerRadius((float)30);
				mainbgBG.setStroke((int)5, getResources().getColor(R.color.colorPrimaryHint));
				mainbg.setBackground(mainbgBG); // sadece arka plan
				
				// Ripple maskesi: sadece ripple efekti için görünmez tabaka
				android.graphics.drawable.GradientDrawable mainbgMask = new android.graphics.drawable.GradientDrawable();
				mainbgMask.setCornerRadius((float)30);
				mainbgMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
				
				android.graphics.drawable.RippleDrawable mainbgRE = new android.graphics.drawable.RippleDrawable(
				new android.content.res.ColorStateList(
				new int[][]{new int[]{}},
				new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
				),
				null, // foreground ripple
				mainbgMask // mask
				);
				
				mainbg.setForeground(mainbgRE);
				mainbg.setClickable(true);
				mainbg.setFocusable(true);
			} else {
				// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
				android.graphics.drawable.GradientDrawable mainbgGG = new android.graphics.drawable.GradientDrawable();
				mainbgGG.setColor(Color.TRANSPARENT);
				mainbgGG.setCornerRadius((float)30);
				mainbgGG.setStroke((int) 5, getResources().getColor(R.color.colorPrimaryHint));
				android.graphics.drawable.RippleDrawable mainbgRE = new android.graphics.drawable.RippleDrawable(
				new android.content.res.ColorStateList(
				new int[][]{new int[]{}},
				new int[]{getResources().getColor(R.color.colorControlHighlight)}
				),
				mainbgGG,
				null
				);
				mainbg.setBackground(mainbgRE);
			}
			if (_data.get((int)_position).containsKey("isFavorite")) {
				if (_data.get((int)_position).get("isFavorite").toString().equals("1")) {
					favimage.setImageResource(R.drawable.star_72dp_black_filled);
					favimage.setColorFilter(0xFFFFEB3B);
				} else {
					favimage.setImageResource(R.drawable.star_72dp_black);
					favimage.setColorFilter(getResources().getColor(R.color.colorOnPrimary));
				}
			}
			mainbg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					final AlertDialog dialog = new AlertDialog.Builder(TrashActivity.this).create();
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
					titleAlertDialogText.setText(getString(R.string.restore));
					final TextView infoAlertDialogText = (TextView) inflate.findViewById(R.id.infoAlertDialogText);
					infoAlertDialogText.setText(getString(R.string.doYouWantToDeleteThisNoteOrRestoreItFromTrash));
					final Button cancelAlertDialogButton = (Button) inflate.findViewById(R.id.cancelAlertDialogButton);
					final Button neutralAlertDialogButton = (Button) inflate.findViewById(R.id.neutralAlertDialogButton);
					neutralAlertDialogButton.setVisibility(View.VISIBLE);
					neutralAlertDialogButton.setText(getString(R.string.restore));
					final Button okAlertDialogButton = (Button) inflate.findViewById(R.id.okAlertDialogButton);
					okAlertDialogButton.setText(getString(R.string.delete));
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
							final Handler wait2 = new Handler(Looper.getMainLooper());
							final Runnable rwait2  = new Runnable() {
								@Override
								public void run() {
									noteValues = _data.get((int)_position);
									noteValues.put("state", "Normal");
									noteValues.put("trashTime", "0");
									Database_update("notes", noteValues, "id" + " = ?", new String[]{noteValues.get("id").toString()});
									_GetDataFromSQLite();
									Sp.edit().putString("reloadData", "note").commit();;
								}
							};
							wait2.postDelayed(rwait2, 50);
							dialog.dismiss();
						}
					});
					okAlertDialogButton.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
							final Handler wait3 = new Handler(Looper.getMainLooper());
							final Runnable rwait3  = new Runnable() {
								@Override
								public void run() {
									noteValues = _data.get((int)_position);
									Database.delete("notes", "id" + " = ?", new String[]{noteValues.get("id").toString()});
									mainbg.setVisibility(View.GONE);
									_GetDataFromSQLite();
									Sp.edit().putString("reloadData", "note").commit();;
								}
							};
							wait3.postDelayed(rwait3, 50);
							dialog.dismiss();
						}
					});
					//dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
					dialog.getWindow().setDimAmount(0.2f);
					dialog.show();
				}
			});
			mainbg.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View _view) {
					mainbg.performClick();
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