package com.untitledhorton.archive.Utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.untitledhorton.archive.Model.CourseAnnouncement;
import com.untitledhorton.archive.R;
import java.util.List;

public class AnnouncementRecyclerAdapter extends RecyclerView.Adapter<AnnouncementRecyclerAdapter.CVHolder>{

    List<CourseAnnouncement> courseAnnouncements;
    Context context;

    public AnnouncementRecyclerAdapter(List<CourseAnnouncement> courseAnnouncements, Context context){
        this.courseAnnouncements = courseAnnouncements;
        this.context = context;
    }

    @Override
    public CVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_announcement_card, null);
        CVHolder cvh = new CVHolder(v);

        return cvh;
    }

    @Override
    public void onBindViewHolder(CVHolder customViewHolder, int i) {
        CourseAnnouncement courseAnnouncement = courseAnnouncements.get(i);

        customViewHolder.tvText.setText(courseAnnouncement.getAnnouncementText());
        customViewHolder.tvDate.setText(courseAnnouncement.getDate());
        customViewHolder.tvTeacherName.setText(courseAnnouncement.getTeacherName());
        if(courseAnnouncement.getPhotoUrl().startsWith("http")){
            Picasso.get().load(courseAnnouncement.getPhotoUrl()).into(customViewHolder.ivProfile);
        }else{
            Picasso.get().load("http://"+courseAnnouncement.getPhotoUrl()).into(customViewHolder.ivProfile);
        }
    }

    @Override
    public int getItemCount() {
        return (null != courseAnnouncements ? courseAnnouncements.size() : 0);

    }

    public class CVHolder extends RecyclerView.ViewHolder {
        protected TextView tvText;
        protected TextView tvDate;
        protected ImageView ivProfile;
        protected TextView tvTeacherName;


        public CVHolder(View view) {
            super(view);
            this.tvText = view.findViewById(R.id.tvText);
            this.tvDate = view.findViewById(R.id.tvDate);
            this.ivProfile = view.findViewById(R.id.ivProfile);
            this.tvTeacherName = view.findViewById(R.id.tvTeacher);
        }
    }}
