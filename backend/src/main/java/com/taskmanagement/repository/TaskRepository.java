package com.taskmanagement.repository;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    Page<Task> findByStatus(String status, Pageable pageable);
    
    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE " +
           "(:title IS NULL OR t.title LIKE %:title%) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:assigneeId IS NULL OR t.assignee.id = :assigneeId)")
    Page<Task> searchTasks(
            @Param("title") String title,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("assigneeId") Long assigneeId,
            Pageable pageable);

    List<Task> findTop5ByOrderByCreatedAtDesc();
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = 'DONE'")
    long countByStatusDone();
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status != 'DONE'")
    long countByStatusNotDone();
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status != 'DONE' AND t.dueDate < :now")
    long countOverdueTasks(LocalDateTime now);
}