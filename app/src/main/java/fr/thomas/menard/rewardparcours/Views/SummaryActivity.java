package fr.thomas.menard.rewardparcours.Views;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
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

import fr.thomas.menard.rewardparcours.BaseActivtiy.BaseActivity;
import fr.thomas.menard.rewardparcours.DataUploadUtils.DataTransfer;
import fr.thomas.menard.rewardparcours.DataUploadUtils.FileManager;
import fr.thomas.menard.rewardparcours.Model.Patient;
import fr.thomas.menard.rewardparcours.R;
import fr.thomas.menard.rewardparcours.Utils.SFTP;
import fr.thomas.menard.rewardparcours.databinding.ActivitySummaryBinding;

public class SummaryActivity extends BaseActivity {

    private ActivitySummaryBinding binding;
    private int numberPicRated = 0;
    private static final int total_pic = 30;

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
        binding.btnScanAnother.setOnClickListener(view -> {
            navigateToNextActivity(MainActivity.class);
        });
    }

    @SuppressLint("SetTextI18n")
    private void NbPictureRated(){
        String filePath = FileManager.getScoreFilename(this, Patient.getPatient());

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

        binding.txtNumberPic.setText(numberPicRated + " / " + total_pic+" pictures");
    }

    private void checkUpload(){
        if(numberPicRated==30){
            binding.PBUpload.setVisibility(View.VISIBLE);
            binding.btnScanAnother.setVisibility(View.GONE);
            uploadData();
        }
    }

    private void uploadData(){
        binding.PBUpload.post(() -> {
            binding.PBUpload.setVisibility(View.VISIBLE);
            binding.btnScanAnother.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                DataTransfer dataTransfer = new DataTransfer(patientInfo, this, IdentificationActivity.class,
                        () -> runOnUiThread(() -> binding.PBUpload.setVisibility(View.INVISIBLE)) // Hide progress when done
                );

                dataTransfer.uploadData();
            }, 100);
        });
    }

    @Override
    public void init() {
        NbPictureRated();
        diplayChart();
        checkUpload();
    }

    @Override
    public void listenBtn() {
        listenBtnScan();
    }

    @Override
    public void setBinding() {
        binding = ActivitySummaryBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }
}