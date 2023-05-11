package com.example.collegeproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.collegeproject.R;
import com.example.collegeproject.databinding.ActivitySplahScreenBinding;

public class SplahScreen extends AppCompatActivity {

    Animation topAnimantion,bottomAnimation,middleAnimation;
    private ActivitySplahScreenBinding splahScreenBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splahScreenBinding=ActivitySplahScreenBinding.inflate(getLayoutInflater());
        setContentView(splahScreenBinding.getRoot());

        //Animation Calls
        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animation_code);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation_code);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation_code);


        splahScreenBinding.imageView.setAnimation(bottomAnimation);
        splahScreenBinding.textView4.setAnimation(middleAnimation);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplahScreen.this, MainActivity.class));
                finish();
            }
        },2000);


    }
}