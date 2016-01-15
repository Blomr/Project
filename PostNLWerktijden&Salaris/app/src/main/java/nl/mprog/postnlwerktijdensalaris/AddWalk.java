package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddWalk extends AppCompatActivity{

    int idMonth;
    int idDay;
    int idWalk;
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
    TextView districtCodeView;
    TextView timeGoalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addwalk);

        idMonth = getIntent().getIntExtra("idMonth", 0);
        idDay = getIntent().getIntExtra("idDay", 0);
        idWalk = getIntent().getIntExtra("idWalk", 0);

        editBeginHour1 = (EditText) findViewById(R.id.beginHour1);
        editBeginMin1 = (EditText) findViewById(R.id.beginMin1);
        editEndHour1 = (EditText) findViewById(R.id.endHour1);
        editEndMin1 = (EditText) findViewById(R.id.endMin1);
        editBeginHour2 = (EditText) findViewById(R.id.beginHour2);
        editBeginMin2 = (EditText) findViewById(R.id.beginMin2);
        editEndHour2 = (EditText) findViewById(R.id.endHour2);
        editEndMin2 = (EditText) findViewById(R.id.endMin2);
        editBeginHour3 = (EditText) findViewById(R.id.beginHour3);
        editBeginMin3 = (EditText) findViewById(R.id.beginMin3);
        editEndHour3 = (EditText) findViewById(R.id.endHour3);
        editEndMin3 = (EditText) findViewById(R.id.endMin3);

        districtCodeView = (TextView) findViewById(R.id.districtCode);
        timeGoalView = (TextView) findViewById(R.id.timeGoal);

        /*if (idWalk != 0) {

        }*/
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(AddWalk.this, Settings.class);
        startActivity(goToSettings);
    }

    public void onClickSave(View view) {
        if (editBeginHour1.getText().toString().equals("0")
                && editBeginMin1.getText().toString().equals("00")
                && editBeginHour1.getText().toString().equals("0")
                && editBeginMin1.getText().toString().equals("00")) {
            Toast.makeText(AddWalk.this, "Ongeldige invoer eerste regel", Toast.LENGTH_SHORT).show();
        }
        else if (editBeginMin1.getText().toString().length() != 2
                || editEndMin1.getText().toString().length() != 2
                || editBeginMin2.getText().toString().length() != 2
                || editEndMin2.getText().toString().length() != 2
                || editBeginMin3.getText().toString().length() != 2
                || editEndMin3.getText().toString().length() != 2) {
            Toast.makeText(AddWalk.this, "Ongeldige invoer minuten", Toast.LENGTH_SHORT).show();
        }
        else if ((Integer.parseInt(editBeginHour1.getText().toString()) < 0
                || Integer.parseInt(editBeginHour1.getText().toString()) >= 24)
                || (Integer.parseInt(editEndHour1.getText().toString()) < 0
                || Integer.parseInt(editEndHour1.getText().toString()) >= 24)
                || (Integer.parseInt(editBeginHour2.getText().toString()) < 0
                || Integer.parseInt(editBeginHour2.getText().toString()) >= 24)
                || (Integer.parseInt(editEndHour2.getText().toString()) < 0
                || Integer.parseInt(editEndHour2.getText().toString()) >= 24)
                || (Integer.parseInt(editBeginHour3.getText().toString()) < 0
                || Integer.parseInt(editBeginHour3.getText().toString()) >= 24)
                || (Integer.parseInt(editEndHour3.getText().toString()) < 0
                || Integer.parseInt(editEndHour3.getText().toString()) >= 24)) {
            Toast.makeText(AddWalk.this, "Ongeldige invoer uren", Toast.LENGTH_SHORT).show();
        }
        else if ((Integer.parseInt(editBeginMin1.getText().toString()) < 0
                || Integer.parseInt(editBeginMin1.getText().toString()) >= 60)
                || (Integer.parseInt(editEndMin1.getText().toString()) < 0
                || Integer.parseInt(editEndMin1.getText().toString()) >= 60)
                || (Integer.parseInt(editBeginMin2.getText().toString()) < 0
                || Integer.parseInt(editBeginMin2.getText().toString()) >= 60)
                || (Integer.parseInt(editEndMin2.getText().toString()) < 0
                || Integer.parseInt(editEndMin2.getText().toString()) >= 60)
                || (Integer.parseInt(editBeginMin3.getText().toString()) < 0
                || Integer.parseInt(editBeginMin3.getText().toString()) >= 60)
                || (Integer.parseInt(editEndMin3.getText().toString()) < 0
                || Integer.parseInt(editEndMin3.getText().toString()) >= 60)) {
            Toast.makeText(AddWalk.this, "Ongeldige invoer minuten", Toast.LENGTH_SHORT).show();
        }
        else {
            String districtCode = districtCodeView.getText().toString();
            String timeGoalStr = timeGoalView.getText().toString();

            if (timeGoalStr.length() == 4) {
                timeGoalStr = "0" + timeGoalStr;
            }

            String beginHour1 = editBeginHour1.getText().toString();
            String beginMin1 = editBeginMin1.getText().toString();
            String timeBegin1Str = beginHour1 + ":" + beginMin1;
            if (timeBegin1Str.length() == 4) {
                timeBegin1Str = "0" + timeBegin1Str;
            }

            String endHour1 = editEndHour1.getText().toString();
            String endMin1 = editEndMin1.getText().toString();
            String timeEnd1Str = endHour1 + ":" + endMin1;
            if (timeEnd1Str.length() == 4) {
                timeEnd1Str = "0" + timeEnd1Str;
            }

            String beginHour2 = editBeginHour2.getText().toString();
            String beginMin2 = editBeginMin2.getText().toString();
            String timeBegin2Str = beginHour2 + ":" + beginMin2;
            if (timeBegin2Str.length() == 4) {
                timeBegin2Str = "0" + timeBegin2Str;
            }

            String endHour2 = editEndHour2.getText().toString();
            String endMin2 = editEndMin2.getText().toString();
            String timeEnd2Str = endHour2 + ":" + endMin2;
            if (timeEnd2Str.length() == 4) {
                timeEnd2Str = "0" + timeEnd2Str;
            }

            String beginHour3 = editBeginHour3.getText().toString();
            String beginMin3 = editBeginMin3.getText().toString();
            String timeBegin3Str = beginHour3 + ":" + beginMin3;
            if (timeBegin3Str.length() == 4) {
                timeBegin3Str = "0" + timeBegin3Str;
            }

            String endHour3 = editEndHour3.getText().toString();
            String endMin3 = editEndMin3.getText().toString();
            String timeEnd3Str = endHour3 + ":" + endMin3;
            if (timeEnd3Str.length() == 4) {
                timeEnd3Str = "0" + timeEnd3Str;
            }

            SimpleDateFormat format = new SimpleDateFormat("HH:mm");

            Date timeBegin1 = null;
            Date timeEnd1 = null;
            Date timeBegin2 = null;
            Date timeEnd2 = null;
            Date timeBegin3 = null;
            Date timeEnd3 = null;

            try {
                timeBegin1 = format.parse(timeBegin1Str);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            try {
                timeEnd1 = format.parse(timeEnd1Str);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            try {
                timeBegin2 = format.parse(timeBegin2Str);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            try {
                timeEnd2 = format.parse(timeEnd2Str);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            try {
                 timeBegin3 = format.parse(timeBegin3Str);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            try {
                timeEnd3 = format.parse(timeEnd3Str);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            long timeDifference1 = 0;
            long timeDifference2 = 0;
            long timeDifference3 = 0;

            try {
                timeDifference1 = timeEnd1.getTime() - timeBegin1.getTime();
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
            try {
                timeDifference2 = timeEnd2.getTime() - timeBegin2.getTime();
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
            try {
                timeDifference3 = timeEnd3.getTime() - timeBegin3.getTime();
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }

            long timeDiffMin = ((timeDifference1 / 1000 / 60)
                    + (timeDifference2 / 1000 / 60) +
                    (timeDifference3 / 1000 / 60));

            long timeDiffHours = timeDiffMin / 60;
            long timeDiffMinRest = timeDiffMin % 60;

            Date timeGoal = null;
            try {
                timeGoal = format.parse(timeGoalStr);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            String timeTotalStr = timeDiffHours + ":" + timeDiffMinRest;
            Date timeTotal = null;
            try {
                timeTotal = format.parse(timeTotalStr);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            long timeExtraMs = 0;
            try {
                timeExtraMs = timeTotal.getTime() - timeGoal.getTime();
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }

            long timeExtraMin = timeExtraMs / 1000 / 60;
            long timeExtraHours = timeExtraMin / 60;
            long timeExtraMinRest = Math.abs(timeExtraMin) % 60;
            String timeExtraStr = timeExtraHours + ":" + timeExtraMinRest;

            WalkObject walkObj = new WalkObject(idMonth, idDay, idWalk, districtCode, timeBegin1Str,
                    timeEnd1Str, timeBegin2Str, timeEnd2Str, timeBegin3Str, timeEnd3Str, timeGoalStr,
                    timeExtraStr, timeTotalStr);

            DatabaseHelper db = new DatabaseHelper(AddWalk.this);
            if (idWalk == 0) {
                db.addWalk(walkObj);
            }
        }
    }

    public void onClickCancel(View view) {
    }
}
