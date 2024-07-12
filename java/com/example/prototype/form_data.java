package com.example.prototype;

public class form_data {
    private String name;
    private String email;
    private String phone;
    private String location;
    private String ownPhone;
    public form_data(String name, String email, String phone, String ownPhone , String location) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.ownPhone=ownPhone;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }
    public String getOwnPhone() {   return ownPhone; }
}