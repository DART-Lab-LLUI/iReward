package fr.thomas.menard.rewardparcours.Views;

import android.app.AlertDialog;
import android.view.LayoutInflater;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import fr.thomas.menard.rewardparcours.BaseActivtiy.BaseActivity;
import fr.thomas.menard.rewardparcours.DataUploadUtils.FileManager;
import fr.thomas.menard.rewardparcours.R;
import fr.thomas.menard.rewardparcours.Utils.WriteCSV;
import fr.thomas.menard.rewardparcours.databinding.ActivityIntroductionBinding;

public class IntroductionActivity extends BaseActivity {

    ActivityIntroductionBinding binding;

    private WriteCSV writeCSVClass;

    String info_csv_path;

    private void listenBtnStart(){
        binding.AIntroductionBtnStart.setOnClickListener(view -> {
            navigateToNextActivity(PermissionsActivity.class);
        });
    }

    private void checkUser(){
        File caseDir = FileManager.getCaseIDFolder(this, patientInfo);
        File[] subFolders = caseDir.listFiles(File::isDirectory);

        if (subFolders != null && subFolders.length > 0) {
            Arrays.sort(subFolders, (f1, f2) -> f2.getName().compareTo(f1.getName()));
            File newestFolder = subFolders[0];
            String newestDate = newestFolder.getName();

            new AlertDialog.Builder(this)
                    .setTitle("Continue last session?")
                    .setMessage("Do you want to continue the last session from " + formatDate(newestDate) + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        patientInfo.setDate(newestDate);
                        navigateToNextActivity(PermissionsActivity.class);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        createNewSession();
                        navigateToNextActivity(PermissionsActivity.class);
                    })
                    .setCancelable(false)
                    .show();
        } else {
            createNewSession();
        }
    }

    private String formatDate(String date){
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            return targetFormat.format(originalFormat.parse(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void createNewSession(){
        info_csv_path = FileManager.getScoreFilename(this, patientInfo);
        writeCSVClass.createAndWriteCSV(info_csv_path, patientInfo.getPatientId(),patientInfo.getCaseId(), patientInfo.getDate(), patientInfo.getClinicIdtoString());
    }

    @Override
    public void init() {
        writeCSVClass = WriteCSV.getInstance(this);
        checkUser();
    }

    @Override
    public void listenBtn() {
        listenBtnStart();
    }

    @Override
    public void setBinding() {
        binding = ActivityIntroductionBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }
}