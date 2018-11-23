package bcleton.com.sharenotif.data;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bcleton.com.sharenotif.Utils;
import bcleton.com.sharenotif.model.Channel;
import bcleton.com.sharenotif.model.Message;

public class NotificationManager extends NotificationListenerService {

    private final String TELEGRAM_PACK_NAME = "org.telegram.messenger";

    private static android.app.NotificationManager instance;
    private static List<INotificationReceiver> receivers = new ArrayList<>();
    private static List<Channel> channels = new ArrayList<>();

    public static void addNotifcation(String author, String text, Date date) {
        Channel channel = getChannel(author);

        if (channel == null) {
            channel = new Channel(author);
            channels.add(channel);
        }

        channel.addMessage(new Message(date, text));
    }

    public static List<Channel> getChannels() {
        return channels;
    }

    public static Channel getChannel(String author) {
        for(Channel channel: channels) {
            if (channel.getTitle().equals(author)) {
                return channel;
            }
        }

        return null;
    }

    public static void addReceiver(INotificationReceiver receiver) {
        receivers.add(receiver);
    }

    public static void removeReceiver(INotificationReceiver receiver) {
        receivers.remove(receiver);
    }

    private void fireNotificationReceived(String author, String text, Date date) {
        addNotifcation(author, text, date);

        for(INotificationReceiver receiver : receivers) {
            receiver.onNotificationReceived(author, text, date);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        if(sbn.getPackageName().equals(TELEGRAM_PACK_NAME)){
            Parcelable notifs[] = (Parcelable[])sbn.getNotification().extras.get(Notification.EXTRA_MESSAGES);
            String author = removeMessageCount(sbn.getNotification().extras.getString("android.conversationTitle", "ERROR NO AUTHOR"));

            if(notifs != null && notifs.length > 0){
                Bundle notifBundle = (Bundle)notifs[notifs.length - 1];
                fireNotificationReceived(author, notifBundle.getString("text", "ERROR NO TEXT"), Utils.getDateNow().getTime());
            }
        }
    }

    private String removeMessageCount(String message) {
        return message.replaceAll("\\(([^)]+)\\)", "").trim();
    }
}
