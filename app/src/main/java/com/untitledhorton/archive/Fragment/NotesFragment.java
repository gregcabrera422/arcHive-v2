package com.untitledhorton.archive.Fragment;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.melnykov.fab.FloatingActionButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.untitledhorton.archive.Model.Note;
import com.untitledhorton.archive.R;
import com.untitledhorton.archive.Utility.CustomNoteAdapter;
import com.untitledhorton.archive.Utility.FirebaseCommand;
import com.untitledhorton.archive.Utility.FirebaseOperation;
import java.util.ArrayList;
import java.util.Calendar;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Greg on 09/03/2018.
 */

public class NotesFragment extends Fragment implements ScreenShotable, FirebaseCommand, View.OnClickListener{

    private View Fragmentone_view;
    private Bitmap bitmap;

    private FloatingActionButton fab;
    private ArrayList<Note> notes;
    private SwipeMenuListView lvNotes;
    private CustomNoteAdapter noteAdapter;
    private ProgressBar pb;
    private EditText etNote;
    private String note;
    private int day, month, year;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public static NotesFragment newInstance() {
        NotesFragment notesFrag = new NotesFragment();
        return notesFrag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

        lvNotes = rootView.findViewById(R.id.lvReminders);
        pb = rootView.findViewById(R.id.pb);
        fab = rootView.findViewById(R.id.fab);
        notes = new ArrayList<Note>();
        fab.attachToListView(lvNotes);
        noteAdapter = new CustomNoteAdapter(getActivity(), notes);

        FirebaseOperation.retrieveNotes(pb, notes, noteAdapter);

        lvNotes.setAdapter(noteAdapter);
        fab.setOnClickListener(this);
        swipeMenuCreator(lvNotes);

        return rootView;
    }

    public void swipeMenuCreator(SwipeMenuListView lvReminders){

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem moveNote = new SwipeMenuItem(
                        getActivity());
                moveNote.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66, 0xff)));
                moveNote.setWidth(200);
                moveNote.setIcon(R.drawable.ic_date_range_black_32dp);
                moveNote.setTitleSize(18);
                moveNote.setTitleColor(Color.WHITE);
                menu.addMenuItem(moveNote);

                SwipeMenuItem editNote = new SwipeMenuItem(
                        getActivity());
                editNote.setBackground(new ColorDrawable(Color.rgb(0,0,205)));
                editNote.setWidth(200);
                editNote.setIcon(R.drawable.ic_edit_black_32dp);
                editNote.setTitleSize(18);
                editNote.setTitleColor(Color.WHITE);
                menu.addMenuItem(editNote);

                SwipeMenuItem deleteNote = new SwipeMenuItem(
                        getActivity());
                deleteNote.setBackground(new ColorDrawable(Color.rgb(237, 41, 57)));
                deleteNote.setWidth(200);
                deleteNote.setIcon(R.drawable.ic_delete_black_32dp);
                deleteNote.setTitleColor(Color.WHITE);
                deleteNote.setTitleSize(18);

                menu.addMenuItem(deleteNote);
            }
        };

        lvReminders.setMenuCreator(creator);
        lvReminders.setCloseInterpolator(new BounceInterpolator());

        lvReminders.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final Note item = notes.get(position);
                switch (index) {
                    case 0:
                        Calendar cal = Calendar.getInstance();
                        int y = cal.get(Calendar.YEAR);
                        int m = cal.get(Calendar.MONTH);
                        int d = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mDateSetListener, y, m, d);
                        datePickerDialog.show();

                        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                year = i;
                                month = i1 + 1;
                                day = i2;

                                if(month != 0 && day != 0 && year != 0) {
                                    FirebaseOperation.moveNote(item.getId(), day, month, year);
                                }
                            }
                        };
                        break;
                    case 1:
                        DialogPlus editDialog = DialogPlus.newDialog(getActivity())
                                .setHeader(R.layout.edit_note_header)
                                .setExpanded(true, 500)
                                .setContentHolder(new ViewHolder(R.layout.add_note_dialog))
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(DialogPlus dialog, View view) {
                                        etNote = dialog.getHolderView().findViewById(R.id.etNote);
                                        switch(view.getId()){
                                            case R.id.btnAddNote:
                                                note = etNote.getText().toString();
                                                FirebaseOperation.editNote(note, item.getId());
                                                notes.clear();
                                                noteAdapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                                break;
                                            case R.id.btnCancel:
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                })
                                .create();
                        editDialog.show();
                        break;
                    case 2:
                        DialogPlus removeDialog = DialogPlus.newDialog(getActivity())
                                .setHeader(R.layout.remove_note_header)
                                .setExpanded(true, 350)
                                .setContentHolder(new ViewHolder(R.layout.remove_note_dialog))
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(DialogPlus dialog, View view) {
                                        switch(view.getId()){
                                            case R.id.btnRemoveNote:
                                                FirebaseOperation.removeNote(item.getId());
                                                notes.clear();
                                                noteAdapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                                break;
                                            case R.id.btnCancel:
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                })
                                .create();
                        removeDialog.show();
                        break;
                }

                return false;
            }

        });
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
                DialogPlus addDialog = DialogPlus.newDialog(getActivity())
                        .setHeader(R.layout.add_note_header)
                        .setExpanded(true, 500)
                        .setContentHolder(new ViewHolder(R.layout.add_note_dialog))
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                etNote = dialog.getHolderView().findViewById(R.id.etNote);
                                switch(view.getId()){
                                    case R.id.btnAddNote:
                                        note = etNote.getText().toString();
                                        FirebaseOperation.insertNote(note);
                                        notes.clear();
                                        noteAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        break;
                                    case R.id.btnCancel:
                                        dialog.dismiss();
                                        break;
                                }

                            }
                        })
                        .setExpanded(true)
                        .create();
                addDialog.show();
                break;
        }
    }
}
