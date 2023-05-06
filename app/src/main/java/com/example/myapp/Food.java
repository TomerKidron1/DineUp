package com.example.myapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Food extends AppCompatActivity implements View.OnClickListener {
    ScrollView scrollView;
    Button next,navigate;
    ImageView plus;
    LinearLayout linearLayout;
    int count;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref,ref2;
    SharedPreferences sp;
    ArrayList<String> people;
    Dialog dialog;
    ArrayAdapter<String> adapter;
    ArrayList<Integer> removedIds;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food);
        scrollView = findViewById(R.id.scroll_view_food);
        next = findViewById(R.id.next_food);
        plus = findViewById(R.id.plus_food);
        navigate = findViewById(R.id.navigate_food);
        navigate.setOnClickListener(this);
        linearLayout = findViewById(R.id.ll_food);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        people = new ArrayList<>();
        removedIds = new ArrayList<>();
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        ref = database.getReference();
        ref2 = database.getReference();
        ref.child("Users").child(user.getUid()).child(sp.getString("number","")).addListenerForSingleValueEvent(new ValueEventListener() {
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
        next.setOnClickListener(this);
        plus.setOnClickListener(this);
        count=0;
        LinearLayout view1=new LinearLayout(this);
        view1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        params.gravity = Gravity.CENTER;
        view1.setLayoutParams(params);
        view1.setGravity(Gravity.CENTER);
        Button person = new Button(this);
        person.setBackgroundResource(R.drawable.rectangle_1_shape);
        person.setText("Name");
        person.setAllCaps(false);
        person.setTextSize(15);
        person.setId(count);
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(Food.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(1000,1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                EditText editText=dialog.findViewById(R.id.edit_text_dialog);
                ListView listView=dialog.findViewById(R.id.list_view_dialog);

                // Initialize array adapter
                adapter=new ArrayAdapter<>(Food.this, android.R.layout.simple_list_item_1,people);

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
                        person.setText(adapter.getItem(position));

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });
        count++;
        LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(450, 140);
        paramstext.setMargins(20,10,20,10);
        person.setLayoutParams(paramstext);
        view1.addView(person);
        ImageView dots = new ImageView(this);
        LinearLayout.LayoutParams paramsdots = new LinearLayout.LayoutParams(50, 100);
        dots.setLayoutParams(paramsdots);
        dots.setImageResource(R.drawable.twodots);
        view1.addView(dots);
        EditText food = new EditText(this);
        food.setHint("Food");
        food.setTextSize(15);
        food.setGravity(Gravity.CENTER);
        food.setHintTextColor(Color.BLACK);
        food.setLayoutParams(paramstext);
        food.setId(count);
        food.setBackgroundResource(R.drawable.rectangle_1_shape);
        food.setImeOptions(EditorInfo.IME_ACTION_DONE);
        food.setSingleLine();
        count++;
        view1.addView(food);
        linearLayout.addView(view1);
        person.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(Food.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.conflicts_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_conflict:
                                AlertDialog.Builder builder= new AlertDialog.Builder(Food.this);
                                builder.setTitle("Are you sure you want to delete?");
                                builder.setCancelable(true);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        linearLayout.removeView(view1);
                                        removedIds.add(person.getId());
                                        count=count-2;
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
        food.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(Food.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.conflicts_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_conflict:
                                AlertDialog.Builder builder= new AlertDialog.Builder(Food.this);
                                builder.setTitle("Are you sure you want to delete?");
                                builder.setCancelable(true);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        linearLayout.removeView(view);
                                        removedIds.add(person.getId());
                                        count=count-2;
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
    private void retrieveObjects(DataSnapshot snapshot) {
        for(int i=0;i<snapshot.child("people").getChildrenCount();i++){
            people.add((String) snapshot.child("people").child("person "+(i+1)).getValue());
        }
    }
    private void retrieveList(DataSnapshot snapshot){

    }
    @Override
    public void onClick(View view) {
        if(view == navigate){
            startActivity(new Intent(Food.this,Navigation.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        if(view ==next){

        }
        if(view ==plus){
            LinearLayout view1=new LinearLayout(this);
            view1.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            params.gravity = Gravity.CENTER;
            view1.setLayoutParams(params);
            view1.setGravity(Gravity.CENTER);
            Button person = new Button(this);
            person.setBackgroundResource(R.drawable.rectangle_1_shape);
            person.setText("Name");
            person.setTextSize(15);
            person.setId(count);
            person.setAllCaps(false);
            person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(Food.this);
                    dialog.setContentView(R.layout.dialog_searchable_spinner);
                    dialog.getWindow().setLayout(1000,1200);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    EditText editText=dialog.findViewById(R.id.edit_text_dialog);
                    ListView listView=dialog.findViewById(R.id.list_view_dialog);

                    // Initialize array adapter
                    adapter=new ArrayAdapter<>(Food.this, android.R.layout.simple_list_item_1,people);

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
                            person.setText(adapter.getItem(position));

                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }
            });
            count++;
            LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(450, 140);
            paramstext.setMargins(20,10,20,10);
            person.setLayoutParams(paramstext);
            view1.addView(person);
            ImageView dots = new ImageView(this);
            LinearLayout.LayoutParams paramsdots = new LinearLayout.LayoutParams(50, 100);
            dots.setLayoutParams(paramsdots);
            dots.setImageResource(R.drawable.twodots);
            view1.addView(dots);
            EditText food = new EditText(this);
            food.setHint("Food");
            food.setTextSize(15);
            food.setGravity(Gravity.CENTER);
            food.setLayoutParams(paramstext);
            food.setId(count);
            food.setImeOptions(EditorInfo.IME_ACTION_DONE);
            food.setSingleLine();
            food.setHintTextColor(Color.BLACK);
            food.setBackgroundResource(R.drawable.rectangle_1_shape);
            count++;
            view1.addView(food);
            linearLayout.addView(view1);
            person.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popup = new PopupMenu(Food.this, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.conflicts_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_conflict:
                                    AlertDialog.Builder builder= new AlertDialog.Builder(Food.this);
                                    builder.setTitle("Are you sure you want to delete?");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            linearLayout.removeView(view1);
                                            removedIds.add(person.getId());
                                            count=count-2;
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
            food.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popup = new PopupMenu(Food.this, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.conflicts_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_conflict:
                                    AlertDialog.Builder builder= new AlertDialog.Builder(Food.this);
                                    builder.setTitle("Are you sure you want to delete?");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            linearLayout.removeView(view1);
                                            removedIds.add(person.getId());
                                            count=count-2;
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
}
