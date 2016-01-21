package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class Districts extends AppCompatActivity {

    ListView listViewDistricts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.districts);

        listViewDistricts = (ListView) findViewById(R.id.listViewDistricts);

        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<DistrictObject> listItems = db.getDistricts();

        DistrictAdapter adapter = new DistrictAdapter(this, R.layout.listview_layout, listItems);
        listViewDistricts.setAdapter(adapter);
    }

    public void onClickAddDistrict(View view) {
        Intent goToAddDistrict = new Intent(Districts.this, AddDistrict.class);
        startActivity(goToAddDistrict);
        finish();
    }
}
