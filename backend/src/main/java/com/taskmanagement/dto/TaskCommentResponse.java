package com.taskmanagement.dto;

import java.time.LocalDateTime;

public class TaskCommentResponse {
    private Long id;
    private String content;
    private TaskResponse.UserSummary user;
    private LocalDateTime createdAt;

    public TaskCommentResponse() {}

    public TaskCommentResponse(Long id, String content, TaskResponse.UserSummary user, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public TaskResponse.UserSummary getUser() { return user; }
    public void setUser(TaskResponse.UserSummary user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}