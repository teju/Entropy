package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class FriendListObj {


    @SerializedName("id")
    @Expose
    private String id ;

    public String getFreind_id() {
        return freind_id;
    }

    public void setFreind_id(String freind_id) {
        this.freind_id = freind_id;
    }

    @SerializedName("freind_id")
    @Expose
    private String freind_id ;


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

    @SerializedName("first_name")
    @Expose
    private String first_name ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    @SerializedName("profile_photo")
    @Expose
    private String profile_photo ;




}
