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
import com.untitledhorton.archive.Model.CourseWorks;
import com.untitledhorton.archive.R;

import java.util.List;

public class CourseWorkRecyclerAdapter extends RecyclerView.Adapter<CourseWorkRecyclerAdapter.CVHolder>{

    List<CourseWorks> courseWorks;
    Context context;

    public CourseWorkRecyclerAdapter(List<CourseWorks> courseWorks, Context context){
        this.courseWorks = courseWorks;
        this.context = context;
    }

    @Override
    public CVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_work_card, null);
        CVHolder cvh = new CVHolder(v);

        return cvh;
    }

    @Override
    public void onBindViewHolder(CVHolder customViewHolder, int i) {
        CourseWorks courseWork = courseWorks.get(i);

        customViewHolder.tvTitle.setText(courseWork.getTitle());
        customViewHolder.tvText.setText(courseWork.getCourseText());
        customViewHolder.tvDate.setText(courseWork.getDate());
        customViewHolder.tvTeacherName.setText(courseWork.getTeacherName());
        if(courseWork.getPhotoUrl().startsWith("http")){
            Picasso.get().load(courseWork.getPhotoUrl()).into(customViewHolder.ivProfile);
        }else{
            Picasso.get().load("http://"+courseWork.getPhotoUrl()).into(customViewHolder.ivProfile);
        }
    }

    @Override
    public int getItemCount() {
        return (null != courseWorks ? courseWorks.size() : 0);

    }

    public class CVHolder extends RecyclerView.ViewHolder {
        protected TextView tvTitle;
        protected TextView tvText;
        protected TextView tvDate;
        protected ImageView ivProfile;
        protected TextView tvTeacherName;


        public CVHolder(View view) {
            super(view);
            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvText = view.findViewById(R.id.tvText);
            this.tvDate = view.findViewById(R.id.tvDate);
            this.ivProfile = view.findViewById(R.id.ivProfile);
            this.tvTeacherName = view.findViewById(R.id.tvTeacher);
        }
    }}
