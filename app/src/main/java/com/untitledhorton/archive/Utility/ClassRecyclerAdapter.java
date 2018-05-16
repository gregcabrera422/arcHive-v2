package com.untitledhorton.archive.Utility;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.untitledhorton.archive.CourseDetailActivity;
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

        customViewHolder.tvCourse.setOnClickListener(clickListener);
        customViewHolder.tvCourse.setTag(customViewHolder);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CVHolder holder = (CVHolder) view.getTag();
            int position = holder.getPosition();
            ClassCourse classCourse = classCourses.get(position);

            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("courseId", classCourse.getCourseId());
            context.startActivity(intent);
        }
    };

    @Override
    public int getItemCount() {
        return (null != classCourses ? classCourses.size() : 0);

    }

    public class CVHolder extends RecyclerView.ViewHolder {
        protected AutofitTextView tvCourse;

        public CVHolder(View view) {
            super(view);
            this.tvCourse = view.findViewById(R.id.tvCourseName);
        }
    }}
