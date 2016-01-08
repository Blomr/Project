package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Days extends AppCompatActivity {

    ListView listViewDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days);

        listViewDays = (ListView) findViewById(R.id.listViewDays);

        String[] days = {"Za 19 dec 15", "Za 12 dec 15", "Za 7 dec 15", "Za 30 nov 15"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, days);
        listViewDays.setAdapter(adapter);

        listViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToWalks = new Intent(Days.this, Walks.class);
                startActivity(goToWalks);
            }
        });
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(Days.this, Settings.class);
        startActivity(goToSettings);
    }
}
