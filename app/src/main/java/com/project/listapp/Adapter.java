package com.project.listapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHodler> {

    private final Context context;

    public static class ViewHodler extends RecyclerView.ViewHolder{
        TextView t1;
        TextView t2;
        TextView p;
        CheckBox chkbox;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            t1=(TextView) itemView.findViewById(R.id.taskTitleTxt);
            t2=(TextView) itemView.findViewById(R.id.taskLastDateTxt);
            p=(TextView) itemView.findViewById(R.id.priorityTextView);
            chkbox=(CheckBox) itemView.findViewById(R.id.checkBox);

        }

        void setData (String s1,String s2,String s3 , int pir, boolean x){
            p.setText(String.valueOf(pir));
            t1.setText(s1);
            t2.setText(s2);
            chkbox.setChecked(x);
        }
    }
    private List<Task> list;
    public Adapter(Context e, List<Task> list){
        this.context=e;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {

        String title=list.get(position).getTitle();
        String date=list.get(position).getLastUpdatedDate();
        String des=list.get(position).getDescription();
        int pir=list.get(position).getPriority();
        boolean d=list.get(position).isFinished();

        holder.setData(title,date,des,pir,d);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
