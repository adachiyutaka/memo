package com.example.e28.memo.model;

import java.util.Date;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by User on 2019/07/14.
 */


public class Todo extends RealmObject {
    @PrimaryKey
    public long id;
    public Date createdAt;
    public Date updatedAt;
    public Date notifyStartTime;
    public boolean isRepeat;
    public RealmList<Repeat> repeatList;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getNotifyStartTime() {
        return notifyStartTime;
    }

    public void setNotifyStartTime(Date notifyStartTime) {
        this.notifyStartTime = notifyStartTime;
    }

    public boolean getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public RealmList<Repeat> getRepeatList() {
        return repeatList;
    }

    public void setRepeatList(RealmList<Repeat> repeatList) {
        this.repeatList = repeatList;
    }

}