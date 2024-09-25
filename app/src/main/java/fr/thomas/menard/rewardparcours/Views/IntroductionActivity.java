package fr.thomas.menard.rewardparcours.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.thomas.menard.rewardparcours.R;
import fr.thomas.menard.rewardparcours.Utils.WriteCSV;
import fr.thomas.menard.rewardparcours.databinding.ActivityIntroductionBinding;

public class IntroductionActivity extends AppCompatActivity {

    ActivityIntroductionBinding binding;

    private WriteCSV writeCSVClass;

    String info_csv_path, csv_path_PID;

    String caseID, patientID, date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroductionBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        init();
        checkUser(patientID);
        listenBtn();


    }

    private void init(){
        writeCSVClass = WriteCSV.getInstance(this);

        Intent intent = getIntent();
        caseID = intent.getStringExtra("caseID");
        patientID = intent.getStringExtra("patientID");
        date = intent.getStringExtra("date");

    }

    private void listenBtn(){
        binding.AIntroductionBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PermissionsActivity.class);
                intent.putExtra("patientID", patientID);
                intent.putExtra("caseID", caseID);
                intent.putExtra("date", date);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkUser(String patientID){
        csv_path_PID = getExternalFilesDir(null).getAbsolutePath() + "/"+patientID+"/";
        info_csv_path = getExternalFilesDir(null).getAbsolutePath() + "/"+patientID+"/"+patientID+"_score.csv";

        File folder = new File(csv_path_PID);

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            // If the folder does not exist, create it
            if (folder.mkdirs()) {
                writeCSVClass.createAndWriteCSV(info_csv_path, patientID,caseID, date);
            } else {
                System.out.println("Failed to create the folder at the specified path.");
            }
        } else {
            System.out.println("The folder already exists at the specified path.");
        }

    }

}