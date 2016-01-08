package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Walks extends AppCompatActivity {

    ListView listViewWalks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walks);

        listViewWalks = (ListView) findViewById(R.id.listViewWalks);

        String[] walks = {"41B", "41J"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, walks);
        listViewWalks.setAdapter(adapter);
    }

    public void onClickAddWalk(View view) {
        Intent goToAddWalk = new Intent(Walks.this, AddWalk.class);
        startActivity(goToAddWalk);
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(Walks.this, Settings.class);
        startActivity(goToSettings);
    }
}
