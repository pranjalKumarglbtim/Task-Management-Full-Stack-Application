package com.taskmanagement.controller;

import com.taskmanagement.dto.*;
import com.taskmanagement.service.CommentService;
import com.taskmanagement.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task Comments", description = "Task comment management APIs")
public class CommentController {
    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    public CommentController(CommentService commentService, JwtTokenProvider jwtTokenProvider) {
        this.commentService = commentService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/{taskId}/comments")
    @Operation(summary = "Get comments for a task")
    public ResponseEntity<List<TaskCommentResponse>> getComments(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId));
    }

    @PostMapping("/{taskId}/comments")
    @Operation(summary = "Add a comment to a task")
    public ResponseEntity<TaskCommentResponse> addComment(
            @PathVariable Long taskId,
            @RequestBody TaskCommentRequest request,
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtTokenProvider.getUserIdFromToken(authorization.substring(7));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(taskId, request, userId));
    }
}