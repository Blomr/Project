package nl.mprog.postnlwerktijdensalaris;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

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

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
            DAYS_COLUMN_IDMONTH + " INTEGER, " +
            DAYS_COLUMN_IDDAY + " INTEGER, " +
            DAYS_COLUMN_DAY + " TEXT, " +
            DAYS_COLUMN_DISTRICTS + " TEXT, " +
            DAYS_COLUMN_TIMETOTAL + " TEXT, " +
            DAYS_COLUMN_TIMEGOAL + " TEXT, " +
            DAYS_COLUMN_TIMEEXTRA + " TEXT )";
        db.execSQL(query2);

        String query3 = "CREATE TABLE " + WALKS_TABLE_NAME +  " ( " +
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
    }

    public int addDay(DayObject dayObject) {
        SQLiteDatabase db = this.getReadableDatabase();
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

        db = this.getWritableDatabase();

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

    public int addWalk(WalkObject walkObject, Bundle sharedPref) {
        SQLiteDatabase db = this.getReadableDatabase();

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
        String timeTotalDay = cursor.getString(4);
        String timeGoalDay = cursor.getString(5);
        String timeExtraDay = cursor.getString(6);
        cursor.close();

        // read from months table
        query = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        cursor = db.rawQuery(query, new String[]{Integer.toString(walkObject.id1)});

        cursor.moveToFirst();
        double salaryMonth = cursor.getDouble(3);
        String timeMonth = cursor.getString(4);
        cursor.close();

        db.close();

        db = this.getWritableDatabase();

        // insert into walks table
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

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date timeTotalDayCur = null;
        Date timeGoalDayCur = null;
        Date timeExtraDayCur = null;
        Date timeTotalWalk = null;
        Date timeGoalWalk = null;
        Date timeExtraWalk = null;

        if (timeTotalDay.length() == 4) {
            timeTotalDay = "0" + timeTotalDay;
        }
        try {
            timeTotalDayCur = format.parse(timeTotalDay);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (timeGoalDay.length() == 4) {
            timeGoalDay = "0" + timeGoalDay;
        }
        try {
            timeGoalDayCur = format.parse(timeGoalDay);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        boolean timeDayNegative = false;
        if (timeExtraDay.charAt(0) == '-') {
            timeExtraDay = timeExtraDay.substring(1);
            timeDayNegative = true;
            if (timeExtraDay.length() == 4) {
                timeExtraDay = "0" + timeExtraDay;
            }
        }
        try {
            timeExtraDayCur = format.parse(timeExtraDay);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (walkObject.timeTotal.length() == 4) {
            walkObject.timeTotal = "0" + walkObject.timeTotal;
        }
        try {
            timeTotalWalk = format.parse(walkObject.timeTotal);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (walkObject.timeGoal.length() == 4) {
            walkObject.timeGoal = "0" + walkObject.timeGoal;
        }
        try {
            timeGoalWalk = format.parse(walkObject.timeGoal);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        boolean timeWalkNegative = false;
        if (walkObject.timeExtra.charAt(0) == '-') {
            walkObject.timeExtra = walkObject.timeExtra.substring(1);
            timeWalkNegative = true;
        }
        try {
            timeExtraWalk = format.parse(walkObject.timeExtra);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long timeTotalMs = 0;
        long timeGoalMs = 0;
        long timeExtraMs = 0;

        try {
            timeTotalMs = timeTotalDayCur.getTime() + timeTotalWalk.getTime();
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        if (!timeDayNegative && !timeWalkNegative) {
            try {
                timeGoalMs = timeGoalDayCur.getTime() + timeGoalWalk.getTime();
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else if (!timeDayNegative && timeWalkNegative){
            try {
                timeGoalMs = timeGoalDayCur.getTime() - timeGoalWalk.getTime();
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                timeGoalMs = -1 * timeGoalDayCur.getTime() - timeGoalWalk.getTime();
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }

        try {
            timeExtraMs = timeExtraDayCur.getTime() + timeExtraWalk.getTime();
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        long timeTotalMin = timeTotalMs / 1000 / 60;
        long timeTotalHours = timeTotalMin / 60;
        long timeTotalMinRest = timeTotalMin % 60;
        String timeTotalMinRestStr = Long.toString(timeTotalMinRest);
        if (timeTotalMinRestStr.length() == 1) {
            timeTotalMinRestStr = "0" + timeTotalMinRestStr;
        }
        timeTotalDay = timeTotalHours + ":" + timeTotalMinRestStr;

        long timeGoalMin = timeGoalMs / 1000 / 60;
        long timeGoalHours = timeGoalMin / 60;
        long timeGoalMinRest = Math.abs(timeGoalMin % 60);
        String timeGoalMinRestStr = Long.toString(timeGoalMinRest);
        if (timeGoalMinRestStr.length() == 1) {
            timeGoalMinRestStr = "0" + timeGoalMinRestStr;
        }
        timeGoalDay = timeGoalHours + ":" + timeGoalMinRestStr;

        long timeExtraMin = timeExtraMs / 1000 / 60;
        long timeExtraHours = timeExtraMin / 60;
        long timeExtraMinRest = timeExtraMin % 60;
        String timeExtraMinRestStr = Long.toString(timeExtraMinRest);
        if (timeExtraMinRestStr.length() == 1) {
            timeExtraMinRestStr = "0" + timeExtraMinRestStr;
        }
        timeExtraDay = timeExtraHours + ":" + timeExtraMinRestStr;

        if (timeExtraMin < 0 && timeExtraHours == 0) {
            timeExtraDay = "-" + timeExtraDay;
        }

        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_DISTRICTS, districts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, timeTotalDay);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, timeGoalDay);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, timeExtraDay);

        db.update(DAYS_TABLE_NAME, dayValues, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY +
                " = ?", new String[]{Integer.toString(walkObject.id1), Integer.toString(walkObject.id2)});

        // update months table
        if (timeMonth.length() == 4) {
            timeMonth = "0" + timeMonth;
        }

        Date timeMonthCur = null;
        try {
            timeMonthCur = format.parse(timeMonth);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long timeMonthMs = 0;
        try {
            timeMonthMs = timeMonthCur.getTime() + timeTotalWalk.getTime();
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        long timeMonthMin = timeMonthMs / 1000 / 60;
        long timeMonthHours = timeMonthMin / 60;
        long timeMonthMinRes = timeMonthMin % 60;
        String timeMonthMinResStr = Long.toString(timeMonthMinRes);
        if (timeMonthMinResStr.length() == 1) {
            timeMonthMinResStr = "0" + timeExtraMinRestStr;
        }
        timeMonth = timeMonthHours + ":" + timeMonthMinResStr;

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

        String timeContractStr = contractHoursStr + ":" + contractMinsStr;
        Date timeContract = null;
        try {
            timeContract = format.parse(timeContractStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long timeExtraMonthMs = 0;
        try {
            timeExtraMonthMs = timeMonthMs - timeContract.getTime();
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        double salaryContractHour = ((double) salaryCents) / 100 + salaryEuro;
        double salaryExtraHour = ((double) extraCents) / 100 + extraEuro;

        long timeExtraMonthMin = timeExtraMonthMs / 1000 / 60;
        long timeExtraMonthHour = timeExtraMonthMin / 60;
        long timeExtraMonthMinRest = timeExtraMonthMin % 60;

        double salaryContractHours;
        double salaryContractMins;
        double salaryExtraHours = 0;
        double salaryExtraMins = 0;

        if (timeExtraMonthMin <= 0) {
            salaryContractHours = timeMonthHours * salaryContractHour;
            salaryContractMins = timeMonthMinRes / 60 * salaryContractHour;
        }
        else {
            salaryContractHours = contractHours * salaryContractHour;
            salaryContractMins = contractMins / 60 * salaryContractHour;
            salaryExtraHours = timeExtraMonthHour * salaryExtraHour;
            salaryExtraMins = timeExtraMonthMinRest / 60 * salaryExtraHour;
        }

        double salaryWalk = salaryContractHours + salaryContractMins + salaryExtraHours +
                            salaryExtraMins;

        double salaryTotal = salaryMonth + salaryWalk;

        ContentValues monthValues = new ContentValues();
        monthValues.put(MONTHS_COLUMN_SALARY, salaryTotal);
        monthValues.put(MONTHS_COLUMN_TIME, timeMonth);

        db.update(MONTHS_TABLE_NAME, monthValues, MONTHS_COLUMN_ID + " = ?",
                new String[]{Integer.toString(walkObject.id1)});

        db.close();

        return idWalk;
    }

    public WalkObject getWalk(int idMonth, int idDay, int idWalk) {
        SQLiteDatabase db = this.getReadableDatabase();
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
        SQLiteDatabase db = this.getReadableDatabase();
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
        SQLiteDatabase db = this.getReadableDatabase();
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
        SQLiteDatabase db = this.getReadableDatabase();
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
}
