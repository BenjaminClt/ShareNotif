package bcleton.com.sharenotif.view;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bcleton.com.sharenotif.R;
import bcleton.com.sharenotif.data.INotificationReceiver;
import bcleton.com.sharenotif.data.NotificationManager;
import bcleton.com.sharenotif.model.Message;
import bcleton.com.sharenotif.viewmodel.MessageAdapter;

public class ChannelActivity extends AppCompatActivity implements INotificationReceiver {

    private ListView messageList;
    private String author;
    private List<Message> messages;
    private MessageAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager.removeReceiver(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        author = getIntent().getExtras().getString("ChannelName");

        setContentView(R.layout.activity_channel);
        setTitle(author);

        messageList = (ListView)findViewById(R.id.messageList);
        messages = NotificationManager.getChannel(author).getMessages();
        NotificationManager.getChannel(author).setUnreadedMessages(0);

        adapter = new MessageAdapter(this, messages);
        messageList.setAdapter(adapter);

        NotificationManager.addReceiver(this);
    }

    @Override
    public void onNotificationReceived(String author, String text, Date date) {
        adapter.notifyDataSetChanged();
    }
}
