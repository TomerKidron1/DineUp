package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

public class WrapUp extends AppCompatActivity implements View.OnClickListener {
    ImageView table,questions,people,conflicts,food,wrapup;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    SharedPreferences sp;
    Button navigate,done;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrap_up);
        navigate = findViewById(R.id.navigate_wrap);
        done = findViewById(R.id.done_wrap);
        navigate.setOnClickListener(this);
        done.setOnClickListener(this);
        table = findViewById(R.id.table_wrap_iv);
        questions  = findViewById(R.id.questions_wrap_iv);
        people = findViewById(R.id.people_wrap_iv);
        conflicts = findViewById(R.id.conflicts_wrap_iv);
        food = findViewById(R.id.food_wrap_iv);
        wrapup = findViewById(R.id.wrapup_wrap_iv);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        ref.child("Users").child(user.getUid()).child(sp.getString("number","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("parameters")){
                    table.setImageResource(R.drawable.checkmark1);
                }
                else
                    table.setImageResource(R.drawable.x);
                if(snapshot.hasChild("questions"))
                    questions.setImageResource(R.drawable.checkmark1);
                else
                    questions.setImageResource(R.drawable.x);
                if(snapshot.hasChild("people"))
                    people.setImageResource(R.drawable.checkmark1);
                else
                    people.setImageResource(R.drawable.x);
                if(snapshot.hasChild("conflicts"))
                    conflicts.setImageResource(R.drawable.checkmark1);
                else
                    conflicts.setImageResource(R.drawable.x);
                if(snapshot.hasChild("food"))
                    food.setImageResource(R.drawable.checkmark1);
                else
                    food.setImageResource(R.drawable.x);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == navigate){
            startActivity(new Intent(WrapUp.this,Navigation.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        if(view == done){

        }

    }
}