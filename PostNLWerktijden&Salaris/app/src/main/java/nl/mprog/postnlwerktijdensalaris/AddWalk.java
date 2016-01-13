package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddWalk extends AppCompatActivity{

    int idMonth;
    int idDay;
    int idWalk;
    EditText beginHour1;
    EditText beginMin1;
    EditText endHour1;
    EditText endMin1;
    EditText beginHour2;
    EditText beginMin2;
    EditText endHour2;
    EditText endMin2;
    EditText beginHour3;
    EditText beginMin3;
    EditText endHour3;
    EditText endMin3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addwalk);

        idMonth = getIntent().getIntExtra("idMonth", 0);
        idDay = getIntent().getIntExtra("idDay", 0);
        idWalk = getIntent().getIntExtra("idWalk", 0);

        beginHour1 = (EditText) findViewById(R.id.beginHour1);
        beginMin1 = (EditText) findViewById(R.id.beginMin1);
        endHour1 = (EditText) findViewById(R.id.endHour1);
        endMin1 = (EditText) findViewById(R.id.endMin1);
        beginHour2 = (EditText) findViewById(R.id.beginHour2);
        beginMin2 = (EditText) findViewById(R.id.beginMin2);
        endHour2 = (EditText) findViewById(R.id.endHour2);
        endMin2 = (EditText) findViewById(R.id.endMin2);
        beginHour3 = (EditText) findViewById(R.id.beginHour3);
        beginMin3 = (EditText) findViewById(R.id.beginMin3);
        endHour3 = (EditText) findViewById(R.id.endHour3);
        endMin3 = (EditText) findViewById(R.id.endMin3);

        /*if (idWalk != 0) {

        }*/
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(AddWalk.this, Settings.class);
        startActivity(goToSettings);
    }

    public void onClickSave(View view) {
        if (beginHour1.getText().toString().equals("0")
                && beginMin1.getText().toString().equals("00")
                && beginHour1.getText().toString().equals("0")
                && beginMin1.getText().toString().equals("00")) {
            Toast.makeText(AddWalk.this, "Ongeldige invoer eerste regel", Toast.LENGTH_SHORT).show();
        }
        else if (beginMin1.getText().toString().length() != 2
                || endMin1.getText().toString().length() != 2
                || beginMin2.getText().toString().length() != 2
                || endMin2.getText().toString().length() != 2
                || beginMin3.getText().toString().length() != 2
                || endMin3.getText().toString().length() != 2) {
            Toast.makeText(AddWalk.this, "Ongeldige invoer minuten", Toast.LENGTH_SHORT).show();
        }
        else if ((Integer.parseInt(beginHour1.getText().toString()) < 0
                || Integer.parseInt(beginHour1.getText().toString()) >= 24)
                || (Integer.parseInt(endHour1.getText().toString()) < 0
                || Integer.parseInt(endHour1.getText().toString()) >= 24)
                || (Integer.parseInt(beginHour2.getText().toString()) < 0
                || Integer.parseInt(beginHour2.getText().toString()) >= 24)
                || (Integer.parseInt(endHour2.getText().toString()) < 0
                || Integer.parseInt(endHour2.getText().toString()) >= 24)
                || (Integer.parseInt(beginHour3.getText().toString()) < 0
                || Integer.parseInt(beginHour3.getText().toString()) >= 24)
                || (Integer.parseInt(endHour3.getText().toString()) < 0
                || Integer.parseInt(endHour3.getText().toString()) >= 24)) {
            Toast.makeText(AddWalk.this, "Ongeldige invoer uren", Toast.LENGTH_SHORT).show();
        }
        else if ((Integer.parseInt(beginMin1.getText().toString()) < 0
                || Integer.parseInt(beginMin1.getText().toString()) >= 60)
                || (Integer.parseInt(endMin1.getText().toString()) < 0
                || Integer.parseInt(endMin1.getText().toString()) >= 60)
                || (Integer.parseInt(beginMin2.getText().toString()) < 0
                || Integer.parseInt(beginMin2.getText().toString()) >= 60)
                || (Integer.parseInt(endMin2.getText().toString()) < 0
                || Integer.parseInt(endMin2.getText().toString()) >= 60)
                || (Integer.parseInt(beginMin3.getText().toString()) < 0
                || Integer.parseInt(beginMin3.getText().toString()) >= 60)
                || (Integer.parseInt(endMin3.getText().toString()) < 0
                || Integer.parseInt(endMin3.getText().toString()) >= 60))
    }

    public void onClickCancel(View view) {
    }
}
