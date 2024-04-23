package com.example.projecttravel.model;

import java.io.Serializable;

public class LocationType implements Serializable {
    int locationType_id;
    String name;
    public LocationType(int locationType_id, String name) {
        this.locationType_id = locationType_id;
        this.name = name;
    }

    public LocationType() {}
    public int getLocationType_id() {
        return locationType_id;
    }

    public void setLocationType_id(int locationType_id) {
        this.locationType_id = locationType_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
