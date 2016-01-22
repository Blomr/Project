package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class AddDistrict extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adddistrict);
    }

    public void onClickSave(View view) {
        EditText editDistrictCode = (EditText) findViewById(R.id.editDistrictCode);
        EditText editBusyHour = (EditText) findViewById(R.id.piekdagHour);
        EditText editBusyMin = (EditText) findViewById(R.id.piekdagMin);
        EditText editCalmHour = (EditText) findViewById(R.id.daldagHour);
        EditText editCalmMin = (EditText) findViewById(R.id.daldagMin);

        String districtCode = editDistrictCode.getText().toString();
        String busyHour = editBusyHour.getText().toString();
        String busyMin = editBusyMin.getText().toString();
        String calmHour = editCalmHour.getText().toString();
        String calmMin = editCalmMin.getText().toString();

        if (editBusyHour.getText().toString().equals("") && editBusyMin.getText().toString().equals("")) {
            busyHour = "0";
            busyMin = "0";
        }
        if (editCalmHour.getText().toString().equals("") && editCalmMin.getText().toString().equals("")) {
            calmHour = "0";
            calmMin = "0";
        }

        if (busyMin.length() == 1) {
            busyMin = "0" + busyMin;
        }
        String timeGoalBusy = busyHour + ":" + busyMin;

        if (calmMin.length() == 1) {
            calmMin = "0" + calmMin;
        }
        String timeGoalCalm = calmHour + ":" + calmMin;

        DistrictObject districtObj = new DistrictObject(0, districtCode, timeGoalBusy, timeGoalCalm);
        DatabaseHandler db = new DatabaseHandler(this);
        db.addDistrict(districtObj);

        Intent goToDistricts = new Intent(this, Districts.class);
        startActivity(goToDistricts);
        finish();
    }

    public void onClickCancel(View view) {
        Intent goToDistricts = new Intent(this, Districts.class);
        startActivity(goToDistricts);
        finish();
    }
}
