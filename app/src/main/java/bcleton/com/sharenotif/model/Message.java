package bcleton.com.sharenotif.model;

import android.icu.util.TimeZone;

import java.util.Calendar;
import java.util.Date;

public class Message implements Comparable<Message>{
    private Date date;
    private String text;
    private Calendar calendar;


    public Message(Date date, String text) {
        this.date = date;
        this.text = text;

        calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(java.util.TimeZone.getTimeZone("Europe/Paris"));
    }

    public String getDateString() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(java.util.TimeZone.getTimeZone("Europe/Paris"));

        if (calendar.get(Calendar.DAY_OF_MONTH) + 1 == now.get(Calendar.DAY_OF_MONTH)) {
            return "hier.";
        }
        else if (calendar.get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY)) {
            return String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        }

        return String.format("%02d/%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @Override
    public int compareTo(Message m) {
        return getDate().compareTo(m.getDate());
    }
}
