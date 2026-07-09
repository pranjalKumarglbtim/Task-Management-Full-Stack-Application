package com.taskmanagement.mapper;

import com.taskmanagement.dto.TaskResponse;
import com.taskmanagement.entity.Task;

public class TaskMapper {
    
    public TaskResponse toResponse(Task task, int commentCount) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus().name());
        response.setPriority(task.getPriority().name());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        response.setCommentCount(commentCount);
        
        if (task.getAssignee() != null) {
            TaskResponse.UserSummary assignee = new TaskResponse.UserSummary();
            assignee.setId(task.getAssignee().getId());
            assignee.setUsername(task.getAssignee().getUsername());
            assignee.setEmail(task.getAssignee().getEmail());
            response.setAssignee(assignee);
        }
        
        if (task.getCreator() != null) {
            TaskResponse.UserSummary creator = new TaskResponse.UserSummary();
            creator.setId(task.getCreator().getId());
            creator.setUsername(task.getCreator().getUsername());
            creator.setEmail(task.getCreator().getEmail());
            response.setCreator(creator);
        }
        
        return response;
    }
}