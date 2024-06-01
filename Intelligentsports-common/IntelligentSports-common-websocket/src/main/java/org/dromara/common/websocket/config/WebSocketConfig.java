package org.dromara.common.websocket.config;

import cn.hutool.core.util.StrUtil;
import org.dromara.common.websocket.config.properties.WebSocketProperties;
import org.dromara.common.websocket.handler.PlusWebSocketHandler;
import org.dromara.common.websocket.interceptor.PlusWebSocketInterceptor;
import org.dromara.common.websocket.listener.WebSocketTopicListener;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * WebSocket 配置
 *
 * @author zendwang
 */
@AutoConfiguration
@ConditionalOnProperty(value = "websocket.enabled", havingValue = "true")
@EnableConfigurationProperties(WebSocketProperties.class)
@EnableWebSocket
public class WebSocketConfig {

    /**
     * 配置WebSocket处理器和拦截器。
     *
     * @param handshakeInterceptor 握手拦截器，用于在WebSocket连接建立时进行拦截。
     * @param webSocketHandler WebSocket处理器，负责处理WebSocket消息。
     * @param webSocketProperties WebSocket配置属性，包括连接路径和允许的来源等配置。
     * @return WebSocketConfigurer 实例，用于配置WebSocket处理器和拦截器等。
     */
    @Bean
    public WebSocketConfigurer webSocketConfigurer(HandshakeInterceptor handshakeInterceptor,
                                                   WebSocketHandler webSocketHandler,
                                                   WebSocketProperties webSocketProperties) {
        // 设置WebSocket连接路径和允许的跨域来源，如果未配置则使用默认值
        if (StrUtil.isBlank(webSocketProperties.getPath())) {
            webSocketProperties.setPath("/websocket");
        }

        if (StrUtil.isBlank(webSocketProperties.getAllowedOrigins())) {
            webSocketProperties.setAllowedOrigins("*");
        }

        return registry -> registry
            .addHandler(webSocketHandler, webSocketProperties.getPath())
            .addInterceptors(handshakeInterceptor)
            .setAllowedOrigins(webSocketProperties.getAllowedOrigins());
    }

    /**
     * 创建并返回一个WebSocket握手拦截器实例。
     *
     * @return HandshakeInterceptor WebSocket握手拦截器实例。
     */
    @Bean
    public HandshakeInterceptor handshakeInterceptor() {
        return new PlusWebSocketInterceptor();
    }

    /**
     * 创建并返回一个WebSocket处理器实例。
     *
     * @return WebSocketHandler WebSocket处理器实例。
     */
    @Bean
    public WebSocketHandler webSocketHandler() {
        return new PlusWebSocketHandler();
    }

    /**
     * 创建并返回一个WebSocket主题监听器实例。
     *
     * @return WebSocketTopicListener WebSocket主题监听器实例。
     */
    @Bean
    public WebSocketTopicListener topicListener() {
        return new WebSocketTopicListener();
    }

}
