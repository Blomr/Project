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

        ArrayList<DistrictObject> listItems = new ArrayList<>();
        DistrictObject item1 = new DistrictObject("41B", "2:11", "1:35");
        listItems.add(item1);
        DistrictObject item2 = new DistrictObject("41J", "1:45", "1:11");
        listItems.add(item2);
        DistrictObject item3 = new DistrictObject("41K", "2:35", "1:55");
        listItems.add(item3);
        DistrictObject item4 = new DistrictObject("41Z", "1:49", "1:20");
        listItems.add(item4);

        DistrictAdapter adapter = new DistrictAdapter(this, R.layout.listview_layout, listItems);
        listViewDistricts.setAdapter(adapter);
    }

    public void onClickAddDistrict(View view) {
        Intent goToAddDistrict = new Intent(Districts.this, AddDistrict.class);
        startActivity(goToAddDistrict);
    }
}
