/**
 * Settings.java
 *
 * In this activity the user is able to choose between two settings options:
 * 'districts' and 'contract and salary'. The options are displayed in a
 * listview.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import nl.mprog.postnlwerktijdensalaris.R;
import nl.mprog.postnlwerktijdensalaris.databasehandler.DatabaseHandler;

public class SettingsActivity extends AppCompatActivity {

    ListView listViewSettings;
    String prevActivity;

    /**
     * Sets listview with two items and title.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // get previous activity from intent
        prevActivity = getIntent().getStringExtra("prevActivity");

        listViewSettings = (ListView) findViewById(R.id.listViewSettings);

        // set listener on item clicks.
        listViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // if first button is clicked, go to ContractAndSalaryActivity
                if (position == 0) {
                    Intent goToContractSalary = new Intent(SettingsActivity.this,
                                                ContractAndSalaryActivity.class);
                    startActivity(goToContractSalary);
                }

                // if last button is clicked, go to DistrictsActivity
                else {
                    Intent goToDistricts = new Intent(SettingsActivity.this, DistrictsActivity.class);
                    goToDistricts.putExtra("fromSettings", true);
                    goToDistricts.putExtra("prevActivity", prevActivity);
                    goToDistricts.putExtra("idMonth", getIntent().getIntExtra("idMonth", 0));
                    goToDistricts.putExtra("idDay", getIntent().getIntExtra("idDay", 0));
                    goToDistricts.putExtra("idWalk", getIntent().getIntExtra("idWalk", 0));
                    startActivity(goToDistricts);
                    finish();
                }
            }
        });
    }

    /**
     * Overrides back button. Goes back to previous activity, depending on the value of prevActivity.
     */
    @Override
    public void onBackPressed() {

        // get previous activity from intent and make new intent
        Intent goToPrevActivity = new Intent(this, MonthsActivity.class);
        boolean noDistricts = false;
        switch (prevActivity) {
            case "Days":
                goToPrevActivity = new Intent(this, DaysActivity.class);
                break;
            case "Walks":
                goToPrevActivity = new Intent(this, WalksActivity.class);
                break;
            case "AddWalk":
                DatabaseHandler dbHandler = new DatabaseHandler(this);
                int districts = dbHandler.getDistricts().size();
                if (districts > 0) {
                    goToPrevActivity = new Intent(this, AddWalkActivity.class);
                }
                else {
                    noDistricts = true;
                    Toast.makeText(SettingsActivity.this, R.string.messageAddDistrict,
                                   Toast.LENGTH_SHORT).show();
                }
                break;
        }

        // if there are districts, put boolean and ids in intent
        if (!noDistricts) {
            goToPrevActivity.putExtra("fromSettings", true);
            goToPrevActivity.putExtra("idMonth", getIntent().getIntExtra("idMonth", 0));
            goToPrevActivity.putExtra("idDay", getIntent().getIntExtra("idDay", 0));
            goToPrevActivity.putExtra("idWalk", getIntent().getIntExtra("idWalk", 0));
            startActivity(goToPrevActivity);
            finish();
        }
    }

    /**
     * Goes back to previous activity, depending on the value of prevActivity.
     */
    public void onClickBack(View view) {
        onBackPressed();
    }
}
