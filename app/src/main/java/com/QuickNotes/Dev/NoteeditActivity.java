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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.*;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.getkeepsafe.taptargetview.*;
import com.google.android.gms.*;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eightbitlab.com.blurview.*;
import java.io.*;
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

public class NoteeditActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private FloatingActionButton _fab;
	private boolean info_state = false;
	private String type = "";
	private double textSize = 0;
	private double noteId = 0;
	private double editPosition = 0;
	private boolean isFavorite = false;
	private boolean infoHidden = false;
	private String category = "";
	private double whileNumber = 0;
	private boolean foundPosition = false;
	private HashMap<String, Object> editNoteMap = new HashMap<>();
	private double color = 0;
	private HashMap<String, Object> noteValues = new HashMap<>();
	private HashMap<String, Object> categoryMap = new HashMap<>();
	private boolean stopAutoSave = false;
	private HashMap<String, Object> autoSaveMap = new HashMap<>();
	private String favorite = "";
	
	private ArrayList<HashMap<String, Object>> notesListMap = new ArrayList<>();
	private ArrayList<String> categoryNameListString = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> category1ListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> category2ListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> category3ListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> category4ListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> category5ListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> category6ListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> category7ListMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> category8ListMap = new ArrayList<>();
	
	private LinearLayout toolbarBg;
	private NestedScrollView noteScroll;
	private Spinner categorySpinner;
	private LinearLayout bottomBarBg;
	private ImageView toolbarBackImage;
	private LinearLayout toolbarTextBg;
	private TextView toolbarInfoText;
	private ImageView toolbarFavoriteImage;
	private ImageView toolbarOptionsImage;
	private TextView toolbarTitle;
	private TextView toolbarSubtitle;
	private LinearLayout noteBg;
	private EditText titleEditText;
	private EditText noteEditText;
	private ImageView noteSizeImage;
	private ImageView undoImage;
	private ImageView redoImage;
	private TextView dateText;
	
	private SharedPreferences Sp;
	private com.google.android.material.bottomsheet.BottomSheetDialog BottomSheet;
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
	private TimerTask Timer1;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.noteedit);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		MobileAds.initialize(this);
		
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_fab = findViewById(R.id._fab);
		toolbarBg = findViewById(R.id.toolbarBg);
		noteScroll = findViewById(R.id.noteScroll);
		categorySpinner = findViewById(R.id.categorySpinner);
		bottomBarBg = findViewById(R.id.bottomBarBg);
		toolbarBackImage = findViewById(R.id.toolbarBackImage);
		toolbarTextBg = findViewById(R.id.toolbarTextBg);
		toolbarInfoText = findViewById(R.id.toolbarInfoText);
		toolbarFavoriteImage = findViewById(R.id.toolbarFavoriteImage);
		toolbarOptionsImage = findViewById(R.id.toolbarOptionsImage);
		toolbarTitle = findViewById(R.id.toolbarTitle);
		toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
		noteBg = findViewById(R.id.noteBg);
		titleEditText = findViewById(R.id.titleEditText);
		noteEditText = findViewById(R.id.noteEditText);
		noteSizeImage = findViewById(R.id.noteSizeImage);
		undoImage = findViewById(R.id.undoImage);
		redoImage = findViewById(R.id.redoImage);
		dateText = findViewById(R.id.dateText);
		Sp = getSharedPreferences("QuickNotesData", Activity.MODE_PRIVATE);
		
		categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				switch(String.valueOf((long)(_position))) {
					case "0": {
						category = "NoCategory";
						break;
					}
					case "1": {
						category = "Category1";
						break;
					}
					case "2": {
						category = "Category2";
						break;
					}
					case "3": {
						category = "Category3";
						break;
					}
					case "4": {
						category = "Category4";
						break;
					}
					case "5": {
						category = "Category5";
						break;
					}
					case "6": {
						category = "Category6";
						break;
					}
					case "7": {
						category = "Category7";
						break;
					}
					case "8": {
						category = "Category8";
						break;
					}
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		toolbarBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});
		
		toolbarInfoText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (toolbarInfoText.getVisibility() == View.VISIBLE) {
					if (info_state) {
						info_state = false;
					} else {
						info_state = true;
					}
					_Info();
				}
			}
		});
		
		toolbarFavoriteImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (favorite.equals("1")) {
					toolbarFavoriteImage.setImageResource(R.drawable.star_72dp_black);
					toolbarFavoriteImage.setColorFilter(getResources().getColor(R.color.colorOnPrimary));
					favorite = "0";
				} else {
					toolbarFavoriteImage.setImageResource(R.drawable.star_72dp_black_filled);
					toolbarFavoriteImage.setColorFilter(0xFFFFFF00);
					favorite = "1";
				}
			}
		});
		
		toolbarOptionsImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final View popupView = getLayoutInflater().inflate(R.layout.popup_menu, null);
				final PopupWindow Popup = new PopupWindow(popupView, -2, -2, true);
				// View tanımlamaları
				BlurView blurpopup = popupView.findViewById(R.id.blurpopup);
				if (Build.VERSION.SDK_INT > 30) {
					View decorView = getWindow().getDecorView();
					ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
					Drawable windowBackground = decorView.getBackground();
					float Blur1 = ((float)10);
					blurpopup.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
					blurpopup.setClipToOutline(true);
					blurpopup.setupWith(rootView, new RenderEffectBlur())
					.setBlurRadius(Blur1)
					.setFrameClearDrawable(windowBackground) 
					.setBlurAutoUpdate(true);
					
					final Choreographer bluranim = Choreographer.getInstance();
					
					final Choreographer.FrameCallback rbluranim = new Choreographer.FrameCallback() {
						@Override
						public void doFrame(long frameTimeNanos) {
							blurpopup.invalidate();;
							bluranim.postFrameCallback(this); // Sorun burada çözüldü
						}
					};
					
					bluranim.postFrameCallback(rbluranim); // Başlat
				} else {
					blurpopup.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)(int) (50 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f), getResources().getColor(R.color.colorPrimary)));
				}
				// View tanımlamaları
				View bgpopup = popupView.findViewById(R.id.bgpopup);
				// View tanımlamaları
				final TextView text1 = popupView.findViewById(R.id.item1text);
				text1.setText(getString(R.string.share));
				// View tanımlamaları
				View item1 = popupView.findViewById(R.id.item1bg);
				
				// Tıklama olayları
				item1.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (!(titleEditText.getText().toString().equals("") && noteEditText.getText().toString().equals(""))) {
							if (!titleEditText.getText().toString().equals("")) {
								Intent i = new Intent(android.content.Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(android.content.Intent.EXTRA_TEXT, noteEditText.getText().toString()); startActivity(Intent.createChooser(i,"Share"));
							} else {
								Intent i = new Intent(android.content.Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(android.content.Intent.EXTRA_TEXT, titleEditText.getText().toString().concat("\n".concat(noteEditText.getText().toString()))); startActivity(Intent.createChooser(i,"Share"));
							}
						} else {
							SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.noNoteToShare));
						};
						Popup.dismiss();
					}
				});
				// View tanımlamaları
				final TextView text2 = popupView.findViewById(R.id.item2text);
				if (!infoHidden) {
					text2.setText(getString(R.string.hideInfo));
					// View tanımlamaları
					View item2 = popupView.findViewById(R.id.item2bg);
					
					// Tıklama olayları
					item2.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							toolbarInfoText.setVisibility(View.INVISIBLE);
							infoHidden = true;;
							Popup.dismiss();
						}
					});
				} else {
					text2.setText(getString(R.string.showInfo));
					// View tanımlamaları
					View item2 = popupView.findViewById(R.id.item2bg);
					
					// Tıklama olayları
					item2.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							toolbarInfoText.setVisibility(View.VISIBLE);
							infoHidden = false;;
							Popup.dismiss();
						}
					});
				}
				// View tanımlamaları
				final TextView text3 = popupView.findViewById(R.id.item3text);
				switch(type) {
					case "new": {
						text3.setText(getString(R.string.delete));
						// View tanımlamaları
						View item3 = popupView.findViewById(R.id.item3bg);
						
						// Tıklama olayları
						item3.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.deleted));
								finishAfterTransition();;
								Popup.dismiss();
							}
						});
						break;
					}
					case "edit": {
						text3.setText(getString(R.string.trash));
						// View tanımlamaları
						View item3 = popupView.findViewById(R.id.item3bg);
						
						// Tıklama olayları
						item3.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								Calendar1 = Calendar.getInstance();
								noteValues.put("title", titleEditText.getText().toString());
								noteValues.put("note", noteEditText.getText().toString());
								noteValues.put("sortTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
								noteValues.put("editTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
								noteValues.put("state", "Trash");
								noteValues.put("trashTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
								if (favorite.equals("1")) {
									noteValues.put("isFavorite", "1");
								} else {
									noteValues.put("isFavorite", "0");
								}
								noteValues.put("category", category);
								Database_update("notes", noteValues, "id" + " = ?", new String[]{noteValues.get("id").toString()});
								Sp.edit().putString("reloadData", "note").commit();
								finishAfterTransition();;
								Popup.dismiss();
							}
						});
						break;
					}
				}
				// View tanımlamaları
				View item4 = popupView.findViewById(R.id.item4bg);
				item4.setVisibility(View.GONE);
				// View tanımlamaları
				View item5 = popupView.findViewById(R.id.item5bg);
				item5.setVisibility(View.GONE);
				// View tanımlamaları
				View item6 = popupView.findViewById(R.id.item6bg);
				item6.setVisibility(View.GONE);
				// View tanımlamaları
				ImageView image1 = popupView.findViewById(R.id.item1image);
				image1.setImageResource(R.drawable.share_72dp_black);
				// View tanımlamaları
				ImageView image2 = popupView.findViewById(R.id.item2image);
				image2.setImageResource(R.drawable.info_outline_72dp_black);
				// View tanımlamaları
				ImageView image3 = popupView.findViewById(R.id.item3image);
				image3.setImageResource(R.drawable.delete_72dp_black);
				Popup.showAsDropDown(toolbarOptionsImage);
			}
		});
		
		titleEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_Info();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		noteEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_Info();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		noteSizeImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!(titleEditText.getText().toString().equals("") && noteEditText.getText().toString().equals(""))) {
					if (type.equals("edit") || type.equals("archive")) {
						if (favorite.equals(noteValues.get("isFavorite").toString()) && (titleEditText.getText().toString().equals(noteValues.get("title").toString()) && (noteEditText.getText().toString().equals(noteValues.get("note").toString()) && category.equals(noteValues.get("category").toString())))) {
							finishAfterTransition();
						} else {
							_SaveNoteToSQLite();
						}
					} else {
						_SaveNoteToSQLite();
					}
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.noteOrTitleCantBeEmpty));
				}
			}
		});
	}
	
	private void initializeLogic() {
		getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		_Info();
		_EdittextHistory(redoImage, undoImage, noteEditText);
		_RippleAndAnims();
		int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
		if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
			// ACTION WHEN DARK MODE IS ON
			
		} else {
			// ACTION IF DARK MODE IS OFF
			getWindow().getDecorView().setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		};
		
		_OnCreateSetSQLite();
		favorite = "0";
		if (getIntent().hasExtra("type")) {
			switch(getIntent().getStringExtra("type")) {
				case "new": {
					type = "new";
					toolbarTitle.setText(getString(R.string.newNote));
					textSize = 18;
					dateText.setVisibility(View.INVISIBLE);
					category = getIntent().getStringExtra("category");
					break;
				}
				case "ocr": {
					type = "new";
					toolbarTitle.setText(getString(R.string.newNote));
					textSize = 18;
					dateText.setVisibility(View.INVISIBLE);
					category = "NoCategory";
					if (getIntent().hasExtra("text")) {
						noteEditText.setText(getIntent().getStringExtra("text"));
					} else {
						finish();
					}
					break;
				}
				case "tutorial": {
					type = "new";
					toolbarTitle.setText(getString(R.string.newNote));
					textSize = 18;
					dateText.setVisibility(View.INVISIBLE);
					category = getIntent().getStringExtra("category");
					TapTargetSequence sequence = new TapTargetSequence(NoteeditActivity.this)
					.targets(
					TapTarget.forView(_fab, getString(R.string.saveNote), getString(R.string.hereYouCanSaveNoteAndGoHomeScreen))
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
					
					TapTarget.forView(categorySpinner, getString(R.string.category), getString(R.string.selectTheCategoryForYourNoteHere))
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
					
					TapTarget.forView(toolbarFavoriteImage, getString(R.string.favorite), getString(R.string.tapHereToAddThisNoteToYourYavorites))
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
					
					TapTarget.forView(toolbarOptionsImage, getString(R.string.noteMenu), getString(R.string.tapHereToOpenNoteMenu))
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
							SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.tutorialCompleted));
							finish();;
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
				case "autoSave": {
					type = "new";
					toolbarTitle.setText(getString(R.string.newNote));
					dateText.setVisibility(View.INVISIBLE);
					autoSaveMap = new Gson().fromJson(Sp.getString("autoSaveNote", ""), new TypeToken<HashMap<String, Object>>(){}.getType());
					if (autoSaveMap.containsKey("title")) {
						titleEditText.setText(autoSaveMap.get("title").toString());
					}
					if (autoSaveMap.containsKey("note")) {
						noteEditText.setText(autoSaveMap.get("note").toString());
					}
					if (autoSaveMap.containsKey("textSize")) {
						textSize = Double.parseDouble(autoSaveMap.get("textSize").toString());
						noteEditText.setTextSize((int)textSize);
					}
					if (autoSaveMap.containsKey("category")) {
						category = autoSaveMap.get("category").toString();
					}
					if (autoSaveMap.containsKey("isFavorite")) {
						if (autoSaveMap.get("isFavorite").toString().equals("1")) {
							toolbarFavoriteImage.performClick();
						}
					}
					Sp.edit().remove("autoSaveNote").commit();
					SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.theUnfinishedNoteHasbeenRestored));
					break;
				}
				case "edit": {
					type = "edit";
					toolbarTitle.setText(getString(R.string.editNote));
					if (getIntent().hasExtra("noteMap")) {
						noteValues = new Gson().fromJson(getIntent().getStringExtra("noteMap"), new TypeToken<HashMap<String, Object>>(){}.getType());
						if (noteValues.containsKey("title")) {
							titleEditText.setText(noteValues.get("title").toString());
						}
						if (noteValues.containsKey("note")) {
							noteEditText.setText(noteValues.get("note").toString());
						}
						if (noteValues.containsKey("textSize")) {
							textSize = Double.parseDouble(noteValues.get("textSize").toString());
							noteEditText.setTextSize((int)textSize);
						}
						if (noteValues.containsKey("category")) {
							category = noteValues.get("category").toString();
						}
						if (noteValues.containsKey("createTime") && noteValues.containsKey("editTime")) {
							if (noteValues.get("editTime").toString().equals("0")) {
								Calendar1.setTimeInMillis((long)(Double.parseDouble(noteValues.get("createTime").toString())));
								dateText.setText(getString(R.string.notedAt).concat(" ".concat(new SimpleDateFormat("HH:mm\ndd MMMM").format(Calendar1.getTime()))));
							} else {
								Calendar1.setTimeInMillis((long)(Double.parseDouble(noteValues.get("editTime").toString())));
								dateText.setText(getString(R.string.lastEditAt).concat(" ".concat(new SimpleDateFormat("HH:mm\ndd MMMM").format(Calendar1.getTime()))));
							}
							if (noteValues.containsKey("isFavorite")) {
								if (noteValues.get("isFavorite").toString().equals("1")) {
									toolbarFavoriteImage.performClick();
								}
							}
						}
					} else {
						SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.somethingWentWrong));
					}
					break;
				}
				case "archive": {
					type = "archive";
					toolbarTitle.setText(getString(R.string.editNote));
					if (getIntent().hasExtra("noteMap")) {
						noteValues = new Gson().fromJson(getIntent().getStringExtra("noteMap"), new TypeToken<HashMap<String, Object>>(){}.getType());
						if (noteValues.containsKey("title")) {
							titleEditText.setText(noteValues.get("title").toString());
						}
						if (noteValues.containsKey("note")) {
							noteEditText.setText(noteValues.get("note").toString());
						}
						if (noteValues.containsKey("textSize")) {
							textSize = Double.parseDouble(noteValues.get("textSize").toString());
							noteEditText.setTextSize((int)textSize);
						}
						if (noteValues.containsKey("category")) {
							category = noteValues.get("category").toString();
						}
						if (noteValues.containsKey("createTime") && noteValues.containsKey("editTime")) {
							if (noteValues.get("editTime").toString().equals("0")) {
								Calendar1.setTimeInMillis((long)(Double.parseDouble(noteValues.get("createTime").toString())));
								dateText.setText(getString(R.string.notedAt).concat(" ".concat(new SimpleDateFormat("HH:mm\ndd MMMM").format(Calendar1.getTime()))));
							} else {
								Calendar1.setTimeInMillis((long)(Double.parseDouble(noteValues.get("editTime").toString())));
								dateText.setText(getString(R.string.lastEditAt).concat(" ".concat(new SimpleDateFormat("HH:mm\ndd MMMM").format(Calendar1.getTime()))));
							}
							if (noteValues.containsKey("isFavorite")) {
								if (noteValues.get("isFavorite").toString().equals("1")) {
									toolbarFavoriteImage.performClick();
								}
							}
						}
					} else {
						SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.somethingWentWrong));
					}
					break;
				}
			}
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.somethingWentWrong));
		}
		_GetCategoryNames();
		_AutoSaveNote();
		_fab.setOnTouchListener(new View.OnTouchListener() {
			float currentX = 0, currentY = 0;
			
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					// Basınca hafif büyüt
					view.animate()
					.scaleX(1.1f)
					.scaleY(1.1f)
					.setDuration(300)
					.start();
					break;
					
					case MotionEvent.ACTION_MOVE:
					float dx = event.getX() - (view.getWidth() / 2f);
					float dy = event.getY() - (view.getHeight() / 2f);
					
					// Parmağın uzaklığı
					double length = Math.sqrt(dx * dx + dy * dy);
					
					// Maksimum kayma mesafesi (örnek 80dp)
					float max = 20 * view.getResources().getDisplayMetrics().density;
					
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
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Timer1.cancel();
		Sp.edit().remove("autoSaveNote").commit();
	}
	
	@Override
	public void onBackPressed() {
		final Handler wait = new Handler(Looper.getMainLooper());
		final Runnable rwait  = new Runnable() {
			@Override
			public void run() {
				if (!(titleEditText.getText().toString().equals("") && noteEditText.getText().toString().equals(""))) {
					if (noteValues.containsKey("title") && (noteValues.containsKey("note") && noteValues.containsKey("category"))) {
						if (favorite.equals(noteValues.get("isFavorite").toString()) && (titleEditText.getText().toString().equals(noteValues.get("title").toString()) && (noteEditText.getText().toString().equals(noteValues.get("note").toString()) && category.equals(noteValues.get("category").toString())))) {
							finishAfterTransition();
						} else {
							_SaveNoteToSQLite();
							SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.saved));
						}
					} else {
						_SaveNoteToSQLite();
						SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.saved));
					}
				} else {
					finishAfterTransition();
				};
			}
		};
		wait.postDelayed(rwait, 100);
	}
	public void _Info() {
		if (info_state) {
			toolbarInfoText.setText(getString(R.string.titleLines).concat(": ").concat(String.valueOf((long)(titleEditText.getLineCount()))).concat("\n".concat(getString(R.string.noteLines).concat(": ").concat(String.valueOf((long)(noteEditText.getLineCount()))))));
		}
		if (!info_state) {
			toolbarInfoText.setText(getString(R.string.titleLetters).concat(": ").concat(String.valueOf((long)(titleEditText.getText().toString().length()))).concat("\n".concat(getString(R.string.noteLetters).concat(": ").concat(String.valueOf((long)(noteEditText.getText().toString().length()))))));
		}
	}
	
	
	public void _EdittextHistory(final View _redo, final View _undo, final TextView _et) {
		final UndoRedoFunction URF = new UndoRedoFunction(_et);
		
		//Undo View
		_undo.setOnClickListener (new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				URF.undo();
			}
		});
		
		//Redo View
		_redo.setOnClickListener (new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				URF.redo();
			}
		});
		
		//Class
	}
	public class UndoRedoFunction {
		
		private boolean mIsUndoOrRedo = false;
		private EditHistory mEditHistory;
		private EditTextChangeListener mChangeListener;
		private TextView mTextView;
		
		
		public UndoRedoFunction(TextView textView) {
			mTextView = textView;
			mEditHistory = new EditHistory();
			mChangeListener = new EditTextChangeListener();
			mTextView.addTextChangedListener(mChangeListener);
		}
		
		public void disconnect() {
			mTextView.removeTextChangedListener(mChangeListener);
		}
		//By THEBI3ST
		public void setMaxHistorySize(int maxHistorySize) {
			mEditHistory.setMaxHistorySize(maxHistorySize);
		}
		
		public void clearHistory() {
			mEditHistory.clear();
		}
		
		
		public boolean getCanUndo() {
			return (mEditHistory.mmPosition > 0);
		}
		
		public void undo() {
			EditItem edit = mEditHistory.getPrevious();
			if (edit == null) {
				return;
			}
			
			Editable text = mTextView.getEditableText();
			int start = edit.mmStart;
			int end = start + (edit.mmAfter != null ? edit.mmAfter.length() : 0);
			
			mIsUndoOrRedo = true;
			text.replace(start, end, edit.mmBefore);
			mIsUndoOrRedo = false;
			
			for (Object o : text.getSpans(0, text.length(), android.text.style.UnderlineSpan.class)) {
				text.removeSpan(o);
			}//xenondry
			
			Selection.setSelection(text, edit.mmBefore == null ? start
			: (start + edit.mmBefore.length()));
		}
		
		public boolean getCanRedo() {
			return (mEditHistory.mmPosition < mEditHistory.mmHistory.size());
		}
		
		public void redo() {
			EditItem edit = mEditHistory.getNext();
			if (edit == null) {
				return;
			}
			//JustMax
			Editable text = mTextView.getEditableText();
			int start = edit.mmStart;
			int end = start + (edit.mmBefore != null ? edit.mmBefore.length() : 0);
			
			mIsUndoOrRedo = true;
			text.replace(start, end, edit.mmAfter);
			mIsUndoOrRedo = false;
			
			for (Object o : text.getSpans(0, text.length(), android.text.style.UnderlineSpan.class)) {
				text.removeSpan(o);
			}
			
			Selection.setSelection(text, edit.mmAfter == null ? start
			: (start + edit.mmAfter.length()));
		}
		
		public void storePersistentState(android.content.SharedPreferences.Editor editor, String prefix) {
			
			editor.putString(prefix + ".hash",
			String.valueOf(mTextView.getText().toString().hashCode()));
			editor.putInt(prefix + ".maxSize", mEditHistory.mmMaxHistorySize);
			editor.putInt(prefix + ".position", mEditHistory.mmPosition);
			editor.putInt(prefix + ".size", mEditHistory.mmHistory.size());
			
			int i = 0;
			for (EditItem ei : mEditHistory.mmHistory) {
				String pre = prefix + "." + i;
				
				editor.putInt(pre + ".start", ei.mmStart);
				editor.putString(pre + ".before", ei.mmBefore.toString());
				editor.putString(pre + ".after", ei.mmAfter.toString());
				
				i++;
			}
		}
		
		public boolean restorePersistentState(SharedPreferences sp, String prefix)
		throws IllegalStateException {
			
			boolean ok = doRestorePersistentState(sp, prefix);
			if (!ok) {
				mEditHistory.clear();
			}
			
			return ok;
		}
		
		private boolean doRestorePersistentState(SharedPreferences sp, String prefix) {
			
			String hash = sp.getString(prefix + ".hash", null);
			if (hash == null) {
				return true;
			}
			
			if (Integer.valueOf(hash) != mTextView.getText().toString().hashCode()) {
				return false;
			}
			
			mEditHistory.clear();
			mEditHistory.mmMaxHistorySize = sp.getInt(prefix + ".maxSize", -1);
			
			int count = sp.getInt(prefix + ".size", -1);
			if (count == -1) {
				return false;
			}
			
			for (int i = 0; i < count; i++) {
				String pre = prefix + "." + i;
				
				int start = sp.getInt(pre + ".start", -1);
				String before = sp.getString(pre + ".before", null);
				String after = sp.getString(pre + ".after", null);
				
				if (start == -1 || before == null || after == null) {
					return false;
				}
				mEditHistory.add(new EditItem(start, before, after));
			}
			
			mEditHistory.mmPosition = sp.getInt(prefix + ".position", -1);
			if (mEditHistory.mmPosition == -1) {
				return false;
			}
			
			return true;
		}
		
		private final class EditHistory {
			
			private int mmPosition = 0;
			private int mmMaxHistorySize = -1;
			private final LinkedList<EditItem> mmHistory = new LinkedList<EditItem>();
			private void clear() {
				mmPosition = 0;
				mmHistory.clear();
			}
			
			private void add(EditItem item) {
				while (mmHistory.size() > mmPosition) {
					mmHistory.removeLast();
				}
				mmHistory.add(item);
				mmPosition++;
				
				if (mmMaxHistorySize >= 0) {
					trimHistory();
				}
			}
			
			private void setMaxHistorySize(int maxHistorySize) {
				mmMaxHistorySize = maxHistorySize;
				if (mmMaxHistorySize >= 0) {
					trimHistory();
				}
			}
			
			private void trimHistory() {
				while (mmHistory.size() > mmMaxHistorySize) {
					mmHistory.removeFirst();
					mmPosition--;
				}
				
				if (mmPosition < 0) {
					mmPosition = 0;
				}
			}
			
			private EditItem getPrevious() {
				if (mmPosition == 0) {
					return null;
				}
				mmPosition--;
				return mmHistory.get(mmPosition);
			}
			
			private EditItem getNext() {
				if (mmPosition >= mmHistory.size()) {
					return null;
				}
				
				EditItem item = mmHistory.get(mmPosition);
				mmPosition++;
				return item;
			}
		}
		
		private final class EditItem {
			private final int mmStart;
			private final CharSequence mmBefore;
			private final CharSequence mmAfter;
			
			public EditItem(int start, CharSequence before, CharSequence after) {
				mmStart = start;
				mmBefore = before;
				mmAfter = after;
			}
		}
		
		private final class EditTextChangeListener implements TextWatcher {
			
			private CharSequence mBeforeChange;
			private CharSequence mAfterChange;
			
			public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
				if (mIsUndoOrRedo) {
					return;
				}
				
				mBeforeChange = s.subSequence(start, start + count);
			}
			
			public void onTextChanged(CharSequence s, int start, int before,
			int count) {
				if (mIsUndoOrRedo) {
					return;
				}
				
				mAfterChange = s.subSequence(start, start + count);
				mEditHistory.add(new EditItem(start, mBeforeChange, mAfterChange));
			}
			
			public void afterTextChanged(Editable s) {
			}
		}
	}
	{
	}
	
	
	public void _RippleAndAnims() {
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
			android.graphics.drawable.GradientDrawable toolbarInfoTextBG = new android.graphics.drawable.GradientDrawable();
			toolbarInfoTextBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarInfoTextBG.setCornerRadius((float)20);
			toolbarInfoTextBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarInfoText.setBackground(toolbarInfoTextBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarInfoTextMask = new android.graphics.drawable.GradientDrawable();
			toolbarInfoTextMask.setCornerRadius((float)20);
			toolbarInfoTextMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarInfoTextRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarInfoTextMask // mask
			);
			
			toolbarInfoText.setForeground(toolbarInfoTextRE);
			toolbarInfoText.setClickable(true);
			toolbarInfoText.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarInfoTextGG = new android.graphics.drawable.GradientDrawable();
			toolbarInfoTextGG.setColor(Color.TRANSPARENT);
			toolbarInfoTextGG.setCornerRadius((float)20);
			toolbarInfoTextGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarInfoTextRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarInfoTextGG,
			null
			);
			toolbarInfoText.setBackground(toolbarInfoTextRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable toolbarFavoriteImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarFavoriteImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarFavoriteImageBG.setCornerRadius((float)50);
			toolbarFavoriteImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarFavoriteImage.setBackground(toolbarFavoriteImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarFavoriteImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarFavoriteImageMask.setCornerRadius((float)50);
			toolbarFavoriteImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarFavoriteImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarFavoriteImageMask // mask
			);
			
			toolbarFavoriteImage.setForeground(toolbarFavoriteImageRE);
			toolbarFavoriteImage.setClickable(true);
			toolbarFavoriteImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarFavoriteImageGG = new android.graphics.drawable.GradientDrawable();
			toolbarFavoriteImageGG.setColor(Color.TRANSPARENT);
			toolbarFavoriteImageGG.setCornerRadius((float)50);
			toolbarFavoriteImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarFavoriteImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarFavoriteImageGG,
			null
			);
			toolbarFavoriteImage.setBackground(toolbarFavoriteImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable toolbarOptionsImageBG = new android.graphics.drawable.GradientDrawable();
			toolbarOptionsImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			toolbarOptionsImageBG.setCornerRadius((float)50);
			toolbarOptionsImageBG.setStroke((int)0, Color.TRANSPARENT);
			toolbarOptionsImage.setBackground(toolbarOptionsImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable toolbarOptionsImageMask = new android.graphics.drawable.GradientDrawable();
			toolbarOptionsImageMask.setCornerRadius((float)50);
			toolbarOptionsImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable toolbarOptionsImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			toolbarOptionsImageMask // mask
			);
			
			toolbarOptionsImage.setForeground(toolbarOptionsImageRE);
			toolbarOptionsImage.setClickable(true);
			toolbarOptionsImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable toolbarOptionsImageGG = new android.graphics.drawable.GradientDrawable();
			toolbarOptionsImageGG.setColor(Color.TRANSPARENT);
			toolbarOptionsImageGG.setCornerRadius((float)50);
			toolbarOptionsImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable toolbarOptionsImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			toolbarOptionsImageGG,
			null
			);
			toolbarOptionsImage.setBackground(toolbarOptionsImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable noteSizeImageBG = new android.graphics.drawable.GradientDrawable();
			noteSizeImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			noteSizeImageBG.setCornerRadius((float)50);
			noteSizeImageBG.setStroke((int)0, Color.TRANSPARENT);
			noteSizeImage.setBackground(noteSizeImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable noteSizeImageMask = new android.graphics.drawable.GradientDrawable();
			noteSizeImageMask.setCornerRadius((float)50);
			noteSizeImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable noteSizeImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			noteSizeImageMask // mask
			);
			
			noteSizeImage.setForeground(noteSizeImageRE);
			noteSizeImage.setClickable(true);
			noteSizeImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable noteSizeImageGG = new android.graphics.drawable.GradientDrawable();
			noteSizeImageGG.setColor(Color.TRANSPARENT);
			noteSizeImageGG.setCornerRadius((float)50);
			noteSizeImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable noteSizeImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			noteSizeImageGG,
			null
			);
			noteSizeImage.setBackground(noteSizeImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable undoImageBG = new android.graphics.drawable.GradientDrawable();
			undoImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			undoImageBG.setCornerRadius((float)50);
			undoImageBG.setStroke((int)0, Color.TRANSPARENT);
			undoImage.setBackground(undoImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable undoImageMask = new android.graphics.drawable.GradientDrawable();
			undoImageMask.setCornerRadius((float)50);
			undoImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable undoImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			undoImageMask // mask
			);
			
			undoImage.setForeground(undoImageRE);
			undoImage.setClickable(true);
			undoImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable undoImageGG = new android.graphics.drawable.GradientDrawable();
			undoImageGG.setColor(Color.TRANSPARENT);
			undoImageGG.setCornerRadius((float)50);
			undoImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable undoImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			undoImageGG,
			null
			);
			undoImage.setBackground(undoImageRE);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable redoImageBG = new android.graphics.drawable.GradientDrawable();
			redoImageBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			redoImageBG.setCornerRadius((float)50);
			redoImageBG.setStroke((int)0, Color.TRANSPARENT);
			redoImage.setBackground(redoImageBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable redoImageMask = new android.graphics.drawable.GradientDrawable();
			redoImageMask.setCornerRadius((float)50);
			redoImageMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable redoImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			redoImageMask // mask
			);
			
			redoImage.setForeground(redoImageRE);
			redoImage.setClickable(true);
			redoImage.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable redoImageGG = new android.graphics.drawable.GradientDrawable();
			redoImageGG.setColor(Color.TRANSPARENT);
			redoImageGG.setCornerRadius((float)50);
			redoImageGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable redoImageRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			redoImageGG,
			null
			);
			redoImage.setBackground(redoImageRE);
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarBackImage.setTooltipText(getString(R.string.goBack));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarInfoText.setTooltipText(getString(R.string.changeInfo));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarFavoriteImage.setTooltipText(getString(R.string.favorite));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarOptionsImage.setTooltipText(getString(R.string.noteMenu));
		}
		if (Build.VERSION.SDK_INT > 25) {
			categorySpinner.setTooltipText(getString(R.string.category));
		}
		if (Build.VERSION.SDK_INT > 25) {
			undoImage.setTooltipText(getString(R.string.undo));
		}
		if (Build.VERSION.SDK_INT > 25) {
			redoImage.setTooltipText(getString(R.string.redo));
		}
		if (Build.VERSION.SDK_INT > 25) {
			_fab.setTooltipText(getString(R.string.save));
		}
	}
	
	
	public void _GetCategoryNames() {
		categoryNameListString.clear();
		categoryMap = new HashMap<>();
		categoryMap = Database_retrieveOne("categoryNames", "NoCategory, Category1, Category2, Category3, Category4, Category5, Category6, Category7, Category8".split(", "), "id" + " = ?", new String[]{"1"});
		categoryNameListString.add((int)(0), getString(R.string.all));
		categoryNameListString.add((int)(1), categoryMap.get("Category1").toString());
		categoryNameListString.add((int)(2), categoryMap.get("Category2").toString());
		categoryNameListString.add((int)(3), categoryMap.get("Category3").toString());
		categoryNameListString.add((int)(4), categoryMap.get("Category4").toString());
		categoryNameListString.add((int)(5), categoryMap.get("Category5").toString());
		categoryNameListString.add((int)(6), categoryMap.get("Category6").toString());
		categoryNameListString.add((int)(7), categoryMap.get("Category7").toString());
		categoryNameListString.add((int)(8), categoryMap.get("Category8").toString());
		if (categoryNameListString.get((int)(1)).equals("")) {
			categoryNameListString.set((int)1, getString(R.string.unnamed).concat(" 1"));
		}
		if (categoryNameListString.get((int)(2)).equals("")) {
			categoryNameListString.set((int)2, getString(R.string.unnamed).concat(" 2"));
		}
		if (categoryNameListString.get((int)(3)).equals("")) {
			categoryNameListString.set((int)3, getString(R.string.unnamed).concat(" 3"));
		}
		if (categoryNameListString.get((int)(4)).equals("")) {
			categoryNameListString.set((int)4, getString(R.string.unnamed).concat(" 4"));
		}
		if (categoryNameListString.get((int)(5)).equals("")) {
			categoryNameListString.set((int)5, getString(R.string.unnamed).concat(" 5"));
		}
		if (categoryNameListString.get((int)(6)).equals("")) {
			categoryNameListString.set((int)6, getString(R.string.unnamed).concat(" 6"));
		}
		if (categoryNameListString.get((int)(7)).equals("")) {
			categoryNameListString.set((int)7, getString(R.string.unnamed).concat(" 7"));
		}
		if (categoryNameListString.get((int)(8)).equals("")) {
			categoryNameListString.set((int)8, getString(R.string.unnamed).concat(" 8"));
		}
		categorySpinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, categoryNameListString));
		switch(category) {
			case "NoCategory": {
				categorySpinner.setSelection((int)(0));
				break;
			}
			case "Category1": {
				categorySpinner.setSelection((int)(1));
				break;
			}
			case "Category2": {
				categorySpinner.setSelection((int)(2));
				break;
			}
			case "Category3": {
				categorySpinner.setSelection((int)(3));
				break;
			}
			case "Category4": {
				categorySpinner.setSelection((int)(4));
				break;
			}
			case "Category5": {
				categorySpinner.setSelection((int)(5));
				break;
			}
			case "Category6": {
				categorySpinner.setSelection((int)(6));
				break;
			}
			case "Category7": {
				categorySpinner.setSelection((int)(7));
				break;
			}
			case "Category8": {
				categorySpinner.setSelection((int)(8));
				break;
			}
		}
	}
	
	
	public void _OnCreateSetSQLite() {
		Database = SQLiteDatabase.openOrCreateDatabase((NoteeditActivity.this).getDatabasePath("QuickNotes" + ".db").getPath(), null);
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
	
	
	public void _AutoSaveNote() {
		if (Sp.contains("autoSave")) {
			Timer1 = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AsyncTask.execute(new Runnable() {
								@Override
								public void run() {
									switch(type) {
										case "new": {
											if (!noteEditText.getText().toString().equals("") || !titleEditText.getText().toString().equals("")) {
												Calendar1 = Calendar.getInstance();
												autoSaveMap = new HashMap<>();
												if (noteEditText.getText().toString().equals("")) {
													autoSaveMap.put("title", "");
													autoSaveMap.put("note", titleEditText.getText().toString());
												} else {
													autoSaveMap.put("title", titleEditText.getText().toString());
													autoSaveMap.put("note", noteEditText.getText().toString());
												}
												autoSaveMap.put("tags", "");
												if (favorite.equals("1")) {
													autoSaveMap.put("isFavorite", "1");
												} else {
													autoSaveMap.put("isFavorite", "0");
												}
												autoSaveMap.put("category", category);
												autoSaveMap.put("state", "Normal");
												autoSaveMap.put("sortTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
												autoSaveMap.put("createTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
												autoSaveMap.put("editTime", "0");
												autoSaveMap.put("trashTime", "0");
												autoSaveMap.put("archiveTime", "0");
												autoSaveMap.put("isReminder", "0");
												autoSaveMap.put("reminderTime", "0");
												Sp.edit().putString("autoSaveNote", new Gson().toJson(autoSaveMap)).commit();
											} else {
												Sp.edit().remove("autoSaveNote").commit();
											}
											break;
										}
									}
								}
							});
						}
					});
				}
			};
			_timer.scheduleAtFixedRate(Timer1, (int)(1000), (int)(1000));
		} else {
			Sp.edit().remove("autoSaveNote").commit();
		}
	}
	
	
	public void _SaveNoteToSQLite() {
		switch(type) {
			case "new": {
				Calendar1 = Calendar.getInstance();
				noteValues = new HashMap<>();
				if (noteEditText.getText().toString().equals("")) {
					noteValues.put("title", "");
					noteValues.put("note", titleEditText.getText().toString());
				} else {
					noteValues.put("title", titleEditText.getText().toString());
					noteValues.put("note", noteEditText.getText().toString());
				}
				noteValues.put("tags", "");
				if (favorite.equals("1")) {
					noteValues.put("isFavorite", "1");
				} else {
					noteValues.put("isFavorite", "0");
				}
				noteValues.put("category", category);
				noteValues.put("state", "Normal");
				noteValues.put("sortTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
				noteValues.put("createTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
				noteValues.put("editTime", "0");
				noteValues.put("trashTime", "0");
				noteValues.put("archiveTime", "0");
				noteValues.put("isReminder", "0");
				noteValues.put("reminderTime", "0");
				Database_insert("notes", noteValues);
				Sp.edit().putString("reloadData", "note").commit();
				break;
			}
			case "edit": {
				Calendar1 = Calendar.getInstance();
				noteValues.put("title", titleEditText.getText().toString());
				noteValues.put("note", noteEditText.getText().toString());
				noteValues.put("sortTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
				noteValues.put("editTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
				if (favorite.equals("1")) {
					noteValues.put("isFavorite", "1");
				} else {
					noteValues.put("isFavorite", "0");
				}
				noteValues.put("category", category);
				Database_update("notes", noteValues, "id" + " = ?", new String[]{noteValues.get("id").toString()});
				Sp.edit().putString("reloadData", "note").commit();
				break;
			}
			case "archive": {
				Calendar1 = Calendar.getInstance();
				noteValues.put("title", titleEditText.getText().toString());
				noteValues.put("note", noteEditText.getText().toString());
				noteValues.put("sortTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
				noteValues.put("editTime", String.valueOf((long)(Calendar1.getTimeInMillis())));
				if (favorite.equals("1")) {
					noteValues.put("isFavorite", "1");
				} else {
					noteValues.put("isFavorite", "0");
				}
				noteValues.put("category", category);
				Database_update("notes", noteValues, "id" + " = ?", new String[]{noteValues.get("id").toString()});
				Sp.edit().putString("reloadData", "note").commit();
				break;
			}
		}
		noteBg.setVisibility(View.GONE);
		finishAfterTransition();
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