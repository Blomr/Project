package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Months extends AppCompatActivity {

    ListView listViewMonths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.months);

        listViewMonths = (ListView) findViewById(R.id.listViewMonths);

        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<MonthObject> listItems = db.getMonths();
        MonthAdapter adapter = new MonthAdapter(this, R.layout.listview_layout, listItems);
        listViewMonths.setAdapter(adapter);

        listViewMonths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idView = (TextView) view.findViewById(R.id.listItemUpCenter);
                TextView titleMonthView = (TextView) view.findViewById(R.id.listItemUpLeft);

                int idMonth = Integer.parseInt(idView.getText().toString());
                String titleMonth = titleMonthView.getText().toString();

                Intent goToDays = new Intent(Months.this, Days.class);
                goToDays.putExtra("idMonth", idMonth);
                goToDays.putExtra("titleMonth", titleMonth);
                startActivity(goToDays);

                finish();
            }
        });
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(Months.this, Settings.class);
        goToSettings.putExtra("prevActivity", "Months");
        startActivity(goToSettings);
        finish();
    }

    public void onClickAddMonth(View view) {
        Intent goToDays = new Intent(Months.this, Days.class);
        startActivity(goToDays);
    }
}
