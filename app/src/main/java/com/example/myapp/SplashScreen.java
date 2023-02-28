package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity  {
    ProgressBar pb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        pb = findViewById(R.id.progressBar);
        SharedPreferences sp = getSharedPreferences("User",MODE_PRIVATE);
        String restoredEmail = sp.getString("email", "");
        Utils.delay(4, new Utils.DelayCallback() {
            @Override
            public void afterDelay() {
                if(restoredEmail.isEmpty())
                {
                    startActivity(new Intent(SplashScreen.this, GetStarted.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else{
                    startActivity(new Intent(SplashScreen.this,MainScreen.class));
                    overridePendingTransition(R.anim.animate_zoom_enter,R.anim.animate_zoom_exit);
                }
            }
        });
    }


}
