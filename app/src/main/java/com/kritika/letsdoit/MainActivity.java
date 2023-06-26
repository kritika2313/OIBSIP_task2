package com.kritika.letsdoit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kritika.letsdoit.Model.todotask;
import com.kritika.letsdoit.adapter.todoadapter;
import com.kritika.letsdoit.utils.databaseHandler;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    TextView tv;
    private RecyclerView tasksRecyclerView;
    private todoadapter tasksadapter;
    private FloatingActionButton fab;
    private List<todotask> tasklist;
    public databaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasksRecyclerView=findViewById(R.id.recyclerviewtask);
        tv=findViewById(R.id.tasks);
        fab=findViewById(R.id.floatingbutton);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerItemTouchHelper(tasksadapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        //database is connected and opened
        db=new databaseHandler(this);
        db.openDatabase();


        tasklist=new ArrayList<>();
        tasksRecyclerView.setLayoutManager( new LinearLayoutManager(this));
        tasksadapter=new todoadapter(db,this);
        tasksRecyclerView.setAdapter(tasksadapter);
       //this will collect all the tasks
        tasklist=db.getAllTasks();
        Collections.reverse(tasklist);
        tasksadapter.setTask(tasklist);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        tasklist=db.getAllTasks();
        Collections.reverse(tasklist);
        tasksadapter.setTask(tasklist);
        tasksadapter.notifyDataSetChanged();

    }
}