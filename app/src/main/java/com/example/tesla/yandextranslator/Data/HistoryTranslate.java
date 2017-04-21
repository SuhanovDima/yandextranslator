package com.example.tesla.yandextranslator.Data;

import android.text.method.DateTimeKeyListener;

import com.orm.SugarRecord;

import java.util.Date;


public class HistoryTranslate extends SugarRecord {
    String nativeValue;
    String foreignValue;
    String nativeLanguage;
    String foreignLanguage;
    Date date;
    Boolean IsFavorite;

    public HistoryTranslate(){

    }

    public HistoryTranslate(String nativeValue, String foreignValue, String nativeLanguage,
                            String foreignLanguage, Date date, Boolean isFavorite) {
        this.nativeValue = nativeValue;
        this.foreignValue = foreignValue;
        this.nativeLanguage = nativeLanguage;
        this.foreignLanguage = foreignLanguage;
        this.date = date;
        IsFavorite = isFavorite;
    }

    public String getNativeValue() {
        return nativeValue;
    }

    public void setNativeValue(String nativeValue) {
        this.nativeValue = nativeValue;
    }

    public String getForeignValue() {
        return foreignValue;
    }

    public void setForeignValue(String foreignValue) {
        this.foreignValue = foreignValue;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getForeignLanguage() {
        return foreignLanguage;
    }

    public void setForeignLanguage(String foreignLanguage) {
        this.foreignLanguage = foreignLanguage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getFavorite() {
        return IsFavorite;
    }

    public void setFavorite(Boolean favorite) {
        IsFavorite = favorite;
    }
}
