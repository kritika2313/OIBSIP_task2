package com.kritika.letsdoit.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kritika.letsdoit.AddNewTask;
import com.kritika.letsdoit.MainActivity;
import com.kritika.letsdoit.Model.todotask;
import com.kritika.letsdoit.R;
import com.kritika.letsdoit.utils.databaseHandler;

import java.util.List;

public class todoadapter extends RecyclerView.Adapter<todoadapter.ViewHolder> {
    private List<todotask> todolist;
    private MainActivity activity;
    private databaseHandler  db;
    public todoadapter(databaseHandler db,MainActivity activity){
        this.db=db;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(activity).inflate(R.layout.task_layout,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            db.openDatabase();
            todotask item= todolist.get(position);
            holder.task.setText(item.getTask());
            holder.task.setChecked(toBoolean(item.getStatus()));
            holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        db.updateStatus(item.getId(),1);
                    }else{
                        db.updateStatus(item.getId(),0);
                    }
                }
            });
    }
    //helper class to check the status as in model we have
    // integer defined id to access data from database
    // and it wants boolean.
    public boolean toBoolean(int n){

        return n!=0;
    }

    @Override
    public int getItemCount() {
        return todolist.size();
    }
    public void setTask(List<todotask>todolist){
        this.todolist=todolist;
        notifyDataSetChanged();
    }
    public Context getContext(){
        return activity;
    }

    public void deleteitem(int position){
        todotask item=todolist.get(position);
        db.delete(item.getId());
        todolist.remove(position);
        notifyItemRemoved(position);
    }
     public void editItem(int position){
        todotask item=todolist.get(position);
        Bundle bundle=new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
         AddNewTask fragment=new AddNewTask();
         fragment.setArguments(bundle);
         fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
     }

    public static class ViewHolder extends RecyclerView.ViewHolder{
         CheckBox task;
        public ViewHolder(View itemView){
        super(itemView);
        task= itemView.findViewById(R.id.todocheckbox);

        }
    }



}
