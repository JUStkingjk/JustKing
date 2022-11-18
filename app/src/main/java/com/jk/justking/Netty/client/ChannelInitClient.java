package com.jk.justking.Netty.client;

import com.jk.justking.Netty.DataHandlerAdapter;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

public class ChannelInitClient extends ChannelInitializer<Channel> {

    private DataHandlerAdapter adapter;

    ChannelInitClient(DataHandlerAdapter adapter){this.adapter = adapter;}

    @Override
    protected void initChannel(Channel ch) throws Exception {
        try {
            ChannelPipeline channelPipeline = ch.pipeline();
            channelPipeline.addLast(new IdleStateHandler(3000,3000,3000, TimeUnit.MILLISECONDS));
            channelPipeline.addLast(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
