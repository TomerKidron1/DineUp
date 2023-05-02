package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Questions extends AppCompatActivity implements View.OnClickListener {
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    RadioGroup radioGroup3;
    SharedPreferences sp,spNumber;
    SharedPreferences.Editor editor;
    Button next;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    ArrayList<String> arraylist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);
        radioGroup1 = findViewById(R.id.radio1);
        radioGroup2 = findViewById(R.id.radio2);
        radioGroup3 = findViewById(R.id.radio3);
        next = findViewById(R.id.next_questions);
        next.setOnClickListener(this);
        arraylist = new ArrayList<>();
        sp = getSharedPreferences("questions", MODE_PRIVATE);
        editor = sp.edit();
        editor.putString("answer1","yes");
        editor.putString("answer2","yes");
        editor.putString("answer3","yes");
        editor.commit();
        spNumber = getSharedPreferences("currentProject", MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = database.getReference("Users");
        ref.child(user.getUid()).child(spNumber.getString("number","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                retreieveObjects(snapshot);
                if(sp.getString("answer1","").equals("no")){
                    radioGroup2.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.yes1){
                    editor.putString("answer1", "yes");
                    editor.commit();
                    RadioButton radio1 = findViewById(R.id.yes2);
                    RadioButton radio3 = findViewById(R.id.no2);
                    radio1.setEnabled(true);
                    radio3.setEnabled(true);
                }
                else {
                    editor.putString("answer1", "no");
                    editor.commit();
                    RadioButton radio = findViewById(R.id.yes2);
                    RadioButton radio2 = findViewById(R.id.no2);
                    radio.setEnabled(false);
                    radio2.setEnabled(false);

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

    private void retreieveObjects(DataSnapshot snapshot) {
        if(snapshot.hasChild("questions")){
            for(int i=0; i<3; i++) {
                arraylist.add((String) snapshot.child("questions").child("answer"+(i+1)).getValue());
            }
            editor.putString("answer1",arraylist.get(0));
            editor.putString("answer2",arraylist.get(1));
            editor.putString("answer3",arraylist.get(2));
            editor.commit();
            updateObjects();
        }
    }
    private void updateObjects() {
        if(sp.getString("answer1","").equals("yes")) {
            radioGroup1.check(R.id.yes1);
            RadioButton radio1 = findViewById(R.id.yes2);
            RadioButton radio3 = findViewById(R.id.no2);
            radio1.setEnabled(true);
            radio3.setEnabled(true);
        }
        else {
            radioGroup1.check(R.id.no1);
            RadioButton radio = findViewById(R.id.yes2);
            RadioButton radio2 = findViewById(R.id.no2);
            radio.setEnabled(false);
            radio2.setEnabled(false);
        }
        if(sp.getString("answer2","").equals("yes"))
            radioGroup2.check(R.id.yes2);
        else
            radioGroup2.check(R.id.no2);
        if(sp.getString("answer3","").equals("yes"))
            radioGroup3.check(R.id.yes3);
        else
            radioGroup3.check(R.id.no3);
    }

    @Override
    public void onClick(View view) {
        if(view == next){
            Map<String,String> map = new HashMap<String,String>();
            map.put("answer1",sp.getString("answer1",""));
            map.put("answer2",sp.getString("answer2",""));
            map.put("answer3",sp.getString("answer3",""));
            ref.child(user.getUid()).child(spNumber.getString("number","")).child("questions").setValue(map);
            if(sp.getString("answer1","").equals("yes")){
                startActivity(new Intent(Questions.this,People.class));
            }
            else if(sp.getString("answer3","").equals("yes")){
                startActivity(new Intent(Questions.this,Food.class));
            }
        }
    }
}
