package com.example.myapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewProject extends AppCompatActivity implements View.OnClickListener {
    EditText projectname,numberpeople;
    Button next;
    TextView datepick;
    ImageView popup,table;
    Spinner category;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newprojecttest);
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        spe = sp.edit();
        projectname = findViewById(R.id.projectname2);
        numberpeople = findViewById(R.id.numberofpeople2);
        next = findViewById(R.id.nextbutton);
        datepick = findViewById(R.id.date_picker);
        category = findViewById(R.id.categoryspinner);
        mAuth = FirebaseAuth.getInstance();
        table =findViewById(R.id.tableimage);
        database =FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
        popup = findViewById(R.id.menuimage5);
        Animation animation = AnimationUtils.loadAnimation(NewProject.this,R.anim.upanddown);
        animation.setDuration(9000);
        table.startAnimation(animation);
        popup.setOnClickListener(this);
        datepick.setOnClickListener(this);
        next.setOnClickListener(this);
        String [] categories = new String[]{"Category", "Birthday", "Dinner", "Lunch", "BBQ", "Bar Mitzva","Custom"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==6){
                    AlertDialog.Builder builder2= new AlertDialog.Builder(NewProject.this);
                    builder2.setTitle("Enter Name pf Category");
                    final EditText input = new EditText(NewProject.this);
                    builder2.setView(input);
                    builder2.setCancelable(true);
                    builder2.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            category.setTag(input.getText().toString());
                        }
                    });
                    builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder2.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if(view == next){
            if(!numberpeople.getText().toString().equals("")&&!projectname.getText().toString().equals("")&&category.getSelectedItem()!="Category") {
                Map<String,String> user = new HashMap<>();
                user.put("numberofpeople",numberpeople.getText().toString());
                if(category.getSelectedItem()=="Custom")
                    user.put("category",category.getTag().toString());
                else
                    user.put("category",category.getSelectedItem().toString());
                user.put("date",datepick.getText().toString());
                user.put("name",projectname.getText().toString());
                FirebaseUser user1 = mAuth.getCurrentUser();
                String userID =user1.getUid();
                DatabaseReference ref2 = ref.child(userID);
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean flag=false;
                        int i=0;
                        while(!flag){
                            if(!snapshot.hasChild(i+"")){
                                ref.child(userID).child(i+"").setValue(user);
                                spe.putString("number",String.valueOf(i));
                                spe.commit();
                                Toast.makeText(NewProject.this,"data added",Toast.LENGTH_LONG).show();
                                flag=true;
                                break;
                            }
                            i++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                startActivity(new Intent(NewProject.this, TableSettings.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else{
                Toast.makeText(NewProject.this, "Enter parameters above",Toast.LENGTH_LONG).show();
            }
        }
        if(view==datepick){

            final Calendar c= Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(NewProject.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    datepick.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                }
            },year,month,day);
            dialog.show();
        }
        if(view == popup){
            showPopUp(view);
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
                        AlertDialog.Builder builder= new AlertDialog.Builder(NewProject.this);
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
                                startActivity(new Intent(NewProject.this, MainActivity.class));
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
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(NewProject.this, MainScreen.class));
        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_left);
    }
}
