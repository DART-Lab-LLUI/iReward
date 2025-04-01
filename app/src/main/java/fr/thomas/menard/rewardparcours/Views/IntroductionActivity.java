package fr.thomas.menard.rewardparcours.Views;

import android.view.LayoutInflater;
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