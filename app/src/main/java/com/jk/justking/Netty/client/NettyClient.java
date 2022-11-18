package com.jk.justking.Netty.client;

import android.os.Handler;
import android.util.Log;

import com.jk.justking.Netty.DataHandlerAdapter;
import com.jk.justking.Netty.HeartBeatListener;
import com.jk.justking.Netty.MessageHandler;
import com.jk.justking.Netty.MessageType;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyClient {
    private static final String TAG = "NettyClient";

    private Channel channel;

    private String address;

    private int port;

    private DataHandlerAdapter dataHandlerAdapter;

    public NettyClient(String address,int port){
        this.address = address;
        this.port = port;
        dataHandlerAdapter = new DataHandlerAdapter(DataHandlerAdapter.ConnectType.CLIENT);
    }

    public void start(){
        Executors.newSingleThreadScheduledExecutor().submit(new Runnable() {
            @Override
            public void run() {
                Log.w(TAG,"启动客户端");
                EventLoopGroup group = new NioEventLoopGroup();
                try{
                    ChannelInitClient channelInit = new ChannelInitClient(dataHandlerAdapter);
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .remoteAddress(new InetSocketAddress(address,port))
                            .handler(channelInit)
                            .option(ChannelOption.TCP_NODELAY,true)
                            .option(ChannelOption.SO_KEEPALIVE,true);
                    ChannelFuture channelFuture = bootstrap.connect().sync();
                    channel = channelFuture.channel();
                    channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if(future.isSuccess()){
                                Log.w(TAG,"客户端连接成功");
                                MessageHandler.getInstance().sendMessage(MessageType.CLIENT_CONNECT_SUCCESS,"客户端连接成功");
                            }else{
                                Log.w(TAG,"客户端连接失败");
                                MessageHandler.getInstance().sendMessage(MessageType.CLIENT_CONNECT_FAILED,"客户端连接失败");
                            }
                        }
                    });
                    channel.closeFuture().sync();
                    Log.w(TAG,"客户端关闭成功");
                    MessageHandler.getInstance().sendMessage(MessageType.CLIENT_CLOSE_SUCCESS,"客户端关闭成功");
                }catch (InterruptedException e){
                    e.printStackTrace();
                    MessageHandler.getInstance().sendMessage(MessageType.CLIENT_EXCEPTION,"客户端异常" + e.getMessage());
                }finally {
                    try{
                        group.shutdownGracefully().sync();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                        MessageHandler.getInstance().sendMessage(MessageType.CLIENT_EXCEPTION,"客户端异常2" + e.getMessage());
                    }
                }
            }
        });
    }

    public void addHeartBeat(HeartBeatListener listener){
        if(dataHandlerAdapter != null){
            dataHandlerAdapter.addHeartBeatListener(listener);
        }
    }

    public void setHandler(Handler handler){
        MessageHandler.getInstance().setHandler(handler);
    }

    public boolean sentData(byte[] data){
        return dataHandlerAdapter.sendData(data);
    }

    public void stop(){
        Executors.newSingleThreadScheduledExecutor().submit(new Runnable() {
            @Override
            public void run() {
                if(channel != null){
                    channel.close();
                    channel = null;
                }
            }
        });
    }
}