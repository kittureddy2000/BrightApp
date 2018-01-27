package com.brightapps.all.mDatabase;

/**
 * Created by kyadamakanti on 1/6/2018.
 */

public class DateTime {
    int year;
    int month;
    int day;
    int hour;
    int minute;
    String amPM;

    public String getAmPM() {
        return amPM;
    }

    public void setAmPM(String amPM) {
        this.amPM = amPM;
    }

    public int getYear() {
        return year;

    }

    public DateTime(int year, int month, int day, int hour, int minute, String amPM) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.amPM = amPM;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
