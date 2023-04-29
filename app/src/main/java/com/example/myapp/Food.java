package com.example.myapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

public class Food extends AppCompatActivity implements View.OnClickListener {
    ScrollView scrollView;
    Button next;
    ImageView plus;
    LinearLayout linearLayout;
    int count;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        scrollView = findViewById(R.id.scroll_view_food);
        next = findViewById(R.id.next_food);
        plus = findViewById(R.id.plus_food);
        linearLayout = findViewById(R.id.ll_food);
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
        EditText person = new EditText(this);
        person.setBackgroundResource(R.drawable.rectangle_1_shape);
        person.setHint("Name");
        person.setTextSize(15);
        person.setId(count);
        person.setSingleLine();
        person.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        count++;
        LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(300, 100);
        paramstext.setMargins(20,10,20,10);
        person.setLayoutParams(paramstext);
        view.addView(person);
        ImageView dots = new ImageView(this);
        dots.setImageResource(R.drawable.twodots);
        view.addView(dots);
        EditText food = new EditText(this);
        food.setBackgroundResource(R.drawable.rectangle_1_shape);
        food.setHint("Food");
        food.setTextSize(15);
        food.setLayoutParams(paramstext);
        food.setId(count);
        food.setImeOptions(EditorInfo.IME_ACTION_DONE);
        food.setSingleLine();
        count++;
        view.addView(food);
        linearLayout.addView(view);

    }

    @Override
    public void onClick(View view) {
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
            EditText person = new EditText(this);
            person.setBackgroundResource(R.drawable.rectangle_1_shape);
            person.setHint("Name");
            person.setTextSize(15);
            person.setId(count);
            person.setSingleLine();
            person.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            count++;
            LinearLayout.LayoutParams paramstext = new LinearLayout.LayoutParams(300, 100);
            paramstext.setMargins(20,10,20,10);
            person.setLayoutParams(paramstext);
            view1.addView(person);
            ImageView dots = new ImageView(this);
            dots.setImageResource(R.drawable.twodots);
            view1.addView(dots);
            EditText food = new EditText(this);
            food.setBackgroundResource(R.drawable.rectangle_1_shape);
            food.setHint("Food");
            food.setTextSize(15);
            food.setLayoutParams(paramstext);
            food.setId(count);
            food.setImeOptions(EditorInfo.IME_ACTION_DONE);
            food.setSingleLine();
            count++;
            view1.addView(food);
            linearLayout.addView(view);
        }
    }
}
