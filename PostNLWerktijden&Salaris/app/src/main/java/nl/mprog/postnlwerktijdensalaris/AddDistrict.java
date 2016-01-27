/**
 * AddDistrict.java
 *
 * In this activity the user is able to add a district to
 * the database. After adding the district is selectable in AddWalk
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddDistrict extends AppCompatActivity {

    // initialize editTexts
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
     * Sets layout and texts in editTexts if id is not 0.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adddistrict);

        // initialize editTexts
        editDistrictCode = (EditText) findViewById(R.id.editDistrictCode);
        editBusyHour = (EditText) findViewById(R.id.piekdagHour);
        editBusyMin = (EditText) findViewById(R.id.piekdagMin);
        editCalmHour = (EditText) findViewById(R.id.daldagHour);
        editCalmMin = (EditText) findViewById(R.id.daldagMin);

        // get id from intent
        id = getIntent().getIntExtra("id", 0);

        // if item was clicked, set object values in editTexts
        if (id != 0) {
            DatabaseHandler dbHandler = new DatabaseHandler(this);
            DistrictObject districtObj = dbHandler.getDistrict(id);

            busyHour = districtObj.timeGoalBusy.substring(0, -3);
            busyMin = districtObj.timeGoalBusy.substring(-2);
            calmHour = districtObj.timeGoalCalm.substring(0, -3);
            calmMin = districtObj.timeGoalCalm.substring(-2);

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

        // get text from editTexts
        districtCode = editDistrictCode.getText().toString();
        busyHour = editBusyHour.getText().toString();
        busyMin = editBusyMin.getText().toString();
        calmHour = editCalmHour.getText().toString();
        calmMin = editCalmMin.getText().toString();

        // if one of editTexts is empty, make string variable equal to 0 or 00
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
            Toast.makeText(AddDistrict.this, "Ongeldige invoer piekdag en daldag",
                    Toast.LENGTH_SHORT).show();
        }
        else if (districtCode.equals("")) {
            Toast.makeText(AddDistrict.this, "Ongeldige invoer wijkcode", Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(busyHour) < 0 || Integer.parseInt(calmHour) < 0) {
            Toast.makeText(AddDistrict.this, "Ongeldige invoer uren", Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(busyMin) < 0 || Integer.parseInt(calmMin) < 0
                || Integer.parseInt(busyMin) >= 60 || Integer.parseInt(calmMin) >= 60) {
            Toast.makeText(AddDistrict.this, "Ongeldige invoer minuten", Toast.LENGTH_SHORT).show();
        }
        else if (busyMin.length() != 2 || calmMin.length() != 2) {
            Toast.makeText(AddDistrict.this, "Ongeldige invoer minuten", Toast.LENGTH_SHORT).show();
        }

        // if input is correct, make district object and save in database
        else {
            String timeGoalBusy = busyHour + ":" + busyMin;
            String timeGoalCalm = calmHour + ":" + calmMin;

            DistrictObject districtObj = new DistrictObject(id, districtCode, timeGoalBusy, timeGoalCalm);
            DatabaseHandler db = new DatabaseHandler(this);
            if (id == 0) {
                db.addDistrict(districtObj);
            }
            else {
                db.editDistrict(districtObj);
            }

            Intent goToDistricts = new Intent(this, Districts.class);
            startActivity(goToDistricts);
            finish();
        }
    }

    /**
     * Handles clicks on cancel button.
     */
    public void onClickCancel(View view) {
        Intent goToDistricts = new Intent(this, Districts.class);
        startActivity(goToDistricts);
        finish();
    }
}
