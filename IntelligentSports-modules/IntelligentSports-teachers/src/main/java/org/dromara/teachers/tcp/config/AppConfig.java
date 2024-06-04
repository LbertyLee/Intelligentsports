package org.dromara.teachers.tcp.config;

import org.dromara.teachers.tcp.client.TcpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public TcpClient tcpClient() {
        return new TcpClient();
    }
}
