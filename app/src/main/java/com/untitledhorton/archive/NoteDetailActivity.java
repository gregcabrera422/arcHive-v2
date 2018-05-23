package com.untitledhorton.archive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NoteDetailActivity extends AppCompatActivity {

    String title, note, priority;
    TextView tvTitle, tvNote, tvPriority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvNote = findViewById(R.id.tvNote);
        tvPriority = findViewById(R.id.tvPriority);

        Bundle extras = getIntent().getExtras();
        title = extras.getString("title");
        note = extras.getString("note");
        priority = extras.getString("priority");

        tvTitle.setText(title);
        tvNote.setText(note);
        tvPriority.setText("Priority: " + priority);

    }
}
