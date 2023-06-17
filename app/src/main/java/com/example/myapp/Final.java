package com.example.myapp;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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
    SharedPreferences sp,sp2;
    int numberObj=0,numberCustom=0,countObj=0;
    ArrayList<String> people;
    ArrayList<DoubleString> conflicts;
    String returnedValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_arrangement);
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        sp2 = getSharedPreferences("setTheTable", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp2.edit();
        returnedValue = "";
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
        if(sp2.getString("done","no").equals("no")){
            ref3.child("Users").child(user.getUid()).child(sp.getString("number","")).child("table sitting").removeValue();
        }
        ref.child("Users").child(user.getUid()).child(sp.getString("number","")).child("parameters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    numberObj = (int) snapshot.getChildrenCount();
                    if (snapshot.hasChild("custom"))
                        numberObj = (numberObj - 1) / 3;
                    else
                        numberObj = numberObj / 3;
                    for (int i = 0; i < numberObj; i++) {
                        if (snapshot.child("custom").exists() && snapshot.child("object " + i).equals("custom")) {
                            numberCustom++;
                            LinearLayout linearLayout = new LinearLayout(Final.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(600, 400);
                            params.gravity = Gravity.CENTER;
                            linearLayout.setBackgroundResource(R.drawable.frame_layout);
                            params.bottomMargin = 30;
                            linearLayout.setLayoutParams(params);
                            ll.setGravity(Gravity.CENTER);
                            ImageView obj = new ImageView(Final.this);
                            obj.setImageResource(R.drawable.custom);
                            int height = Math.toIntExact((Long) snapshot.child("custom").child("height " + numberCustom).getValue());
                            int width = Math.toIntExact((Long) snapshot.child("custom").child("width " + numberCustom).getValue());
                            LinearLayout.LayoutParams objParmas = new LinearLayout.LayoutParams(width, height);
                            objParmas.gravity = Gravity.CENTER;
                            obj.setLayoutParams(objParmas);
                            obj.setForegroundGravity(Gravity.CENTER);
                            linearLayout.setId(countObj);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setGravity(Gravity.CENTER);
                            TextView tableNumber = new TextView(Final.this);
                            tableNumber.setText("Table " + (countObj + 1));
                            tableNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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
                                    Intent i = new Intent(Final.this, TableSitting.class);
                                    i.putExtra("TableNumber", linearLayout.getId());
                                    startActivity(i);
                                }
                            });
                        } else {
                            LinearLayout linearLayout = new LinearLayout(Final.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(600, 400);
                            params.gravity = Gravity.CENTER;
                            linearLayout.setBackgroundResource(R.drawable.frame_layout);
                            params.bottomMargin = 30;
                            linearLayout.setLayoutParams(params);
                            ll.setGravity(Gravity.CENTER);
                            ImageView obj = new ImageView(Final.this);
                            determinObject((String) snapshot.child("object " + i).getValue(), obj);
                            LinearLayout.LayoutParams objParmas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            objParmas.gravity = Gravity.CENTER;
                            obj.setLayoutParams(objParmas);
                            obj.setForegroundGravity(Gravity.CENTER);
                            linearLayout.setId(countObj);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setGravity(Gravity.CENTER);
                            TextView tableNumber = new TextView(Final.this);
                            tableNumber.setText("Table " + (countObj + 1));
                            tableNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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
                                    Intent i = new Intent(Final.this, TableSitting.class);
                                    i.putExtra("TableNumber", (linearLayout.getId()));
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
             ArrayList<ArrayList<String>> arrayList = new ArrayList<>();
             for(int k=0;k<countObj;k++){
                 if(!people.isEmpty()) {
                     ArrayList<String> arrayList1 = new ArrayList<>();
                     arrayList1.add(people.get(0));
                     people.remove(0);
                     arrayList.add(arrayList1);
                 }
             }
             while(!people.isEmpty()){
                 for(int i=0;i<countObj;i++){
                     if(!people.isEmpty()){
                         arrayList.get(i).add(people.get(0));
                         people.remove(0);
                     }
                 }
             }
             if(arrayList.size()<countObj){
                 showPopUp2();
             }
             else {
                 submitToFirebase(arrayList);
                 if (!returnedValue.equals("")) {
                     while (!returnedValue.equals("")) {
                         submitToFirebase(arrayList);
                     }
                 }
             }


    }
    private void submitToFirebase(ArrayList<ArrayList<String>> arrayList){
        for(int i=0;i<countObj;i++){
            Map<String, String> map = new HashMap<>();
            for(int h=0;h<arrayList.get(i).size();h++){
                conflictsArrange(arrayList.get(i));
                map.put("person "+h, arrayList.get(i).get(h));
            }
            ref3.child("Users").child(user.getUid()).child(sp.getString("number","")).child("table sitting").child("table "+(i+1)).setValue(map);
        }
    }
    private void conflictsArrange(ArrayList<String> array){
        if(!returnedValue.equals("")){
            array.add(returnedValue);
            returnedValue= "";
        }
        for(int i=0;i<conflicts.size();i++){
            ArrayList<String> conf = new ArrayList<>();
            conf.add(conflicts.get(i).getString1());
            conf.add(conflicts.get(i).getString2());
            if(array.containsAll(conf)){
                returnedValue = conf.get(0);
                array.remove(conf.get(0));
            }
        }
    }
    private void showPopUp2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Final.this);
        builder.setTitle("Not enough people for that amount of tables");
        builder.setMessage("Would you like to return to the table Arrangement section and try again?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Final.this,TableSettings.class));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

    private void checkConflicts(String nameConf) {
        final String[] finalNameConf = {nameConf};
        for (int i = 0; i < countObj; i++) {
            ArrayList<String> arrayList = new ArrayList<>();
            if(!returnedValue.equals("")){
                arrayList.add(returnedValue);
                returnedValue ="";
            }
            //addValues(arrayList, i);
            int finalI = i;
            Utils.delay(1, new Utils.DelayCallback() {
                @Override
                public void afterDelay() {
                    for (int j = 0; j < conflicts.size(); j++) {
                        ArrayList<String> current = new ArrayList<>();
                        current.add(conflicts.get(j).getString1());
                        current.add(conflicts.get(j).getString2());
                        if (arrayList.containsAll(current)) {
                            boolean flag = false;
                            for (int s = 0; (flag == false) || (s < arrayList.size()); s++) {
                                if (arrayList.get(s).equals(current.get(0))) {
                                    returnedValue = arrayList.get(s);
                                    arrayList.remove(s);
                                    flag = true;
                                }
                            }
                        }
                    }
                    ref3.child("Users").child(user.getUid()).child(sp.getString("number", "")).child("table sitting").child("table " + (finalI + 1)).setValue(arrayList);
                }
            });

        }

    }
    private void addValues(ArrayList<String> array, int i){
        ref3.child("Users").child(user.getUid()).child(sp.getString("number","")).child("table sitting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int j = 0; j<snapshot.child("table "+(i +1)).getChildrenCount(); j++){
                    array.add((String)snapshot.child("table "+(i +1)).child("person "+j).getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}
