package com.example.tesla.yandextranslator.JsonResponseObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by suhanov on 24.04.2017.
 */

public class Ex {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("tr")
    @Expose
    private List<Tr_> tr = null;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tr_> getTr() {
        return tr;
    }

    public void setTr(List<Tr_> tr) {
        this.tr = tr;
    }
}
