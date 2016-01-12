package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Days extends AppCompatActivity {

    ListView listViewDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days);

        listViewDays = (ListView) findViewById(R.id.listViewDays);

        String[] days = {"Za 19 dec 15", "Za 12 dec 15", "Za 7 dec 15", "Za 30 nov 15"};

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
        listViewDays.setAdapter(adapter);

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

    public void onClickAddDay(View view) {
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
