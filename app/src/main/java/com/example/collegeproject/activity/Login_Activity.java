package com.example.collegeproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.collegeproject.R;
import com.example.collegeproject.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    FirebaseAuth mAuth ;
    public  String Sname;
    public  String srollno;
    private ActivityLoginBinding lbinding;

    SharedPreferences sh ;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser !=null){
            startActivity(new Intent(Login_Activity.this,Student_detail_Activity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lbinding=ActivityLoginBinding.inflate(getLayoutInflater());
        sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        setContentView(lbinding.getRoot());
        // Get an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

// Get the user's email and password from the EditText fields


        lbinding.loginl1.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = lbinding.loginl1.lemail.getText().toString();
                String password = lbinding.loginl1.lpass.getText().toString();
                Sname=lbinding.loginl1.lname.getText().toString();
                srollno=lbinding.loginl1.lroolno.getText().toString();

                if(TextUtils.isEmpty(email)){
                    lbinding.loginl1.lemail.setError("Invalid field");
                }else if(TextUtils.isEmpty(password)){
                    lbinding.loginl1.lpass.setError("Invalid field");
                }else if(TextUtils.isEmpty(Sname)){
                    lbinding.loginl1.lname.setError("Invalid field");
                }else if(TextUtils.isEmpty(srollno)){
                    lbinding.loginl1.lroolno.setError("Invalid field");
                }else if(!isValidEmailAddress(email)){
                    lbinding.loginl1.lemail.setError("Inavlid email");
                }else{
                    lbinding.loginl1.loginprogress.setVisibility(View.VISIBLE);
// Use the signInWithEmailAndPassword method to sign in the user
           mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                      lbinding.loginl1.loginprogress.setVisibility(View.GONE);
                       savedstudentsharedpre();
                       startActivity(new Intent(Login_Activity.this,Student_detail_Activity.class));
                       finish();
                   }else{
                       lbinding.loginl1.loginprogress.setVisibility(View.GONE);
                       Toast.makeText(Login_Activity.this, "Login-failed", Toast.LENGTH_SHORT).show();
                   }
               }
           });
                }

            }
        });
    }
    public void savedstudentsharedpre() {
        // Creating a shared pref object with a file name "MySharedPref" in private mode
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("name",Sname );
        myEdit.putString("rollno",srollno);
        myEdit.apply();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}