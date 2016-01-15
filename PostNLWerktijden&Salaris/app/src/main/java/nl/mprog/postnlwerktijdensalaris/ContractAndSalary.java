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
    int salaryHour;
    int salaryMin;
    int extraHour;
    int extraMin;

    String contractHoursStr;
    String contractMinsStr;
    String salaryHourStr;
    String salaryMinStr;
    String extraHourStr;
    String extraMinStr;

    EditText editContractHours;
    EditText editContractMins;
    EditText editSalaryHour;
    EditText editSalaryMin;
    EditText editExtraHour;
    EditText editExtraMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contractandsalary);

        editContractHours = (EditText) findViewById(R.id.contractHours);
        editContractMins = (EditText) findViewById(R.id.contractMins);
        editSalaryHour = (EditText) findViewById(R.id.salaryHour);
        editSalaryMin = (EditText) findViewById(R.id.salaryMin);
        editExtraHour = (EditText) findViewById(R.id.extraHour);
        editExtraMin = (EditText) findViewById(R.id.extraMin);

        sharedPref = getSharedPreferences("contractAndSalary", MODE_PRIVATE);
        contractHours = sharedPref.getInt("contractHours", 0);
        contractMins = sharedPref.getInt("contractMins", 0);
        salaryHour = sharedPref.getInt("salaryHour", 0);
        salaryMin = sharedPref.getInt("salaryMin", 0);
        extraHour = sharedPref.getInt("extraHour", 0);
        extraMin = sharedPref.getInt("extraMin", 0);

        contractHoursStr = Integer.toString(contractHours);
        editContractHours.setText(contractHoursStr);

        contractMinsStr = Integer.toString(contractMins);
        if (contractMinsStr.length() == 1) {
            contractMinsStr = "0" + contractMinsStr;
        }
        editContractMins.setText(contractMinsStr);

        salaryHourStr = Integer.toString(salaryHour);
        editSalaryHour.setText(salaryHourStr);

        salaryMinStr = Integer.toString(salaryMin);
        if (salaryMinStr.length() == 1) {
            salaryMinStr = "0" + salaryMinStr;
        }
        editSalaryMin.setText(salaryMinStr);

        extraHourStr = Integer.toString(extraHour);
        editExtraHour.setText(extraHourStr);

        extraMinStr = Integer.toString(extraMin);
        if (extraMinStr.length() == 1) {
            extraMinStr = "0" + extraMinStr;
        }
        editExtraMin.setText(extraMinStr);
    }

    public void onClickSave(View view) {

        contractHours = Integer.parseInt(editContractHours.getText().toString());
        salaryHour = Integer.parseInt(editSalaryHour.getText().toString());
        extraHour = Integer.parseInt(editExtraHour.getText().toString());

        contractMinsStr = editContractMins.getText().toString();
        if (contractMinsStr.charAt(0) == '0') {
            contractMinsStr = String.valueOf(contractMinsStr.charAt(1));
        }
        contractMins = Integer.parseInt(contractMinsStr);

        salaryMinStr = editSalaryMin.getText().toString();
        if (salaryMinStr.charAt(0) == '0') {
            salaryMinStr = String.valueOf(salaryMinStr.charAt(1));
        }
        salaryMin = Integer.parseInt(salaryMinStr);

        extraMinStr = editExtraMin.getText().toString();
        if (extraMinStr.charAt(0) == '0') {
            extraMinStr = String.valueOf(extraMinStr.charAt(1));
        }
        extraMin = Integer.parseInt(extraMinStr);

        sharedPrefEdit = sharedPref.edit();
        sharedPrefEdit.putInt("contractHours", contractHours);
        sharedPrefEdit.putInt("contractMins", contractMins);
        sharedPrefEdit.putInt("salaryHour", salaryHour);
        sharedPrefEdit.putInt("salaryMin", salaryMin);
        sharedPrefEdit.putInt("extraHour", extraHour);
        sharedPrefEdit.putInt("extraMin", extraMin);
        sharedPrefEdit.apply();

        finish();
    }

    public void onClickCancel(View view) {
        finish();
    }
}
