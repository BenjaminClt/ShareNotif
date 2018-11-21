package bcleton.com.sharenotif.viewmodel;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bcleton.com.sharenotif.R;
import bcleton.com.sharenotif.model.Message;

public class MessageAdapter extends BaseAdapter {

    private List<Message> messages;
    private Activity activity;

    public MessageAdapter(Activity activity, List<Message> messages){
        this.messages = messages;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.message_row, parent, false);
        }

        Message currentMessage = (Message) getItem(position);

        TextView text = (TextView)convertView.findViewById(R.id.textMessage);
        TextView date = (TextView)convertView.findViewById(R.id.dateMessage);

        //sets the text for item name and item description from the current item object
        text.setText(currentMessage.getText());
        date.setText(currentMessage.getDateString());

        // returns the view for the current row
        return convertView;
    }

}
