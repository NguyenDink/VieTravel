package com.example.projecttravel.model;

import java.util.List;

public class Manage {
    private int manage_id;
    private Account account;
    private List<Hotel> hotels;

    public Manage(int manage_id, Account account, List<Hotel> hotels) {
        this.manage_id = manage_id;
        this.account = account;
        this.hotels = hotels;
    }

    public Manage() {
    }

    public int getManage_id() {
        return manage_id;
    }

    public void setManage_id(int manage_id) {
        this.manage_id = manage_id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }
}
