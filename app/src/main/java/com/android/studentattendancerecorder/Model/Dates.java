package com.android.studentattendancerecorder.Model;

import java.util.ArrayList;

public class Dates {
    private int year,month,date;
private int attendance;
    public Dates(int year, int month, int date,int attendance) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.attendance=attendance;
    }

    public int getYear() {
        return year;
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

}
