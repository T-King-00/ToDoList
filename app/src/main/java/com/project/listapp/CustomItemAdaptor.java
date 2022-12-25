package com.project.listapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomItemAdaptor extends ArrayAdapter<Task> {


    public static final  String TAG="CustomItemAdaptor";
    ArrayList<Task> tasks;


    public CustomItemAdaptor(@NonNull Context context, ArrayList<Task> objects) {
        super(context, R.layout.item ,objects);
        this.tasks=(ArrayList<Task>) objects.clone();
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Task T=getItem(position);
        if(view==null)
        {
            view=LayoutInflater.from(getContext()).inflate(R.layout.item,viewGroup,false);
        }

        TextView t1=(TextView) view.findViewById(R.id.taskTitleTxt);
        TextView t2=(TextView) view.findViewById(R.id.taskLastDateTxt);
        TextView p=(TextView) view.findViewById(R.id.priorityTextView);
        CheckBox chkbox=(CheckBox) view.findViewById(R.id.checkBox);
        p.setText(String.valueOf(T.getPriority()));
        t1.setText(T.getTitle());
        t2.setText(T.getLastUpdatedDate());
        chkbox.setChecked(T.isFinished());

        return view;
    }
    public void clear()
    {
        tasks.clear();
        notifyDataSetChanged();
    }

}
