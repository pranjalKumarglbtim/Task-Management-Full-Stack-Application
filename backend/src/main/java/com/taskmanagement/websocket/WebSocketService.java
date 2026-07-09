package com.taskmanagement.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastTask(Object taskData) {
        messagingTemplate.convertAndSend("/topic/tasks", taskData);
    }

    public void broadcastNotification(Object notificationData) {
        messagingTemplate.convertAndSend("/topic/notifications", notificationData);
    }

    public void broadcastPresence(Object presenceData) {
        messagingTemplate.convertAndSend("/topic/presence", presenceData);
    }
}