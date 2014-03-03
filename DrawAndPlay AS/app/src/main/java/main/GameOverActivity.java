package main;

import com.example.drawandplay.R;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GameOverActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(
	                WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    	setContentView(R.layout.activity_game_over);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_over, menu);
		return true;
	}
	
	protected void onStop(){
		super.onStop();
		
	}
	public void yes(View view){
		
		Intent intent = new Intent(this,LevelLauncher.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT",false);
		startActivity(intent);
	
		
		
	}
	
	public void no(View view){
		Intent intent = new Intent(this,LevelLauncher.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT",true);
		startActivity(intent);
	}
	
	public void onBackPressed() {
		Intent intent = new Intent(this,LevelLauncher.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT",true);
		startActivity(intent);
		
		
	}

}
