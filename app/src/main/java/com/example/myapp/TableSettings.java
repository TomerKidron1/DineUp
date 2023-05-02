package com.example.myapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class TableSettings extends AppCompatActivity implements View.OnClickListener {
    FrameLayout layout;
    Button bigCircle,smallCircle;
    Button rectangle,verticalRectangle;
    Button roundedRectangle,verticalRoundedRectangle,square;
    Button custom;
    ImageView question;
    ImageView popup;
    Button reset;
    Button procceed;
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference ref2;
    FirebaseAuth auth;
    int count;
    SharedPreferences sp;
    Button button2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablesettings);
        count=0;
        sp = getSharedPreferences("currentProject",MODE_PRIVATE);
        layout = findViewById(R.id.layout);
        reset= findViewById(R.id.reset_button);
        reset.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        question = findViewById(R.id.question);
        question.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
        ref2 = ref.child(auth.getCurrentUser().getUid());
        procceed = findViewById(R.id.procceed);
        procceed.setOnClickListener(this);
        bigCircle = findViewById(R.id.big_circle_table);
        smallCircle = findViewById(R.id.small_circle_table);
        rectangle = findViewById(R.id.rectangle_table);
        verticalRectangle = findViewById(R.id.vertical_rectangle_table);
        verticalRoundedRectangle = findViewById(R.id.vertical_rounded_table);
        roundedRectangle = findViewById(R.id.rounded_table);
        square = findViewById(R.id.square_table);
        popup = findViewById(R.id.menuimage2);
        popup.setOnClickListener(this);
        custom = findViewById(R.id.custom);
        bigCircle.setOnClickListener(this);
        smallCircle.setOnClickListener(this);
        rectangle.setOnClickListener(this);
        verticalRectangle.setOnClickListener(this);
        roundedRectangle.setOnClickListener(this);
        verticalRoundedRectangle.setOnClickListener(this);
        square.setOnClickListener(this);
        custom.setOnClickListener(this);
        ref2.child(sp.getString("number","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>4)
                    retrieveObjects(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void retrieveObjects(DataSnapshot snapshot) {
        for(int i=0;i<(snapshot.child("parameters").getChildrenCount())/3;i++)
        {
            //Toast.makeText(this, ""+snapshot.child("parameters").child("top-imageview "+i).getValue(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, ""+snapshot.child("parameters").child("left-imageview "+i).getValue(), Toast.LENGTH_SHORT).show();
            addView(Math.toIntExact((Long)snapshot.child("parameters").child("object "+i).getValue()),Math.toIntExact((Long)snapshot.child("parameters").child("top-imageview "+i).getValue()),Math.toIntExact((Long)snapshot.child("parameters").child("left-imageview "+i).getValue()));
            count++;
        }
    }
    private int xDelta,yDelta;

    public void addView(int object,int top,int left ) {
        ImageView imageview = new ImageView(TableSettings.this);
        FrameLayout frameLayout = findViewById(R.id.layout);
        imageview.setImageResource(object);
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, 0, 0);
        imageview.setLayoutParams(params);
        imageview.setId(count);
        imageview.setTag(object);
        imageview.setScaleType(ImageView.ScaleType.FIT_XY);


        imageview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {
                        FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if (x - xDelta + view.getWidth() <= frameLayout.getWidth()
                                && y - yDelta + view.getHeight() <= frameLayout.getHeight()
                                && x - xDelta >= 0
                                && y - yDelta >= 0) {
                            FrameLayout.LayoutParams layoutParams =
                                    (FrameLayout.LayoutParams) view.getLayoutParams();
                            layoutParams.leftMargin = x - xDelta;
                            layoutParams.topMargin = y - yDelta;
                            layoutParams.rightMargin = 0;
                            layoutParams.bottomMargin = 0;
                            view.setLayoutParams(layoutParams);
                        }
                        break;
                    }
                }
                frameLayout.invalidate();
                return true;
            }
        });
        frameLayout.addView(imageview);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClick(View view) {
        if(view == procceed){
            String number = sp.getString("number","");
            ref2.child(number).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Map<String,Object> map = new HashMap<>();
                    if(count!=0){
                        for(int i=0;i<count;i++) {
                            int[] location = new int[2];
                            ImageView imageView = findViewById(i);
                            imageView.getLocationOnScreen(location);
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                            map.put("top-imageview " + i, params.topMargin);
                            map.put("left-imageview " + i, params.leftMargin);
                            map.put("object "+ i,imageView.getTag());
                            ref2.child(number).child("parameters").setValue(map);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            startActivity(new Intent(TableSettings.this,Questions.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        }
        if(view == reset){
            layout.removeAllViews();
            count=0;
        }
        if(view == question){
            startActivity(new Intent(TableSettings.this,TableInfo.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        if (view == bigCircle) {
            ImageView imageview = new ImageView(TableSettings.this);
            FrameLayout linearlayout = findViewById(R.id.layout);
            imageview.setImageResource(R.drawable.big_circle_white);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageview.setLayoutParams(params);
            imageview.setId(count);
            imageview.setTag(R.drawable.big_circle_white);
            count++;

            imageview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN: {
                            FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            if (x - xDelta + view.getWidth() <= linearlayout.getWidth()
                                    && y - yDelta + view.getHeight() <= linearlayout.getHeight()
                                    && x - xDelta >= 0
                                    && y - yDelta >= 0) {
                                FrameLayout.LayoutParams layoutParams =
                                        (FrameLayout.LayoutParams) view.getLayoutParams();
                                layoutParams.leftMargin = x - xDelta;
                                layoutParams.topMargin = y - yDelta;
                                layoutParams.rightMargin = 0;
                                layoutParams.bottomMargin = 0;
                                view.setLayoutParams(layoutParams);
                            }
                            break;
                        }
                    }
                    linearlayout.invalidate();
                    return true;
                }
            });
            linearlayout.addView(imageview);
        }
        if(view == smallCircle){
            ImageView imageview = new ImageView(TableSettings.this);
            FrameLayout linearlayout = findViewById(R.id.layout);
            imageview.setImageResource(R.drawable.small_circle_white);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageview.setLayoutParams(params);
            imageview.setTag(R.drawable.small_circle_white);
            imageview.setId(count);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            count++;
            imageview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN: {
                            FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            if (x - xDelta + view.getWidth() <= linearlayout.getWidth()
                                    && y - yDelta + view.getHeight() <= linearlayout.getHeight()
                                    && x - xDelta >= 0
                                    && y - yDelta >= 0) {
                                FrameLayout.LayoutParams layoutParams =
                                        (FrameLayout.LayoutParams) view.getLayoutParams();
                                layoutParams.leftMargin = x - xDelta;
                                layoutParams.topMargin = y - yDelta;
                                layoutParams.rightMargin = 0;
                                layoutParams.bottomMargin = 0;
                                view.setLayoutParams(layoutParams);
                            }
                            break;
                        }
                    }
                    linearlayout.invalidate();
                    return true;
                }
            });
            linearlayout.addView(imageview);
        }
        if(view == rectangle){
            ImageView imageview = new ImageView(TableSettings.this);
            FrameLayout linearlayout = findViewById(R.id.layout);
            imageview.setImageResource(R.drawable.rectangle_white);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageview.setLayoutParams(params);
            imageview.setTag(R.drawable.rectangle_white);
            imageview.setId(count);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            count++;
            imageview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN: {
                            FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            if (x - xDelta + view.getWidth() <= linearlayout.getWidth()
                                    && y - yDelta + view.getHeight() <= linearlayout.getHeight()
                                    && x - xDelta >= 0
                                    && y - yDelta >= 0) {
                                FrameLayout.LayoutParams layoutParams =
                                        (FrameLayout.LayoutParams) view.getLayoutParams();
                                layoutParams.leftMargin = x - xDelta;
                                layoutParams.topMargin = y - yDelta;
                                layoutParams.rightMargin = 0;
                                layoutParams.bottomMargin = 0;
                                view.setLayoutParams(layoutParams);
                            }
                            break;
                        }
                    }
                    linearlayout.invalidate();
                    return true;
                }
            });
            linearlayout.addView(imageview);
        }
        if(view == verticalRectangle){
            ImageView imageview = new ImageView(TableSettings.this);
            FrameLayout linearlayout = findViewById(R.id.layout);
            imageview.setImageResource(R.drawable.vertical_rectangle_table_white);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageview.setLayoutParams(params);
            imageview.setTag(R.drawable.vertical_rectangle_table_white);
            imageview.setId(count);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            count++;
            imageview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN: {
                            FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            if (x - xDelta + view.getWidth() <= linearlayout.getWidth()
                                    && y - yDelta + view.getHeight() <= linearlayout.getHeight()
                                    && x - xDelta >= 0
                                    && y - yDelta >= 0) {
                                FrameLayout.LayoutParams layoutParams =
                                        (FrameLayout.LayoutParams) view.getLayoutParams();
                                layoutParams.leftMargin = x - xDelta;
                                layoutParams.topMargin = y - yDelta;
                                layoutParams.rightMargin = 0;
                                layoutParams.bottomMargin = 0;
                                view.setLayoutParams(layoutParams);
                            }
                            break;
                        }
                    }
                    linearlayout.invalidate();
                    return true;
                }
            });
            linearlayout.addView(imageview);
        }
        if(view == roundedRectangle){
            ImageView imageview = new ImageView(TableSettings.this);
            FrameLayout linearlayout = findViewById(R.id.layout);
            imageview.setImageResource(R.drawable.rounded_table_white);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageview.setLayoutParams(params);
            imageview.setTag(R.drawable.rounded_table_white);
            imageview.setId(count);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            count++;
            imageview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN: {
                            FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            if (x - xDelta + view.getWidth() <= linearlayout.getWidth()
                                    && y - yDelta + view.getHeight() <= linearlayout.getHeight()
                                    && x - xDelta >= 0
                                    && y - yDelta >= 0) {
                                FrameLayout.LayoutParams layoutParams =
                                        (FrameLayout.LayoutParams) view.getLayoutParams();
                                layoutParams.leftMargin = x - xDelta;
                                layoutParams.topMargin = y - yDelta;
                                layoutParams.rightMargin = 0;
                                layoutParams.bottomMargin = 0;
                                view.setLayoutParams(layoutParams);
                            }
                            break;
                        }
                    }
                    linearlayout.invalidate();
                    return true;
                }
            });
            linearlayout.addView(imageview);
        }
        if(view == verticalRoundedRectangle){
            ImageView imageview = new ImageView(TableSettings.this);
            FrameLayout linearlayout = findViewById(R.id.layout);
            imageview.setImageResource(R.drawable.vertical_rounded_table_white);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageview.setLayoutParams(params);
            imageview.setTag(R.drawable.vertical_rounded_table_white);
            imageview.setId(count);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            count++;
            imageview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN: {
                            FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            if (x - xDelta + view.getWidth() <= linearlayout.getWidth()
                                    && y - yDelta + view.getHeight() <= linearlayout.getHeight()
                                    && x - xDelta >= 0
                                    && y - yDelta >= 0) {
                                FrameLayout.LayoutParams layoutParams =
                                        (FrameLayout.LayoutParams) view.getLayoutParams();
                                layoutParams.leftMargin = x - xDelta;
                                layoutParams.topMargin = y - yDelta;
                                layoutParams.rightMargin = 0;
                                layoutParams.bottomMargin = 0;
                                view.setLayoutParams(layoutParams);
                            }
                            break;
                        }
                    }
                    linearlayout.invalidate();
                    return true;
                }
            });
            linearlayout.addView(imageview);
        }
        if(view == square){
            ImageView imageview = new ImageView(TableSettings.this);
            FrameLayout linearlayout = findViewById(R.id.layout);
            imageview.setImageResource(R.drawable.square_white);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageview.setLayoutParams(params);
            imageview.setTag(R.drawable.square_white);
            imageview.setId(count);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            count++;
            imageview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN: {
                            FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            if (x - xDelta + view.getWidth() <= linearlayout.getWidth()
                                    && y - yDelta + view.getHeight() <= linearlayout.getHeight()
                                    && x - xDelta >= 0
                                    && y - yDelta >= 0) {
                                FrameLayout.LayoutParams layoutParams =
                                        (FrameLayout.LayoutParams) view.getLayoutParams();
                                layoutParams.leftMargin = x - xDelta;
                                layoutParams.topMargin = y - yDelta;
                                layoutParams.rightMargin = 0;
                                layoutParams.bottomMargin = 0;
                                view.setLayoutParams(layoutParams);
                            }
                            break;
                        }
                    }
                    linearlayout.invalidate();

                    return true;
                }
            });
            linearlayout.addView(imageview);
        }
        if(view == custom){
            Map<String,Double> map = showPopUpCustom();
            double height = 0,width=0;
            for(Map.Entry<String,Double> me: map.entrySet())
            {
               if(me.getKey().equals("height")){
                   height=me.getValue();
               }
               else
                   width=me.getValue();
            }

            Toast.makeText(TableSettings.this,height+" "+width,Toast.LENGTH_LONG).show();
            if(height!=0&&width!=0){
                ImageView imageview = new ImageView(TableSettings.this);
                FrameLayout linearlayout = findViewById(R.id.layout);
                LinearLayout.LayoutParams params = new LinearLayout
                        .LayoutParams((int)width, (int)height);
                imageview.setImageResource(R.drawable.custom);
                imageview.setLayoutParams(params);
                imageview.setTag(R.drawable.custom);
                imageview.setId(count);
                imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                count++;
                imageview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        final int x = (int) event.getRawX();
                        final int y = (int) event.getRawY();
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN: {
                                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                                xDelta = x - lParams.leftMargin;
                                yDelta = y - lParams.topMargin;
                                break;
                            }
                            case MotionEvent.ACTION_UP: {
                                break;
                            }
                            case MotionEvent.ACTION_MOVE: {
                                if (x - xDelta + view.getWidth() <= linearlayout.getWidth()
                                        && y - yDelta + view.getHeight() <= linearlayout.getHeight()
                                        && x - xDelta >= 0
                                        && y - yDelta >= 0) {
                                    FrameLayout.LayoutParams layoutParams =
                                            (FrameLayout.LayoutParams) view.getLayoutParams();
                                    layoutParams.leftMargin = x - xDelta;
                                    layoutParams.topMargin = y - yDelta;
                                    layoutParams.rightMargin = 0;
                                    layoutParams.bottomMargin = 0;
                                    view.setLayoutParams(layoutParams);
                                }
                                break;
                            }
                        }
                        linearlayout.invalidate();
                        return true;
                    }
                });
                linearlayout.addView(imageview);
            }
        }
        if(view == popup){
            showPopUpMenu(view);
        }
    }
    public Map<String,Double> showPopUpCustom(){
        Map<String,Double> values = new HashMap<>();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(TableSettings.this);
        builder1.setTitle("Enter Custom Height");
        builder1.setMessage("Enter parameters in cm");
        builder1.setCancelable(false);
        LinearLayout layout = new LinearLayout(TableSettings.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText inputHeight = new EditText(TableSettings.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inputHeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputHeight.setHint("Height");
        inputHeight.setLayoutParams(lp);
        layout.addView(inputHeight);
        final EditText inputWidth = new EditText(TableSettings.this);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inputWidth.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputWidth.setHint("Width");
        inputWidth.setLayoutParams(lp2);
        layout.addView(inputWidth);
        builder1.setView(layout);
        builder1.setPositiveButton("Enter", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!inputHeight.getText().toString().equals("")&&!inputWidth.getText().toString().equals("")) {
                    //values.put("height",Integer.valueOf(inputHeight.getText().toString()));
                    //values.put("width",Integer.valueOf(inputWidth.getText().toString()));
                    ImageView imageview = new ImageView(TableSettings.this);
                    FrameLayout linearlayout = findViewById(R.id.layout);
                    LinearLayout.LayoutParams params = new LinearLayout
                            .LayoutParams(Integer.valueOf(inputWidth.getText().toString()), Integer.valueOf(inputHeight.getText().toString()));
                    imageview.setImageResource(R.drawable.custom);
                    imageview.setLayoutParams(params);
                    imageview.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            final int x = (int) event.getRawX();
                            final int y = (int) event.getRawY();
                            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                case MotionEvent.ACTION_DOWN: {
                                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                                    xDelta = x - lParams.leftMargin;
                                    yDelta = y - lParams.topMargin;
                                    break;
                                }
                                case MotionEvent.ACTION_UP: {
                                    break;
                                }
                                case MotionEvent.ACTION_MOVE: {
                                    if (x - xDelta + view.getWidth() <= linearlayout.getWidth()
                                            && y - yDelta + view.getHeight() <= linearlayout.getHeight()
                                            && x - xDelta >= 0
                                            && y - yDelta >= 0) {
                                        FrameLayout.LayoutParams layoutParams =
                                                (FrameLayout.LayoutParams) view.getLayoutParams();
                                        layoutParams.leftMargin = x - xDelta;
                                        layoutParams.topMargin = y - yDelta;
                                        layoutParams.rightMargin = 0;
                                        layoutParams.bottomMargin = 0;
                                        view.setLayoutParams(layoutParams);
                                    }
                                    break;
                                }
                            }
                            linearlayout.invalidate();
                            return true;
                        }
                    });
                    linearlayout.addView(imageview);

                }
                else{
                    Toast.makeText(TableSettings.this,"Enter Height or Width", Toast.LENGTH_LONG).show();
                }


            }
        }));
        builder1.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        builder1.show();

        return values;
    }
    public void showPopUpMenu(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.project_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(TableSettings.this);
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
                                Intent i = new Intent(TableSettings.this, MainActivity.class);
                                startActivity(i);
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

}