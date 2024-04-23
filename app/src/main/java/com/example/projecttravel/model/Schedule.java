package com.example.projecttravel.model;

import java.util.List;

public class Schedule {
    private int schedule_id;
    private Account account;
    private String title;
    private List<ScheduleDetail> locations;

    public Schedule(int schedule_id, Account account, String title, List<ScheduleDetail> locations) {
        this.schedule_id = schedule_id;
        this.account = account;
        this.title = title;
        this.locations = locations;
    }

    public Schedule() {
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ScheduleDetail> getLocations() {
        return locations;
    }

    public void setLocations(List<ScheduleDetail> locations) {
        this.locations = locations;
    }
}
