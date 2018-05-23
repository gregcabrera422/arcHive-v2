package com.untitledhorton.archive.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.api.client.util.DateTime;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.untitledhorton.archive.EditActivity;
import com.untitledhorton.archive.Model.Note;
import com.untitledhorton.archive.NoteDetailActivity;
import com.untitledhorton.archive.R;
import com.untitledhorton.archive.Utility.CustomNoteAdapter;
import com.untitledhorton.archive.Utility.FirebaseCommand;
import com.untitledhorton.archive.Utility.FirebaseOperation;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Greg on 10/03/2018.
 */

public class MonthLogFragment extends Fragment implements ScreenShotable, FirebaseCommand{

    private View Fragmentone_view;
    private Bitmap bitmap;

    private ArrayList<Note> notes;
    private TextView tvEmpty;
    private Spinner spinner;
    private SwipeMenuListView lvNotes;
    private CustomNoteAdapter noteAdapter;
    private ProgressBar pb;
    private EditText etNote;
    private String note, day, month, year, selectedMonth;

    public static MonthLogFragment newInstance() {
        MonthLogFragment monthLogFragment = new MonthLogFragment();
        return monthLogFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_monthlog, container, false);

        lvNotes = rootView.findViewById(R.id.lvReminders);
        pb = rootView.findViewById(R.id.pb);
        notes = new ArrayList<Note>();
        noteAdapter = new CustomNoteAdapter(getActivity(), notes);
        tvEmpty = rootView.findViewById(R.id.tvEmpty);
        spinner = rootView.findViewById(R.id.spinner);
        lvNotes.setEmptyView(tvEmpty);

        lvNotes.setAdapter(noteAdapter);
        swipeMenuCreator(lvNotes);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.months));
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(monthAdapter);

        Calendar cal = Calendar.getInstance();

        selectedMonth = new DateFormatSymbols().getMonths()[cal.get(Calendar.MONTH)];
        int spinnerPosition = monthAdapter.getPosition(selectedMonth);
        spinner.setSelection(spinnerPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("month selected: " + adapterView.getSelectedItem().toString());
                switch(adapterView.getSelectedItem().toString()){
                    case "January":
                        selectedMonth = "01";
                        break;
                    case "February":
                        selectedMonth = "02";
                        break;
                    case "March":
                        selectedMonth = "03";
                        break;
                    case "April":
                        selectedMonth = "04";
                        break;
                    case "May":
                        selectedMonth = "05";
                        break;
                    case "June":
                        selectedMonth = "06";
                        break;
                    case "July":
                        selectedMonth = "07";
                        break;
                    case "August":
                        selectedMonth = "08";
                        break;
                    case "September":
                        selectedMonth = "09";
                        break;
                    case "October":
                        selectedMonth = "10";
                        break;
                    case "November":
                        selectedMonth = "11";
                        break;
                    case "December":
                        selectedMonth = "12";
                        break;
                }
                FirebaseOperation.retrieveMonth(pb, notes, noteAdapter, tvEmpty, selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Note item = notes.get(position);

                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("note", item.getNote());
                intent.putExtra("priority", item.getPriority());
                getActivity().startActivity(intent);

            }
        });

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
                Note item = notes.get(position);
                switch (index) {
                    case 0:
                        Calendar cal = Calendar.getInstance();
                        final String moveKey = item.getId();

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int i, int i1, int i2) {
                                year = Integer.toString(i);
                                month = Integer.toString(i1 + 1);
                                day = Integer.toString(i2);

                                if(Integer.parseInt(day)<10){
                                    day = "0"+day;
                                }

                                if(Integer.parseInt(month)<10){
                                    month = "0"+month;
                                }
                                FirebaseOperation.moveNote(moveKey, day, month, year);
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();

                        break;
                    case 1:
                        final String editKey = item.getId();
                        final String editTitle = item.getTitle();
                        final String editNote = item.getNote();

                        Intent intent = new Intent(getActivity(), EditActivity.class);
                        intent.putExtra("editKey", editKey);
                        intent.putExtra("editTitle", editTitle);
                        intent.putExtra("editNote", editNote);
                        getActivity().startActivity(intent);
                        break;
                    case 2:
                        final String removeKey = item.getId();

                        DialogPlus removeDialog = DialogPlus.newDialog(getActivity())
                                .setHeader(R.layout.remove_note_header)
                                .setExpanded(true, 280)
                                .setContentHolder(new ViewHolder(R.layout.remove_note_dialog))
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(DialogPlus dialog, View view) {
                                        switch(view.getId()){
                                            case R.id.btnRemoveNote:
                                                FirebaseOperation.removeNote(removeKey);
                                                notes.clear();
                                                noteAdapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                                break;
                                            case R.id.btnNo:
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
                MonthLogFragment.this.bitmap = bitmap;
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

}
