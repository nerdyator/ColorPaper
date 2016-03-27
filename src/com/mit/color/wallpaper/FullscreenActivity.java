package com.mit.color.wallpaper;

import com.mit.color.wallpaper.R;
import com.mit.color.wallpaper.util.SystemUiHider;

import android.R.bool;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_FULLSCREEN;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	int changedColor=0;
    private static final int initColor = 0xFFFF0000;
    private static final int ACTION_GETCOLOR = 1;
    FrameLayout frameLayout;
    Button button,set_wall;
    boolean isSelect=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(
				mDelayHideTouchListener);
		
		
		changedColor=initColor;
		frameLayout = (FrameLayout)findViewById(R.id.fLayout);
		button = (Button)findViewById(R.id.dummy_button);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),org.superdry.util.colorpicker.lib.SuperdryColorPicker.class);
				intent.putExtra("SelectedColor", changedColor);
				startActivityForResult(intent,org.superdry.util.colorpicker.lib.SuperdryColorPicker.ACTION_GETCOLOR);
				isSelect=true;	
			}
		});
		
		set_wall = (Button)findViewById(R.id.set_wall);
		set_wall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isSelect==false){
				 Toast.makeText(getApplicationContext(), "Please Select Color First", Toast.LENGTH_SHORT).show();
				}
				else{
					try{
					Toast.makeText(getApplicationContext(), "Setting wallpaper..", Toast.LENGTH_SHORT).show();
					WallpaperManager manager = (WallpaperManager) getApplicationContext().getSystemService("wallpaper");
					DisplayMetrics metrics = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(metrics);
					int maxDimension = Math.max(metrics.heightPixels,metrics.widthPixels);
					Bitmap bitmap = Bitmap.createBitmap(maxDimension, maxDimension,Bitmap.Config.ARGB_8888);
					bitmap.eraseColor(changedColor);
					//bitmap = Bitmap.createScaledBitmap(bitmap, maxDimension, maxDimension, true);
					manager.setBitmap(bitmap);
					Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_SHORT).show();
					}
					catch(Exception e){
					Log.d("exception", e.getMessage());
					}
				}
				
			}
		});
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == ACTION_GETCOLOR) {

                if (resultCode == RESULT_OK) {
                        Bundle b = intent.getExtras();
                        if (b != null) {
                                changedColor = b.getInt("SelectedColor");
                                //log.d("color", String.valueOf(changedColor));
                                frameLayout.setBackgroundColor(changedColor);
                        }
                } else if (resultCode == RESULT_CANCELED) {
                	isSelect=false;  
                	//Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                      
                }
               
        }
	}
}
