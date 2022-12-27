package com.project.listapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHodler> {

    private final Context context;
    private SelectListener listener;

    public static class ViewHodler extends RecyclerView.ViewHolder{
        TextView t1;
        TextView t2;
        TextView p;
        CheckBox chkbox;
        public RelativeLayout mLayout;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            t1=(TextView) itemView.findViewById(R.id.taskTitleTxt);
            t2=(TextView) itemView.findViewById(R.id.taskLastDateTxt);
            p=(TextView) itemView.findViewById(R.id.priorityTextView);
            chkbox=(CheckBox) itemView.findViewById(R.id.checkBox);
            mLayout=(RelativeLayout) itemView.findViewById(R.id.TaskContainer);

        }
        void setData (String s1,String s2,String s3 , int pir, boolean x){
            p.setText(String.valueOf(pir));
            t1.setText(s1);
            t2.setText(s2);
            chkbox.setChecked(x);
        }

    }
    private List<Task> list;
    public Adapter(Context e, List<Task> list,SelectListener listener){
        this.context=e;
        this.list=list;
        this.listener=listener;
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
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(list.get(holder.getAdapterPosition()));

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
