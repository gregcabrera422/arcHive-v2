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

public class CustomNoteAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Note> notes;
    LayoutInflater inflater;

    public CustomNoteAdapter(Context mContext, ArrayList<Note> notes) {
      this.mContext = mContext;
      this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater==null)
        {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.custom_note_row,parent,false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        ImageView ivPriority = convertView.findViewById(R.id.ivPriority);
        TextView tvPriority = convertView.findViewById(R.id.tvPriority);

        tvTitle.setText(notes.get(position).getTitle());
        switch(notes.get(position).getPriority()){
            case "High":
                ivPriority.setImageResource(R.drawable.red_circle);
                tvPriority.setText("High Priority");
                break;
            case "Medium":
                ivPriority.setImageResource(R.drawable.orange_circle);
                tvPriority.setText("Medium Priority");
                break;
            case "Low":
                ivPriority.setImageResource(R.drawable.yellow_circle);
                tvPriority.setText("Low Priority");
                break;
        }
        notifyDataSetChanged();
        return convertView;
    }

}
