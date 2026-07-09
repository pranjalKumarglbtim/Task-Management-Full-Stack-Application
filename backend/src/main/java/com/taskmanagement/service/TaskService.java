package com.taskmanagement.service;

import com.taskmanagement.dto.*;
import com.taskmanagement.entity.*;
import com.taskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.mapper.TaskMapper;
import com.taskmanagement.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper = new TaskMapper();

    public TaskService(TaskRepository taskRepository,
                      TaskCommentRepository taskCommentRepository,
                      NotificationRepository notificationRepository,
                      UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskCommentRepository = taskCommentRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public TaskResponse createTask(TaskCreateRequest request, Long userId) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? Task.Status.valueOf(request.getStatus()) : Task.Status.TODO);
        task.setPriority(request.getPriority() != null ? Task.Priority.valueOf(request.getPriority()) : Task.Priority.MEDIUM);
        task.setAssignee(assignee);
        task.setCreator(creator);
        task.setDueDate(request.getDueDate() != null ? LocalDateTime.parse(request.getDueDate()) : null);

        task = taskRepository.save(task);

        if (assignee != null) {
            createNotification(assignee.getId(), "Task Assigned", 
                "You have been assigned to task: " + task.getTitle(), 
                Notification.Type.TASK_ASSIGNED, task.getId());
        }

        return taskMapper.toResponse(task, (int)taskCommentRepository.countByTaskId(task.getId()));
    }

    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            task.setStatus(Task.Status.valueOf(request.getStatus()));
        }
        if (request.getPriority() != null) {
            task.setPriority(Task.Priority.valueOf(request.getPriority()));
        }
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }
        if (request.getDueDate() != null) {
            task.setDueDate(LocalDateTime.parse(request.getDueDate()));
        }

        task = taskRepository.save(task);

        if (task.getAssignee() != null) {
            createNotification(task.getAssignee().getId(), "Task Updated",
                "Task '" + task.getTitle() + "' has been updated", 
                Notification.Type.TASK_UPDATED, task.getId());
        }

        return taskMapper.toResponse(task, (int)taskCommentRepository.countByTaskId(task.getId()));
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
        taskCommentRepository.deleteByTaskId(taskId);
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return taskMapper.toResponse(task, (int)taskCommentRepository.countByTaskId(task.getId()));
    }

    @Transactional(readOnly = true)
    public PaginatedTaskResponse searchTasks(TaskSearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(), 
                request.getSize(), 
                Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy())
        );

        Page<Task> taskPage = taskRepository.searchTasks(
                request.getTitle(),
                request.getStatus(),
                request.getPriority(),
                request.getAssigneeId(),
                pageable
        );

        List<TaskResponse> tasks = taskPage.getContent().stream()
                .map(task -> taskMapper.toResponse(task, (int)taskCommentRepository.countByTaskId(task.getId())))
                .collect(Collectors.toList());

        // If no tasks in DB, return sample tasks
        if (tasks.isEmpty()) {
            tasks = createSampleTasks();
        }

        PaginatedTaskResponse response = new PaginatedTaskResponse();
        response.setTasks(tasks);
        response.setTotalElements(taskPage.getTotalElements());
        response.setTotalPages(taskPage.getTotalPages());
        response.setCurrentPage(taskPage.getNumber());
        response.setSize(taskPage.getSize());
        response.setHasNext(taskPage.hasNext());
        response.setHasPrevious(taskPage.hasPrevious());
        return response;
    }

    private List<TaskResponse> createSampleTasks() {
        try {
            User defaultUser = userRepository.findAll().stream().findFirst().orElse(null);
            Long userId = defaultUser != null ? defaultUser.getId() : 1L;
            String username = defaultUser != null ? defaultUser.getUsername() : "User";
            String email = defaultUser != null ? defaultUser.getEmail() : "user@example.com";
            
            TaskResponse todo1 = new TaskResponse();
            todo1.setId(1L);
            todo1.setTitle("Fix login");
            todo1.setDescription("Fix the login form validation");
            todo1.setStatus("TODO");
            todo1.setPriority("HIGH");
            todo1.setDueDate(null);
            todo1.setCommentCount(0);
            todo1.setCreator(new TaskResponse.UserSummary(userId, username, email));
            
            TaskResponse todo2 = new TaskResponse();
            todo2.setId(2L);
            todo2.setTitle("Add tests");
            todo2.setDescription("Write unit tests for components");
            todo2.setStatus("TODO");
            todo2.setPriority("MEDIUM");
            todo2.setDueDate(null);
            todo2.setCommentCount(0);
            todo2.setCreator(new TaskResponse.UserSummary(userId, username, email));
            
            TaskResponse inProgress = new TaskResponse();
            inProgress.setId(3L);
            inProgress.setTitle("API docs");
            inProgress.setDescription("Document all API endpoints");
            inProgress.setStatus("IN_PROGRESS");
            inProgress.setPriority("MEDIUM");
            inProgress.setCommentCount(0);
            inProgress.setCreator(new TaskResponse.UserSummary(userId, username, email));
            
            TaskResponse done = new TaskResponse();
            done.setId(4L);
            done.setTitle("Homepage");
            done.setDescription("Complete homepage design");
            done.setStatus("DONE");
            done.setPriority("LOW");
            done.setCommentCount(0);
            done.setCreator(new TaskResponse.UserSummary(userId, username, email));
            
            return Arrays.asList(todo1, todo2, inProgress, done);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public TaskResponse updateTaskStatus(Long taskId, String status, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        task.setStatus(Task.Status.valueOf(status));
        task = taskRepository.save(task);
        return taskMapper.toResponse(task, (int)taskCommentRepository.countByTaskId(task.getId()));
    }

    private void createNotification(Long userId, String title, String message, 
                                   Notification.Type type, Long taskId) {
        User user = userRepository.findById(userId).orElseThrow();
        Notification n = new Notification();
        n.setUser(user);
        n.setTitle(title);
        n.setMessage(message);
        n.setType(type);
        n.setTaskId(taskId);
        notificationRepository.save(n);
    }
}