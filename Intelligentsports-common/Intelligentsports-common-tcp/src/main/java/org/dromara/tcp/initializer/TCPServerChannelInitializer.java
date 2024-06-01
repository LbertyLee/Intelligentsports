package org.dromara.tcp.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.Resource;
import org.dromara.tcp.handler.TCPServerHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TCPServerChannelInitializer extends ChannelInitializer {

    @Resource
    TCPServerHandler tcpServerHandler;

    protected void initChannel(Channel channel) throws Exception {
        //心跳超时控制
        channel.pipeline().addLast("idle",
            new IdleStateHandler(15, 0, 0, TimeUnit.MINUTES));
        channel.pipeline().addLast(tcpServerHandler);
    }

}
