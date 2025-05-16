package fr.thomas.menard.rewardparcours.DataUploadUtils;

import static fr.thomas.menard.rewardparcours.BuildConfig.MINIO_HS_ACCESS;
import static fr.thomas.menard.rewardparcours.BuildConfig.MINIO_HS_BUCKET;
import static fr.thomas.menard.rewardparcours.BuildConfig.MINIO_HS_ENDPOINT;
import static fr.thomas.menard.rewardparcours.BuildConfig.MINIO_HS_SECRET;
import static fr.thomas.menard.rewardparcours.BuildConfig.MINIO_VZ_ACCESS;
import static fr.thomas.menard.rewardparcours.BuildConfig.MINIO_VZ_BUCKET;
import static fr.thomas.menard.rewardparcours.BuildConfig.MINIO_VZ_ENDPOINT;
import static fr.thomas.menard.rewardparcours.BuildConfig.MINIO_VZ_SECRET;
import static fr.thomas.menard.rewardparcours.DataUploadUtils.FileManager.getSessionFolder;

import android.app.AlertDialog;

import fr.thomas.menard.rewardparcours.Model.Patient;
import fr.thomas.menard.rewardparcours.BaseActivtiy.BaseActivity;


public class DataTransfer {
    public interface TransferCallback {
        void onTransferComplete();
    }

    private TransferCallback callback;
    private MinioHelper minioHelper;
    private final BaseActivity mainActivity;
    private final Class<?> nextActivity;
    private final Patient patientInfo;

    public DataTransfer(Patient patientInfo, BaseActivity mainActivity, Class<?> nextActivity, TransferCallback callback) {
        this.patientInfo = patientInfo;
        this.mainActivity = mainActivity;
        this.nextActivity = nextActivity;
        this.callback = callback;

        try{
            switch (patientInfo.getClinicId()){
                case 0:
                    this.minioHelper = new MinioHelper(MINIO_HS_ENDPOINT, MINIO_HS_ACCESS, MINIO_HS_SECRET, MINIO_HS_BUCKET);
                    break;
                case 1:
                    this.minioHelper = new MinioHelper(MINIO_VZ_ENDPOINT, MINIO_VZ_ACCESS, MINIO_VZ_SECRET, MINIO_VZ_BUCKET);
                    break;
            }
        } catch (Exception e){
            tryAgainMessage(e);
        }
    }

    public MinioHelper getMinioClient() {
        return minioHelper;
    }

    public void uploadData() {
        new Thread(this::runMinio).start();
    }

    private void runMinio(){
        try{
            minioHelper.sendFolderToMinio(getSessionFolder(mainActivity, patientInfo), patientInfo, (uploadStatus, statusMsg) -> {
                mainActivity.runOnUiThread(() -> {

                    int totalFiles = uploadStatus[0];
                    int successfulUploads = uploadStatus[1];

                    if ((totalFiles == 0) || (successfulUploads == totalFiles))  {
                        logoutMessage();
                    }else {
                        tryAgainMessage(new Exception(statusMsg));
                    }

                    if (callback != null) {
                        callback.onTransferComplete();
                    }
                });
            });
        } catch (Exception e){
            tryAgainMessage(e);

            if (callback != null) {
                callback.onTransferComplete();
            }
        }
    }


    private void logoutMessage(){
        new AlertDialog.Builder(mainActivity)
                .setTitle("All Files successfully uploaded")
                .setMessage("You are going to be logged out")
                .setPositiveButton("Ok", ((dialogInterface, i) -> mainActivity.navigateToNextActivity(nextActivity)))
                .show();
    }

    private void tryAgainMessage(Exception e) {

        MinioExceptionLogger.logException(mainActivity, patientInfo, e);

        new AlertDialog.Builder(mainActivity)
                .setTitle("Error in Uploading. Try again?")
                .setMessage("Do you want to try uploading the data again or transfer it manually?\nPlease, check if you are in the correct WLAN.\nWhen choosing second, you will be logged out.")
                .setPositiveButton("Try Again", (dialog, which) -> runMinio())
                .setNegativeButton("Manually", ((dialogInterface, i) -> mainActivity.navigateToNextActivity(nextActivity)))
                .show();
    }
}
