package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> arrayList;


    public ListViewAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context,resource,objects);
        this.context = context;
        this.arrayList = objects;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        String tmp = arrayList.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.listview_items,null);
        TextView name = view.findViewById(R.id.peoplename);
        name.setText(tmp);
        return view;
    }
}
