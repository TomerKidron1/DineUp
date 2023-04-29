package com.example.myapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Conflicts extends AppCompatActivity implements View.OnClickListener {
    ScrollView scrollView;
    LinearLayout linearLayout;
    Button next;
    ImageView plus;
    int count;
    FirebaseDatabase database;
    DatabaseReference ref;
    SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conflicts);
        scrollView= findViewById(R.id.scroll_view_conflicts);
        linearLayout= findViewById(R.id.ll_conflicts);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        next=findViewById(R.id.next_conflicts);
        plus=findViewById(R.id.plus_conflicts);
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        next.setOnClickListener(this);
        plus.setOnClickListener(this);
        count=0;
        LinearLayout view=new LinearLayout(this);
        view.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        view.setGravity(Gravity.CENTER);
        EditText person1 = new EditText(this);
        person1.setBackgroundResource(R.drawable.rectangle_1_shape);
        person1.setHint("Name");
        person1.setTextSize(15);
        person1.setId(count);
        person1.setSingleLine();
        person1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        count++;
        LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(300, 100);
        paramstext.setMargins(20,10,20,10);
        person1.setLayoutParams(paramstext);
        view.addView(person1);
        ImageView arrow = new ImageView(this);
        arrow.setImageResource(R.drawable.arrow);
        view.addView(arrow);
        EditText person2 = new EditText(this);
        person2.setBackgroundResource(R.drawable.rectangle_1_shape);
        person2.setHint("Name");
        person2.setTextSize(15);
        person2.setLayoutParams(paramstext);
        person2.setId(count);
        person2.setImeOptions(EditorInfo.IME_ACTION_DONE);
        person2.setSingleLine();
        count++;
        view.addView(person2);
        linearLayout.addView(view);
    }

    @Override
    public void onClick(View view) {
        if(view==next){
            for(int i=0; i<count; i++){
                EditText editText = findViewById(i);
                if(editText.getText()==null||editText.getText().length()==0){
                    Toast.makeText(this, "There are empty names", Toast.LENGTH_SHORT).show();
                }
                else{

                }
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
            EditText person1 = new EditText(this);
            person1.setBackgroundResource(R.drawable.rectangle_1_shape);
            person1.setHint("Name");
            person1.setTextSize(15);
            person1.setId(count);
            person1.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            count++;
            person1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            person1.setSingleLine();
            LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(300, 100);
            paramstext.setMargins(20,10,20,10);
            person1.setLayoutParams(paramstext);
            person1.setNextFocusForwardId(count);
            view1.addView(person1);
            ImageView arrow = new ImageView(this);
            arrow.setImageResource(R.drawable.arrow);
            view1.addView(arrow);
            EditText person2 = new EditText(this);
            person2.setBackgroundResource(R.drawable.rectangle_1_shape);
            person2.setHint("Name");
            person2.setTextSize(15);
            person2.setLayoutParams(paramstext);
            person2.setId(count);
            count++;
            person2.setSingleLine();
            person2.setImeOptions(EditorInfo.IME_ACTION_DONE);
            view1.addView(person2);
            linearLayout.addView(view1);
        }
    }
}
