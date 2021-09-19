package com.lya_cacoi.lotylops.Statistics;

public class OneDayInfo {
    private int day;
    private int remember_count;
    private int forget_count;
    private int studied_count;
    private int pace;
    private int time;

    @Override
    public String toString() {
        return "OneDayInfo{" +
                "day=" + day +
                ", remember_count=" + remember_count +
                ", forget_count=" + forget_count +
                ", studied_count=" + studied_count +
                ", pace=" + pace +
                ", time=" + time +
                '}';
    }

    public OneDayInfo(int day, int remember_count, int forget_count, int studied_count, int pace, int time) {
        this.day = day;
        this.remember_count = remember_count;
        this.forget_count = forget_count;
        this.studied_count = studied_count;
        this.pace = pace;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getRemember_count() {
        return remember_count;
    }

    public void setRemember_count(int remember_count) {
        this.remember_count = remember_count;
    }

    public int getForget_count() {
        return forget_count;
    }

    public void setForget_count(int forget_count) {
        this.forget_count = forget_count;
    }

    public int getStudied_count() {
        return studied_count;
    }

    public void setStudied_count(int studied_count) {
        this.studied_count = studied_count;
    }

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }
}
