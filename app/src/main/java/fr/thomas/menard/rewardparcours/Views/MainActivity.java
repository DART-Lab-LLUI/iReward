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
import java.util.List;

import fr.thomas.menard.rewardparcours.R;

public class MainActivity extends AppCompatActivity {

    CodeScannerView scannerView;

    private CodeScanner codeScanner;
    private int CAMERA_PERMISSION_CODE = 1;
    String patientID, caseID, date, categorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inits();
        checkPermissions();
    }

    private void checkPermissions(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            scanCode();
        }
    }

    private void inits(){
        scannerView = findViewById(R.id.AMain_scanner);
        codeScanner = new CodeScanner(getApplicationContext(), scannerView);

        Intent intent = getIntent();
        caseID = intent.getStringExtra("caseID");
        patientID = intent.getStringExtra("patientID");
        date = intent.getStringExtra("date");
    }

    private void scanCode(){
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            String numberPart = String.valueOf(result).replace("picture", "");
            int pictureID = Integer.parseInt(numberPart);
            if(checkPicRated(pictureID)){
                popupAlreadyScanned();
            }else{
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra("patientID", patientID);
                intent.putExtra("caseID", caseID);
                intent.putExtra("date", date);
                intent.putExtra("pictureID", pictureID);
                startActivity(intent);
                finish();
            }

        }));
        scannerView.setOnClickListener(v -> codeScanner.startPreview());
    }

    private boolean checkPicRated(int pic){
        String score_csv_path = getExternalFilesDir(null).getAbsolutePath() + "/"+patientID+"/"+patientID+"_score.csv";

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
                .setMessage("You have already scanned this picture. Please scan antoher QR code")
                .setPositiveButton("GET IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("patientID", patientID);
                        intent.putExtra("caseID", caseID);
                        intent.putExtra("date", date);
                        startActivity(intent);
                        finish();
                    }
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
}