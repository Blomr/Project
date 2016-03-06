/**
 * DaysActivity.java
 *
 * In this activity the user is able to see the days he added to
 * the corresponding month. It is also possible to add new days by pressing
 * the plus button. Content in DaysActivity changes by adding walks in the day.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.activities;

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

import nl.mprog.postnlwerktijdensalaris.modelclasses.Month;
import nl.mprog.postnlwerktijdensalaris.R;
import nl.mprog.postnlwerktijdensalaris.adapters.DayAdapter;
import nl.mprog.postnlwerktijdensalaris.databasehandler.DatabaseHandler;
import nl.mprog.postnlwerktijdensalaris.modelclasses.Day;

public class DaysActivity extends AppCompatActivity {

    EditText editTitle;
    Button okButton;
    ImageView addButton;
    ImageView backButton;
    TextView titleMonthView;
    int idMonth;

    /**
     * Sets layout, adding content depending on idMonth from intent.
     * Sets also listeners on clicks and long clicks.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);

        titleMonthView = (TextView) findViewById(R.id.titleMonth);
        editTitle = (EditText) findViewById(R.id.editTitleMonth);
        okButton = (Button) findViewById(R.id.okButtonMonth);
        backButton = (ImageView) findViewById(R.id.backButtonDays);
        addButton = (ImageView) findViewById(R.id.addButtonDays);

        // get id from intent or savedinstancestate
        if (savedInstanceState != null) {
            idMonth = savedInstanceState.getInt("idMonth", 0);
        }
        else {
            idMonth = getIntent().getIntExtra("idMonth", 0);
        }

        // if there is no id, set content for making a new month
        if (idMonth == 0) {


            editTitle.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.INVISIBLE);
        }

        // if there is an id, get objects with idMonth from database and set in listview
        else {
            ListView listViewDays = (ListView) findViewById(R.id.listViewDays);

            final DatabaseHandler dbHandler = new DatabaseHandler(this);
            final ArrayList<Day> listItems = dbHandler.getDaysOfMonth(idMonth);
            final DayAdapter adapter = new DayAdapter(this, R.layout.custom_listitem_layout, listItems);
            listViewDays.setAdapter(adapter);

            String titleMonth = dbHandler.getMonthName(idMonth);
            titleMonthView.setText(titleMonth);
            titleMonthView.setVisibility(View.VISIBLE);

            // set listener on item clicks
            listViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // get idDay from list item
                    TextView idDayView = (TextView) view.findViewById(R.id.listItemUpCenter);
                    int idDay = Integer.parseInt(idDayView.getText().toString());

                    // put idMonth and idDay in intent, go to WalksActivity
                    Intent goToWalks = new Intent(DaysActivity.this, WalksActivity.class);
                    goToWalks.putExtra("idMonth", idMonth);
                    goToWalks.putExtra("idDay", idDay);
                    startActivity(goToWalks);
                    finish();
                }
            });

            // set listener on long item clicks
            listViewDays.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    // get idDay from list item
                    TextView idDayView = (TextView) view.findViewById(R.id.listItemUpCenter);
                    final int idDay = Integer.parseInt(idDayView.getText().toString());

                    // put shared preferences into bundle
                    final Bundle sharedPrefBundle = new Bundle();
                    SharedPreferences sharedPref = getSharedPreferences("contractAndSalary", MODE_PRIVATE);
                    sharedPrefBundle.putInt("contractHours", sharedPref.getInt("contractHours", 0));
                    sharedPrefBundle.putInt("contractMins", sharedPref.getInt("contractMins", 0));
                    sharedPrefBundle.putInt("salaryEuro", sharedPref.getInt("salaryEuro", 0));
                    sharedPrefBundle.putInt("salaryCents", sharedPref.getInt("salaryCents", 0));
                    sharedPrefBundle.putInt("extraEuro", sharedPref.getInt("extraEuro", 0));
                    sharedPrefBundle.putInt("extraCents", sharedPref.getInt("extraCents", 0));

                    // make alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(DaysActivity.this);
                    builder.setMessage(R.string.messageDeleteDay);

                    // if ok button is pressed, delete day
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHandler.deleteDay(idMonth, idDay, sharedPrefBundle);
                            listItems.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });

                    // if cancel is pressed, go back to activity
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });
        }
    }

    /**
     * Handles clicks on settings button. Goes to SettingsActivity.
     */
    public void onClickSettings(View view) {

        // put Days as previous activity and id in intent
        Intent goToSettings = new Intent(DaysActivity.this, SettingsActivity.class);
        goToSettings.putExtra("prevActivity", "Days");
        goToSettings.putExtra("idMonth", idMonth);
        startActivity(goToSettings);
        finish();
    }

    /**
     * Handles clicks on plus button. Goes to WalksActivity.
     */
    public void onClickAddDay(View view) {
        Intent goToWalks = new Intent(DaysActivity.this, WalksActivity.class);
        goToWalks.putExtra("idMonth", idMonth);
        startActivity(goToWalks);
        finish();
    }

    /**
     * Handles clicks on ok button if user makes new month.
     */
    public void onClickOkDays(View view) {
        String title = editTitle.getText().toString();

        if (!title.equals("")) {
            editTitle.setVisibility(View.GONE);
            okButton.setVisibility(View.GONE);

            // delete keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            addButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);

            // get title from edittext and set in textview
            TextView titleView = (TextView) findViewById(R.id.titleMonth);
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title);

            // save month in database
            DatabaseHandler dbHandler = new DatabaseHandler(DaysActivity.this);
            if (idMonth == 0) {
                Month monthObj = new Month(0, title, 0, 0.00, "0:00");
                idMonth = dbHandler.addMonth(monthObj);
            }

            // if month already existed, update in database
            else {
                dbHandler.editMonthName(idMonth, title);
            }
        }

        // send toast if title is empty
        else {
            Toast.makeText(DaysActivity.this, R.string.messageInputTitle, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles clicks on title. Title becomes editable.
     */
    public void onClickTitle(View view) {
        titleMonthView.setVisibility(View.INVISIBLE);
        editTitle.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.INVISIBLE);

        editTitle.setText(titleMonthView.getText().toString());
    }

    /**
     * Saves idMonth if screen orientation changes.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("idMonth", idMonth);
    }

    /**
     * Overrides back button. Goes to MonthsActivity.
     */
    @Override
    public void onBackPressed() {
        Intent goToMonths = new Intent(DaysActivity.this, MonthsActivity.class);
        startActivity(goToMonths);
        finish();
    }

    /**
     * Goes to MonthsActivity.
     */
    public void onClickBack(View view) {
        onBackPressed();
    }
}
