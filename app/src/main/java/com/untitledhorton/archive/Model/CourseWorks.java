package com.untitledhorton.archive.Model;

/**
 * Created by Greg on 16/05/2018.
 */

public class CourseWorks {

    private String title;
    private String courseText;
    private String date;
    private String teacherName;
    private String photoUrl;


    public CourseWorks(String title, String courseText, String date, String teacherName, String photoUrl) {
        this.title = title;
        this.courseText = courseText;
        this.date = date;
        this.teacherName = teacherName;
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseText() {
        return courseText;
    }

    public void setCourseText(String courseText) {
        courseText = courseText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
