package com.fjdynamics.fjkey.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by keven.liu on 2019/3/19.
 */

public class Record {

    private String time;
    private String openName;
    private String status;
    private int id;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpenName() {
        return openName;
    }

    public void setOpenName(String openName) {
        this.openName = openName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String state) {
        this.status = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
