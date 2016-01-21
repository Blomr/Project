package nl.mprog.postnlwerktijdensalaris;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Days extends AppCompatActivity {

    EditText editTitle;
    Button okButton;
    ImageView addButton;
    int idMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days);

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
            ListView listViewDays = (ListView) findViewById(R.id.listViewDays);
            TextView titleMonthView = (TextView) findViewById(R.id.titleMonth);

            String titleMonth = getIntent().getStringExtra("titleMonth");
            titleMonthView.setText(titleMonth);
            titleMonthView.setVisibility(View.VISIBLE);

            DatabaseHandler db = new DatabaseHandler(this);
            ArrayList<DayObject> listItems = db.getDaysOfMonth(idMonth);
            DayAdapter adapter = new DayAdapter(this, R.layout.listview_layout, listItems);
            listViewDays.setAdapter(adapter);

            listViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView idView = (TextView) view.findViewById(R.id.listItemUpCenter);
                    TextView titleDayView = (TextView) view.findViewById(R.id.listItemUpLeft);

                    int idDay = Integer.parseInt(idView.getText().toString());
                    String titleDay = titleDayView.getText().toString();

                    Intent goToWalks = new Intent(Days.this, Walks.class);
                    goToWalks.putExtra("idMonth", idMonth);
                    goToWalks.putExtra("idDay", idDay);
                    goToWalks.putExtra("titleDay", titleDay);
                    startActivity(goToWalks);
                    finish();
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

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            addButton.setVisibility(View.VISIBLE);

            TextView title = (TextView) findViewById(R.id.titleMonth);
            title.setVisibility(View.VISIBLE);
            title.setText(getText);

            MonthObject monthObj = new MonthObject(0, getText, 0, 0.00, "0:00");
            DatabaseHandler db = new DatabaseHandler(Days.this);
            idMonth = db.addMonth(monthObj);
        }
        else {
            Toast.makeText(Days.this, "Vul een titel in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent goToMonths = new Intent(Days.this, Months.class);
        startActivity(goToMonths);
        finish();
    }
}
