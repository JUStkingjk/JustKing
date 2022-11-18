package com.jk.justking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jk.justking.Netty.client.NettyClient;
import com.jk.justking.Netty.server.NettyServer;

import java.io.UnsupportedEncodingException;

public class NettyActivity extends AppCompatActivity {

    private static final String TAG = "JustKing";

    //通知
    private NotificationManager Manager;
    //Netty
    private boolean isTestServer = false;
    private boolean isTestClient = true;
    private boolean isConnected = false;
    private NettyClient client;
    private NettyServer server;
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netty);

        //通知控件
        Manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("z","learning"
                    , NotificationManager.IMPORTANCE_HIGH);
            Manager.createNotificationChannel(channel);

        }

    }

    //封装一个通知发送方法
    public void sendNotification(String str){
        //小/=图标
        //小图标颜色
        Notification notification = new NotificationCompat.Builder(this, "z")
                .setContentTitle("通知")
                .setContentText(str)
                .setSmallIcon(R.drawable.lag)//小/=图标
                .setColor(Color.parseColor("#ff2322"))//小图标颜色
                .setAutoCancel(true)
                .build();
        Manager.notify(1, notification);
    }

    @SuppressLint("SetTextI18n")
    public void modeChange(View view){
        Button btn3 = findViewById(R.id.button3);
        Log.w(TAG,"mode change");
        if(!isConnected) {
            if (isTestServer) {
                isTestServer = false;
                isTestClient = true;
                btn3.setText("TCP Client");
            } else if (isTestClient) {
                isTestClient = false;
                isTestServer = true;
                btn3.setText("TCP Server");
            }
        }else{
            btn3.setText("连接中，无法切换");
        }
    }

    public void clear(View view){
        Log.w(TAG,"input clear");
        EditText EdT1 = findViewById(R.id.inputBar1);
        EditText EdT2 = findViewById(R.id.inputBar2);

        EdT1.setText("");
        EdT2.setText("");

        sendNotification("全部木大！");
    }

    @SuppressLint("SetTextI18n")
    public void connect(View view){
        EditText EdT1 = findViewById(R.id.inputBar1);
        EditText EdT2 = findViewById(R.id.inputBar2);
        String str_ip = EdT1.getText().toString();
        String str_port = EdT2.getText().toString();

        Button btn1 = findViewById(R.id.button1);

        if(!str_ip.equals("") || !str_port.equals("")) {
            int port = Integer.parseInt(str_port);
            if (!str_ip.equals("")) {
                if (!isConnected) {
                    if (isTestClient) {
                        testNettyClient(str_ip, port);
                        btn1.setText("disconnect");
                    } else if (isTestServer) {
                        testNettyServer(port);
                        btn1.setText("disconnect");
                    }
                    isConnected = true;
                } else {
                    if (isTestClient) {
                        stopNettyClient();
                        btn1.setText("connect");
                    } else if (isTestServer) {
                        stopNettyServer();
                        btn1.setText("connect");
                    }

                    isConnected = false;
                    sendNotification("断开连接");
                }
            }
        }else{
            sendNotification("请输入数据");
        }

    }

    private void testNettyClient(String ip,int port){
        client = new NettyClient(ip,port);
        client.addHeartBeat(() -> {
            String data = "心跳";
            try{
                client.sentData("测试数据".getBytes("GBK"));
                return data.getBytes("GBK");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            return null;
        });
        client.setHandler(handler);
        client.start();
    }

    private void stopNettyClient(){
        if(client != null){
            client.stop();
        }
    }

    private void testNettyServer(int port){
        server = new NettyServer(port);
        server.addHeartBeat(() -> {
            String data = "心跳";
            try{
                server.sendData("测试数据".getBytes("GBK"));
                return data.getBytes("GBK");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            return null;
        });
        server.setHandler(handler);
        server.start();
    }

    private void stopNettyServer(){
        if(server != null){
            server.stop();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            result += "\r\n";
            result += msg.obj;
            TextView tv1 = findViewById(R.id.message_show);
            tv1.setText(result);
            Log.w(TAG,result);
        }
    };
}