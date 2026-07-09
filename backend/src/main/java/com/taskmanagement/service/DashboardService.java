package com.taskmanagement.service;

import com.taskmanagement.dto.*;
import com.taskmanagement.entity.*;
import com.taskmanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DashboardService {
    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;

    public DashboardService(TaskRepository taskRepository,
                           NotificationRepository notificationRepository) {
        this.taskRepository = taskRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStats getDashboardStats() {
        long totalTasks = taskRepository.count();
        long completedTasks = taskRepository.countByStatusDone();
        long pendingTasks = taskRepository.countByStatusNotDone();
        long overdueTasks = taskRepository.countOverdueTasks(LocalDateTime.now());

        List<DashboardStats.RecentActivity> recentActivities = getRecentActivities();
        List<DashboardStats.UpcomingDeadline> upcomingDeadlines = getUpcomingDeadlines();
        DashboardStats.ProductivityMetrics metrics = getProductivityMetrics();

        DashboardStats stats = new DashboardStats();
        stats.setTotalTasks(totalTasks);
        stats.setCompletedTasks(completedTasks);
        stats.setPendingTasks(pendingTasks);
        stats.setOverdueTasks(overdueTasks);
        stats.setRecentActivities(recentActivities);
        stats.setUpcomingDeadlines(upcomingDeadlines);
        stats.setProductivityMetrics(metrics);
        return stats;
    }

    private List<DashboardStats.RecentActivity> getRecentActivities() {
        return taskRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(task -> {
                    DashboardStats.RecentActivity activity = new DashboardStats.RecentActivity();
                    activity.setTaskId(task.getId());
                    activity.setTitle(task.getTitle());
                    activity.setAction("CREATED");
                    activity.setUsername(task.getCreator().getUsername());
                    activity.setTimestamp(task.getCreatedAt());
                    return activity;
                })
                .collect(Collectors.toList());
    }

    private List<DashboardStats.UpcomingDeadline> getUpcomingDeadlines() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusDays(7);
        return taskRepository.findAll().stream()
                .filter(task -> task.getDueDate() != null && 
                       task.getDueDate().isAfter(now) && 
                       task.getDueDate().isBefore(nextWeek))
                .sorted(Comparator.comparing(Task::getDueDate))
                .limit(5)
                .map(task -> {
                    DashboardStats.UpcomingDeadline deadline = new DashboardStats.UpcomingDeadline();
                    deadline.setTaskId(task.getId());
                    deadline.setTitle(task.getTitle());
                    deadline.setAssignee(task.getAssignee() != null ? task.getAssignee().getUsername() : "Unassigned");
                    deadline.setDueDate(task.getDueDate());
                    return deadline;
                })
                .collect(Collectors.toList());
    }

    private DashboardStats.ProductivityMetrics getProductivityMetrics() {
        long total = taskRepository.count();
        long completed = taskRepository.countByStatusDone();
        double completionRate = total > 0 ? ((double) completed / total) * 100 : 0;

        DashboardStats.ProductivityMetrics metrics = new DashboardStats.ProductivityMetrics();
        metrics.setCompletionRate(Math.round(completionRate * 100.0) / 100.0);
        metrics.setTasksCompletedThisWeek(0);
        metrics.setTasksCreatedThisWeek(0);
        return metrics;
    }
}