package org.dromara.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TCPClientHandler extends SimpleChannelInboundHandler<String> {

    // 登录请求
    private static final String LOGIN_REQUEST = "{\"type\":\"LOGIN\",\"name\":\"fjqm\",\"password\":\"fjqmProd@2024\"}";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当通道激活时发送登录请求
        ctx.writeAndFlush(LOGIN_REQUEST);
    }

    /**
     * 当从通道读取到消息时执行此方法。
     * 此方法重写了ChannelInboundHandlerAdapter的channelRead0方法，专门处理读取到的字符串消息。
     *
     * @param ctx 通道上下文，用于通道的管理和操作。
     * @param msg 从通道读取到的字符串消息。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        // 记录接收到的TCP服务器消息。
        log.info("Receive TCPServer Message:\n" + msg);
    }

    /**
     * 当发生异常时执行此方法。
     * 此方法重写了ChannelInboundHandlerAdapter的exceptionCaught方法，用于处理通道操作过程中发生的异常。
     *
     * @param ctx 通道上下文，用于通道的管理和操作。
     * @param cause 异常原因，记录并关闭通道。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 记录发生的异常并关闭通道。
        log.error("TCPClient Error", cause);
        ctx.close();
    }
}