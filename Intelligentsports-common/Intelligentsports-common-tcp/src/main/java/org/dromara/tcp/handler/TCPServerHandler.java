package org.dromara.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TCPServerHandler extends ChannelInboundHandlerAdapter {

    public ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //从 channel  中取到msg  转换为buf
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            log.info("终端IP：{}", ctx.channel().remoteAddress());
            log.info("收到数据：{}", byteBuf.toString(CharsetUtil.UTF_8));

            ctx.channel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep( 1000);
                        String resStr = "01 03 00 02 00 23 A5 D3";
                        ctx.writeAndFlush(Unpooled.copiedBuffer(resStr, CharsetUtil.UTF_8));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

}
