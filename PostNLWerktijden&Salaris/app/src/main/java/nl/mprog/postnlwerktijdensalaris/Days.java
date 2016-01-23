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

public class Days extends AppCompatActivity {

    EditText editTitle;
    Button okButton;
    ImageView addButton;
    TextView titleMonthView;
    int idMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days);

        idMonth = getIntent().getIntExtra("idMonth", 0);

        if (idMonth == 0) {
            editTitle = (EditText) findViewById(R.id.editTitleMonth);
            okButton = (Button) findViewById(R.id.okButtonMonth);
            addButton = (ImageView) findViewById(R.id.addButtonDays);

            editTitle.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
        }
        else {
            ListView listViewDays = (ListView) findViewById(R.id.listViewDays);
            titleMonthView = (TextView) findViewById(R.id.titleMonth);

            final DatabaseHandler db = new DatabaseHandler(this);
            final ArrayList<DayObject> listItems = db.getDaysOfMonth(idMonth);
            final DayAdapter adapter = new DayAdapter(this, R.layout.listview_layout, listItems);
            listViewDays.setAdapter(adapter);

            String titleMonth = db.getMonthName(idMonth);
            titleMonthView.setText(titleMonth);
            titleMonthView.setVisibility(View.VISIBLE);

            listViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView idView = (TextView) view.findViewById(R.id.listItemUpCenter);
                    TextView titleDayView = (TextView) view.findViewById(R.id.listItemUpLeft);

                    int idDay = Integer.parseInt(idView.getText().toString());
                    String titleDay = titleDayView.getText().toString();

                    Intent goToWalks = new Intent(Days.this, Walks.class);
                    goToWalks.putExtra("idMonth", idMonth);
                    goToWalks.putExtra("idDay", idDay);
                    goToWalks.putExtra("titleDay", titleDay);
                    startActivity(goToWalks);
                    finish();
                }
            });

            listViewDays.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    TextView idDayView = (TextView) view.findViewById(R.id.listItemUpCenter);
                    final int idDay = Integer.parseInt(idDayView.getText().toString());
                    final Bundle sharedPrefBundle = new Bundle();
                    SharedPreferences sharedPref = getSharedPreferences("contractAndSalary", MODE_PRIVATE);
                    sharedPrefBundle.putInt("contractHours", sharedPref.getInt("contractHours", 0));
                    sharedPrefBundle.putInt("contractMins", sharedPref.getInt("contractMins", 0));
                    sharedPrefBundle.putInt("salaryEuro", sharedPref.getInt("salaryEuro", 0));
                    sharedPrefBundle.putInt("salaryCents", sharedPref.getInt("salaryCents", 0));
                    sharedPrefBundle.putInt("extraEuro", sharedPref.getInt("extraEuro", 0));
                    sharedPrefBundle.putInt("extraCents", sharedPref.getInt("extraCents", 0));

                    AlertDialog.Builder builder = new AlertDialog.Builder(Days.this);
                    builder.setMessage("Weet u zeker dat u deze dag wil verwijderen?");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteDay(idMonth, idDay, sharedPrefBundle);
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
        Intent goToSettings = new Intent(Days.this, Settings.class);
        goToSettings.putExtra("prevActivity", "Days");
        goToSettings.putExtra("idMonth", idMonth);
        startActivity(goToSettings);
        finish();
    }

    public void onClickAddDay(View view) {
        Intent goToWalks = new Intent(Days.this, Walks.class);
        goToWalks.putExtra("idMonth", idMonth);
        startActivity(goToWalks);
    }

    public void onClickOkDays(View view) {
        String getText = editTitle.getText().toString();

        if (!getText.equals("")) {
            editTitle.setVisibility(View.GONE);
            okButton.setVisibility(View.GONE);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            addButton.setVisibility(View.VISIBLE);

            TextView title = (TextView) findViewById(R.id.titleMonth);
            title.setVisibility(View.VISIBLE);
            title.setText(getText);

            DatabaseHandler db = new DatabaseHandler(Days.this);
            if (idMonth == 0) {
                MonthObject monthObj = new MonthObject(0, getText, 0, 0.00, "0:00");
                idMonth = db.addMonth(monthObj);
            }
            else {
                db.editMonthName(idMonth, getText);
            }
        }
        else {
            Toast.makeText(Days.this, "Vul een titel in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent goToMonths = new Intent(Days.this, Months.class);
        startActivity(goToMonths);
        finish();
    }

    public void onClickTitle(View view) {
        titleMonthView.setVisibility(View.INVISIBLE);
        editTitle.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.VISIBLE);

        editTitle.setText(titleMonthView.getText().toString());
    }
}
