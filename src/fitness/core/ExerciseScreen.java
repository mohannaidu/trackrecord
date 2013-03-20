package fitness.core;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fitness.data.ExerciseAdapter;
import fitness.model.Exercise;

import sra.gg.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;


public class ExerciseScreen extends Activity {
	private Exercise exercise = new Exercise();
	private List<EditText> editTextList = new ArrayList<EditText>();
	ExerciseAdapter helper;
	ControlHelper controlHelper; 
	ScrollView sv;
	RelativeLayout rl;
	LinearLayout ll;
	LayoutParams lp;
	LayoutParams lpSave;
	LayoutParams lpTopLevel;
	Button save;
	int iViewCounter = 1;
	LayoutParams lpAdd;
	Display display;
	Point size;
	int screenWidth;
	Double exercisePct;
    Double weightPct;
    Double setsPct;
    Double repsPct;
    Double targetPct;
    Double tempoPct;
    Double restPct;
	
	@SuppressLint({ "NewApi", "ResourceAsColor" })
	@SuppressWarnings("deprecation")
	@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			// style 1
			this.setTheme(android.R.style.Theme_Black_NoTitleBar);
			/*if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
				Resources res = getResources();
				Drawable drawable = res.getDrawable(R.drawable.di_sails_dark_gray_mdpi2); 
				getWindow().getDecorView().setBackground(drawable);
			}
			*/
			//pale blue bg
			getWindow().getDecorView().setBackgroundColor(Color.parseColor("#AFC7C7"));
			
			display = getWindowManager().getDefaultDisplay();
	        size = new Point();
	        display.getSize(size);
	        screenWidth = size.x;
			
			helper = new ExerciseAdapter(this);
			controlHelper = new ControlHelper();
			
			sv = new ScrollView(this);
			rl = new RelativeLayout(this);
							
			ll = new LinearLayout(this);
	    	ll.setOrientation(LinearLayout.HORIZONTAL);
	    	ll.setId(iViewCounter++);
	    	
	    	TextView tv = new TextView(this);
	    	tv.setWidth(screenWidth);
	    	//LayoutParams lpTopView = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
	    	//lpTopView.setMargins(15, 9, 9, 4);    
	    	
			tv.setText(">>>" + getIntent().getStringExtra("workoutTitle"));
			tv.setTextAppearance(this, R.style.workoutTextView);			
			tv.setBackgroundColor(Color.parseColor("#CFECEC"));
			tv.setId(iViewCounter++);
			ll.addView(tv);
	    	
			rl.addView(ll);
			
			Resources res = getResources();
			RelativeLayout topLevelView = new RelativeLayout(this);
			topLevelView.setId(iViewCounter++);
	    	
	    	Button add = new Button(this);
			add.setText("New");
			add.setBackground(res.getDrawable(R.drawable.red_button));
			add.setWidth(80);
			add.setOnClickListener(onAddNewRow);
			add.setId(iViewCounter++);
			
			lpAdd = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			//lpAdd.setMargins(300, 0, 0, 0);
			//lpAdd.addRule(RelativeLayout.LEFT_OF, ll.getId());
			
			save = new Button(this);
			save.setText("Save");
			save.setId(iViewCounter++);
			save.setBackground(res.getDrawable(R.drawable.red_button));
			save.setWidth(80);
			save.setOnClickListener(onSave);
			
			lpSave = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lpSave.addRule(RelativeLayout.RIGHT_OF, add.getId());
			//minus 2 text box width
			lpSave.setMargins(screenWidth-80-80,0,0,0);
			
			
			topLevelView.addView(add, lpAdd);	
			topLevelView.addView(save, lpSave);
			
			lpTopLevel = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lpTopLevel.addRule(RelativeLayout.BELOW, ll.getId());
	    	rl.addView(topLevelView, lpTopLevel);
	    	
