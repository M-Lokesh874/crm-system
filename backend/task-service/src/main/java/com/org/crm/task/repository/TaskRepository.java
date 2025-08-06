package com.org.crm.task.repository;

import com.org.crm.task.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Task entity
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTitle(String title);

    List<Task> findByStatus(Task.TaskStatus status);

    List<Task> findByPriority(Task.TaskPriority priority);

    List<Task> findByAssignedTo(String assignedTo);

    List<Task> findByRelatedTypeAndRelatedId(String relatedType, Long relatedId);

    List<Task> findByDueDateBefore(LocalDateTime date);

    List<Task> findByDueDateAfter(LocalDateTime date);

    List<Task> findByStatusAndAssignedTo(Task.TaskStatus status, String assignedTo);

    Page<Task> findByStatus(Task.TaskStatus status, Pageable pageable);

    Page<Task> findByAssignedTo(String assignedTo, Pageable pageable);

    Page<Task> findByPriority(Task.TaskPriority priority, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :startDate AND :endDate")
    List<Task> findByDueDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Task t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<Task> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    long countByStatus(Task.TaskStatus status);

    long countByPriority(Task.TaskPriority priority);

    long countByAssignedTo(String assignedTo);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.dueDate < :now AND t.status <> 'COMPLETED'")
    long countOverdueTasks(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = 'COMPLETED' AND t.completedAt BETWEEN :startDate AND :endDate")
    long countCompletedTasksInRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    boolean existsByTitle(String title);
}