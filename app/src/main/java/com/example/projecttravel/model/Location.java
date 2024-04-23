package com.example.projecttravel.model;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable {
    private int location_id;
    private String name;
    private LocationType type;
    private String title;
    private String images;
    private String description;
    private String address;

    public Location(int location_id, String name, LocationType type, String title, String images, String description, String address) {
        this.location_id = location_id;
        this.name = name;
        this.type = type;
        this.title = title;
        this.images = images;
        this.description = description;
        this.address = address;
    }

    public int getFavorite() {
        return 0;
    }

    public Location() {
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
