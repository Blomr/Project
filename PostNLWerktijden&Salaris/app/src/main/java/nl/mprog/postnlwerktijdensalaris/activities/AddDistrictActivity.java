/**
 * AddDistrictActivity.java
 *
 * In this activity the user is able to add a district to
 * the database. After adding the district is selectable in AddWalkActivity
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import nl.mprog.postnlwerktijdensalaris.databasehandler.DatabaseHandler;
import nl.mprog.postnlwerktijdensalaris.modelclasses.District;
import nl.mprog.postnlwerktijdensalaris.R;

public class AddDistrictActivity extends AppCompatActivity {

    EditText editDistrictCode;
    EditText editBusyHour;
    EditText editBusyMin;
    EditText editCalmHour;
    EditText editCalmMin;
    String districtCode;
    String busyHour;
    String busyMin;
    String calmHour;
    String calmMin;
    int id;

    /**
     * Sets layout and texts in edittexts if id is not 0.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_district);

        // initialize edittexts
        editDistrictCode = (EditText) findViewById(R.id.editDistrictCode);
        editBusyHour = (EditText) findViewById(R.id.editBusyHour);
        editBusyMin = (EditText) findViewById(R.id.editBusyMin);
        editCalmHour = (EditText) findViewById(R.id.editCalmHour);
        editCalmMin = (EditText) findViewById(R.id.editCalmMin);

        // get id from intent
        id = getIntent().getIntExtra("id", 0);

        // if item was clicked, set object values in edittexts
        if (id != 0) {
            DatabaseHandler dbHandler = new DatabaseHandler(this);
            District districtObj = dbHandler.getDistrict(id);
            System.out.println(districtObj.timeGoalBusy);
            System.out.println(districtObj.timeGoalCalm);
            System.out.println(districtObj.districtCode);

            busyHour = districtObj.timeGoalBusy.substring(0, districtObj.timeGoalBusy.indexOf(":"));
            busyMin = districtObj.timeGoalBusy.substring(districtObj.timeGoalBusy.indexOf(":") + 1);
            calmHour = districtObj.timeGoalCalm.substring(0, districtObj.timeGoalCalm.indexOf(":"));
            calmMin = districtObj.timeGoalCalm.substring(districtObj.timeGoalCalm.indexOf(":") + 1);

            editDistrictCode.setText(districtObj.districtCode);
            editBusyHour.setText(busyHour);
            editBusyMin.setText(busyMin);
            editCalmHour.setText(calmHour);
            editCalmMin.setText(calmMin);
        }
    }

    /**
     * Handles clicks on save button.
     */
    public void onClickSave(View view) {

        // get text from edittexts
        districtCode = editDistrictCode.getText().toString();
        busyHour = editBusyHour.getText().toString();
        busyMin = editBusyMin.getText().toString();
        calmHour = editCalmHour.getText().toString();
        calmMin = editCalmMin.getText().toString();

        // if one of edittexts is empty, make string variable equal to 0 or 00
        if (busyHour.equals("") && busyMin.equals("")) {
            busyHour = "0";
            busyMin = "00";
        }
        if (calmHour.equals("") && calmMin.equals("")) {
            calmHour = "0";
            calmMin = "00";
        }

        // check for correct input
        if (busyHour.equals("0") && busyMin.equals("00") && calmHour.equals("0")
                && calmMin.equals("00")) {
            Toast.makeText(AddDistrictActivity.this, R.string.invalidBusyCalmDay,
                    Toast.LENGTH_SHORT).show();
        }
        else if (districtCode.equals("")) {
            Toast.makeText(AddDistrictActivity.this, R.string.invalidDistrictCode, Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(busyHour) < 0 || Integer.parseInt(calmHour) < 0) {
            Toast.makeText(AddDistrictActivity.this, R.string.invalidHours, Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(busyMin) < 0 || Integer.parseInt(calmMin) < 0
                || Integer.parseInt(busyMin) >= 60 || Integer.parseInt(calmMin) >= 60) {
            Toast.makeText(AddDistrictActivity.this, R.string.invalidMinutes, Toast.LENGTH_SHORT).show();
        }
        else if (busyMin.length() != 2 || calmMin.length() != 2) {
            Toast.makeText(AddDistrictActivity.this, R.string.invalidMinutes, Toast.LENGTH_SHORT).show();
        }

        // if input is correct, make district object and save in database
        else {
            String timeGoalBusy = busyHour + ":" + busyMin;
            String timeGoalCalm = calmHour + ":" + calmMin;

            District districtObj = new District(id, districtCode, timeGoalBusy, timeGoalCalm);
            DatabaseHandler dbHandler = new DatabaseHandler(this);
            if (id == 0) {
                dbHandler.addDistrict(districtObj);
            }
            else {
                dbHandler.editDistrict(districtObj);
            }

            // go to DistrictsActivity
            Intent goToDistricts = new Intent(this, DistrictsActivity.class);
            startActivity(goToDistricts);
            finish();
        }
    }

    /**
     * Handles clicks on cancel button. Goes to DistrictsActivity.
     */
    public void onClickCancel(View view) {
        Intent goToDistricts = new Intent(this, DistrictsActivity.class);
        startActivity(goToDistricts);
        finish();
    }

    /**
     * Goes to DistrictsActivity.
     */
    public void onClickBack(View view) {
        onBackPressed();
    }
}
