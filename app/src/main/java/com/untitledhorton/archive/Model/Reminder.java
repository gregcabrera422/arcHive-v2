package com.untitledhorton.archive.Model;

/**
 * Created by Greg on 09/02/2018.
 */

public class Reminder {
    private String id;
    private String title;
    private String message;
    private String date;
    private String time;


    public Reminder(){
    }

    public Reminder(String id, String title, String message, String date, String time) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.date = date;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
