package fr.thomas.menard.rewardparcours.App;

import android.app.Application;

import fr.thomas.menard.rewardparcours.Utils.CustomExceptionHandler;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Set the crash log file path
        String crashLogFilePath = getExternalFilesDir(null) + "/crash_log.txt";

        // Set the custom uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(crashLogFilePath));
    }
}

