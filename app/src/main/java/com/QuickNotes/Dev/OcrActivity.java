package com.QuickNotes.Dev;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import eightbitlab.com.blurview.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class OcrActivity extends AppCompatActivity {
	
	private FloatingActionButton _fab;
	private String result = "";
	
	private LinearLayout toolbarBg;
	private ScrollView mainScroll;
	private ImageView toolbarBackImage;
	private LinearLayout toolbarTextBg;
	private TextView toolbarTitle;
	private TextView toolbarSubtitle;
	private LinearLayout mainBg;
	private ImageView ocrImage;
	private TextView ocrText;
	
	private Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.ocr);
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
		_fab = findViewById(R.id._fab);
		toolbarBg = findViewById(R.id.toolbarBg);
		mainScroll = findViewById(R.id.mainScroll);
		toolbarBackImage = findViewById(R.id.toolbarBackImage);
		toolbarTextBg = findViewById(R.id.toolbarTextBg);
		toolbarTitle = findViewById(R.id.toolbarTitle);
		toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
		mainBg = findViewById(R.id.mainBg);
		ocrImage = findViewById(R.id.ocrImage);
		ocrText = findViewById(R.id.ocrText);
		
		toolbarBackImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), NoteeditActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("type", "ocr");
				intent.putExtra("text", ocrText.getText().toString());
				View _fabt = findViewById(R.id._fab);
				_fabt.setTransitionName("logo");  
				
				
				
				ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
				OcrActivity.this,
				_fabt,
				"logo"
				);
				
				startActivity(intent, options.toBundle());
				finish();
			}
		});
	}
	
	private void initializeLogic() {
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (getIntent().hasExtra("filePath")) {
			ocrImage.setVisibility(View.VISIBLE);
			String result = "";
			
			try {
				String path = getIntent().getStringExtra("filePath");
				if (path != null) {
					
					// 1️⃣ Bitmap'i mümkün olduğunca yüksek çözünürlükte yükle
					Bitmap bitmap = FileUtil.decodeSampleBitmapFromPath(path, 2048, 2048);
					
					// 2️⃣ EXIF bilgisine bak ve rotasyonu düzelt
					ExifInterface exif = new ExifInterface(path);
					int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
					Matrix matrix = new Matrix();
					switch (orientation) {
						case ExifInterface.ORIENTATION_ROTATE_90: matrix.postRotate(90); break;
						case ExifInterface.ORIENTATION_ROTATE_180: matrix.postRotate(180); break;
						case ExifInterface.ORIENTATION_ROTATE_270: matrix.postRotate(270); break;
					}
					Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					
					// 3️⃣ Gri tonlama ve kontrast artırma (isteğe bağlı ama OCR doğruluğunu artırır)
					Bitmap grayBitmap = Bitmap.createBitmap(rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(grayBitmap);
					Paint paint = new Paint();
					ColorMatrix cm = new ColorMatrix();
					cm.setSaturation(0); // gri ton
					paint.setColorFilter(new ColorMatrixColorFilter(cm));
					canvas.drawBitmap(rotatedBitmap, 0, 0, paint);
					
					// 4️⃣ OCR için bitmap hazır, ImageView'a yükleyebilirsin
					ocrImage.setImageBitmap(rotatedBitmap); // gösterim için orijinal döndürülmüş bitmap
					
					// 5️⃣ Google Vision OCR
					com.google.android.gms.vision.text.TextRecognizer recognizer =
					new com.google.android.gms.vision.text.TextRecognizer.Builder(getApplicationContext()).build();
					com.google.android.gms.vision.Frame frame =
					new com.google.android.gms.vision.Frame.Builder().setBitmap(grayBitmap).build();
					SparseArray<com.google.android.gms.vision.text.TextBlock> items = recognizer.detect(frame);
					
					if (items.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < items.size(); i++) {
							sb.append(" ").append(items.valueAt(i).getValue()); // satır yerine boşluk ile birleştirme
						}
						result = sb.toString().trim();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 6️⃣ Sonuç
			if (result.equals("")) {
				SketchwareUtil.showMessage(getApplicationContext(), "Try Again!");
				finish();
			} else {
				ocrText.setText(result);
			}
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.error));
			finish();
		}
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
		FileUtil.deleteFile(FileUtil.getExternalStorageDir().concat("/Android/data/".concat(getApplicationContext().getPackageName().concat("/files/DCIM"))));
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			ocrImage.setClipToOutline(true);
			ocrImage.setOutlineProvider(new ViewOutlineProvider() {
				@Override
				public void getOutline(View view, Outline outline) {
					outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), (int) (25 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f));
				}
			});
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