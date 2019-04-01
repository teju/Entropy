package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class Error {


    public List<String> getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(List<String> otp_code) {
        this.otp_code = otp_code;
    }

    @SerializedName("otp_code")
    @Expose
    private List<String> otp_code = new ArrayList<>();


}
