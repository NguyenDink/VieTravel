package com.example.projecttravel.model;

public class ScheduleDetail {
    private int detail_id;
    private Location location;
    private String description;

    public ScheduleDetail(int detail_id, Location location, String description) {
        this.detail_id = detail_id;
        this.location = location;
        this.description = description;
    }

    public ScheduleDetail() {
    }

    public int getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(int detail_id) {
        this.detail_id = detail_id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
