package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class ConnectedContactsFriend {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private String status;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    @SerializedName("total_count")
    @Expose
    private int total_count = 0;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;


    public List<MatchedContacts> getFriend_request_list() {
        return friend_request_list;
    }

    public void setFriend_request_list(List<MatchedContacts> friend_request_list) {
        this.friend_request_list = friend_request_list;
    }

    @SerializedName("friend-request-list")
    @Expose
    private List<MatchedContacts> friend_request_list;

    public List<MatchedContacts> getLocation_list() {
        return location_list;
    }

    public void setLocation_list(List<MatchedContacts> location_list) {
        this.location_list = location_list;
    }

    @SerializedName("location-list")
    @Expose
    private List<MatchedContacts> location_list;
}
