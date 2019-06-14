package com.example.anonymous.childcareadminapp;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Anonymous on 6/5/2019.
 */

public class Child implements Serializable{

    private String ChildId, Fullname, Nickname, Age, Nationality, Religion, Fatheremail, Motheremail, Date, ImageURL;
    private String Entrytime, Leavetime, Entryid, Entrydate;
    private Boolean Status;

    public Child (){

    }

    public Child (String childId, String fullname, String nickname, String age, String nationality,
                  String religion, String fatheremail, String motheremail, String date, String imageUrl){
        this.ChildId = childId;
        this.Fullname = fullname;
        this.Nickname = nickname;
        this.Age = age;
        this.Nationality = nationality;
        this.Religion = religion;
        this.Fatheremail = fatheremail;
        this.Motheremail = motheremail;
        this.Date = date;
        this.ImageURL = imageUrl;
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

    public String getEntrytime() {
        return Entrytime;
    }

    public void setEntrytime(String entrytime) {
        Entrytime = entrytime;
    }

    public String getLeavetime() {
        return Leavetime;
    }

    public void setLeavetime(String leavetime) {
        Leavetime = leavetime;
    }

    public String getEntryid() {
        return Entryid;
    }

    public void setEntryid(String entryid) {
        Entryid = entryid;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public Child (String childId, String fullname, String entryid, String entry_time, String leave_time, String entrydate, Boolean status){
        this.ChildId = childId;
        this.Fullname = fullname;
        this.Entryid = entryid;
        this.Entrytime = entry_time;
        this.Leavetime = leave_time;
        this.Entrydate = entrydate;
        this.Status = status;
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

    @Exclude
    public Map<String, Object> toMapEntry(){
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String,Object> dailyValue = new HashMap<>();
        dailyValue.put("meal", "no");
        dailyValue.put("temperature", "no");

        result.put("childId", this.ChildId);
        result.put("fullname", this.Fullname);
        result.put("entryid", this.Entryid);
        result.put("entrytime", this.Entrytime);
        result.put("leavetime", this.Leavetime);
        result.put("entrydate", this.Entrydate);
        result.put("status", this.Status);
        result.put("morning", dailyValue);
        result.put("noon", dailyValue);
        result.put("night", dailyValue);
        return result;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
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




}


