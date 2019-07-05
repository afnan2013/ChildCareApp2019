package com.example.childcareapp;

import java.io.Serializable;

/**
 * Created by Anonymous on 6/27/2019.
 */

public class Complain implements Serializable{
    private String Complainid, Userid, Complain, Username, Email, Date;
    private boolean Status;

    public Complain(){

    }

    public Complain(String complainid, String id, String name, String email, String text, String date, Boolean status){
        Complainid = complainid;
        Userid = id;
        Complain = text;
        Username = name;
        Email = email;
        Date = date;
        Status = status;

    }

    public String getComplainid() {
        return Complainid;
    }

    public void setComplainid(String complainid) {
        Complainid = complainid;
    }

    public boolean isStatus() {
        return Status;
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

    public void setStatus(boolean status) {
        Status = status;
    }
}
