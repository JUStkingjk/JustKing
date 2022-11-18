package com.jk.justking.Netty.server;

import com.jk.justking.Netty.DataHandlerAdapter;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class ChannelInitServer extends ChannelInitializer<SocketChannel> {

    private final DataHandlerAdapter adapter;

    ChannelInitServer(DataHandlerAdapter adapter){this.adapter = adapter;}


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        try{
            ChannelPipeline channelPipeline = ch.pipeline();
            //添加心跳机制
            channelPipeline.addLast(new IdleStateHandler(3000,3000,3000, TimeUnit.MILLISECONDS));
            //添加数据处理
            channelPipeline.addLast(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
