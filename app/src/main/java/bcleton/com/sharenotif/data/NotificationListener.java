package bcleton.com.sharenotif.data;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

public class NotificationListener extends NotificationListenerService {

    private final String TELEGRAM_PACK_NAME = "org.telegram.messenger";

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        Intent intent = new  Intent("bcleton.com.sharenotif");
        intent.putExtra("text", sbn.getNotification().tickerText);
        sendBroadcast(intent);

        if(sbn.getPackageName().equals(TELEGRAM_PACK_NAME)){

        }
    }

}
