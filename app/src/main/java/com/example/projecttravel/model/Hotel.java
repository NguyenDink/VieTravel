package com.example.projecttravel.model;

import java.util.List;

public class Hotel {
    private int hotel_id;
    private int location_id;
    private String name;
    private int category_id;
    private String description;
    private int capacity;
    private String address;
    private double price;
    private String owner_id;

    public Hotel(int hotel_id, int location_id, String name, int category_id, String description, int capacity, String address, double price, String owner_id) {
        this.hotel_id = hotel_id;
        this.location_id = location_id;
        this.name = name;
        this.category_id = category_id;
        this.description = description;
        this.capacity = capacity;
        this.address = address;
        this.price = price;
        this.owner_id = owner_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public Hotel() {
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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
