package com.example.projecttravel.model;

public class Role {
    int Role_id;
    String name;
    public Role(int role_id, String name) {
        Role_id = role_id;
        this.name = name;
    }
    public Role(){}

    public int getRole_id() {
        return Role_id;
    }

    public void setRole_id(int role_id) {
        Role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
