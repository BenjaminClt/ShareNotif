package bcleton.com.sharenotif;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Calendar getDateNow() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(java.util.TimeZone.getTimeZone("Europe/Paris"));

        return now;
    }

    public static DateFormat getDateFormat() {
        return dateFormat;
    }

}
