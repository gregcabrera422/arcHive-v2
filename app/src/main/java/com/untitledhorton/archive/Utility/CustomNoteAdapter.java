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

    public CustomNoteAdapter(Context mContext, ArrayList<Note> reminders) {
      this.mContext = mContext;
      this.notes = reminders;
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
        //View v = View.inflate(mContext, R.layout.custom_note_row, null);

        if(inflater==null)
        {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.custom_note_row,parent,false);
        }

        TextView tvNote = (TextView) convertView.findViewById(R.id.tvNote);

        tvNote.setText(notes.get(position).getNote());
        notifyDataSetChanged();
        return convertView;
    }

}
