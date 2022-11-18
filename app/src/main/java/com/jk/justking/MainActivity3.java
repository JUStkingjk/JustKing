package com.jk.justking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_layout);

        Intent i = this.getIntent();
        TextView tv = findViewById(R.id.TextView1);
        tv.setText(i.getStringExtra("data_work1"));
    }

    public void click_cencel(View v){
        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);
    }
}