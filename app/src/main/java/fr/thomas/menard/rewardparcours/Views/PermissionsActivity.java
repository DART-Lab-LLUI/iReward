package fr.thomas.menard.rewardparcours.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;

import fr.thomas.menard.rewardparcours.BaseActivtiy.BaseActivity;
import fr.thomas.menard.rewardparcours.R;

public class PermissionsActivity extends BaseActivity {

    private static final int CAMERA_PERMISSION_CODE = 1;

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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                navigateToNextActivity(MainActivity.class);
            }
        }
    }

    @Override
    public void init() {
        requestPermissionCamera();
    }

    @Override
    public void listenBtn() {

    }

    @Override
    public void setBinding() {
        setContentView(R.layout.activity_permissions);
    }
}
