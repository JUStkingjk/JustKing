package com.jk.justking.Netty.server;

import android.os.Handler;
import android.util.Log;

import com.jk.justking.Netty.DataHandlerAdapter;
import com.jk.justking.Netty.HeartBeatListener;
import com.jk.justking.Netty.MessageHandler;
import com.jk.justking.Netty.MessageType;

import java.util.concurrent.Executors;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyServer {

    private static final String TAG = "Netty Server";

    private Channel channel;

    private int port;

    private DataHandlerAdapter dataHandlerAdapter;

    public NettyServer(int port){
        this.port = port;
        dataHandlerAdapter = new DataHandlerAdapter(DataHandlerAdapter.ConnectType.SERVER);
    }

    public void start(){
        Executors.newSingleThreadScheduledExecutor().submit(new Runnable() {
            @Override
            public void run() {
                Log.w(TAG,"服务启动");
                EventLoopGroup boss = new NioEventLoopGroup();
                EventLoopGroup worker = new NioEventLoopGroup();
                try{
                    ChannelInitServer channelInit = new ChannelInitServer(dataHandlerAdapter);
                    ServerBootstrap serverBootstrap = new ServerBootstrap();
                    serverBootstrap.group(boss,worker)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(channelInit)
                            .option(ChannelOption.SO_BACKLOG,128)
                            .option(ChannelOption.TCP_NODELAY,true)
                            .option(ChannelOption.SO_KEEPALIVE,true);
                    ChannelFuture cf = serverBootstrap.bind(port).sync();
                    channel = cf.channel();
                    cf.addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if(future.isSuccess()){
                                Log.w(TAG,"服务启动成功");
                                MessageHandler.getInstance().sendMessage(MessageType.SERVER_START_SUCCESS,"服务启动成功");
                            }else{
                                Log.w(TAG,"服务启动失败");
                                MessageHandler.getInstance().sendMessage(MessageType.SERVER_START_FAILED,"服务启动失败");
                            }
                        }
                    });
                    channel.closeFuture().sync();
                    Log.w(TAG,"服务器关成功");
                    MessageHandler.getInstance().sendMessage(MessageType.SERVER_CLOSE_SUCCESS,"服务器关闭成功");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    MessageHandler.getInstance().sendMessage(MessageType.SERVER_EXCEPTION,"服务异常：" + e.getMessage());
                }finally {
                    try{
                        worker.shutdownGracefully().sync();
                        boss.shutdownGracefully().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        MessageHandler.getInstance().sendMessage(MessageType.SERVER_EXCEPTION,"服务异常2" + e.getMessage());
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

    public boolean sendData(byte[] data){
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
