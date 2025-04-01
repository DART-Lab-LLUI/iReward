package fr.thomas.menard.rewardparcours.BaseActivtiy;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import fr.thomas.menard.rewardparcours.BaseActivtiy.IActivityCreator;
import fr.thomas.menard.rewardparcours.BaseActivtiy.IIntentHandler;
import fr.thomas.menard.rewardparcours.Model.Patient;
import fr.thomas.menard.rewardparcours.R;

/**
 * BaseActivity is an abstract class that provides common functionality
 * for all activities in the application.
 */
public abstract class BaseActivity extends AppCompatActivity implements IIntentHandler, IActivityCreator {
    protected Patient patientInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientInfo = Patient.getPatient();
        retrieveIntent();
        setBinding();
        init();
        listenBtn();
    }

    public void navigateToNextActivity(Class<?> nextClass) {
        Intent intent = new Intent(this, nextClass);
        prepareIntent(intent);
        this.startActivity(intent);
        this.finish();
    }

    public void navigateToNextActivityWithoutFinish(Class<?> nextClass) {
        Intent intent = new Intent(this, nextClass);
        prepareIntent(intent);
        this.startActivity(intent);
    }

    protected void retrieveIntent() {
        Intent intent = this.getIntent();
        processReceivedIntent(intent);
    }

    @Override
    public void prepareIntent(Intent intent) {
    }

    @Override
    public void processReceivedIntent(Intent intent) {
    }
}
