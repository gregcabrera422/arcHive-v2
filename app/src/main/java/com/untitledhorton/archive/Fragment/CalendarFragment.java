package com.untitledhorton.archive.Fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.untitledhorton.archive.R;
import com.untitledhorton.archive.Utility.FirebaseOperation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Greg on 10/03/2018.
 */

public class CalendarFragment extends Fragment implements ScreenShotable {

    private View Fragmentone_view;
    private Bitmap bitmap;

    private CompactCalendarView compactCalendar;
    private TextView lblMonth, lblDate, tvEmpty;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatMonthDay = new SimpleDateFormat("MMMM dd", Locale.getDefault());
    private ArrayList<String> eventNote;
    private ListView lvEvents;

    public static CalendarFragment newInstance() {
        CalendarFragment calFrag = new CalendarFragment();
        return calFrag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        compactCalendar = rootView.findViewById(R.id.compactcalendar_view);
        lblMonth = rootView.findViewById(R.id.lblMonth);
        lblDate = rootView.findViewById(R.id.lblDate);
        lvEvents = rootView.findViewById(R.id.lvEvents);
        tvEmpty = rootView.findViewById(R.id.tvEmpty);

        lblDate.setText("Notes for " + dateFormatMonthDay.format(Calendar.getInstance().getTime()));
        Date currentDate = new Date();
        lblMonth.setText(dateFormatMonth.format(currentDate));
        FirebaseOperation.retrieveNoteDates(compactCalendar);
        eventNote = new ArrayList<String>();

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT);
                eventNote.clear();
                lblDate.setText("Notes for " + dateFormatMonthDay.format(dateClicked));
                List<Event> events = compactCalendar.getEvents(dateClicked);
                for(int ctr = 0; ctr<events.size(); ctr++){
                    eventNote.add(events.get(ctr).getData().toString());
                }

                ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, eventNote);
                lvEvents.setEmptyView(tvEmpty);
                lvEvents.setAdapter(eventAdapter);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                lblMonth.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        return rootView;
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
                CalendarFragment.this.bitmap = bitmap;
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
