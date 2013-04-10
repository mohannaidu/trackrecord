package fitness.core;

import com.trackrecord.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class HomeScreen extends Activity {

	Display display;
	Point size;
	int screenHeight;


	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Intent myScreen = new Intent(this, ExerciseScreen.class);
		//this.startActivity(myScreen);


		setTitle("Track Record");
		getWindow().getDecorView().setBackgroundColor(Color.parseColor("#1e1e1e"));

		ImageButton button = new ImageButton(this);
		RelativeLayout rl = new RelativeLayout(this);
		int iViewCounter = 1;
		rl.setId(iViewCounter++);

		Drawable replacer = getResources().getDrawable(R.drawable.empty_black_box);
		button.setBackgroundDrawable(replacer);
		button.setId(iViewCounter++);
		rl.addView(button);

		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, button.getId());
		button = new ImageButton(this);
		button.setId(iViewCounter++);
		replacer = getResources().getDrawable(R.drawable.workout_btn);
		button.setBackgroundDrawable(replacer);
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
		replacer = getResources().getDrawable(R.drawable.progress_btn);
		button.setBackgroundDrawable(replacer);
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
		replacer = getResources().getDrawable(R.drawable.exercise_btn);
		button.setBackgroundDrawable(replacer);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myScreen = new Intent(v.getContext(), PrefsScreen.class);
				startActivity(myScreen);
			}
		});
		rl.addView(button, lp);

		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		screenHeight = size.y;

		lp = new LayoutParams(LayoutParams.MATCH_PARENT,(int) (screenHeight*0.34));
		lp.addRule(RelativeLayout.BELOW, button.getId());

		View blankSpace = new View(this);
		blankSpace.setId(iViewCounter++);

		rl.addView(blankSpace, lp);

		lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, blankSpace.getId());
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		button = new ImageButton(this);
		button.setId(iViewCounter++);
		replacer = getResources().getDrawable(R.drawable.prefs_button);
		button.setBackgroundDrawable(replacer);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myScreen = new Intent(v.getContext(), PrefsScreen.class);
				startActivity(myScreen);
			}
		});
		rl.addView(button, lp);

		this.setContentView(rl);

		//getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private View.OnClickListener openWorkout = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent myScreen = new Intent(v.getContext(), WorkoutScreen.class);
			startActivity(myScreen);
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed
			// in the Action Bar.
			Intent parentActivityIntent = new Intent(this, HomeScreen.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
