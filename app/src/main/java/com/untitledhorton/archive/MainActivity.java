package com.untitledhorton.archive;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.untitledhorton.archive.Fragment.ClassFragment;
import com.untitledhorton.archive.Fragment.MonthLogFragment;
import com.untitledhorton.archive.Fragment.ProfileFragment;
import com.untitledhorton.archive.Fragment.CalendarFragment;
import com.untitledhorton.archive.Fragment.NotesFragment;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator viewAnimator;
    private LinearLayout linearLayout;
    private NotesFragment notesFrag;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesFrag = NotesFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, notesFrag)
                .commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, notesFrag, drawerLayout, this);
    }

    private void createMenuList() {
        SlideMenuItem menuItem = new SlideMenuItem("close", R.drawable.icn_close);
        list.add(menuItem);
        SlideMenuItem menuItem1 = new SlideMenuItem("one", R.drawable.notes_icon);
        list.add(menuItem1);
        SlideMenuItem menuItem2 = new SlideMenuItem("two", R.drawable.calendar_icon);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem("three", R.drawable.monthlog_icon);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem("four", R.drawable.classroom_icon);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem("five", R.drawable.profile_icon);
        list.add(menuItem5);
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_item_add:
                DialogPlus removeDialog = DialogPlus.newDialog(this)
                        .setHeader(R.layout.confirmation_header)
                        .setExpanded(true, 450)
                        .setContentHolder(new ViewHolder(R.layout.confirmation_dialog))
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                switch(view.getId()){
                                    case R.id.btnYes:
                                        FirebaseAuth.getInstance().signOut();

                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        break;
                                    case R.id.btnNo:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        })
                        .setGravity(Gravity.CENTER)
                        .create();
                removeDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case "close":
                return screenShotable;
            default:
                return replaceFragment(slideMenuItem,screenShotable, position);

        }
    }

    private ScreenShotable replaceFragment(Resourceble slideMenuItem,ScreenShotable screenShotable, int topPosition) {
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);
        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();

        switch (slideMenuItem.getName()){
            case "one":
                NotesFragment notesFrag  = NotesFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, notesFrag).commit();
                break;
            case "two":
                CalendarFragment calFrag = CalendarFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, calFrag).commit();
                break;
            case "three":
                MonthLogFragment monthLogFragment = MonthLogFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, monthLogFragment).commit();
                break;
            case "four":
                ClassFragment classFrag = ClassFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, classFrag).commit();
                break;
            case "five":
                ProfileFragment profileFrag = ProfileFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, profileFrag).commit();
                break;
            default:
                break;

        }

        return notesFrag;
    }


    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();
    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

}
