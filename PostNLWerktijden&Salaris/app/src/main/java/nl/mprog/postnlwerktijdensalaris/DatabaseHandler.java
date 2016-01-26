package nl.mprog.postnlwerktijdensalaris;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private static final String DAYS_COLUMN_IDMONTH = "idMonth";
    private static final String DAYS_COLUMN_IDDAY = "idDay";
    private static final String DAYS_COLUMN_DAY = "day";
    private static final String DAYS_COLUMN_DISTRICTS = "districts";
    private static final String DAYS_COLUMN_TIMETOTAL = "timeTotal";
    private static final String DAYS_COLUMN_TIMEGOAL = "timeGoal";
    private static final String DAYS_COLUMN_TIMEEXTRA = "timeExtra";

    private static final String WALKS_TABLE_NAME = "walks";
    private static final String WALKS_COLUMN_IDMONTH = "idMonth";
    private static final String WALKS_COLUMN_IDDAY = "idDay";
    private static final String WALKS_COLUMN_IDWALK = "idWalk";
    private static final String WALKS_COLUMN_DISTRICTCODE = "districtCode";
    private static final String WALKS_COLUMN_DAYTYPE = "dayType";
    private static final String WALKS_COLUMN_TIMEBEGIN1 = "timeBegin1";
    private static final String WALKS_COLUMN_TIMEEND1 = "timeEnd1";
    private static final String WALKS_COLUMN_TIMEBEGIN2 = "timeBegin2";
    private static final String WALKS_COLUMN_TIMEEND2 = "timeEnd2";
    private static final String WALKS_COLUMN_TIMEBEGIN3 = "timeBegin3";
    private static final String WALKS_COLUMN_TIMEEND3 = "timeEnd3";
    private static final String WALKS_COLUMN_TIMEGOAL = "timeGoal";
    private static final String WALKS_COLUMN_TIMEEXTRA = "timeExtra";
    private static final String WALKS_COLUMN_TIMETOTAL = "timeTotal";

    private static final String DISTRICTS_TABLE_NAME = "districts";
    private static final String DISTRICTS_COLUMN_ID = "id";
    private static final String DISTRICTS_COLUMN_DISTRICTCODE = "districtCode";
    private static final String DISTRICTS_COLUMN_TIMEGOALBUSY = "timeBusyDay";
    private static final String DISTRICTS_COLUMN_TIMEGOALCALM = "timeCalmDay";

    private final int ZERO_TIME_VALUE = 3600000;

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryMonths = "CREATE TABLE " + MONTHS_TABLE_NAME +  " ( " +
                             MONTHS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             MONTHS_COLUMN_MONTH + " TEXT, " +
                             MONTHS_COLUMN_DAYS + " INTEGER, " +
                             MONTHS_COLUMN_SALARY + " DOUBLE, " +
                             MONTHS_COLUMN_TIME + " TEXT )";
        db.execSQL(queryMonths);

        String queryDays = "CREATE TABLE " + DAYS_TABLE_NAME +  " ( " +
                           DAYS_COLUMN_IDMONTH + " INTEGER, " +
                           DAYS_COLUMN_IDDAY + " INTEGER, " +
                           DAYS_COLUMN_DAY + " TEXT, " +
                           DAYS_COLUMN_DISTRICTS + " TEXT, " +
                           DAYS_COLUMN_TIMETOTAL + " TEXT, " +
                           DAYS_COLUMN_TIMEGOAL + " TEXT, " +
                           DAYS_COLUMN_TIMEEXTRA + " TEXT )";
        db.execSQL(queryDays);

        String queryWalks = "CREATE TABLE " + WALKS_TABLE_NAME +  " ( " +
                            WALKS_COLUMN_IDMONTH + " INTEGER, " +
                            WALKS_COLUMN_IDDAY + " INTEGER, " +
                            WALKS_COLUMN_IDWALK + " INTEGER, " +
                            WALKS_COLUMN_DISTRICTCODE + " TEXT, " +
                            WALKS_COLUMN_DAYTYPE + " TEXT, " +
                            WALKS_COLUMN_TIMEBEGIN1 + " TEXT, " +
                            WALKS_COLUMN_TIMEEND1 + " TEXT, " +
                            WALKS_COLUMN_TIMEBEGIN2 + " TEXT, " +
                            WALKS_COLUMN_TIMEEND2 + " TEXT, " +
                            WALKS_COLUMN_TIMEBEGIN3 + " TEXT, " +
                            WALKS_COLUMN_TIMEEND3 + " TEXT, " +
                            WALKS_COLUMN_TIMEGOAL + " TEXT, " +
                            WALKS_COLUMN_TIMEEXTRA + " TEXT, " +
                            WALKS_COLUMN_TIMETOTAL + " TEXT )";
        db.execSQL(queryWalks);

        String queryDistricts = "CREATE TABLE " + DISTRICTS_TABLE_NAME + " ( " +
                                DISTRICTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                DISTRICTS_COLUMN_DISTRICTCODE + " TEXT, " +
                                DISTRICTS_COLUMN_TIMEGOALBUSY + " TEXT, " +
                                DISTRICTS_COLUMN_TIMEGOALCALM + " TEXT )";
        db.execSQL(queryDistricts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MONTHS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DAYS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WALKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DISTRICTS_TABLE_NAME);
        onCreate(db);
    }

    public int addMonth(MonthObject monthObject) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MONTHS_COLUMN_MONTH, monthObject.month);
        values.put(MONTHS_COLUMN_DAYS, monthObject.days);
        values.put(MONTHS_COLUMN_SALARY, monthObject.salary);
        values.put(MONTHS_COLUMN_TIME, monthObject.time);
        int lastId = (int) db.insert(MONTHS_TABLE_NAME, null, values);
        db.close();

        return lastId;
    }

    public int addDay(DayObject dayObject) {
        SQLiteDatabase db = getReadableDatabase();
        int idDay = 0;
        boolean foundFreeId = false;
        while (!foundFreeId) {
            idDay++;
            String query = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH +
                           " = ? AND " + DAYS_COLUMN_IDDAY + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(dayObject.id1),
                            Integer.toString(idDay)});
            if (cursor.getCount() == 0) {
                foundFreeId = true;
                cursor.close();
            }
        }

        String query = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(dayObject.id1)});
        cursor.moveToFirst();

        int days = cursor.getInt(2);

        cursor.close();
        db.close();

        days += 1;

        db = getWritableDatabase();
        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_IDMONTH, dayObject.id1);
        dayValues.put(DAYS_COLUMN_IDDAY, idDay);
        dayValues.put(DAYS_COLUMN_DAY, dayObject.day);
        dayValues.put(DAYS_COLUMN_DISTRICTS, dayObject.districts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, dayObject.timeTotal);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, dayObject.timeGoal);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, dayObject.timeExtra);
        db.insert(DAYS_TABLE_NAME, null, dayValues);

        ContentValues monthValues = new ContentValues();
        monthValues.put(MONTHS_COLUMN_DAYS, days);
        db.update(MONTHS_TABLE_NAME, monthValues, MONTHS_COLUMN_ID + " = ?",
                  new String[]{Integer.toString(dayObject.id1)});
        db.close();

        return idDay;
    }

    public void addWalk(WalkObject walkObject, Bundle sharedPref) {
        SQLiteDatabase db = getReadableDatabase();

        // read from walks table
        int idWalk = 0;
        boolean foundFreeId = false;
        while (!foundFreeId) {
            idWalk++;
            String query = "SELECT * FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_IDMONTH +
                           " = ? AND " + WALKS_COLUMN_IDDAY + " = ? AND " + WALKS_COLUMN_IDWALK +
                           " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(walkObject.id1),
                            Integer.toString(walkObject.id2), Integer.toString(idWalk)});
            if (cursor.getCount() == 0) {
                foundFreeId = true;
                cursor.close();
            }
        }

        // read from days table
        String query = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH +
                       " = ? AND " + DAYS_COLUMN_IDDAY + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(walkObject.id1),
                Integer.toString(walkObject.id2)});
        cursor.moveToFirst();

        String districts = cursor.getString(3);
        String dayTimeTotalStr = cursor.getString(4);
        String dayTimeGoalStr = cursor.getString(5);
        String dayTimeExtraStr = cursor.getString(6);

        cursor.close();

        // read from months table
        query = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        cursor = db.rawQuery(query, new String[]{Integer.toString(walkObject.id1)});
        cursor.moveToFirst();

        String timeMonth = cursor.getString(4);

        cursor.close();
        db.close();

        // insert into walks table
        db = getWritableDatabase();
        ContentValues walkValues = new ContentValues();
        walkValues.put(WALKS_COLUMN_IDMONTH, walkObject.id1);
        walkValues.put(WALKS_COLUMN_IDDAY, walkObject.id2);
        walkValues.put(WALKS_COLUMN_IDWALK, idWalk);
        walkValues.put(WALKS_COLUMN_DISTRICTCODE, walkObject.districtCode);
        walkValues.put(WALKS_COLUMN_DAYTYPE, walkObject.dayType);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN1, walkObject.timeBegin1);
        walkValues.put(WALKS_COLUMN_TIMEEND1, walkObject.timeEnd1);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN2, walkObject.timeBegin2);
        walkValues.put(WALKS_COLUMN_TIMEEND2, walkObject.timeEnd2);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN3, walkObject.timeBegin3);
        walkValues.put(WALKS_COLUMN_TIMEEND3, walkObject.timeEnd3);
        walkValues.put(WALKS_COLUMN_TIMEGOAL, walkObject.timeGoal);
        walkValues.put(WALKS_COLUMN_TIMEEXTRA, walkObject.timeExtra);
        walkValues.put(WALKS_COLUMN_TIMETOTAL, walkObject.timeTotal);
        db.insert(WALKS_TABLE_NAME, null, walkValues);

        // update days table
        districts = districts + walkObject.districtCode + " ";

        Date dayTimeTotal = strToDateParser(dayTimeTotalStr);
        Date dayTimeGoal = strToDateParser(dayTimeGoalStr);

        boolean timeDayNegative = false;
        if (dayTimeExtraStr.charAt(0) == '-') {
            dayTimeExtraStr = dayTimeExtraStr.substring(1);
            timeDayNegative = true;
        }
        Date dayTimeExtra = strToDateParser(dayTimeExtraStr);

        Date walkTimeTotal = strToDateParser(walkObject.timeTotal);
        Date walkTimeGoal = strToDateParser(walkObject.timeGoal);

        boolean timeWalkNegative = false;
        if (walkObject.timeExtra.charAt(0) == '-') {
            walkObject.timeExtra = walkObject.timeExtra.substring(1);
            timeWalkNegative = true;
        }
        Date walkTimeExtra = strToDateParser(walkObject.timeExtra);

        long timeTotalMs = 0;
        long timeGoalMs = 0;
        long timeExtraMs = 0;

        try {
            timeTotalMs = dayTimeTotal.getTime() + walkTimeTotal.getTime() + ZERO_TIME_VALUE * 2;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        try {
            timeGoalMs = dayTimeGoal.getTime() + walkTimeGoal.getTime() + ZERO_TIME_VALUE * 2;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        if (!timeDayNegative && !timeWalkNegative) {
            try {
                timeExtraMs = dayTimeExtra.getTime() + walkTimeExtra.getTime() + ZERO_TIME_VALUE * 2;
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else if (!timeDayNegative && timeWalkNegative){
            try {
                timeExtraMs = dayTimeExtra.getTime() + ZERO_TIME_VALUE - (walkTimeExtra.getTime()
                              + ZERO_TIME_VALUE);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                timeExtraMs = -1 * (dayTimeExtra.getTime() + ZERO_TIME_VALUE)
                              - (walkTimeExtra.getTime() + ZERO_TIME_VALUE);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }

        dayTimeTotalStr = msToTimeStrConverter(timeTotalMs);
        dayTimeGoalStr = msToTimeStrConverter(timeGoalMs);
        dayTimeExtraStr = msToTimeStrConverter(timeExtraMs);

        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_DISTRICTS, districts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, dayTimeTotalStr);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, dayTimeGoalStr);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, dayTimeExtraStr);
        db.update(DAYS_TABLE_NAME, dayValues, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY +
                  " = ?", new String[]{Integer.toString(walkObject.id1), Integer.toString(walkObject.id2)});

        // update months table
        Date timeMonthCur = strToDateParser(timeMonth);

        long monthTimeMs = 0;
        try {
            monthTimeMs = timeMonthCur.getTime() + walkTimeTotal.getTime() + ZERO_TIME_VALUE * 2;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        timeMonth = msToTimeStrConverter(monthTimeMs);
        double salaryTotal = salaryCalculator(monthTimeMs, sharedPref);

        ContentValues monthValues = new ContentValues();
        monthValues.put(MONTHS_COLUMN_SALARY, salaryTotal);
        monthValues.put(MONTHS_COLUMN_TIME, timeMonth);
        db.update(MONTHS_TABLE_NAME, monthValues, MONTHS_COLUMN_ID + " = ?",
                  new String[]{Integer.toString(walkObject.id1)});
        db.close();
    }

    public void addDistrict(DistrictObject districtObject) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DISTRICTS_COLUMN_DISTRICTCODE, districtObject.districtCode);
        values.put(DISTRICTS_COLUMN_TIMEGOALBUSY, districtObject.timeGoalBusy);
        values.put(DISTRICTS_COLUMN_TIMEGOALCALM, districtObject.timeGoalCalm);
        db.insert(DISTRICTS_TABLE_NAME, null, values);
        db.close();
    }

    public WalkObject getWalk(int idMonth, int idDay, int idWalk) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_IDMONTH + " = ? AND "
                       + WALKS_COLUMN_IDDAY + " = ? AND " + WALKS_COLUMN_IDWALK + " = ?";
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
        db.close();

        WalkObject walkObj = new WalkObject(idMonth, idDay, idWalk, districtCode, dayType, timeBegin1,
                             timeEnd1, timeBegin2, timeEnd2, timeBegin3, timeEnd3, timeGoal, timeExtra,
                             timeTotal);

        return walkObj;
    }

    public ArrayList<WalkObject> getWalksOfDay(int idMonth, int idDay) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WalkObject> walkObjects = new ArrayList<>();
        String query = "SELECT * FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_IDMONTH + " = ? AND "
                       + WALKS_COLUMN_IDDAY + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idMonth), String.valueOf(idDay)});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int idWalk = cursor.getInt(2);
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

            WalkObject walkObj = new WalkObject(idMonth, idDay, idWalk, districtCode, dayType, timeBegin1,
                                 timeEnd1, timeBegin2, timeEnd2, timeBegin3, timeEnd3, timeGoal, timeExtra,
                                 timeTotal);

            walkObjects.add(walkObj);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return walkObjects;
    }

    public ArrayList<DayObject> getDaysOfMonth(int idMonth) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DayObject> dayObjects = new ArrayList<>();
        String query = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idMonth)});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int idDay = cursor.getInt(1);
            String day = cursor.getString(2);
            String districts = cursor.getString(3);
            String timeTotal = cursor.getString(4);
            String timeGoal = cursor.getString(5);
            String timeExtra = cursor.getString(6);

            DayObject dayObj = new DayObject(idMonth, idDay, day, districts, timeTotal, timeGoal,
                               timeExtra);

            dayObjects.add(dayObj);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return dayObjects;
    }

    public ArrayList<MonthObject> getMonths() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<MonthObject> monthObjects = new ArrayList<>();
        String query = "SELECT * FROM " + MONTHS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String month = cursor.getString(1);
            int days = cursor.getInt(2);
            double salary = cursor.getDouble(3);
            String time = cursor.getString(4);

            MonthObject monthObj = new MonthObject(id, month, days, salary, time);

            monthObjects.add(monthObj);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return monthObjects;
    }

    public ArrayList<DistrictObject> getDistricts() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DistrictObject> districtObjects = new ArrayList<>();
        String query = "SELECT * FROM " + DISTRICTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String districtCode = cursor.getString(1);
            String timeGoalBusy = cursor.getString(2);
            String timeGoalCalm = cursor.getString(3);

            DistrictObject districtObj = new DistrictObject(id, districtCode, timeGoalBusy, timeGoalCalm);

            districtObjects.add(districtObj);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return districtObjects;
    }

    public String getMonthName(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(id)});
        cursor.moveToFirst();

        String monthName = cursor.getString(1);

        cursor.close();
        db.close();

        return monthName;
    }

    public String getDayName(int idMonth, int idDay) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH +
                       " = ? AND " + DAYS_COLUMN_IDDAY + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(idMonth),
                Integer.toString(idDay)});
        cursor.moveToFirst();

        String dayName = cursor.getString(2);

        cursor.close();
        db.close();

        return dayName;
    }

    public void deleteMonth(int idMonth) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MONTHS_TABLE_NAME, MONTHS_COLUMN_ID + " = ?", new String[]{Integer.toString(idMonth)});
        db.delete(DAYS_TABLE_NAME, DAYS_COLUMN_IDMONTH + " = ?", new String[]{Integer.toString(idMonth)});
        db.delete(WALKS_TABLE_NAME, WALKS_COLUMN_IDMONTH + " = ?", new String[]{Integer.toString(idMonth)});
        db.close();
    }

    public void deleteDay(int idMonth, int idDay, Bundle sharedPref) {
        SQLiteDatabase db = getReadableDatabase();
        String queryMonths = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(queryMonths, new String[]{Integer.toString(idMonth)});
        cursor.moveToFirst();

        int days = cursor.getInt(2);
        String oldMonthTimeStr = cursor.getString(4);

        cursor.close();

        String queryDays = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH +
                           " = ? AND " + DAYS_COLUMN_IDDAY + " = ?";
        cursor = db.rawQuery(queryDays, new String[]{Integer.toString(idMonth), Integer.toString(idDay)});
        cursor.moveToFirst();

        String dayTimeTotalStr = cursor.getString(4);

        cursor.close();
        db.close();

        days -= 1;

        Date oldMonthTime = strToDateParser(oldMonthTimeStr);
        Date dayTimeTotal = strToDateParser(dayTimeTotalStr);

        long newMonthTimeMs = 0;
        try {
            newMonthTimeMs = oldMonthTime.getTime() + ZERO_TIME_VALUE - (dayTimeTotal.getTime()
                             + ZERO_TIME_VALUE);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        String newMonthTime = msToTimeStrConverter(newMonthTimeMs);
        double newSalaryTotal = salaryCalculator(newMonthTimeMs, sharedPref);

        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MONTHS_COLUMN_DAYS, days);
        values.put(MONTHS_COLUMN_SALARY, newSalaryTotal);
        values.put(MONTHS_COLUMN_TIME, newMonthTime);
        db.update(MONTHS_TABLE_NAME, values, MONTHS_COLUMN_ID + " = ?",
                new String[]{Integer.toString(idMonth)});
        db.delete(DAYS_TABLE_NAME, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY + " = ?",
                new String[]{Integer.toString(idMonth), Integer.toString(idDay)});
        db.delete(WALKS_TABLE_NAME, WALKS_COLUMN_IDMONTH + " = ? AND " + WALKS_COLUMN_IDDAY + " = ?",
                  new String[]{Integer.toString(idMonth), Integer.toString(idDay)});
        db.close();
    }

    public void deleteWalk(int idMonth, int idDay, int idWalk, Bundle sharedPref) {
        SQLiteDatabase db = getReadableDatabase();
        String queryWalks = "SELECT * FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_IDMONTH +
                            " = ? AND " + WALKS_COLUMN_IDDAY + " = ? AND " + WALKS_COLUMN_IDWALK +
                            " = ?";
        Cursor cursor = db.rawQuery(queryWalks, new String[]{Integer.toString(idMonth),
                        Integer.toString(idDay), Integer.toString(idWalk)});
        cursor.moveToFirst();

        String districtCode = cursor.getString(3);
        String walkTimeGoalStr = cursor.getString(11);
        String walkTimeExtraStr = cursor.getString(12);
        String walkTimeTotalStr = cursor.getString(13);

        cursor.close();

        String queryDays = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH +
                       " = ? AND " + DAYS_COLUMN_IDDAY + " = ?";
        cursor = db.rawQuery(queryDays, new String[]{Integer.toString(idMonth),
                Integer.toString(idDay)});
        cursor.moveToFirst();

        String districts = cursor.getString(3);
        String oldDayTimeTotalStr = cursor.getString(4);
        String oldDayTimeGoalStr = cursor.getString(5);
        String oldDayTimeExtraStr = cursor.getString(6);

        cursor.close();

        String queryMonths = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        cursor = db.rawQuery(queryMonths, new String[]{Integer.toString(idMonth)});
        cursor.moveToFirst();

        String oldMonthTimeStr = cursor.getString(4);

        cursor.close();
        db.close();

        districts = districts.replaceFirst(districtCode + " ", "");

        Date oldDayTimeTotal = strToDateParser(oldDayTimeTotalStr);
        Date oldDayTimeGoal = strToDateParser(oldDayTimeGoalStr);

        boolean oldDayTimeNeg = false;
        if (oldDayTimeExtraStr.charAt(0) == '-') {
            oldDayTimeExtraStr = oldDayTimeExtraStr.substring(1);
            oldDayTimeNeg = true;
        }
        Date oldDayTimeExtra = strToDateParser(oldDayTimeExtraStr);

        Date walkTimeTotal = strToDateParser(walkTimeTotalStr);
        Date walkTimeGoal = strToDateParser(walkTimeGoalStr);

        boolean walkTimeNeg = false;
        if (walkTimeExtraStr.charAt(0) == '-') {
            walkTimeExtraStr = walkTimeExtraStr.substring(1);
            walkTimeNeg = true;
        }
        Date walkTimeExtra = strToDateParser(walkTimeExtraStr);

        long newDayTimeTotalMs = 0;
        long newDayTimeGoalMs = 0;
        long newDayTimeExtraMs = 0;

        try {
            newDayTimeTotalMs = oldDayTimeTotal.getTime() + ZERO_TIME_VALUE - (walkTimeTotal.getTime()
                                + ZERO_TIME_VALUE);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }
        try {
            newDayTimeGoalMs = oldDayTimeGoal.getTime() + ZERO_TIME_VALUE - (walkTimeGoal.getTime()
                               + ZERO_TIME_VALUE);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        if (!oldDayTimeNeg && !walkTimeNeg) {
            try {
                newDayTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE - (walkTimeExtra.getTime()
                                    + ZERO_TIME_VALUE);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else if (!oldDayTimeNeg && walkTimeNeg) {
            try {
                newDayTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE + walkTimeExtra.getTime()
                                    + ZERO_TIME_VALUE;
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else if (oldDayTimeNeg && !walkTimeNeg) {
            try {
                newDayTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE)
                                    - (walkTimeExtra.getTime() + ZERO_TIME_VALUE);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                newDayTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE)
                                    + (walkTimeExtra.getTime() + ZERO_TIME_VALUE);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }

        String newDayTimeTotal = msToTimeStrConverter(newDayTimeTotalMs);
        String newDayTimeGoal = msToTimeStrConverter(newDayTimeGoalMs);
        String newDayTimeExtra = msToTimeStrConverter(newDayTimeExtraMs);

        db = getWritableDatabase();
        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_DISTRICTS, districts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, newDayTimeTotal);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, newDayTimeGoal);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, newDayTimeExtra);
        db.update(DAYS_TABLE_NAME, dayValues, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY + " = ?",
                  new String[]{Integer.toString(idMonth), Integer.toString(idDay)});

        Date oldMonthTime = strToDateParser(oldMonthTimeStr);

        long newMonthTimeMs = 0;
        try {
            newMonthTimeMs = oldMonthTime.getTime() + ZERO_TIME_VALUE - (oldDayTimeTotal.getTime()
                             + ZERO_TIME_VALUE) + newDayTimeTotalMs;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        String newMonthTime = msToTimeStrConverter(newMonthTimeMs);
        double newSalaryTotal = salaryCalculator(newMonthTimeMs, sharedPref);

        ContentValues monthValues = new ContentValues();
        monthValues.put(MONTHS_COLUMN_SALARY, newSalaryTotal);
        monthValues.put(MONTHS_COLUMN_TIME, newMonthTime);
        db.update(MONTHS_TABLE_NAME, monthValues, MONTHS_COLUMN_ID + " = ?",
                  new String[]{Integer.toString(idMonth)});

        db.delete(WALKS_TABLE_NAME, WALKS_COLUMN_IDMONTH + " = ? AND " + WALKS_COLUMN_IDDAY + " = ? AND "
                  + WALKS_COLUMN_IDWALK + " = ?", new String[]{Integer.toString(idMonth),
                  Integer.toString(idDay), Integer.toString(idWalk)});
        db.close();
    }

    public void editMonthName(int idMonth, String monthName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MONTHS_COLUMN_MONTH, monthName);
        db.update(MONTHS_TABLE_NAME, values, MONTHS_COLUMN_ID + " = ?",
                  new String[]{Integer.toString(idMonth)});
        db.close();
    }

    public void editDayName(int idMonth, int idDay, String dayName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DAYS_COLUMN_DAY, dayName);
        db.update(DAYS_TABLE_NAME, values, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY +
                  " = ?", new String[]{Integer.toString(idMonth), Integer.toString(idDay)});
        db.close();
    }

    public void editWalk(WalkObject walkObject, Bundle sharedPref) {
        SQLiteDatabase db = getReadableDatabase();
        String queryWalk = "SELECT * FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_IDMONTH +
                           " = ? AND " + WALKS_COLUMN_IDDAY + " = ? AND " + WALKS_COLUMN_IDWALK + " = ?";
        Cursor cursor = db.rawQuery(queryWalk, new String[]{Integer.toString(walkObject.id1),
                        Integer.toString(walkObject.id2), Integer.toString(walkObject.id3)});
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

        WalkObject oldWalkObj = new WalkObject(walkObject.id1, walkObject.id2, walkObject.id3,
                                districtCode, dayType, timeBegin1, timeEnd1, timeBegin2, timeEnd2,
                                timeBegin3, timeEnd3, timeGoal, timeExtra, timeTotal);

        String queryDay = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH +
                          " = ? AND " + DAYS_COLUMN_IDDAY + " = ?";
        cursor = db.rawQuery(queryDay, new String[]{Integer.toString(walkObject.id1),
                             Integer.toString(walkObject.id2)});
        cursor.moveToFirst();

        String day = cursor.getString(2);
        String districts = cursor.getString(3);
        timeTotal = cursor.getString(4);
        timeGoal = cursor.getString(5);
        timeExtra = cursor.getString(6);

        cursor.close();

        DayObject oldDayObj = new DayObject(walkObject.id1, walkObject.id2, day, districts,
                              timeTotal, timeGoal, timeExtra);

        String queryMonth = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        cursor = db.rawQuery(queryMonth, new String[]{Integer.toString(walkObject.id1)});
        cursor.moveToFirst();

        String month = cursor.getString(1);
        int days = cursor.getInt(2);
        double salary = cursor.getDouble(3);
        String time = cursor.getString(4);

        cursor.close();
        db.close();

        MonthObject oldMonthObj = new MonthObject(walkObject.id1, month, days, salary, time);

        db = getWritableDatabase();
        ContentValues walkValues = new ContentValues();
        walkValues.put(WALKS_COLUMN_DISTRICTCODE, walkObject.districtCode);
        walkValues.put(WALKS_COLUMN_DAYTYPE, walkObject.dayType);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN1, walkObject.timeBegin1);
        walkValues.put(WALKS_COLUMN_TIMEEND1, walkObject.timeEnd1);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN2, walkObject.timeBegin2);
        walkValues.put(WALKS_COLUMN_TIMEEND2, walkObject.timeEnd2);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN3, walkObject.timeBegin3);
        walkValues.put(WALKS_COLUMN_TIMEEND3, walkObject.timeEnd3);
        walkValues.put(WALKS_COLUMN_TIMEGOAL, walkObject.timeGoal);
        walkValues.put(WALKS_COLUMN_TIMEEXTRA, walkObject.timeExtra);
        walkValues.put(WALKS_COLUMN_TIMETOTAL, walkObject.timeTotal);
        db.update(WALKS_TABLE_NAME, walkValues, WALKS_COLUMN_IDMONTH + " = ? AND " + WALKS_COLUMN_IDDAY +
                  " = ? AND " + WALKS_COLUMN_IDWALK + " = ?", new String[]{Integer.toString(walkObject.id1),
                  Integer.toString(walkObject.id2), Integer.toString(walkObject.id3)});

        String newDistricts = oldDayObj.districts.replaceFirst(oldWalkObj.districtCode,
                              walkObject.districtCode);

        Date oldDayTimeTotal = strToDateParser(oldDayObj.timeTotal);
        Date oldWalkTimeTotal = strToDateParser(oldWalkObj.timeTotal);
        Date newWalkTimeTotal = strToDateParser(walkObject.timeTotal);

        long newTimeTotalMs = 0;
        try {
            newTimeTotalMs = oldDayTimeTotal.getTime() + ZERO_TIME_VALUE - (oldWalkTimeTotal.getTime()
                             + ZERO_TIME_VALUE) + newWalkTimeTotal.getTime() + ZERO_TIME_VALUE;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        String newDayTimeTotal = msToTimeStrConverter(newTimeTotalMs);

        Date oldDayTimeGoal = strToDateParser(oldDayObj.timeGoal);
        Date oldWalkTimeGoal = strToDateParser(oldWalkObj.timeGoal);
        Date newWalkTimeGoal = strToDateParser(walkObject.timeGoal);

        long newTimeGoalMs = 0;
        try {
            newTimeGoalMs = oldDayTimeGoal.getTime() + ZERO_TIME_VALUE - (oldWalkTimeGoal.getTime()
                            + ZERO_TIME_VALUE) + newWalkTimeGoal.getTime() + ZERO_TIME_VALUE;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        String newDayTimeGoal = msToTimeStrConverter(newTimeGoalMs);

        boolean oldDayTimeNeg = false;
        if (oldDayObj.timeExtra.charAt(0) == '-') {
            oldDayObj.timeExtra = oldDayObj.timeExtra.substring(1);
            oldDayTimeNeg = true;
        }
        Date oldDayTimeExtra = strToDateParser(oldDayObj.timeExtra);

        boolean oldWalkTimeNeg = false;
        if (oldWalkObj.timeExtra.charAt(0) == '-') {
            oldWalkObj.timeExtra = oldWalkObj.timeExtra.substring(1);
            oldWalkTimeNeg = true;
        }
        Date oldWalkTimeExtra = strToDateParser(oldWalkObj.timeExtra);

        boolean newWalkTimeNeg = false;
        if (walkObject.timeExtra.charAt(0) == '-') {
            walkObject.timeExtra = walkObject.timeExtra.substring(1);
            newWalkTimeNeg = true;
        }
        Date newWalkTimeExtra = strToDateParser(walkObject.timeExtra);

        long newTimeExtraMs;
        if (!oldDayTimeNeg && !oldWalkTimeNeg && !newWalkTimeNeg) {
            newTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE - (oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE) + newWalkTimeExtra.getTime() + ZERO_TIME_VALUE;
        }
        else if (!oldDayTimeNeg && !oldWalkTimeNeg && newWalkTimeNeg) {
            newTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE - (oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE) - (newWalkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }
        else if (!oldDayTimeNeg && oldWalkTimeNeg && !newWalkTimeNeg) {
            newTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE + oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE + newWalkTimeExtra.getTime() + ZERO_TIME_VALUE;
        }
        else if (!oldDayTimeNeg && oldWalkTimeNeg && newWalkTimeNeg) {
            newTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE + oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE - (newWalkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }
        else if (oldDayTimeNeg && !oldWalkTimeNeg && !newWalkTimeNeg) {
            newTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE) - (oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE) + newWalkTimeExtra.getTime() + ZERO_TIME_VALUE;
        }
        else if (oldDayTimeNeg && !oldWalkTimeNeg && newWalkTimeNeg) {
            newTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE) - (oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE) - (newWalkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }
        else if (oldDayTimeNeg && oldWalkTimeNeg && !newWalkTimeNeg) {
            newTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE) + oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE + newWalkTimeExtra.getTime() + ZERO_TIME_VALUE;
        }
        else {
            newTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE) + oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE - (newWalkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }

        String newDayTimeExtra = msToTimeStrConverter(newTimeExtraMs);

        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_DISTRICTS, newDistricts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, newDayTimeTotal);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, newDayTimeGoal);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, newDayTimeExtra);
        db.update(DAYS_TABLE_NAME, dayValues, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY
                  + " = ?", new String[]{Integer.toString(walkObject.id1),
                  Integer.toString(walkObject.id2)});

        Date oldMonthTime = strToDateParser(oldMonthObj.time);

        long newMonthTimeMs = oldMonthTime.getTime() + ZERO_TIME_VALUE - (oldDayTimeTotal.getTime()
                              + ZERO_TIME_VALUE) + newTimeTotalMs;

        String newMonthTime = msToTimeStrConverter(newMonthTimeMs);

        double newSalaryTotal = salaryCalculator(newMonthTimeMs, sharedPref);

        ContentValues monthValues = new ContentValues();
        monthValues.put(MONTHS_COLUMN_SALARY, newSalaryTotal);
        monthValues.put(MONTHS_COLUMN_TIME, newMonthTime);
        db.update(MONTHS_TABLE_NAME, monthValues, MONTHS_COLUMN_ID + " = ?",
                new String[]{Integer.toString(walkObject.id1)});
        db.close();
    }

    private double salaryCalculator(long newMonthTimeMs, Bundle sharedPref) {
        int contractHours = sharedPref.getInt("contractHours");
        int contractMins = sharedPref.getInt("contractMins");
        int salaryEuro = sharedPref.getInt("salaryEuro");
        int salaryCents = sharedPref.getInt("salaryCents");
        int extraEuro = sharedPref.getInt("extraEuro");
        int extraCents = sharedPref.getInt("extraCents");

        String contractHoursStr = Integer.toString(contractHours);
        if (contractHoursStr.length() == 1) {
            contractHoursStr = "0" + contractHoursStr;
        }

        String contractMinsStr = Integer.toString(contractMins);
        if (contractMinsStr.length() == 1) {
            contractMinsStr = "0" + contractMinsStr;
        }

        Date timeContract = strToDateParser(contractHoursStr + ":" + contractMinsStr);

        long timeExtraMonthMs = 0;
        try {
            timeExtraMonthMs = newMonthTimeMs - (timeContract.getTime() + ZERO_TIME_VALUE);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        long monthHours = newMonthTimeMs / 1000 / 60 / 60;
        long monthMins = newMonthTimeMs / 1000 / 60 % 60;

        double salaryContractHour = ((double) salaryCents) / 100 + salaryEuro;
        double salaryExtraHour = ((double) extraCents) / 100 + extraEuro;

        long timeExtraMonthHours = timeExtraMonthMs / 1000 / 60 / 60;
        long timeExtraMonthMins = timeExtraMonthMs / 1000 / 60 % 60;

        double salaryContractHours;
        double salaryContractMins;
        double salaryExtraHours = 0;
        double salaryExtraMins = 0;

        if (timeExtraMonthMs <= 0) {
            salaryContractHours = monthHours * salaryContractHour;
            salaryContractMins = ((double) monthMins) / 60 * salaryContractHour;
        }
        else {
            salaryContractHours = contractHours * salaryContractHour;
            salaryContractMins = contractMins / 60 * salaryContractHour;
            salaryExtraHours = timeExtraMonthHours * salaryExtraHour;
            salaryExtraMins = timeExtraMonthMins / 60 * salaryExtraHour;
        }

        double newSalaryTotal = salaryContractHours + salaryContractMins + salaryExtraHours +
                salaryExtraMins;

        DecimalFormat twoDecFormat = new DecimalFormat("#.##");
        String salaryTotalStr = twoDecFormat.format(newSalaryTotal).replaceAll(",",".");
        newSalaryTotal = Double.parseDouble(salaryTotalStr);

        return newSalaryTotal;
    }

    private String msToTimeStrConverter(long milliSeconds) {
        long hours = milliSeconds / 1000 / 60 / 60;
        long minutes = Math.abs(milliSeconds / 1000 / 60 % 60);

        String minutesStr = Long.toString(minutes);
        if (minutesStr.length() == 1) {
            minutesStr = "0" + minutesStr;
        }
        String time = hours + ":" + minutesStr;

        if (milliSeconds < 0 && hours == 0) {
            time = "-" + time;
        }

        return time;
    }

    private Date strToDateParser(String timeStr) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date timeDate = null;

        if (timeStr.length() == 4) {
            timeStr = "0" + timeStr;
        }
        try {
            timeDate = format.parse(timeStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return timeDate;
    }
}
