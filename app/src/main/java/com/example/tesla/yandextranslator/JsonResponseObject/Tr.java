package com.example.tesla.yandextranslator.JsonResponseObject;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tr {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("pos")
    @Expose
    private String pos;
    @SerializedName("syn")
    @Expose
    private List<Syn> syn = null;
    @SerializedName("mean")
    @Expose
    private List<Mean> mean = null;
    @SerializedName("ex")
    @Expose
    private List<Ex> ex = null;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public List<Syn> getSyn() {
        return syn;
    }

    public void setSyn(List<Syn> syn) {
        this.syn = syn;
    }

    public List<Mean> getMean() {
        return mean;
    }

    public void setMean(List<Mean> mean) {
        this.mean = mean;
    }

    public List<Ex> getEx() {
        return ex;
    }

    public void setEx(List<Ex> ex) {
        this.ex = ex;
    }
}
