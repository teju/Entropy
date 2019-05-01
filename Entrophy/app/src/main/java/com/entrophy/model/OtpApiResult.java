package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class OtpApiResult {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("user_id")
    @Expose
    private int user_id;


    @SerializedName("access_token")
    @Expose
    private String access_token;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("name")
    @Expose
    private String name;


    public String getInvitemessage() {
        return invitemessage;
    }

    public void setInvitemessage(String invitemessage) {
        this.invitemessage = invitemessage;
    }

    @SerializedName("invite-message")
    @Expose
    private String invitemessage;


    public Error getErrors() {
        return errors;
    }

    public void setErrors(Error errors) {
        this.errors = errors;
    }

    @SerializedName("errors")
    @Expose
    private Error errors;


    public String getProfile_image() {
        return profile_path;
    }

    public void setProfile_image(String profile_image) {
        this.profile_path = profile_image;
    }

    @SerializedName("profile_path")
    @Expose
    private String profile_path;
}
