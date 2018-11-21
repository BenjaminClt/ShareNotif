package bcleton.com.sharenotif.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import bcleton.com.sharenotif.R;
import bcleton.com.sharenotif.data.FirebaseManager;
import bcleton.com.sharenotif.data.NotificationListener;
import bcleton.com.sharenotif.model.Channel;
import bcleton.com.sharenotif.viewmodel.ChannelAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";


    private ListView channelList;
    private List<Channel> channels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ShareNotif");

        channelList = (ListView)findViewById(R.id.channelList);
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
                intent.putExtra("ChannelId", position);
                intent.putExtra("ChannelName", channels.get(position).getTitle());
                startActivity(intent);
            }
        });

        channels = new ArrayList<Channel>();

        ChannelAdapter adapter = new ChannelAdapter(this, channels);
        channelList.setAdapter(adapter);

        checkSignIn();
    }

    private void checkSignIn() {
        if (!FirebaseManager.isSignedIn()) {
            FirebaseManager.signIn(this);
        }
        else if (FirebaseManager.isUserAdmin()){
            startNotificationListener();
        }
    }

    private void startNotificationListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("bcleton.com.sharenotif");
        NotificationReceiver nr = new NotificationReceiver();

        registerReceiver(nr,filter);
    }

    private void interceptNotification(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("text");
            interceptNotification(text);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FirebaseManager.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Connexion réussi", Toast.LENGTH_SHORT).show();
                FirebaseMessaging.getInstance().subscribeToTopic("AllIn")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Souscription réussi au topic : AllIn !", Toast.LENGTH_SHORT).show();
                                    if (FirebaseManager.isUserAdmin()){
                                        startNotificationListener();
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Souscription échoué au topic : AllIn ! TU NE RECEVRAS RIEN !!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(MainActivity.this, "Connexion refusée. Email ou mot de passe invalide.", Toast.LENGTH_SHORT).show();
                IdpResponse response = IdpResponse.fromResultIntent(data);
            }
        }
    }
}
