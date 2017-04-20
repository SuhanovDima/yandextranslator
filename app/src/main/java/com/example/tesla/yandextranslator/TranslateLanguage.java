package com.example.tesla.yandextranslator;

public enum TranslateLanguage {
    EN_TO_RU("en-ru"),
    RU_TO_EN("ru-en");
    private final String name;

    private TranslateLanguage(String name) {
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
