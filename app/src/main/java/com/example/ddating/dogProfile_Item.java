package com.example.ddating;

public class dogProfile_Item {


    private String dogID;
    private String dogName;
    private String dogTyoe;
    private String dogGender;
    private String dogAge;
    private String dogImageURI;


    public dogProfile_Item(String dogID, String dogName, String dogTyoe, String dogGender, String dogAge, String dogImageURI) {
        this.dogID = dogID;
        this.dogName = dogName;
        this.dogTyoe = dogTyoe;
        this.dogGender = dogGender;
        this.dogAge = dogAge;
        this.dogImageURI = dogImageURI;
    }

    public String getDogID() {
        return dogID;
    }

    public String getDogName() {
        return dogName;
    }

    public String getDogTyoe() {
        return dogTyoe;
    }

    public String getDogGender() {
        return dogGender;
    }

    public String getDogAge() {
        return dogAge;
    }

    public String getDogImageURI() {
        return dogImageURI;
    }
}
