package com.example.myapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.IdRes;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Conflicts extends AppCompatActivity implements View.OnClickListener {
    ScrollView scrollView;
    LinearLayout linearLayout;
    Button next,navigate;
    ImageView plus;
    int count,arrow_count;
    FirebaseDatabase database;
    DatabaseReference ref,refPeople,refConflicts,refConflictsObjects;
    SharedPreferences sp;
    Dialog dialog;
    ArrayList<String> people,conflicts;
    FirebaseUser user;
    FirebaseAuth auth;
    Map<String,String> mapRetrieveObjects;
    ArrayAdapter<String> adapter,adapter2;
    Map<String,String> mapConflicts,mapComplete;
    ArrayList<Integer> removedIds;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conflicts);
        scrollView= findViewById(R.id.scroll_view_conflicts);
        linearLayout= findViewById(R.id.ll_conflicts);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        refPeople = database.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        people = new ArrayList<>();
        mapConflicts = new HashMap<>();
        mapComplete = new HashMap<>();
        conflicts = new ArrayList<>();
        arrow_count = 0;
        removedIds = new ArrayList<>();
        mapRetrieveObjects = new HashMap<>();
        refConflictsObjects = database.getReference();
        refConflicts = database.getReference();
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        refPeople.child("Users").child(user.getUid()).child(sp.getString("number","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>=6) {
                    retrieveObjects(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        next=findViewById(R.id.next_conflicts);
        plus=findViewById(R.id.plus_conflicts);
        next.setOnClickListener(this);
        plus.setOnClickListener(this);
        navigate = findViewById(R.id.navigation_conflicts);
        navigate.setOnClickListener(this);
        count=0;
        refConflictsObjects.child("Users").child(user.getUid()).child(sp.getString("number","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!retrieveConflicts(snapshot)){
                    LinearLayout view=new LinearLayout(Conflicts.this);
                    view.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 10);
                    params.gravity = Gravity.CENTER;
                    view.setLayoutParams(params);
                    view.setGravity(Gravity.CENTER);
                    Button person1 = new Button(Conflicts.this);
                    person1.setBackgroundResource(R.drawable.rectangle_1_shape);
                    person1.setText("Name");
                    person1.setTextSize(15);
                    person1.setId(count);
                    person1.setAllCaps(false);
                    count++;
                    LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(400, 140);
                    paramstext.setMargins(20,10,20,10);
                    person1.setLayoutParams(paramstext);
                    view.addView(person1);
                    ImageView arrow = new ImageView(Conflicts.this);
                    arrow.setImageResource(R.drawable.arrow);
                    view.addView(arrow);
                    Button person2 = new Button(Conflicts.this);
                    person2.setBackgroundResource(R.drawable.rectangle_1_shape);
                    person2.setText("Name");
                    person2.setTextSize(15);
                    person2.setLayoutParams(paramstext);
                    person2.setId(count);
                    person2.setAllCaps(false);
                    count++;
                    view.addView(person2);
                    linearLayout.addView(view);
                    person1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog = new Dialog(Conflicts.this);
                            dialog.setContentView(R.layout.dialog_searchable_spinner);
                            dialog.getWindow().setLayout(1000,1200);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                            EditText editText=dialog.findViewById(R.id.edit_text_dialog);
                            ListView listView=dialog.findViewById(R.id.list_view_dialog);

                            // Initialize array adapter
                            adapter=new ArrayAdapter<>(Conflicts.this, android.R.layout.simple_list_item_1,people);

                            // set adapter
                            listView.setAdapter(adapter);
                            editText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapter.getFilter().filter(s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // when item selected from list
                                    // set selected item on textView
                                    person1.setText(adapter.getItem(position));
                                    mapConflicts.put(""+person1.getText().toString(),"empty");

                                    // Dismiss dialog
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                    person1.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            PopupMenu popup = new PopupMenu(Conflicts.this, view);
                            MenuInflater inflater = popup.getMenuInflater();
                            inflater.inflate(R.menu.conflicts_menu, popup.getMenu());
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.delete_conflict:
                                            AlertDialog.Builder builder= new AlertDialog.Builder(Conflicts.this);
                                            builder.setTitle("Are you sure you want to delete?");
                                            builder.setCancelable(true);
                                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    linearLayout.removeView(view);
                                                    count=count-2;
                                                    removedIds.add(person1.getId());

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
                            return false;
                        }
                    });

                    person2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog = new Dialog(Conflicts.this);
                            dialog.setContentView(R.layout.dialog_searchable_spinner);
                            dialog.getWindow().setLayout(1000,1200);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                            EditText editText=dialog.findViewById(R.id.edit_text_dialog);
                            ListView listView=dialog.findViewById(R.id.list_view_dialog);

                            // Initialize array adapter
                            adapter=new ArrayAdapter<>(Conflicts.this, android.R.layout.simple_list_item_1,people);

                            // set adapter
                            listView.setAdapter(adapter);
                            editText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapter.getFilter().filter(s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // when item selected from list
                                    // set selected item on textView
                                    person2.setText(adapter.getItem(position));
                                    mapConflicts.replace(""+person1.getText().toString(),""+person2.getText().toString());
                                    // Dismiss dialog
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                    person2.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            PopupMenu popup = new PopupMenu(Conflicts.this, view);
                            MenuInflater inflater = popup.getMenuInflater();
                            inflater.inflate(R.menu.conflicts_menu, popup.getMenu());
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.delete_conflict:
                                            AlertDialog.Builder builder= new AlertDialog.Builder(Conflicts.this);
                                            builder.setTitle("Are you sure you want to delete?");
                                            builder.setCancelable(true);
                                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    linearLayout.removeView(view);
                                                    count=count-2;
                                                    removedIds.add(person1.getId());
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
                            return false;
                        }
                    });
                }
                else{
                    retrieveConflicts(snapshot);
                    addViews();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addViews() {
        Collection<String> values = mapRetrieveObjects.values();
        ArrayList<String> array = new ArrayList<>(values);
        for(int i =0;i<mapRetrieveObjects.size();i++) {
            String name1 = "";
            int iend = array.get(i).indexOf("+");
            if (iend != -1) {
                name1 = array.get(i).substring(0, iend);
            }
            LinearLayout view1 = new LinearLayout(this);
            view1.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            params.gravity = Gravity.CENTER;
            view1.setLayoutParams(params);
            view1.setGravity(Gravity.CENTER);
            Button person1 = new Button(this);
            person1.setBackgroundResource(R.drawable.rectangle_1_shape);
            person1.setText(""+name1);
            person1.setTextSize(15);
            person1.setId(count);
            person1.setAllCaps(false);
            count++;
            person1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(Conflicts.this);
                    dialog.setContentView(R.layout.dialog_searchable_spinner);
                    dialog.getWindow().setLayout(1000, 1200);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    EditText editText = dialog.findViewById(R.id.edit_text_dialog);
                    ListView listView = dialog.findViewById(R.id.list_view_dialog);

                    // Initialize array adapter
                    adapter = new ArrayAdapter<>(Conflicts.this, android.R.layout.simple_list_item_1, people);

                    // set adapter
                    listView.setAdapter(adapter);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // when item selected from list
                            // set selected item on textView
                            person1.setText(adapter.getItem(position));

                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }
            });
            LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(400, 140);
            paramstext.setMargins(20, 10, 20, 10);
            person1.setLayoutParams(paramstext);
            view1.addView(person1);
            ImageView arrow = new ImageView(this);
            arrow.setImageResource(R.drawable.arrow);
            view1.addView(arrow);
            Button person2 = new Button(this);
            person2.setBackgroundResource(R.drawable.rectangle_1_shape);
            person2.setText(""+array.get(i).substring(array.get(i).lastIndexOf("+")+1));
            person2.setAllCaps(false);
            person2.setTextSize(15);
            person2.setLayoutParams(paramstext);
            person2.setId(count);
            count++;
            person2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(Conflicts.this);
                    dialog.setContentView(R.layout.dialog_searchable_spinner);
                    dialog.getWindow().setLayout(1000, 1200);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    EditText editText = dialog.findViewById(R.id.edit_text_dialog);
                    ListView listView = dialog.findViewById(R.id.list_view_dialog);

                    // Initialize array adapter
                    adapter = new ArrayAdapter<>(Conflicts.this, android.R.layout.simple_list_item_1, people);

                    // set adapter
                    listView.setAdapter(adapter);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // when item selected from list
                            // set selected item on textView
                            person2.setText(adapter.getItem(position));

                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }
            });
            view1.addView(person2);
            linearLayout.addView(view1);
            person1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popup = new PopupMenu(Conflicts.this, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.conflicts_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_conflict:
                                    AlertDialog.Builder builder= new AlertDialog.Builder(Conflicts.this);
                                    builder.setTitle("Are you sure you want to delete?");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            linearLayout.removeView(view1);
                                            count=count-2;
                                            removedIds.add(person1.getId());
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
                    return false;
                }
            });
            person2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popup = new PopupMenu(Conflicts.this, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.conflicts_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_conflict:
                                    AlertDialog.Builder builder= new AlertDialog.Builder(Conflicts.this);
                                    builder.setTitle("Are you sure you want to delete?");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            linearLayout.removeView(view1);
                                            count=count-2;
                                            removedIds.add(person1.getId());
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
                    return false;
                }
            });
        }
    }

    private void retrieveObjects(DataSnapshot snapshot) {
        for(int i=0;i<snapshot.child("people").getChildrenCount();i++){
            people.add((String) snapshot.child("people").child("person "+(i+1)).getValue());
        }
    }
    private boolean retrieveConflicts(DataSnapshot snapshot) {
        if(!snapshot.child("conflicts").exists()){
            return false;
        }
        for(int i=0;i<snapshot.child("conflicts").getChildrenCount();i++){
            mapRetrieveObjects.put(""+(i+1), (String) snapshot.child("conflicts").child(""+(i+1)).getValue());
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view==next){
            if(removedIds.size()>0)
                rearrageViews();
            Button test = findViewById(0);
            String name="";
            boolean flag = true;
            if(test != null) {
                if (!test.getText().toString().equals("Name"))
                    name = test.getText().toString();
            }
            for(int i=0; i<count&&flag == true; i++){
                Button button = findViewById(i);
                if(button==null){
                    continue;
                }
                if(button.getText().equals("Name")){
                    Toast.makeText(this, "There are empty names", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                else {
                    if(i!=0&&name.equals(button.getText().toString())&&i%2!=0){
                        Toast.makeText(this, "There is 1 person in different sides of conflict", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                }
                name = button.getText().toString();
            }
            if(flag){
                String name1="";
                int count1=1;
                for(int i=0; i<count; i++){
                    Button button1 = findViewById(i);
                    if(i!=0&&i%2!=0){
                        mapComplete.put(count1+"",name1+"+"+button1.getText().toString());
                        count1++;
                    }
                    name1 = button1.getText().toString();
                }
                Toast.makeText(this, ""+removedIds, Toast.LENGTH_SHORT).show();
                refConflicts.child("Users").child(user.getUid()).child(sp.getString("number","")).child("conflicts").setValue(mapComplete);
                startActivity(new Intent(this,Food.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        }
        if(view==plus){
            LinearLayout view1=new LinearLayout(this);
            view1.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            params.gravity = Gravity.CENTER;
            view1.setLayoutParams(params);
            view1.setGravity(Gravity.CENTER);
            Button person1 = new Button(this);
            person1.setBackgroundResource(R.drawable.rectangle_1_shape);
            person1.setText("Name");
            person1.setTextSize(15);
            person1.setId(count);
            person1.setAllCaps(false);
            count++;
            person1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(Conflicts.this);
                    dialog.setContentView(R.layout.dialog_searchable_spinner);
                    dialog.getWindow().setLayout(1000,1200);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    EditText editText=dialog.findViewById(R.id.edit_text_dialog);
                    ListView listView=dialog.findViewById(R.id.list_view_dialog);

                    // Initialize array adapter
                    adapter=new ArrayAdapter<>(Conflicts.this, android.R.layout.simple_list_item_1,people);

                    // set adapter
                    listView.setAdapter(adapter);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // when item selected from list
                            // set selected item on textView
                            person1.setText(adapter.getItem(position));

                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }
            });
            LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(400, 140);
            paramstext.setMargins(20,10,20,10);
            person1.setLayoutParams(paramstext);
            view1.addView(person1);
            ImageView arrow = new ImageView(this);
            arrow.setImageResource(R.drawable.arrow);
            view1.addView(arrow);
            Button person2 = new Button(this);
            person2.setBackgroundResource(R.drawable.rectangle_1_shape);
            person2.setText("Name");
            person2.setAllCaps(false);
            person2.setTextSize(15);
            person2.setLayoutParams(paramstext);
            person2.setId(count);
            count++;
            person2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(Conflicts.this);
                    dialog.setContentView(R.layout.dialog_searchable_spinner);
                    dialog.getWindow().setLayout(1000,1200);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    EditText editText=dialog.findViewById(R.id.edit_text_dialog);
                    ListView listView=dialog.findViewById(R.id.list_view_dialog);

                    // Initialize array adapter
                    adapter=new ArrayAdapter<>(Conflicts.this, android.R.layout.simple_list_item_1,people);

                    // set adapter
                    listView.setAdapter(adapter);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // when item selected from list
                            // set selected item on textView
                            person2.setText(adapter.getItem(position));

                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }
            });
            view1.addView(person2);
            linearLayout.addView(view1);
            person1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popup = new PopupMenu(Conflicts.this, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.conflicts_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_conflict:
                                    AlertDialog.Builder builder= new AlertDialog.Builder(Conflicts.this);
                                    builder.setTitle("Are you sure you want to delete?");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            linearLayout.removeView(view1);
                                            count=count-2;
                                            removedIds.add(person1.getId());
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
                    return false;
                }
            });
            person2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popup = new PopupMenu(Conflicts.this, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.conflicts_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_conflict:
                                    AlertDialog.Builder builder= new AlertDialog.Builder(Conflicts.this);
                                    builder.setTitle("Are you sure you want to delete?");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            linearLayout.removeView(view1);
                                            count=count-2;
                                            removedIds.add(person1.getId());
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
                    return false;
                }
            });
        }
        if(view == navigate){
            startActivity(new Intent(Conflicts.this,Navigation.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
    }
    private void rearrageViews(){
        ArrayList<Integer> ids = new ArrayList<>();
        Button person1 = null;
        int countfast = 0;
        for(int i=0; i<(count+(removedIds.size()*2)); i++){
            Button person2 = findViewById(i);
            if(removedIds.contains(i)||person2 == null){
                continue;
            }
            if(i!=0&&i%2!=0){
                ids.add(person1.getId());
                ids.add(person2.getId());
            }
            person1 = person2;
            countfast++;

        }
        Toast.makeText(this, ""+removedIds, Toast.LENGTH_SHORT).show();
        int countDone = 0;
        for(int j=0;j<(count/2);j++){
            Button bt1 = findViewById(ids.get(countDone));
            countDone++;
            Button bt2 = findViewById(ids.get(countDone));
            countDone--;
            bt1.setId(countDone);
            countDone++;
            bt2.setId(countDone);
            countDone++;
        }
    }
}
