package bcleton.com.sharenotif.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bcleton.com.sharenotif.R;
import bcleton.com.sharenotif.model.Message;
import bcleton.com.sharenotif.viewmodel.MessageAdapter;

public class ChannelActivity extends AppCompatActivity {

    private ListView messageList;
    private int channelId;
    private List<Message> messages = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        setTitle(savedInstanceState.getString("ChannelName"));

        channelId = savedInstanceState.getInt("ChannelId");

        messageList = (ListView)findViewById(R.id.messageList);
        MessageAdapter adapter = new MessageAdapter(this, messages);
        messageList.setAdapter(adapter);


        messages.add(new Message(new Date(), "Nous allons donc voir demain une confirmation si nous repartons à la hausse. Il se peut aussi que nous avons un scénario de stabilisation pour les deux prochains jours. C'est pour cela, que je vais attendre la confirmation."));
        messages.add(new Message(new Date(), "Voici pour la seconde photo avec les plus bas"));
    }
}
