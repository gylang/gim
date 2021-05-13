package com.zone.test.webrtc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * Created by Owen Pan on 2017/1/17.
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketServer implements WebSocketConfigurer, WebMvcConfigurer {

    @Autowired
    @Lazy
    private SimpMessagingTemplate template;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(logWebSocketHandler(), "/websocket").setAllowedOrigins("*"); // 此处与客户端的 URL 相对应
        registry.addHandler(logWebSocketHandler(), "/sockjs/webSocketServer").setAllowedOrigins("*").addInterceptors(new WebRTCInterceptor()).withSockJS();

        registry.addHandler(videoHandler(), "/video").setAllowedOrigins("*"); // 此处与客户端的 URL 相对应
        registry.addHandler(videoHandler(), "/sockjs/videoServer").setAllowedOrigins("*").addInterceptors(new WebRTCInterceptor()).withSockJS();
    }

    @Bean
    public WebSocketHandler logWebSocketHandler() {
        return new WebRTCHandler(template);
    }

    @Bean
    public VideoHandler videoHandler() {
        return new VideoHandler();
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(500000);
        container.setMaxBinaryMessageBufferSize(500000);
        return container;
    }

}