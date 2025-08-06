package com.org.crm.task.controller;

import com.org.crm.task.model.Task;
import com.org.crm.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Task operations
 */
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task Management", description = "APIs for managing tasks and activities")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task with the provided information")
    public ResponseEntity<TaskService.TaskResponse> createTask(
            @Valid @RequestBody TaskService.CreateTaskRequest request) {
        log.info("Creating new task with title: {}", request.title());
        TaskService.TaskResponse response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieves a task by its ID")
    public ResponseEntity<TaskService.TaskResponse> getTaskById(
            @Parameter(description = "Task ID") @PathVariable Long id) {
        log.debug("Fetching task by ID: {}", id);
        Optional<TaskService.TaskResponse> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/title/{title}")
    @Operation(summary = "Get task by title", description = "Retrieves a task by its title")
    public ResponseEntity<TaskService.TaskResponse> getTaskByTitle(
            @Parameter(description = "Task title") @PathVariable String title) {
        log.debug("Fetching task by title: {}", title);
        Optional<TaskService.TaskResponse> task = taskService.getTaskByTitle(title);
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Updates an existing task's information")
    public ResponseEntity<TaskService.TaskResponse> updateTask(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @Valid @RequestBody TaskService.UpdateTaskRequest request) {
        log.info("Updating task with ID: {}", id);
        try {
            TaskService.TaskResponse response = taskService.updateTask(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating task: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Deletes a task by its ID")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Task ID") @PathVariable Long id) {
        log.info("Deleting task with ID: {}", id);
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting task: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieves all tasks with pagination and sorting")
    public ResponseEntity<Page<TaskService.TaskResponse>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.debug("Fetching all tasks with pagination: page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TaskService.TaskResponse> tasks = taskService.getAllTasks(pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get tasks by status", description = "Retrieves tasks filtered by status")
    public ResponseEntity<Page<TaskService.TaskResponse>> getTasksByStatus(
            @Parameter(description = "Task status") @PathVariable Task.TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching tasks by status: {}", status);
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskService.TaskResponse> tasks = taskService.getTasksByStatus(status, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/assigned/{assignedTo}")
    @Operation(summary = "Get tasks by assigned user", description = "Retrieves tasks assigned to a specific user")
    public ResponseEntity<Page<TaskService.TaskResponse>> getTasksByAssignedTo(
            @Parameter(description = "Assigned user") @PathVariable String assignedTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching tasks assigned to: {}", assignedTo);
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskService.TaskResponse> tasks = taskService.getTasksByAssignedTo(assignedTo, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get tasks by priority", description = "Retrieves tasks by priority")
    public ResponseEntity<Page<TaskService.TaskResponse>> getTasksByPriority(
            @Parameter(description = "Task priority") @PathVariable Task.TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching tasks by priority: {}", priority);
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskService.TaskResponse> tasks = taskService.getTasksByPriority(priority, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/related")
    @Operation(summary = "Get tasks by related entity", description = "Retrieves tasks by related entity type and ID")
    public ResponseEntity<List<TaskService.TaskResponse>> getTasksByRelatedEntity(
            @RequestParam String relatedType,
            @RequestParam Long relatedId) {
        log.debug("Fetching tasks by related entity: {}-{}", relatedType, relatedId);
        List<TaskService.TaskResponse> tasks = taskService.getTasksByRelatedEntity(relatedType, relatedId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/due-date-range")
    @Operation(summary = "Get tasks by due date range", description = "Retrieves tasks with due dates in a given range")
    public ResponseEntity<List<TaskService.TaskResponse>> getTasksByDueDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        log.debug("Fetching tasks by due date range: {} - {}", startDate, endDate);
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<TaskService.TaskResponse> tasks = taskService.getTasksByDueDateRange(start, end);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/created-at-range")
    @Operation(summary = "Get tasks by created at range", description = "Retrieves tasks created in a given range")
    public ResponseEntity<List<TaskService.TaskResponse>> getTasksByCreatedAtRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        log.debug("Fetching tasks by created at range: {} - {}", startDate, endDate);
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<TaskService.TaskResponse> tasks = taskService.getTasksByCreatedAtRange(start, end);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update task status", description = "Updates the status of a task")
    public ResponseEntity<TaskService.TaskResponse> updateTaskStatus(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @Parameter(description = "New status") @RequestParam Task.TaskStatus status) {
        log.info("Updating task status to {} for task ID: {}", status, id);
        try {
            TaskService.TaskResponse response = taskService.updateTaskStatus(id, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating task status: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/assignment")
    @Operation(summary = "Update task assignment", description = "Updates the assignment of a task to a user")
    public ResponseEntity<TaskService.TaskResponse> updateTaskAssignment(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @Parameter(description = "Assigned user") @RequestParam String assignedTo) {
        log.info("Updating task assignment to {} for task ID: {}", assignedTo, id);
        try {
            TaskService.TaskResponse response = taskService.updateTaskAssignment(id, assignedTo);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating task assignment: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/priority")
    @Operation(summary = "Update task priority", description = "Updates the priority of a task")
    public ResponseEntity<TaskService.TaskResponse> updateTaskPriority(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @Parameter(description = "Priority") @RequestParam Task.TaskPriority priority) {
        log.info("Updating task priority to {} for task ID: {}", priority, id);
        try {
            TaskService.TaskResponse response = taskService.updateTaskPriority(id, priority);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating task priority: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/due-date")
    @Operation(summary = "Update task due date", description = "Updates the due date of a task")
    public ResponseEntity<TaskService.TaskResponse> updateTaskDueDate(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @RequestParam String dueDate) {
        log.info("Updating task due date for task ID: {}", id);
        try {
            LocalDateTime date = LocalDateTime.parse(dueDate);
            TaskService.TaskResponse response = taskService.updateTaskDueDate(id, date);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating task due date: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Mark task as completed", description = "Marks a task as completed")
    public ResponseEntity<TaskService.TaskResponse> markTaskCompleted(
            @Parameter(description = "Task ID") @PathVariable Long id) {
        log.info("Marking task as completed for task ID: {}", id);
        try {
            TaskService.TaskResponse response = taskService.markTaskCompleted(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error marking task as completed: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get task statistics", description = "Retrieves comprehensive task statistics")
    public ResponseEntity<TaskService.TaskStatistics> getTaskStatistics() {
        log.debug("Fetching task statistics");
        TaskService.TaskStatistics statistics = taskService.getTaskStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/check-title/{title}")
    @Operation(summary = "Check if title exists", description = "Checks if a task with the given title exists")
    public ResponseEntity<Boolean> checkTitleExists(
            @Parameter(description = "Task title") @PathVariable String title) {
        log.debug("Checking if title exists: {}", title);
        boolean exists = taskService.titleExists(title);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Get task count by status", description = "Retrieves the count of tasks by status")
    public ResponseEntity<Long> getTaskCountByStatus(
            @Parameter(description = "Task status") @PathVariable Task.TaskStatus status) {
        log.debug("Fetching task count by status: {}", status);
        long count = taskService.getTaskCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/priority/{priority}")
    @Operation(summary = "Get task count by priority", description = "Retrieves the count of tasks by priority")
    public ResponseEntity<Long> getTaskCountByPriority(
            @Parameter(description = "Task priority") @PathVariable Task.TaskPriority priority) {
        log.debug("Fetching task count by priority: {}", priority);
        long count = taskService.getTaskCountByPriority(priority);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/assigned/{assignedTo}")
    @Operation(summary = "Get task count by assigned user", description = "Retrieves the count of tasks by assigned user")
    public ResponseEntity<Long> getTaskCountByAssignedTo(
            @Parameter(description = "Assigned user") @PathVariable String assignedTo) {
        log.debug("Fetching task count by assigned user: {}", assignedTo);
        long count = taskService.getTaskCountByAssignedTo(assignedTo);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/overdue")
    @Operation(summary = "Get overdue task count", description = "Retrieves the count of overdue tasks")
    public ResponseEntity<Long> getOverdueTaskCount() {
        log.debug("Fetching overdue task count");
        long count = taskService.getOverdueTaskCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/completed-in-range")
    @Operation(summary = "Get completed task count in range", description = "Retrieves the count of completed tasks in a date range")
    public ResponseEntity<Long> getCompletedTaskCountInRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        log.debug("Fetching completed task count in range: {} - {}", startDate, endDate);
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        long count = taskService.getCompletedTaskCountInRange(start, end);
        return ResponseEntity.ok(count);
    }
}