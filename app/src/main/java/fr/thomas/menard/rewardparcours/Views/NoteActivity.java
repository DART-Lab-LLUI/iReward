package fr.thomas.menard.rewardparcours.Views;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.thomas.menard.rewardparcours.BaseActivtiy.BaseActivity;
import fr.thomas.menard.rewardparcours.DataUploadUtils.FileManager;
import fr.thomas.menard.rewardparcours.Model.Patient;
import fr.thomas.menard.rewardparcours.R;
import fr.thomas.menard.rewardparcours.databinding.ActivityNoteBinding;

public class NoteActivity extends BaseActivity {

    private String note;
    private int pictureID;

    ActivityNoteBinding binding;

    private void listenSeekBar(){
        binding.ANoteSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.ANoteBtnSubmit.setVisibility(View.VISIBLE);
                binding.ANoteTxtNote.setText("Rating : " + progress);
                note = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void displayPic(int pictureID){
        switch (pictureID){
            case 1:
                binding.ANoteImNote.setImageResource(R.drawable.pic1);
                break;
            case 2:
                binding.ANoteImNote.setImageResource(R.drawable.pic2);
                break;
            case 3:
                binding.ANoteImNote.setImageResource(R.drawable.pic3);
                break;
            case 4:
                binding.ANoteImNote.setImageResource(R.drawable.pic4);
                break;
            case 5:
                binding.ANoteImNote.setImageResource(R.drawable.pic5);
                break;
            case 6:
                binding.ANoteImNote.setImageResource(R.drawable.pic6);
                break;
            case 7:
                binding.ANoteImNote.setImageResource(R.drawable.pic7);
                break;
            case 8:
                binding.ANoteImNote.setImageResource(R.drawable.pic8);
                break;
            case 9:
                binding.ANoteImNote.setImageResource(R.drawable.pic9);
                break;
            case 10:
                binding.ANoteImNote.setImageResource(R.drawable.pic10);
                break;
            case 11:
                binding.ANoteImNote.setImageResource(R.drawable.pic11);
                break;
            case 12:
                binding.ANoteImNote.setImageResource(R.drawable.pic12);
                break;
            case 13:
                binding.ANoteImNote.setImageResource(R.drawable.pic13);
                break;
            case 14:
                binding.ANoteImNote.setImageResource(R.drawable.pic14);
                break;
            case 15:
                binding.ANoteImNote.setImageResource(R.drawable.pic15);
                break;
            case 16:
                binding.ANoteImNote.setImageResource(R.drawable.pic16);
                break;
            case 17:
                binding.ANoteImNote.setImageResource(R.drawable.pic17);
                break;
            case 18:
                binding.ANoteImNote.setImageResource(R.drawable.pic18);
                break;
            case 19:
                binding.ANoteImNote.setImageResource(R.drawable.pic19);
                break;
            case 20:
                binding.ANoteImNote.setImageResource(R.drawable.pic20);
                break;
            case 21:
                binding.ANoteImNote.setImageResource(R.drawable.pic21);
                break;
            case 22:
                binding.ANoteImNote.setImageResource(R.drawable.pic22);
                break;
            case 23:
                binding.ANoteImNote.setImageResource(R.drawable.pic23);
                break;
            case 24:
                binding.ANoteImNote.setImageResource(R.drawable.pic24);
                break;
            case 25:
                binding.ANoteImNote.setImageResource(R.drawable.pic25);
                break;
            case 26:
                binding.ANoteImNote.setImageResource(R.drawable.pic26);
                break;
            case 27:
                binding.ANoteImNote.setImageResource(R.drawable.pic27);
                break;
            case 28:
                binding.ANoteImNote.setImageResource(R.drawable.pic28);
                break;
            case 29:
                binding.ANoteImNote.setImageResource(R.drawable.pic29);
                break;
            case 30:
                binding.ANoteImNote.setImageResource(R.drawable.pic30);
                break;

            default:
                break;
        }
    }

    private void confirmVote(){
        new AlertDialog.Builder(this)
                .setTitle("Confirm your mark")
                .setMessage("Are you sure to give " + note + " points to this picture ?")
                .setPositiveButton("YES", (dialog, which) -> {
                    modifyCSV(pictureID, note,
                            FileManager.getScoreFilename(this, Patient.getPatient()));

                    navigateToNextActivity(SummaryActivity.class);
                })
                .setNegativeButton("NO", null)
                .create().show();
    }


    private void listenBtnSubmit(){
        binding.ANoteBtnSubmit.setOnClickListener(view -> confirmVote());
    }

    private void modifyCSV(int rowposition, String newValue, String filePath){
        int rowIndex = rowposition + 3; // Index de la ligne à modifier
        int columnIndex = 2; // Index de la colonne à modifier

        // Lire le contenu du fichier CSV dans une structure de données
        List<String[]> data = readCSV(filePath);

        // Vérifier que l'index de ligne et de colonne est valide
        if (rowIndex >= 0 && rowIndex < data.size()) {
            String[] row = data.get(rowIndex);
            if (columnIndex >= 0 && columnIndex < row.length) {
                // Modifier l'élément spécifique
                row[columnIndex] = newValue;
            }
        }else{
            Log.d("TEST", "ERRORR" + rowIndex + " data " + data.size());
        }

        // Écrire la structure de données modifiée dans le fichier CSV
        writeCSV(data, filePath);
    }

    private static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(";");
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static void writeCSV(List<String[]> data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                String line = String.join(";", row);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        displayPic(pictureID);
    }

    @Override
    public void listenBtn() {
        listenSeekBar();
        listenBtnSubmit();
    }

    @Override
    public void setBinding() {
        binding = ActivityNoteBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }

    @Override
    public void processReceivedIntent(Intent intent) {
        super.processReceivedIntent(intent);
        pictureID = intent.getIntExtra("pictureID", 0);
    }
}