/**
 * DatabaseHandler.java
 *
 * The databasehandler is a subclass of the SQLiteOpenHelper.
 * It contains methods to create, update and delete rows in the four tables.
 * The four tables are: Months, Days, Walks and Districts.
 * They are corresponding with the same name activities.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.databasehandler;

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
import java.util.Locale;

import nl.mprog.postnlwerktijdensalaris.modelclasses.Day;
import nl.mprog.postnlwerktijdensalaris.modelclasses.District;
import nl.mprog.postnlwerktijdensalaris.modelclasses.Month;
import nl.mprog.postnlwerktijdensalaris.modelclasses.Walk;

public class DatabaseHandler extends SQLiteOpenHelper {

    // initialize constant variables
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

    // milliseconds value to make correct time calculations
    private final int ZERO_TIME_VALUE = 3600000;

    /**
     * Constructor for DatabaseHandler objects.
     */
    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates database if not already created.
     */
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

    /**
     * Upgrades database if database is already created.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MONTHS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DAYS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WALKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DISTRICTS_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Creates row in months table and returns id.
     */
    public int addMonth(Month monthObj) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MONTHS_COLUMN_MONTH, monthObj.month);
        values.put(MONTHS_COLUMN_DAYS, monthObj.days);
        values.put(MONTHS_COLUMN_SALARY, monthObj.salary);
        values.put(MONTHS_COLUMN_TIME, monthObj.time);
        int lastId = (int) db.insert(MONTHS_TABLE_NAME, null, values);
        db.close();

        return lastId;
    }

    /**
     * Creates row in days table, updates month row in months table and returns id of day row.
     */
    public int addDay(Day dayObj) {
        SQLiteDatabase db = getReadableDatabase();
        int idDay = 0;
        boolean foundFreeId = false;
        while (!foundFreeId) {
            idDay++;
            String query = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH +
                           " = ? AND " + DAYS_COLUMN_IDDAY + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(dayObj.id1),
                            Integer.toString(idDay)});
            if (cursor.getCount() == 0) {
                foundFreeId = true;
                cursor.close();
            }
        }

        String query = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(dayObj.id1)});
        cursor.moveToFirst();

        int days = cursor.getInt(2);

        cursor.close();
        db.close();

        // by adding a day, increase days value of corresponding month row by 1
        days++;

        db = getWritableDatabase();
        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_IDMONTH, dayObj.id1);
        dayValues.put(DAYS_COLUMN_IDDAY, idDay);
        dayValues.put(DAYS_COLUMN_DAY, dayObj.day);
        dayValues.put(DAYS_COLUMN_DISTRICTS, dayObj.districts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, dayObj.timeTotal);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, dayObj.timeGoal);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, dayObj.timeExtra);
        db.insert(DAYS_TABLE_NAME, null, dayValues);

        ContentValues monthValues = new ContentValues();
        monthValues.put(MONTHS_COLUMN_DAYS, days);
        db.update(MONTHS_TABLE_NAME, monthValues, MONTHS_COLUMN_ID + " = ?",
                  new String[]{Integer.toString(dayObj.id1)});
        db.close();

        return idDay;
    }

    /**
     * Creates row in walks table, updates corresponding day row in days table and updates
     * corresponding month row in months table.
     */
    public void addWalk(Walk walkObj, Bundle sharedPref) {
        SQLiteDatabase db = getReadableDatabase();

        // read from walks table and initialize free id
        int idWalk = 0;
        boolean foundFreeId = false;
        while (!foundFreeId) {
            idWalk++;
            String query = "SELECT * FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_IDMONTH +
                           " = ? AND " + WALKS_COLUMN_IDDAY + " = ? AND " + WALKS_COLUMN_IDWALK +
                           " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(walkObj.id1),
                            Integer.toString(walkObj.id2), Integer.toString(idWalk)});
            if (cursor.getCount() == 0) {
                foundFreeId = true;
                cursor.close();
            }
        }

        // read from days table and initialize variables of corresponding idMonth and idDay
        String query = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH +
                       " = ? AND " + DAYS_COLUMN_IDDAY + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(walkObj.id1),
                Integer.toString(walkObj.id2)});
        cursor.moveToFirst();

        String districts = cursor.getString(3);
        String dayTimeTotalStr = cursor.getString(4);
        String dayTimeGoalStr = cursor.getString(5);
        String dayTimeExtraStr = cursor.getString(6);

        cursor.close();

        // read from months table and initialize time variable of corresponding idMonth
        query = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        cursor = db.rawQuery(query, new String[]{Integer.toString(walkObj.id1)});
        cursor.moveToFirst();

        String monthTime = cursor.getString(4);

        cursor.close();
        db.close();

        // insert walk object and id into walks table
        db = getWritableDatabase();
        ContentValues walkValues = new ContentValues();
        walkValues.put(WALKS_COLUMN_IDMONTH, walkObj.id1);
        walkValues.put(WALKS_COLUMN_IDDAY, walkObj.id2);
        walkValues.put(WALKS_COLUMN_IDWALK, idWalk);
        walkValues.put(WALKS_COLUMN_DISTRICTCODE, walkObj.districtCode);
        walkValues.put(WALKS_COLUMN_DAYTYPE, walkObj.dayType);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN1, walkObj.timeBegin1);
        walkValues.put(WALKS_COLUMN_TIMEEND1, walkObj.timeEnd1);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN2, walkObj.timeBegin2);
        walkValues.put(WALKS_COLUMN_TIMEEND2, walkObj.timeEnd2);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN3, walkObj.timeBegin3);
        walkValues.put(WALKS_COLUMN_TIMEEND3, walkObj.timeEnd3);
        walkValues.put(WALKS_COLUMN_TIMEGOAL, walkObj.timeGoal);
        walkValues.put(WALKS_COLUMN_TIMEEXTRA, walkObj.timeExtra);
        walkValues.put(WALKS_COLUMN_TIMETOTAL, walkObj.timeTotal);
        db.insert(WALKS_TABLE_NAME, null, walkValues);

        // add district code to districts of day
        districts = districts + walkObj.districtCode + "  ";

        // parse time strings into date objects
        Date dayTimeTotal = strToDateParser(dayTimeTotalStr);
        Date dayTimeGoal = strToDateParser(dayTimeGoalStr);

        // if there is a minus char in string, remove it and set boolean to true
        boolean timeDayNegative = false;
        if (dayTimeExtraStr.charAt(0) == '-') {
            dayTimeExtraStr = dayTimeExtraStr.substring(1);
            timeDayNegative = true;
        }
        Date dayTimeExtra = strToDateParser(dayTimeExtraStr);

        Date walkTimeTotal = strToDateParser(walkObj.timeTotal);
        Date walkTimeGoal = strToDateParser(walkObj.timeGoal);

        boolean timeWalkNegative = false;
        if (walkObj.timeExtra.charAt(0) == '-') {
            walkObj.timeExtra = walkObj.timeExtra.substring(1);
            timeWalkNegative = true;
        }
        Date walkTimeExtra = strToDateParser(walkObj.timeExtra);

        // get new day times by adding walk times to old day times
        long timeTotalMs = dayTimeTotal.getTime() + walkTimeTotal.getTime() + ZERO_TIME_VALUE * 2;
        long timeGoalMs = dayTimeGoal.getTime() + walkTimeGoal.getTime() + ZERO_TIME_VALUE * 2;

        // add, subtract or invert times depending on boolean values
        long timeExtraMs;
        if (!timeDayNegative && !timeWalkNegative) {
            timeExtraMs = dayTimeExtra.getTime() + walkTimeExtra.getTime() + ZERO_TIME_VALUE * 2;
        }
        else if (!timeDayNegative && timeWalkNegative) {
            timeExtraMs = dayTimeExtra.getTime() + ZERO_TIME_VALUE - (walkTimeExtra.getTime()
                          + ZERO_TIME_VALUE);
        }
        else if (timeDayNegative && !timeWalkNegative) {
            timeExtraMs = -1 * (dayTimeExtra.getTime() + ZERO_TIME_VALUE)
                          + walkTimeExtra.getTime() + ZERO_TIME_VALUE;
        }
        else {
            timeExtraMs = -1 * (dayTimeExtra.getTime() + ZERO_TIME_VALUE)
                          - (walkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }

        // convert milliseconds into time strings
        dayTimeTotalStr = msToTimeStrConverter(timeTotalMs);
        dayTimeGoalStr = msToTimeStrConverter(timeGoalMs);
        dayTimeExtraStr = msToTimeStrConverter(timeExtraMs);

        // update days table
        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_DISTRICTS, districts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, dayTimeTotalStr);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, dayTimeGoalStr);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, dayTimeExtraStr);
        db.update(DAYS_TABLE_NAME, dayValues, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY +
                  " = ?", new String[]{Integer.toString(walkObj.id1), Integer.toString(walkObj.id2)});

        Date oldMonthTime = strToDateParser(monthTime);

        // add time of walk to total time of month
        long newMonthTimeMs = oldMonthTime.getTime() + walkTimeTotal.getTime() + ZERO_TIME_VALUE * 2;
        monthTime = msToTimeStrConverter(newMonthTimeMs);

        // calculate corresponding salary of new total time of month
        double salaryTotal = salaryCalculator(newMonthTimeMs, sharedPref);

        // update months table
        ContentValues monthValues = new ContentValues();
        monthValues.put(MONTHS_COLUMN_SALARY, salaryTotal);
        monthValues.put(MONTHS_COLUMN_TIME, monthTime);
        db.update(MONTHS_TABLE_NAME, monthValues, MONTHS_COLUMN_ID + " = ?",
                  new String[]{Integer.toString(walkObj.id1)});
        db.close();
    }

    /**
     * Creates row in districts table.
     */
    public void addDistrict(District districtObj) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DISTRICTS_COLUMN_DISTRICTCODE, districtObj.districtCode);
        values.put(DISTRICTS_COLUMN_TIMEGOALBUSY, districtObj.timeGoalBusy);
        values.put(DISTRICTS_COLUMN_TIMEGOALCALM, districtObj.timeGoalCalm);
        db.insert(DISTRICTS_TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Gets specific row from walks table and returns values in walk object.
     */
    public Walk getWalk(int idMonth, int idDay, int idWalk) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_IDMONTH + " = ? AND "
                       + WALKS_COLUMN_IDDAY + " = ? AND " + WALKS_COLUMN_IDWALK + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(idMonth), Integer.toString(idDay),
                        Integer.toString(idWalk)});
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

        return new Walk(idMonth, idDay, idWalk, districtCode, dayType, timeBegin1, timeEnd1,
                              timeBegin2, timeEnd2, timeBegin3, timeEnd3, timeGoal, timeExtra,
                              timeTotal);
    }

    /**
     * Gets specific row from districts table and returns values in district object.
     */
    public District getDistrict(int idDistrict) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DISTRICTS_TABLE_NAME + " WHERE " + DISTRICTS_COLUMN_ID
                       + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(idDistrict)});
        cursor.moveToFirst();

        String districtCode = cursor.getString(1);
        String timeGoalBusy = cursor.getString(2);
        String timeGoalCalm = cursor.getString(3);

        cursor.close();
        db.close();

        return new District(idDistrict, districtCode, timeGoalBusy, timeGoalCalm);
    }

    /**
     * Gets all walk rows of a specific day id from walks table and returns objects in an arraylist.
     */
    public ArrayList<Walk> getWalksOfDay(int idMonth, int idDay) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Walk> walkObjects = new ArrayList<>();
        String query = "SELECT * FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_IDMONTH + " = ? AND "
                       + WALKS_COLUMN_IDDAY + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(idMonth),
                Integer.toString(idDay)});
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

            Walk walkObj = new Walk(idMonth, idDay, idWalk, districtCode, dayType, timeBegin1,
                                 timeEnd1, timeBegin2, timeEnd2, timeBegin3, timeEnd3, timeGoal, timeExtra,
                                 timeTotal);

            walkObjects.add(walkObj);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return walkObjects;
    }

    /**
     * Gets all day rows of a specific month id from days table and returns objects in an arraylist.
     */
    public ArrayList<Day> getDaysOfMonth(int idMonth) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Day> dayObjects = new ArrayList<>();
        String query = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(idMonth)});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int idDay = cursor.getInt(1);
            String day = cursor.getString(2);
            String districts = cursor.getString(3);
            String timeTotal = cursor.getString(4);
            String timeGoal = cursor.getString(5);
            String timeExtra = cursor.getString(6);

            Day dayObj = new Day(idMonth, idDay, day, districts, timeTotal, timeGoal,
                               timeExtra);

            dayObjects.add(dayObj);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return dayObjects;
    }

    /**
     * Gets all month rows of months table.
     */
    public ArrayList<Month> getMonths() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Month> monthObjects = new ArrayList<>();
        String query = "SELECT * FROM " + MONTHS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String month = cursor.getString(1);
            int days = cursor.getInt(2);
            double salary = cursor.getDouble(3);
            String time = cursor.getString(4);

            Month monthObj = new Month(id, month, days, salary, time);

            monthObjects.add(monthObj);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return monthObjects;
    }

    /**
     * Gets all district rows of districts table.
     */
    public ArrayList<District> getDistricts() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<District> districtObjects = new ArrayList<>();
        String query = "SELECT * FROM " + DISTRICTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String districtCode = cursor.getString(1);
            String timeGoalBusy = cursor.getString(2);
            String timeGoalCalm = cursor.getString(3);

            District districtObj = new District(id, districtCode, timeGoalBusy, timeGoalCalm);

            districtObjects.add(districtObj);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return districtObjects;
    }

    /**
     * Gets specific name of month of months table.
     */
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

    /**
     * Gets specific name of day of days table.
     */
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

    /**
     * Deletes specific month row from months table.
     */
    public void deleteMonth(int idMonth) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MONTHS_TABLE_NAME, MONTHS_COLUMN_ID + " = ?", new String[]{Integer.toString(idMonth)});
        db.delete(DAYS_TABLE_NAME, DAYS_COLUMN_IDMONTH + " = ?", new String[]{Integer.toString(idMonth)});
        db.delete(WALKS_TABLE_NAME, WALKS_COLUMN_IDMONTH + " = ?", new String[]{Integer.toString(idMonth)});
        db.close();
    }

    /**
     * Deletes specific day row from days table, deletes corresponding walk rows from walks table
     * and updates corresponding month row in months table.
     */
    public void deleteDay(int idMonth, int idDay, Bundle sharedPref) {

        // read from months table and initialize variables of corresponding idMonth
        SQLiteDatabase db = getReadableDatabase();
        String queryMonths = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(queryMonths, new String[]{Integer.toString(idMonth)});
        cursor.moveToFirst();

        int days = cursor.getInt(2);
        String oldMonthTimeStr = cursor.getString(4);

        cursor.close();

        // read from days table and initialize variables of corresponding idMonth and idDay
        String queryDays = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH +
                           " = ? AND " + DAYS_COLUMN_IDDAY + " = ?";
        cursor = db.rawQuery(queryDays, new String[]{Integer.toString(idMonth), Integer.toString(idDay)});
        cursor.moveToFirst();

        String dayTimeTotalStr = cursor.getString(4);

        cursor.close();
        db.close();

        // by deleting a day, decrease days value of corresponding month row by 1
        days--;

        // subtract day's total time of month's total time
        Date oldMonthTime = strToDateParser(oldMonthTimeStr);
        Date dayTimeTotal = strToDateParser(dayTimeTotalStr);

        long newMonthTimeMs = oldMonthTime.getTime() + ZERO_TIME_VALUE - (dayTimeTotal.getTime()
                              + ZERO_TIME_VALUE);
        String newMonthTime = msToTimeStrConverter(newMonthTimeMs);
        double newSalaryTotal = salaryCalculator(newMonthTimeMs, sharedPref);

        // update months, and delete rows from days table and walks table
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

    /**
     * Deletes specific walk row from walks table, updates corresponding day row from days table
     * and updates corresponding month row in months table.
     */
    public void deleteWalk(int idMonth, int idDay, int idWalk, Bundle sharedPref) {

        // read from walks table and initialize variables of corresponding idMonth, idDay and idWalk
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

        // read from days table and initialize variables of corresponding idMonth and idDay
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

        // read from months table and initialize variables of corresponding idMonth
        String queryMonths = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        cursor = db.rawQuery(queryMonths, new String[]{Integer.toString(idMonth)});
        cursor.moveToFirst();

        String oldMonthTimeStr = cursor.getString(4);

        cursor.close();
        db.close();

        // delete district code from days' districts string
        districts = districts.replaceFirst(districtCode + " ", "");

        // parse time string into date object
        Date oldDayTimeTotal = strToDateParser(oldDayTimeTotalStr);
        Date oldDayTimeGoal = strToDateParser(oldDayTimeGoalStr);

        // if there is a minus char in string, remove it and set boolean to true
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

        // get new day times by distracting walk times of old day times
        long newDayTimeTotalMs = oldDayTimeTotal.getTime() + ZERO_TIME_VALUE - (walkTimeTotal.getTime()
                            + ZERO_TIME_VALUE);
        long newDayTimeGoalMs = oldDayTimeGoal.getTime() + ZERO_TIME_VALUE - (walkTimeGoal.getTime()
                           + ZERO_TIME_VALUE);

        // add, subtract or invert times depending on boolean values
        long newDayTimeExtraMs;
        if (!oldDayTimeNeg && !walkTimeNeg) {
            newDayTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE - (walkTimeExtra.getTime()
                                + ZERO_TIME_VALUE);
        }
        else if (!oldDayTimeNeg && walkTimeNeg) {
            newDayTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE + walkTimeExtra.getTime()
                                + ZERO_TIME_VALUE;
        }
        else if (oldDayTimeNeg && !walkTimeNeg) {
            newDayTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE)
                                - (walkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }
        else {
            newDayTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE)
                                + (walkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }

        // convert milliseconds into time strings
        String newDayTimeTotal = msToTimeStrConverter(newDayTimeTotalMs);
        String newDayTimeGoal = msToTimeStrConverter(newDayTimeGoalMs);
        String newDayTimeExtra = msToTimeStrConverter(newDayTimeExtraMs);

        // update days table
        db = getWritableDatabase();
        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_DISTRICTS, districts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, newDayTimeTotal);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, newDayTimeGoal);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, newDayTimeExtra);
        db.update(DAYS_TABLE_NAME, dayValues, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY
                  + " = ?", new String[]{Integer.toString(idMonth), Integer.toString(idDay)});

        // parse month's time string into date
        Date oldMonthTime = strToDateParser(oldMonthTimeStr);

        // subtract walk's total time from month's time
        long newMonthTimeMs = oldMonthTime.getTime() + ZERO_TIME_VALUE - (walkTimeTotal.getTime()
                              + ZERO_TIME_VALUE);
        String newMonthTime = msToTimeStrConverter(newMonthTimeMs);

        // calculate new salary of month
        double newSalaryTotal = salaryCalculator(newMonthTimeMs, sharedPref);

        // update month's table and delete walk row from walks table
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

    /**
     * Deletes specific row from districts table.
     */
    public void deleteDistrict(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DISTRICTS_TABLE_NAME, DISTRICTS_COLUMN_ID + " = ?",
                new String[]{Integer.toString(id)});
        db.close();
    }

    /**
     * Updates specific month's name in months table.
     */
    public void editMonthName(int idMonth, String monthName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MONTHS_COLUMN_MONTH, monthName);
        db.update(MONTHS_TABLE_NAME, values, MONTHS_COLUMN_ID + " = ?",
                new String[]{Integer.toString(idMonth)});
        db.close();
    }

    /**
     * Updates specific day's name in days table.
     */
    public void editDayName(int idMonth, int idDay, String dayName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DAYS_COLUMN_DAY, dayName);
        db.update(DAYS_TABLE_NAME, values, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY +
                  " = ?", new String[]{Integer.toString(idMonth), Integer.toString(idDay)});
        db.close();
    }

    /**
     * Updates specific walk in walks table and updates corresponding day in days table and
     * month in months table.
     */
    public void editWalk(Walk walkObj, Bundle sharedPref) {

        // read from walks table and initialize variables of corresponding ids in new object
        SQLiteDatabase db = getReadableDatabase();
        String queryWalk = "SELECT * FROM " + WALKS_TABLE_NAME + " WHERE " + WALKS_COLUMN_IDMONTH +
                           " = ? AND " + WALKS_COLUMN_IDDAY + " = ? AND " + WALKS_COLUMN_IDWALK + " = ?";
        Cursor cursor = db.rawQuery(queryWalk, new String[]{Integer.toString(walkObj.id1),
                        Integer.toString(walkObj.id2), Integer.toString(walkObj.id3)});
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

        Walk oldWalkObj = new Walk(walkObj.id1, walkObj.id2, walkObj.id3,
                                districtCode, dayType, timeBegin1, timeEnd1, timeBegin2, timeEnd2,
                                timeBegin3, timeEnd3, timeGoal, timeExtra, timeTotal);

        // read from days table and initialize variables of corresponding ids in new object
        String queryDay = "SELECT * FROM " + DAYS_TABLE_NAME + " WHERE " + DAYS_COLUMN_IDMONTH +
                          " = ? AND " + DAYS_COLUMN_IDDAY + " = ?";
        cursor = db.rawQuery(queryDay, new String[]{Integer.toString(walkObj.id1),
                             Integer.toString(walkObj.id2)});
        cursor.moveToFirst();

        String day = cursor.getString(2);
        String districts = cursor.getString(3);
        timeTotal = cursor.getString(4);
        timeGoal = cursor.getString(5);
        timeExtra = cursor.getString(6);

        cursor.close();

        Day oldDayObj = new Day(walkObj.id1, walkObj.id2, day, districts,
                              timeTotal, timeGoal, timeExtra);

        // read from months table and initialize variables of corresponding id in new object
        String queryMonth = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        cursor = db.rawQuery(queryMonth, new String[]{Integer.toString(walkObj.id1)});
        cursor.moveToFirst();

        String month = cursor.getString(1);
        int days = cursor.getInt(2);
        double salary = cursor.getDouble(3);
        String time = cursor.getString(4);

        cursor.close();
        db.close();

        Month oldMonthObj = new Month(walkObj.id1, month, days, salary, time);

        // update walk's row of corresponding ids with values of new walk object
        db = getWritableDatabase();
        ContentValues walkValues = new ContentValues();
        walkValues.put(WALKS_COLUMN_DISTRICTCODE, walkObj.districtCode);
        walkValues.put(WALKS_COLUMN_DAYTYPE, walkObj.dayType);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN1, walkObj.timeBegin1);
        walkValues.put(WALKS_COLUMN_TIMEEND1, walkObj.timeEnd1);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN2, walkObj.timeBegin2);
        walkValues.put(WALKS_COLUMN_TIMEEND2, walkObj.timeEnd2);
        walkValues.put(WALKS_COLUMN_TIMEBEGIN3, walkObj.timeBegin3);
        walkValues.put(WALKS_COLUMN_TIMEEND3, walkObj.timeEnd3);
        walkValues.put(WALKS_COLUMN_TIMEGOAL, walkObj.timeGoal);
        walkValues.put(WALKS_COLUMN_TIMEEXTRA, walkObj.timeExtra);
        walkValues.put(WALKS_COLUMN_TIMETOTAL, walkObj.timeTotal);
        db.update(WALKS_TABLE_NAME, walkValues, WALKS_COLUMN_IDMONTH + " = ? AND " + WALKS_COLUMN_IDDAY +
                  " = ? AND " + WALKS_COLUMN_IDWALK + " = ?", new String[]{Integer.toString(walkObj.id1),
                  Integer.toString(walkObj.id2), Integer.toString(walkObj.id3)});

        // replace old district with new district in day's districts string
        String newDistricts = oldDayObj.districts.replaceFirst(oldWalkObj.districtCode,
                              walkObj.districtCode);

        // parse time strings into date objects
        Date oldDayTimeTotal = strToDateParser(oldDayObj.timeTotal);
        Date oldWalkTimeTotal = strToDateParser(oldWalkObj.timeTotal);
        Date newWalkTimeTotal = strToDateParser(walkObj.timeTotal);

        // subtract old walk's time total from day's time and add new walk's time
        long newDayTimeTotalMs = oldDayTimeTotal.getTime() + ZERO_TIME_VALUE - (oldWalkTimeTotal.getTime()
                                 + ZERO_TIME_VALUE) + newWalkTimeTotal.getTime() + ZERO_TIME_VALUE;
        String newDayTimeTotal = msToTimeStrConverter(newDayTimeTotalMs);

        Date oldDayTimeGoal = strToDateParser(oldDayObj.timeGoal);
        Date oldWalkTimeGoal = strToDateParser(oldWalkObj.timeGoal);
        Date newWalkTimeGoal = strToDateParser(walkObj.timeGoal);

        long newDayTimeGoalMs = oldDayTimeGoal.getTime() + ZERO_TIME_VALUE - (oldWalkTimeGoal.getTime()
                                + ZERO_TIME_VALUE) + newWalkTimeGoal.getTime() + ZERO_TIME_VALUE;
        String newDayTimeGoal = msToTimeStrConverter(newDayTimeGoalMs);

        // if there is a minus char in string, remove it and set boolean to true
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
        if (walkObj.timeExtra.charAt(0) == '-') {
            walkObj.timeExtra = walkObj.timeExtra.substring(1);
            newWalkTimeNeg = true;
        }
        Date newWalkTimeExtra = strToDateParser(walkObj.timeExtra);

        // add, subtract or invert times depending on boolean values
        long newDayTimeExtraMs;
        if (!oldDayTimeNeg && !oldWalkTimeNeg && !newWalkTimeNeg) {
            newDayTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE - (oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE) + newWalkTimeExtra.getTime() + ZERO_TIME_VALUE;
        }
        else if (!oldDayTimeNeg && !oldWalkTimeNeg && newWalkTimeNeg) {
            newDayTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE - (oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE) - (newWalkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }
        else if (!oldDayTimeNeg && oldWalkTimeNeg && !newWalkTimeNeg) {
            newDayTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE + oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE + newWalkTimeExtra.getTime() + ZERO_TIME_VALUE;
        }
        else if (!oldDayTimeNeg && oldWalkTimeNeg && newWalkTimeNeg) {
            newDayTimeExtraMs = oldDayTimeExtra.getTime() + ZERO_TIME_VALUE + oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE - (newWalkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }
        else if (oldDayTimeNeg && !oldWalkTimeNeg && !newWalkTimeNeg) {
            newDayTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE) - (oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE) + newWalkTimeExtra.getTime() + ZERO_TIME_VALUE;
        }
        else if (oldDayTimeNeg && !oldWalkTimeNeg && newWalkTimeNeg) {
            newDayTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE) - (oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE) - (newWalkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }
        else if (oldDayTimeNeg && oldWalkTimeNeg && !newWalkTimeNeg) {
            newDayTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE) + oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE + newWalkTimeExtra.getTime() + ZERO_TIME_VALUE;
        }
        else {
            newDayTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + ZERO_TIME_VALUE) + oldWalkTimeExtra.getTime()
                             + ZERO_TIME_VALUE - (newWalkTimeExtra.getTime() + ZERO_TIME_VALUE);
        }
        String newDayTimeExtra = msToTimeStrConverter(newDayTimeExtraMs);

        // update days table
        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_DISTRICTS, newDistricts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, newDayTimeTotal);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, newDayTimeGoal);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, newDayTimeExtra);
        db.update(DAYS_TABLE_NAME, dayValues, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY
                  + " = ?", new String[]{Integer.toString(walkObj.id1),
                  Integer.toString(walkObj.id2)});

        // parse time string into date object
        Date oldMonthTime = strToDateParser(oldMonthObj.time);

        // calculate month's new time by subtracting old day's time total and adding new day's time total
        long newMonthTimeMs = oldMonthTime.getTime() + ZERO_TIME_VALUE - (oldDayTimeTotal.getTime()
                              + ZERO_TIME_VALUE) + newDayTimeTotalMs;
        String newMonthTime = msToTimeStrConverter(newMonthTimeMs);

        // calculate new salary of month
        double newSalaryTotal = salaryCalculator(newMonthTimeMs, sharedPref);

        // update months table
        ContentValues monthValues = new ContentValues();
        monthValues.put(MONTHS_COLUMN_SALARY, newSalaryTotal);
        monthValues.put(MONTHS_COLUMN_TIME, newMonthTime);
        db.update(MONTHS_TABLE_NAME, monthValues, MONTHS_COLUMN_ID + " = ?",
                new String[]{Integer.toString(walkObj.id1)});
        db.close();
    }

    /**
     * Updates specific district in districts table.
     */
    public void editDistrict(District districtObj) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DISTRICTS_COLUMN_DISTRICTCODE, districtObj.districtCode);
        values.put(DISTRICTS_COLUMN_TIMEGOALBUSY, districtObj.timeGoalBusy);
        values.put(DISTRICTS_COLUMN_TIMEGOALCALM, districtObj.timeGoalCalm);
        db.update(DISTRICTS_TABLE_NAME, values, DISTRICTS_COLUMN_ID + " = ?",
                  new String[]{Integer.toString(districtObj.id)});
        db.close();
    }

    /**
     * Calculates the corresponding salary of month's time.
     * Contract hours will be multiplied by salary per hour.
     * Extra hours will be multiplied by overtime's salary.
     */
    private double salaryCalculator(long newMonthTimeMs, Bundle sharedPref) {

        // initialize shared preferences variable from bundle
        int contractHours = sharedPref.getInt("contractHours");
        int contractMins = sharedPref.getInt("contractMins");
        int salaryEuro = sharedPref.getInt("salaryEuro");
        int salaryCents = sharedPref.getInt("salaryCents");
        int extraEuro = sharedPref.getInt("extraEuro");
        int extraCents = sharedPref.getInt("extraCents");

        // adding a zero if minutes is one digit
        String contractHoursStr = Integer.toString(contractHours);
        if (contractHoursStr.length() == 1) {
            contractHoursStr = "0" + contractHoursStr;
        }

        String contractMinsStr = Integer.toString(contractMins);
        if (contractMinsStr.length() == 1) {
            contractMinsStr = "0" + contractMinsStr;
        }

        Date timeContract = strToDateParser(contractHoursStr + ":" + contractMinsStr);

        // subtract contract time from month time to calculate overtime
        long timeExtraMonthMs = newMonthTimeMs - (timeContract.getTime() + ZERO_TIME_VALUE);

        // calculate month's hours and minutes
        long monthHours = newMonthTimeMs / 1000 / 60 / 60;
        long monthMins = newMonthTimeMs / 1000 / 60 % 60;

        // calculate salaries per hour
        double salaryContractHour = ((double) salaryCents) / 100 + salaryEuro;
        double salaryExtraHour = ((double) extraCents) / 100 + extraEuro;

        // calculate overtime's hours and minutes
        long timeExtraMonthHours = timeExtraMonthMs / 1000 / 60 / 60;
        long timeExtraMonthMins = timeExtraMonthMs / 1000 / 60 % 60;

        // calculate hour's and minute's salary, and if exist, overtime's salary
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

        // adding all salary values together to calculate total salary
        double newSalaryTotal = salaryContractHours + salaryContractMins + salaryExtraHours +
                                salaryExtraMins;

        // round to two decimals
        DecimalFormat twoDecFormat = new DecimalFormat("#.##");
        String salaryTotalStr = twoDecFormat.format(newSalaryTotal).replaceAll(",",".");
        newSalaryTotal = Double.parseDouble(salaryTotalStr);

        return newSalaryTotal;
    }

    /**
     * Converts a long value of milliseconds into a string format of HH:mm.
     */
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

    /**
     * Parses a time string of HH:mm into a date object.
     */
    private Date strToDateParser(String timeStr) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
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
