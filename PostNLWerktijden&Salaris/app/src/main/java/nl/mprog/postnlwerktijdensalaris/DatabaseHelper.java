package nl.mprog.postnlwerktijdensalaris;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private static final String DAYS_COLUMN_TIMEGOAL = "goal";
    private static final String DAYS_COLUMN_TIMEEXTRA = "extra";

    private static final String WALKS_TABLE_NAME = "walks";
    private static final String WALKS_COLUMN_ID1 = "id1";
    private static final String WALKS_COLUMN_ID2 = "id2";
    private static final String WALKS_COLUMN_ID3 = "id3";
    private static final String WALKS_COLUMN_DISTRICT = "district";
    private static final String WALKS_COLUMN_TIMEBEGIN = "timeBegin";
    private static final String WALKS_COLUMN_TIMEEND = "timeEnd";
    private static final String WALKS_COLUMN_TIMEGOAL = "goal";
    private static final String WALKS_COLUMN_TIMEEXTRA = "extra";
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
            MONTHS_COLUMN_SALARY + " DOUBLE, " +
            MONTHS_COLUMN_TIME + " TEXT )";
        db.execSQL(query1);

        String query2 = "CREATE TABLE " + DAYS_TABLE_NAME +  " ( " +
            DAYS_COLUMN_ID1 + " INTEGER, " +
            DAYS_COLUMN_ID2 + " INTEGER, " +
            DAYS_COLUMN_DAY + " TEXT, " +
            DAYS_COLUMN_DISTRICTS + " TEXT, " +
            DAYS_COLUMN_TIMETOTAL + " TEXT, " +
            DAYS_COLUMN_TIMEGOAL + " TEXT, " +
            DAYS_COLUMN_TIMEEXTRA + " TEXT )";
        db.execSQL(query2);

        String query3 = "CREATE TABLE " + WALKS_TABLE_NAME +  " ( " +
            WALKS_COLUMN_ID1 + " INTEGER, " +
            WALKS_COLUMN_ID2 + " INTEGER, " +
            WALKS_COLUMN_ID3 + " INTEGER, " +
            WALKS_COLUMN_DISTRICT + " TEXT, " +
            WALKS_COLUMN_TIMEBEGIN + " TEXT, " +
            WALKS_COLUMN_TIMEEND + " TEXT, " +
            WALKS_COLUMN_TIMEGOAL + " TEXT, " +
            WALKS_COLUMN_TIMEEXTRA + " TEXT, " +
            WALKS_COLUMN_TIMETOTAL + " TEXT )";
        db.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MONTHS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DAYS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WALKS_TABLE_NAME);
        this.onCreate(db);
    }

    public int addMonth(MonthObject monthObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MONTHS_COLUMN_MONTH, monthObject.month);
        values.put(MONTHS_COLUMN_DAYS, monthObject.days);
        values.put(MONTHS_COLUMN_SALARY, monthObject.salary);
        values.put(MONTHS_COLUMN_TIME, monthObject.time);

        db.insert(MONTHS_TABLE_NAME, null, values);
        db.close();

        db = this.getReadableDatabase();
        String query = "SELECT ROWID from " + MONTHS_TABLE_NAME + " order by ROWID DESC limit 1";
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getInt(0);
    }

    public void addDay(DayObject dayObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DAYS_COLUMN_DAY, dayObject.day);
        values.put(DAYS_COLUMN_DISTRICTS, dayObject.districts);
        values.put(DAYS_COLUMN_TIMETOTAL, dayObject.timeTotal);
        values.put(DAYS_COLUMN_TIMEGOAL, dayObject.timeGoal);
        values.put(DAYS_COLUMN_TIMEEXTRA, dayObject.timeExtra);
    }
}
