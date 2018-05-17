package com.untitledhorton.archive.Model;

/**
 * Created by Greg on 09/02/2018.
 */

public class Note {
    private String id;
    private String title;
    private String note;
    private String priority;
    private String day;
    private String month;
    private String year;

    public Note(){
    }

    public Note(String id, String title, String note, String priority, String day, String month, String year) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.priority = priority;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


}
