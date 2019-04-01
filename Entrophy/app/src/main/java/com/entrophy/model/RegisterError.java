package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class RegisterError {

    public List<String> getFirst_name() {
        return first_name;
    }

    public void setFirst_name(List<String> first_name) {
        this.first_name = first_name;
    }

    @SerializedName("first_name")
    @Expose
    private List<String> first_name = new ArrayList<>();

    public List<String> getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(List<String> mobile_no) {
        this.mobile_no = mobile_no;
    }

    @SerializedName("mobile_no")
    @Expose
    private List<String> mobile_no = new ArrayList<>();


}
