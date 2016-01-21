package nl.mprog.postnlwerktijdensalaris;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Walks extends AppCompatActivity {

    EditText editTitle;
    Button okButton;
    ImageView addButton;
    int idMonth;
    int idDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walks);

        idMonth = getIntent().getIntExtra("idMonth", 0);
        idDay = getIntent().getIntExtra("idDay", 0);

        if (idDay == 0) {
            editTitle = (EditText) findViewById(R.id.editTitleDay);
            okButton = (Button) findViewById(R.id.okButtonDay);
            addButton = (ImageView) findViewById(R.id.addButtonWalks);

            editTitle.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
        }
        else {
            ListView listViewWalks = (ListView) findViewById(R.id.listViewWalks);
            TextView titleDayView = (TextView) findViewById(R.id.titleDay);

            String titleDay = getIntent().getStringExtra("titleDay");
            titleDayView.setText(titleDay);
            titleDayView.setVisibility(View.VISIBLE);

            DatabaseHandler db = new DatabaseHandler(this);
            ArrayList<WalkObject> listItems = db.getWalksOfDay(idMonth, idDay);
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
        String getText = editTitle.getText().toString();

        if (!getText.equals("")) {
            editTitle.setVisibility(View.GONE);
            okButton.setVisibility(View.GONE);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            addButton.setVisibility(View.VISIBLE);

            TextView title = (TextView) findViewById(R.id.titleDay);
            title.setVisibility(View.VISIBLE);
            title.setText(getText);

            DayObject dayObj = new DayObject(idMonth, 0, getText, "", "0:00", "0:00", "0:00");
            DatabaseHandler db = new DatabaseHandler(Walks.this);
            idDay = db.addDay(dayObj);
        }
        else {
            Toast.makeText(Walks.this, "Vul een titel in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent goToDays = new Intent(Walks.this, Days.class);
        goToDays.putExtra("idMonth", idMonth);
        startActivity(goToDays);
        finish();
    }
}
