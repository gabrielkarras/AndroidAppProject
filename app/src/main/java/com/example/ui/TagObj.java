package com.example.ui;

public class TagObj {
    private String name;
    public boolean alarm = false;
    public boolean selected = false;

    public TagObj (String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
