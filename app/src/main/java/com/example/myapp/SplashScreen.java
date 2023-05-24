package com.example.myapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;


public class SplashScreen extends AppCompatActivity {
    ProgressBar pb;
    NetReciever netReciever = new NetReciever();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        pb = findViewById(R.id.progressBar);
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        String restoredEmail = sp.getString("email", "");
        if(Common.isConnectedToInternet(SplashScreen.this)) {
            FirebaseDatabase.getInstance().getReference().keepSynced(true);
            delay(restoredEmail);
        }

    }
    public void delay(String restoredEmail) {
        Utils.delay(4, new Utils.DelayCallback() {
            @Override
            public void afterDelay() {
                if (restoredEmail.isEmpty()) {
                    startActivity(new Intent(SplashScreen.this, GetStarted.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    startActivity(new Intent(SplashScreen.this, MainScreen.class));
                    overridePendingTransition(R.anim.animate_zoom_enter, R.anim.animate_zoom_exit);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReciever,intentFilter);
        super.onStart();

    }

    @Override
    protected void onStop() {
        unregisterReceiver(netReciever);
        super.onStop();
    }


}
