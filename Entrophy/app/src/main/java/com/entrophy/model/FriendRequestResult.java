package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class FriendRequestResult {

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


    public List<MatchedContacts> getFriend_request_list() {
        return friend_request_list;
    }

    public void setFriend_request_list(List<MatchedContacts> friend_request_list) {
        this.friend_request_list = friend_request_list;
    }

    @SerializedName("friend-request-list")
    @Expose
    private List<MatchedContacts> friend_request_list = new ArrayList<>();


}
