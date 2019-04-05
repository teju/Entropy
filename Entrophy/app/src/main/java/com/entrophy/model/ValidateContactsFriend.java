package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class ValidateContactsFriend {

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

    public List<MatchedContacts> getMatched_contacts() {
        return matched_contacts;
    }

    public void setMatched_contacts(List<MatchedContacts> matched_contacts) {
        this.matched_contacts = matched_contacts;
    }

    @SerializedName("matched_contacts")
    @Expose
    private List<MatchedContacts> matched_contacts;
}
