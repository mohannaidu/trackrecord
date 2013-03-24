package fitness.data;

import java.sql.SQLException;

import fitness.model.Workout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

public class WorkoutAdapter {
    	public static final String KEY_ROWID = "_id";
    	public static final String KEY_NAME = "name";
    	public static final String KEY_ORDERING_VALUE = "orderingValue";
    	public static final String KEY_DATE_CREATED = "dateCreated";
    
    public static final String DATABASE_TABLE = "Workout";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	db.execSQL(DBAdapter.WorkoutTable);
        	db.execSQL(DBAdapter.ExerciseTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	       	 db.execSQL("DROP TABLE IF EXISTS " + WorkoutAdapter.DATABASE_TABLE);
	         db.execSQL("DROP TABLE IF EXISTS " + ExerciseAdapter.DATABASE_TABLE);
        }
    }
   
    public WorkoutAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    
    public WorkoutAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        this.mDbHelper.close();
    }
   
    public long createEntry(Workout workout){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, workout.getName());
        /** setting date for exercise */ 
    	long dtDob = workout.getDateCreated().toMillis(true); 
		cv.put(KEY_DATE_CREATED, DateFormat.format("dd-MM-yy", dtDob).toString());
        cv.put(KEY_ORDERING_VALUE, workout.getOrderingValue());
        return this.mDb.insert(DATABASE_TABLE, null, cv);
    }
    
    public boolean deleteWorkout(long rowId) {
        return this.mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0; 
    }
   
    public Cursor getAllWorkout() {

        return this.mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
        		KEY_DATE_CREATED, KEY_ORDERING_VALUE }, null, null, null, null, null);
    }
    
    /*public Cursor getWorkout(long rowId) throws SQLException {

        Cursor mCursor =

        this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME,
                MODEL, YEAR}, ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public boolean updateWorkout(long rowId, String name, String model,String year){
        ContentValues args = new ContentValues();
        args.put(NAME, name);
        args.put(MODEL, model);
        args.put(YEAR, year);

        return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0; 
    }*/

}