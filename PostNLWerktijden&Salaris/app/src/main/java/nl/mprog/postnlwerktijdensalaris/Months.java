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

        ArrayList<MonthObject> listItems = new ArrayList<>();
        MonthObject item1 = new MonthObject(4, "december 2015", 4, 117.34, "18:46");
        listItems.add(item1);
        MonthObject item2 = new MonthObject(3, "november 2015", 3, 114.63, "19:51");
        listItems.add(item2);
        MonthObject item3 = new MonthObject(2, "oktober 2015", 4, 119.74, "19:23");
        listItems.add(item3);
        MonthObject item4 = new MonthObject(1, "september 2015", 4, 112.85, "18:58");
        listItems.add(item4);

        final MonthAdapter adapter = new MonthAdapter(this, R.layout.listview_layout, listItems);
        listViewMonths.setAdapter(adapter);

        listViewMonths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idView = (TextView) view.findViewById(R.id.listItemUpCenter);
                int idMonth = Integer.parseInt(idView.getText().toString());
                Intent goToDays = new Intent(Months.this, Days.class);
                goToDays.putExtra("idMonth", idMonth);
                startActivity(goToDays);
                finish();
            }
        });
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(Months.this, Settings.class);
        startActivity(goToSettings);
    }

    public void onClickAddMonth(View view) {
        Intent goToDays = new Intent(Months.this, Days.class);
        startActivity(goToDays);
    }
}
