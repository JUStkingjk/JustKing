package com.jk.justking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "page_main";

    public static int moduleNumber = 0;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btn1 = findViewById(R.id.imageBtn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleNumber = 1;
                jump(moduleNumber);
            }
        });

        ImageButton btn2 = findViewById(R.id.imageBtn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleNumber = 2;
                jump(moduleNumber);
            }
        });

        ImageButton btn3 = findViewById(R.id.imageBtn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleNumber = 3;
                jump(moduleNumber);
            }
        });
    }

    public void jump(int moduleNumber){
        Log.w(TAG,"page_jump");
        switch (moduleNumber){
            case 1: intent = new Intent(this,MainActivity2.class);
                    startActivity(intent);
                    break;
            case 2: intent = new Intent(this,SqliteActivity.class);
                    startActivity(intent);
                    break;
            case 3: intent = new Intent(this,NettyActivity.class);
                    startActivity(intent);
                    break;
        }
    }
}