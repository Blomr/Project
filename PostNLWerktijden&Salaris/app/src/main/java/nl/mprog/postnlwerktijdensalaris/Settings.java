package nl.mprog.postnlwerktijdensalaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Settings extends AppCompatActivity {

    ListView listViewSettings;
    String prevActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        prevActivity = getIntent().getStringExtra("prevActivity");

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

    @Override
    public void onBackPressed() {
        Intent goToPrevActivity;
        switch (prevActivity) {
            case "Days":
                goToPrevActivity = new Intent(this, Days.class);
                break;
            case "Walks":
                goToPrevActivity = new Intent(this, Walks.class);
                break;
            case "AddWalk":
                goToPrevActivity = new Intent(this, AddWalk.class);
                break;
            default:
                goToPrevActivity = new Intent(this, Months.class);
        }

        goToPrevActivity.putExtra("fromSettings", true);
        goToPrevActivity.putExtra("idMonth", getIntent().getIntExtra("idMonth", 0));
        goToPrevActivity.putExtra("idDay", getIntent().getIntExtra("idDay", 0));
        goToPrevActivity.putExtra("idWalk", getIntent().getIntExtra("idWalk", 0));
        startActivity(goToPrevActivity);
        finish();
    }
}
