package com.untitledhorton.archive.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.untitledhorton.archive.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Greg on 09/03/2018.
 */

public class CalendarFragment extends Fragment implements ScreenShotable{

    private View Fragmentone_view;
    private Bitmap bitmap;

    private TextView lblMonth;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    public static CalendarFragment newInstance() {
        CalendarFragment calFrag = new CalendarFragment();
        return calFrag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        compactCalendar = rootView.findViewById(R.id.compactcalendar_view);
        lblMonth = rootView.findViewById(R.id.lblMonth);
        Date currentDate = new Date();
        lblMonth.setText(dateFormatMonth.format(currentDate));

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT);
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
