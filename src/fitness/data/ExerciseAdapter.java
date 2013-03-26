package fitness.data;

import java.sql.SQLException;

import fitness.model.Exercise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.text.format.Time;

public class ExerciseAdapter {
    public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_WEIGHT = "weight";
	public static final String KEY_SETS = "sets";
	public static final String KEY_REPS = "reps";
	public static final String KEY_TARGET = "target";
	public static final String KEY_TEMPO = "tempo";
	public static final String KEY_REST = "rest";
	public static final String KEY_DATE_ENTERED = "dateEntered";
	public static final String KEY_ORDERING_VALUE = "orderingValue";
	public static final String KEY_WORKOUTID = "workoutID";

    public static final String DATABASE_TABLE = "Exercise";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	//db.execSQL(DBAdapter.ExerciseTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
             db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        }
    }
   
    public ExerciseAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    
    public ExerciseAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        this.mDbHelper.close();
    }
   
    public long createEntry(Exercise exercise){
    	Cursor checkExercise = null;
    	
		checkExercise = this.getExercise(exercise.getExercise(), exercise.getDateEntered());
		
    	if (checkExercise.getCount() == 0){
        	ContentValues cv = new ContentValues();
        	cv.put(KEY_NAME, exercise.getExercise());
			cv.put(KEY_WEIGHT, exercise.getWeight());
			cv.put(KEY_SETS, exercise.getSets());
			cv.put(KEY_REPS, exercise.getReps());
			cv.put(KEY_TARGET, exercise.getTarget());
			cv.put(KEY_TEMPO, exercise.getTempo());
			cv.put(KEY_REST, exercise.getRest());			
			cv.put(KEY_WORKOUTID, exercise.getWorkoutID());
			cv.put(KEY_ORDERING_VALUE, exercise.getOrderingValue());
			/** setting date for exercise */ 
	    	long dtDob = exercise.getDateEntered().toMillis(true); 
			cv.put(KEY_DATE_ENTERED, DateFormat.format("dd-MM-yy", dtDob).toString());
			return this.mDb.insert(DATABASE_TABLE, null, cv);
    	}else{
    		if (this.updateExercise(exercise))
    			return 0;
    		else
    			return -1;
        }
    }
    
    public boolean deleteExercise(long rowId) {
        return this.mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0; 
    }
   
    public Cursor getAllExercise(Time dateCreated) {
    	long dtDob = dateCreated.toMillis(true);
    	Cursor mCursor = null;
        // Wrap the next line in try-catch
        try{
        	
        	mCursor = this.mDb.query(DATABASE_TABLE, new String[] {
        			KEY_NAME, KEY_WEIGHT, KEY_SETS, KEY_REPS, KEY_TARGET, KEY_TEMPO, KEY_REST, KEY_DATE_ENTERED }, KEY_DATE_ENTERED + " = '" + DateFormat.format("dd-MM-yy", dtDob).toString() + "'", 
        			  null, null, null, null, null);
        }catch(Exception e){
        	e.printStackTrace();        	
        }
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

        //return this.mDb.query(DATABASE_TABLE, new String[] { KEY_NAME,
        //		KEY_WEIGHT, KEY_SETS, KEY_REPS, KEY_TARGET, KEY_TEMPO, KEY_REST, KEY_DATE_ENTERED }, null, null, null, null, null);
    }
    
    public Cursor getAllExerciseName(){
    	String query ="SELECT DISTINCT " + KEY_NAME + ", 'id' as " + KEY_ROWID + " FROM " + DATABASE_TABLE;

        Cursor  cursor =  this.mDb.rawQuery(query,null);
          if (cursor != null) {
              cursor.moveToFirst();
          }
          return cursor;     
    }
    
    public Cursor getExercise(String exercise, Time dateEntered) {

        Cursor mCursor = null;
        // Wrap the next line in try-catch
        try{
        	//mCursor = this.mDb.rawQuery("SELECT name FROM " + DATABASE_TABLE +  " WHERE like '" + exercise + "%'", null);
        	long dtDob = dateEntered.toMillis(true); 
        	
        	mCursor = this.mDb.query(DATABASE_TABLE, new String[] {
        			  KEY_ROWID, KEY_NAME, KEY_SETS }, KEY_NAME + " = '" + exercise + "' AND " + KEY_DATE_ENTERED + " = '" + DateFormat.format("dd-MM-yy", dtDob).toString() + "'", 
        			  null, null, null, null, null);
        }catch(Exception e){
        	e.printStackTrace();        	
        }
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
   
    public boolean updateExercise(Exercise exercise){
    	ContentValues cv = new ContentValues();
		cv.put(KEY_WEIGHT, exercise.getWeight());
		cv.put(KEY_SETS, exercise.getSets());
		cv.put(KEY_REPS, exercise.getReps());
		cv.put(KEY_TARGET, exercise.getTarget());
		cv.put(KEY_TEMPO, exercise.getTempo());
		cv.put(KEY_REST, exercise.getRest());
		/** setting date for exercise */ 
		long dtDob = exercise.getDateEntered().toMillis(true); 
		cv.put(KEY_DATE_ENTERED, DateFormat.format("dd-MM-yy", dtDob).toString());		
		cv.put(KEY_WORKOUTID, exercise.getWorkoutID());
		cv.put(KEY_ORDERING_VALUE, exercise.getOrderingValue());

        return this.mDb.update(DATABASE_TABLE, cv, KEY_NAME + "='" + exercise.getExercise() + "'", null) >0; 
    }

}