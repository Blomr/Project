/**
 * DistrictsActivity.java
 *
 * In this activity the user is able to see the districts that he has
 * made in a listview. It is possible to click an item to edit the district.
 * It also possible to add a new district by clicking the plus button.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import nl.mprog.postnlwerktijdensalaris.R;
import nl.mprog.postnlwerktijdensalaris.adapters.DistrictAdapter;
import nl.mprog.postnlwerktijdensalaris.databasehandler.DatabaseHandler;
import nl.mprog.postnlwerktijdensalaris.modelclasses.District;

public class DistrictsActivity extends AppCompatActivity {

    ListView listViewDistricts;

    /**
     * Sets layout, gets districts from database and sets into listview.
     * Sets also a listener on item clicks.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_districts);

        listViewDistricts = (ListView) findViewById(R.id.listViewDistricts);

        // get districts from database
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        final ArrayList<District> listItems = dbHandler.getDistricts();

        // adapt arraylist of district objects on listview
        final DistrictAdapter adapter = new DistrictAdapter(this, R.layout.custom_listitem_layout, listItems);
        listViewDistricts.setAdapter(adapter);

        // set listener on item clicks
        listViewDistricts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idView = (TextView) view.findViewById(R.id.listItemUpCenter);
                int idDistrict = Integer.parseInt(idView.getText().toString());

                // put id in intent, go to AddDistrictActivity
                Intent goToAddDistrict = new Intent(DistrictsActivity.this, AddDistrictActivity.class);
                goToAddDistrict.putExtra("idDistrict", idDistrict);
                goToAddDistrict.putExtra("fromSettings", true);
                goToAddDistrict.putExtra("prevActivity", getIntent().getStringExtra("prevActivity"));
                goToAddDistrict.putExtra("idMonth", getIntent().getIntExtra("idMonth", 0));
                goToAddDistrict.putExtra("idDay", getIntent().getIntExtra("idDay", 0));
                goToAddDistrict.putExtra("idWalk", getIntent().getIntExtra("idWalk", 0));
                startActivity(goToAddDistrict);
                finish();
            }
        });

        // set listener on long item clicks
        listViewDistricts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // get id from list item
                TextView idDistrictView = (TextView) view.findViewById(R.id.listItemUpCenter);
                final int idDistrict = Integer.parseInt(idDistrictView.getText().toString());

                // make alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(DistrictsActivity.this);
                builder.setMessage(R.string.messageDeleteDistrict);

                // if ok button is pressed, delete district
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler dbHandler = new DatabaseHandler(DistrictsActivity.this);
                        dbHandler.deleteDistrict(idDistrict);
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

    /**
     * Handles clicks on plus button. Goes to AddDistrictActivity.
     */
    public void onClickAddDistrict(View view) {
        Intent goToAddDistrict = new Intent(this, AddDistrictActivity.class);
        goToAddDistrict.putExtra("fromSettings", true);
        goToAddDistrict.putExtra("prevActivity", getIntent().getStringExtra("prevActivity"));
        goToAddDistrict.putExtra("idMonth", getIntent().getIntExtra("idMonth", 0));
        goToAddDistrict.putExtra("idDay", getIntent().getIntExtra("idDay", 0));
        goToAddDistrict.putExtra("idWalk", getIntent().getIntExtra("idWalk", 0));
        startActivity(goToAddDistrict);
        finish();
    }

    /**
     * Overrides back button. Goes to SettingsActivity.
     */
    @Override
    public void onBackPressed() {
        Intent goToSettings = new Intent(this, SettingsActivity.class);
        goToSettings.putExtra("fromSettings", true);
        goToSettings.putExtra("prevActivity", getIntent().getStringExtra("prevActivity"));
        goToSettings.putExtra("idMonth", getIntent().getIntExtra("idMonth", 0));
        goToSettings.putExtra("idDay", getIntent().getIntExtra("idDay", 0));
        goToSettings.putExtra("idWalk", getIntent().getIntExtra("idWalk", 0));
        startActivity(goToSettings);
        finish();
    }

    /**
     * Goes to SettingsActivity.
     */
    public void onClickBack(View view) {
        onBackPressed();
    }
}
