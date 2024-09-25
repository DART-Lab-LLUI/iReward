package fr.thomas.menard.rewardparcours.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.thomas.menard.rewardparcours.R;
import fr.thomas.menard.rewardparcours.Utils.SFTP;
import fr.thomas.menard.rewardparcours.databinding.ActivitySummaryBinding;

public class SummaryActivity extends AppCompatActivity {

    ActivitySummaryBinding binding;
    String patientID, caseID, date;
    int numberPicRated = 0;

    File folderSRC;
    String PATH_SERVER;
    private static final String host = "172.20.11.10";
    private static final int port = 22;
    private static final String user = "lli_admin";
    private static final String password = "0YVjglmuevOh";

    private static final int total_pic = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySummaryBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        init();
        NbPictureRated();
        diplayChart();
        listenBtnScan();
        checkUpload();
    }

    private void init(){
        Intent intent = getIntent();
        caseID = intent.getStringExtra("caseID");
        patientID = intent.getStringExtra("patientID");
        date = intent.getStringExtra("date");
    }

    private void diplayChart(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(numberPicRated, "Rated"));
        entries.add(new PieEntry(total_pic - numberPicRated, "Remaining"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.rgb("#1069B2"));
        ArrayList<Integer> colorss = new ArrayList<>();
        colorss.add(colors.get(0));
        colorss.add(colors.get(5));

        dataSet.setColors(colorss);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(getResources().getColor(R.color.white));
        binding.pieChart.setData(data);
        binding.pieChart.invalidate();



    }

    private void listenBtnScan(){
        binding.btnScanAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("patientID", patientID);
                intent.putExtra("caseID", caseID);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void NbPictureRated(){
        String filePath = getExternalFilesDir(null).getAbsolutePath() + "/"+patientID+"/"+patientID+"_score.csv";
        List<String[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int linenumber = 0;
            while ((line = reader.readLine()) != null) {
                linenumber++;
                if (linenumber <= 4) {
                    continue; // Skip the first three lines
                }
                String[] row = line.split(";");
                Log.d("TEST", "row " + row[2]);
                if(!row[2].equals("Nan"))
                    numberPicRated++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding.txtNumberPic.setText(String.valueOf(numberPicRated) + " / " + total_pic+" pictures");

    }

    private void checkUpload(){
        if(numberPicRated==30){
            binding.PBUpload.setVisibility(View.VISIBLE);
            binding.btnScanAnother.setVisibility(View.GONE);
            uploadData(patientID+"_score.csv");
        }
    }

    private void uploadData(String file){
        folderSRC = new File(getExternalFilesDir(null).getAbsolutePath() + "/"+patientID);
        PATH_SERVER = "data/raw_data/" + patientID + "/reward_path/" + date + "/";
        String file_to_send = folderSRC + "/" + file;
        
        SFTP sftp = new SFTP();
        sftp.connect(host, user, password, port, file_to_send, PATH_SERVER, success -> {
            if (success) {
                Log.d("TEST", "File uploaded successfully");
                binding.PBUpload.setVisibility(View.GONE);
                binding.linearUploadDone.setVisibility(View.VISIBLE);

            } else {
                Log.d("TEST", "File upload failed");
                binding.PBUpload.setVisibility(View.GONE);
                binding.linearUploadFailed.setVisibility(View.VISIBLE);


            }
        });

    }
}