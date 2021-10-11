package com.web.quiz.components;

import com.web.quiz.models.Message;
import com.web.quiz.models.Player;
import com.web.quiz.services.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private PlayerService playerService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
//        System.out.println(sessionId);
        if(sessionId != null) {
            Optional<Player> optionalPlayer = this.playerService.findBySessionIdAndStatus(sessionId, "in");
            if (optionalPlayer.isPresent()) {
//                System.out.println(optionalPlayer.get());
                Player player = optionalPlayer.get();
                logger.info("User Disconnected : " + player.getName());
                Message message = new Message();
                message.setType(Message.MessageType.LEAVE);
                message.setSender(new Message.Sender(player.getName(), player.getId(), player.getPoint()));
                player.setStatus("out");
                this.playerService.savePlayer(player);
                messagingTemplate.convertAndSend("/play/public-room/" + player.getIdGame().getId(), message);
            }
        }
    }

}