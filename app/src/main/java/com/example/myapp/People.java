package com.example.myapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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

public class People extends AppCompatActivity implements View.OnClickListener {
    ListView people_list;
    Button next,navigate;
    ImageView plus;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;
    SharedPreferences sp;
    SharedPreferences sp1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref=ref.child("Users");
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        sp1 = getSharedPreferences("questions",MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        people_list = findViewById(R.id.listview_people);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                R.layout.listview_items, R.id.peoplename, arrayList);
        people_list.setAdapter(adapter);
        people_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showPopUp(view,adapter.getItem(i));
                return true;
            }
        });
        next = findViewById(R.id.next_people);
        plus = findViewById(R.id.plus_people);
        navigate = findViewById(R.id.navigate_people);
        navigate.setOnClickListener(this);
        plus.setOnClickListener(this);
        next.setOnClickListener(this);
        ref.child(user.getUid()).child(sp.getString("number","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>5) {
                    retrieveObjects(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveObjects(DataSnapshot snapshot) {
        for(int i=0;i<snapshot.child("people").getChildrenCount();i++){
            arrayList.add((String) snapshot.child("people").child("person "+(i+1)).getValue());
            adapter.notifyDataSetChanged();
        }
    }

    private void showPopUp(View v,String name) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.people_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_people:
                        AlertDialog.Builder builder= new AlertDialog.Builder(People.this);
                        builder.setTitle("Are you sure you want to delete?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrayList.remove(name);
                                adapter.notifyDataSetChanged();
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
                    case R.id.rename_people:
                        
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    @Override
    public void onClick(View view) {
        if(view == plus){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter new person Name");
            LinearLayout linear = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final EditText editText = new EditText(this);
            editText.setHint("Name");
            editText.setLayoutParams(params);
            editText.setSingleLine(true);
            linear.addView(editText);
            builder.setView(linear);
            builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    arrayList.add(editText.getText().toString());
                    adapter.notifyDataSetChanged();
                }
            });
            builder.setCancelable(true);
            builder.show();
        }
        if(view == next){
            String userId = user.getUid();
            Map<String,String> map = new HashMap<>();
           for(int i=0;i<arrayList.size();i++){
               map.put("person "+(i+1),arrayList.get(i));
           }
           ref.child(userId).child(sp.getString("number","")).child("people").setValue(map);
           if(sp1.getString("answer2","").equals("yes")) {
               startActivity(new Intent(this, Conflicts.class));
               overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
           }
           else if(sp1.getString("answer3","").equals("yes")) {
               startActivity(new Intent(this, Food.class));
               overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
           }
           else{

           }


        }
        if(view == navigate){
            startActivity(new Intent(People.this,Navigation.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}
