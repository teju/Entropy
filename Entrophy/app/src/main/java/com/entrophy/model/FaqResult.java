package com.entrophy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 29/03/2019.
 */

public class FaqResult {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private String status;

    public List<FAQObj> getFaq_list() {
        return faq_list;
    }

    public void setFaq_list(List<FAQObj> faq_list) {
        this.faq_list = faq_list;
    }

    @SerializedName("faq-list")
    @Expose
    List<FAQObj> faq_list = new ArrayList<>();


}
