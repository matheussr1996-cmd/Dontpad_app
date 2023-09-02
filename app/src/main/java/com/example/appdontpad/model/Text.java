package com.example.appdontpad.model;

import com.google.firebase.database.DatabaseReference;

public class Text {
    private String text;
    private String tag;
    private String id;

    public Text() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Text(String text, String tag, String id) {
        this.text = text;
        this.tag = tag;
        this.id = id;
    }

    public Text(Class<? extends DatabaseReference> aClass) {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
