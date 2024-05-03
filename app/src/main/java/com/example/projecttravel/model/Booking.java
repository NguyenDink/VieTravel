package com.example.projecttravel.model;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    private int booking_id;
    private Account account;
    private Hotel hotel;
    private String checkIn;
    private String checkOut;
    private double price;
    private int status_id;

    public Booking(int booking_id, Account account, Hotel hotel, String checkIn, String checkOut, double price, int status_id) {
        this.booking_id = booking_id;
        this.account = account;
        this.hotel = hotel;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.price = price;
        this.status_id = status_id;
    }

    public Booking() {
    }

    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }
}
