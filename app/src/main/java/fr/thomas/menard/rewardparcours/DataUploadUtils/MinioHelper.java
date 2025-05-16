package fr.thomas.menard.rewardparcours.DataUploadUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import fr.thomas.menard.rewardparcours.Model.Patient;
import fr.thomas.menard.rewardparcours.Utils.DebugLogger;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

public class MinioHelper {
    private static final String APPNAME = "ireward";
    private MinioClient minioClient;
    private final String MINIO_ENDPOINT, MINIO_ACCESS, MINIO_SECRET, MINIO_BUCKET;

    public MinioHelper(String endpoint, String access, String secret, String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        this.MINIO_ENDPOINT = endpoint;
        this.MINIO_ACCESS = access;
        this.MINIO_SECRET = secret;
        this.MINIO_BUCKET = bucket;

        // Initialize the MinIO client
        minioClient = MinioClient.builder()
                .endpoint(MINIO_ENDPOINT)
                .credentials(MINIO_ACCESS, MINIO_SECRET)
                .build();

        if (minioClient != null) {
            DebugLogger.debugLog("MINIOTEST","MinioClient initialized successfully.");
        } else {
            DebugLogger.debugLog("MINIOTEST","Failed to initialize MinioClient.");
        }

        // Check if bucket exists
        DebugLogger.debugLog("MINIOTEST", "Checking if landingzone bucket exists");
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(MINIO_BUCKET).build());
        if (found) {
            DebugLogger.debugLog("MINIOTEST", "MinIO client built successfully! Bucket exists.");
        } else {
            DebugLogger.debugLog("MINIOTEST", "Bucket not found, but MinIO client is working.");
        }
    }

    public void sendFolderToMinio(File folder, Patient patient, MinioUploadCallback callback) {
        new Thread(() -> {
            int[] uploadStatus = {0, 0, 0}; // [totalFiles, successfulUploads, failedUploads]
            String statusMessage = "Error in uploading Folder to Minio";

            if (folder.exists() && folder.isDirectory()) {
                String customMinioPath = APPNAME + "/" + patient.getClinicIdtoString()  + "/" + patient.getCaseId() + "/" + patient.getDate();

                // Recursively upload all files and subfolders
                try {
                    uploadFolderToMinio(folder, folder.getAbsolutePath(), customMinioPath, uploadStatus);
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onUploadComplete(uploadStatus, e.getMessage());  // Return the upload status
                    }
                }

                // Generate success message based on the result
                if (uploadStatus[0] == 0) {
                    statusMessage = "No files to upload.";
                } else if (uploadStatus[0] == uploadStatus[1]) {
                    statusMessage = "All files uploaded successfully.";
                } else if (uploadStatus[1] > 0) {
                    statusMessage = uploadStatus[1] + " out of " + uploadStatus[0] + " files uploaded successfully.";
                } else {
                    statusMessage = "None of the files could be uploaded.";
                }
            } else {
                statusMessage = "The specified folder does not exist or is not a directory.";
            }

            if (callback != null) {
                callback.onUploadComplete(uploadStatus, statusMessage);  // Return the upload status
            }
        }).start();
    }

    private void uploadFolderToMinio(File folder, String rootFolderPath, String customMinioPath, int[] uploadStatus) throws Exception {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // Increment total files count
                    uploadStatus[0]++;

                    // Compute the relative path of the file (to simulate folder structure)
                    String relativePath = file.getAbsolutePath().substring(rootFolderPath.length() + 1).replace("\\", "/");

                    // Prepend the custom folder path (iGait/01/{fid}/{time}) to the relative path
                    String objectName = customMinioPath + "/" + relativePath;

                    DebugLogger.debugLog("MINIOTEST", "Uploading: " + objectName);
                    boolean success = uploadFileToMinio(file, objectName);
                    if (success) {
                        uploadStatus[1]++; // Increment successful uploads count
                    } else {
                        uploadStatus[2]++; // Increment failed uploads count
                    }
                } else if (file.isDirectory()) {
                    // Recursively upload files from subdirectories
                    uploadFolderToMinio(file, rootFolderPath, customMinioPath, uploadStatus);
                }
            }
        }
    }

    private boolean uploadFileToMinio(File file, String objectName) throws Exception {
        // Convert the file content to a byte array
        byte[] fileContent = Files.readAllBytes(file.toPath());

        // Upload the file to MinIO using the relative path as the object name
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(MINIO_BUCKET)
                        .object(objectName)  // Use the relative path (with folder) as the object name
                        .stream(new ByteArrayInputStream(fileContent), fileContent.length, -1)
                        .contentType("application/octet-stream") // Optionally, adjust content type based on file
                        .build()
        );

        DebugLogger.debugLog("MINIOTEST", "Uploaded: " + objectName);
        return true;
    }

    public interface MinioUploadCallback {
        void onUploadComplete(int[] uploadStatus, String statusMessage); // Will return the [totalFiles, successfulUploads, failedUploads]
    }

}