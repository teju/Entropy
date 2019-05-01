package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class ProfileInfo implements Serializable {

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

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public UserDetails getErrors() {
        return userDetails;
    }

    public void setErrors(UserDetails errors) {
        this.userDetails = errors;
    }

    @SerializedName("userDetails")
    @Expose
    private UserDetails userDetails;
}
