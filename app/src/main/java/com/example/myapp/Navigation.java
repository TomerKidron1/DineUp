package com.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class Navigation extends AppCompatActivity implements View.OnClickListener {
    ImageView table,questions,people,conflicts,food,wrapup;
    Button tableBt,questionsBt,peopleBt,conflictsBt,foodBt,wrapupBt,home;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref,ref2;
    SharedPreferences sp;
    ArrayList<String> listQuestions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = database.getReference("Users");
        ref2 = database.getReference("Users");
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        listQuestions = new ArrayList<>();
        table = findViewById(R.id.table_nav_iv);
        questions  = findViewById(R.id.questions_nav_iv);
        people = findViewById(R.id.people_nav_iv);
        conflicts = findViewById(R.id.conflicts_nav_iv);
        food = findViewById(R.id.food_nav_iv);
        wrapup = findViewById(R.id.wrapup_nav_iv);
        tableBt = findViewById(R.id.table_nav_bt);
        questionsBt = findViewById(R.id.questions_nav_bt);
        peopleBt = findViewById(R.id.people_nav_bt);
        conflictsBt = findViewById(R.id.conflicts_nav_bt);
        foodBt = findViewById(R.id.food_nav_bt);
        wrapupBt = findViewById(R.id.wrapup_nav_bt);
        home = findViewById(R.id.home_nav_bt);
        home.setOnClickListener(this);
        tableBt.setOnClickListener(this);
        questionsBt.setOnClickListener(this);
        peopleBt.setOnClickListener(this);
        conflictsBt.setOnClickListener(this);
        foodBt.setOnClickListener(this);
        wrapupBt.setOnClickListener(this);
        ref.child(user.getUid()).child(sp.getString("number","")).addValueEventListener(new ValueEventListener() {
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
                if(snapshot.hasChild("wrapUp"))
                    wrapup.setImageResource(R.drawable.checkmark1);
                else
                    wrapup.setImageResource(R.drawable.x);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref2.child(user.getUid()).child(sp.getString("number","")).child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i=0;i<snapshot.getChildrenCount();i++){
                    listQuestions.add((String) snapshot.child("answer"+(i+1)).getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view ==tableBt){
            startActivity(new Intent(Navigation.this,TableSettings.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(view ==questionsBt){
            startActivity(new Intent(Navigation.this,Questions.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(view ==peopleBt){
            if(listQuestions.get(0).equals("yes")) {
                startActivity(new Intent(Navigation.this, People.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else{
                listQuestions.set(0,"yes");
                showPopUp("people");
            }
        }
        if(view ==conflictsBt){
            if(listQuestions.get(1).equals("yes")) {
                startActivity(new Intent(Navigation.this, Conflicts.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else {
                listQuestions.set(1,"yes");
                showPopUp("conflicts");
            }
        }
        if(view == foodBt){
            if(listQuestions.get(2).equals("yes")) {
                startActivity(new Intent(Navigation.this, Food.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else{
                listQuestions.set(2,"yes");
                showPopUp("food");
            }
        }
        if(view == home){
            startActivity(new Intent(Navigation.this, MainScreen.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        if(view == wrapupBt){
            startActivity(new Intent(Navigation.this,WrapUp.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
    }
    private void showPopUp(String what){
        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation.this);
        builder.setTitle("Would you like to create a "+what+" list?");
        builder.setMessage("you didnt selected it in the questions page, would you like to change your selection?");
        builder.setCancelable(true);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Map<String,String> map = new HashMap<>();
                map.put("answer1",listQuestions.get(0));
                map.put("answer2",listQuestions.get(1));
                map.put("answer3",listQuestions.get(2));
                ref2.child(user.getUid()).child(sp.getString("number","")).child("questions").setValue(map);
                if(what.equals("people")){
                    startActivity(new Intent(Navigation.this,People.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                }
                if(what.equals("conflicts")){
                    startActivity(new Intent(Navigation.this,Conflicts.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
                if(what.equals("food")){
                    startActivity(new Intent(Navigation.this,Food.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}
