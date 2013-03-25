package fitness.core;

import sra.gg.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class HomeScreen extends Activity {

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Intent myScreen = new Intent(this, ExerciseScreen.class);
		//this.startActivity(myScreen);
		

		setTitle("Fitness Tracker");
		getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFFFFF"));
		
		ImageButton button = new ImageButton(this);
		RelativeLayout rl = new RelativeLayout(this);
		int iViewCounter = 1;
		rl.setId(iViewCounter++);
		
		Drawable replacer = getResources().getDrawable(R.drawable.empty_white_box);
		button.setBackground(replacer);
		button.setId(iViewCounter++);
		rl.addView(button);
		
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, button.getId());			
		button = new ImageButton(this);
		button.setId(iViewCounter++);
		replacer = getResources().getDrawable(R.drawable.workout);
		button.setBackground(replacer);
		button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent myScreen = new Intent(v.getContext(), WorkoutScreen.class);
				startActivity(myScreen);
			}
		});
		
		rl.addView(button, lp);
		
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, button.getId());			
		button = new ImageButton(this);
		button.setId(iViewCounter++);
		replacer = getResources().getDrawable(R.drawable.progress);
		button.setBackground(replacer);
		button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent myScreen = new Intent(v.getContext(), ProgressScreen.class);
				startActivity(myScreen);
			}
		});
		rl.addView(button, lp);
		
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, button.getId());			
		button = new ImageButton(this);
		button.setId(iViewCounter++);
		replacer = getResources().getDrawable(R.drawable.prefs);
		button.setBackground(replacer);
		button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent myScreen = new Intent(v.getContext(), PrefsScreen.class);
				startActivity(myScreen);
			}
		});
		rl.addView(button, lp);
		
		
		/*
		LinearLayout ll = new LinearLayout(this);
		int iViewCounter = 1;
		ll.setId(iViewCounter++);
    	ll.setOrientation(LinearLayout.HORIZONTAL);
    	
    	TextView vt = new TextView(this);
		vt.setWidth(80);
		vt.setText("Home Screen");
		vt.setTextColor(Color.parseColor("#00ccff"));
		ll.addView(vt);
	
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_TOP);	
		
		RelativeLayout rl = new RelativeLayout(this);
		rl.setId(iViewCounter++);
		rl.addView(ll, lp);
		
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, ll.getId());
		
		ll = new LinearLayout(this);
		ll.setId(iViewCounter++);
    	ll.setOrientation(LinearLayout.HORIZONTAL);
    	
		Button bWorkout = new Button(this);
		bWorkout.setText("Workout");
		bWorkout.setOnClickListener(openWorkout);
		ll.addView(bWorkout);	
		rl.addView(ll, lp);	
		
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.RIGHT_OF, ll.getId());
		
		Button bprefs = new Button(this);
		bprefs.setText("Preferences");
		rl.addView(bprefs, lp);	
		*/	
		this.setContentView(rl);
	}
	
	private View.OnClickListener openWorkout = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent myScreen = new Intent(v.getContext(), WorkoutScreen.class);
			startActivity(myScreen);
		}
	};
		
}
