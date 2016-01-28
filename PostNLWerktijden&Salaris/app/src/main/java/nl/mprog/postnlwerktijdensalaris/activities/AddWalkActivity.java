/**
 * AddWalkActivity.java
 *
 * In this activity the user is able to add a walk to
 * the database. After adding the walk, content of the corresponding
 * day and month will also change.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import nl.mprog.postnlwerktijdensalaris.databasehandler.DatabaseHandler;
import nl.mprog.postnlwerktijdensalaris.modelclasses.District;
import nl.mprog.postnlwerktijdensalaris.R;
import nl.mprog.postnlwerktijdensalaris.modelclasses.Walk;

public class AddWalkActivity extends AppCompatActivity{

    int idMonth;
    int idDay;
    int idWalk;
    String beginHour1;
    String beginMin1;
    String endHour1;
    String endMin1;
    String beginHour2;
    String beginMin2;
    String endHour2;
    String endMin2;
    String beginHour3;
    String beginMin3;
    String endHour3;
    String endMin3;
    String savedDistrict;
    String savedDayType;
    final String busyDay = "Piekdag";
    final String calmDay = "Daldag";
    EditText editBeginHour1;
    EditText editBeginMin1;
    EditText editEndHour1;
    EditText editEndMin1;
    EditText editBeginHour2;
    EditText editBeginMin2;
    EditText editEndHour2;
    EditText editEndMin2;
    EditText editBeginHour3;
    EditText editBeginMin3;
    EditText editEndHour3;
    EditText editEndMin3;
    TextView timeGoalView;
    Spinner spinnerDistrict;
    Spinner spinnerDayType;
    ArrayList<String> districtCodes;
    ArrayAdapter<String> adapterDistrict;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;

    /**
     * Sets layout and listeners, initialize variables and if exist loads shared preferences
     * into edittexts. Sets content of selected item in layout, if id is not 0.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_walk);

        // get ids and boolean from intent
        idMonth = getIntent().getIntExtra("idMonth", 0);
        idDay = getIntent().getIntExtra("idDay", 0);
        idWalk = getIntent().getIntExtra("idWalk", 0);
        boolean fromSettings = getIntent().getBooleanExtra("fromSettings", false);

        // initialize edittexts
        editBeginHour1 = (EditText) findViewById(R.id.editBeginHour1);
        editBeginMin1 = (EditText) findViewById(R.id.editBeginMin1);
        editEndHour1 = (EditText) findViewById(R.id.editEndHour1);
        editEndMin1 = (EditText) findViewById(R.id.editEndMin1);
        editBeginHour2 = (EditText) findViewById(R.id.editBeginHour2);
        editBeginMin2 = (EditText) findViewById(R.id.editBeginMin2);
        editEndHour2 = (EditText) findViewById(R.id.editEndHour2);
        editEndMin2 = (EditText) findViewById(R.id.editEndMin2);
        editBeginHour3 = (EditText) findViewById(R.id.editBeginHour3);
        editBeginMin3 = (EditText) findViewById(R.id.editBeginMin3);
        editEndHour3 = (EditText) findViewById(R.id.editEndHour3);
        editEndMin3 = (EditText) findViewById(R.id.editEndMin3);

        // if user went to settings, load latest changes from shared preferences
        if (fromSettings) {
            prefs = getPreferences(MODE_PRIVATE);
            idMonth = prefs.getInt("idMonth", 0);
            idDay = prefs.getInt("idDay", 0);
            idWalk = prefs.getInt("idWalk", 0);
            editBeginHour1.setText(prefs.getString("beginHour1", ""));
            editBeginMin1.setText(prefs.getString("beginMin1", ""));
            editEndHour1.setText(prefs.getString("endHour1", ""));
            editEndMin1.setText(prefs.getString("endMin1", ""));
            editBeginHour2.setText(prefs.getString("beginHour2", ""));
            editBeginMin2.setText(prefs.getString("beginMin2", ""));
            editEndHour2.setText(prefs.getString("endHour2", ""));
            editEndMin2.setText(prefs.getString("endMin2", ""));
            editBeginHour3.setText(prefs.getString("beginHour3", ""));
            editBeginMin3.setText(prefs.getString("beginMin3", ""));
            editEndHour3.setText(prefs.getString("endHour3", ""));
            editEndMin3.setText(prefs.getString("endMin3", ""));
            savedDistrict = prefs.getString("spinnerDistrict", "");
            savedDayType = prefs.getString("spinnerDayType", "");
        }

        timeGoalView = (TextView) findViewById(R.id.timeGoal);
        TextView titleView = (TextView) findViewById(R.id.addWalkTitle);

        // get all district codes from database
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        final ArrayList<District> districtObjects = dbHandler.getDistricts();
        districtCodes = new ArrayList<>();
        for (District districtObj : districtObjects) {
            districtCodes.add(districtObj.districtCode);
        }

        // adapt district codes into spinner
        spinnerDistrict = (Spinner) findViewById(R.id.spinnerDistrict);
        adapterDistrict = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                          districtCodes);
        spinnerDistrict.setAdapter(adapterDistrict);

        // if user went to settings, load latest selection into district code spinner
        if (fromSettings) {
            for (int i = 0; i < districtCodes.size(); i++) {
                if (districtCodes.get(i).equals(savedDistrict)) {
                    spinnerDistrict.setSelection(i);
                    break;
                }
            }
        }

        // get day types of current selected item and adapt into second spinner
        spinnerDayType = (Spinner) findViewById(R.id.spinnerDayType);
        String districtCode = spinnerDistrict.getSelectedItem().toString();
        District currentObj = new District(0, "", "", "");
        final ArrayList<String> dayTypes = new ArrayList<>();
        for (District districtObj : districtObjects) {
            if (districtObj.districtCode.equals(districtCode)) {
                currentObj = districtObj;
                if (!districtObj.timeGoalBusy.equals("0:00")) {
                    dayTypes.add(busyDay);
                }
                if (!districtObj.timeGoalCalm.equals("0:00")) {
                    dayTypes.add(calmDay);
                }
                break;
            }
        }
        final ArrayAdapter<String> adapterDayType = new ArrayAdapter<>(this,
                                                    android.R.layout.simple_dropdown_item_1line,
                                                    dayTypes);
        spinnerDayType.setAdapter(adapterDayType);

        // if user went to settings, load latest selection into day type spinner
        if (fromSettings) {
            for (int i = 0; i < dayTypes.size(); i++) {
                if (dayTypes.get(i).equals(savedDayType)) {
                    spinnerDayType.setSelection(i);
                    break;
                }
            }
        }

        // get time goal of corresponding district code and day type
        String dayType = spinnerDayType.getSelectedItem().toString();
        if (dayType.equals(busyDay)) {
            timeGoalView.setText(currentObj.timeGoalBusy);
        }
        else {
            timeGoalView.setText(currentObj.timeGoalCalm);
        }

        // set listener on item select changes of district spinner
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = spinnerDistrict.getItemAtPosition(position).toString();

                // search for district code that is equal to the clicked item
                for (District districtObj : districtObjects) {
                    if (districtObj.districtCode.equals(clickedItem)) {

                        // if time is not equal to 0:00, add day type to spinner if it isn't in there
                        if (!districtObj.timeGoalBusy.equals("0:00")) {
                            if (!dayTypes.contains(busyDay)) {
                                dayTypes.add(busyDay);
                            }
                        }
                        // else remove it, if it is in the spinner
                        else {
                            if (dayTypes.contains(busyDay)) {
                                dayTypes.remove(busyDay);
                            }
                        }

                        if (!districtObj.timeGoalCalm.equals("0:00")) {
                            if (!dayTypes.contains(calmDay)) {
                                dayTypes.add(calmDay);
                            }
                        } else {
                            if (dayTypes.contains(calmDay)) {
                                dayTypes.remove(calmDay);
                            }
                        }
                        adapterDayType.notifyDataSetChanged();

                        // get the day type and set the new corresponding time goal
                        String dayType;
                        try {
                            dayType = spinnerDayType.getSelectedItem().toString();
                        } catch (java.lang.IndexOutOfBoundsException e) {
                            dayType = spinnerDayType.getItemAtPosition(0).toString();
                        }
                        if (dayType.equals(busyDay)) {
                            timeGoalView.setText(districtObj.timeGoalBusy);
                        } else {
                            timeGoalView.setText(districtObj.timeGoalCalm);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // set listener on item select changes of day type spinner
        spinnerDayType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = spinnerDayType.getItemAtPosition(position).toString();
                String districtCode = spinnerDistrict.getSelectedItem().toString();

                // search for district code that is equal to the selected item of district spinner
                for (District districtObj : districtObjects) {
                    if (districtObj.districtCode.equals(districtCode)) {

                        // set the right time goal that's corresponding with the clicked item
                        if (clickedItem.equals(busyDay)) {
                            timeGoalView.setText(districtObj.timeGoalBusy);
                        } else {
                            timeGoalView.setText(districtObj.timeGoalCalm);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // if id from intent is not 0, get walk object from database and set into edittexts and set title
        if (idWalk != 0) {
            titleView.setText(R.string.editWalk);

            dbHandler = new DatabaseHandler(this);
            Walk walkObj = dbHandler.getWalk(idMonth, idDay, idWalk);

            beginHour1 = walkObj.timeBegin1.substring(0, 2);
            editBeginHour1.setText(beginHour1);

            beginMin1 = walkObj.timeBegin1.substring(3);
            editBeginMin1.setText(beginMin1);

            endHour1 = walkObj.timeEnd1.substring(0, 2);
            editEndHour1.setText(endHour1);

            endMin1 = walkObj.timeEnd1.substring(3);
            editEndMin1.setText(endMin1);

            beginHour2 = walkObj.timeBegin2.substring(0, 2);
            editBeginHour2.setText(beginHour2);

            beginMin2 = walkObj.timeBegin2.substring(3);
            editBeginMin2.setText(beginMin2);

            endHour2 = walkObj.timeEnd2.substring(0, 2);
            editEndHour2.setText(endHour2);

            endMin2 = walkObj.timeEnd2.substring(3);
            editEndMin2.setText(endMin2);

            beginHour3 = walkObj.timeBegin3.substring(0, 2);
            editBeginHour3.setText(beginHour3);

            beginMin3 = walkObj.timeBegin3.substring(3);
            editBeginMin3.setText(beginMin3);

            endHour3 = walkObj.timeEnd3.substring(0, 2);
            editEndHour3.setText(endHour3);

            endMin3 = walkObj.timeEnd3.substring(3);
            editEndMin3.setText(endMin3);

            // set spinners on right selected items
            int districtPos = adapterDistrict.getPosition(walkObj.districtCode);
            spinnerDistrict.setSelection(districtPos);

            int dayTypePos = adapterDayType.getPosition(walkObj.dayType);
            spinnerDayType.setSelection(dayTypePos);
        }

        // if id is 0, set other title
        else {
            titleView.setText(R.string.addWalk);
        }
    }

    /**
     * Handles clicks on settings button.
     */
    public void onClickSettings(View view) {

        // put editTexts into shared preferences
        prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt("idMonth", idMonth);
        prefsEditor.putInt("idDay", idDay);
        prefsEditor.putInt("idWalk", idWalk);
        prefsEditor.putString("beginHour1", editBeginHour1.getText().toString());
        prefsEditor.putString("beginMin1", editBeginMin1.getText().toString());
        prefsEditor.putString("endHour1", editEndHour1.getText().toString());
        prefsEditor.putString("endMin1", editEndMin1.getText().toString());
        prefsEditor.putString("beginHour2", editBeginHour2.getText().toString());
        prefsEditor.putString("beginMin2", editBeginMin2.getText().toString());
        prefsEditor.putString("endHour2", editEndHour2.getText().toString());
        prefsEditor.putString("endMin2", editEndMin2.getText().toString());
        prefsEditor.putString("beginHour3", editBeginHour3.getText().toString());
        prefsEditor.putString("beginMin3", editBeginMin3.getText().toString());
        prefsEditor.putString("endHour3", editEndHour3.getText().toString());
        prefsEditor.putString("endMin3", editEndMin3.getText().toString());
        prefsEditor.putString("spinnerDistrict", spinnerDistrict.getSelectedItem().toString());
        prefsEditor.putString("spinnerDayType", spinnerDayType.getSelectedItem().toString());
        prefsEditor.apply();

        // put AddWalk as previous activity and ids in intent
        Intent goToSettings = new Intent(AddWalkActivity.this, SettingsActivity.class);
        goToSettings.putExtra("prevActivity", "AddWalk");
        goToSettings.putExtra("idMonth", idMonth);
        goToSettings.putExtra("idDay", idDay);
        goToSettings.putExtra("idWalk", idWalk);
        startActivity(goToSettings);
        finish();
    }

    /**
     * Handles clicks on save button.
     */
    public void onClickSave(View view) {

        // if editText is empty, set default value of zero
        if (editBeginHour1.getText().toString().equals("")) {
            editBeginHour1.setText(R.string.oneZero);
        }
        if (editBeginMin1.getText().toString().equals("")) {
            editBeginMin1.setText(R.string.twoZeroes);
        }
        if (editEndHour1.getText().toString().equals("")) {
            editEndHour1.setText(R.string.oneZero);
        }
        if (editEndMin1.getText().toString().equals("")) {
            editEndMin1.setText(R.string.twoZeroes);
        }
        if (editBeginHour2.getText().toString().equals("")) {
            editBeginHour2.setText(R.string.oneZero);
        }
        if (editBeginMin2.getText().toString().equals("")) {
            editBeginMin2.setText(R.string.twoZeroes);
        }
        if (editEndHour2.getText().toString().equals("")) {
            editEndHour2.setText(R.string.oneZero);
        }
        if (editEndMin2.getText().toString().equals("")) {
            editEndMin2.setText(R.string.twoZeroes);
        }
        if (editBeginHour3.getText().toString().equals("")) {
            editBeginHour3.setText(R.string.oneZero);
        }
        if (editBeginMin3.getText().toString().equals("")) {
            editBeginMin3.setText(R.string.twoZeroes);
        }
        if (editEndHour3.getText().toString().equals("")) {
            editEndHour3.setText(R.string.oneZero);
        }
        if (editEndMin3.getText().toString().equals("")) {
            editEndMin3.setText(R.string.twoZeroes);
        }

        // check for correct input
        if (editBeginHour1.getText().toString().equals("0")
                && editBeginMin1.getText().toString().equals("00")
                && editBeginHour1.getText().toString().equals("0")
                && editBeginMin1.getText().toString().equals("00")) {
            Toast.makeText(AddWalkActivity.this, R.string.invalidFirstLine, Toast.LENGTH_SHORT).show();
        }
        else if (editBeginMin1.getText().toString().length() != 2
                || editEndMin1.getText().toString().length() != 2
                || editBeginMin2.getText().toString().length() != 2
                || editEndMin2.getText().toString().length() != 2
                || editBeginMin3.getText().toString().length() != 2
                || editEndMin3.getText().toString().length() != 2) {
            Toast.makeText(AddWalkActivity.this, R.string.invalidMinutes, Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(editBeginHour1.getText().toString()) >= 24
                || Integer.parseInt(editEndHour1.getText().toString()) >= 24
                || Integer.parseInt(editBeginHour2.getText().toString()) >= 24
                || Integer.parseInt(editEndHour2.getText().toString()) >= 24
                || Integer.parseInt(editBeginHour3.getText().toString()) >= 24
                || Integer.parseInt(editEndHour3.getText().toString()) >= 24) {
            Toast.makeText(AddWalkActivity.this, R.string.invalidHours, Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(editBeginMin1.getText().toString()) >= 60
                || Integer.parseInt(editEndMin1.getText().toString()) >= 60
                || Integer.parseInt(editBeginMin2.getText().toString()) >= 60
                || Integer.parseInt(editEndMin2.getText().toString()) >= 60
                || Integer.parseInt(editBeginMin3.getText().toString()) >= 60
                || Integer.parseInt(editEndMin3.getText().toString()) >= 60) {
            Toast.makeText(AddWalkActivity.this, R.string.invalidMinutes, Toast.LENGTH_SHORT).show();
        }

        // if input is correct, begin saving to database
        else {

            // get strings of spinners' selected items
            String districtCode = spinnerDistrict.getSelectedItem().toString();
            String dayType = spinnerDayType.getSelectedItem().toString();

            // get strings of editTexts, add 0 if hours is one digit
            beginHour1 = editBeginHour1.getText().toString();
            beginMin1 = editBeginMin1.getText().toString();
            String timeBegin1Str = beginHour1 + ":" + beginMin1;
            if (timeBegin1Str.length() == 4) {
                timeBegin1Str = "0" + timeBegin1Str;
            }

            endHour1 = editEndHour1.getText().toString();
            endMin1 = editEndMin1.getText().toString();
            String timeEnd1Str = endHour1 + ":" + endMin1;
            if (timeEnd1Str.length() == 4) {
                timeEnd1Str = "0" + timeEnd1Str;
            }

            beginHour2 = editBeginHour2.getText().toString();
            beginMin2 = editBeginMin2.getText().toString();
            String timeBegin2Str = beginHour2 + ":" + beginMin2;
            if (timeBegin2Str.length() == 4) {
                timeBegin2Str = "0" + timeBegin2Str;
            }

            endHour2 = editEndHour2.getText().toString();
            endMin2 = editEndMin2.getText().toString();
            String timeEnd2Str = endHour2 + ":" + endMin2;
            if (timeEnd2Str.length() == 4) {
                timeEnd2Str = "0" + timeEnd2Str;
            }

            beginHour3 = editBeginHour3.getText().toString();
            beginMin3 = editBeginMin3.getText().toString();
            String timeBegin3Str = beginHour3 + ":" + beginMin3;
            if (timeBegin3Str.length() == 4) {
                timeBegin3Str = "0" + timeBegin3Str;
            }

            endHour3 = editEndHour3.getText().toString();
            endMin3 = editEndMin3.getText().toString();
            String timeEnd3Str = endHour3 + ":" + endMin3;
            if (timeEnd3Str.length() == 4) {
                timeEnd3Str = "0" + timeEnd3Str;
            }

            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            Date timeBegin1 = new Date();
            Date timeEnd1 = new Date();
            Date timeBegin2 = new Date();
            Date timeEnd2 = new Date();
            Date timeBegin3 = new Date();
            Date timeEnd3 = new Date();

            // parse string into date object
            try {
                timeBegin1 = format.parse(timeBegin1Str);
                timeEnd1 = format.parse(timeEnd1Str);
                timeBegin2 = format.parse(timeBegin2Str);
                timeEnd2 = format.parse(timeEnd2Str);
                timeBegin3 = format.parse(timeBegin3Str);
                timeEnd3 = format.parse(timeEnd3Str);
            } catch (java.text.ParseException e) {
                Toast.makeText(AddWalkActivity.this, R.string.parseError, Toast.LENGTH_SHORT).show();
            }

            // calculate time difference between begin and end
            long timeDifference1 = timeEnd1.getTime() - timeBegin1.getTime();
            long timeDifference2 = timeEnd2.getTime() - timeBegin2.getTime();
            long timeDifference3 = timeEnd3.getTime() - timeBegin3.getTime();

            // add differences together and calculate total hours and minutes
            long timeTotalMs = timeDifference1 + timeDifference2 + timeDifference3;
            long timeTotalHours = timeTotalMs / 1000 / 60 / 60;
            long timeTotalMins = timeTotalMs / 1000 / 60 % 60;

            // get time goal from textView
            String timeGoalStr = timeGoalView.getText().toString();
            if (timeGoalStr.length() == 4) {
                timeGoalStr = "0" + timeGoalStr;
            }

            // make total time string from hours and minutes
            String timeTotalMinsStr = Long.toString(timeTotalMins);
            if (timeTotalMinsStr.length() == 1) {
                timeTotalMinsStr = "0" + timeTotalMinsStr;
            }
            String timeTotalStr = timeTotalHours + ":" + timeTotalMinsStr;

            Date timeGoal = new Date();
            Date timeTotal = new Date();

            try {
                timeGoal = format.parse(timeGoalStr);
                timeTotal = format.parse(timeTotalStr);
            } catch (java.text.ParseException e) {
                Toast.makeText(AddWalkActivity.this, R.string.parseError, Toast.LENGTH_SHORT).show();
            }

            // calculate extra time by subtracting goal time from total time
            long timeExtraMs = timeTotal.getTime() - timeGoal.getTime();

            // make time string from milliseconds
            long timeExtraHours = timeExtraMs / 1000 / 60 / 60;
            long timeExtraMins = Math.abs(timeExtraMs / 1000 / 60 % 60);
            String timeExtraMinsStr = Long.toString(timeExtraMins);
            if (timeExtraMinsStr.length() == 1) {
                timeExtraMinsStr = "0" + timeExtraMinsStr;
            }
            String timeExtraStr = timeExtraHours + ":" + timeExtraMinsStr;

            // remove first zero if exist and place minus if hours is 0 and time is negative
            if (timeGoalStr.charAt(0) == '0') {
                timeGoalStr = timeGoalStr.substring(1);
            }
            if (timeExtraMs < 0 && timeExtraHours == 0) {
                timeExtraStr = "-" + timeExtraStr;
            }

            Walk walkObj = new Walk(idMonth, idDay, idWalk, districtCode, dayType, timeBegin1Str,
                    timeEnd1Str, timeBegin2Str, timeEnd2Str, timeBegin3Str, timeEnd3Str, timeGoalStr,
                    timeExtraStr, timeTotalStr);

            // put shared preferences in bundle
            SharedPreferences sharedPref = getSharedPreferences("contractAndSalary", MODE_PRIVATE);

            Bundle bundle = new Bundle();
            bundle.putInt("contractHours", sharedPref.getInt("contractHours", 0));
            bundle.putInt("contractMins", sharedPref.getInt("contractMins", 0));
            bundle.putInt("salaryEuro", sharedPref.getInt("salaryEuro", 0));
            bundle.putInt("salaryCents", sharedPref.getInt("salaryCents", 0));
            bundle.putInt("extraEuro", sharedPref.getInt("extraEuro", 0));
            bundle.putInt("extraCents", sharedPref.getInt("extraCents", 0));

            // depending on idWalk, add or update row in database
            DatabaseHandler db = new DatabaseHandler(AddWalkActivity.this);
            if (idWalk == 0) {
                db.addWalk(walkObj, bundle);
            }
            else {
                db.editWalk(walkObj, bundle);
            }

            // if editTexts were saved, remove shared preferences
            if (prefsEditor != null) {
                prefsEditor.clear();
                prefsEditor.apply();
            }

            // go to walks
            Intent goToWalks = new Intent(AddWalkActivity.this, WalksActivity.class);
            goToWalks.putExtra("idMonth", idMonth);
            goToWalks.putExtra("idDay", idDay);
            startActivity(goToWalks);
            finish();
        }
    }

    /**
     * Handles clicks on cancel button. Goes to WalksActivity.
     */
    public void onClickCancel(View view) {
        if (prefsEditor != null) {
            prefsEditor.clear();
            prefsEditor.apply();
        }

        // go to WalksActivity
        Intent goToWalks = new Intent(AddWalkActivity.this, WalksActivity.class);
        goToWalks.putExtra("idMonth", idMonth);
        goToWalks.putExtra("idDay", idDay);
        startActivity(goToWalks);
        finish();
    }

    /**
     * Overrides back button. Goes to WalksActivity.
     */
    @Override
    public void onBackPressed() {
        if (prefsEditor != null) {
            prefsEditor.clear();
            prefsEditor.apply();
        }

        Intent goToWalks = new Intent(AddWalkActivity.this, WalksActivity.class);
        goToWalks.putExtra("idMonth", idMonth);
        goToWalks.putExtra("idDay", idDay);
        startActivity(goToWalks);
        finish();
    }

    /**
     * Goes to WalksActivity.
     */
    public void onClickBack(View view) {
        onBackPressed();
    }
}
