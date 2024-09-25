package fr.thomas.menard.rewardparcours.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.thomas.menard.rewardparcours.R;
import fr.thomas.menard.rewardparcours.databinding.ActivityIdentificationBinding;

public class IdentificationActivity extends AppCompatActivity {

    ActivityIdentificationBinding binding;
    String date, patientID, caseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIdentificationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        listenBtn();
    }

    private void listenBtn(){
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date =new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                patientID = binding.APatientTxtIdPatient.getText().toString().trim();
                caseID = binding.APatientTxtIdCase.getText().toString().trim();

                Log.d("TEST", "id" + patientID);
                if(patientID.length()!=7 || caseID.length()!=7){
                    Toast.makeText(getApplicationContext(), "Please, write a correct PID & FID", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), IntroductionActivity.class);
                    intent.putExtra("date", date);
                    intent.putExtra("patientID", patientID);
                    intent.putExtra("caseID", caseID);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}