package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Months extends AppCompatActivity {

    ListView listViewMonths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.months);

        listViewMonths = (ListView) findViewById(R.id.listViewMonths);

        String[] months = {"januari", "februari", "maart", "april", "mei", "juni",
                          "juli", "augustus", "september", "oktober", "november",
                          "december"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, months);
        listViewMonths.setAdapter(adapter);

        listViewMonths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToDays = new Intent(Months.this, Days.class);
                startActivity(goToDays);
            }
        });
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(Months.this, Settings.class);
        startActivity(goToSettings);
    }
}
