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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import eightbitlab.com.blurview.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class CategorynameeditActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private FloatingActionButton _fab;
	private HashMap<String, Object> categoryMap = new HashMap<>();
	
	private ArrayList<String> categoryNames = new ArrayList<>();
	
	private LinearLayout toolbarBg;
	private LinearLayout showcaseTitleBg;
	private HorizontalScrollView categoryScrollBg;
	private ScrollView editCategoryScollBg;
	private ImageView toolbarBackImage;
	private LinearLayout toolbarTextBg;
	private TextView toolbarTitle;
	private TextView toolbarSubtitle;
	private TextView showcaseTitleText;
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
	private LinearLayout editCategoryBg;
	private TextView editCategoryTitle;
	private LinearLayout category1Bg;
	private LinearLayout category2Bg;
	private LinearLayout category3Bg;
	private LinearLayout category4Bg;
	private LinearLayout category5Bg;
	private LinearLayout category6Bg;
	private LinearLayout category7Bg;
	private LinearLayout category8Bg;
	private EditText category1EditText;
	private MaterialButton category1ButtonEdit;
	private EditText category2EditText;
	private MaterialButton category2ButtonEdit;
	private EditText category3EditText;
	private MaterialButton category3ButtonEdit;
	private EditText category4EditText;
	private MaterialButton category4ButtonEdit;
	private EditText category5EditText;
	private MaterialButton category5ButtonEdit;
	private EditText category6EditText;
	private MaterialButton category6ButtonEdit;
	private EditText category7EditText;
	private MaterialButton category7ButtonEdit;
	private EditText category8EditText;
	private MaterialButton category8ButtonEdit;
	
	private SharedPreferences Sp;
	private TimerTask Timer1;
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
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.categorynameedit);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		MobileAds.initialize(this);
		
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_fab = findViewById(R.id._fab);
		toolbarBg = findViewById(R.id.toolbarBg);
		showcaseTitleBg = findViewById(R.id.showcaseTitleBg);
		categoryScrollBg = findViewById(R.id.categoryScrollBg);
		editCategoryScollBg = findViewById(R.id.editCategoryScollBg);
		toolbarBackImage = findViewById(R.id.toolbarBackImage);
		toolbarTextBg = findViewById(R.id.toolbarTextBg);
		toolbarTitle = findViewById(R.id.toolbarTitle);
		toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
		showcaseTitleText = findViewById(R.id.showcaseTitleText);
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
		editCategoryBg = findViewById(R.id.editCategoryBg);
		editCategoryTitle = findViewById(R.id.editCategoryTitle);
		category1Bg = findViewById(R.id.category1Bg);
		category2Bg = findViewById(R.id.category2Bg);
		category3Bg = findViewById(R.id.category3Bg);
		category4Bg = findViewById(R.id.category4Bg);
		category5Bg = findViewById(R.id.category5Bg);
		category6Bg = findViewById(R.id.category6Bg);
		category7Bg = findViewById(R.id.category7Bg);
		category8Bg = findViewById(R.id.category8Bg);
		category1EditText = findViewById(R.id.category1EditText);
		category1ButtonEdit = findViewById(R.id.category1ButtonEdit);
		category2EditText = findViewById(R.id.category2EditText);
		category2ButtonEdit = findViewById(R.id.category2ButtonEdit);
		category3EditText = findViewById(R.id.category3EditText);
		category3ButtonEdit = findViewById(R.id.category3ButtonEdit);
		category4EditText = findViewById(R.id.category4EditText);
		category4ButtonEdit = findViewById(R.id.category4ButtonEdit);
		category5EditText = findViewById(R.id.category5EditText);
		category5ButtonEdit = findViewById(R.id.category5ButtonEdit);
		category6EditText = findViewById(R.id.category6EditText);
		category6ButtonEdit = findViewById(R.id.category6ButtonEdit);
		category7EditText = findViewById(R.id.category7EditText);
		category7ButtonEdit = findViewById(R.id.category7ButtonEdit);
		category8EditText = findViewById(R.id.category8EditText);
		category8ButtonEdit = findViewById(R.id.category8ButtonEdit);
		Sp = getSharedPreferences("QuickNotesData", Activity.MODE_PRIVATE);
		
		toolbarBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finishAfterTransition();
			}
		});
		
		category1EditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_CategoryNamesChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		category2EditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_CategoryNamesChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		category3EditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_CategoryNamesChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		category4EditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_CategoryNamesChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		category5EditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_CategoryNamesChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		category6EditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_CategoryNamesChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		category7EditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_CategoryNamesChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		category8EditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_CategoryNamesChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				categoryMap = new HashMap<>();
				categoryMap.put("NoCategory", getString(R.string.all));
				categoryMap.put("Category1", category1EditText.getText().toString());
				categoryMap.put("Category2", category2EditText.getText().toString());
				categoryMap.put("Category3", category3EditText.getText().toString());
				categoryMap.put("Category4", category4EditText.getText().toString());
				categoryMap.put("Category5", category5EditText.getText().toString());
				categoryMap.put("Category6", category6EditText.getText().toString());
				categoryMap.put("Category7", category7EditText.getText().toString());
				categoryMap.put("Category8", category8EditText.getText().toString());
				Database_update("categoryNames", categoryMap, "id" + " = ?", new String[]{"1"});
				Sp.edit().putString("reloadData", "category").commit();
				finishAfterTransition();
			}
		});
	}
	
	private void initializeLogic() {
		_GetCategoryNamesFromSharedPref();
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
					float max = 10 * view.getResources().getDisplayMetrics().density;
					
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
		_RippleAndTooltips();
	}
	
	public void _GetCategoryNamesFromSharedPref() {
		Database = SQLiteDatabase.openOrCreateDatabase((CategorynameeditActivity.this).getDatabasePath("QuickNotes" + ".db").getPath(), null);
		categoryMap = new HashMap<>();
		categoryMap.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
		categoryMap.put("NoCategory", "TEXT");
		categoryMap.put("Category1", "TEXT");
		categoryMap.put("Category2", "TEXT");
		categoryMap.put("Category3", "TEXT");
		categoryMap.put("Category4", "TEXT");
		categoryMap.put("Category5", "TEXT");
		categoryMap.put("Category6", "TEXT");
		categoryMap.put("Category7", "TEXT");
		categoryMap.put("Category8", "TEXT");
		Database_createTable("categoryNames", categoryMap);
		categoryNames.clear();
		categoryMap = new HashMap<>();
		categoryMap = Database_retrieveOne("categoryNames", "NoCategory, Category1, Category2, Category3, Category4, Category5, Category6, Category7, Category8".split(", "), "id" + " = ?", new String[]{"1"});
		categoryNames.add((int)(0), categoryMap.get("NoCategory").toString());
		categoryNames.add((int)(1), categoryMap.get("Category1").toString());
		categoryNames.add((int)(2), categoryMap.get("Category2").toString());
		categoryNames.add((int)(3), categoryMap.get("Category3").toString());
		categoryNames.add((int)(4), categoryMap.get("Category4").toString());
		categoryNames.add((int)(5), categoryMap.get("Category5").toString());
		categoryNames.add((int)(6), categoryMap.get("Category6").toString());
		categoryNames.add((int)(7), categoryMap.get("Category7").toString());
		categoryNames.add((int)(8), categoryMap.get("Category8").toString());
		if (!categoryNames.get((int)(1)).equals("")) {
			category1Button.setText(categoryNames.get((int)(1)));
			category1ButtonEdit.setText(categoryNames.get((int)(1)));
			category1EditText.setText(categoryNames.get((int)(1)));
		} else {
			category1Button.setText(getString(R.string.unnamed).concat(" 1"));
			category1ButtonEdit.setText(getString(R.string.unnamed).concat(" 1"));
		}
		if (!categoryNames.get((int)(2)).equals("")) {
			category2Button.setText(categoryNames.get((int)(2)));
			category2ButtonEdit.setText(categoryNames.get((int)(2)));
			category2EditText.setText(categoryNames.get((int)(2)));
		} else {
			category2Button.setText(getString(R.string.unnamed).concat(" 2"));
			category2ButtonEdit.setText(getString(R.string.unnamed).concat(" 2"));
		}
		if (!categoryNames.get((int)(3)).equals("")) {
			category3Button.setText(categoryNames.get((int)(3)));
			category3ButtonEdit.setText(categoryNames.get((int)(3)));
			category3EditText.setText(categoryNames.get((int)(3)));
		} else {
			category3Button.setText(getString(R.string.unnamed).concat(" 3"));
			category3ButtonEdit.setText(getString(R.string.unnamed).concat(" 3"));
		}
		if (!categoryNames.get((int)(4)).equals("")) {
			category4Button.setText(categoryNames.get((int)(4)));
			category4ButtonEdit.setText(categoryNames.get((int)(4)));
			category4EditText.setText(categoryNames.get((int)(4)));
		} else {
			category4Button.setText(getString(R.string.unnamed).concat(" 4"));
			category4ButtonEdit.setText(getString(R.string.unnamed).concat(" 4"));
		}
		if (!categoryNames.get((int)(5)).equals("")) {
			category5Button.setText(categoryNames.get((int)(5)));
			category5ButtonEdit.setText(categoryNames.get((int)(5)));
			category5EditText.setText(categoryNames.get((int)(5)));
		} else {
			category5Button.setText(getString(R.string.unnamed).concat(" 5"));
			category5ButtonEdit.setText(getString(R.string.unnamed).concat(" 5"));
		}
		if (!categoryNames.get((int)(6)).equals("")) {
			category6Button.setText(categoryNames.get((int)(6)));
			category6ButtonEdit.setText(categoryNames.get((int)(6)));
			category6EditText.setText(categoryNames.get((int)(6)));
		} else {
			category6Button.setText(getString(R.string.unnamed).concat(" 6"));
			category6ButtonEdit.setText(getString(R.string.unnamed).concat(" 6"));
		}
		if (!categoryNames.get((int)(7)).equals("")) {
			category7Button.setText(categoryNames.get((int)(7)));
			category7ButtonEdit.setText(categoryNames.get((int)(7)));
			category7EditText.setText(categoryNames.get((int)(7)));
		} else {
			category7Button.setText(getString(R.string.unnamed).concat(" 7"));
			category7ButtonEdit.setText(getString(R.string.unnamed).concat(" 7"));
		}
		if (!categoryNames.get((int)(8)).equals("")) {
			category8Button.setText(categoryNames.get((int)(8)));
			category8ButtonEdit.setText(categoryNames.get((int)(8)));
			category8EditText.setText(categoryNames.get((int)(8)));
		} else {
			category8Button.setText(getString(R.string.unnamed).concat(" 8"));
			category8ButtonEdit.setText(getString(R.string.unnamed).concat(" 8"));
		}
	}
	
	
	public void _CategoryNamesChanged() {
		if (!category1EditText.getText().toString().equals("")) {
			category1Button.setText(category1EditText.getText().toString());
			category1ButtonEdit.setText(category1EditText.getText().toString());
		} else {
			category1Button.setText(getString(R.string.unnamed).concat(" 1"));
			category1ButtonEdit.setText(getString(R.string.unnamed).concat(" 1"));
		}
		if (!category2EditText.getText().toString().equals("")) {
			category2Button.setText(category2EditText.getText().toString());
			category2ButtonEdit.setText(category2EditText.getText().toString());
		} else {
			category2Button.setText(getString(R.string.unnamed).concat(" 2"));
			category2ButtonEdit.setText(getString(R.string.unnamed).concat(" 2"));
		}
		if (!category3EditText.getText().toString().equals("")) {
			category3Button.setText(category3EditText.getText().toString());
			category3ButtonEdit.setText(category3EditText.getText().toString());
		} else {
			category3Button.setText(getString(R.string.unnamed).concat(" 3"));
			category3ButtonEdit.setText(getString(R.string.unnamed).concat(" 3"));
		}
		if (!category4EditText.getText().toString().equals("")) {
			category4Button.setText(category4EditText.getText().toString());
			category4ButtonEdit.setText(category4EditText.getText().toString());
		} else {
			category4Button.setText(getString(R.string.unnamed).concat(" 4"));
			category4ButtonEdit.setText(getString(R.string.unnamed).concat(" 4"));
		}
		if (!category5EditText.getText().toString().equals("")) {
			category5Button.setText(category5EditText.getText().toString());
			category5ButtonEdit.setText(category5EditText.getText().toString());
		} else {
			category5Button.setText(getString(R.string.unnamed).concat(" 5"));
			category5ButtonEdit.setText(getString(R.string.unnamed).concat(" 5"));
		}
		if (!category6EditText.getText().toString().equals("")) {
			category6Button.setText(category6EditText.getText().toString());
			category6ButtonEdit.setText(category6EditText.getText().toString());
		} else {
			category6Button.setText(getString(R.string.unnamed).concat(" 6"));
			category6ButtonEdit.setText(getString(R.string.unnamed).concat(" 6"));
		}
		if (!category7EditText.getText().toString().equals("")) {
			category7Button.setText(category7EditText.getText().toString());
			category7ButtonEdit.setText(category7EditText.getText().toString());
		} else {
			category7Button.setText(getString(R.string.unnamed).concat(" 7"));
			category7ButtonEdit.setText(getString(R.string.unnamed).concat(" 7"));
		}
		if (!category8EditText.getText().toString().equals("")) {
			category8Button.setText(category8EditText.getText().toString());
			category8ButtonEdit.setText(category8EditText.getText().toString());
		} else {
			category8Button.setText(getString(R.string.unnamed).concat(" 8"));
			category8ButtonEdit.setText(getString(R.string.unnamed).concat(" 8"));
		}
	}
	
	
	public void _RippleAndTooltips() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Arka plan: Şeffaf ya da dışarıdan verilen
			android.graphics.drawable.GradientDrawable _fabBG = new android.graphics.drawable.GradientDrawable();
			_fabBG.setColor(Color.TRANSPARENT); // Color.TRANSPARENT genelde Color.TRANSPARENT olur
			_fabBG.setCornerRadius((float)50);
			_fabBG.setStroke((int)0, Color.TRANSPARENT);
			_fab.setBackground(_fabBG); // sadece arka plan
			
			// Ripple maskesi: sadece ripple efekti için görünmez tabaka
			android.graphics.drawable.GradientDrawable _fabMask = new android.graphics.drawable.GradientDrawable();
			_fabMask.setCornerRadius((float)50);
			_fabMask.setColor(android.graphics.Color.WHITE); // Görünmez ama ripple'ı gösterir
			
			android.graphics.drawable.RippleDrawable _fabRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)} // ripple rengi
			),
			null, // foreground ripple
			_fabMask // mask
			);
			
			_fab.setForeground(_fabRE);
			_fab.setClickable(true);
			_fab.setFocusable(true);
		} else {
			// Desteklemeyen Android sürümleri için eski yöntem (arka plan ripple)
			android.graphics.drawable.GradientDrawable _fabGG = new android.graphics.drawable.GradientDrawable();
			_fabGG.setColor(Color.TRANSPARENT);
			_fabGG.setCornerRadius((float)50);
			_fabGG.setStroke((int) 0, Color.TRANSPARENT);
			android.graphics.drawable.RippleDrawable _fabRE = new android.graphics.drawable.RippleDrawable(
			new android.content.res.ColorStateList(
			new int[][]{new int[]{}},
			new int[]{getResources().getColor(R.color.colorControlHighlight)}
			),
			_fabGG,
			null
			);
			_fab.setBackground(_fabRE);
		}
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
		if (Build.VERSION.SDK_INT > 25) {
			_fab.setTooltipText(getString(R.string.save));
		}
		if (Build.VERSION.SDK_INT > 25) {
			toolbarBackImage.setTooltipText(getString(R.string.goBack));
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