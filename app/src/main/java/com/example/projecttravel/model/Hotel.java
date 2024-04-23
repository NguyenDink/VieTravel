package com.example.projecttravel.model;

import java.util.List;

public class Hotel {
    private int hotel_id;
    private Location location;
    private String name;
    private Category type;
    private List<String> images;
    private String description;
    private int capacity;
    private String address;
    private double price;
    public Hotel(int hotel_id, Location location, String name, Category type, List<String> images, String description, int capacity, String address, double price) {
        this.hotel_id = hotel_id;
        this.location = location;
        this.name = name;
        this.type = type;
        this.images = images;
        this.description = description;
        this.capacity = capacity;
        this.address = address;
        this.price = price;
    }

    public Hotel() {
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getType() {
        return type;
    }

    public void setType(Category type) {
        this.type = type;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
