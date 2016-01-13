package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Days extends AppCompatActivity {

    ListView listViewDays;
    TextView idMonthView;
    EditText editTitle;
    Button okButton;
    ImageView addButton;
    int idMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days);

        idMonthView = (TextView) findViewById(R.id.idMonthDays);

        idMonth = getIntent().getIntExtra("idMonth", 0);

        if (idMonth == 0) {
            editTitle = (EditText) findViewById(R.id.editTitleMonth);
            okButton = (Button) findViewById(R.id.okButtonMonth);
            addButton = (ImageView) findViewById(R.id.addButtonDays);
            editTitle.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
        }
        else {
            listViewDays = (ListView) findViewById(R.id.listViewDays);

            ArrayList<DayObject> listItems = new ArrayList<>();
            DayObject item1 = new DayObject(1, "Za 23 nov 15", "41B, 41J", "5:13", "4:23", "0:49");
            listItems.add(item1);
            DayObject item2 = new DayObject(1, "Za 16 nov 15", "41B, 41J", "5:01", "4:23", "0:50");
            listItems.add(item2);
            DayObject item3 = new DayObject(1, "Za 9 nov 15", "41B, 41K", "4:55", "4:23", "0:21");
            listItems.add(item3);
            DayObject item4 = new DayObject(1, "Za 2 nov 15", "41B, 41J", "5:11", "5:00", "0:11");
            listItems.add(item4);

            DayAdapter adapter = new DayAdapter(this, R.layout.listview_layout, listItems);
            listViewDays.setAdapter(adapter);

            listViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent goToWalks = new Intent(Days.this, Walks.class);
                    startActivity(goToWalks);
                }
            });
        }
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(Days.this, Settings.class);
        startActivity(goToSettings);
    }

    public void onClickAddDay(View view) {
        Intent goToWalks = new Intent(Days.this, Walks.class);
        goToWalks.putExtra("idMonth", idMonth);
        startActivity(goToWalks);
    }

    public void onClickOkDays(View view) {
        String getText = editTitle.getText().toString();

        if (!getText.equals("")) {
            editTitle.setVisibility(View.GONE);
            okButton.setVisibility(View.GONE);
            addButton.setVisibility(View.VISIBLE);

            TextView title = (TextView) findViewById(R.id.titleMonth);
            title.setVisibility(View.VISIBLE);
            title.setText(getText);

            MonthObject monthObj = new MonthObject(getText, 0, 0.00, "0:00");
            DatabaseHelper db = new DatabaseHelper(Days.this);
            idMonth = db.addMonth(monthObj);
            String idMonthString = Integer.toString(idMonth);
            idMonthView.setText(idMonthString);
        }
        else {
            Toast.makeText(Days.this, "Vul een titel in", Toast.LENGTH_SHORT).show();
        }
    }
}