	    	EditText et;
	    	Cursor exerciseCursor = null;
										
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, topLevelView.getId());		
			
			// adding textview for field description
			ll = new LinearLayout(this);
	    	ll.setOrientation(LinearLayout.HORIZONTAL);
	    	ll.setId(iViewCounter++);
	    	
	    	display = getWindowManager().getDefaultDisplay();
	        size = new Point();
	        display.getSize(size);
	        screenWidth = size.x;
	    	
	        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
		        exercisePct = 0.4;
		        weightPct = 0.1;
		        setsPct = 0.1;
		        repsPct = 0.1;
		        targetPct = 0.1;
		        tempoPct = 0.1;
		        restPct = 0.1;
	        }else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
		        exercisePct = 0.33;
		        weightPct = 0.167;
		        setsPct = 0.125;
		        repsPct = 0.125;
		        targetPct = 0.125;
		        restPct = 0.125;
	        }
	    			    	
			ll.addView(controlHelper.createTextView(this," Exercise", (int) (exercisePct * screenWidth), 20));	
			ll.addView(controlHelper.createTextView(this," Weight(kg)", (int) (weightPct * screenWidth), 20));
			ll.addView(controlHelper.createTextView(this," Sets", (int) (setsPct * screenWidth), 20));
			ll.addView(controlHelper.createTextView(this," Reps", (int) (repsPct * screenWidth), 20));
			ll.addView(controlHelper.createTextView(this," Target", (int) (targetPct * screenWidth), 20));
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
				ll.addView(controlHelper.createTextView(this," Tempo", (int) (tempoPct* screenWidth), 20));
			}
			ll.addView(controlHelper.createTextView(this," Rest(s)", (int) (restPct * screenWidth), 20));
	    
	    	rl.addView(ll, lp);	
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, ll.getId());	
			
			
			try {
				helper.open();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				exerciseCursor = helper.getAllExercise();
				exerciseCursor.moveToFirst();
			} catch (Exception e) {}
				
	        do {
				
		    	ll = new LinearLayout(this);
		    	ll.setOrientation(LinearLayout.HORIZONTAL);
		    	ll.setId(iViewCounter++);
		    	
		    	AutoCompleteTextView actv = controlHelper.createAutoCompleteText(ExerciseScreen.this,  (exerciseCursor == null) ?  "" : exerciseCursor.getString(0),  (int) (exercisePct * screenWidth), true, InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS, 80);
		    	String[] countries = getResources().getStringArray(R.array.muscle_array);
		    	ArrayAdapter<String> adapter =  new ArrayAdapter<String>(ExerciseScreen.this, android.R.layout.simple_dropdown_item_1line, countries);
		    	actv.setAdapter(adapter);
		    	editTextList.add(actv);
				ll.addView(actv);
				
				et = controlHelper.createEditText(this,  (exerciseCursor == null) ?  "" : exerciseCursor.getString(1), (int) (weightPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL, 5);
				editTextList.add(et);
				ll.addView(et);
								
				et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(2), (int) (setsPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 2);
				editTextList.add(et);
				ll.addView(et);
				
				et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(3), (int) (repsPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				ll.addView(et);
				
				et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(4), (int) (targetPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				ll.addView(et);
				
				if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(5), (int) (tempoPct* screenWidth), true, InputType.TYPE_CLASS_NUMBER, 4);
					ll.addView(et);
				}else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					et = controlHelper.createEditText(this,  (exerciseCursor != null) ? exerciseCursor.getString(5) : "0202", 60, true, InputType.TYPE_CLASS_NUMBER, 4);
				}
				editTextList.add(et);
				
				et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(6), (int) (restPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				ll.addView(et);
						
				rl.addView(ll, lp);	
				lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.BELOW, ll.getId());	
				
				if (exerciseCursor == null)
					break;
				
	        } while (exerciseCursor.moveToNext());		
	        
			sv.addView(rl);
			
			this.setContentView(sv);
		}
	
		@Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	       
	        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	        }
	    }
	
		private View.OnClickListener onAddNewRow = new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				LinearLayout llAddNewRow = new LinearLayout(ExerciseScreen.this);
		    	llAddNewRow.setOrientation(LinearLayout.HORIZONTAL);
		    	llAddNewRow.setId(iViewCounter++);
		    									
				EditText et;
				AutoCompleteTextView actv;
				
				display = getWindowManager().getDefaultDisplay();
		        size = new Point();
		        display.getSize(size);
		        screenWidth = size.x;
				
				if (ExerciseScreen.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					 
					Double exercisePct = 0.4;					 
			        Double weightPct = 0.1;
			        Double setsPct = 0.1;
			        Double repsPct = 0.1;
			        Double targetPct = 0.1;
			        Double tempoPct = 0.1;
			        Double restPct = 0.1;
			        
				}else if(ExerciseScreen.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
									

			        Double exercisePct = 0.33;
			        Double weightPct = 0.167;
			        Double setsPct = 0.125;
			        Double repsPct = 0.125;
			        Double targetPct = 0.125;
			        Double restPct = 0.125;
				}
				        
				actv = controlHelper.createAutoCompleteText(ExerciseScreen.this, "", (int) (exercisePct * screenWidth), true, InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS, 80);
		    		String[] countries = getResources().getStringArray(R.array.muscle_array);
		    	ArrayAdapter<String> adapter = 
		    	        new ArrayAdapter<String>(ExerciseScreen.this, android.R.layout.simple_dropdown_item_1line, countries);
		    	actv.setAdapter(adapter);
		    	actv.setTag(getIntent().getStringExtra("workoutRowID"));	
				editTextList.add(actv);	
				llAddNewRow.addView(actv);
				
				et = controlHelper.createEditText(ExerciseScreen.this, "",  (int) (weightPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL, 5);
				editTextList.add(et);
				llAddNewRow.addView(et);
								
				et = controlHelper.createEditText(ExerciseScreen.this, "", (int) (setsPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 2);
				editTextList.add(et);
				llAddNewRow.addView(et);
				
				et = controlHelper.createEditText(ExerciseScreen.this, "", (int) (repsPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				llAddNewRow.addView(et);
				
				et = controlHelper.createEditText(ExerciseScreen.this, "", (int) (targetPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				llAddNewRow.addView(et);
				
				if (ExerciseScreen.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					et = controlHelper.createEditText(ExerciseScreen.this, "", (int) (tempoPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 4);
					llAddNewRow.addView(et);
				}else if(ExerciseScreen.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					// assuming default tempo of 0202
					et = controlHelper.createEditText(ExerciseScreen.this, "0202", 60, true, InputType.TYPE_CLASS_NUMBER, 4);
				}
				editTextList.add(et);
								
				et = controlHelper.createEditText(ExerciseScreen.this, "", (int) (restPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				llAddNewRow.addView(et);							
								
				// need to check if this is the first row to be added or not; cos it will throw exception
				View lastLinearLayout = rl.getChildAt(rl.getChildCount()-1);
				
				
				lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.BELOW, lastLinearLayout.getId());
				rl.addView(llAddNewRow, lp);	
				
				/*//retrieve save button from relativelayout and get re-add below the layout				
				lpSave = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				lpSave.addRule(RelativeLayout.BELOW, llAddNewRow.getId());
				rl.addView(button, lpSave);*/
				
			}
		};
	
		private View.OnClickListener onSave = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Assumption is that 10 rows are max listed in the screen
				String valList[] = new String[editTextList.size()+10];
				int i = 0;
				
				
				/* 
				 * need to check for existing data before saving. 
				 * 		- if new, add new entry
				 * 		- if existing, update the entries								
				*/
				
				for (EditText editText : editTextList) {
					Log.d("MyApp",editText.getText().toString());
					if(i % 8 == 0)
						valList[i++] = editText.getTag().toString();
					valList[i++] = editText.getText().toString();
	            }
				
				// save function
				exercise = new Exercise();
				for (int k=0 ; k<i ; k++){	
					
				//exercise.setExercise(e.getText().toString());		
					switch(k % 8){
					case 0:
						exercise.setWorkoutID(valList[k]);
						break;
					case 1:
						exercise.setExercise(valList[k]);
						break;
					case 2:
						exercise.setWeight(valList[k]);
						break;
					case 3:
						exercise.setSets(valList[k]);
						break;
					case 4:
						exercise.setReps(valList[k]);
						break;
					case 5:
						exercise.setTarget(valList[k]);
						break;
					case 6:
						exercise.setTempo(valList[k]);
						break;
					case 7:
						exercise.setRest(valList[k]);
						//later need to be included in the screen
						exercise.setOrderingValue("0");							
						helper.createEntry(exercise);							
						exercise = new Exercise();
						break;
						
					}
				}
				Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
			}
		};
		
		@Override
		public void onDestroy(){
			super.onDestroy();
			helper.close();
		}
}

