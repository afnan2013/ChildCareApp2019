package com.example.anonymous.childcareadminapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anonymous on 6/5/2019.
 */

public class Child {

    private String ChildId, Fullname, Nickname, Age, Nationality, Religion, Fatheremail, Motheremail, Date;
    private int i;

    public Child (){

    }

    public Child (String fullname, String nickname, String age, String nationality,
                  String religion, String fatheremail, String motheremail, String date){
        this.Fullname = fullname;
        this.Nickname = nickname;
        this.Age = age;
        this.Nationality = nationality;
        this.Religion = religion;
        this.Fatheremail = fatheremail;
        this.Motheremail = motheremail;
        this.Date = date;
    }

    public Child (String nickname, String age, String religion){
        this.Nickname = nickname;
        this.Age = age;
        this.Religion = religion;
    }

    public Child(String childId, String fullname, String date, int i) {
        this.ChildId = childId;
        this.Fullname = fullname;
        this.Date = date;
        this.i = i;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fullname", this.Fullname);
        result.put("nickname", this.Nickname);
        result.put("age", this.Age);
        result.put("nationality", this.Nationality);
        result.put("religion", this.Religion);
        result.put("date", this.Date);
        result.put("fatheremail", this.Fatheremail);
        result.put("motheremail", this.Motheremail);
        return result;
    }

    public String getChildId() {
        return ChildId;
    }

    public void setChildId(String childId) {
        ChildId = childId;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getReligion() {
        return Religion;
    }

    public void setReligion(String religion) {
        Religion = religion;
    }

    public String getFatheremail() {
        return Fatheremail;
    }

    public void setFatheremail(String fatheremail) {
        Fatheremail = fatheremail;
    }

    public String getMotheremail() {
        return Motheremail;
    }

    public void setMotheremail(String motheremail) {
        Motheremail = motheremail;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }



}


