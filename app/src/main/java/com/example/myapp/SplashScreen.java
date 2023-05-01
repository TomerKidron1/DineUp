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
    /*if (isNetworkConnected()&& networkSpeed()>=1000) {
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
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please check your internet connection\nwould you like to try again?");
            builder.setCancelable(true);
            builder.setPositiveButton("Retry", (new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(SplashScreen.this, SplashScreen.class));
                }
            }));
            builder.setNegativeButton("Cancel", (new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }));
            builder.show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private int networkSpeed() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        int downSpeed = nc.getLinkDownstreamBandwidthKbps();
        return downSpeed;
    }*/


}
