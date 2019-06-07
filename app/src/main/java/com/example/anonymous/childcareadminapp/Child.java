package com.example.anonymous.childcareadminapp;

/**
 * Created by Anonymous on 6/5/2019.
 */

public class Child {

    private String Fullname, Nickname, Age, Nationality, Religion, Fatheremail, Motheremail, Date;

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


