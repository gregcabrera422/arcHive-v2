package com.untitledhorton.archive.Utility;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;
import com.untitledhorton.archive.AnnouncementActivity;
import com.untitledhorton.archive.CourseWorkActivity;
import com.untitledhorton.archive.Model.ClassCourse;
import com.untitledhorton.archive.R;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by Greg on 14/04/2018.
 */

public class ClassRecyclerAdapter extends RecyclerView.Adapter<ClassRecyclerAdapter.CVHolder>{

    List<ClassCourse> classCourses;
    Context context;
    public ClassRecyclerAdapter(List<ClassCourse>classCourses, Context context){
        this.classCourses = classCourses;
        this.context = context;
    }

    @Override
    public CVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_card, null);
        CVHolder cvh = new CVHolder(v);

        return cvh;
    }

    @Override
    public void onBindViewHolder(CVHolder customViewHolder, int i) {
        ClassCourse classCourse = classCourses.get(i);

        customViewHolder.tvCourse.setText(classCourse.getCourseName());
        customViewHolder.tvTeacher.setText(classCourse.getTeacherName());
        if(classCourse.getPhotoUrl().startsWith("http")){
            Picasso.get().load(classCourse.getPhotoUrl()).into(customViewHolder.ivProfile);
        }else{
            Picasso.get().load("http://"+classCourse.getPhotoUrl()).into(customViewHolder.ivProfile);
        }


        customViewHolder.tvCourse.setOnClickListener(clickListener);
        customViewHolder.tvCourse.setTag(customViewHolder);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CVHolder holder = (CVHolder) view.getTag();
            int position = holder.getPosition();
            ClassCourse classCourse = classCourses.get(position);

            final String classId = classCourse.getCourseId();
            final String className = classCourse.getCourseName();
            final String photoURL = classCourse.getPhotoUrl();
            final String courseTeacher = classCourse.getTeacherName();
            DialogPlus removeDialog = DialogPlus.newDialog(context)
                    .setExpanded(true, 150)
                    .setContentHolder(new ViewHolder(R.layout.course_dialog))
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(DialogPlus dialog, View view) {
                            switch(view.getId()){
                                case R.id.btnAnnouncements:
                                    Intent announcementIntent = new Intent(context, AnnouncementActivity.class);
                                    announcementIntent.putExtra("courseId", classId);
                                    announcementIntent.putExtra("courseName", className);
                                    announcementIntent.putExtra("photoURL", photoURL);
                                    announcementIntent.putExtra("courseTeacher", courseTeacher);
                                    context.startActivity(announcementIntent);
                                    dialog.dismiss();
                                    break;
                                case R.id.btnCourseWork:
                                    Intent courseWorkIntent = new Intent(context, CourseWorkActivity.class);
                                    courseWorkIntent.putExtra("courseId", classId);
                                    courseWorkIntent.putExtra("courseName", className);
                                    courseWorkIntent.putExtra("photoURL", photoURL);
                                    courseWorkIntent.putExtra("courseTeacher", courseTeacher);
                                    context.startActivity(courseWorkIntent);
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    })
                    .create();
            removeDialog.show();


        }
    };

    @Override
    public int getItemCount() {
        return (null != classCourses ? classCourses.size() : 0);

    }

    public class CVHolder extends RecyclerView.ViewHolder {
        protected AutofitTextView tvCourse;
        protected TextView tvTeacher;
        protected ImageView ivProfile;

        public CVHolder(View view) {
            super(view);
            this.tvCourse = view.findViewById(R.id.tvCourseName);
            this.tvTeacher = view.findViewById(R.id.tvTeacher);
            this.ivProfile = view.findViewById(R.id.ivProfile);

        }
    }}
