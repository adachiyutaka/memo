package com.example.e28.memo.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by User on 2019/07/14.
 */


public class Todo extends RealmObject {
    @PrimaryKey
    public long id;
    public long createdAt = 0;
    public long updatedAt = 0;
    public long notifyStartTime = 0;
    public boolean isRepeat;
    public long repeatId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getNotifyStartTime() {
        return notifyStartTime;
    }

    public void setNotifyStartTime(long notifyStartTime) {
        this.notifyStartTime = notifyStartTime;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public long getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(long repeatId) {
        this.repeatId = repeatId;
    }
}