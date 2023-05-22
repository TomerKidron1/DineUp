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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Food extends AppCompatActivity implements View.OnClickListener {
    ScrollView scrollView;
    Button next, navigate;
    Button plus;
    LinearLayout linearLayout;
    int count;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref, ref2,ref3;
    SharedPreferences sp;
    ArrayList<String> people;
    Dialog dialog;
    ArrayAdapter<String> adapter;
    ArrayList<Integer> removedIds;
    Map<String, String> mapComplete;
    Map<String,String> mapRetrieveObjects;


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
        mapComplete = new HashMap<>();
        mapRetrieveObjects = new HashMap<>();
        sp = getSharedPreferences("currentProject", MODE_PRIVATE);
        ref = database.getReference();
        ref2 = database.getReference();
        ref3 = database.getReference();
        ref.child("Users").child(user.getUid()).child(sp.getString("number", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() >= 6) {
                    retrieveObjects(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        next.setOnClickListener(this);
        plus.setOnClickListener(this);
        count = 0;
        ref3.child("Users").child(user.getUid()).child(sp.getString("number", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!retrieveList(snapshot)){
                    LinearLayout view1 = new LinearLayout(Food.this);
                    view1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 10);
                    params.gravity = Gravity.CENTER;
                    view1.setLayoutParams(params);
                    view1.setGravity(Gravity.CENTER);
                    Button person = new Button(Food.this);
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
                            dialog.getWindow().setLayout(1000, 1200);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                            EditText editText = dialog.findViewById(R.id.edit_text_dialog);
                            ListView listView = dialog.findViewById(R.id.list_view_dialog);
                            Button cancel = dialog.findViewById(R.id.cancel_dialog);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();
                                }
                            });
                            // Initialize array adapter
                            adapter = new ArrayAdapter<>(Food.this, android.R.layout.simple_list_item_1, people);

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
                    LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(300, 90);
                    paramstext.setMargins(20, 10, 20, 10);
                    person.setLayoutParams(paramstext);
                    view1.addView(person);
                    ImageView dots = new ImageView(Food.this);
                    LinearLayout.LayoutParams paramsdots = new LinearLayout.LayoutParams(50, 100);
                    dots.setLayoutParams(paramsdots);
                    dots.setImageResource(R.drawable.twodots);
                    view1.addView(dots);
                    EditText food = new EditText(Food.this);
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
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Food.this);
                                            builder.setTitle("Are you sure you want to delete?");
                                            builder.setCancelable(true);
                                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    linearLayout.removeView(view1);
                                                    removedIds.add(person.getId());
                                                    count = count - 2;
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
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Food.this);
                                            builder.setTitle("Are you sure you want to delete?");
                                            builder.setCancelable(true);
                                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    linearLayout.removeView(view);
                                                    removedIds.add(person.getId());
                                                    count = count - 2;
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
                    retrieveList(snapshot);
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
            Button person = new Button(this);
            person.setBackgroundResource(R.drawable.rectangle_1_shape);
            person.setText("" + name1);
            person.setTextSize(15);
            person.setId(count);
            person.setAllCaps(false);
            count++;
            person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(Food.this);
                    dialog.setContentView(R.layout.dialog_searchable_spinner);
                    dialog.getWindow().setLayout(1000, 1200);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    EditText editText = dialog.findViewById(R.id.edit_text_dialog);
                    ListView listView = dialog.findViewById(R.id.list_view_dialog);
                    Button cancel = dialog.findViewById(R.id.cancel_dialog);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    // Initialize array adapter
                    adapter = new ArrayAdapter<>(Food.this, android.R.layout.simple_list_item_1, people);

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
            LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(300, 90);
            paramstext.setMargins(20, 10, 20, 10);
            person.setLayoutParams(paramstext);
            view1.addView(person);
            ImageView dots = new ImageView(Food.this);
            dots.setImageResource(R.drawable.twodots);
            LinearLayout.LayoutParams paramsdots = new LinearLayout.LayoutParams(50, 100);
            dots.setLayoutParams(paramsdots);
            view1.addView(dots);
            EditText food = new EditText(Food.this);
            food.setText(""+array.get(i).substring(array.get(i).lastIndexOf("+")+1));
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Food.this);
                                    builder.setTitle("Are you sure you want to delete?");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            linearLayout.removeView(view1);
                                            count = count - 2;
                                            removedIds.add(person.getId());
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Food.this);
                                    builder.setTitle("Are you sure you want to delete?");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            linearLayout.removeView(view1);
                                            count = count - 2;
                                            removedIds.add(food.getId());
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
        for (int i = 0; i < snapshot.child("people").getChildrenCount(); i++) {
            people.add((String) snapshot.child("people").child("person " + (i + 1)).getValue());
        }
    }

    private boolean retrieveList(DataSnapshot snapshot){
        if(!snapshot.hasChild("food"))
            return false;
        for(int i=0;i<snapshot.child("food").getChildrenCount();i++){
            mapRetrieveObjects.put(""+(i+1), (String) snapshot.child("food").child(""+(i+1)).getValue());
        }
        return true;
    }
    private void reArrangeIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        Button person = null;
        int countfast = 0;
        for (int i = 0; i < (count + (removedIds.size() * 2)); i++) {
            Button food = findViewById(i);
            if (removedIds.contains(i) || food == null) {
                continue;
            }
            if (i != 0 && i % 2 != 0) {
                ids.add(person.getId());
                ids.add(food.getId());
            }
            person = food;
            countfast++;

        }
        Toast.makeText(this, "" + removedIds, Toast.LENGTH_SHORT).show();
        int countDone = 0;
        for (int j = 0; j < (count / 2); j++) {
            Button person1 = findViewById(ids.get(countDone));
            countDone++;
            Button food1 = findViewById(ids.get(countDone));
            countDone--;
            person1.setId(countDone);
            countDone++;
            food1.setId(countDone);
            countDone++;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == navigate) {
            startActivity(new Intent(Food.this, Navigation.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        if (view == next) {
            if (removedIds.size() > 0)
                reArrangeIds();
            Button test = findViewById(0);
            String name = "";
            boolean flag = true;
            if (test != null) {
                if (!test.getText().toString().equals("Name"))
                    name = test.getText().toString();
            }
            for (int i = 0; i < count && flag == true; i=i+2) {
                if(i+1<count) {
                    Button button = findViewById(i);
                    EditText food = findViewById(i + 1);
                    if (button == null) {
                        continue;
                    }
                    if (button.getText().equals("Name")||button.getText() == null||food.getText()==null) {
                        Toast.makeText(this, "There are empty Spots", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                }
            }
            if (flag) {
                int count1 = 1;
                for (int i = 0; i < count; i=i+2) {
                    EditText food2 = findViewById(i+1);
                    Button person1 = findViewById(i);
                    mapComplete.put(count1+"",person1.getText().toString()+"+"+food2.getText().toString());
                    count1++;
                }
                ref2.child("Users").child(user.getUid()).child(sp.getString("number", "")).child("food").setValue(mapComplete);
                startActivity(new Intent(Food.this, WrapUp.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
            if (view == plus) {
                LinearLayout view1 = new LinearLayout(Food.this);
                view1.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10, 10, 10);
                params.gravity = Gravity.CENTER;
                view1.setLayoutParams(params);
                view1.setGravity(Gravity.CENTER);
                Button person = new Button(Food.this);
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
                        dialog.getWindow().setLayout(1000, 1200);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        EditText editText = dialog.findViewById(R.id.edit_text_dialog);
                        ListView listView = dialog.findViewById(R.id.list_view_dialog);
                        Button cancel = dialog.findViewById(R.id.cancel_dialog);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                        // Initialize array adapter
                        adapter = new ArrayAdapter<>(Food.this, android.R.layout.simple_list_item_1, people);

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
                LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(300, 90);
                paramstext.setMargins(20, 10, 20, 10);
                person.setLayoutParams(paramstext);
                view1.addView(person);
                ImageView dots = new ImageView(Food.this);
                LinearLayout.LayoutParams paramsdots = new LinearLayout.LayoutParams(50, 100);
                dots.setLayoutParams(paramsdots);
                dots.setImageResource(R.drawable.twodots);
                view1.addView(dots);
                EditText food = new EditText(Food.this);
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
                linearLayout.invalidate();
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Food.this);
                                        builder.setTitle("Are you sure you want to delete?");
                                        builder.setCancelable(true);
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                linearLayout.removeView(view1);
                                                removedIds.add(person.getId());
                                                count = count - 2;
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Food.this);
                                        builder.setTitle("Are you sure you want to delete?");
                                        builder.setCancelable(true);
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                linearLayout.removeView(view1);
                                                removedIds.add(person.getId());
                                                count = count - 2;
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

