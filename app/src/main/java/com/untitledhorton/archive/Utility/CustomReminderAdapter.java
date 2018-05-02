package com.untitledhorton.archive.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.untitledhorton.archive.Model.Note;
import com.untitledhorton.archive.R;

import java.util.ArrayList;

public class CustomReminderAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Note> reminders;
    LayoutInflater inflater;

    public CustomReminderAdapter(Context mContext, ArrayList<Note> reminders) {
      this.mContext = mContext;
      this.reminders = reminders;
    }

    @Override
    public int getCount() {
        return reminders.size();
    }

    @Override
    public Object getItem(int position) {
        return reminders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View v = View.inflate(mContext, R.layout.custom_note_row, null);

        if(inflater==null)
        {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.custom_note_row,parent,false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        ImageView ivNotification = (ImageView) convertView.findViewById(R.id.ivNotification);

        tvTitle.setText(reminders.get(position).getNote());
        ivNotification.setImageResource(R.drawable.ic_notifications_black_24dp);
        notifyDataSetChanged();
        return convertView;
    }

}
