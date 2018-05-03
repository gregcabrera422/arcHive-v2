package com.untitledhorton.archive.Utility;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.untitledhorton.archive.Model.Note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 03/05/2018.
 */

public class FirebaseOperation implements FirebaseCommand {

    public static void retrieveNotes(final ProgressBar pb, final ArrayList<Note> notes, final CustomNoteAdapter noteAdapter){
        NOTES_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Note note;
                notes.clear();
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();

                    NOTES_TABLE.child(key.toString());
                    note = objSnapshot.getValue(Note.class);
                    note.setId(key.toString());
                    notes.add(note);

                    noteAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public static void insertNote(String note){
        HashMap<String, Object> result = new HashMap<>();
        result.put("note", note);
        result.put("day", 0);
        result.put("month", 0);
        result.put("year", 0);

        NOTES_TABLE.push().setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
//                    Toast.makeText(getActivity(), "Report failed to be sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void removeNote(String item){
        NOTES_TABLE.child(item).removeValue();
    }

    public static void moveNote(String item, int day, int month, int year){
        HashMap<String, Object> date = new HashMap<>();
        date.put("day", day);
        date.put("month", month);
        date.put("year", year);

        NOTES_TABLE.child(item).updateChildren(date);
    }

    public static void editNote(String note, String item){
        HashMap<String, Object> updatedNote = new HashMap<>();
        updatedNote.put("note", note);

        NOTES_TABLE.child(item).updateChildren(updatedNote);
    }
}
