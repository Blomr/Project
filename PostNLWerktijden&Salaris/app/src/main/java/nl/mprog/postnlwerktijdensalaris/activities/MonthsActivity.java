/**
 * MonthsActivity.java
 *
 * In this activity the user is able to see the months he added.
 * It is also possible to add new months by pressing the plus button.
 * Content in Months changes by adding walks.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import nl.mprog.postnlwerktijdensalaris.R;
import nl.mprog.postnlwerktijdensalaris.adapters.MonthAdapter;
import nl.mprog.postnlwerktijdensalaris.databasehandler.DatabaseHandler;
import nl.mprog.postnlwerktijdensalaris.modelclasses.Month;

public class MonthsActivity extends AppCompatActivity {

    ListView listViewMonths;

    /**
     * Sets layout, sets monthsObjects from database in listview.
     * Sets also click- and long click listeners on list items.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months);

        listViewMonths = (ListView) findViewById(R.id.listViewMonths);

        // get arraylist of all months in database and adapt on listview
        final DatabaseHandler dbHandler = new DatabaseHandler(this);
        final ArrayList<Month> listItems = dbHandler.getMonths();
        final MonthAdapter adapter = new MonthAdapter(this, R.layout.custom_listitem_layout, listItems);
        listViewMonths.setAdapter(adapter);

        // set listener on item clicks
        listViewMonths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get idMonth from list item
                TextView idMonthView = (TextView) view.findViewById(R.id.listItemUpCenter);
                int idMonth = Integer.parseInt(idMonthView.getText().toString());

                // put idMonth in intent, go to DaysActivity
                Intent goToDays = new Intent(MonthsActivity.this, DaysActivity.class);
                goToDays.putExtra("idMonth", idMonth);
                startActivity(goToDays);
                finish();
            }
        });

        // set listener on long item clicks
        listViewMonths.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                // get idMonth from list item
                TextView idMonthView = (TextView) view.findViewById(R.id.listItemUpCenter);
                final int idMonth = Integer.parseInt(idMonthView.getText().toString());

                // make alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MonthsActivity.this);
                builder.setMessage(R.string.messageDeleteMonth);

                // if ok button is pressed, delete month
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandler.deleteMonth(idMonth);
                        listItems.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                // if cancel button is pressed, go back to activity
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

    /**
     * Handles clicks on settings button. Goes to SettingsActivity.
     */
    public void onClickSettings(View view) {

        // put Months as previous activity in intent
        Intent goToSettings = new Intent(MonthsActivity.this, SettingsActivity.class);
        goToSettings.putExtra("prevActivity", "Months");
        startActivity(goToSettings);
        finish();
    }

    /**
     * Handles clicks on plus button. Goes to DaysActivity.
     */
    public void onClickAddMonth(View view) {
        Intent goToDays = new Intent(MonthsActivity.this, DaysActivity.class);
        startActivity(goToDays);
        finish();
    }

    /**
     * Closes app.
     */
    public void onClickExit(View view) {
        finish();
        System.exit(0);
    }
}
