package com.example.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PeopleFragment extends Fragment {
    View view;
    ListView list;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.people_fragment,container,false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = database.getReference();
        sp = getActivity().getSharedPreferences("currentProject", Context.MODE_PRIVATE);
        list = view.findViewById(R.id.listview_people_final);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(view.getContext(), R.layout.listview_items, R.id.peoplename, arrayList);
        list.setAdapter(adapter);
        ref.child("Users").child(user.getUid()).child(sp.getString("number","")).child("people").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i=0;i<snapshot.getChildrenCount();i++){
                    arrayList.add((String) snapshot.child("person "+(i+1)).getValue());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}
