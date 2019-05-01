package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class UserDetails {



    public ProfilePArams getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(ProfilePArams profile_image) {
        this.profile_image = profile_image;
    }

    @SerializedName("profile")
    @Expose
    private ProfilePArams profile_image;

    public LocationParams getLocation() {
        return location;
    }

    public void setLocation(LocationParams location) {
        this.location = location;
    }

    @SerializedName("location")
    @Expose
    private LocationParams location;


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

     @SerializedName("bio")
    @Expose
    private String bio;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("marital_status")
    @Expose
    private String marital_status;

    @SerializedName("no_of_kids")
    @Expose
    private String no_of_kids;

    @SerializedName("home_location")
    @Expose
    private String home_location;


   @SerializedName("work_location")
    @Expose
    private String work_location;

    @SerializedName("native_place")
    @Expose
    private String native_place;

   @SerializedName("education")
    @Expose
    private List<String> education = new ArrayList<>();

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

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getNo_of_kids() {
        return no_of_kids;
    }

    public void setNo_of_kids(String no_of_kids) {
        this.no_of_kids = no_of_kids;
    }

    public String getHome_location() {
        return home_location;
    }

    public void setHome_location(String home_location) {
        this.home_location = home_location;
    }

    public String getWork_location() {
        return work_location;
    }

    public void setWork_location(String work_location) {
        this.work_location = work_location;
    }

    public String getNative_place() {
        return native_place;
    }

    public void setNative_place(String native_place) {
        this.native_place = native_place;
    }

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getWork() {
        return work;
    }

    public void setWork(List<String> work) {
        this.work = work;
    }

    @SerializedName("work")
    @Expose
    private List<String> work = new ArrayList<>();


}
