package com.taskmanagement.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DashboardStats {
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long overdueTasks;
    private List<RecentActivity> recentActivities;
    private List<UpcomingDeadline> upcomingDeadlines;
    private ProductivityMetrics productivityMetrics;

    public DashboardStats() {}

    // Getters and Setters
    public long getTotalTasks() { return totalTasks; }
    public void setTotalTasks(long totalTasks) { this.totalTasks = totalTasks; }

    public long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(long completedTasks) { this.completedTasks = completedTasks; }

    public long getPendingTasks() { return pendingTasks; }
    public void setPendingTasks(long pendingTasks) { this.pendingTasks = pendingTasks; }

    public long getOverdueTasks() { return overdueTasks; }
    public void setOverdueTasks(long overdueTasks) { this.overdueTasks = overdueTasks; }

    public List<RecentActivity> getRecentActivities() { return recentActivities; }
    public void setRecentActivities(List<RecentActivity> recentActivities) { this.recentActivities = recentActivities; }

    public List<UpcomingDeadline> getUpcomingDeadlines() { return upcomingDeadlines; }
    public void setUpcomingDeadlines(List<UpcomingDeadline> upcomingDeadlines) { this.upcomingDeadlines = upcomingDeadlines; }

    public ProductivityMetrics getProductivityMetrics() { return productivityMetrics; }
    public void setProductivityMetrics(ProductivityMetrics productivityMetrics) { this.productivityMetrics = productivityMetrics; }

    public static class RecentActivity {
        private Long taskId;
        private String title;
        private String action;
        private String username;
        private LocalDateTime timestamp;

        public RecentActivity() {}

        // Getters and Setters
        public Long getTaskId() { return taskId; }
        public void setTaskId(Long taskId) { this.taskId = taskId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }

    public static class UpcomingDeadline {
        private Long taskId;
        private String title;
        private String assignee;
        private LocalDateTime dueDate;

        public UpcomingDeadline() {}

        // Getters and Setters
        public Long getTaskId() { return taskId; }
        public void setTaskId(Long taskId) { this.taskId = taskId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAssignee() { return assignee; }
        public void setAssignee(String assignee) { this.assignee = assignee; }

        public LocalDateTime getDueDate() { return dueDate; }
        public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    }

    public static class ProductivityMetrics {
        private double completionRate;
        private int tasksCompletedThisWeek;
        private int tasksCreatedThisWeek;

        public ProductivityMetrics() {}

        // Getters and Setters
        public double getCompletionRate() { return completionRate; }
        public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }

        public int getTasksCompletedThisWeek() { return tasksCompletedThisWeek; }
        public void setTasksCompletedThisWeek(int tasksCompletedThisWeek) { this.tasksCompletedThisWeek = tasksCompletedThisWeek; }

        public int getTasksCreatedThisWeek() { return tasksCreatedThisWeek; }
        public void setTasksCreatedThisWeek(int tasksCreatedThisWeek) { this.tasksCreatedThisWeek = tasksCreatedThisWeek; }
    }
}