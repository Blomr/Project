package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Districts extends AppCompatActivity {

    ListView listViewDistricts;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.districts);

        listViewDistricts = (ListView) findViewById(R.id.listViewDistricts);

        String[] districts = {"41A", "41B", "41G", "41J", "41L", "41Z", "42A"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, districts);
        listViewDistricts.setAdapter(adapter);
    }

    public void onClickAddDistrict(View view) {
        Intent goToAddDistrict = new Intent(Districts.this, AddDistrict.class);
        startActivity(goToAddDistrict);
    }
}
