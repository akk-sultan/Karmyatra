package com.example.rahul_jareda.karmyatra;
/**
 * Created by Belal on 9/5/2017.
 */


//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
public class User {

    private int id;
    private String mobile, firstname, lastname;

    public User(int id, String mobile, String firstname, String lastname) {
        this.id = id;
        this.mobile = mobile;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }
}