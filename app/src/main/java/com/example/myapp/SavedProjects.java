package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class SavedProjects extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference ref,ref2,ref3;
    FirebaseUser user;
    ArrayList<SavedProject> saved = new ArrayList<>();
    ScrollView scrollView;
    LinearLayout linearLayout;
    Button back;
    TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_projects_test);
        back = findViewById(R.id.back_saved);
        back.setOnClickListener(this);
        scrollView = findViewById(R.id.scrollview5);
        linearLayout = findViewById(R.id.line_layout);
        auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
        ref2 = database.getReference("Users");
        ref3 = database.getReference("Authentication");
        name = findViewById(R.id.name_saved);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getDisplayName()!=null&&!user.getDisplayName().equals(""))
            name.setText(user.getDisplayName());
        else {
            int iend = user.getEmail().indexOf("@");
            String username = "";
            if (iend != -1)
                username = user.getEmail().substring(0, iend);
            final HashMap<String, String> nameMap = new HashMap<>();
            String finalUsername = username;
            final Map<String, String>[] map1 = new Map[]{new HashMap<>()};
            ref3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        if (messageSnapshot.getKey().equals(finalUsername)) {
                            map1[0] = (Map) messageSnapshot.getValue();
                            name.setText(map1[0].get("name"));
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            name.setText(nameMap.get("name"));
        }
        String userID = auth.getCurrentUser().getUid();
        DatabaseReference userRef = ref.child(userID);
        final Map<String, String>[] map = new Map[]{new HashMap<>()};
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    map[0] = (Map) postSnapshot.getValue();
                    saved.add(new SavedProject(map[0].get("name"),map[0].get("numberofpeople"),map[0].get("category"),map[0].get("date")));
                }
                link(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});

    }
    public void link(DataSnapshot snapshot){
        for(int i=0;i<saved.size();i++){
            LinearLayout button = new LinearLayout(SavedProjects.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,20,10,20);
            params.gravity = Gravity.CENTER;
            button.setLayoutParams(params);
            button.setElevation(20);
            button.setOrientation(LinearLayout.VERTICAL);
            TextView name = new TextView(SavedProjects.this);
            name.setTextSize(20);
            name.setTextColor(Color.BLACK);
            LinearLayout.LayoutParams paramsName = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsName.setMargins(20,20,0,0);
            name.setLayoutParams(paramsName);
            Typeface typeface = ResourcesCompat.getFont(SavedProjects.this,R.font.inter_bold);
            name.setTypeface(typeface);
            name.setText(saved.get(i).getProject_name());
            TextView category = new TextView(SavedProjects.this);
            LinearLayout.LayoutParams paramsCat = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsCat.setMargins(20,0,0,0);
            category.setTextSize(12);
            category.setTextColor(Color.BLACK);
            category.setLayoutParams(paramsCat);
            Typeface typeface1 = ResourcesCompat.getFont(SavedProjects.this,R.font.inter_regular);
            category.setTypeface(typeface1);
            category.setText("Category: "+saved.get(i).getCategory());
            TextView date = new TextView(SavedProjects.this);
            date.setTypeface(typeface1);
            date.setText("Date: "+saved.get(i).getDate());
            date.setTextColor(Color.BLACK);
            date.setTextSize(12);
            LinearLayout.LayoutParams paramsDate = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsDate.setMargins(20,0,0,40);
            date.setLayoutParams(paramsDate);
            button.addView(name);
            button.addView(category);
            button.addView(date);
            button.setForegroundGravity(Gravity.CENTER);
            button.setBackgroundResource(R.drawable.saved_proj_tiles);
            int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int rightNumber=0;
                    for(int j=0;j< snapshot.getChildrenCount();j++)
                    {
                        if(snapshot.child(String.valueOf(j)).child("name").getValue().equals(saved.get(finalI).getProject_name()))
                            rightNumber = j;

                    }
                    SharedPreferences sp= getSharedPreferences("currentProject",MODE_PRIVATE);
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putString("number", String.valueOf(rightNumber));
                    spe.commit();
                    startActivity(new Intent(SavedProjects.this,Navigation.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

                }
            });
            linearLayout.addView(button);
        }



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SavedProjects.this, MainScreen.class));
        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_left);
    }

    @Override
    public void onClick(View view) {
        if(view == back){
            startActivity(new Intent(SavedProjects.this,MainScreen.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
    }
}
