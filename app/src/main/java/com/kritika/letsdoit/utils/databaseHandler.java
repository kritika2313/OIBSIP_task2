package com.kritika.letsdoit.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kritika.letsdoit.Model.todotask;

import java.util.ArrayList;
import java.util.List;

public class databaseHandler extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String name="letsdoitdatabase";
    private static final String doit_table="doit";
    private static final String ID="id";
    private static final String Task="task";
    private static final String Status="status";
    private static final String Create_doit_table="CREATE TABLE " + doit_table + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Task + " TEXT, "
            + Status + " INTEGER)";

    private SQLiteDatabase db;



    public databaseHandler(Context context) {
        super(context, name, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_doit_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //drop the table if it exists
        db.execSQL("DROP TABLE IF EXISTS " +doit_table);
        //create new table
        onCreate(db);

    }
    public void openDatabase(){
        db=this.getWritableDatabase();
    }
    public void insert(todotask task){
        ContentValues cv=new ContentValues();
        cv.put(Task,task.getTask());
        cv.put(Status,0);
        db.insert(doit_table,null,cv);
    }
    public List<todotask> getAllTasks(){
        List<todotask>tasklist=new ArrayList<>();
        Cursor cur=null;
        db.beginTransaction();
        try{
            cur=db.query(doit_table,null,null,null,null,null,null);
            if(cur!=null){
                if(cur.moveToFirst()) {
                    do {
                        todotask task = new todotask();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(Task)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(Status)));
                        tasklist.add(task);
                    }while(cur.moveToNext());
                }
            }

        }finally{
            db.endTransaction();
            cur.close();
        }
        return tasklist;
    }

    public void updateStatus(int id,int status){
        ContentValues cv=new ContentValues();
        cv.put(Status,status);
        db.update(doit_table,cv,ID + "=?",new String[]{String.valueOf(id)});

    }
    public void updateTask(int id,String task){
        ContentValues cv=new ContentValues();
        cv.put(Task,task);
        db.update(doit_table,cv,ID + "+?",new String[]{String.valueOf(id)});

    }
    public void delete(int id){
        db.delete(doit_table,ID +"=?",new String[]{String.valueOf(id)});

    }

    public void closeDatabse(){
        db.close();
    }

}
