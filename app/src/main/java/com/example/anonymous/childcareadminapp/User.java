package com.example.anonymous.childcareadminapp;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anonymous on 6/11/2019.
 */

public class User implements Serializable{

    private String Userid, Username, Email, Gender, Childid, ImageURL;
    private Boolean Status, Admin;

    public User(){

    }

    public User(String userid, String username, String email, String gender, String imageURL, Boolean status, Boolean admin) {
        Userid = userid;
        Username = username;
        Email = email;
        Gender = gender;
        //Childid = childid;
        ImageURL = imageURL;
        Status = status;
        Admin = admin;
    }

    public User(String username, String email){
        Username = username;
        Email = email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", this.Username);
        result.put("email", this.Email);
        return result;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getChildid() {
        return Childid;
    }

    public void setChildid(String childid) {
        Childid = childid;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(Boolean admin) {
        Admin = admin;
    }
}

