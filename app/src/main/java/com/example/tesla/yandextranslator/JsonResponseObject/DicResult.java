package com.example.tesla.yandextranslator.JsonResponseObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DicResult {
    @SerializedName("head")
    @Expose
    private Head head;
    @SerializedName("def")
    @Expose
    private List<Def> def;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public List<Def> getDef() {
        return def;
    }

    public void setDef(List<Def> def) {
        this.def = def;
    }
}
