package com.untitledhorton.archive.Fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.untitledhorton.archive.Model.Note;
import com.untitledhorton.archive.R;
import com.untitledhorton.archive.Utility.CustomReminderAdapter;

import java.util.ArrayList;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Greg on 09/03/2018.
 */

public class NotesFragment extends Fragment implements ScreenShotable, View.OnClickListener{

    private View Fragmentone_view;
    private Bitmap bitmap;

    DatabaseReference mDatabase, insidemDatabase;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FloatingActionButton fab;
    private ArrayList<Note> notes;
    private SwipeMenuListView lvReminders;
    private CustomReminderAdapter reminderAdapter;
    private ProgressBar pb;
    private EditText etNote;

    public static NotesFragment newInstance() {
        NotesFragment notesFrag = new NotesFragment();
        return notesFrag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

        lvReminders = rootView.findViewById(R.id.lvReminders);
        pb = rootView.findViewById(R.id.pb);
        fab = rootView.findViewById(R.id.fab);
        notes = new ArrayList<Note>();
        fab.attachToListView(lvReminders);
        reminderAdapter = new CustomReminderAdapter(getActivity(), notes);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Note note;
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();

                    insidemDatabase = mDatabase.child(key.toString());
                    note = objSnapshot.getValue(Note.class);
                    note.setId(key.toString());
                    notes.add(note);
                    reminderAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

        lvReminders.setAdapter(reminderAdapter);
        fab.setOnClickListener(this);
        swipeMenuCreator(lvReminders);

        return rootView;
    }

    public void swipeMenuCreator(SwipeMenuListView lvReminders){

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getActivity());
                editItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                        0xff)));
                editItem.setWidth(200);
                editItem.setTitle("EDIT");
                editItem.setTitleSize(18);
                editItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(editItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());

                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));

                deleteItem.setWidth(200);
                deleteItem.setTitle("DELETE");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(18);

                menu.addMenuItem(deleteItem);
            }
        };

        lvReminders.setMenuCreator(creator);
        lvReminders.setCloseInterpolator(new BounceInterpolator());
    }

    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(Fragmentone_view.getWidth(),
                        Fragmentone_view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Fragmentone_view.draw(canvas);
                NotesFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.Fragmentone_view = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.fab:
                DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setHeader(R.layout.add_note_header)
                        .setInAnimation(R.anim.abc_fade_in)
                        .setContentHolder(new ViewHolder(R.layout.add_note_dialog))
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                etNote = dialog.getHolderView().findViewById(R.id.etNote);
                                switch(view.getId()){
                                    case R.id.btnAddNote:
                                        System.out.println("CLICK CLICK");
                                        System.out.println("note: " + etNote.getText().toString());
                                        dialog.dismiss();
                                    case R.id.btnCancel:
                                        dialog.dismiss();
                                }

                            }
                        })
                        .setExpanded(true)
                        .setCancelable(true)
                        .create();
                dialog.show();
                break;
        }

    }
}
