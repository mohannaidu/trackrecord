package fitness.data;

import java.sql.SQLException;

import fitness.model.Exercise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExerciseAdapter {
    public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_WEIGHT = "weight";
	public static final String KEY_SETS = "sets";
	public static final String KEY_REPS = "reps";
	public static final String KEY_TARGET = "target";
	public static final String KEY_TEMPO = "tempo";
	public static final String KEY_REST = "rest";
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
        return this.mDb.insert(DATABASE_TABLE, null, cv);
    }
    
    public boolean deleteExercise(long rowId) {
        return this.mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0; 
    }
   
    public Cursor getAllExercise() {

        return this.mDb.query(DATABASE_TABLE, new String[] { KEY_NAME,
        		KEY_WEIGHT, KEY_SETS, KEY_REPS, KEY_TARGET, KEY_TEMPO, KEY_REST }, null, null, null, null, null);
    }
    
    /*public Cursor getExercise(long rowId) throws SQLException {

        Cursor mCursor =

        this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME,
                MODEL, YEAR}, ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public boolean updateExercise(long rowId, String name, String model,String year){
        ContentValues args = new ContentValues();
        args.put(NAME, name);
        args.put(MODEL, model);
        args.put(YEAR, year);

        return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0; 
    }*/

}