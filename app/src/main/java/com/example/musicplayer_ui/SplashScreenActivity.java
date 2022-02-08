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

        getSupportActionBar().hide();

        Thread td = new Thread(){

            public void run(){
                try {
                    sleep(2500);
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