package com.example.ui;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class TagObj implements Parcelable {

    private String name;
    private String tagAddress;
    public boolean selected = false;


    public TagObj (String name, String tagAddress){
        this.name = name;
        this.tagAddress = tagAddress;
    }

    public TagObj (Parcel in){
        this.name = in.readString();
        this.tagAddress = in.readString();
        this.selected = in.readInt() != 0 ? true : false;
    }

    private  void readFromParcel (Parcel in){
        this.name = in.readString();
        this.tagAddress = in.readString();
        this.selected = in.readInt() != 0 ? true : false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(tagAddress);
        dest.writeInt(selected ? 1 : 0);
    }



    public static final Creator<TagObj> CREATOR = new Creator<TagObj>() {
        @Override
        public TagObj createFromParcel(Parcel source) {
            return new TagObj(source);
        }

        @Override
        public TagObj[] newArray(int size) {
            return new TagObj[size];
        }
    };


    public String getName(){
        return name;
    }

    public String getTagAddress() {
        return tagAddress;
    }

    public void updateFields(String name){
        this.name = name;
    }

    public JSONObject getJSONObject(){
        try {
            JSONObject temp = new JSONObject();
            temp.put("name", name);
            temp.put("address", tagAddress);
            return temp;
        } catch(Exception e){
        }
        return null;
    }
}
