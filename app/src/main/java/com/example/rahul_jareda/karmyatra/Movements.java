package com.example.rahul_jareda.karmyatra;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "Movements")
public class Movements {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Mobile")
    private String mobile;

    @ColumnInfo(name = "Latitude")
    private String latitude;

    @ColumnInfo(name = "Longitude")
    private String longitude;

    @ColumnInfo(name = "Time")
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {return mobile;};

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude ( String latitude){
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude){
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
