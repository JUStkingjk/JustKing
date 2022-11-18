package com.jk.justking.Netty;


import android.util.Log;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
public class DataHandlerAdapter extends ChannelHandlerAdapter {

    private static final String TAG = "DataHandlerAdapter";

    public enum ConnectType{
        SERVER,
        CLIENT,
    }

    private ConnectType type;

    private ChannelHandlerContext channelHandlerContext;

    private HeartBeatListener listener;

    public DataHandlerAdapter(ConnectType type){this.type = type;}

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channelHandlerContext = ctx;

        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String connectAddress = socketAddress.getAddress().getHostAddress();
        if(type == ConnectType.SERVER){
            MessageHandler.getInstance().sendMessage(MessageType.SERVER_CONNECT_SUCCESS,connectAddress);
        }
        Log.w(TAG,"连接成功" + connectAddress);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.channelHandlerContext = ctx;
        Log.w(TAG,"连接断开");
        if(type == ConnectType.CLIENT){
            MessageHandler.getInstance().sendMessage(MessageType.CLIENT_DISCONNECT_SUCCESS,"连接断开");
        }else{
            MessageHandler.getInstance().sendMessage(MessageType.SERVER_DISCONNECT_SUCCESS,"连接断开");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.channelHandlerContext = ctx;
        Log.w(TAG,"收到数据");
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] recvData = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(recvData);
        byteBuf.clear();
        MessageHandler.getInstance().sendMessage(MessageType.RECEIVE_DATA,recvData);

    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        this.channelHandlerContext = ctx;
        ctx.write(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        this.channelHandlerContext = ctx;
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent)evt).state();
            if(state == IdleState.ALL_IDLE){
                sendData(listener.getHeartBeat());
            }else{
                super.userEventTriggered(ctx,evt);
            }
        }
    }

    public void addHeartBeatListener(HeartBeatListener listener){
        this.listener = listener;
    }

    public boolean sendData(byte[] data){
        ByteBuf byteBuf =channelHandlerContext.alloc().buffer();
        byteBuf.writeBytes(data);
        ChannelFuture future = channelHandlerContext.channel().write(byteBuf);
        channelHandlerContext.flush();
        return future.isSuccess();
    }
}
