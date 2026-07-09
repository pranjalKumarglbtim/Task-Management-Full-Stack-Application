package com.taskmanagement.controller;

import com.taskmanagement.dto.*;
import com.taskmanagement.service.NotificationService;
import com.taskmanagement.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification management APIs")
public class NotificationController {
    private final NotificationService notificationService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    @Operation(summary = "Get all notifications for user")
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtTokenProvider.getUserIdFromToken(authorization.substring(7));
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications count")
    public ResponseEntity<Long> getUnreadCount(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtTokenProvider.getUserIdFromToken(authorization.substring(7));
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    @PutMapping("/read")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<Void> markAllAsRead(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtTokenProvider.getUserIdFromToken(authorization.substring(7));
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}