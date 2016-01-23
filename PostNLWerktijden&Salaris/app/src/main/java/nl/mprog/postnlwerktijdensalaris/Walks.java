package nl.mprog.postnlwerktijdensalaris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Walks extends AppCompatActivity {

    EditText editTitle;
    Button okButton;
    ImageView addButton;
    TextView titleDayView;
    int idMonth;
    int idDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walks);

        idMonth = getIntent().getIntExtra("idMonth", 0);
        idDay = getIntent().getIntExtra("idDay", 0);
        addButton = (ImageView) findViewById(R.id.addButtonWalks);

        if (idDay == 0) {
            editTitle = (EditText) findViewById(R.id.editTitleDay);
            okButton = (Button) findViewById(R.id.okButtonDay);

            editTitle.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
        }
        else {
            ListView listViewWalks = (ListView) findViewById(R.id.listViewWalks);
            titleDayView = (TextView) findViewById(R.id.titleDay);

            final DatabaseHandler db = new DatabaseHandler(this);
            final ArrayList<WalkObject> listItems = db.getWalksOfDay(idMonth, idDay);
            final WalkAdapter adapter = new WalkAdapter(this, R.layout.listview_layout, listItems);
            listViewWalks.setAdapter(adapter);

            String titleDay = db.getDayName(idMonth, idDay);
            titleDayView.setText(titleDay);
            titleDayView.setVisibility(View.VISIBLE);

            checkForDistricts();

            listViewWalks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    TextView idWalkView = (TextView) view.findViewById(R.id.listItemUpCenter);
                    final int idWalk = Integer.parseInt(idWalkView.getText().toString());
                    final Bundle sharedPrefBundle = new Bundle();
                    SharedPreferences sharedPref = getSharedPreferences("contractAndSalary", MODE_PRIVATE);
                    sharedPrefBundle.putInt("contractHours", sharedPref.getInt("contractHours", 0));
                    sharedPrefBundle.putInt("contractMins", sharedPref.getInt("contractMins", 0));
                    sharedPrefBundle.putInt("salaryEuro", sharedPref.getInt("salaryEuro", 0));
                    sharedPrefBundle.putInt("salaryCents", sharedPref.getInt("salaryCents", 0));
                    sharedPrefBundle.putInt("extraEuro", sharedPref.getInt("extraEuro", 0));
                    sharedPrefBundle.putInt("extraCents", sharedPref.getInt("extraCents", 0));

                    AlertDialog.Builder builder = new AlertDialog.Builder(Walks.this);
                    builder.setMessage("Weet u zeker dat u deze loop wil verwijderen?");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteWalk(idMonth, idDay, idWalk, sharedPrefBundle);
                            listItems.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("annuleren", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    return true;
                }
            });
        }
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(Walks.this, Settings.class);
        goToSettings.putExtra("prevActivity", "Walks");
        goToSettings.putExtra("idMonth", idMonth);
        goToSettings.putExtra("idDay", idDay);
        startActivity(goToSettings);
        finish();
    }

    public void onClickAddWalk(View view) {
        Intent goToAddWalk = new Intent(Walks.this, AddWalk.class);
        goToAddWalk.putExtra("idMonth", idMonth);
        goToAddWalk.putExtra("idDay", idDay);
        startActivity(goToAddWalk);
    }

    public void onClickOkWalks(View view) {
        String getText = editTitle.getText().toString();

        if (!getText.equals("")) {
            editTitle.setVisibility(View.GONE);
            okButton.setVisibility(View.GONE);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            checkForDistricts();

            TextView title = (TextView) findViewById(R.id.titleDay);
            title.setVisibility(View.VISIBLE);
            title.setText(getText);

            DatabaseHandler db = new DatabaseHandler(Walks.this);
            if (idDay == 0) {
                DayObject dayObj = new DayObject(idMonth, 0, getText, "", "0:00", "0:00", "0:00");
                idDay = db.addDay(dayObj);
            }
            else {
                db.editDayName(idMonth, idDay, getText);
            }
        }
        else {
            Toast.makeText(Walks.this, "Vul een titel in", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkForDistricts() {
        DatabaseHandler db = new DatabaseHandler(this);
        if (db.getDistricts().size() != 0) {
            addButton.setVisibility(View.VISIBLE);
        }
        else {
            TextView addDistrictMessage = (TextView) findViewById(R.id.addDistrictMessage);
            addDistrictMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent goToDays = new Intent(Walks.this, Days.class);
        goToDays.putExtra("idMonth", idMonth);
        startActivity(goToDays);
        finish();
    }

    public void onClickTitle(View view) {
        titleDayView.setVisibility(View.INVISIBLE);
        editTitle.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.VISIBLE);

        editTitle.setText(titleDayView.getText().toString());
    }
}
