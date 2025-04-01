package fr.thomas.menard.rewardparcours.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import fr.thomas.menard.rewardparcours.BaseActivtiy.BaseActivity;
import fr.thomas.menard.rewardparcours.DataUploadUtils.FileManager;
import fr.thomas.menard.rewardparcours.Model.Patient;
import fr.thomas.menard.rewardparcours.R;
import fr.thomas.menard.rewardparcours.Utils.DebugLogger;

public class MainActivity extends BaseActivity {

    CodeScannerView scannerView;
    private CodeScanner codeScanner;
    private int pictureID;


    private void checkPermissions(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            scanCode();
        }
    }

    private void inits(){
        scannerView = findViewById(R.id.AMain_scanner);
        codeScanner = new CodeScanner(getApplicationContext(), scannerView);
    }

    private void scanCode(){
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            String numberPart = String.valueOf(result).replace("picture", "");
            pictureID = Integer.parseInt(numberPart);
            DebugLogger.debugLog(numberPart + " " + pictureID);
            if(checkPicRated(pictureID)){
                popupAlreadyScanned();
            }else{
                navigateToNextActivity(NoteActivity.class);
            }

        }));
        scannerView.setOnClickListener(v -> codeScanner.startPreview());
    }

    private boolean checkPicRated(int pic){
        String score_csv_path = FileManager.getScoreFilename(this, Patient.getPatient());

        try {
            CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();

            // Create a CSVReader with FileReader and custom CSVParser
            CSVReader reader = new CSVReaderBuilder(new FileReader(score_csv_path))
                    .withCSVParser(csvParser)
                    .build();


            List<String[]> csvEntries = reader.readAll();
            int rowIndex = pic + 3;
            String[] row = csvEntries.get(rowIndex);

            reader.close();

            if(row[2].equals("Nan"))
                return false;
            else
                return true;

        } catch (IOException | CsvException e) {
            Log.d("TEST", "infos " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    private void popupAlreadyScanned(){
        new AlertDialog.Builder(this)
                .setTitle("Picture already marked")
                .setMessage("You have already scanned this picture. Please scan another QR code")
                .setPositiveButton("GET IT", (dialog, which) -> {
                    navigateToNextActivity(MainActivity.class);
                })
                .create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onStop() {
        codeScanner.releaseResources();
        super.onStop();
    }

    @Override
    public void init() {
        inits();
        checkPermissions();
    }

    @Override
    public void listenBtn() {

    }

    @Override
    public void setBinding() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void prepareIntent(Intent intent) {
        super.prepareIntent(intent);
        intent.putExtra("pictureID", pictureID);
    }
}