package com.example.e28.memo.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by User on 2019/07/16.
 */

public class Repeat extends RealmObject {
    @PrimaryKey
    public long id;
    public Date createdAt;
    public Date updatedAt;
    public Integer repeatScale; //リピートなし=0、日=1、週=2、月=3、年=4
    public long repeatInterval;
    public boolean isNotifyMonday;
    public boolean isNotifyTuesday;
    public boolean isNotifyWednesday;
    public boolean isNotifyThursday;
    public boolean isNotifyFriday;
    public boolean isNotifySaturday;
    public boolean isNotifySunday;
    public long notifyRemainCount; //設定回数
    public long notifyEndCount; //残り回数
    public Date notifyEndDate;

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

    public Integer getRepeatScale() {
        return repeatScale;
    }

    public void setRepeatScale(Integer repeatScale) {
        this.repeatScale = repeatScale;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public boolean isNotifyMonday() {
        return isNotifyMonday;
    }

    public void setNotifyMonday(boolean notifyMonday) {
        isNotifyMonday = notifyMonday;
    }

    public boolean isNotifyTuesday() {
        return isNotifyTuesday;
    }

    public void setNotifyTuesday(boolean notifyTuesday) {
        isNotifyTuesday = notifyTuesday;
    }

    public boolean isNotifyWednesday() {
        return isNotifyWednesday;
    }

    public void setNotifyWednesday(boolean notifyWednesday) {
        isNotifyWednesday = notifyWednesday;
    }

    public boolean isNotifyThursday() {
        return isNotifyThursday;
    }

    public void setNotifyThursday(boolean notifyThursday) {
        isNotifyThursday = notifyThursday;
    }

    public boolean isNotifyFriday() {
        return isNotifyFriday;
    }

    public void setNotifyFriday(boolean notifyFriday) {
        isNotifyFriday = notifyFriday;
    }

    public boolean isNotifySaturday() {
        return isNotifySaturday;
    }

    public void setNotifySaturday(boolean notifySaturday) {
        isNotifySaturday = notifySaturday;
    }

    public boolean isNotifySunday() {
        return isNotifySunday;
    }

    public void setNotifySunday(boolean notifySunday) {
        isNotifySunday = notifySunday;
    }

    public long getNotifyRemainCount() {
        return notifyRemainCount;
    }

    public void setNotifyRemainCount(long notifyRemainCount) {
        this.notifyRemainCount = notifyRemainCount;
    }

    public long getNotifyEndCount() {
        return notifyEndCount;
    }

    public void setNotifyEndCount(long notifyEndCount) {
        this.notifyEndCount = notifyEndCount;
    }

    public Date getNotifyEndDate() {
        return notifyEndDate;
    }

    public void setNotifyEndDate(Date notifyEndDate) {
        this.notifyEndDate = notifyEndDate;
    }
}
