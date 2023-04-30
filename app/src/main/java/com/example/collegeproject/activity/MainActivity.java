package com.example.collegeproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.example.collegeproject.R;
import com.example.collegeproject.databinding.ActivityMainBinding;
import com.example.collegeproject.databinding.AdminverficationLayoutBinding;
import com.example.collegeproject.databinding.RegistrationLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null){
            startActivity(new Intent(MainActivity.this,Student_detail_Activity.class));
            finish();
        }
        SharedPreferences sh = getSharedPreferences("MyadminPref", MODE_PRIVATE);
        String sname = sh.getString("admin_name", "");
        String rollmo=sh.getString("admin_pass","0");
        if(sname.equals("rahul") && rollmo.equals("rahul")){
            startActivity(new Intent(MainActivity.this,Admin_Activity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login_Activity.class));
            }
        });

        binding.resgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showalterdialogue();
            }
        });

        binding.admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showalterdialogueforadmin();
            }
        });



    }
    private void showalterdialogueforadmin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

// Inflate the custom layout for the AlertDialog
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.adminverfication_layout, null);
// Set the custom layout for the AlertDialog

        AdminverficationLayoutBinding binding = AdminverficationLayoutBinding.bind(view);


        builder.setView(view);

// Set any other properties for the AlertDialog as needed
        builder.setTitle("Welcome");

        binding.edgetaccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.edusername.getText().toString();
                String password=binding.edpaddword.getText().toString();
                // Handle the OK button click here
                if(TextUtils.isEmpty(email)){
                    binding.edusername.setError("Invalid field");
                }else if(TextUtils.isEmpty(password)){
                    binding.edpaddword.setError("Invalid field");
                }else if(password.length()<4){
                    binding.edpaddword.setError("minimum 4 character");
                }else if(email.equals("rahul") && password.equals("rahul")){
                    savedstudentsharedpre(email,password);
                    Intent intent=new Intent(MainActivity.this,Admin_Activity.class);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
// Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showalterdialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

// Inflate the custom layout for the AlertDialog
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.registration_layout, null);
// Set the custom layout for the AlertDialog

        RegistrationLayoutBinding binding = RegistrationLayoutBinding.bind(view);


        builder.setView(view);

// Set any other properties for the AlertDialog as needed
        builder.setTitle("Welcome");

        binding.rl2.rregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.rl2.remil.getText().toString();
                String password=binding.rl2.rpassword.getText().toString();
                // Handle the OK button click here
                if(TextUtils.isEmpty(email)){
                    binding.rl2.remil.setError("Invalid field");
                }else if(TextUtils.isEmpty(password)){
                    binding.rl2.rpassword.setError("Invalid field");
                }else if(password.length()<8){
                    binding.rl2.rpassword.setError("minimum 8 character");
                }else if(!isValidEmailAddress(email)) {
                    binding.rl2.remil.setError("Invalid email");
                }else{
                    Intent intent=new Intent(MainActivity.this,Registration_Activity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("password",password);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
// Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
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