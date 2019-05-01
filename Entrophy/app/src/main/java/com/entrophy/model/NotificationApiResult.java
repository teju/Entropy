package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class NotificationApiResult {

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

    public List<NotiModel> getNotification_list() {
        return notification_list;
    }

    public void setNotification_list(List<NotiModel> notification_list) {
        this.notification_list = notification_list;
    }

    @SerializedName("notification-list")
    @Expose
    private List<NotiModel> notification_list = new ArrayList<>();


}
