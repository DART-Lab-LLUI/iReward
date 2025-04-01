package fr.thomas.menard.rewardparcours.Utils;

import android.os.Looper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class is for some additional feature, such as: check Bluetooth adapter, check location premission...etc.
 */
public class Utils {

    /**
     * Check the current thread is main thread or background thread.
     *
     * @return True - If running on main thread
     */
    public static boolean isMainThread() {

        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static String changeDateFormatFromYMDToDMY(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        Date newDate = null;
        try {
            newDate = inputFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return outputFormat.format(newDate);
    }

}
