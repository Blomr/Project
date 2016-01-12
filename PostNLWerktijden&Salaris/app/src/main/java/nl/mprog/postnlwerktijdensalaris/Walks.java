package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Walks extends AppCompatActivity {

    ListView listViewWalks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walks);

        boolean newDay = getIntent().getBooleanExtra("newDay", false);

        if (newDay) {
            final EditText editTitle = (EditText) findViewById(R.id.editTitleDay);
            final Button okButton = (Button) findViewById(R.id.okButtonDay);

            editTitle.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String getText = String.valueOf(R.id.editTitleDay);

                    if (!getText.equals("")) {
                        editTitle.setVisibility(View.GONE);
                        okButton.setVisibility(View.GONE);

                        TextView title = (TextView) findViewById(R.id.titleDay);
                        title.setVisibility(View.VISIBLE);
                        title.setText(getText);

                        DayObject dayObj = new DayObject(getText, 0, 0.00, "0:00");
                        DatabaseHelper db = new DatabaseHelper(Days.this);
                        db.addMonth(monthObj);
                    }
                    else {
                        Toast.makeText(Days.this, "Please fill in a title", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        listViewWalks = (ListView) findViewById(R.id.listViewWalks);

        ArrayList<WalkObject> listItems = new ArrayList<>();
        WalkObject item1 = new WalkObject("41B", "11:35", "13:23", "18:46", "22:34", "22:34", "13:34", "2:11", "0:11", "2:22");
        listItems.add(item1);
        WalkObject item2 = new WalkObject("41J", "11:35", "13:23", "18:46", "22:34", "22:34", "13:34", "2:11", "0:11", "2:22");
        listItems.add(item2);
        WalkObject item3 = new WalkObject("41K", "11:35", "13:23", "18:46", "22:34", "22:34", "13:34", "2:11", "0:11", "2:22");
        listItems.add(item3);
        WalkObject item4 = new WalkObject("41Z", "11:35", "13:23", "18:46", "22:34", "22:34", "13:34", "2:11", "0:11", "2:22");
        listItems.add(item4);

        WalkAdapter adapter = new WalkAdapter(this, R.layout.listview_layout, listItems);
        listViewWalks.setAdapter(adapter);
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(Walks.this, Settings.class);
        startActivity(goToSettings);
    }

    public void onClickAddWalk(View view) {
        Intent goToAddWalk = new Intent(Walks.this, AddWalk.class);
        startActivity(goToAddWalk);
    }
}
