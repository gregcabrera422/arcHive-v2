package com.untitledhorton.archive.Model;

/**
 * Created by Greg on 14/04/2018.
 */

public class Subjects {
    private String id;
    private String name;
    private String section;
    private String teacherName;

    public Subjects(String id, String name, String section, String teacherName) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.teacherName = teacherName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
