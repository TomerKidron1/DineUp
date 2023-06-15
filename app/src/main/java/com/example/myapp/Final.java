package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

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
import java.util.concurrent.CountDownLatch;

public class Final extends AppCompatActivity implements View.OnClickListener {
    Button nav,peoplelist;
    ImageView share;
    ScrollView scrollView;
    LinearLayout ll;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref,ref2,ref3;
    SharedPreferences sp;
    int numberObj=0,numberCustom=0,countObj=0;
    ArrayList<String> people;
    ArrayList<DoubleString> conflicts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_arrangement);
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        nav = findViewById(R.id.nav_final);
        peoplelist = findViewById(R.id.peoplelist_final);
        share = findViewById(R.id.share_final);
        scrollView = findViewById(R.id.scroll_view_final);
        ll = findViewById(R.id.ll_final);
        nav.setOnClickListener(this);
        peoplelist.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = database.getReference();
        conflicts = new ArrayList<>();
        people = new ArrayList<>();
        ref2 = database.getReference();
        ref3 = database.getReference();
        ref.child("Users").child(user.getUid()).child(sp.getString("number","")).child("parameters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numberObj = (int) snapshot.getChildrenCount();
                if(snapshot.hasChild("custom"))
                    numberObj = (numberObj-1)/3;
                else
                    numberObj = numberObj/3;
                for(int i=0;i<numberObj;i++){
                    if(snapshot.child("custom").exists() && snapshot.child("object "+i).equals("custom")){
                        numberCustom++;
                        LinearLayout linearLayout = new LinearLayout(Final.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(600,400);
                        params.gravity = Gravity.CENTER;
                        linearLayout.setBackgroundResource(R.drawable.frame_layout);
                        params.bottomMargin = 30;
                        linearLayout.setLayoutParams(params);
                        ll.setGravity(Gravity.CENTER);
                        ImageView obj = new ImageView(Final.this);
                        obj.setImageResource(R.drawable.custom);
                        int height = Math.toIntExact((Long) snapshot.child("custom").child("height "+numberCustom).getValue());
                        int width = Math.toIntExact((Long) snapshot.child("custom").child("width "+numberCustom).getValue());
                        LinearLayout.LayoutParams objParmas = new LinearLayout.LayoutParams(width, height);
                        objParmas.gravity = Gravity.CENTER;
                        obj.setLayoutParams(objParmas);
                        obj.setForegroundGravity(Gravity.CENTER);
                        linearLayout.setId(countObj);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        linearLayout.setGravity(Gravity.CENTER);
                        TextView tableNumber = new TextView(Final.this);
                        tableNumber.setText("Table "+(countObj+1));
                        tableNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        tableNumber.setTextColor(Color.WHITE);
                        Typeface typeface1 = ResourcesCompat.getFont(Final.this, R.font.inter_bold);
                        tableNumber.setTypeface(typeface1);
                        linearLayout.addView(tableNumber);
                        linearLayout.addView(obj);
                        ll.addView(linearLayout);
                        countObj++;
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Final.this,TableSitting.class);
                                i.putExtra("TableNumber",linearLayout.getId());
                                startActivity(i);
                            }
                        });
                    }
                    else{
                        LinearLayout linearLayout = new LinearLayout(Final.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(600,400);
                        params.gravity = Gravity.CENTER;
                        linearLayout.setBackgroundResource(R.drawable.frame_layout);
                        params.bottomMargin = 30;
                        linearLayout.setLayoutParams(params);
                        ll.setGravity(Gravity.CENTER);
                        ImageView obj = new ImageView(Final.this);
                        determinObject((String)snapshot.child("object "+i).getValue(),obj);
                        LinearLayout.LayoutParams objParmas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        objParmas.gravity = Gravity.CENTER;
                        obj.setLayoutParams(objParmas);
                        obj.setForegroundGravity(Gravity.CENTER);
                        linearLayout.setId(countObj);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        linearLayout.setGravity(Gravity.CENTER);
                        TextView tableNumber = new TextView(Final.this);
                        tableNumber.setText("Table "+(countObj+1));
                        tableNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        tableNumber.setTextColor(Color.WHITE);
                        Typeface typeface1 = ResourcesCompat.getFont(Final.this, R.font.inter_bold);
                        tableNumber.setTypeface(typeface1);
                        linearLayout.addView(tableNumber);
                        linearLayout.addView(obj);
                        ll.addView(linearLayout);
                        countObj++;
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Final.this,TableSitting.class);
                                i.putExtra("TableNumber",(linearLayout.getId()));
                                startActivity(i);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref2.child("Users").child(user.getUid()).child(sp.getString("number","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i=0;i<snapshot.child("people").getChildrenCount();i++){
                    people.add((String)snapshot.child("people").child("person "+(i+1)).getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Utils.delay(2, new Utils.DelayCallback() {
            @Override
            public void afterDelay() {
                buildArrays();
            }
        });
        ref3.child("Users").child(user.getUid()).child(sp.getString("number","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("conflicts").exists()){
                    for(int i=0;i<snapshot.child("conflicts").getChildrenCount();i++){
                        String conf =(String) snapshot.child("conflicts").child(""+(i+1)).getValue();
                        String name1 = "";
                        int iend = conf.indexOf("+");
                        if (iend != -1) {
                            name1 = conf.substring(0, iend);
                        }
                        DoubleString doubleString = new DoubleString(name1,conf.substring(conf.lastIndexOf("+")+1));
                        conflicts.add(doubleString);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == nav){
            startActivity(new Intent(Final.this,Navigation.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        if(view == peoplelist){
            startActivity(new Intent(Final.this,PeopleFoodFinal.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
    }
    private void determinObject(String object,ImageView imageview) {
        if(object.equals("bigCircle"))
            imageview.setImageResource(R.drawable.big_circle_white);
        if(object.equals("smallCircle"))
            imageview.setImageResource(R.drawable.small_circle_white);
        if(object.equals("rectangle"))
            imageview.setImageResource(R.drawable.rectangle_white);
        if(object.equals("verticalRectangle"))
            imageview.setImageResource(R.drawable.vertical_rectangle_table_white);
        if(object.equals("roundedRectangle"))
            imageview.setImageResource(R.drawable.rounded_table_white);
        if(object.equals("verticalRoundedRectangle"))
            imageview.setImageResource(R.drawable.vertical_rounded_table_white);
        if(object.equals("square"))
            imageview.setImageResource(R.drawable.square_white);
        if(object.equals("custom"))
            imageview.setImageResource(R.drawable.custom);

    }
    private void buildArrays(){
        int peopleintable;
        if(people.size()%2==0){
             peopleintable = (people.size()/countObj);
            for(int i=0;i<countObj;i++){
                Map<String,String> map = new HashMap<>();
                for(int j=0;j<peopleintable;j++){
                    map.put("person "+(j+1),people.get(0));
                    people.remove(0);
                }
                ref3.child("Users").child(user.getUid()).child(sp.getString("number","")).child("table sitting").child("table "+(i+1)).setValue(map);
            }
        }
        else{
            int large = largestTable();
            peopleintable = (people.size()/countObj);
            for(int i=0;i<countObj;i++){
                Map<String,String> map = new HashMap<>();
                for(int j=0;j<peopleintable;j++){
                    map.put("person "+(j+1),people.get(0));
                    people.remove(0);
                    if(i==(large)&&(j==(peopleintable-1))){
                        map.put("person "+(j+2),people.get(0));
                        people.remove(0);
                    }
                }

                ref3.child("Users").child(user.getUid()).child(sp.getString("number","")).child("table sitting").child("table "+(i+1)).setValue(map);

            }
        }
           checkConflicts("");


    }

    private void checkConflicts(String nameConf) {
        final String[] finalNameConf = {nameConf};
        for(int i=0;i<countObj;i++) {
            ArrayList<String> array = new ArrayList<>();
            addValues(array,i);
            int finalI = i;
            Utils.delay(1, new Utils.DelayCallback() {
                @Override
                public void afterDelay() {
                    if (!finalNameConf[0].equals("")) {
                        array.add(finalNameConf[0]);
                        finalNameConf[0] = "";
                    }
                    int size = array.size();
                    ArrayList<String> remove = new ArrayList<>();
                    for (int k = 0; k < size; k++) {
                        for (int h = 0; h < conflicts.size(); h++) {
                            if (array.get(k).equals(conflicts.get(h).getString1())) {
                                for (int l = 0; l < array.size(); l++) {
                                    if (array.get(l).equals(conflicts.get(h).getString2())) {
                                        finalNameConf[0] = array.get(l);
                                        remove.add(array.get(l));
                                    }
                                }
                            }

                        }
                    }
                    for(int b=0;b<remove.size();b++) {
                        array.remove(remove.get(b));
                    }
                    ref3.child("Users").child(user.getUid()).child(sp.getString("number","")).child("table sitting").child("table "+(finalI +1)).setValue(array);
                }
            });

        }
        if(!finalNameConf[0].equals("")){
            checkConflicts(finalNameConf[0]);
        }
    }
    private void addValues(ArrayList<String> array, int i){
        CountDownLatch done = new CountDownLatch(0);
        ref3.child("Users").child(user.getUid()).child(sp.getString("number","")).child("table sitting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int j = 0; j<snapshot.child("table "+(i +1)).getChildrenCount(); j++){
                    array.add((String)snapshot.child("table "+(i +1)).child("person "+(j+1)).getValue());
                }
                done.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        try {
            done.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private int largestTable() {
        final int[] largestNumber = {0};
        ref3.child("Users").child(user.getUid()).child(sp.getString("number","")).child("parameters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int largest=0;
                for(int i=0;i<countObj;i++){
                    if(snapshot.child("object "+i).getValue().equals("bigCircle"))
                        if(largest<16) {
                            largest = 16;
                            largestNumber[0] = i;
                        }
                    if(snapshot.child("object "+i).getValue().equals("smallCircle"))
                        if(largest<8) {
                            largest = 8;
                            largestNumber[0] = i;
                        }
                    if(snapshot.child("object "+i).getValue().equals("rectangle"))
                        if(largest<20) {
                            largest = 20;
                            largestNumber[0] = i;
                        }
                    if(snapshot.child("object "+i).getValue().equals("verticalRectangle"))
                        if(largest<20) {
                            largest = 20;
                            largestNumber[0] = i;
                        }
                    if(snapshot.child("object "+i).getValue().equals("roundedRectangle"))
                        if(largest<18) {
                            largest = 18;
                            largestNumber[0] = i;
                        }
                    if(snapshot.child("object "+i).getValue().equals("verticalRoundedRectangle"))
                        if(largest<18) {
                            largest = 18;
                            largestNumber[0] = i;
                        }
                    if(snapshot.child("object "+i).getValue().equals("square"))
                        if(largest<14) {
                            largest = 14;
                            largestNumber[0] = i;
                        }
                    if(snapshot.child("object "+i).getValue().equals("custom"))
                        if(Math.toIntExact((Long)snapshot.child("custom").child("seets").getValue())>largest) {
                            largest = Math.toIntExact((Long) snapshot.child("custom").child("seets").getValue());
                            largestNumber[0] = i;
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return (largestNumber[0]+1);
    }
}
