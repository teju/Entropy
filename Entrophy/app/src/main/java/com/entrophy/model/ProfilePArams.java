package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class ProfilePArams {



    @SerializedName("original_path")
    @Expose
    private String original_path;

    @SerializedName("thumb_path")
    @Expose
    private String thumb_path;

    public String getOriginal_path() {
        return original_path;
    }

    public void setOriginal_path(String original_path) {
        this.original_path = original_path;
    }

    public String getThumb_path() {
        return thumb_path;
    }

    public void setThumb_path(String thumb_path) {
        this.thumb_path = thumb_path;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    @SerializedName("profile_path")
    @Expose
    private String profile_path;



}
