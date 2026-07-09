package com.taskmanagement.service;

import com.taskmanagement.dto.*;
import com.taskmanagement.entity.*;
import com.taskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentService(TaskCommentRepository taskCommentRepository,
                        TaskRepository taskRepository,
                        UserRepository userRepository) {
        this.taskCommentRepository = taskCommentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskCommentResponse addComment(Long taskId, TaskCommentRequest request, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TaskComment comment = new TaskComment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(request.getContent());

        comment = taskCommentRepository.save(comment);

        TaskCommentResponse response = new TaskCommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        
        TaskResponse.UserSummary userSummary = new TaskResponse.UserSummary();
        userSummary.setId(user.getId());
        userSummary.setUsername(user.getUsername());
        userSummary.setEmail(user.getEmail());
        response.setUser(userSummary);
        
        return response;
    }

    @Transactional(readOnly = true)
    public List<TaskCommentResponse> getCommentsByTaskId(Long taskId) {
        return taskCommentRepository.findByTaskIdOrderByCreatedAtAsc(taskId).stream()
                .map(comment -> {
                    TaskCommentResponse response = new TaskCommentResponse();
                    response.setId(comment.getId());
                    response.setContent(comment.getContent());
                    response.setCreatedAt(comment.getCreatedAt());
                    
                    TaskResponse.UserSummary userSummary = new TaskResponse.UserSummary();
                    userSummary.setId(comment.getUser().getId());
                    userSummary.setUsername(comment.getUser().getUsername());
                    userSummary.setEmail(comment.getUser().getEmail());
                    response.setUser(userSummary);
                    
                    return response;
                })
                .collect(Collectors.toList());
    }
}