package com.pratham.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_TIME = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        new Handler().postDelayed(new Runnable() {
            public void run() {

                startActivity(new Intent(Splashscreen.this, MainActivity.class));
                finish();

            }
        }, SPLASH_DISPLAY_TIME);
    }
}
