package nl.mprog.postnlwerktijdensalaris;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

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
    private static final String WALKS_COLUMN_DISTRICTCODE = "districtCode";
    private static final String WALKS_COLUMN_TIMEBEGIN1 = "timeBegin1";
    private static final String WALKS_COLUMN_TIMEEND1 = "timeEnd1";
    private static final String WALKS_COLUMN_TIMEBEGIN2 = "timeBegin2";
    private static final String WALKS_COLUMN_TIMEEND2 = "timeEnd2";
    private static final String WALKS_COLUMN_TIMEBEGIN3 = "timeBegin3";
    private static final String WALKS_COLUMN_TIMEEND3 = "timeEnd3";
    private static final String WALKS_COLUMN_TIMEGOAL = "goal";
    private static final String WALKS_COLUMN_TIMEEXTRA = "extra";
    private static final String WALKS_COLUMN_TIMETOTAL = "timeTotal";

    public DatabaseHandler(Context context)
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
            WALKS_COLUMN_DISTRICTCODE + " TEXT, " +
            WALKS_COLUMN_TIMEBEGIN1 + " TEXT, " +
            WALKS_COLUMN_TIMEEND1 + " TEXT, " +
            WALKS_COLUMN_TIMEBEGIN2 + " TEXT, " +
            WALKS_COLUMN_TIMEEND2 + " TEXT, " +
            WALKS_COLUMN_TIMEBEGIN3 + " TEXT, " +
            WALKS_COLUMN_TIMEEND3 + " TEXT, " +
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

        int lastId = (int) db.insert(MONTHS_TABLE_NAME, null, values);
        db.close();
        return lastId;

        /*db = this.getReadableDatabase();
        String query = "SELECT ROWID from " + MONTHS_TABLE_NAME + " order by ROWID DESC limit 1";
        Cursor cursor = db.rawQuery(query, null);
        int lastId = cursor.getInt(0);
        cursor.close();
        return lastId;*/
    }

    public int addDay(DayObject dayObject) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT (*) FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_ID1 + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(dayObject.id1)});
        int count = 0;
        if(cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DAYS_COLUMN_ID1, dayObject.id1);
        values.put(DAYS_COLUMN_ID2, count + 1);
        values.put(DAYS_COLUMN_DAY, dayObject.day);
        values.put(DAYS_COLUMN_DISTRICTS, dayObject.districts);
        values.put(DAYS_COLUMN_TIMETOTAL, dayObject.timeTotal);
        values.put(DAYS_COLUMN_TIMEGOAL, dayObject.timeGoal);
        values.put(DAYS_COLUMN_TIMEEXTRA, dayObject.timeExtra);

        db.insert(DAYS_TABLE_NAME, null, values);
        db.close();
        return count + 1;
    }

    public int addWalk(WalkObject walkObject) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT (*) FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_ID1 +
                       " = ? AND " + WALKS_COLUMN_ID2 + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(walkObject.id1),
                        String.valueOf(walkObject.id2)});
        int count = 0;
        if(cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WALKS_COLUMN_ID1, walkObject.id1);
        values.put(WALKS_COLUMN_ID2, walkObject.id2);
        values.put(WALKS_COLUMN_ID3, count + 1);
        values.put(WALKS_COLUMN_DISTRICTCODE, walkObject.districtCode);
        values.put(WALKS_COLUMN_TIMEBEGIN1, walkObject.timeBegin1);
        values.put(WALKS_COLUMN_TIMEEND1, walkObject.timeEnd1);
        values.put(WALKS_COLUMN_TIMEBEGIN2, walkObject.timeBegin1);
        values.put(WALKS_COLUMN_TIMEEND2, walkObject.timeEnd1);
        values.put(WALKS_COLUMN_TIMEBEGIN3, walkObject.timeBegin1);
        values.put(WALKS_COLUMN_TIMEEND3, walkObject.timeEnd1);
        values.put(WALKS_COLUMN_TIMEGOAL, walkObject.timeGoal);
        values.put(WALKS_COLUMN_TIMEEXTRA, walkObject.timeExtra);
        values.put(WALKS_COLUMN_TIMETOTAL, walkObject.timeTotal);


        db.insert(WALKS_TABLE_NAME, null, values);
        db.close();
        return count + 1;
    }

    public WalkObject getWalk(int idMonth, int idDay, int idWalk) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_ID1 + " = ? AND "
                       + WALKS_COLUMN_ID2 + " = ? AND " + WALKS_COLUMN_ID3 + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idMonth), String.valueOf(idDay),
                String.valueOf(idWalk)});
        cursor.moveToFirst();

        String districtCode = cursor.getString(3);
        String dayType = cursor.getString(4);
        String timeBegin1 = cursor.getString(5);
        String timeEnd1 = cursor.getString(6);
        String timeBegin2 = cursor.getString(7);
        String timeEnd2 = cursor.getString(8);
        String timeBegin3 = cursor.getString(9);
        String timeEnd3 = cursor.getString(10);
        String timeGoal = cursor.getString(11);
        String timeExtra = cursor.getString(12);
        String timeTotal = cursor.getString(13);
        cursor.close();

        WalkObject walkObj = new WalkObject(idMonth, idDay, idWalk, districtCode, dayType, timeBegin1,
                             timeEnd1, timeBegin2, timeEnd2, timeBegin3, timeEnd3, timeGoal, timeExtra,
                             timeTotal);

        return walkObj;
    }
}
