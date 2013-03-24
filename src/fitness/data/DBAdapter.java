package fitness.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

    public static final String DATABASE_NAME = "fitness.db"; 	
    public static final String DATABASE_TABLE1 = "workout";	
	public static final String DATABASE_TABLE2 = "exercise";
    public static final int DATABASE_VERSION = 1;

	public static final String WorkoutTable = "CREATE TABLE " + DATABASE_TABLE1 + " (" +
			WorkoutAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			WorkoutAdapter.KEY_NAME + " TEXT, " +
			WorkoutAdapter.KEY_DATE_CREATED + " TEXT, " + 
			WorkoutAdapter.KEY_ORDERING_VALUE + " TEXT);";
	
	public static final String ExerciseTable = "CREATE TABLE " + DATABASE_TABLE2 + " (" +
			ExerciseAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			ExerciseAdapter.KEY_NAME + " TEXT, " +
			ExerciseAdapter.KEY_WEIGHT + " TEXT, " + 
			ExerciseAdapter.KEY_SETS + " TEXT, " + 
			ExerciseAdapter.KEY_REPS + " TEXT, " + 
			ExerciseAdapter.KEY_TARGET + " TEXT, " + 
			ExerciseAdapter.KEY_TEMPO + " TEXT, " + 
			ExerciseAdapter.KEY_REST + " TEXT, " + 
			ExerciseAdapter.KEY_DATE_ENTERED + " TEXT, " + 
			ExerciseAdapter.KEY_ORDERING_VALUE + " TEXT, " +
			ExerciseAdapter.KEY_WORKOUTID + " TEXT, "  +
			"FOREIGN KEY(" + ExerciseAdapter.KEY_WORKOUTID + ") REFERENCES " +
			(WorkoutAdapter.DATABASE_TABLE) + "(" + WorkoutAdapter.KEY_ROWID + ") ON DELETE CASCADE);";
 


    private final Context context; 
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(WorkoutTable);
            db.execSQL(ExerciseTable);           
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {               
            db.execSQL("DROP TABLE IF EXISTS " + WorkoutAdapter.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ExerciseAdapter.DATABASE_TABLE);
        }
    } 


    public DBAdapter open() throws SQLException 
    {
        this.db = this.DBHelper.getWritableDatabase();
        return this;
    }


    public void close() 
    {
        this.DBHelper.close();
    }
}