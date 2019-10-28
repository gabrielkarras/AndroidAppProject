package com.example.ui;

public class TagObj {
    private String name;
    private int tagID;

    private boolean buzzerEnabled = true;
    private String status = "Unreachable";
    public boolean alarm = false;
    public boolean selected = false;

    public TagObj (String name, int ID, boolean buzzerEnabled){
        this.name = name;
        this.tagID = ID;
        this.buzzerEnabled = buzzerEnabled;
    }

    public String getName(){
        return name;
    }

    public int getTagID() {
        return tagID;
    }

    public boolean isBuzzerEnabled() {
        return buzzerEnabled;
    }

    public void updateFields(String name, int ID, boolean buzzerEnabled){
        this.name = name;
        this.tagID = ID;
        this.buzzerEnabled = buzzerEnabled;
    }

    public void updateStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
