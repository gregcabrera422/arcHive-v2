package com.untitledhorton.archive.Model;

/**
 * Created by Greg on 16/05/2018.
 */

public class ClassCourse {

    private String courseId;
    private String courseName;
    private String teacherName;
    private String photoUrl;

    public ClassCourse(String courseId, String courseName, String teacherName, String photoUrl) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.photoUrl = photoUrl;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
