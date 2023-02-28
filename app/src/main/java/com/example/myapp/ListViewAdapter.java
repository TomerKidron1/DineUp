package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<SavedProject> {
    Context context;
    ArrayList<SavedProject> arrayList;


    public ListViewAdapter(Context context, int resource, ArrayList<SavedProject> objects) {
        super(context,resource,objects);
        this.context = context;
        this.arrayList = objects;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        SavedProject tmp = arrayList.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.listview_items,null);
        TextView name = view.findViewById(R.id.savedname);
        TextView category = view.findViewById(R.id.category_lv);
        TextView date = view.findViewById(R.id.date_lv);
        name.setText(tmp.getProject_name());
        category.setText(tmp.getCategory());
        date.setText(tmp.getDate());
        return view;
    }
}
