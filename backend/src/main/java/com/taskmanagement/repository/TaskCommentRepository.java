package com.taskmanagement.repository;

import com.taskmanagement.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findByTaskIdOrderByCreatedAtAsc(Long taskId);
    
    void deleteByTaskId(Long taskId);

    @Query("SELECT COUNT(tc) FROM TaskComment tc WHERE tc.task.id = :taskId")
    long countByTaskId(Long taskId);
}