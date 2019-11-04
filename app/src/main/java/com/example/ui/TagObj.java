package com.example.ui;

import java.io.Serializable;

public class TagObj implements Serializable{

    private String name;
    private long tagID;
    private boolean buzzerEnabled;
    private String status;

    public boolean alarm = false;
    public boolean selected = false;

    public TagObj (String name, long ID, boolean buzzerEnabled){
        this.name = name;
        this.tagID = ID;
        this.buzzerEnabled = buzzerEnabled;
        status = "Unreachable";

    }

    public TagObj (String name, long ID, boolean buzzerEnabled, String status){
        this.name = name;
        this.tagID = ID;
        this.buzzerEnabled = buzzerEnabled;
        this.status = status;
    }

    public String getName(){
        return name;
    }

    public long getTagID() {
        return tagID;
    }

    public boolean isBuzzerEnabled() {
        return buzzerEnabled;
    }

    public void updateFields(String name, boolean buzzerEnabled, String status ){
        this.name = name;
        this.status = status;
        this.buzzerEnabled = buzzerEnabled;
    }

    public void updateStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


}
