package com.valuewith.tweaver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  /**
   * 클라이언트에서 websocket 연결할 때 사용할 api 경로를 설정해 주는 메서드입니다.
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    /*
    메시지 수신 경로입니다.
    수신자는 설정한 경로를 구독합니다.
    이후 MessageBroker가 구독자들에게 메시지를 전달해 줍니다.
     */
    registry.enableSimpleBroker("/sub");

    /*
    메시지 발신 경로입니다.
    설정한 경로가 접두사로 붙을 때, MessageBroker가 전달되는 메시지를 받아
    해당 채팅방을 구독하고 있는 클라이언트에게 메시지를 전달해 줍니다.
     */
    registry.setApplicationDestinationPrefixes("/pub");
  }
}
