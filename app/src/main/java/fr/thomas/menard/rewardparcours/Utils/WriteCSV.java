package fr.thomas.menard.rewardparcours.Utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriteCSV extends ViewModel {

    public static WriteCSV getInstance(@NonNull ViewModelStoreOwner owner) {

        return new ViewModelProvider(owner, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(WriteCSV.class);
    }


    public void createAndWriteCSV(String filePath, String patientID, String caseId, String time) {


        File file = new File(filePath);

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter with ';' as separator
            CSVWriter writer = new CSVWriter(outputfile, ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            List<String[]> summ = new ArrayList<String[]>();
            summ.add(new String[] { "Patient_ID", "Case_ID", "Time"});
            summ.add(new String[] { patientID, caseId, time});
            summ.add(new String[]{});
            summ.add(new String[] { "Number","Categorie", "Score"});
            summ.add(new String[] { "Picture 1","Music", "Nan"});
            summ.add(new String[] { "Picture 2","Music", "Nan"});
            summ.add(new String[] { "Picture 3","Luxury", "Nan"});
            summ.add(new String[] { "Picture 4","Food", "Nan"});
            summ.add(new String[] { "Picture 5","Food", "Nan"});
            summ.add(new String[] { "Picture 6","Luxury", "Nan"});
            summ.add(new String[] { "Picture 7","Food", "Nan"});
            summ.add(new String[] { "Picture 8","Luxury", "Nan"});
            summ.add(new String[] { "Picture 9","Food", "Nan"});
            summ.add(new String[] { "Picture 10","Food", "Nan"});
            summ.add(new String[] { "Picture 11","Luxury", "Nan"});
            summ.add(new String[] { "Picture 12","Food", "Nan"});
            summ.add(new String[] { "Picture 13","Luxury", "Nan"});
            summ.add(new String[] { "Picture 14","Food", "Nan"});
            summ.add(new String[] { "Picture 15","Food", "Nan"});
            summ.add(new String[] { "Picture 16","Music", "Nan"});
            summ.add(new String[] { "Picture 17","Luxury", "Nan"});
            summ.add(new String[] { "Picture 18","Music", "Nan"});
            summ.add(new String[] { "Picture 19","Luxury", "Nan"});
            summ.add(new String[] { "Picture 20","Music", "Nan"});
            summ.add(new String[] { "Picture 21","Music", "Nan"});
            summ.add(new String[] { "Picture 22","Music", "Nan"});
            summ.add(new String[] { "Picture 23","Luxury", "Nan"});
            summ.add(new String[] { "Picture 24","Luxury", "Nan"});
            summ.add(new String[] { "Picture 25","Food", "Nan"});
            summ.add(new String[] { "Picture 26","Music", "Nan"});
            summ.add(new String[] { "Picture 27","Music", "Nan"});
            summ.add(new String[] { "Picture 28","Food", "Nan"});
            summ.add(new String[] { "Picture 29","Luxury", "Nan"});
            summ.add(new String[] { "Picture 30","Music", "Nan"});
            writer.writeAll(summ);

            // closing writer connection
            writer.close();

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




    public boolean checkFileName(String outputFileName, String filePath) {
        boolean flag = false;
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();
        String files;

        if(!folder.exists()){
            folder.mkdirs();
            return false;
        }

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                if (files.equals(outputFileName)) {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

}
