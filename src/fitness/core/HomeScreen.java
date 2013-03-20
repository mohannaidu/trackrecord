package fitness.core;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class HomeScreen extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Intent myScreen = new Intent(this, ExerciseScreen.class);
		//this.startActivity(myScreen);
		
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
