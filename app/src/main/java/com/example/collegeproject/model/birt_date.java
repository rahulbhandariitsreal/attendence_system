package com.example.collegeproject.model;

import androidx.annotation.NonNull;

public class birt_date {
    int day;
    int month;
    int year;

    public birt_date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }


    @NonNull
    @Override
    public String toString() {
        return day+"/"+month+"/"+year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
