package bcleton.com.sharenotif.data;

import java.util.Date;

public interface INotificationReceiver {
    void onNotificationReceived(String author, String text, Date date);
}
