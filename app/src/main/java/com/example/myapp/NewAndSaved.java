package com.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NewAndSaved extends AppCompatActivity implements View.OnClickListener {
    Button New;
    Button Save;
    ImageView menuclick;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.newsaved);
        New = findViewById(R.id.newproj);
        Save = findViewById(R.id.savedproj);
        menuclick = findViewById(R.id.menuimage);
        menuclick.setOnClickListener(this);
        New.setOnClickListener(this);
        Save.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == New){
            startActivity(new Intent(NewAndSaved.this, NewProject.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if(view == menuclick){
            showPopUp(view);
        }
        if(view == Save){
            startActivity(new Intent(NewAndSaved.this,SavedProjects.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }

    }
    public void showPopUp(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.project_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        AlertDialog.Builder builder= new AlertDialog.Builder(NewAndSaved.this);
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
                                startActivity(new Intent(NewAndSaved.this, MainActivity.class));
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
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
