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

public class Final extends AppCompatActivity implements View.OnClickListener {
    Button nav,peoplelist;
    ImageView share;
    ScrollView scrollView;
    LinearLayout ll;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    SharedPreferences sp;
    int numberObj=0,numberCustom=0,countObj=0;
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
}
