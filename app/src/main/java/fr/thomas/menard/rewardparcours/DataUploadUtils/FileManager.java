package fr.thomas.menard.rewardparcours.DataUploadUtils;

import android.content.Context;

import java.io.File;

import fr.thomas.menard.rewardparcours.Model.Patient;

public class FileManager {

    private static File createFolder(File parent, String child) {
        File directory = new File(parent, child);

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + directory.getAbsolutePath());
            }
        }
        return directory;
    }

    public static File getCaseIDFolder(Context context, Patient patient){
        String filename = patient.getCaseId();
        return new File(context.getExternalFilesDir(null), filename);
    }

    private static String getFilename(File file){
        return file.getAbsolutePath();
    }

    private static boolean isFileExists(File file){
        return file.exists();
    }

    public static File getSessionFolder(Context context, Patient patient) {
        // Get the external files directory specific to this app
        File baseDir = context.getExternalFilesDir(null);

        // Folder Structure CID / date_time / files

        // Construct the patient-case directory path
        File caseDir = createFolder(baseDir,patient.getCaseId());
        File dateDir = createFolder(caseDir, patient.getDate());

        // Construct the session path
        return createFolder(dateDir, "raw_data");
    }

    public static File getMinioExceptionFile(Context context, Patient patient){
        String filename = "minio_exception.txt";
        return new File(getSessionFolder(context, patient), filename);
    }

    public static File getScoreFile(Context context, Patient patient){
        String filename = patient.getPatientId() + "_score.csv";
        return new File(getSessionFolder(context, patient), filename);
    }

    public static String getScoreFilename(Context context, Patient patient){
        return getFilename(getScoreFile(context, patient));
    }

    public static boolean isScoreFileExist(Context context, Patient patient){
        return isFileExists(getScoreFile(context, patient));
    }
}
