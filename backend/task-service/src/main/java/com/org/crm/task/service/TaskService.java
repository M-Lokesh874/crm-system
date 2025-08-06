package com.org.crm.task.service;

import com.org.crm.task.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Task operations
 */
public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);

    Optional<TaskResponse> getTaskById(Long id);

    Optional<TaskResponse> getTaskByTitle(String title);

    TaskResponse updateTask(Long id, UpdateTaskRequest request);

    void deleteTask(Long id);

    Page<TaskResponse> getAllTasks(Pageable pageable);

    Page<TaskResponse> getTasksByStatus(Task.TaskStatus status, Pageable pageable);

    Page<TaskResponse> getTasksByAssignedTo(String assignedTo, Pageable pageable);

    Page<TaskResponse> getTasksByPriority(Task.TaskPriority priority, Pageable pageable);

    List<TaskResponse> getTasksByRelatedEntity(String relatedType, Long relatedId);

    List<TaskResponse> getTasksByDueDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<TaskResponse> getTasksByCreatedAtRange(LocalDateTime startDate, LocalDateTime endDate);

    TaskResponse updateTaskStatus(Long id, Task.TaskStatus status);

    TaskResponse updateTaskAssignment(Long id, String assignedTo);

    TaskResponse updateTaskPriority(Long id, Task.TaskPriority priority);

    TaskResponse updateTaskDueDate(Long id, LocalDateTime dueDate);

    TaskResponse markTaskCompleted(Long id);

    TaskStatistics getTaskStatistics();

    boolean titleExists(String title);

    long getTaskCountByStatus(Task.TaskStatus status);

    long getTaskCountByPriority(Task.TaskPriority priority);

    long getTaskCountByAssignedTo(String assignedTo);

    long getOverdueTaskCount();

    long getCompletedTaskCountInRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Task response DTO
     */
    record TaskResponse(
            Long id,
            String title,
            String description,
            Task.TaskStatus status,
            Task.TaskPriority priority,
            LocalDateTime dueDate,
            LocalDateTime completedAt,
            String assignedTo,
            String relatedType,
            Long relatedId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static TaskResponse fromTask(Task task) {
            return new TaskResponse(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getPriority(),
                    task.getDueDate(),
                    task.getCompletedAt(),
                    task.getAssignedTo(),
                    task.getRelatedType(),
                    task.getRelatedId(),
                    task.getCreatedAt(),
                    task.getUpdatedAt()
            );
        }
    }

    /**
     * Create task request DTO
     */
    record CreateTaskRequest(
            String title,
            String description,
            Task.TaskStatus status,
            Task.TaskPriority priority,
            LocalDateTime dueDate,
            String assignedTo,
            String relatedType,
            Long relatedId
    ) {}

    /**
     * Update task request DTO
     */
    record UpdateTaskRequest(
            String title,
            String description,
            Task.TaskStatus status,
            Task.TaskPriority priority,
            LocalDateTime dueDate,
            String assignedTo,
            String relatedType,
            Long relatedId
    ) {}

    /**
     * Task statistics DTO
     */
    record TaskStatistics(
            long totalTasks,
            long pendingTasks,
            long inProgressTasks,
            long completedTasks,
            long overdueTasks,
            long cancelledTasks,
            long lowPriorityTasks,
            long mediumPriorityTasks,
            long highPriorityTasks,
            long urgentPriorityTasks
    ) {}
}