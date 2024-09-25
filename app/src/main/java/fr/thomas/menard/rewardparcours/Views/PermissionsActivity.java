package fr.thomas.menard.rewardparcours.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import fr.thomas.menard.rewardparcours.R;

public class PermissionsActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1;
    String patientID, caseID, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        getId();
        requestPermissionCamera();
    }

    private void getId(){
        Intent intent = getIntent();
        caseID = intent.getStringExtra("caseID");
        patientID = intent.getStringExtra("patientID");
        date = intent.getStringExtra("date");
    }

    private void requestPermissionCamera() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("You have to accept the permission to scan a QR code")
                    .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("patientID", patientID);
                intent.putExtra("caseID", caseID);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        }
    }
}
