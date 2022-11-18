package com.jk.justking.Sql;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBManage {
    private final String TAG = "DBManage";
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBManage(Context context){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void add(Data user){
        db.beginTransaction();
        try{
            db.execSQL("insert into user values(?,?,?)",new Object[]{user.getU_id(),user.getU_name(),user.getAge()});
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void update(Data user){
        List<Data> arrayList = queryAll();
        for (Data data : arrayList) {
            if(data.u_id == user.u_id){
                Log.w(TAG,data.toString());
                ContentValues cv = new ContentValues();
                cv.put("u_name",user.u_name);
                cv.put("age",user.age);
                db.update("user",cv,"u_id=?",new String[]{String.valueOf(user.u_id)});
            }
        }
    }

    public void delete(Data user){
        if(!isDataExist(user)) {
            Log.w(TAG,"data not found");
        }else{
            db.delete("user", "u_name=? and u_id=?",new String[]{user.u_name,String.valueOf(user.u_id)});
        }
    }

    public boolean isDataExist(Data user){
        List<Data> arrayList = queryAll();
        for (Data data : arrayList) {
            if(data.u_id == user.u_id && data.u_name.equals(user.u_name) && data.age == user.age){
                Log.w(TAG,data.toString());
                return true;
            }
        }
        Log.w(TAG,"data not exist");
        return false;
    }

    @SuppressLint("Range")
    public List<Data> queryAll(){
        ArrayList<Data> users = new ArrayList<Data>();
        Cursor cursor = db.rawQuery("select * from user",null);
       // cursor.moveToFirst();
        while (cursor.moveToNext()){
            Data user = new Data();
            user.setU_id( cursor.getInt(cursor.getColumnIndex("u_id")));
            user.setU_name( cursor.getString(cursor.getColumnIndex("u_name")));
            user.setAge( cursor.getInt(cursor.getColumnIndex("age")));
            users.add(user);
        }
        cursor.close();
        return users;
    }

}
