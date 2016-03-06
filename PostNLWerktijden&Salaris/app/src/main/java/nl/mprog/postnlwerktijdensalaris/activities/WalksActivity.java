/**
 * WalksActivity.java
 *
 * In this activity the user is able to see the walks he added to
 * the corresponding day. It is also possible to add new walks by pressing
 * the plus button.
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

import nl.mprog.postnlwerktijdensalaris.R;
import nl.mprog.postnlwerktijdensalaris.adapters.WalkAdapter;
import nl.mprog.postnlwerktijdensalaris.databasehandler.DatabaseHandler;
import nl.mprog.postnlwerktijdensalaris.modelclasses.Day;
import nl.mprog.postnlwerktijdensalaris.modelclasses.Walk;

public class WalksActivity extends AppCompatActivity {

    EditText editTitle;
    Button okButton;
    ImageView backButton;
    TextView titleDayView;
    int idMonth;
    int idDay;

    /**
     * Sets layout, adding content depending on idMonth and idDay from intent.
     * Sets also listeners on clicks and long clicks.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walks);

        titleDayView = (TextView) findViewById(R.id.titleDay);
        editTitle = (EditText) findViewById(R.id.editTitleDay);
        okButton = (Button) findViewById(R.id.okButtonDay);
        backButton = (ImageView) findViewById(R.id.backButtonWalks);

        // get ids from intent or savedinstancestate
        if (savedInstanceState != null) {
            idMonth = savedInstanceState.getInt("idMonth", 0);
            idDay = savedInstanceState.getInt("idDay", 0);
        }
        else {
            idMonth = getIntent().getIntExtra("idMonth", 0);
            idDay = getIntent().getIntExtra("idDay", 0);
        }

        // if there is no idDay, set edittext and button to make new day
        if (idDay == 0) {
            editTitle.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.INVISIBLE);
        }

        // if there is an idDay, get arraylist of objects with idMonth and idDay and adapt on listview
        else {
            ListView listViewWalks = (ListView) findViewById(R.id.listViewWalks);

            // get arraylist of objects from database
            final DatabaseHandler dbHandler = new DatabaseHandler(this);
            final ArrayList<Walk> listItems = dbHandler.getWalksOfDay(idMonth, idDay);
            final WalkAdapter adapter = new WalkAdapter(this, R.layout.custom_listitem_layout, listItems);
            listViewWalks.setAdapter(adapter);

            // make title visible
            String titleDay = dbHandler.getDayName(idMonth, idDay);
            titleDayView.setText(titleDay);
            titleDayView.setVisibility(View.VISIBLE);

            // check if settings has already districts
            checkForDistricts();

            // set listener on item clicks, if there are districts
            if (dbHandler.getDistricts().size() > 0) {
                listViewWalks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // get idWalk from list item
                        TextView idWalkView = (TextView) view.findViewById(R.id.listItemUpCenter);
                        int idWalk = Integer.parseInt(idWalkView.getText().toString());

                        // go to AddWalkActivity
                        Intent goToAddWalk = new Intent(WalksActivity.this, AddWalkActivity.class);
                        goToAddWalk.putExtra("idMonth", idMonth);
                        goToAddWalk.putExtra("idDay", idDay);
                        goToAddWalk.putExtra("idWalk", idWalk);
                        startActivity(goToAddWalk);
                        finish();
                    }
                });
            }

            // set listener on long item clicks
            listViewWalks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    // get idWalk from list item
                    TextView idWalkView = (TextView) view.findViewById(R.id.listItemUpCenter);
                    final int idWalk = Integer.parseInt(idWalkView.getText().toString());

                    // get shared preferences and put in bundle
                    final Bundle sharedPrefBundle = new Bundle();
                    SharedPreferences sharedPref = getSharedPreferences("contractAndSalary", MODE_PRIVATE);
                    sharedPrefBundle.putInt("contractHours", sharedPref.getInt("contractHours", 0));
                    sharedPrefBundle.putInt("contractMins", sharedPref.getInt("contractMins", 0));
                    sharedPrefBundle.putInt("salaryEuro", sharedPref.getInt("salaryEuro", 0));
                    sharedPrefBundle.putInt("salaryCents", sharedPref.getInt("salaryCents", 0));
                    sharedPrefBundle.putInt("extraEuro", sharedPref.getInt("extraEuro", 0));
                    sharedPrefBundle.putInt("extraCents", sharedPref.getInt("extraCents", 0));

                    // make alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(WalksActivity.this);
                    builder.setMessage(R.string.messageDeleteWalk);

                    // if ok button is clicked, delete walk
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHandler.deleteWalk(idMonth, idDay, idWalk, sharedPrefBundle);
                            listItems.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });

                    // if cancel button is clicked, go back to activity
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

        // put Walks as previous activity and ids in intent
        Intent goToSettings = new Intent(WalksActivity.this, SettingsActivity.class);
        goToSettings.putExtra("prevActivity", "Walks");
        goToSettings.putExtra("idMonth", idMonth);
        goToSettings.putExtra("idDay", idDay);
        startActivity(goToSettings);
        finish();
    }

    /**
     * Handles clicks on plus button. Goes to AddWalkActivity.
     */
    public void onClickAddWalk(View view) {
        Intent goToAddWalk = new Intent(WalksActivity.this, AddWalkActivity.class);
        goToAddWalk.putExtra("idMonth", idMonth);
        goToAddWalk.putExtra("idDay", idDay);
        startActivity(goToAddWalk);
        finish();
    }

    /**
     * Handles clicks on ok button if user makes new day.
     */
    public void onClickOkWalks(View view) {
        String title = editTitle.getText().toString();

        if (!title.equals("")) {
            editTitle.setVisibility(View.GONE);
            okButton.setVisibility(View.GONE);

            // delete keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            backButton.setVisibility(View.VISIBLE);

            // check if there are districts in settings
            checkForDistricts();

            // set title from edittext in textview
            TextView titleView = (TextView) findViewById(R.id.titleDay);
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title);

            // save day in database
            DatabaseHandler db = new DatabaseHandler(WalksActivity.this);
            if (idDay == 0) {
                Day dayObj = new Day(idMonth, 0, title, "", "0:00", "0:00", "0:00");
                idDay = db.addDay(dayObj);
            }

            // if day already existed, update in database
            else {
                db.editDayName(idMonth, idDay, title);
            }
        }

        // if edittext is empty, send user a message
        else {
            Toast.makeText(WalksActivity.this, R.string.messageInputTitle, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks in database for rows in districts table.
     * If there's none, display message and hide plus button.
     */
    public void checkForDistricts() {
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        if (dbHandler.getDistricts().size() != 0) {
            ImageView addButton = (ImageView) findViewById(R.id.addButtonWalks);
            addButton.setVisibility(View.VISIBLE);
        }
        else {
            TextView addDistrictMessage = (TextView) findViewById(R.id.addDistrictMessage);
            addDistrictMessage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Handles clicks on title. Title becomes editable.
     */
    public void onClickTitle(View view) {
        titleDayView.setVisibility(View.INVISIBLE);
        editTitle.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.INVISIBLE);

        editTitle.setText(titleDayView.getText().toString());
    }

    /**
     * Saves idMonth and idDay if screen orientation changes.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("idMonth", idMonth);
        outState.putInt("idDay", idDay);
    }

    /**
     * Overrides back button. Goes to DaysActivity.
     */
    @Override
    public void onBackPressed() {
        Intent goToDays = new Intent(WalksActivity.this, DaysActivity.class);
        goToDays.putExtra("idMonth", idMonth);
        startActivity(goToDays);
        finish();
    }

    /**
     * Goes to DaysActivity.
     */
    public void onClickBack(View view) {
        onBackPressed();
    }
}
