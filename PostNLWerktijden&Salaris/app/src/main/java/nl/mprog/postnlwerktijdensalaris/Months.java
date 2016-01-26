package nl.mprog.postnlwerktijdensalaris;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

        final DatabaseHandler db = new DatabaseHandler(this);
        final ArrayList<MonthObject> listItems = db.getMonths();
        final MonthAdapter adapter = new MonthAdapter(this, R.layout.listview_layout, listItems);
        listViewMonths.setAdapter(adapter);

        listViewMonths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idMonthView = (TextView) view.findViewById(R.id.listItemUpCenter);
                int idMonth = Integer.parseInt(idMonthView.getText().toString());

                Intent goToDays = new Intent(Months.this, Days.class);
                goToDays.putExtra("idMonth", idMonth);
                startActivity(goToDays);

                finish();
            }
        });

        listViewMonths.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView idMonthView = (TextView) view.findViewById(R.id.listItemUpCenter);
                final int idMonth = Integer.parseInt(idMonthView.getText().toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(Months.this);
                builder.setMessage("Weet u zeker dat u deze maand wil verwijderen?");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteMonth(idMonth);
                        listItems.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("annuleren", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
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
