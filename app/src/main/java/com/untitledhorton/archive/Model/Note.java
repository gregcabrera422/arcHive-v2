package com.untitledhorton.archive.Model;

/**
 * Created by Greg on 09/02/2018.
 */

public class Note {
    private String id;
    private String note;
    private String day;
    private String month;
    private String year;

    public Note(){
    }

    public Note(String id, String note, String day, String month, String year) {
        this.id = id;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
