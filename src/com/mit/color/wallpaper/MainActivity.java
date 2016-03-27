package com.mit.color.wallpaper;

import com.mit.color.wallpaper.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

	int changedColor;
    private static final int initColor = 0xFFFF0000;
    private static final int ACTION_GETCOLOR = 1;
    RelativeLayout layout;
    Button button;
    Log log;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		changedColor = initColor;
		layout = (RelativeLayout)findViewById(R.id.rLayout);
		button = (Button)findViewById(R.id.button1);
		
		button.setOnClickListener(this);
		log.d("1", "Created");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			log.d("2", "Clicked");
			Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
			startActivity(intent);
			break;

		}
		
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == ACTION_GETCOLOR) {

                if (resultCode == RESULT_OK) {
                        Bundle b = intent.getExtras();
                        if (b != null) {
                                changedColor = b.getInt("SelectedColor");
                                log.d("color", String.valueOf(changedColor));
                        }
                } else if (resultCode == RESULT_CANCELED) {
                      Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
               layout.setBackgroundColor(changedColor);
        }
}
	
}
