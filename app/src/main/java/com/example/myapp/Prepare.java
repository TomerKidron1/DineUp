package com.example.myapp;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Prepare extends AppCompatActivity {
    ProgressBar progress;
    TextView percentege;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preparing_project);
        progress = findViewById(R.id.progress_prepare);
        percentege = findViewById(R.id.number_progress);

    }
}
