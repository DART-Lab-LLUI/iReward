package fr.thomas.menard.rewardparcours.Views;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.thomas.menard.rewardparcours.BaseActivtiy.BaseActivity;
import fr.thomas.menard.rewardparcours.R;
import fr.thomas.menard.rewardparcours.databinding.ActivityIdentificationBinding;

public class IdentificationActivity extends BaseActivity {

    ActivityIdentificationBinding binding;
    String date, patientID, caseID;
    int clinicID;

    @Override
    public void init() {
        initClinicIdSpinner();
    }

    @Override
    public void listenBtn() {
        listenConfirmBtn();
    }

    private void listenConfirmBtn(){
        binding.btnConfirm.setOnClickListener(view -> {
            date =new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            patientID = binding.APatientTxtIdPatient.getText().toString().trim();
            caseID = binding.APatientTxtIdCase.getText().toString().trim();
            clinicID = binding.clinicIdSpinner.getSelectedItemPosition();

            if(patientID.length()!=7 || caseID.length()!=7){
                Toast.makeText(getApplicationContext(), "Please, write a correct PID & FID", Toast.LENGTH_SHORT).show();
            } else {
                patientInfo.setPatientData(patientID, caseID, clinicID, this);
                navigateToNextActivity(IntroductionActivity.class);
            }
        });
    }

    @Override
    public void setBinding() {
        binding = ActivityIdentificationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }

    private void initClinicIdSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.clinic_names, R.layout.item_spinner
        );

        adapter.setDropDownViewResource(R.layout.item_spinner);
        binding.clinicIdSpinner.setAdapter(adapter);
        binding.clinicIdSpinner.setSelection(0);
    }
}