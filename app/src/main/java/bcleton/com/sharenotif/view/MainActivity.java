package bcleton.com.sharenotif.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bcleton.com.sharenotif.R;
import bcleton.com.sharenotif.data.FirebaseManager;
import bcleton.com.sharenotif.data.INotificationReceiver;
import bcleton.com.sharenotif.data.NotificationManager;
import bcleton.com.sharenotif.model.Channel;
import bcleton.com.sharenotif.model.Message;
import bcleton.com.sharenotif.viewmodel.ChannelAdapter;

public class MainActivity extends AppCompatActivity implements INotificationReceiver {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private TextView textViewTest;
    private ListView channelList;
    private ChannelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ShareNotif");

        textViewTest = (TextView)findViewById(R.id.textViewTest);
        channelList = (ListView)findViewById(R.id.channelList);
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
                intent.putExtra("ChannelName", NotificationManager.getChannels().get(position).getTitle());
                startActivity(intent);
            }
        });

        adapter = new ChannelAdapter(this, NotificationManager.getChannels());
        channelList.setAdapter(adapter);

        NotificationManager.addReceiver(this);

        checkSignIn();
    }

    private void checkSignIn() {
        if (!FirebaseManager.isSignedIn()) {
            FirebaseManager.signIn(this);
        }
        else if (FirebaseManager.isUserAdmin()){
            textViewTest.setText("LISTENING...");
        }
        else {
            textViewTest.setText("NOT LISTENING");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager.removeReceiver(this);
    }

    @Override
    public void onNotificationReceived(String author, String text, Date date) {
        textViewTest.setText(author + " " + text + " " + date.toString());
        Channel ch = NotificationManager.getChannel(author);
        ch.setUnreadedMessages(ch.getUnreadedMessages() + 1);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FirebaseManager.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Connexion réussi", Toast.LENGTH_SHORT).show();
                subscribeToTopic();
            } else {
                Toast.makeText(MainActivity.this, "Connexion refusée. Email ou mot de passe invalide.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("AllIn")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Souscription réussi au topic : AllIn !", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Souscription échoué au topic : AllIn ! TU NE RECEVRAS RIEN !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
