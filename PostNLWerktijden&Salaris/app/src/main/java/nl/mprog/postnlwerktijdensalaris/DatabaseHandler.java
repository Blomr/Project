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
        String timeTotalDay = cursor.getString(4);
        String timeGoalDay = cursor.getString(5);
        String timeExtraDay = cursor.getString(6);
        cursor.close();

        // read from months table
        query = "SELECT * FROM " + MONTHS_TABLE_NAME + " WHERE " + MONTHS_COLUMN_ID + " = ?";
        cursor = db.rawQuery(query, new String[]{Integer.toString(walkObject.id1)});

        cursor.moveToFirst();
        String timeMonth = cursor.getString(4);
        cursor.close();

        db.close();

        db = getWritableDatabase();

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
        }
        if (timeExtraDay.length() == 4) {
            timeExtraDay = "0" + timeExtraDay;
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
        if (walkObject.timeExtra.length() == 4) {
            walkObject.timeExtra = "0" + walkObject.timeExtra;
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
            timeTotalMs = timeTotalDayCur.getTime() + timeTotalWalk.getTime() + 7200000;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        try {
            timeGoalMs = timeGoalDayCur.getTime() + timeGoalWalk.getTime() + 7200000;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        if (!timeDayNegative && !timeWalkNegative) {
            try {
                timeExtraMs = timeExtraDayCur.getTime() + timeExtraWalk.getTime() + 7200000;
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else if (!timeDayNegative && timeWalkNegative){
            try {
                timeExtraMs = timeExtraDayCur.getTime() + 3600000 - (timeExtraWalk.getTime() + 3600000);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                timeExtraMs = -1 * (timeExtraDayCur.getTime() + 3600000) - (timeExtraWalk.getTime() + 3600000);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }

        long timeTotalMin = timeTotalMs / 1000 / 60;
        long timeTotalHours = timeTotalMin / 60;
        long timeTotalMinRes = timeTotalMin % 60;
        String timeTotalMinResStr = Long.toString(timeTotalMinRes);
        if (timeTotalMinResStr.length() == 1) {
            timeTotalMinResStr = "0" + timeTotalMinResStr;
        }
        timeTotalDay = timeTotalHours + ":" + timeTotalMinResStr;

        long timeGoalMin = timeGoalMs / 1000 / 60;
        long timeGoalHours = timeGoalMin / 60;
        long timeGoalMinRes = timeGoalMin % 60;
        String timeGoalMinResStr = Long.toString(timeGoalMinRes);
        if (timeGoalMinResStr.length() == 1) {
            timeGoalMinResStr = "0" + timeGoalMinResStr;
        }
        timeGoalDay = timeGoalHours + ":" + timeGoalMinResStr;

        long timeExtraMin = timeExtraMs / 1000 / 60;
        long timeExtraHours = timeExtraMin / 60;
        long timeExtraMinRes = Math.abs(timeExtraMin % 60);
        String timeExtraMinResStr = Long.toString(timeExtraMinRes);
        if (timeExtraMinResStr.length() == 1) {
            timeExtraMinResStr = "0" + timeExtraMinResStr;
        }
        timeExtraDay = timeExtraHours + ":" + timeExtraMinResStr;

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
            timeMonthMs = timeMonthCur.getTime() + timeTotalWalk.getTime() + 7200000;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        long timeMonthMin = timeMonthMs / 1000 / 60;
        long timeMonthHours = timeMonthMin / 60;
        long timeMonthMinRes = timeMonthMin % 60;
        String timeMonthMinResStr = Long.toString(timeMonthMinRes);
        if (timeMonthMinResStr.length() == 1) {
            timeMonthMinResStr = "0" + timeMonthMinResStr;
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
            timeExtraMonthMs = timeMonthMs - (timeContract.getTime() + 3600000);
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
            salaryContractMins = ((double) timeMonthMinRes) / 60 * salaryContractHour;
        }
        else {
            salaryContractHours = contractHours * salaryContractHour;
            salaryContractMins = contractMins / 60 * salaryContractHour;
            salaryExtraHours = timeExtraMonthHour * salaryExtraHour;
            salaryExtraMins = timeExtraMonthMinRest / 60 * salaryExtraHour;
        }

        double salaryTotal = salaryContractHours + salaryContractMins + salaryExtraHours +
                             salaryExtraMins;

        DecimalFormat threeDecFormat = new DecimalFormat("#.##");
        String salaryTotalStr = threeDecFormat.format(salaryTotal).replaceAll(",",".");
        salaryTotal = Double.parseDouble(salaryTotalStr);

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

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date oldMonthTime = null;
        Date dayTimeTotal = null;

        try {
            oldMonthTime = format.parse(oldMonthTimeStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        try {
            dayTimeTotal = format.parse(dayTimeTotalStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long newMonthTimeMs = 0;
        try {
            newMonthTimeMs = oldMonthTime.getTime() + 3600000 - (dayTimeTotal.getTime() + 3600000);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        long newMonthTimeMin = newMonthTimeMs / 1000 / 60;
        long newMonthTimeHour = newMonthTimeMin / 60;
        long newMonthTimeMinRes = newMonthTimeMin % 60;

        String newMonthTimeMinResStr = Long.toString(newMonthTimeMinRes);
        if (newMonthTimeMinResStr.length() == 1) {
            newMonthTimeMinResStr = "0" + newMonthTimeMinResStr;
        }
        String newMonthTime = newMonthTimeHour + ":" + newMonthTimeMinResStr;

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
            timeExtraMonthMs = newMonthTimeMs - (timeContract.getTime() + 3600000);
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
            salaryContractHours = newMonthTimeHour * salaryContractHour;
            salaryContractMins = ((double) newMonthTimeMinRes) / 60 * salaryContractHour;
        }
        else {
            salaryContractHours = contractHours * salaryContractHour;
            salaryContractMins = contractMins / 60 * salaryContractHour;
            salaryExtraHours = timeExtraMonthHour * salaryExtraHour;
            salaryExtraMins = timeExtraMonthMinRest / 60 * salaryExtraHour;
        }

        double newSalaryTotal = salaryContractHours + salaryContractMins + salaryExtraHours +
                salaryExtraMins;

        DecimalFormat threeDecFormat = new DecimalFormat("#.##");
        String salaryTotalStr = threeDecFormat.format(newSalaryTotal).replaceAll(",",".");
        newSalaryTotal = Double.parseDouble(salaryTotalStr);

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

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date oldDayTimeTotal = null;
        Date oldDayTimeGoal = null;
        Date oldDayTimeExtra = null;
        Date walkTimeTotal = null;
        Date walkTimeGoal = null;
        Date walkTimeExtra = null;

        if (oldDayTimeTotalStr.length() == 4) {
            oldDayTimeTotalStr = "0" + oldDayTimeTotalStr;
        }
        try {
            oldDayTimeTotal = format.parse(oldDayTimeTotalStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (oldDayTimeGoalStr.length() == 4) {
            oldDayTimeGoalStr = "0" + oldDayTimeGoalStr;
        }
        try {
            oldDayTimeGoal = format.parse(oldDayTimeGoalStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        boolean oldDayTimeNeg = false;
        if (oldDayTimeExtraStr.charAt(0) == '-') {
            oldDayTimeExtraStr = oldDayTimeExtraStr.substring(1);
            oldDayTimeNeg = true;
        }
        if (oldDayTimeExtraStr.length() == 4) {
            oldDayTimeExtraStr = "0" + oldDayTimeExtraStr;
        }
        try {
            oldDayTimeExtra = format.parse(oldDayTimeExtraStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (walkTimeTotalStr.length() == 4) {
            walkTimeTotalStr = "0" + walkTimeTotalStr;
        }
        try {
            walkTimeTotal = format.parse(walkTimeTotalStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (walkTimeGoalStr.length() == 4) {
            walkTimeGoalStr = "0" + walkTimeGoalStr;
        }
        try {
            walkTimeGoal = format.parse(walkTimeGoalStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        boolean walkTimeNeg = false;
        if (walkTimeExtraStr.charAt(0) == '-') {
            walkTimeExtraStr = walkTimeExtraStr.substring(1);
            walkTimeNeg = true;
        }
        if (walkTimeExtraStr.length() == 4) {
            walkTimeExtraStr = "0" + walkTimeExtraStr;
        }
        try {
            walkTimeExtra = format.parse(walkTimeExtraStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long newDayTimeTotalMs = 0;
        long newDayTimeGoalMs = 0;
        long newDayTimeExtraMs = 0;

        try {
            newDayTimeTotalMs = oldDayTimeTotal.getTime() + 3600000 - (walkTimeTotal.getTime()
                              + 3600000);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }
        try {
            newDayTimeGoalMs = oldDayTimeGoal.getTime() + 3600000 - (walkTimeGoal.getTime()
                    + 3600000);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        if (!oldDayTimeNeg && !walkTimeNeg) {
            try {
                newDayTimeExtraMs = oldDayTimeExtra.getTime() + 3600000 - (walkTimeExtra.getTime()
                        + 3600000);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else if (!oldDayTimeNeg && walkTimeNeg) {
            try {
                newDayTimeExtraMs = oldDayTimeExtra.getTime() + 3600000 + walkTimeExtra.getTime()
                        + 3600000;
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else if (oldDayTimeNeg && !walkTimeNeg) {
            try {
                newDayTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + 3600000)
                                    - (walkTimeExtra.getTime() + 3600000);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                newDayTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + 3600000)
                                    + (walkTimeExtra.getTime() + 3600000);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }

        long newDayTimeTotalMin = newDayTimeTotalMs / 1000 / 60;
        long newDayTimeTotalHour = newDayTimeTotalMin / 60;
        long newDayTimeTotalMinRes = newDayTimeTotalMin % 60;
        String newDayTimeTotalMinResStr = Long.toString(newDayTimeTotalMinRes);
        if (newDayTimeTotalMinResStr.length() == 1) {
            newDayTimeTotalMinResStr = "0" + newDayTimeTotalMinResStr;
        }
        String newDayTimeTotal = newDayTimeTotalHour + ":" + newDayTimeTotalMinResStr;

        long newDayTimeGoalMin = newDayTimeGoalMs / 1000 / 60;
        long newDayTimeGoalHour = newDayTimeGoalMin / 60;
        long newDayTimeGoalMinRes = newDayTimeGoalMin % 60;
        String newDayTimeGoalMinResStr = Long.toString(newDayTimeGoalMinRes);
        if (newDayTimeGoalMinResStr.length() == 1) {
            newDayTimeGoalMinResStr = "0" + newDayTimeGoalMinResStr;
        }
        String newDayTimeGoal = newDayTimeGoalHour + ":" + newDayTimeGoalMinResStr;

        long newDayTimeExtraMin = newDayTimeExtraMs / 1000 / 60;
        long newDayTimeExtraHour = newDayTimeExtraMin / 60;
        long newDayTimeExtraMinRes = Math.abs(newDayTimeExtraMin % 60);
        String newDayTimeExtraMinResStr = Long.toString(newDayTimeExtraMinRes);
        if (newDayTimeExtraMinResStr.length() == 1) {
            newDayTimeExtraMinResStr = "0" + newDayTimeExtraMinResStr;
        }
        String newDayTimeExtra = newDayTimeExtraHour + ":" + newDayTimeExtraMinResStr;
        if (newDayTimeExtraMin < 0 && newDayTimeExtraHour == 0) {
            newDayTimeExtra = "-" + newDayTimeExtra;
        }

        db = getWritableDatabase();
        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_DISTRICTS, districts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, newDayTimeTotal);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, newDayTimeGoal);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, newDayTimeExtra);
        db.update(DAYS_TABLE_NAME, dayValues, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY + " = ?",
                  new String[]{Integer.toString(idMonth), Integer.toString(idDay)});

        Date oldMonthTime = null;

        if (oldMonthTimeStr.length() == 4) {
            oldMonthTimeStr = "0" + oldMonthTimeStr;
        }
        try {
            oldMonthTime = format.parse(oldMonthTimeStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long newMonthTimeMs = 0;
        try {
            newMonthTimeMs = oldMonthTime.getTime() + 3600000 - (oldDayTimeTotal.getTime() + 3600000)
                             + newDayTimeTotalMs;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        long newMonthTimeMin = newMonthTimeMs / 1000 / 60;
        long newMonthTimeHour = newMonthTimeMin / 60;
        long newMonthTimeMinRes = newMonthTimeMin % 60;
        String newMonthTimeMinResStr = Long.toString(newMonthTimeMinRes);
        if (newMonthTimeMinResStr.length() == 1) {
            newMonthTimeMinResStr = "0" + newMonthTimeMinResStr;
        }
        String newMonthTime = newMonthTimeHour + ":" + newMonthTimeMinResStr;

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
            timeExtraMonthMs = newMonthTimeMs - (timeContract.getTime() + 3600000);
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
            salaryContractHours = newMonthTimeHour * salaryContractHour;
            salaryContractMins = ((double) newMonthTimeMinRes) / 60 * salaryContractHour;
        }
        else {
            salaryContractHours = contractHours * salaryContractHour;
            salaryContractMins = contractMins / 60 * salaryContractHour;
            salaryExtraHours = timeExtraMonthHour * salaryExtraHour;
            salaryExtraMins = timeExtraMonthMinRest / 60 * salaryExtraHour;
        }

        double newSalaryTotal = salaryContractHours + salaryContractMins + salaryExtraHours +
                salaryExtraMins;

        DecimalFormat threeDecFormat = new DecimalFormat("#.##");
        String salaryTotalStr = threeDecFormat.format(newSalaryTotal).replaceAll(",",".");
        newSalaryTotal = Double.parseDouble(salaryTotalStr);

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

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date oldDayTimeTotal = null;
        Date oldWalkTimeTotal = null;
        Date newWalkTimeTotal = null;

        if (oldDayObj.timeTotal.length() == 4) {
            oldDayObj.timeTotal = "0" + oldDayObj.timeTotal;
        }
        try {
            oldDayTimeTotal = format.parse(oldDayObj.timeTotal);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (oldWalkObj.timeTotal.length() == 4) {
            oldWalkObj.timeTotal = "0" + oldWalkObj.timeTotal;
        }
        try {
            oldWalkTimeTotal = format.parse(oldWalkObj.timeTotal);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (walkObject.timeTotal.length() == 4) {
            walkObject.timeTotal = "0" + walkObject.timeTotal;
        }
        try {
            newWalkTimeTotal = format.parse(walkObject.timeTotal);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long newTimeTotalMs = 0;
        try {
            newTimeTotalMs = oldDayTimeTotal.getTime() + 3600000 - (oldWalkTimeTotal.getTime() +
                             3600000) + newWalkTimeTotal.getTime() + 3600000;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        long newTimeTotalMin = newTimeTotalMs / 1000 / 60;
        long newTimeTotalHour = newTimeTotalMin / 60;
        long newTimeTotalMinRest = newTimeTotalMin % 60;

        String newTimeTotalMinRestStr = Long.toString(newTimeTotalMinRest);
        if (newTimeTotalMinRestStr.length() == 1) {
            newTimeTotalMinRestStr = "0" + newTimeTotalMinRestStr;
        }
        String newDayTimeTotal = newTimeTotalHour + ":" + newTimeTotalMinRestStr;

        Date oldDayTimeGoal = null;
        Date oldWalkTimeGoal = null;
        Date newWalkTimeGoal = null;

        if (oldDayObj.timeGoal.length() == 4) {
            oldDayObj.timeGoal = "0" + oldDayObj.timeGoal;
        }
        try {
            oldDayTimeGoal = format.parse(oldDayObj.timeGoal);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (oldWalkObj.timeGoal.length() == 4) {
            oldWalkObj.timeGoal = "0" + oldWalkObj.timeGoal;
        }
        try {
            oldWalkTimeGoal = format.parse(oldWalkObj.timeGoal);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if (walkObject.timeGoal.length() == 4) {
            walkObject.timeGoal = "0" + walkObject.timeGoal;
        }
        try {
            newWalkTimeGoal = format.parse(walkObject.timeGoal);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long newTimeGoalMs = 0;
        try {
            newTimeGoalMs = oldDayTimeGoal.getTime() + 3600000 - (oldWalkTimeGoal.getTime() +
                            3600000) + newWalkTimeGoal.getTime() + 3600000;
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        long newTimeGoalMin = newTimeGoalMs / 1000 / 60;
        long newTimeGoalHour = newTimeGoalMin / 60;
        long newTimeGoalMinRest = newTimeGoalMin % 60;

        String newTimeGoalMinRestStr = Long.toString(newTimeGoalMinRest);
        if (newTimeGoalMinRestStr.length() == 1) {
            newTimeGoalMinRestStr = "0" + newTimeGoalMinRestStr;
        }
        String newDayTimeGoal = newTimeGoalHour + ":" + newTimeGoalMinRestStr;

        Date oldDayTimeExtra = null;
        Date oldWalkTimeExtra = null;
        Date newWalkTimeExtra = null;

        boolean oldDayTimeNeg = false;
        if (oldDayObj.timeExtra.charAt(0) == '-') {
            oldDayObj.timeExtra = oldDayObj.timeExtra.substring(1);
            oldDayTimeNeg = true;
        }
        if (oldDayObj.timeExtra.length() == 4) {
            oldDayObj.timeExtra = "0" + oldDayObj.timeExtra;
        }
        try {
            oldDayTimeExtra = format.parse(oldDayObj.timeExtra);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        boolean oldWalkTimeNeg = false;
        if (oldWalkObj.timeExtra.charAt(0) == '-') {
            oldWalkObj.timeExtra = oldWalkObj.timeExtra.substring(1);
            oldWalkTimeNeg = true;
        }
        if (oldWalkObj.timeExtra.length() == 4) {
            oldWalkObj.timeExtra = "0" + oldWalkObj.timeExtra;
        }
        try {
            oldWalkTimeExtra = format.parse(oldWalkObj.timeExtra);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        boolean newWalkTimeNeg = false;
        if (walkObject.timeExtra.charAt(0) == '-') {
            walkObject.timeExtra = walkObject.timeExtra.substring(1);
            newWalkTimeNeg = true;
        }
        if (walkObject.timeExtra.length() == 4) {
            walkObject.timeExtra = "0" + walkObject.timeExtra;
        }
        try {
            newWalkTimeExtra = format.parse(walkObject.timeExtra);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long newTimeExtraMs = 0;
        if (!oldDayTimeNeg && !oldWalkTimeNeg && !newWalkTimeNeg) {
            newTimeExtraMs = oldDayTimeExtra.getTime() + 3600000 - (oldWalkTimeExtra.getTime() +
                             3600000) + newWalkTimeExtra.getTime() + 3600000;
        }
        else if (!oldDayTimeNeg && !oldWalkTimeNeg && newWalkTimeNeg) {
            newTimeExtraMs = oldDayTimeExtra.getTime() + 3600000 - (oldWalkTimeExtra.getTime() +
                             3600000) - (newWalkTimeExtra.getTime() + 3600000);
        }
        else if (!oldDayTimeNeg && oldWalkTimeNeg && !newWalkTimeNeg) {
            newTimeExtraMs = oldDayTimeExtra.getTime() + 3600000 + oldWalkTimeExtra.getTime() +
                             3600000 + newWalkTimeExtra.getTime() + 3600000;
        }
        else if (!oldDayTimeNeg && oldWalkTimeNeg && newWalkTimeNeg) {
            newTimeExtraMs = oldDayTimeExtra.getTime() + 3600000 + oldWalkTimeExtra.getTime() +
                             3600000 - (newWalkTimeExtra.getTime() + 3600000);
        }
        else if (oldDayTimeNeg && !oldWalkTimeNeg && !newWalkTimeNeg) {
            newTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + 3600000) - (oldWalkTimeExtra.getTime()
                             + 3600000) + newWalkTimeExtra.getTime() + 3600000;
        }
        else if (oldDayTimeNeg && !oldWalkTimeNeg && newWalkTimeNeg) {
            newTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + 3600000) - (oldWalkTimeExtra.getTime()
                             + 3600000) - (newWalkTimeExtra.getTime() + 3600000);
        }
        else if (oldDayTimeNeg && oldWalkTimeNeg && !newWalkTimeNeg) {
            newTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + 3600000) + oldWalkTimeExtra.getTime()
                             + 3600000 + newWalkTimeExtra.getTime() + 3600000;
        }
        else {
            newTimeExtraMs = -1 * (oldDayTimeExtra.getTime() + 3600000) + oldWalkTimeExtra.getTime()
                             + 3600000 - (newWalkTimeExtra.getTime() + 3600000);
        }

        long newTimeExtraMin = newTimeExtraMs / 1000 / 60;
        long newTimeExtraHour = Math.abs(newTimeExtraMin / 60);
        long newTimeExtraMinRes = newTimeExtraMin % 60;

        String newTimeExtraMinResStr = Long.toString(newTimeExtraMinRes);
        if (newTimeExtraMinResStr.length() == 1) {
            newTimeExtraMinResStr = "0" + newTimeExtraMinResStr;
        }
        String newDayTimeExtra = newTimeExtraHour + ":" + newTimeExtraMinResStr;

        if (newTimeExtraMin < 0 && newTimeExtraHour == 0) {
            newDayTimeExtra = "-" + newDayTimeExtra;
        }

        ContentValues dayValues = new ContentValues();
        dayValues.put(DAYS_COLUMN_DISTRICTS, newDistricts);
        dayValues.put(DAYS_COLUMN_TIMETOTAL, newDayTimeTotal);
        dayValues.put(DAYS_COLUMN_TIMEGOAL, newDayTimeGoal);
        dayValues.put(DAYS_COLUMN_TIMEEXTRA, newDayTimeExtra);
        db.update(DAYS_TABLE_NAME, dayValues, DAYS_COLUMN_IDMONTH + " = ? AND " + DAYS_COLUMN_IDDAY
                  + " = ?", new String[]{Integer.toString(walkObject.id1),
                  Integer.toString(walkObject.id2)});

        if (oldMonthObj.time.length() == 4) {
            oldMonthObj.time = "0" + oldMonthObj.time;
        }

        Date oldMonthTime = null;
        try {
            oldMonthTime = format.parse(oldMonthObj.time);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long newMonthTimeMs = oldMonthTime.getTime() + 3600000 - (oldDayTimeTotal.getTime() + 3600000)
                              + newTimeTotalMs;

        long newMonthTimeMin = newMonthTimeMs / 1000 / 60;
        long newMonthTimeHour = newMonthTimeMin / 60;
        long newMonthTimeMinRes = newMonthTimeMin % 60;

        String newMonthTimeMinResStr = Long.toString(newMonthTimeMinRes);
        if (newMonthTimeMinResStr.length() == 1) {
            newMonthTimeMinResStr = "0" + newMonthTimeMinResStr;
        }
        String newMonthTime = newMonthTimeHour + ":" + newMonthTimeMinResStr;

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
            timeExtraMonthMs = newMonthTimeMs - (timeContract.getTime() + 3600000);
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
            salaryContractHours = newMonthTimeHour * salaryContractHour;
            salaryContractMins = ((double) newMonthTimeMinRes) / 60 * salaryContractHour;
        }
        else {
            salaryContractHours = contractHours * salaryContractHour;
            salaryContractMins = contractMins / 60 * salaryContractHour;
            salaryExtraHours = timeExtraMonthHour * salaryExtraHour;
            salaryExtraMins = timeExtraMonthMinRest / 60 * salaryExtraHour;
        }

        double newSalaryTotal = salaryContractHours + salaryContractMins + salaryExtraHours +
                salaryExtraMins;

        DecimalFormat threeDecFormat = new DecimalFormat("#.##");
        String salaryTotalStr = threeDecFormat.format(newSalaryTotal).replaceAll(",",".");
        newSalaryTotal = Double.parseDouble(salaryTotalStr);

        ContentValues monthValues = new ContentValues();
        monthValues.put(MONTHS_COLUMN_SALARY, newSalaryTotal);
        monthValues.put(MONTHS_COLUMN_TIME, newMonthTime);
        db.update(MONTHS_TABLE_NAME, monthValues, MONTHS_COLUMN_ID + " = ?",
                new String[]{Integer.toString(walkObject.id1)});
        db.close();
    }
}
