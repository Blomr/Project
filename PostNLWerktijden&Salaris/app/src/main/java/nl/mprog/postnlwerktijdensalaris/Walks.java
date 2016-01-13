package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Walks extends AppCompatActivity {

    ListView listViewWalks;
    TextView idMonthView;
    TextView idDayView;
    EditText editTitle;
    Button okButton;
    ImageView addButton;
    int idMonth;
    int idDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walks);

        idMonthView = (TextView) findViewById(R.id.idMonthWalks);
        idDayView = (TextView) findViewById(R.id.idDayWalks);

        idMonth = getIntent().getIntExtra("idMonth", 0);
        idDay = getIntent().getIntExtra("idDay", 0);

        String idMonthString = Integer.toString(idMonth);
        idMonthView.setText(idMonthString);

        if (idDay == 0) {
            editTitle = (EditText) findViewById(R.id.editTitleDay);
            okButton = (Button) findViewById(R.id.okButtonDay);
            addButton = (ImageView) findViewById(R.id.addButtonWalks);
            editTitle.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
        }
        else {
            listViewWalks = (ListView) findViewById(R.id.listViewWalks);

            ArrayList<WalkObject> listItems = new ArrayList<>();
            WalkObject item1 = new WalkObject(1, 1, "41B", "11:35", "13:23", "18:46", "22:34", "22:34", "13:34", "2:11", "0:11", "2:22");
            listItems.add(item1);
            WalkObject item2 = new WalkObject(1, 1, "41J", "11:35", "13:23", "18:46", "22:34", "22:34", "13:34", "2:11", "0:11", "2:22");
            listItems.add(item2);
            WalkObject item3 = new WalkObject(1, 1, "41K", "11:35", "13:23", "18:46", "22:34", "22:34", "13:34", "2:11", "0:11", "2:22");
            listItems.add(item3);
            WalkObject item4 = new WalkObject(1, 1, "41Z", "11:35", "13:23", "18:46", "22:34", "22:34", "13:34", "2:11", "0:11", "2:22");
            listItems.add(item4);

            WalkAdapter adapter = new WalkAdapter(this, R.layout.listview_layout, listItems);
            listViewWalks.setAdapter(adapter);
        }
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(Walks.this, Settings.class);
        startActivity(goToSettings);
    }

    public void onClickAddWalk(View view) {
        Intent goToAddWalk = new Intent(Walks.this, AddWalk.class);
        goToAddWalk.putExtra("idMonth", idMonth);
        goToAddWalk.putExtra("idDay", idDay);
        startActivity(goToAddWalk);
    }

    public void onClickOkWalks(View view) {
        String getText = String.valueOf(R.id.editTitleDay);

        if (!getText.equals("")) {
            editTitle.setVisibility(View.GONE);
            okButton.setVisibility(View.GONE);
            addButton.setVisibility(View.VISIBLE);

            TextView title = (TextView) findViewById(R.id.titleDay);
            title.setVisibility(View.VISIBLE);
            title.setText(getText);

            DayObject dayObj = new DayObject(idMonth, getText, "", "0:00", "0:00", "0:00");
            DatabaseHelper db = new DatabaseHelper(Walks.this);
            idDay = db.addDay(dayObj);
            String idDayString = Integer.toString(idDay);
            idDayView.setText(idDayString);
        }
        else {
            Toast.makeText(Walks.this, "Vul een titel in", Toast.LENGTH_SHORT).show();
        }
    }
}
