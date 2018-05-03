package com.untitledhorton.archive.Model;

/**
 * Created by Greg on 09/02/2018.
 */

public class Note {
    private String id;
    private String note;
    private int day;
    private int month;
    private int year;

    public Note(){
    }

    public Note(String id, String note, int day, int month, int year) {
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


}
