package com.amit.cronovolibrary;

public class UserDetails {
    private long signal;
    private long hrm;
    private long time_sec;
    private long time_ms;
    private long cadence;
    private long steps;
    private long vo2;
    private long calories;
    private long entry_time;
    private String date;


    public long getSignal() {
        return signal;
    }

    public void setSignal(long signal) {
        this.signal = signal;
    }

    public long getHrm() {
        return hrm;
    }

    public void setHrm(long hrm) {
        this.hrm = hrm;
    }

    public long getTime_sec() {
        return time_sec;
    }

    public void setTime_sec(long time_sec) {
        this.time_sec = time_sec;
    }

    public long getTime_ms() {
        return time_ms;
    }

    public void setTime_ms(long time_ms) {
        this.time_ms = time_ms;
    }

    public long getCadence() {
        return cadence;
    }

    public void setCadence(long cadence) {
        this.cadence = cadence;
    }

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public long getVo2() {
        return vo2;
    }

    public void setVo2(long vo2) {
        this.vo2 = vo2;
    }

    public long getCalories() {
        return calories;
    }

    public void setCalories(long calories) {
        this.calories = calories;
    }

    public long getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(long entry_time) {
        this.entry_time = entry_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

