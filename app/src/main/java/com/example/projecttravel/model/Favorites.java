package com.example.projecttravel.model;

import java.util.List;

public class Favorites {
    private int favorites_id;
    private Account account;
    private List<Location> locations;

    public Favorites(int favorites_id, Account account, List<Location> locations) {
        this.favorites_id = favorites_id;
        this.account = account;
        this.locations = locations;
    }

    public Favorites() {
    }

    public int getFavorites_id() {
        return favorites_id;
    }

    public void setFavorites_id(int favorites_id) {
        this.favorites_id = favorites_id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
