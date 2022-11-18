package com.jk.justking;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class MainActivity2 extends AppCompatActivity {

    private final String TAG = "module001";

    private PopupWindow popupWindow;
    private String string;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }


    public void click_jump(View v){
        Log.w(TAG,"show");
        EditText editText = findViewById(R.id.edt001);
        string = editText.getText().toString();
        Intent intent = new Intent(this,MainActivity3.class);
        intent.putExtra("data_work1",string);
        startActivity(intent);
        Log.w(TAG,string);
    }

    public void backToMainPage(View v){
        Log.w(TAG,"back to main page");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}