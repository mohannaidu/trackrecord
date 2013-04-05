package fitness.core;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fitness.data.DBAdapter;
import fitness.model.Exercise;
import fitness.model.Set;

import sra.gg.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
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


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class ExerciseScreen extends Activity {
	private static final int MY_DATE_DIALOG_ID = 3;
	private Exercise exercise = new Exercise();
	private static Time exerciseDate = new Time();
	private List<TextView> editTextList = new ArrayList<TextView>();
	DBAdapter helper;
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
    Double setsPct;
    Double targetPct;
    Double tempoPct;
    Double restPct;
    Double orderingPct;
    Double enterPct;
    Double superSetPct;
    Double deletePct;
    int textMaxLength = 20;
    private static boolean bDateAvailable = false;
    
    private static int dbExercise = 0;
    private static int dbSets = 1;
    private static int dbTarget = 2;
    private static int dbTempo = 3;
    private static int dbRest = 4;
    private static int dbDateEntered = 5;
    private static int dbExerciseID = 7;
    
    private static final int noOfRows = 10;
    
	
	@SuppressLint({"ResourceAsColor" })
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
			this.setTitle(getIntent().getStringExtra("workoutTitle"));
			
			display = getWindowManager().getDefaultDisplay();
	        size = new Point();
	        display.getSize(size);
	        screenWidth = size.x;
			
			helper = DBAdapter.getInstance(this);
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
	    	
	    	ll = createTextViewRow(this, ll, getWindowManager().getDefaultDisplay());
	    	
	    	rl.addView(ll, lp);	
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, ll.getId());	
			
			
			
			try {
				/** Get all exercises logged for the selected day */
				exerciseCursor = helper.getAllExercise(getIntent().getStringExtra("workoutRowID"), exerciseDate);
				if (exerciseCursor.getCount() == 0)
					exerciseCursor = null;					
				else{
					exerciseCursor.moveToFirst();
					bDateAvailable = true;
				}
			} catch (Exception e) {}
				
			int rowNo = 1;
	        do {
				
		    	ll = new LinearLayout(this);
		    	ll.setOrientation(LinearLayout.HORIZONTAL);
		    	ll.setId(iViewCounter++);
		    	ll.setBaselineAligned(false);
		    	
		    	ll = createEditViewRow(this, ll, getWindowManager().getDefaultDisplay(), exerciseCursor, rowNo);
		    	rowNo++;
		    				
				/** need to check in debug */
				if (exerciseCursor != null){
					char[] date = new char[2];
					char[] month = new char[2];
					char[] year = new char[2];
					exerciseCursor.getString(dbDateEntered).getChars(0, 2, date, 0);
					exerciseCursor.getString(dbDateEntered).getChars(3, 5, month, 0);
					exerciseCursor.getString(dbDateEntered).getChars(6, 8, year, 0);
					
					exerciseDate.set(Integer.parseInt(new String(date)), Integer.parseInt(new String(month))-1, Integer.parseInt("20" + new String(year)));
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
		
		private String retrieveMuscle(String str)
		{
		    int num;
		    str = str.substring(0);
		    num = str.indexOf('|');
		    return str.substring(0,num);
		}
		
		
		private LinearLayout createEditViewRow(Context ctx, LinearLayout ll, Display display, Cursor exerciseCursor, int rowNo){
			EditText et;
		 	size = new Point();
	        display.getSize(size);
	        screenWidth = size.x;
	    	
	        if (ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
		        exercisePct = 0.4;setsPct = 0.075;targetPct = 0.075;tempoPct = 0.075;restPct = 0.075;orderingPct=0.075;deletePct=0.075;enterPct=0.075;superSetPct=0.075;
	        }else if (ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
		        exercisePct = 0.4;setsPct = 0.108;restPct = 0.108;orderingPct=0.108;enterPct=0.108;targetPct=0.108;tempoPct = 0.108;
	        }
			
			AutoCompleteTextView actv = controlHelper.createAutoCompleteText(ExerciseScreen.this,  (exerciseCursor == null) ?  "" : exerciseCursor.getString(dbExercise), "Enter exercise",  (int) (exercisePct * screenWidth), true, InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS, 80);
	    	String[] array = getResources().getStringArray(R.array.muscle_array);
	    	String arr[] = new String[array.length];
	    	for(int i=0 ; i<array.length ; i++)
	    		arr[i] = retrieveMuscle(array[i]);
	    	
	    	ArrayAdapter<String> adapter =  new ArrayAdapter<String>(ExerciseScreen.this, android.R.layout.simple_dropdown_item_1line, arr);
	    	actv.setAdapter(adapter);
	    	actv.setTag(getIntent().getStringExtra("workoutRowID"));
	    	editTextList.add(actv);ll.addView(actv);
										
			et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(dbSets), "4", (int) (setsPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 2);
			editTextList.add(et);
			ll.addView(et);
			
			et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(dbTarget), "8", (int) (targetPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
			editTextList.add(et);
			ll.addView(et);		
			
			et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(dbTempo), "0202", (int) (tempoPct* screenWidth), true, InputType.TYPE_CLASS_NUMBER, 4);
			editTextList.add(et);
			ll.addView(et);
			
			et = controlHelper.createEditText(this, (exerciseCursor == null) ?  "" : exerciseCursor.getString(dbRest), "30", (int) (restPct * screenWidth), true, InputType.TYPE_CLASS_NUMBER, 3);
			editTextList.add(et);
			ll.addView(et);
			
			Button enterScreen = new Button(this);
			enterScreen.setText("Enter");			
			enterScreen.setTag((exerciseCursor == null) ?  "" : exerciseCursor.getString(dbExerciseID));
			enterScreen.setOnClickListener(openSetScreen);
			enterScreen.setId(iViewCounter++);					
			enterScreen.setWidth((int) (enterPct* screenWidth));
			enterScreen.setHeight(40);
			editTextList.add(enterScreen);
			ll.addView(enterScreen);
			
			
			Button deleteRow = new Button(this);
			deleteRow.setText("Del");			
			deleteRow.setTag(rowNo);
			deleteRow.setOnClickListener(onDeleteRow);
			deleteRow.setId(iViewCounter++);		
			editTextList.add(deleteRow);
			/** delete button is visible only in landscape mode */
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
				deleteRow.setWidth((int) (deletePct* screenWidth));
				ll.addView(deleteRow);
			}
			
			Button createSuperSet = new Button(this);
			createSuperSet.setText("^");			
			createSuperSet.setTag(rowNo);
			createSuperSet.setOnClickListener(onSuperSet);
			createSuperSet.setId(iViewCounter++);		
			editTextList.add(createSuperSet);
			/** superset linking button is visible only in landscape mode */
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
				createSuperSet.setWidth((int) (superSetPct* screenWidth));
				ll.addView(createSuperSet);
			}
				
			
			Button ordering = new Button(this);
			ordering.setText("Up");
			ordering.setWidth((int) (orderingPct* screenWidth));
			ordering.setTag(rowNo);
			ordering.setOnClickListener(onMoveRowUp);
			ordering.setId(iViewCounter++);		
			editTextList.add(ordering);
			ll.addView(ordering);
			
			return ll;			
		}
		
		private LinearLayout createTextViewRow(Context ctx, LinearLayout ll, Display display){
			
	        size = new Point();
	        display.getSize(size);
	        screenWidth = size.x;
	    	
	        if (ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
		        exercisePct = 0.4;setsPct = 0.075;targetPct = 0.075;tempoPct = 0.075;restPct = 0.075;orderingPct=0.075;deletePct=0.075;enterPct=0.075;superSetPct=0.075;
	        }else if (ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
		        exercisePct = 0.4;setsPct = 0.108;restPct = 0.108;orderingPct=0.108;enterPct=0.108;targetPct=0.108;tempoPct = 0.108;
	        }
	    			    	
			ll.addView(controlHelper.createTextView(ctx," Exercise", (int) (exercisePct * screenWidth), textMaxLength));	
			
			ll.addView(controlHelper.createTextView(ctx," Sets", (int) (setsPct * screenWidth), textMaxLength));
			ll.addView(controlHelper.createTextView(ctx," Target", (int) (targetPct * screenWidth), textMaxLength));			
			ll.addView(controlHelper.createTextView(ctx," Tempo", (int) (tempoPct* screenWidth), textMaxLength));
			ll.addView(controlHelper.createTextView(ctx," Rest(s)", (int) (restPct * screenWidth), textMaxLength));
			ll.addView(controlHelper.createTextView(ctx," Edit", (int) (enterPct * screenWidth), textMaxLength));
			if (ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
				ll.addView(controlHelper.createTextView(ctx," Del", (int) (deletePct * screenWidth), textMaxLength));	
				ll.addView(controlHelper.createTextView(ctx," SuperSet", (int) (superSetPct * screenWidth), textMaxLength));	
			}
			ll.addView(controlHelper.createTextView(ctx," Order", (int) (restPct * screenWidth), textMaxLength));
			
			
			return ll;
		}
		
		private View.OnClickListener openSetScreen = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myScreen = new Intent(v.getContext(), SetScreen.class);
				TextView vt = (TextView)v;
				myScreen.putExtra("workoutTitle", getIntent().getStringExtra("workoutTitle"));
				myScreen.putExtra("fitnessDate", tvCalendar.getText());				
				myScreen.putExtra("exerciseID", vt.getTag().toString());
				helper.close();
				startActivity(myScreen);
			}
		};
	
		private View.OnClickListener onAddNewRow = new View.OnClickListener() {			

			@Override
			public void onClick(View v) {
				LinearLayout llAddNewRow = new LinearLayout(ExerciseScreen.this);
		    	llAddNewRow.setOrientation(LinearLayout.HORIZONTAL);
		    	llAddNewRow.setId(iViewCounter++);
		    	llAddNewRow.setBaselineAligned(false);
				
				display = getWindowManager().getDefaultDisplay();
				LinearLayout lastLinearLayout = (LinearLayout) rl.getChildAt(rl.getChildCount()-1);
				
				int i = 0;
				int rowNo = 0;
				for (TextView editText : editTextList) {
					if(i % 9 == 8){
						rowNo = Integer.parseInt(editText.getTag().toString());
					}
					i++;
				}
				llAddNewRow = createEditViewRow(ExerciseScreen.this, llAddNewRow, display, null, rowNo+1);								
				
				lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.BELOW, lastLinearLayout.getId());
				rl.addView(llAddNewRow, lp);				
			}
		};
		
		private View.OnClickListener onDeleteRow = new View.OnClickListener() {			

			@Override
			public void onClick(View v) {
				Button button;
				/** get row number to be moved */
				int rowMove = Integer.parseInt(v.getTag().toString()); 
				
				/** get total number of rows */
				LinearLayout lastLinearLayout = (LinearLayout) rl.getChildAt(rl.getChildCount()-1);
				button = (Button)lastLinearLayout.getChildAt(lastLinearLayout.getChildCount()-1);				
				int rowNo = Integer.parseInt(button.getTag().toString());			   
				
				LinearLayout[] linearLayout = new LinearLayout[rowNo];
				int rowNoRelativeLayout = rl.getChildCount();
				int rowNoEditText = rowNoRelativeLayout - rowNo;
				/** resetting button numbering */
				int iDeleteRowRemovedFromView = 0;
				int iNewNo = 1;
			    for (int i=1; i<=rowNo; i++){
			    	 /** if current row is the row to be deleted */
			    	if(i == rowMove){	
			    		rl.removeViewAt(rowNoEditText-1);	
			    		iDeleteRowRemovedFromView = 1;
			    		/** remove from edittextlist */
			    		Log.d("MyApp",String.valueOf(editTextList.size()));
			    		String exerciseName = "";
			    		for (int k=1;k< noOfRows; k++) {
			    			if (k==1)
			    				exerciseName = editTextList.get((i-1)*(noOfRows-1)).getText().toString();
			    			editTextList.remove((i-1)*(noOfRows-1));							
						}
			    		/** remove from database */
			    		helper.deleteExercise(exerciseName, exerciseDate);
			    	}else{
			    		/** all other rows */
			    		linearLayout[iNewNo-1] = (LinearLayout)rl.getChildAt(rowNoEditText - iDeleteRowRemovedFromView);
			    		/** get the update button and update the tag */
			    		button = (Button)linearLayout[iNewNo-1].getChildAt(linearLayout[iNewNo-1].getChildCount()-1);
			    		button.setTag(iNewNo);
			    		
			    		/** get the delete button and update the tag */
			    		button = (Button)linearLayout[iNewNo-1].getChildAt(linearLayout[iNewNo-1].getChildCount()-2);
			    		button.setTag(iNewNo++);
			    	}
			    	rowNoEditText++;
			    }
			    /** remove all editrowtext */
			    rl.removeViews(rowNoRelativeLayout-rowNo, rowNo - iDeleteRowRemovedFromView);
			    
			    /** get textview id and set layout params */
			    lastLinearLayout = (LinearLayout) rl.getChildAt(rl.getChildCount()-1);
			    lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.BELOW, lastLinearLayout.getId());
				
			    for (int i=0; i<(rowNo - iDeleteRowRemovedFromView); i++){
			    	rl.addView(linearLayout[i], lp);
			    	lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					lp.addRule(RelativeLayout.BELOW, linearLayout[i].getId());
			    }
			}
		};
		
		private View.OnClickListener onSuperSet = new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				//long iSaveRtnVal = 0;
				Button button;
				Button button2;
				String ssID;
				
				//Log.d("MyApp","SupersetKey: " + String.valueOf(iSaveRtnVal)); // supersetkey
				Log.d("MyApp","WorkoutID: " + getIntent().getStringExtra("workoutRowID")); //workoutid
				Log.d("MyApp","Row No: " + v.getTag().toString()); //rowNo
				
				/** get row number to be connected as superset to top */
				int rowConnect = Integer.parseInt(v.getTag().toString());
				
				/** get total number of rows */
				LinearLayout lastLinearLayout = (LinearLayout) rl.getChildAt(rl.getChildCount()-1);
				button = (Button)lastLinearLayout.getChildAt(lastLinearLayout.getChildCount()-1);				
				int rowNo = Integer.parseInt(button.getTag().toString());		
				int iNewNo = 0;
				
				if (rowNo > 1){
					LinearLayout[] linearLayout = new LinearLayout[rowNo];
					int rowNoRelativeLayout = rl.getChildCount();
					int rowNoEditText = rowNoRelativeLayout - rowNo;
				    for (int i=1; i<=rowNo; i++){
				    	 /** if current row is the row to be moved up */
				    	if(i == rowConnect){			    	
				    		linearLayout[iNewNo] = (LinearLayout)rl.getChildAt(rowNoEditText);
				    		/** get the button and update the tag */
				    		button = (Button)linearLayout[iNewNo].getChildAt(linearLayout[iNewNo].getChildCount()-4);
				    		Log.d("MyApp","Exercise ID to be connected: " +button.getTag().toString());//exercise ID
				    		
				    		linearLayout[iNewNo+1] = (LinearLayout)rl.getChildAt(rowNoEditText-1);
				    		button2 = (Button)linearLayout[iNewNo+1].getChildAt(linearLayout[iNewNo+1].getChildCount()-4);
				    		Log.d("MyApp","Exercise ID to be connected with: " + button2.getTag().toString());//exercise ID to be connected with
				    		
				    		Cursor checkSSID = null;				        	
				        	checkSSID = helper.getSuperSetID(button2.getTag().toString(), getIntent().getStringExtra("workoutRowID"));
				        	
				        	if (checkSSID.getString(0) == null)
				        		ssID  = String.valueOf(helper.getLastInsertRow());  
				        	else
				        		ssID =  checkSSID.getString(0);
				        	
				    		
				    		helper.updateExerciseSuperSet(ssID, getIntent().getStringExtra("workoutRowID"),button.getTag().toString());
				    		helper.updateExerciseSuperSet(ssID, getIntent().getStringExtra("workoutRowID"),button2.getTag().toString());
				    		Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
				    	}
				    	rowNoEditText++;
				    }
				}				  
			}
			
		};
		
		private View.OnClickListener onMoveRowUp = new View.OnClickListener() {			

			@Override
			public void onClick(View v) {
				Button button;
				/** get row number to be moved */
				int rowMove = Integer.parseInt(v.getTag().toString()); 
				
				/** get total number of rows */
				LinearLayout lastLinearLayout = (LinearLayout) rl.getChildAt(rl.getChildCount()-1);
				button = (Button)lastLinearLayout.getChildAt(lastLinearLayout.getChildCount()-1);				
				int rowNo = Integer.parseInt(button.getTag().toString());			   
				
				if (rowNo > 1){
					LinearLayout[] linearLayout = new LinearLayout[rowNo];
					int rowNoRelativeLayout = rl.getChildCount();
					int rowNoEditText = rowNoRelativeLayout - rowNo;
				    for (int i=1; i<=rowNo; i++){
				    	/** if current row is above the row to be moved up */
				    	if (i == (rowMove-1)){
				    		linearLayout[i-1] = (LinearLayout)rl.getChildAt(rowNoEditText+1);
				    		/** get the button and update the tag */
				    		button = (Button)linearLayout[i-1].getChildAt(linearLayout[i-1].getChildCount()-1);
				    		button.setTag(i);
				    	}else /** if current row is the row to be moved up */
				    		if(i == rowMove){			    	
				    		linearLayout[i-1] = (LinearLayout)rl.getChildAt(rowNoEditText-1);
				    		/** get the button and update the tag */
				    		button = (Button)linearLayout[i-1].getChildAt(linearLayout[i-1].getChildCount()-1);
				    		button.setTag(i);
				    	}else{
				    		/** all other rows */
				    		linearLayout[i-1] = (LinearLayout)rl.getChildAt(rowNoEditText);
				    		/** get the button and update the tag */
				    		button = (Button)linearLayout[i-1].getChildAt(linearLayout[i-1].getChildCount()-1);
				    		button.setTag(i);
				    	}
				    	rowNoEditText++;
				    }
				    /** remove all editrowtext */
				    rl.removeViews(rowNoRelativeLayout-rowNo, rowNo);
				    
				    /** get textview id and set layout params */
				    lastLinearLayout = (LinearLayout) rl.getChildAt(rl.getChildCount()-1);
				    lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					lp.addRule(RelativeLayout.BELOW, lastLinearLayout.getId());
					
				    for (int i=0; i<rowNo; i++){
				    	rl.addView(linearLayout[i], lp);
				    	lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
						lp.addRule(RelativeLayout.BELOW, linearLayout[i].getId());
				    }	
				}				  
			}
		};
		
	
		private View.OnClickListener onSave = new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// Assumption is that 10 rows are max listed in the screen
				String valList[] = new String[editTextList.size()+noOfRows];
				int i = 0;
				int iSets = 0;
				boolean bSaved = true;
				long iSaveRtnVal = 0;
				Set mySet;
				
				for (TextView editText : editTextList) {
					Log.d("MyApp",editText.getText().toString());
					if(i % noOfRows == 0){
						valList[i++] = editText.getTag().toString();
					}
					if(i % noOfRows == (noOfRows-1)){
						valList[i++] = editText.getTag().toString();
					}else{
						valList[i++] = editText.getText().toString();
					}
	            }
				
				// save function
				exercise = new Exercise();
				for (int k=0 ; k<i ; k++){	
					
				//exercise.setExercise(e.getText().toString());		
					switch(k % (noOfRows-1)){
					case 0:
						exercise.setWorkoutID(valList[k]);
						break;
					case 1:
						exercise.setExercise(valList[k]);
						break;
					case 2:
						exercise.setSets(valList[k]);
						break;
					case 3:
						exercise.setTarget(valList[k]);
						break;
					case 4:
						exercise.setTempo(valList[k]);
						break;
					case 5:
						exercise.setRest(valList[k]);
						break;
					case 6:
						/* for enter button */
						break;
					case 7:
						/* for delete button */
						break;	
					case 8:
						/* for superset button */
						break;
					case 9:
						exercise.setOrderingValue(valList[k]);
						
						/** setting date for exercise */ 	
				    	exercise.setDateEntered(exerciseDate);
				    	
				    	iSaveRtnVal	= helper.createEntry(exercise);											
						if (iSaveRtnVal == -1)
							bSaved = false;	
						
						/** pre-save data in sets table */
						/** after saving, buttons in linearlayout are not loaded with new tag values... throws error if "Enter" button is pressed right after save */
						if (iSaveRtnVal >= 1 ){							
							
							for (iSets=1; iSets<=Integer.parseInt(exercise.getSets());iSets++){
								mySet = new Set();
								mySet.setExerciseID(String.valueOf(iSaveRtnVal));
								mySet.setReps(exercise.getTarget());
								mySet.setOrderingValue(String.valueOf(iSets));
								helper.createEntry(mySet);
							}
						}
						
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
			                       //Toast.makeText(ExerciseScreen.this,"Date picked: " + strDate, Toast.LENGTH_SHORT).show();
			                       /** restart the whole screen with updated values */
			                       finish();
			  			           startActivity(getIntent());
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
			for (TextView editText : editTextList) {
				editText.setFocusable(false);				
				editText.setFocusableInTouchMode(false);
            }
		}
		  
		
}

