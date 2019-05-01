package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class DAshboardRes {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("friend_count")
    @Expose
    private String friend_count;


    @SerializedName("notification_count")
    @Expose
    private String notification_count;


    public String getFriend_count() {
        return friend_count;
    }

    public void setFriend_count(String friend_count) {
        this.friend_count = friend_count;
    }

    public String getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(String notification_count) {
        this.notification_count = notification_count;
    }

    public String getStealth_mode() {
        return stealth_mode;
    }

    public void setStealth_mode(String stealth_mode) {
        this.stealth_mode = stealth_mode;
    }

    @SerializedName("stealth_mode")
    @Expose
    private String stealth_mode;

}
