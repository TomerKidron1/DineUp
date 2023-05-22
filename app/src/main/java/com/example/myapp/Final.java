package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Final extends AppCompatActivity implements View.OnClickListener {
    Button nav,peoplelist;
    ImageView share;
    ScrollView scrollView;
    LinearLayout ll;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_arrangement);
        nav = findViewById(R.id.nav_final);
        peoplelist = findViewById(R.id.peoplelist_final);
        share = findViewById(R.id.share_final);
        scrollView = findViewById(R.id.scroll_view_final);
        ll = findViewById(R.id.ll_final);
        nav.setOnClickListener(this);
        peoplelist.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == nav){
            startActivity(new Intent(Final.this,Navigation.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        if(view == peoplelist){

        }
    }
}
