package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class WrapUp extends AppCompatActivity implements View.OnClickListener {
    ImageView table,questions,people,conflicts,food,wrapup;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref,ref2;
    SharedPreferences sp,sp2;
    Button navigate,done;
    ArrayList<String> whatsdone;


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
        ref2 = database.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        whatsdone = new ArrayList<>();
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        sp2 = getSharedPreferences("questions",MODE_PRIVATE);
        Map<String,String> map = new HashMap<>();
        map.put("wraped up","yes");
        ref2.child("Users").child(user.getUid()).child(sp.getString("number","")).child("wrapUp").setValue("yes");
        ref.child("Users").child(user.getUid()).child(sp.getString("number","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild("parameters")) {
                    table.setImageResource(R.drawable.checkmark1);
                    whatsdone.add("yes");
                }
                else {
                    table.setImageResource(R.drawable.x);
                    whatsdone.add("no");

                }
                if(snapshot.hasChild("questions")) {
                    questions.setImageResource(R.drawable.checkmark1);
                    whatsdone.add("yes");

                }
                else {
                    questions.setImageResource(R.drawable.x);
                    whatsdone.add("no");

                }
                if(snapshot.hasChild("people")) {
                    people.setImageResource(R.drawable.checkmark1);
                    whatsdone.add("yes");

                }
                else {
                    people.setImageResource(R.drawable.x);
                    whatsdone.add("no");

                }
                if(snapshot.hasChild("conflicts")||sp2.getString("answer2","").equals("no")) {
                    conflicts.setImageResource(R.drawable.checkmark1);
                    whatsdone.add("yes");

                }
                else {
                    conflicts.setImageResource(R.drawable.x);
                    whatsdone.add("no");

                }
                if(snapshot.hasChild("food")) {
                    food.setImageResource(R.drawable.checkmark1);
                    whatsdone.add("yes");

                }
                else {
                    food.setImageResource(R.drawable.x);
                    whatsdone.add("no");

                }
                if(snapshot.hasChild("wrapUp")) {
                    wrapup.setImageResource(R.drawable.checkmark1);
                    whatsdone.add("yes");
                }
                else {
                    wrapup.setImageResource(R.drawable.x);
                    whatsdone.add("no");
                }
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
            boolean flag=true;
            for(int i=0;i<whatsdone.size()&&flag==true;i++){
                if(whatsdone.get(i).equals("no")){
                    flag=false;
                }
            }
            if(flag==false){
                Toast.makeText(this, "You havent completed all your tasks", Toast.LENGTH_LONG).show();
            }
            else{

            }
        }

    }
}
