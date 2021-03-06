/**
 * ContractAndSalaryActivity.java
 *
 * In this activity the user is able to change his contract hours and salary.
 * If the user clicks the save button, the changes will be saved in
 * shared preferences. The shared preferences will be used by the
 * databasehandler.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import nl.mprog.postnlwerktijdensalaris.R;

public class ContractAndSalaryActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEdit;
    int contractHours;
    int contractMins;
    int salaryEuro;
    int salaryCents;
    int extraEuro;
    int extraCents;
    String contractHoursStr;
    String contractMinsStr;
    String salaryEuroStr;
    String salaryCentsStr;
    String extraEuroStr;
    String extraCentsStr;
    EditText editContractHours;
    EditText editContractMins;
    EditText editSalaryEuro;
    EditText editSalaryCents;
    EditText editExtraEuro;
    EditText editExtraCents;

    /**
     * Sets layout, initialize variables and loads shared preferences into edittexts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_and_salary);

        // initialize editTexts
        editContractHours = (EditText) findViewById(R.id.contractHours);
        editContractMins = (EditText) findViewById(R.id.contractMins);
        editSalaryEuro = (EditText) findViewById(R.id.salaryEuro);
        editSalaryCents = (EditText) findViewById(R.id.salaryCents);
        editExtraEuro = (EditText) findViewById(R.id.extraEuro);
        editExtraCents = (EditText) findViewById(R.id.extraCents);

        // get shared preferences
        sharedPref = getSharedPreferences("contractAndSalary", MODE_PRIVATE);
        contractHours = sharedPref.getInt("contractHours", 0);
        contractMins = sharedPref.getInt("contractMins", 0);
        salaryEuro = sharedPref.getInt("salaryEuro", 0);
        salaryCents = sharedPref.getInt("salaryCents", 0);
        extraEuro = sharedPref.getInt("extraEuro", 0);
        extraCents = sharedPref.getInt("extraCents", 0);

        // set shared preferences into editTexts
        contractHoursStr = Integer.toString(contractHours);
        editContractHours.setText(contractHoursStr);

        // if minutes or cents is one digit, add 0
        contractMinsStr = Integer.toString(contractMins);
        if (contractMinsStr.length() == 1) {
            contractMinsStr = "0" + contractMinsStr;
        }
        editContractMins.setText(contractMinsStr);

        salaryEuroStr = Integer.toString(salaryEuro);
        editSalaryEuro.setText(salaryEuroStr);

        salaryCentsStr = Integer.toString(salaryCents);
        if (salaryCentsStr.length() == 1) {
            salaryCentsStr = "0" + salaryCentsStr;
        }
        editSalaryCents.setText(salaryCentsStr);

        extraEuroStr = Integer.toString(extraEuro);
        editExtraEuro.setText(extraEuroStr);

        extraCentsStr = Integer.toString(extraCents);
        if (extraCentsStr.length() == 1) {
            extraCentsStr = "0" + extraCentsStr;
        }
        editExtraCents.setText(extraCentsStr);
    }

    /**
     * Handles clicks on save button.
     */
    public void onClickSave(View view) {

        // get values from edittexts
        contractHours = Integer.parseInt(editContractHours.getText().toString());
        contractMinsStr = editContractMins.getText().toString();
        salaryEuro = Integer.parseInt(editSalaryEuro.getText().toString());
        salaryCentsStr = editSalaryCents.getText().toString();
        extraEuro = Integer.parseInt(editExtraEuro.getText().toString());
        extraCentsStr = editExtraCents.getText().toString();

        // check if input is correct
        if (Integer.parseInt(contractMinsStr) >= 60 || contractMinsStr.length() != 2) {
            Toast.makeText(ContractAndSalaryActivity.this, R.string.invalidMinutes, Toast.LENGTH_SHORT).show();
        }
        else if (salaryCentsStr.length() != 2 || extraCentsStr.length() != 2) {
            Toast.makeText(ContractAndSalaryActivity.this, R.string.invalidCents, Toast.LENGTH_SHORT).show();
        }

        // if input is correct, delete zeroes in strings if necessary and convert into integers
        else {
            if (salaryCentsStr.charAt(0) == '0') {
                salaryCentsStr = String.valueOf(salaryCentsStr.charAt(1));
            }
            salaryCents = Integer.parseInt(salaryCentsStr);

            if (extraCentsStr.charAt(0) == '0') {
                extraCentsStr = String.valueOf(extraCentsStr.charAt(1));
            }
            extraCents = Integer.parseInt(extraCentsStr);

            // save integers into shared preferences
            sharedPrefEdit = sharedPref.edit();
            sharedPrefEdit.putInt("contractHours", contractHours);
            sharedPrefEdit.putInt("contractMins", contractMins);
            sharedPrefEdit.putInt("salaryEuro", salaryEuro);
            sharedPrefEdit.putInt("salaryCents", salaryCents);
            sharedPrefEdit.putInt("extraEuro", extraEuro);
            sharedPrefEdit.putInt("extraCents", extraCents);
            sharedPrefEdit.apply();

            finish();
        }
    }

    /**
     * Handles clicks on cancel button. Goes to SettingsActivity.
     */
    public void onClickCancel(View view) {
        finish();
    }

    /**
     * Goes to SettingsActivity.
     */
    public void onClickBack(View view) {
        onBackPressed();
    }
}
