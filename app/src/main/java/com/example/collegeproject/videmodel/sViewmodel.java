package com.example.collegeproject.videmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.collegeproject.model.Student;
import com.example.collegeproject.repository.Repository;

import java.util.ArrayList;

public class sViewmodel extends ViewModel {


    private Repository repository;

    private LiveData<ArrayList<Student>> arrayListLiveData;


    public sViewmodel() {
      repository=new Repository();
    }

    public LiveData<ArrayList<Student >> getallstudentData(){
       arrayListLiveData= repository.getstudentlist();
       return arrayListLiveData;
    }

    public void editstudentdetails(Student student){
        repository.editstudentdetail(student);
    }

    public void deletestudentdetails(Student student){
        repository.removestudentdetail(student);
    }




    public void upload_student_data(Student student, Uri image_uri,String email,String password){
repository.uploadstudent_data(student,image_uri,email,password);
    }








}
