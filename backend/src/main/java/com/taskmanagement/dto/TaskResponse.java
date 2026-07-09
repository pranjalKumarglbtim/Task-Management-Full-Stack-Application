package com.taskmanagement.dto;

import java.util.List;

public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private java.time.LocalDateTime dueDate;
    private UserSummary assignee;
    private UserSummary creator;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    private int commentCount;

    public TaskResponse() {}

    public static class UserSummary {
        private Long id;
        private String username;
        private String email;

        public UserSummary() {}

        public UserSummary(Long id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public java.time.LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(java.time.LocalDateTime dueDate) { this.dueDate = dueDate; }

    public UserSummary getAssignee() { return assignee; }
    public void setAssignee(UserSummary assignee) { this.assignee = assignee; }

    public UserSummary getCreator() { return creator; }
    public void setCreator(UserSummary creator) { this.creator = creator; }

    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }

    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
}