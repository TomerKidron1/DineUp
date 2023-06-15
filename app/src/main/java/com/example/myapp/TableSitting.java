package com.example.myapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
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

public class TableSitting extends AppCompatActivity {
    TextView table_number;
    ImageView table_image;
    ListView people_list;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    SharedPreferences sp;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_arr);
        table_number = findViewById(R.id.table_number);
        table_image = findViewById(R.id.table_image);
        people_list = findViewById(R.id.list_view_table);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                R.layout.listview_items, R.id.peoplename, arrayList);
        people_list.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = database.getReference();
        int tableNum = (getIntent().getIntExtra("TableNumber",0))+1;
        Toast.makeText(this, ""+tableNum, Toast.LENGTH_SHORT).show();
        table_number.setText("Table "+tableNum);
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        ref.child("Users").child(user.getUid()).child(sp.getString("number","")).child("table sitting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i=0;i<snapshot.child("table "+tableNum).getChildrenCount();i++){
                    arrayList.add((String)snapshot.child(("table "+tableNum)).child("person "+(i+1)).getValue());
                    adapter.notifyDataSetChanged();
                }
                Toast.makeText(TableSitting.this, ""+arrayList, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child("Users").child(user.getUid()).child(sp.getString("number","")).child("parameters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                determinObject((String) snapshot.child("object "+(tableNum-1)).getValue(),table_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
