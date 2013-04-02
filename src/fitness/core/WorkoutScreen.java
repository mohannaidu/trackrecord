package fitness.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fitness.data.WorkoutAdapter;
import fitness.model.Workout;

import sra.gg.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class WorkoutScreen extends Activity {
	WorkoutAdapter helper;
	TextView vt;
	EditText et;
	int iViewCounter;
	Button save;
	private Workout workout = new Workout();
	private List<EditText> editTextList = new ArrayList<EditText>();
	LayoutParams lpSave;	
	LayoutParams lpAdd;
	LayoutParams lp;
	RelativeLayout rl;
	ControlHelper controlHelper; 
	Cursor workoutCursor;
	Display display;
	Point size;
	int screenWidth;
	protected Dialog mSplashDialog;
	RelativeLayout rlWorkoutRow;
	LayoutParams lpWorkoutRow; 
	

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// style 1
		//this.setTheme(android.R.style.Theme_Black_NoTitleBar);
		Resources res = getResources();
		/*Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.di_sails_dark_gray_mdpi2); 
		getWindow().getDecorView().setBackground(drawable);
		*/

		
		display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screenWidth = size.x;
		
		// pale blue bg
		getWindow().getDecorView().setBackgroundColor(Color.parseColor("#AFC7C7"));
		
		List<Workout> workout = null;
		iViewCounter = 0;
		//Intent myScreen = new Intent(this, ExerciseScreen.class);
		//this.startActivity(myScreen);
		//DBAdapter adapter = new DBAdapter(this);
		
		helper = new WorkoutAdapter(this);
		try {
			helper.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			workoutCursor = helper.getAllWorkout();
		}catch (Exception e){
			
		}
		
		rl = new RelativeLayout(this);
    	rl.setId(iViewCounter++);
    	
    	// style 2
    	TextView tv = new TextView(this);    	
    	LayoutParams lpTopView = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
    	lpTopView.setMargins(15, 9, 9, 4);    
    	tv.setLayoutParams(lpTopView); 
    	tv.setId(iViewCounter++);
    	tv.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
    	tv.setText("Fitness Tracker");
    	tv.setTextColor(Color.BLACK);    	
    	rl.addView(tv);
    	
    	RelativeLayout topLevelView = new RelativeLayout(this);
    	topLevelView.setId(iViewCounter++);
    	
    	Button add = new Button(this);
		add.setText("New");
		add.setBackgroundDrawable(res.getDrawable(R.drawable.red_button));
		add.setWidth(80);
		add.setOnClickListener(onAddNewRow);
		add.setId(iViewCounter++);
		
		lpAdd = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	
		save = new Button(this);
		save.setText("Save");
		save.setId(iViewCounter++);
		save.setBackgroundDrawable(res.getDrawable(R.drawable.red_button));
		save.setWidth(80);
		save.setOnClickListener(onSave);
		
		lpSave = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lpSave.addRule(RelativeLayout.RIGHT_OF, add.getId());
		//minus 2 text box width
		lpSave.setMargins(screenWidth-80-80,0,0,0);
		
		
		topLevelView.addView(add, lpAdd);	
		topLevelView.addView(save, lpSave);
		
		lpTopView = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lpTopView.addRule(RelativeLayout.BELOW, tv.getId());
    	rl.addView(topLevelView, lpTopView);
    	
    	lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, topLevelView.getId());	
		
		controlHelper = new ControlHelper();
				
		Button bClear;
		
		if (workoutCursor != null && workoutCursor.moveToFirst()) {
	        do {
	        	/** adding relative layout */
	        	rlWorkoutRow = new RelativeLayout(this);
	    		lpWorkoutRow = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	    		rlWorkoutRow.setPadding(0, 10, 0, 0);
	    		rlWorkoutRow.setLayoutParams(lpWorkoutRow);
	    		rlWorkoutRow.setId(iViewCounter++);
	    		
	        	/**  add textview for workout name */
				vt = new TextView(this);
				vt.setId(iViewCounter++);
				vt.setWidth((int)(screenWidth * 0.8));  /** 80% width of screen */
				vt.setTag(workoutCursor.getString(0));
				vt.setText(workoutCursor.getString(1));				
				vt.setTextAppearance(this, R.style.workoutTextView);				
				vt.setBackgroundColor(Color.parseColor("#CFECEC"));
				vt.setSingleLine(true);
				vt.setOnClickListener(openExercise);
				rlWorkoutRow.addView(vt);
				
				/** add button for remove of workout name */
				lpAdd = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				lpAdd.addRule(RelativeLayout.RIGHT_OF, vt.getId());				
				bClear = new Button(this);
				bClear.setText("Del"); /** to be changed to delete image */
				bClear.setGravity(Gravity.RIGHT);
				bClear.setWidth((int)(screenWidth * 0.2)); /** 20% width of screen */
				rlWorkoutRow.addView(bClear, lpAdd);
				rl.addView(rlWorkoutRow, lp);
				
				lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.BELOW, rlWorkoutRow.getId());	
				
				
	        } while (workoutCursor.moveToNext());
    	}/*else{
    		
    		et = controlHelper.createEditText(this, "", 80, true, InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS, 80);
			et.setTextColor(Color.parseColor("#00ccff"));
			editTextList.add(et);
			rl.addView(et);
    	}*/
		
    	
    	
		this.setContentView(rl);
		//removeSplashScreen();
	}
	
	private View.OnClickListener openExercise = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent myScreen = new Intent(v.getContext(), ExerciseScreen.class);
			TextView vt = (TextView)v;
			myScreen.putExtra("workoutTitle", vt.getText());
			myScreen.putExtra("workoutRowID", vt.getTag().toString());
			helper.close();
			startActivity(myScreen);
		}
	};
	
	private View.OnClickListener onAddNewRow = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			LinearLayout llAddNewRow = new LinearLayout(WorkoutScreen.this);
	    	llAddNewRow.setOrientation(LinearLayout.HORIZONTAL);
	    	llAddNewRow.setId(iViewCounter++);
	    	
	    	et = controlHelper.createEditText(WorkoutScreen.this, "", "Enter workout name", 500, true, InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS, 80);	
			et.setHint("Enter workout name");
	    	editTextList.add(et);
			llAddNewRow.addView(et);		
			
			//View button = rl.getChildAt(rl.getChildCount()-1);
			//rl.removeViewAt(rl.getChildCount()-1);
			
			// need to check if this is the first row to be added or not; cos it will throw exception
			View lastLinearLayout = rl.getChildAt(rl.getChildCount()-1);
			
			
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, lastLinearLayout.getId());
			rl.addView(llAddNewRow, lp);	
			/*
			//retrieve save button from relativelayout and get re-add below the layout				
			lpSave = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lpSave.addRule(RelativeLayout.BELOW, llAddNewRow.getId());
			rl.addView(button, lpSave);*/
			
		}
	};
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String valList[] = new String[editTextList.size()];
			int i = 0;
			
			for (EditText editText : editTextList) {
				Log.d("MyApp",editText.getText().toString());
				valList[i++] = editText.getText().toString();
            }
			
			// save function
			for (int k=0 ; k<i ; k++){				
			//exercise.setExercise(e.getText().toString());		
				workout.setName(valList[k]);
				
				/** setting date */
				Time newTime = new Time();
				newTime.setToNow();
		    	workout.setDateCreated(newTime);
				workout.setOrderingValue(1);
				helper.createEntry(workout);
			}
			Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
			
			/** reload activity */
			onCreate(null);
			/*finish();
			startActivity(getIntent());*/
		}
	};
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		helper.close();
	}
	
	//** splash screen code */
	
	/**
	 * Removes the Dialog that displays the splash screen
	 */
	protected void removeSplashScreen() {
	    if (mSplashDialog != null) {
	        mSplashDialog.dismiss();
	        mSplashDialog = null;
	    }
	}
	 
	/**
	 * Shows the splash screen over the full Activity
	 */
	protected void showSplashScreen() {
	    mSplashDialog = new Dialog(this, R.style.SplashScreen);
	    mSplashDialog.setContentView(R.layout.test);
	    mSplashDialog.setCancelable(false);
	    mSplashDialog.show();
	     
	    // Set Runnable to remove splash screen just in case
	    final Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	      @Override
	      public void run() {
	        removeSplashScreen();
	      }
	    }, 100000);
	}
	 

}
