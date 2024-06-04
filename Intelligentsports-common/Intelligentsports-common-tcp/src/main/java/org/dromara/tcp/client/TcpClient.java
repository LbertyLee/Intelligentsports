package org.dromara.tcp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.tcp.handler.TCPClientHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TcpClient implements CommandLineRunner {

    private static final String SERVER_ADDRESS = "api.atomconnection.com";
    private static final int SERVER_PORT = 8062;


    /**
     * 程序的入口点，用于启动TCP客户端。
     * 这里使用Netty框架来建立与服务器的连接，并处理通信。
     *
     * @param args 命令行参数，目前未使用。
     * @throws Exception 如果连接或通信过程中发生错误。
     */
    @Override
    public void run(String... args) throws Exception {
        // 创建一个事件循环组，用于处理I/O事件。
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 配置客户端启动程序。
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 配置频道的处理器管道，用于编码、解码和处理业务逻辑。
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new TCPClientHandler());
                        }
                    });
            // 连接到服务器，并同步等待连接完成。
            ChannelFuture future = bootstrap.connect(SERVER_ADDRESS, SERVER_PORT).sync();
            log.info("TCPClient Start , Connect host:"+SERVER_ADDRESS+":"+SERVER_PORT);
            // 等待频道关闭。
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("TCPClient Error", e);
        } finally {
            // 关闭事件循环组，释放资源。
            group.shutdownGracefully();
        }
    }

}
