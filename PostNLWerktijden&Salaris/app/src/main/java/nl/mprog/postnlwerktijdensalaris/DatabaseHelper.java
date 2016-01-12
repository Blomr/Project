package nl.mprog.postnlwerktijdensalaris;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Database.db";

    private static final String MONTHS_TABLE_NAME = "months";
    private static final String MONTHS_COLUMN_ID = "id";
    private static final String MONTHS_COLUMN_MONTH = "month";
    private static final String MONTHS_COLUMN_DAYS = "days";
    private static final String MONTHS_COLUMN_SALARY = "salary";
    private static final String MONTHS_COLUMN_TIME = "time";

    private static final String DAYS_TABLE_NAME = "days";
    private static final String DAYS_COLUMN_ID1 = "id1";
    private static final String DAYS_COLUMN_ID2 = "id2";
    private static final String DAYS_COLUMN_DAY = "day";
    private static final String DAYS_COLUMN_DISTRICTS = "districts";
    private static final String DAYS_COLUMN_TIMETOTAL = "timeTotal";
    private static final String DAYS_COLUMN_GOAL = "goal";
    private static final String DAYS_COLUMN_EXTRA = "extra";

    private static final String WALKS_TABLE_NAME = "walks";
    private static final String WALKS_COLUMN_ID1 = "id1";
    private static final String WALKS_COLUMN_ID2 = "id2";
    private static final String WALKS_COLUMN_ID3 = "id3";
    private static final String WALKS_COLUMN_DISTRICT = "district";
    private static final String WALKS_COLUMN_TIMEBEGIN = "timeBegin";
    private static final String WALKS_COLUMN_TIMEEND = "timeEnd";
    private static final String WALKS_COLUMN_GOAL = "goal";
    private static final String WALKS_COLUMN_EXTRA = "extra";
    private static final String WALKS_COLUMN_TIMETOTAL = "timeTotal";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + MONTHS_TABLE_NAME +  " ( " +
            MONTHS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MONTHS_COLUMN_MONTH + " TEXT, " +
            MONTHS_COLUMN_DAYS + " INTEGER, " +
            MONTHS_COLUMN_SALARY + " FLOAT, " +
            MONTHS_COLUMN_TIME + " TIME )";
        db.execSQL(query1);

        String query2 = "CREATE TABLE " + DAYS_TABLE_NAME +  " ( " +
            DAYS_COLUMN_ID1 + " INTEGER, " +
            DAYS_COLUMN_ID2 + " INTEGER, " +
            DAYS_COLUMN_DAY + " TEXT, " +
            DAYS_COLUMN_DISTRICTS + " TEXT, " +
            DAYS_COLUMN_TIMETOTAL + " TIME, " +
            DAYS_COLUMN_GOAL + " TIME, " +
            DAYS_COLUMN_EXTRA + " TIME )";
        db.execSQL(query2);

        String query3 = "CREATE TABLE " + WALKS_TABLE_NAME +  " ( " +
            WALKS_COLUMN_ID1 + " INTEGER, " +
            WALKS_COLUMN_ID2 + " INTEGER, " +
            WALKS_COLUMN_ID3 + " INTEGER, " +
            WALKS_COLUMN_DISTRICT + " TEXT, " +
            WALKS_COLUMN_TIMEBEGIN + " TIME, " +
            WALKS_COLUMN_TIMEEND + " TIME, " +
            WALKS_COLUMN_GOAL + " TIME, " +
            WALKS_COLUMN_EXTRA + " TIME, " +
            WALKS_COLUMN_TIMETOTAL + " TIME )";
        db.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MONTHS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DAYS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WALKS_TABLE_NAME);
        this.onCreate(db);
    }
}
