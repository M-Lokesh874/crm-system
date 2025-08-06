package com.org.crm.task.service.impl;

import com.org.crm.common.events.BaseEvent;
import com.org.crm.common.events.TaskEvents;
import com.org.crm.common.events.EventPublisher;
import com.org.crm.task.exception.GlobalExceptionHandler;
import com.org.crm.task.model.Task;
import com.org.crm.task.repository.TaskRepository;
import com.org.crm.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of TaskService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EventPublisher eventPublisher;

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        log.info("Creating new task with title: {}", request.title());

        if (taskRepository.existsByTitle(request.title())) {
            throw new GlobalExceptionHandler.TaskAlreadyExistsException("Task with title '" + request.title() + "' already exists");
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status() != null ? request.status() : Task.TaskStatus.PENDING)
                .priority(request.priority() != null ? request.priority() : Task.TaskPriority.MEDIUM)
                .dueDate(request.dueDate())
                .assignedTo(request.assignedTo())
                .relatedType(request.relatedType())
                .relatedId(request.relatedId())
                .build();

        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with ID: {}", savedTask.getId());

        // Publish task created event
        BaseEvent event = new TaskEvents.TaskCreatedEvent(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getType().toString(),
                savedTask.getPriority().toString(),
                savedTask.getAssignedTo(),
                savedTask.getCustomerId(),
                savedTask.getLeadId(),
                savedTask.getDueDate()
        );
        eventPublisher.publishTaskEvent(event);

        return TaskResponse.fromTask(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskResponse> getTaskById(Long id) {
        log.debug("Fetching task by ID: {}", id);
        return taskRepository.findById(id).map(TaskResponse::fromTask);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskResponse> getTaskByTitle(String title) {
        log.debug("Fetching task by title: {}", title);
        return taskRepository.findByTitle(title).map(TaskResponse::fromTask);
    }

    @Override
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        log.info("Updating task with ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.TaskNotFoundException("Task not found with ID: " + id));

        if (request.title() != null) task.setTitle(request.title());
        if (request.description() != null) task.setDescription(request.description());
        if (request.status() != null) task.setStatus(request.status());
        if (request.priority() != null) task.setPriority(request.priority());
        if (request.dueDate() != null) task.setDueDate(request.dueDate());
        if (request.assignedTo() != null) task.setAssignedTo(request.assignedTo());
        if (request.relatedType() != null) task.setRelatedType(request.relatedType());
        if (request.relatedId() != null) task.setRelatedId(request.relatedId());

        if (Task.TaskStatus.COMPLETED.equals(task.getStatus()) && task.getCompletedAt() == null) {
            task.setCompletedAt(LocalDateTime.now());
        }

        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully with ID: {}", updatedTask.getId());

        // Publish task updated event
        BaseEvent event = new TaskEvents.TaskUpdatedEvent(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getType().toString(),
                updatedTask.getPriority().toString(),
                updatedTask.getAssignedTo(),
                updatedTask.getCustomerId(),
                updatedTask.getLeadId(),
                updatedTask.getDueDate()
        );
        eventPublisher.publishTaskEvent(event);

        return TaskResponse.fromTask(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task with ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.TaskNotFoundException("Task not found with ID: " + id));

        // Publish task deleted event before deletion
        BaseEvent event = new TaskEvents.TaskDeletedEvent(
                task.getId(),
                task.getTitle(),
                task.getAssignedTo()
        );
        eventPublisher.publishTaskEvent(event);

        taskRepository.deleteById(id);
        log.info("Task deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getAllTasks(Pageable pageable) {
        log.debug("Fetching all tasks with pagination: {}", pageable);
        return taskRepository.findAll(pageable).map(TaskResponse::fromTask);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasksByStatus(Task.TaskStatus status, Pageable pageable) {
        log.debug("Fetching tasks by status: {}", status);
        return taskRepository.findByStatus(status, pageable).map(TaskResponse::fromTask);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasksByAssignedTo(String assignedTo, Pageable pageable) {
        log.debug("Fetching tasks assigned to: {}", assignedTo);
        return taskRepository.findByAssignedTo(assignedTo, pageable).map(TaskResponse::fromTask);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasksByPriority(Task.TaskPriority priority, Pageable pageable) {
        log.debug("Fetching tasks by priority: {}", priority);
        return taskRepository.findByPriority(priority, pageable).map(TaskResponse::fromTask);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByRelatedEntity(String relatedType, Long relatedId) {
        log.debug("Fetching tasks by related entity: {}-{}", relatedType, relatedId);
        return taskRepository.findByRelatedTypeAndRelatedId(relatedType, relatedId)
                .stream().map(TaskResponse::fromTask).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByDueDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching tasks by due date range: {} - {}", startDate, endDate);
        return taskRepository.findByDueDateBetween(startDate, endDate)
                .stream().map(TaskResponse::fromTask).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByCreatedAtRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching tasks by created at range: {} - {}", startDate, endDate);
        return taskRepository.findByCreatedAtBetween(startDate, endDate)
                .stream().map(TaskResponse::fromTask).toList();
    }

    @Override
    public TaskResponse updateTaskStatus(Long id, Task.TaskStatus status) {
        log.info("Updating task status to {} for task ID: {}", status, id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.TaskNotFoundException("Task not found with ID: " + id));
        task.setStatus(status);
        if (Task.TaskStatus.COMPLETED.equals(status)) {
            task.setCompletedAt(LocalDateTime.now());
        }
        Task updatedTask = taskRepository.save(task);

        // Publish task updated event
        BaseEvent event = new TaskEvents.TaskUpdatedEvent(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getType().toString(),
                updatedTask.getPriority().toString(),
                updatedTask.getAssignedTo(),
                updatedTask.getCustomerId(),
                updatedTask.getLeadId(),
                updatedTask.getDueDate()
        );
        eventPublisher.publishTaskEvent(event);

        return TaskResponse.fromTask(updatedTask);
    }

    @Override
    public TaskResponse updateTaskAssignment(Long id, String assignedTo) {
        log.info("Updating task assignment to {} for task ID: {}", assignedTo, id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.TaskNotFoundException("Task not found with ID: " + id));
        task.setAssignedTo(assignedTo);
        Task updatedTask = taskRepository.save(task);

        // Publish task updated event
        BaseEvent event = new TaskEvents.TaskUpdatedEvent(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getType().toString(),
                updatedTask.getPriority().toString(),
                updatedTask.getAssignedTo(),
                updatedTask.getCustomerId(),
                updatedTask.getLeadId(),
                updatedTask.getDueDate()
        );
        eventPublisher.publishTaskEvent(event);

        return TaskResponse.fromTask(updatedTask);
    }

    @Override
    public TaskResponse updateTaskPriority(Long id, Task.TaskPriority priority) {
        log.info("Updating task priority to {} for task ID: {}", priority, id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.TaskNotFoundException("Task not found with ID: " + id));
        task.setPriority(priority);
        Task updatedTask = taskRepository.save(task);

        // Publish task updated event
        BaseEvent event = new TaskEvents.TaskUpdatedEvent(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getType().toString(),
                updatedTask.getPriority().toString(),
                updatedTask.getAssignedTo(),
                updatedTask.getCustomerId(),
                updatedTask.getLeadId(),
                updatedTask.getDueDate()
        );
        eventPublisher.publishTaskEvent(event);

        return TaskResponse.fromTask(updatedTask);
    }

    @Override
    public TaskResponse updateTaskDueDate(Long id, LocalDateTime dueDate) {
        log.info("Updating task due date to {} for task ID: {}", dueDate, id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.TaskNotFoundException("Task not found with ID: " + id));
        task.setDueDate(dueDate);
        Task updatedTask = taskRepository.save(task);

        // Publish task updated event
        BaseEvent event = new TaskEvents.TaskUpdatedEvent(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getType().toString(),
                updatedTask.getPriority().toString(),
                updatedTask.getAssignedTo(),
                updatedTask.getCustomerId(),
                updatedTask.getLeadId(),
                updatedTask.getDueDate()
        );
        eventPublisher.publishTaskEvent(event);

        return TaskResponse.fromTask(updatedTask);
    }

    @Override
    public TaskResponse markTaskCompleted(Long id) {
        log.info("Marking task as completed for task ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.TaskNotFoundException("Task not found with ID: " + id));
        task.setStatus(Task.TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());
        Task updatedTask = taskRepository.save(task);

        // Publish task completed event
        BaseEvent event = new TaskEvents.TaskCompletedEvent(
                updatedTask.getId(),
                updatedTask.getAssignedTo(),
                updatedTask.getTitle(),
                updatedTask.getCompletedAt()
        );
        eventPublisher.publishTaskEvent(event);

        return TaskResponse.fromTask(updatedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskStatistics getTaskStatistics() {
        log.debug("Fetching task statistics");
        long totalTasks = taskRepository.count();
        long pendingTasks = taskRepository.countByStatus(Task.TaskStatus.PENDING);
        long inProgressTasks = taskRepository.countByStatus(Task.TaskStatus.IN_PROGRESS);
        long completedTasks = taskRepository.countByStatus(Task.TaskStatus.COMPLETED);
        long overdueTasks = taskRepository.countOverdueTasks(LocalDateTime.now());
        long cancelledTasks = taskRepository.countByStatus(Task.TaskStatus.CANCELLED);
        long lowPriorityTasks = taskRepository.countByPriority(Task.TaskPriority.LOW);
        long mediumPriorityTasks = taskRepository.countByPriority(Task.TaskPriority.MEDIUM);
        long highPriorityTasks = taskRepository.countByPriority(Task.TaskPriority.HIGH);
        long urgentPriorityTasks = taskRepository.countByPriority(Task.TaskPriority.URGENT);
        return new TaskStatistics(
                totalTasks,
                pendingTasks,
                inProgressTasks,
                completedTasks,
                overdueTasks,
                cancelledTasks,
                lowPriorityTasks,
                mediumPriorityTasks,
                highPriorityTasks,
                urgentPriorityTasks
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean titleExists(String title) {
        return taskRepository.existsByTitle(title);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTaskCountByStatus(Task.TaskStatus status) {
        return taskRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTaskCountByPriority(Task.TaskPriority priority) {
        return taskRepository.countByPriority(priority);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTaskCountByAssignedTo(String assignedTo) {
        return taskRepository.countByAssignedTo(assignedTo);
    }

    @Override
    @Transactional(readOnly = true)
    public long getOverdueTaskCount() {
        return taskRepository.countOverdueTasks(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public long getCompletedTaskCountInRange(LocalDateTime startDate, LocalDateTime endDate) {
        return taskRepository.countCompletedTasksInRange(startDate, endDate);
    }
}