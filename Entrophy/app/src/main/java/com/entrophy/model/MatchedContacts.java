package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class MatchedContacts {

    public String getFreind_id() {
        return freind_id;
    }

    public void setFreind_id(String freind_id) {
        this.freind_id = freind_id;
    }

    @SerializedName("freind_id")
    @Expose
    private String freind_id ;

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    @SerializedName("gps")
    @Expose
    private String gps ;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no ;

    @SerializedName("profile_photo")
    @Expose
    private String image ;


    @SerializedName("first_name")
    @Expose
    private String name ;


    @SerializedName("city")
    @Expose
    private String city ;

    @SerializedName("state")
    @Expose
    private String state ;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @SerializedName("country")
    @Expose
    private String country ;

    private String isselected;

    public String getID() {
        return ID;
    }
    @SerializedName("id")
    @Expose
    private String ID;

    public String getImage() {
        return image;
    }

    public String getIsselected() {
        return isselected;
    }

    public String getName() {
        return name;
    }

    public String getContact_id() {
        return contact_id;
    }

    private String contact_id;

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @SerializedName("user_id")
    @Expose
    private String user_id ;


    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsselected(String isselected) {
        this.isselected = isselected;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFriend_cnt() {
        return friend_cnt;
    }

    public void setFriend_cnt(String friend_cnt) {
        this.friend_cnt = friend_cnt;
    }

    @SerializedName("friend_cnt")
    @Expose
    private String friend_cnt ;

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    @SerializedName("updated_on")
    @Expose
    private String updated_on ;


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @SerializedName("location")
    @Expose
    private String location ;
}
