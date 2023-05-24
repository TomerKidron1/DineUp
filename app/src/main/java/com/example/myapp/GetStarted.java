package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GetStarted extends AppCompatActivity implements View.OnClickListener {
    Button getStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started);
        getStarted = findViewById(R.id.lets_get_started);
        getStarted.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == getStarted){
            startActivity(new Intent(GetStarted.this, Register.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
    }
}
