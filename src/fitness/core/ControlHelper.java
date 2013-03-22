package fitness.core;

import sra.gg.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class ControlHelper {
	
	EditText et;
	TextView vt;
	InputFilter[] FilterArray;
	AutoCompleteTextView actv;
	
	public EditText createEditText(Context ctx, String sValue, String sHint, int iWidth, boolean selectOnFocus, int sInputType, int maxLength){
		et = new EditText(ctx);
		et.setText(sValue);
		et.setHint(sHint);
		et.setWidth(iWidth);
		et.setSelectAllOnFocus(selectOnFocus);
		et.setFocusable(false);
		et.setFocusableInTouchMode(false);
		et.setOnClickListener(editTextFocusable);
		//et.setOnFocusChangeListener(editTextFocus);
		et.setInputType(sInputType);
		FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(maxLength);
		et.setFilters(FilterArray);
				
		return et;
	}
	
	public AutoCompleteTextView  createAutoCompleteText(Context ctx, String sValue, String sHint, int iWidth, boolean selectOnFocus, int sInputType, int maxLength){
		actv = new AutoCompleteTextView(ctx);
		actv.setText(sValue);
		actv.setWidth(iWidth);
		actv.setHint(sHint);
		actv.setDropDownWidth(400);
		actv.setSelectAllOnFocus(selectOnFocus);
		actv.setFocusable(false);
		actv.setFocusableInTouchMode(false);
		actv.setOnClickListener(editTextFocusable);
		//actv.setOnFocusChangeListener(editTextFocus);
		//actv.setInputType(sInputType);
		FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(maxLength);
		actv.setFilters(FilterArray);
				
		return actv;
	}
	
	@SuppressLint("ResourceAsColor")
	public TextView createTextView(Context ctx, String sValue, int iWidth, int iHeight){
		vt = new TextView(ctx);
		vt.setText(sValue);
		vt.setWidth(iWidth);
		vt.setHeight(iHeight);
		vt.setTextColor(R.color.gray);
				
		return vt;
	}
	
	private View.OnClickListener editTextFocusable = new View.OnClickListener() {
		
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			EditText et = (EditText)v;
			et.setFocusable(true);
			et.setFocusableInTouchMode(true);
			et.requestFocus();
		}
	};
	
	/*private OnFocusChangeListener editTextFocus =  new OnFocusChangeListener() {
		  public void onFocusChange(View view, boolean gainFocus) {
		   //onFocus
		   if (gainFocus) {
		    //set the text
		    //((EditText) view).setText("In focus now");
		   } 
		   //onBlur
		   else {
		    //clear the text
		    ((EditText) view).setFocusable(false);
		    ((EditText) view).setFocusableInTouchMode(false);
		   }
		  };
		 };*/
}
