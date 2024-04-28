package com.example.projecttravel.model;

import java.util.List;

public class Favorites {
    private String account_id;
    private List<String> location_id;

    public Favorites() {
    }

    public Favorites(String account_id, List<String> location_id) {
        this.account_id = account_id;
        this.location_id = location_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public List<String> getLocation_id() {
        return location_id;
    }

    public void setLocation_id(List<String> location_id) {
        this.location_id = location_id;
    }
}
