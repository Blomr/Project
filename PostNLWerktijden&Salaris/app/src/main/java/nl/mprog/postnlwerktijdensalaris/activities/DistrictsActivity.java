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
        ArrayList<District> listItems = dbHandler.getDistricts();

        // adapt arraylist of district objects on listview
        DistrictAdapter adapter = new DistrictAdapter(this, R.layout.custom_listitem_layout, listItems);
        listViewDistricts.setAdapter(adapter);

        // set listener on item clicks
        listViewDistricts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idView = (TextView) view.findViewById(R.id.listItemUpCenter);
                int idDistrict = Integer.parseInt(idView.getText().toString());

                // put id in intent, go to AddDistrictActivity
                Intent goToAddDistrict = new Intent(DistrictsActivity.this, AddDistrictActivity.class);
                goToAddDistrict.putExtra("id", idDistrict);
                startActivity(goToAddDistrict);
                finish();
            }
        });
    }

    /**
     * Handles clicks on plus button. Goes to AddDistrictActivity.
     */
    public void onClickAddDistrict(View view) {
        Intent goToAddDistrict = new Intent(this, AddDistrictActivity.class);
        startActivity(goToAddDistrict);
        finish();
    }

    /**
     * Goes to SettingsActivity.
     */
    public void onClickBack(View view) {
        onBackPressed();
    }
}
