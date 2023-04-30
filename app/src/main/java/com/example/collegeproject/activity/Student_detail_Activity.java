package com.example.collegeproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.collegeproject.R;
import com.example.collegeproject.databinding.ActivityStudentDetailBinding;
import com.example.collegeproject.model.Student;
import com.example.collegeproject.videmodel.sViewmodel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class Student_detail_Activity extends AppCompatActivity {


    private sViewmodel student_viewModel;
    private Student Mystudent;
    private ActivityStudentDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStudentDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String sname = sh.getString("name", "");
        String rollmo=sh.getString("rollno","0");
        Mystudent=new Student();
        Log.v("TAG",sname);
        Log.v("TAG",rollmo);

        student_viewModel=new ViewModelProvider(this).get(sViewmodel.class);

        binding.studentlayout.studentprogressbar.setVisibility(View.VISIBLE);
        student_viewModel.getallstudentData().observe(this, new Observer<ArrayList<Student>>() {
            @Override
            public void onChanged(ArrayList<Student> students) {
                for(Student  student:students){
                    if(Objects.equals(student.getName(), sname) && Objects.equals(student.getRoll_no(), rollmo)){
                        Mystudent=student;
                        Log.v("TAG",student.getstudentdetail());
                        binding.studentlayout.studentprogressbar.setVisibility(View.GONE);
                        setviewinlayout(Mystudent);
                        break;
                    }
                }
                if(Mystudent.getName()==null){
                    binding.studentlayout.studentprogressbar.setVisibility(View.GONE);
                    Toast.makeText(Student_detail_Activity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Student_detail_Activity.this);
                builder.setMessage("Are you sure you want to log out?")
                        .setTitle("Log out")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Perform logout action
                                savedstudentsharedpre("","");
                                FirebaseAuth mauth=FirebaseAuth.getInstance();
                                mauth.signOut();
                                startActivity(new Intent(Student_detail_Activity.this,MainActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the logout action
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });





    }

    private void setviewinlayout(Student mystudent) {
        Glide.with(this).load(mystudent.getImage_url()).into(binding.studentlayout.SDStudentImage);
        binding.studentlayout.SDStudentDetails.setText(mystudent.getstudentdetail());
    }

    public void savedstudentsharedpre(String Ename,String Eroll) {
        // Creating a shared pref object with a file name "MySharedPref" in private mode
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("name",Ename );
        myEdit.putString("rollno",Eroll);
        myEdit.apply();
    }
}