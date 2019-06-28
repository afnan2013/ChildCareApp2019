package com.example.anonymous.childcareadminapp;

import java.io.Serializable;

/**
 * Created by Anonymous on 6/27/2019.
 */

public class Complain implements Serializable{
    private String Userid, Complain, Username, Email, Date;

    public Complain(){

    }

    public Complain(String id, String name, String email, String text, String date){
        Userid = id;
        Complain = text;
        Username = name;
        Email = email;
        Date = date;

    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        this.Userid = userid;
    }

    public String getComplain() {
        return Complain;
    }

    public void setComplain(String complain) {
        this.Complain = complain;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
