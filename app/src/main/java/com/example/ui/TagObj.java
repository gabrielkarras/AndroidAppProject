package com.example.ui;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

public class TagObj implements Serializable{

    private String name;
    private String tagAddress;
    public boolean selected = false;


    public TagObj (String name, String tagAddress){
        this.name = name;
        this.tagAddress = tagAddress;
    }

    public String getName(){
        return name;
    }

    public String getTagAddress() {
        return tagAddress;
    }

    public void updateFields(String name){
        this.name = name;
    }


}
