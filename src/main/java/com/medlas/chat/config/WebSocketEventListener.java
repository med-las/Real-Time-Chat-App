package com.medlas.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import com.medlas.chat.chat.ChatMessage;
import com.medlas.chat.chat.MessageType;



@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    public final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketEventListener (SessionConnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            log.info("User Disconnected: " + username);

            var chatMessage = ChatMessage.builder()
                    .content("User Disconnected")
                    .sender(username)
                    .type(MessageType.LEAVE)
                    .build();
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }

    }
}
