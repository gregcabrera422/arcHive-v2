package com.untitledhorton.archive.Fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.untitledhorton.archive.R;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Greg on 10/03/2018.
 */

public class ClassFragment extends Fragment implements ScreenShotable{

    private View Fragmentone_view;
    private Bitmap bitmap;

    public static ClassFragment newInstance() {
        ClassFragment classFrag = new ClassFragment();
        return classFrag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_class, container, false);


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
                ClassFragment.this.bitmap = bitmap;
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
