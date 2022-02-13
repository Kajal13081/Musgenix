package com.example.musicplayer_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
// for not showing app bar at time of animation
        getSupportActionBar().hide();
// using a thread for animation purpose
        Thread td = new Thread(){
// execution of thread starts
            public void run(){
                try {
                    sleep(2500); //Reduced to ~2.5 seconds from 5 seconds
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };td.start();
    }
}
