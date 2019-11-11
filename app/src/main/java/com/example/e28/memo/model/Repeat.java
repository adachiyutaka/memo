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
    public int repeatInterval;
    public boolean isNotifySunday;
    public boolean isNotifyMonday;
    public boolean isNotifyTuesday;
    public boolean isNotifyWednesday;
    public boolean isNotifyThursday;
    public boolean isNotifyFriday;
    public boolean isNotifySaturday;
    public boolean isNotifySameDay;
    public boolean isNotifySameDOW;
    public int notifySameDOW; //日 = 0, 月 = 1, 火 = 2, 水 = 3, 木 = 4, 金 = 5, 土 = 6
    public int notifySameDOWOrdinal;
    public boolean isNotifySameLastDay;
    public boolean isNoEnd;
    public boolean isEndCount;
    public boolean isEndDate;
    public int notifyRemainCount; //設定回数
    public int notifyEndCount; //残り回数
    public Date notifyEndDate;
    public String summary;

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

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public boolean isNotifySunday() {
        return isNotifySunday;
    }

    public void setNotifySunday(boolean notifySunday) {
        isNotifySunday = notifySunday;
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

    public boolean isNotifySameDay() {
        return isNotifySameDay;
    }

    public void setNotifySameDay(boolean notifySameDay) {
        isNotifySameDay = notifySameDay;
    }

    public boolean isNotifySameDOW() {
        return isNotifySameDOW;
    }

    public void setNotifySameDOW(boolean notifySameDOW) {
        isNotifySameDOW = notifySameDOW;
    }

    public int getNotifySameDOW() {
        return notifySameDOW;
    }

    public void setNotifySameDOW(int notifySameDOW) {
        this.notifySameDOW = notifySameDOW;
    }

    public int getNotifySameDOWOrdinal() {
        return notifySameDOWOrdinal;
    }

    public void setNotifySameDOWOrdinal(int notifySameDOWOrdinal) {
        this.notifySameDOWOrdinal = notifySameDOWOrdinal;
    }

    public boolean isNotifySameLastDay() {
        return isNotifySameLastDay;
    }

    public void setNotifySameLastDay(boolean notifySameLastDay) {
        isNotifySameLastDay = notifySameLastDay;
    }

    public boolean isNoEnd() {
        return isNoEnd;
    }

    public void setNoEnd(boolean noEnd) {
        isNoEnd = noEnd;
    }

    public boolean isEndCount() {
        return isEndCount;
    }

    public void setEndCount(boolean endCount) {
        isEndCount = endCount;
    }

    public boolean isEndDate() {
        return isEndDate;
    }

    public void setEndDate(boolean endDate) {
        isEndDate = endDate;
    }

    public int getNotifyRemainCount() {
        return notifyRemainCount;
    }

    public void setNotifyRemainCount(int notifyRemainCount) {
        this.notifyRemainCount = notifyRemainCount;
    }

    public int getNotifyEndCount() {
        return notifyEndCount;
    }

    public void setNotifyEndCount(int notifyEndCount) {
        this.notifyEndCount = notifyEndCount;
    }

    public Date getNotifyEndDate() {
        return notifyEndDate;
    }

    public void setNotifyEndDate(Date notifyEndDate) {
        this.notifyEndDate = notifyEndDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}