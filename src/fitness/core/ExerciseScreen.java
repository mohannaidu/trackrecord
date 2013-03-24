package fitness.core;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fitness.data.ExerciseAdapter;
import fitness.model.Exercise;

import sra.gg.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
	private static final int MY_DATE_DIALOG_ID = 3;
	private Exercise exercise = new Exercise();
	private static Time exerciseDate = new Time();
	private List<EditText> editTextList = new ArrayList<EditText>();
	ExerciseAdapter helper;
	TextView tvCalendar;
	ControlHelper controlHelper; 
	ScrollView sv;
	RelativeLayout rl;
	RelativeLayout rlTopView;
	LinearLayout ll;
	LayoutParams lp;
	LayoutParams lpSave;
	LayoutParams lpTopLevel;	
	LayoutParams lpTopView;
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
    private static boolean bDateAvailable = false;
	
	@SuppressLint({ "NewApi", "ResourceAsColor" })
	@SuppressWarnings("deprecation")
	@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			// style 1
			//this.setTheme(android.R.style.Theme_Black_NoTitleBar);
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
							
			/*ll = new LinearLayout(this);
	    	ll.setOrientation(LinearLayout.HORIZONTAL);
	    	ll.setId(iViewCounter++);
	    	*/
			rlTopView = new RelativeLayout(this);
			lpTopView = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			rlTopView.setPadding(0, 10, 0, 10);
			rlTopView.setLayoutParams(lpTopView);
			rlTopView.setId(iViewCounter++);
			
	    	/** Workout Title */
	    	TextView tv = new TextView(this);
	    	tv.setWidth(screenWidth/2);
	    	tv.setId(iViewCounter++);
			tv.setText(">>>" + getIntent().getStringExtra("workoutTitle"));
			tv.setTextAppearance(this, R.style.exerciseTextView);			
			tv.setBackgroundColor(Color.parseColor("#CFECEC"));
			tv.setId(iViewCounter++);
			rlTopView.addView(tv);
			//ll.addView(tv);
			
			/** Calendar Display */
			lpAdd = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lpAdd.addRule(RelativeLayout.RIGHT_OF, tv.getId());
			tvCalendar = new TextView(this);
			tvCalendar.setWidth(screenWidth/2);
			tvCalendar.setGravity(Gravity.RIGHT);
	    	
	    	// should initialize in the different place cos if rotate will reset to old value
			if (!bDateAvailable)
				exerciseDate.setToNow();
	    	long dtDob = exerciseDate.toMillis(true);    	
	    	tvCalendar.setText(DateFormat.format("dd-MM-yy", dtDob));
	    	tvCalendar.setTextAppearance(this, R.style.calendarTextView);		
	    	tvCalendar.setBackgroundColor(getResources().getColor(R.color.orange));
	    	tvCalendar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showDialog(MY_DATE_DIALOG_ID);
				}
			});
	    	tvCalendar.setId(iViewCounter++);
			
			rlTopView.addView(tvCalendar, lpAdd);			
			rl.addView(rlTopView);
			
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
			lpTopLevel.addRule(RelativeLayout.BELOW, rlTopView.getId());
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
				/** Get all exercises logged for the selected day */
				exerciseCursor = helper.getAllExercise(exerciseDate);
				if (exerciseCursor.getCount() == 0)
					exerciseCursor = null;					
				else{
					exerciseCursor.moveToFirst();
					bDateAvailable = true;
				}
			} catch (Exception e) {}
				
	        do {
				
		    	ll = new LinearLayout(this);
		    	ll.setOrientation(LinearLayout.HORIZONTAL);
		    	ll.setId(iViewCounter++);
		    	
		    	AutoCompleteTextView actv = controlHelper.createAutoCompleteText(ExerciseScreen.this,  (exerciseCursor == null) ?  "" : exerciseCursor.getString(0), "Enter exercise",  (int) (exercisePct * screenWidth), true, InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS, 80);
		    	String[] countries = getResources().getStringArray(R.array.muscle_array);
		    	ArrayAdapter<String> adapter =  new ArrayAdapter<String>(ExerciseScreen.this, android.R.layout.simple_dropdown_item_1line, countries);
		    	actv.setAdapter(adapter);
		    	actv.setTag(getIntent().getStringExtra("workoutRowID"));
		    	editTextList.add(actv);
				ll.addView(actv);
				
				et = controlHelper.createEditText(this,  (exerciseCursor == null) ?  "" : exerciseCursor.getString(1),"10.0", (int) (weightPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL, 5);
				editTextList.add(et);
				ll.addView(et);
								
				et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(2), "4", (int) (setsPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 2);
				editTextList.add(et);
				ll.addView(et);
				
				et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(3), "8", (int) (repsPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				ll.addView(et);
				
				et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(4), "8", (int) (targetPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				ll.addView(et);
				
				if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(5), "0202", (int) (tempoPct* screenWidth), true, InputType.TYPE_CLASS_NUMBER, 4);
					ll.addView(et);
				}else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					et = controlHelper.createEditText(this,  (exerciseCursor != null) ? exerciseCursor.getString(5) : "0202", "0202", 60, true, InputType.TYPE_CLASS_NUMBER, 4);
				}
				editTextList.add(et);
				
				et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(6), "30", (int) (restPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				ll.addView(et);
							
				/** need to check in debug */
				if (exerciseCursor != null){
					char[] date = new char[2];
					char[] month = new char[2];
					char[] year = new char[2];
					exerciseCursor.getString(7).getChars(0, 2, date, 0);
					exerciseCursor.getString(7).getChars(3, 2, month, 0);
					exerciseCursor.getString(7).getChars(6, 2, year, 0);
					
					exerciseDate.set(Integer.parseInt(new String(date)), Integer.parseInt(new String(month)), Integer.parseInt(new String(year)));
					dtDob = exerciseDate.toMillis(true);    	
			    	tvCalendar.setText(DateFormat.format("dd-MM-yy", dtDob));
										
				}
					
						
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
				        
				actv = controlHelper.createAutoCompleteText(ExerciseScreen.this, "",  "Enter exercise", (int) (exercisePct * screenWidth), true, InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS, 80);
		    		String[] countries = getResources().getStringArray(R.array.muscle_array);
		    	ArrayAdapter<String> adapter = 
		    	        new ArrayAdapter<String>(ExerciseScreen.this, android.R.layout.simple_dropdown_item_1line, countries);
		    	actv.setAdapter(adapter);
		    	actv.setTag(getIntent().getStringExtra("workoutRowID"));	
				editTextList.add(actv);	
				llAddNewRow.addView(actv);
				
				et = controlHelper.createEditText(ExerciseScreen.this, "", "10.0",  (int) (weightPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL, 5);
				editTextList.add(et);
				llAddNewRow.addView(et);
								
				et = controlHelper.createEditText(ExerciseScreen.this, "", "4", (int) (setsPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 2);
				editTextList.add(et);
				llAddNewRow.addView(et);
				
				et = controlHelper.createEditText(ExerciseScreen.this, "", "8", (int) (repsPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				llAddNewRow.addView(et);
				
				et = controlHelper.createEditText(ExerciseScreen.this, "", "8", (int) (targetPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
				editTextList.add(et);
				llAddNewRow.addView(et);
				
				if (ExerciseScreen.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					et = controlHelper.createEditText(ExerciseScreen.this, "", "0202",(int) (tempoPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 4);
					llAddNewRow.addView(et);
				}else if(ExerciseScreen.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					// assuming default tempo of 0202
					et = controlHelper.createEditText(ExerciseScreen.this, "0202", "0202", 60, true, InputType.TYPE_CLASS_NUMBER, 4);
				}
				editTextList.add(et);
								
				et = controlHelper.createEditText(ExerciseScreen.this, "", "30", (int) (restPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
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
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// Assumption is that 10 rows are max listed in the screen
				String valList[] = new String[editTextList.size()+10];
				int i = 0;
				boolean bSaved = true;
				
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
						
						/** setting date for exercise */ 	
				    	exercise.setDateEntered(exerciseDate);
						
						//later need to be included in the screen
						exercise.setOrderingValue("0");						
						if (helper.createEntry(exercise) == -1)
							bSaved = false;						
						exercise = new Exercise();
						break;
						
					}
					
					if (!bSaved)
						break;
				}
				if (bSaved){
					Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
					releaseFocus();
				}
			}
		};
		
		@Override
		public void onDestroy(){
			super.onDestroy();
			helper.close();
		}
		
		protected Dialog onCreateDialog(int id) {
			switch (id) {
			case MY_DATE_DIALOG_ID:
			    DatePickerDialog dateDlg = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			            public void onDateSet(DatePicker view, int year,
			                                                int monthOfYear, int dayOfMonth)
			            {
			                       Time chosenDate = new Time();
			                       chosenDate.set(dayOfMonth, monthOfYear, year);
			                       exerciseDate = chosenDate;
			                       bDateAvailable = true;
			                       long dtDob = exerciseDate.toMillis(true);			                       
			                       CharSequence strDate = DateFormat.format("dd-MM-yy", dtDob);
			                       tvCalendar.setText(strDate);
			                       Toast.makeText(ExerciseScreen.this,
			                            "Date picked: " + strDate, Toast.LENGTH_SHORT).show();
			           }}, 2011,0, 1);
			         dateDlg.setMessage("When's Your Workout?");
			         return dateDlg;
			}
			return null;
		}
		
		@Override
		protected void onPrepareDialog(final int id, final Dialog dialog) {
		  switch (id) {
		  case MY_DATE_DIALOG_ID:
			  DatePickerDialog dateDlg = (DatePickerDialog) dialog;
			     int iDay,iMonth,iYear;
			     Calendar cal = Calendar.getInstance();
			     iDay = cal.get(Calendar.DAY_OF_MONTH);
			     iMonth = cal.get(Calendar.MONTH);
			     iYear = cal.get(Calendar.YEAR);
			     dateDlg.updateDate(iYear, iMonth, iDay);
			     break;
		  }
		}
		
		private void releaseFocus(){
			for (EditText editText : editTextList) {
				editText.setFocusable(false);				
				editText.setFocusableInTouchMode(false);
            }
		}
		  
		  
}

