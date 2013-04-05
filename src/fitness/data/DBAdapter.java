package fitness.data;

import fitness.model.Exercise;
import fitness.model.Set;
import fitness.model.Workout;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.text.format.Time;

public class DBAdapter extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "fitness.db"; 	
    public static final String WORKOUT_DATABASE_TABLE = "workout";	
	public static final String EXERCISE_DATABASE_TABLE = "exercise";
	public static final String SUPERSETKEY_DATABASE_TABLE = "supersetkey";
	public static final String SUPERSET_DATABASE_TABLE = "superset";
	public static final String SET_DATABASE_TABLE = "sets";
    public static final int DATABASE_VERSION = 1;
    
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SUPERSETID = "ssID";
    public static final String KEY_ORDERING_VALUE = "orderingValue";
    public static final String KEY_REPS = "reps";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_EXERCISEID = "exerciseID";
	public static final String KEY_NAME = "name";
	public static final String KEY_SETS = "sets";
	public static final String KEY_TARGET = "target";
	public static final String KEY_TEMPO = "tempo";
	public static final String KEY_REST = "rest";
	public static final String KEY_DATE_ENTERED = "dateEntered";
	public static final String KEY_WORKOUTID = "workoutID";
	public static final String KEY_DATE_CREATED = "dateCreated";


    
	public static final String WorkoutTable = "CREATE TABLE " + WORKOUT_DATABASE_TABLE + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NAME + " TEXT, " +
			KEY_DATE_CREATED + " TEXT, " + 
			KEY_ORDERING_VALUE + " TEXT);";
	
	public static final String ExerciseTable = "CREATE TABLE " + EXERCISE_DATABASE_TABLE + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NAME + " TEXT, " +
			KEY_SETS + " TEXT, " + 
			KEY_TARGET + " TEXT, " + 
			KEY_TEMPO + " TEXT, " + 
			KEY_REST + " TEXT, " + 
			KEY_DATE_ENTERED + " TEXT, " + 
			KEY_ORDERING_VALUE + " TEXT, " +
			KEY_WORKOUTID + " TEXT, "  +
			KEY_SUPERSETID + " TEXT);";
 
	public static final String SuperSetKeyTable = "CREATE TABLE " + SUPERSETKEY_DATABASE_TABLE + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT);";

	public static final String SuperSetTable = "CREATE TABLE " + SUPERSET_DATABASE_TABLE + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_SUPERSETID + " TEXT, " +
			KEY_ORDERING_VALUE + " TEXT);";
	
	public static final String SetTable = "CREATE TABLE " + SET_DATABASE_TABLE + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_WEIGHT + " TEXT, " + 
			KEY_REPS + " TEXT, " + 			
			KEY_ORDERING_VALUE + " TEXT, " +
			KEY_EXERCISEID + " TEXT);";

	
	private static DBAdapter  mInstance = null;
		
		DBAdapter(Context context) {
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }
        
        public static DBAdapter getInstance(Context ctx) {
            /** 
             * use the application context as suggested by CommonsWare.
             * this will ensure that you dont accidentally leak an Activitys
             * context (see this article for more information: 
             * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
             */
            if (mInstance == null) {
                mInstance = new DBAdapter(ctx.getApplicationContext());
            }
            return mInstance;
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
        	db.execSQL(DBAdapter.WorkoutTable);
        	db.execSQL(DBAdapter.ExerciseTable);
        	db.execSQL(DBAdapter.SetTable);
        	db.execSQL(DBAdapter.SuperSetTable);
        	db.execSQL(DBAdapter.SuperSetKeyTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	       	 db.execSQL("DROP TABLE IF EXISTS " + WORKOUT_DATABASE_TABLE);
	         db.execSQL("DROP TABLE IF EXISTS " + EXERCISE_DATABASE_TABLE);
	         db.execSQL("DROP TABLE IF EXISTS " + SET_DATABASE_TABLE);
	         db.execSQL("DROP TABLE IF EXISTS " + SUPERSET_DATABASE_TABLE);
	         db.execSQL("DROP TABLE IF EXISTS " + SUPERSETKEY_DATABASE_TABLE);
        }
        
        public void close() {
        	 DBAdapter.mInstance = null;
        }
        
 /** workout CRUD */
        
        public long createEntry(Workout workout){
        	Cursor checkWorkout = null;
        	
        	checkWorkout = this.getWorkout(workout.getName());

        	if (checkWorkout.getCount() == 0){
        		ContentValues cv = new ContentValues();
	            cv.put(KEY_NAME, workout.getName());
	            /** setting date for exercise */ 
	        	long dtDob = workout.getDateCreated().toMillis(true); 
	    		cv.put(KEY_DATE_CREATED, DateFormat.format("dd-MM-yy", dtDob).toString());
	            cv.put(KEY_ORDERING_VALUE, workout.getOrderingValue());
	            return DBAdapter.mInstance.getWritableDatabase().insert(WORKOUT_DATABASE_TABLE, null, cv);
        	}else{
        		if (this.updateWorkout(workout))
        			return 0;
        		else
        			return -1;
        	}
        }
        
        public Cursor getWorkout(String workoutName) {

            Cursor mCursor = null;
            // Wrap the next line in try-catch
            try{
            	mCursor = DBAdapter.mInstance.getWritableDatabase().query(WORKOUT_DATABASE_TABLE, new String[] {
            			  KEY_ROWID}, KEY_NAME + " = '" + workoutName + "'", 
            			  null, null, null, null, null);
            }catch(Exception e){
            	e.printStackTrace();        	
            }
            
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        }
        
        public boolean updateWorkout(Workout workout){
        	ContentValues cv = new ContentValues();        	
        	long dtDob = workout.getDateCreated().toMillis(true); 
    		cv.put(KEY_DATE_CREATED, DateFormat.format("dd-MM-yy", dtDob).toString());
            cv.put(KEY_ORDERING_VALUE, workout.getOrderingValue());

            return DBAdapter.mInstance.getWritableDatabase().update(EXERCISE_DATABASE_TABLE, cv, KEY_NAME + "='" + workout.getName() + "'", null) >0; 
        }
        
        public boolean deleteWorkout(String workoutName) {
        	Cursor mCursor = null;
            // Wrap the next line in try-catch
            try{
            	
            	mCursor = DBAdapter.mInstance.getWritableDatabase().query(WORKOUT_DATABASE_TABLE, new String[] {
            			KEY_ROWID }, KEY_NAME + " = '" + workoutName + "'", 
            			  null, null, null, null, null);
            }catch(Exception e){
            	e.printStackTrace();        	
            }
            
            if (mCursor != null) {
                mCursor.moveToFirst();
                DBAdapter.mInstance.getWritableDatabase().delete(EXERCISE_DATABASE_TABLE, KEY_WORKOUTID + " = '" +  mCursor.getString(0) + "'", null);
            }
            return DBAdapter.mInstance.getWritableDatabase().delete(WORKOUT_DATABASE_TABLE, KEY_NAME + " = '" + workoutName + "'", null) > 0; 
        }
       
        public Cursor getAllWorkout() {

            return DBAdapter.mInstance.getWritableDatabase().query(WORKOUT_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
            		KEY_DATE_CREATED, KEY_ORDERING_VALUE }, null, null, null, null, null);
        }
        
        
        /** exercise CRUD */
        
        public long createEntry(Exercise exercise){
        	Cursor checkExercise = null;
        	
    		checkExercise = this.getExercise(exercise.getExercise(), exercise.getDateEntered());
    		
        	if (checkExercise.getCount() == 0){
            	ContentValues cv = new ContentValues();
            	cv.put(KEY_NAME, exercise.getExercise());
    			cv.put(KEY_SETS, exercise.getSets());
    			cv.put(KEY_TARGET, exercise.getTarget());
    			cv.put(KEY_TEMPO, exercise.getTempo());
    			cv.put(KEY_REST, exercise.getRest());			
    			cv.put(KEY_WORKOUTID, exercise.getWorkoutID());
    			cv.put(KEY_ORDERING_VALUE, exercise.getOrderingValue());
    			/** setting date for exercise */ 
    	    	long dtDob = exercise.getDateEntered().toMillis(true); 
    			cv.put(KEY_DATE_ENTERED, DateFormat.format("dd-MM-yy", dtDob).toString());
    			return DBAdapter.mInstance.getWritableDatabase().insert(EXERCISE_DATABASE_TABLE, null, cv);
        	}else{
        		if (this.updateExercise(exercise))
        			return 0;
        		else
        			return -1;
            }
        }
        
        public boolean deleteExercise(String exerciseName, Time dateCreated) {
        	long dtDob = dateCreated.toMillis(true); 
            return DBAdapter.mInstance.getWritableDatabase().delete(EXERCISE_DATABASE_TABLE, KEY_NAME + " = '" + exerciseName + "' AND " + KEY_DATE_ENTERED + " = '" + DateFormat.format("dd-MM-yy", dtDob).toString() + "'", null) > 0; 
        }
       
        public Cursor getAllExercise(String workoutNo, Time dateCreated) {
        	long dtDob = dateCreated.toMillis(true);
        	Cursor mCursor = null;
            // Wrap the next line in try-catch
            try{
            	
            	mCursor = DBAdapter.mInstance.getWritableDatabase().query(EXERCISE_DATABASE_TABLE, new String[] {
            			KEY_NAME, KEY_SETS, KEY_TARGET, KEY_TEMPO, KEY_REST, KEY_DATE_ENTERED, KEY_ORDERING_VALUE, KEY_ROWID }, KEY_DATE_ENTERED + " = '" + DateFormat.format("dd-MM-yy", dtDob).toString() + "' AND " + KEY_WORKOUTID + " = '" +  workoutNo + "'", 
            			  null, null, null, KEY_ORDERING_VALUE + " ASC", null);
            }catch(Exception e){
            	e.printStackTrace();        	
            }
            
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        }
        
        public Cursor getAllExerciseName(){
        	String query ="SELECT DISTINCT " + KEY_NAME + ", 'id' as " + KEY_ROWID + " FROM " + EXERCISE_DATABASE_TABLE;

            Cursor  cursor =  DBAdapter.mInstance.getWritableDatabase().rawQuery(query,null);
              if (cursor != null) {
                  cursor.moveToFirst();
              }
              return cursor;     
        }
        
        public Cursor getExercise(String exercise, Time dateEntered) {

            Cursor mCursor = null;
            // Wrap the next line in try-catch
            try{
            	//mCursor = DBAdapter.mInstance.getWritableDatabase().rawQuery("SELECT name FROM " + DATABASE_TABLE +  " WHERE like '" + exercise + "%'", null);
            	long dtDob = dateEntered.toMillis(true); 
            	
            	mCursor = DBAdapter.mInstance.getWritableDatabase().query(EXERCISE_DATABASE_TABLE, new String[] {
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
        
        public Cursor getExercise(String exercise) {

            Cursor mCursor = null;
            
            try{
            	
            	mCursor = DBAdapter.mInstance.getWritableDatabase().query(EXERCISE_DATABASE_TABLE, new String[] {
            			  KEY_NAME, KEY_DATE_ENTERED}, KEY_NAME + " = '" + exercise + "'", 
            			  null, null, null, KEY_DATE_ENTERED + " ASC", null);
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
    		cv.put(KEY_SETS, exercise.getSets());
    		cv.put(KEY_TARGET, exercise.getTarget());
    		cv.put(KEY_TEMPO, exercise.getTempo());
    		cv.put(KEY_REST, exercise.getRest());
    		/** setting date for exercise */ 
    		long dtDob = exercise.getDateEntered().toMillis(true); 
    		cv.put(KEY_DATE_ENTERED, DateFormat.format("dd-MM-yy", dtDob).toString());		
    		cv.put(KEY_WORKOUTID, exercise.getWorkoutID());
    		cv.put(KEY_ORDERING_VALUE, exercise.getOrderingValue());

            return DBAdapter.mInstance.getWritableDatabase().update(EXERCISE_DATABASE_TABLE, cv, KEY_NAME + "='" + exercise.getExercise() + "' AND " + KEY_DATE_ENTERED + " = '" + DateFormat.format("dd-MM-yy", dtDob).toString() + "'", null) >0; 
        }
        
        /** set CRUD */
        public long createEntry(Set mySet){    	
        	ContentValues cv = new ContentValues();
        	cv.put(KEY_EXERCISEID, mySet.getExerciseID());
			cv.put(KEY_REPS, mySet.getReps());				
			cv.put(KEY_ORDERING_VALUE, mySet.getOrderingValue());			
			return DBAdapter.mInstance.getWritableDatabase().insert(SET_DATABASE_TABLE, null, cv);    	
        }
        
        public boolean updateSet(Set mySet){
        	ContentValues cv = new ContentValues();
			cv.put(KEY_REPS, mySet.getReps());			
			cv.put(KEY_ORDERING_VALUE, mySet.getOrderingValue());;
    		cv.put(KEY_WEIGHT, mySet.getWeight());

            return DBAdapter.mInstance.getWritableDatabase().update(SET_DATABASE_TABLE, cv, KEY_ROWID + "=" + mySet.getId(), null) >0; 
        }
        
        public Cursor getAllSets(String id) {
        	
        	Cursor mCursor = null;
            // Wrap the next line in try-catch
            try{
            	String sql = "select s._id, name, weight, reps  from sets s, exercise e where s.exerciseid = e._id and s.exerciseid = ? order by s.orderingValue asc" ;
            	mCursor = DBAdapter.mInstance.getWritableDatabase().rawQuery(sql, new String [] {id});
            		
            }catch(Exception e){
            	e.printStackTrace();        	
            }
            
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        }
        
        /** superset CRUD */
        public long getLastInsertRow(){    	
        	ContentValues cv = new ContentValues();    
			return DBAdapter.mInstance.getWritableDatabase().insert(SUPERSETKEY_DATABASE_TABLE, "_id", cv);    	
        }
        
        public boolean updateExerciseSuperSet(String ssID, String workoutID, String exerciseID){
        	ContentValues cv = new ContentValues();
        
        	/** need to check for giant set */
        	cv.put(KEY_SUPERSETID, ssID);
        	
            return DBAdapter.mInstance.getWritableDatabase().update(EXERCISE_DATABASE_TABLE, cv, KEY_ROWID + "='" + exerciseID + "' AND " + KEY_WORKOUTID + " = '" +workoutID + "'", null) >0; 
        }
        
        public Cursor getSuperSetID(String exerciseID, String workoutID) {

            Cursor mCursor = null;
           
            try{
            	mCursor = DBAdapter.mInstance.getWritableDatabase().query(EXERCISE_DATABASE_TABLE, new String[] {
            			  KEY_SUPERSETID }, KEY_ROWID + " = '" + exerciseID + "' AND " + KEY_WORKOUTID + " = '" + workoutID + "'", 
            			  null, null, null, null, null);
            }catch(Exception e){
            	e.printStackTrace();        	
            }
            
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        }
        
        
       
}

