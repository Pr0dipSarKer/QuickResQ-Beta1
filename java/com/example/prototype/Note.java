package com.example.prototype;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String documentId;
    private String name;
    private String address;
    private String phone;
    private String website;
    private String tag;
    private String map;

    public Note() {
        //public no-arg constructor needed
    }

    public Note(String name, String address, String phone, String website, String tag, String map) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.tag = tag;
        this.map = map;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getTag() {
        return tag;
    }

    public String getMap() {
        return map;
    }


    public String getWebsite() {
        return website;
    }

    public void setWebsite() {
        this.website = website;
    }
}