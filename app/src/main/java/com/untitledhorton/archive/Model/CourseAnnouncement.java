package com.untitledhorton.archive.Model;

/**
 * Created by Greg on 16/05/2018.
 */

public class CourseAnnouncement {

    private String AnnouncementText;
    private String date;

    public CourseAnnouncement(String AnnouncementText, String date) {
        this.AnnouncementText = AnnouncementText;
        this.date = date;
    }

    public String getAnnouncementText() {
        return AnnouncementText;
    }

    public void setAnnouncementText(String announcementText) {
        AnnouncementText = announcementText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
