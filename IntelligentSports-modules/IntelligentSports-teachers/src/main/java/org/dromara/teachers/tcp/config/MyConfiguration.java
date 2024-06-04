package org.dromara.teachers.tcp.config;

import org.dromara.teachers.tcp.client.TcpClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MyConfiguration {

    @Bean
    public ApplicationRunner myApplicationRunner(TcpClient tcpClient) throws IOException {
        return args -> {
            Thread tcpClientThread = new Thread(tcpClient::run);
            tcpClientThread.start();
        };
    }

}
