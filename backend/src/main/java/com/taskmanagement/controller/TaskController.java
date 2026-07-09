package com.taskmanagement.controller;

import com.taskmanagement.dto.*;
import com.taskmanagement.service.TaskService;
import com.taskmanagement.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Task management APIs")
public class TaskController {
    private final TaskService taskService;
    private final JwtTokenProvider jwtTokenProvider;

    public TaskController(TaskService taskService, JwtTokenProvider jwtTokenProvider) {
        this.taskService = taskService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<DashboardStats> getStats() {
        DashboardStats stats = new DashboardStats();
        stats.setTotalTasks(12L);
        stats.setCompletedTasks(5L);
        stats.setOverdueTasks(3L);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/activity")
    @Operation(summary = "Get recent activity")
    public ResponseEntity<Map<String, Object>> getActivity() {
        List<String> activities = Arrays.asList(
            "John updated \"Fix login\" status",
            "Sarah commented on \"API docs\"",
            "New task \"Deploy to production\" created"
        );
        return ResponseEntity.ok(Collections.singletonMap("activities", activities));
    }

    @GetMapping
    @Operation(summary = "Search tasks")
    public ResponseEntity<PaginatedTaskResponse> searchTasks(TaskSearchRequest request) {
        return ResponseEntity.ok(taskService.searchTasks(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id, null));
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskCreateRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = 1L;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            userId = jwtTokenProvider.getUserIdFromToken(authorization.substring(7));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request, userId));
    }
}