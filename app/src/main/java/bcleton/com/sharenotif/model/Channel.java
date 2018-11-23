package bcleton.com.sharenotif.model;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import bcleton.com.sharenotif.BR;
import bcleton.com.sharenotif.view.ChannelActivity;

public class Channel extends BaseObservable {
    private String title;
    private int unreadedMessages = 0;
    private List<Message> messages;
    private Context context;

    public Channel(String title) {
        this.title = title;

        messages = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    @Bindable
    public int getUnreadedMessages() {
        return unreadedMessages;
    }

    @Bindable
    public Message getLastestMessage() {
        if (messages.size() > 0) {
            Collections.sort(messages);
            return messages.get(messages.size() - 1);
        }

        return null;
    }

    @Bindable
    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message m) {
        messages.add(m);
        notifyPropertyChanged(BR.messages);
        notifyPropertyChanged(BR.lastestMessage);
    }

    public void setUnreadedMessages(int count) {
        unreadedMessages = count;
        notifyPropertyChanged(BR.unreadedMessages);
    }
}
