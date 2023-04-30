package com.example.collegeproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.collegeproject.R;
import com.example.collegeproject.databinding.ActivityAdminBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Admin_Activity extends AppCompatActivity {

    private String admin_name;
    private String admin_password;
    private ActivityAdminBinding activityAdminBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminBinding=ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(activityAdminBinding.getRoot());

        SharedPreferences sh = getSharedPreferences("MyadminPref", MODE_PRIVATE);
        String sname = sh.getString("admin_name", "");
        String rollmo=sh.getString("admin_pass","0");

        activityAdminBinding.logadminout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showadminlogdialogue();
            }
        });


    }

    private void showadminlogdialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Activity.this);
        builder.setMessage("Are you sure you want to log out?")
                .setTitle("Log out")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Perform logout action
                        savedstudentsharedpre("","");
                        FirebaseAuth mauth=FirebaseAuth.getInstance();
                        mauth.signOut();
                        startActivity(new Intent(Admin_Activity.this,MainActivity.class));
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

    public void savedstudentsharedpre(String Ename,String Eroll) {
        // Creating a shared pref object with a file name "MySharedPref" in private mode
        SharedPreferences sharedPreferences = getSharedPreferences("MyadminPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("admin_name",Ename );
        myEdit.putString("admin_pass",Eroll);
        myEdit.apply();
    }
}