package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TableInfo extends AppCompatActivity implements View.OnClickListener {
    ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_info);
        back = findViewById(R.id.back_info);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == back){
            startActivity(new Intent(TableInfo.this,TableSettings.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
    }
}
