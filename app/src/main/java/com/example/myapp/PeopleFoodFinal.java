package com.example.myapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PeopleFoodFinal extends AppCompatActivity implements View.OnClickListener {
    FrameLayout frameLayout;
    Button people,food,back;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_and_food_final);
        people = findViewById(R.id.people_bt_final);
        food = findViewById(R.id.food_bt_final);
        frameLayout = findViewById(R.id.fl_final);
        back = findViewById(R.id.back_people_food_final);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = database.getReference();
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        back.setOnClickListener(this);
        people.setOnClickListener(this);
        food.setOnClickListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_final, new PeopleFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View view) {
        if(view == people){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_final, new PeopleFragment());
            fragmentTransaction.commit();
        }
        if(view == food){
            if(ref.child("Users").child(user.getUid()).child(sp.getString("number","")).child("questions").child("answer3").equals("no")){
                Toast.makeText(this, "You didnt create a food list", Toast.LENGTH_LONG).show();
            }
            else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fl_final, new FoodFragment());
                fragmentTransaction.commit();
            }
        }
        if(view == back){
            startActivity(new Intent(PeopleFoodFinal.this,Final.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
    }
}
