package nl.mprog.postnlwerktijdensalaris;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class ContractAndSalary extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contractandsalary);

        editContractHours = (EditText) findViewById(R.id.contractHours);
        editContractMins = (EditText) findViewById(R.id.contractMins);
        editSalaryEuro = (EditText) findViewById(R.id.salaryEuro);
        editSalaryCents = (EditText) findViewById(R.id.salaryCents);
        editExtraEuro = (EditText) findViewById(R.id.extraEuro);
        editExtraCents = (EditText) findViewById(R.id.extraCents);

        sharedPref = getSharedPreferences("contractAndSalary", MODE_PRIVATE);
        contractHours = sharedPref.getInt("contractHours", 0);
        contractMins = sharedPref.getInt("contractMins", 0);
        salaryEuro = sharedPref.getInt("salaryEuro", 0);
        salaryCents = sharedPref.getInt("salaryCents", 0);
        extraEuro = sharedPref.getInt("extraEuro", 0);
        extraCents = sharedPref.getInt("extraCents", 0);

        contractHoursStr = Integer.toString(contractHours);
        editContractHours.setText(contractHoursStr);

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

    public void onClickSave(View view) {

        contractHours = Integer.parseInt(editContractHours.getText().toString());
        salaryEuro = Integer.parseInt(editSalaryEuro.getText().toString());
        extraEuro = Integer.parseInt(editExtraEuro.getText().toString());

        contractMinsStr = editContractMins.getText().toString();
        if (contractMinsStr.charAt(0) == '0') {
            contractMinsStr = String.valueOf(contractMinsStr.charAt(1));
        }
        contractMins = Integer.parseInt(contractMinsStr);

        salaryCentsStr = editSalaryCents.getText().toString();
        if (salaryCentsStr.charAt(0) == '0') {
            salaryCentsStr = String.valueOf(salaryCentsStr.charAt(1));
        }
        salaryCents = Integer.parseInt(salaryCentsStr);

        extraCentsStr = editExtraCents.getText().toString();
        if (extraCentsStr.charAt(0) == '0') {
            extraCentsStr = String.valueOf(extraCentsStr.charAt(1));
        }
        extraCents = Integer.parseInt(extraCentsStr);

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

    public void onClickCancel(View view) {
        finish();
    }
}
