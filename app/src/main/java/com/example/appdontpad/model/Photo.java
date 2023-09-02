package com.example.appdontpad.model;

import java.io.Serializable;

public class Photo implements Serializable{
    private String tag;
    private String imageUrl;
    private String imagepath;
    private String id;

    public Photo(String tag, String imageUrl, String imagepath, String id) {
        if (tag.trim().equals("")){
            tag = "No name";
        }
        this.tag = tag;
        this.imageUrl = imageUrl;
        this.imagepath= imagepath;
        this.id = id;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public Photo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id= id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
