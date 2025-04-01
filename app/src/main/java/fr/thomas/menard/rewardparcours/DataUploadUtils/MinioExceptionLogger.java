package fr.thomas.menard.rewardparcours.DataUploadUtils;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.thomas.menard.rewardparcours.Model.Patient;

public class MinioExceptionLogger {
    public static void logException(Context context, Patient patientInfo, Exception e) {
        File exceptionFile = FileManager.getMinioExceptionFile(context, patientInfo);

        if (exceptionFile == null) {
            System.err.println("Error: Could not get exception file.");
            return;
        }

        try (FileWriter writer = new FileWriter(exceptionFile, true)) { // Append mode
            writer.write("Exception occurred: " + e.toString() + "\n");
            for (StackTraceElement element : e.getStackTrace()) {
                writer.write("\tat " + element.toString() + "\n");
            }
            writer.write("\n"); // New line for separation
            System.out.println("Exception logged successfully.");
        } catch (IOException ioException) {
            System.err.println("Error writing to file: " + ioException.getMessage());
        }
    }
}
