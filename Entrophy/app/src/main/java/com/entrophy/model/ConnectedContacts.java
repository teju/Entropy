package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class ConnectedContacts {


    @SerializedName("mobile_no")
    @Expose
    private String mobile_no ;



    @SerializedName("profile_photo")
    @Expose
    private String image ;


    @SerializedName("first_name")
    @Expose
    private String name ;

    private String isselected;

    public String getID() {
        return ID;
    }

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
}
