package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Months extends AppCompatActivity {

    ListView listViewMonths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.months);

        listViewMonths = (ListView) findViewById(R.id.listViewMonths);

        ArrayList<ListItemObj> listItems = new ArrayList<>();
        ListItemObj item1 = new ListItemObj("december 2015", 4, 117.34, "18:46");
        listItems.add(item1);
        ListItemObj item2 = new ListItemObj("november 2015", 3, 114.63, "19:51");
        listItems.add(item2);
        ListItemObj item3 = new ListItemObj("oktober 2015", 4, 119.74, "19:23");
        listItems.add(item3);
        ListItemObj item4 = new ListItemObj("september 2015", 4, 112.85, "18:58");
        listItems.add(item4);

        CustomAdapter adapter = new CustomAdapter(this, R.layout.listview_layout_1, listItems);
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

    public void onClickAddMonth(View view) {
        Intent goToDays = new Intent(Months.this, Days.class);
        startActivity(goToDays);
    }

    public class ListItemObj {
        public String upLeft;
        public String downLeft;
        public String upRight;
        public String downRight;

        public ListItemObj(String month, int days, double salary, String time) {
            this.upLeft = month;
            this.downLeft = Integer.toString(days);
            this.upRight = Double.toString(salary);
            this.downRight = time;
        }
    }
}
