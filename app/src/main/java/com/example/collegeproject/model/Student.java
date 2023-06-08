package com.example.collegeproject.model;

import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Student  {
    private String name;
    private String fName;
    private String dob;
    private String roll_no;
    private String branch_name;
    private String image_url;

    private String emailID;

    private String uniqueID;

    private String phone_number;



    public Student() {

    }

    public Student(String name, String fName, String dob, String roll_no, String branch_name, String image_url, String emailID, String uniqueID, String phone_number) {
        this.name = name;
        this.fName = fName;
        this.dob = dob;
        this.roll_no = roll_no;
        this.branch_name = branch_name;
        this.image_url = image_url;
        this.emailID = emailID;
        this.uniqueID = uniqueID;
        this.phone_number = phone_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getstudentdetail() {
        String details="Name: "+name+"\nRoll No: "+roll_no+"\nFather Name :"+fName+"\nBranch: "+branch_name
                +"\nDob :"+dob+"\nEmail ID :"+emailID;
        return details;
    }


}
