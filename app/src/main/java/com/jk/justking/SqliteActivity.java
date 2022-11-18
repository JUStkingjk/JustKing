package com.jk.justking;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jk.justking.Sql.DBManage;
import com.jk.justking.Sql.Data;

import java.util.ArrayList;
import java.util.List;


public class SqliteActivity extends AppCompatActivity {

    private static final String TAG = "1010001";

    //数据库操作模式
    private String mode;
    //表相关信息
    private final String name_table = "user";
    private int id;
    private String name_column;
    private String value;
    private int age;

    private String table_Data = "";
    private DBManage manage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        manage = new DBManage(this);
        Spinner spinner = findViewById(R.id.spinner);
        List<String> data_list = new ArrayList<>();
        data_list.add("null");
        data_list.add("add");
        data_list.add("delete");
        data_list.add("update");
        data_list.add("check");
        ArrayAdapter<String> arr_adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, data_list);
        arr_adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arr_adapter);

        //操作行为选定
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EditText editText1 = findViewById(R.id.input1);
                EditText editText2 = findViewById(R.id.input2);
                EditText editText3 = findViewById(R.id.input3);
                switch (position){
                    case 0:
                        mode = "null";
                        editText1.setHint("id");
                        editText2.setHint("name");
                        editText3.setHint("age");
                        break;
                    case 1:
                        mode = "insert";

                        break;
                    case 2:
                        mode = "delete";

                        break;
                    case 3:
                        mode = "update";

                        break;
                    case 4:
                        mode = "search";

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sendHint("请选择数据库操作");
            }
        });
    }

    //执行操作
    public void click_submit(View view){
        EditText editText1 = findViewById(R.id.input1);
        EditText editText2 = findViewById(R.id.input2);
        EditText editText3 = findViewById(R.id.input3);
        switch (mode) {
            case "insert":
                id = Integer.parseInt(editText1.getEditableText().toString());
                name_column = editText2.getText().toString();
                age = Integer.parseInt(editText3.getEditableText().toString());
                insert();
                break;
            case "delete":
                id = Integer.parseInt(editText1.getEditableText().toString());
                name_column = editText2.getText().toString();
                age = Integer.parseInt(editText3.getEditableText().toString());
                delete();
                break;
            case "update":
                sendHint("update");
                id = Integer.parseInt(editText1.getEditableText().toString());
                name_column = editText2.getText().toString();
                age = Integer.parseInt(editText3.getEditableText().toString());
                update();
                break;
            case "search":
                sendHint("search");
                search();
                break;
            default:
                sendHint("no");
                break;
        }

    }

    public void insert(){
        sendHint( "insert" + id + name_column + age);
        manage = new DBManage(this);
        Data user = new Data(id, name_column, age);
        manage.add(user);
        user = null;
    }

    public void delete(){
        sendHint(  "delete" + id + name_column + age);
        Data user = new Data(id, name_column, age);
        manage = new DBManage(this);
        manage.delete(user);
        user = null;
    }

    public void update(){
        Data user = new Data(id, name_column, age);
        manage.update(user);
    }

    public void search(){
        //读取数据库
        List<Data> arrayList = manage.queryAll();
        for (Data data : arrayList) {
            table_Data += data.toString();
        }
        Log.w(TAG, table_Data);
        //打印读取数据
        TextView textView = findViewById(R.id.data_show);
        textView.setText(table_Data);
        //清空
        table_Data = "";
    }

    public void sendHint(String string){
        Toast toast = Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT);
        toast.show();
    }


}