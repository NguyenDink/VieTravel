package com.example.projecttravel.model;

public class Account {
    private String account_id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private int gender;
    private String phone;
    private int role_id;
    public Account(String account_id, String email, String password, String firstName, String lastName, int gender, String phone, int role_id) {
        this.account_id = account_id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.role_id = role_id;
    }

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Account(){
        super();
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role_id;
    }

    public void setRole(int role_id) {
        this.role_id = role_id;
    }

}
