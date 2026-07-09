package com.taskmanagement.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TaskCommentRequest {
    private String content;

    public TaskCommentRequest() {}

    public TaskCommentRequest(String content) {
        this.content = content;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}