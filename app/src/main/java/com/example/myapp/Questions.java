package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Questions extends AppCompatActivity implements View.OnClickListener {
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    RadioGroup radioGroup3;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Button next;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);
        radioGroup1 = findViewById(R.id.radio1);
        radioGroup2 = findViewById(R.id.radio2);
        radioGroup3 = findViewById(R.id.radio3);
        next = findViewById(R.id.next_questions);
        next.setOnClickListener(this);
        sp = getSharedPreferences("questions", MODE_PRIVATE);
        editor = sp.edit();
        editor.putString("answer1","no");
        editor.putString("answer2","no");
        editor.putString("answer3","no");
        editor.commit();

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.yes1){
                    editor.putString("answer1", "yes");
                    editor.commit();
                }
                else {
                    editor.putString("answer1", "no");
                    editor.commit();
                }
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.yes2){
                    editor.putString("answer2", "yes");
                    editor.commit();
                }
                else {
                    editor.putString("answer2", "no");
                    editor.commit();
                }
            }
        });
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.yes3){
                    editor.putString("answer3", "yes");
                    editor.commit();
                }
                else {
                    editor.putString("answer3", "no");
                    editor.commit();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view == next){
            if(sp.getString("answer1","").equals("yes")){
                startActivity(new Intent(Questions.this,People.class));
            }
            else if(sp.getString("answer3","").equals("yes")){
                startActivity(new Intent(Questions.this,Food.class));
            }
        }
    }
}
