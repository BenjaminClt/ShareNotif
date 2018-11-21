package bcleton.com.sharenotif.viewmodel;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import bcleton.com.sharenotif.R;
import bcleton.com.sharenotif.model.Channel;
import bcleton.com.sharenotif.databinding.ChannelRowBinding;

public class ChannelAdapter extends BaseAdapter {

    private List<Channel> channels;
    private Activity activity;

    public ChannelAdapter(Activity activity, List<Channel> channels){
        this.channels = channels;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return channels.size();
    }

    @Override
    public Object getItem(int position) {
        return channels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChannelRowBinding binding;
        if(convertView == null){
            convertView = LayoutInflater.from(activity).inflate(R.layout.channel_row, null);
            binding = DataBindingUtil.bind(convertView);
            convertView.setTag(binding);
        }else{
            binding = (ChannelRowBinding) convertView.getTag();
        }
        binding.setChannel(channels.get(position));
        return binding.getRoot();
    }
}
