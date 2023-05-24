package com.example.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FoodFragment extends Fragment {
    View view;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    SharedPreferences sp;
    LinearLayout ll;
    Map<String,String> mapRetrieveObjects;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.food_fragment, container,false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = database.getReference();
        sp = getActivity().getSharedPreferences("currentProject", Context.MODE_PRIVATE);
        ll = view.findViewById(R.id.ll_food_final);
        mapRetrieveObjects = new HashMap<>();
        ref.child("Users").child(user.getUid()).child(sp.getString("number","")).child("food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i=0;i<snapshot.getChildrenCount();i++){
                    mapRetrieveObjects.put(""+(i+1), (String) snapshot.child(""+(i+1)).getValue());
                }
                addView(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;

    }

    private void addView(View view) {
        Collection<String> values = mapRetrieveObjects.values();
        ArrayList<String> array = new ArrayList<>(values);
        for(int i=0;i<mapRetrieveObjects.size();i++) {
            String name1 = "";
            int iend = array.get(i).indexOf("+");
            if (iend != -1) {
                name1 = array.get(i).substring(0, iend);
            }
            LinearLayout object = new LinearLayout(view.getContext());
            object.setOrientation(LinearLayout.HORIZONTAL);
            object.setGravity(Gravity.CENTER);
            Typeface typeface1 = ResourcesCompat.getFont(view.getContext(), R.font.inter_regular);
            TextView people = new TextView(view.getContext());
            people.setText(""+name1);
            people.setTypeface(typeface1);
            people.setTextSize(16);
            people.setTextColor(Color.BLACK);
            people.setGravity(Gravity.CENTER);
            object.addView(people);
            TextView brings = new TextView(view.getContext());
            brings.setText(" Brings ");
            brings.setGravity(Gravity.CENTER);
            brings.setTextColor(Color.BLACK);
            Typeface typeface2 = ResourcesCompat.getFont(view.getContext(), R.font.inter_semibold);
            brings.setTypeface(typeface2);
            brings.setTextSize(16);
            object.addView(brings);
            TextView food = new TextView(view.getContext());
            food.setTextColor(Color.BLACK);
            food.setText(""+array.get(i).substring(array.get(i).lastIndexOf("+")+1));
            food.setTextSize(16);
            food.setTypeface(typeface1);
            food.setGravity(Gravity.CENTER);
            object.addView(food);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,20,0,20);
            object.setLayoutParams(params);
            ll.addView(object);
        }
    }
}
