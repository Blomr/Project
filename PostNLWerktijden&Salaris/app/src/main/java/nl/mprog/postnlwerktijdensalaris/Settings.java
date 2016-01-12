package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Settings extends AppCompatActivity {

    ListView listViewSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        listViewSettings = (ListView) findViewById(R.id.listViewSettings);

        listViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent goToContractSalary = new Intent(Settings.this, ContractAndSalary.class);
                    startActivity(goToContractSalary);
                }
                else {
                    Intent goToDistricts = new Intent(Settings.this, Districts.class);
                    startActivity(goToDistricts);
                }
            }
        });
    }
}
