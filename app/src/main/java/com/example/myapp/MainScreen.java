package com.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Stack;


public class MainScreen extends AppCompatActivity implements View.OnClickListener {
    HorizontalScrollView recent;
    TextView name;
    TextView see_all;
    ImageButton new_project;
    ImageButton saved_projects;
    Button Birthday,BBQ,Dinner,Lunch,BarMitzva,settings,Custom;
    FirebaseDatabase database;
    DatabaseReference ref,ref2,ref3;
    FirebaseAuth auth;
    ArrayList<SavedProject> saved = new ArrayList<>();
    LinearLayout linearLayout;
    SharedPreferences sp1;
    SharedPreferences.Editor editor1;
    NetReciever netReciever = new NetReciever();



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        linearLayout = findViewById(R.id.lin_layout_main);
        recent = findViewById(R.id.recent_scroll);
        name = findViewById(R.id.name_main_screen);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Authentication");
        ref2 = database.getReference("Users");
        ref3 = database.getReference("Users");
        sp1 = getSharedPreferences("categoryPref",MODE_PRIVATE);
        editor1 = sp1.edit();
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
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
            final Map<String, String>[] map = new Map[]{new HashMap<>()};
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        if(messageSnapshot.getKey().equals(finalUsername)){
                            map[0] = (Map) messageSnapshot.getValue();
                            name.setText(map[0].get("name"));
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            name.setText(nameMap.get("name"));
        }
        new_project = findViewById(R.id.new_project_main);
        settings = findViewById(R.id.settings_button);
        settings.setOnClickListener(this);
        new_project.setOnClickListener(this);
        saved_projects = findViewById(R.id.saved_projects_button_main);
        saved_projects.setOnClickListener(this);
        see_all = findViewById(R.id.see_all_button_main);
        Birthday = findViewById(R.id.birthday_button_main);
        Birthday.setOnClickListener(this);
        BBQ = findViewById(R.id.bbq_button_main);
        BBQ.setOnClickListener(this);
        Dinner = findViewById(R.id.dinner_button_main);
        Dinner.setOnClickListener(this);
        Lunch = findViewById(R.id.lunch_button_main);
        Lunch.setOnClickListener(this);
        BarMitzva = findViewById(R.id.barmitzva_button_main);
        BarMitzva.setOnClickListener(this);
        Custom = findViewById(R.id.custom_button_main);
        Custom.setOnClickListener(this);
        see_all.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        DatabaseReference userRef = ref2.child(userID);
        final Map<String, String>[] map = new Map[]{new HashMap<>()};
        Stack<SavedProject> stack = new Stack<>();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    map[0] = (Map) postSnapshot.getValue();
                    stack.add(new SavedProject(map[0].get("name"),map[0].get("numberofpeople"),map[0].get("category"),map[0].get("date")));
                }
                int size = stack.size();
                for(int j=0;j<size;j++){
                    saved.add(stack.pop());
                }
                for (int i = 0; i < 7&&i<saved.size(); i++) {
                    LinearLayout button = new LinearLayout(MainScreen.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 20, 10, 20);
                    params.gravity = Gravity.CENTER;
                    button.setLayoutParams(params);
                    button.setElevation(20);
                    button.setOrientation(LinearLayout.VERTICAL);
                    TextView name = new TextView(MainScreen.this);
                    name.setTextSize(20);
                    name.setTextColor(Color.BLACK);
                    LinearLayout.LayoutParams paramsName = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramsName.setMargins(20, 20, 0, 0);
                    name.setLayoutParams(paramsName);
                    Typeface typeface = ResourcesCompat.getFont(MainScreen.this, R.font.inter_bold);
                    name.setTypeface(typeface);
                    name.setText(saved.get(i).getProject_name());
                    TextView category = new TextView(MainScreen.this);
                    LinearLayout.LayoutParams paramsCat = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramsCat.setMargins(20, 0, 0, 0);
                    category.setTextSize(12);
                    category.setTextColor(Color.BLACK);
                    category.setLayoutParams(paramsCat);
                    Typeface typeface1 = ResourcesCompat.getFont(MainScreen.this, R.font.inter_regular);
                    category.setTypeface(typeface1);
                    category.setText("Category: " + saved.get(i).getCategory());
                    TextView date = new TextView(MainScreen.this);
                    date.setTypeface(typeface1);
                    date.setText("Date: " + saved.get(i).getDate());
                    date.setTextColor(Color.BLACK);
                    date.setTextSize(12);
                    LinearLayout.LayoutParams paramsDate = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramsDate.setMargins(20, 0, 0, 20);
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
                            startActivity(new Intent(MainScreen.this,Navigation.class));
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        }
                    });
                    linearLayout.addView(button);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});
    }
    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReciever,intentFilter);
        super.onStart();

    }

    @Override
    protected void onStop() {
        unregisterReceiver(netReciever);
        super.onStop();
    }


    @Override
    public void onClick(View view) {
        if(view == see_all){
            startActivity(new Intent(MainScreen.this,SavedProjects.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(view == new_project){
            startActivity(new Intent(MainScreen.this,NewProject.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(view == saved_projects){
            startActivity(new Intent(MainScreen.this,SavedProjects.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(view == settings){
            showPopUp(view);
        }
        if(view == Birthday){
            editor1.putString("pref","Birthday");
            editor1.commit();
            startActivity(new Intent(MainScreen.this,SavedByCategory.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(view == Dinner){
            editor1.putString("pref","Dinner");
            editor1.commit();
            startActivity(new Intent(MainScreen.this,SavedByCategory.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(view == Lunch){
            editor1.putString("pref","Lunch");
            editor1.commit();
            startActivity(new Intent(MainScreen.this,SavedByCategory.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(view == BarMitzva){
            editor1.putString("pref","BarMitzva");
            editor1.commit();
            startActivity(new Intent(MainScreen.this,SavedByCategory.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(view == BBQ){
            editor1.putString("pref","BBQ");
            editor1.commit();
            startActivity(new Intent(MainScreen.this,SavedByCategory.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        if(view == Custom){
            editor1.putString("pref","Custom");
            editor1.commit();
            startActivity(new Intent(MainScreen.this,SavedByCategory.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
    }
    public void showPopUp(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.project_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        AlertDialog.Builder builder= new AlertDialog.Builder(MainScreen.this);
                        builder.setTitle("Do you want to sign out?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                                SharedPreferences.Editor spe = sp.edit();
                                spe.putString("name","");
                                spe.putString("email", "");
                                spe.putString("password","");
                                spe.commit();
                                startActivity(new Intent(MainScreen.this, MainActivity.class));
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
