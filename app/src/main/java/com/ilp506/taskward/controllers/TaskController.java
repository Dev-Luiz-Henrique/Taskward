package com.ilp506.taskward.controllers;

import android.content.Context;

import com.ilp506.taskward.data.models.Task;
import com.ilp506.taskward.data.models.TaskEvent;
import com.ilp506.taskward.data.repositories.TaskEventRepository;
import com.ilp506.taskward.data.repositories.TaskRepository;
import com.ilp506.taskward.exceptions.ExceptionHandler;
import com.ilp506.taskward.utils.OperationResponse;
import com.ilp506.taskward.utils.TaskScheduler;

import java.util.List;

/**
 * Controller class responsible for managing Task operations.
 * This class interacts with the TaskRepository to perform database operations
 * while handling errors and returning structured responses.
 */
public class TaskController {
    private final ExceptionHandler exceptionHandler;
    private final TaskRepository taskRepository;
    private final TaskEventRepository taskEventRepository;

    /**
     * Constructs a TaskController with a TaskRepository and TaskEventRepository instance.
     *
     * @param context The application context used to initialize the repositories.
     */
    public TaskController(Context context) {
        this.exceptionHandler = ExceptionHandler.getInstance();
        this.taskRepository = new TaskRepository(context);
        this.taskEventRepository = new TaskEventRepository(context);
    }

    /**
     * Validates the task ID to ensure it is greater than 0.
     *
     * @param taskId The task ID to be validated.
     * @throws IllegalArgumentException If the task ID is less than or equal to 0.
     */
    private void validateTaskId(int taskId) {
        if (taskId <= 0) throw new IllegalArgumentException("Task ID must be greater than 0.");
    }

    /**
     * Creates a new task and its first event if applicable.
     *
     * @param task The task to be created.
     * @return OperationResponse containing the created task or failure message.
     */
    public OperationResponse<Task> createTask(Task task) {
        try {
            task.validate();
            Task createdTask = taskRepository.createTask(task);

            TaskEvent firstEvent = TaskScheduler.generateNextTaskEvent(createdTask, null);
            if (firstEvent != null)
                taskEventRepository.createTaskEvent(firstEvent);

            return OperationResponse.success("Task and initial event created successfully", createdTask);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to create task.");
        }
    }

    /**
     * Retrieves all tasks.
     *
     * @return OperationResponse containing the list of tasks or failure message.
     */
    public OperationResponse<List<Task>> getAllTasks() {
        try {
            List<Task> tasks = taskRepository.getAllTasks();
            if (tasks.isEmpty())
                return OperationResponse.failure("No tasks found.");

            return OperationResponse.success("Tasks retrieved successfully", tasks);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to retrieve tasks.");
        }
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return OperationResponse containing the task or failure message.
     */
    public OperationResponse<Task> getTaskById(int taskId) {
        try {
            validateTaskId(taskId);
            Task task = taskRepository.getTaskById(taskId);
            if (task == null)
                return OperationResponse.failure("Task not found.");

            return OperationResponse.success("Task retrieved successfully", task);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to retrieve task.");
        }
    }

    /**
     * Updates an existing task.
     *
     * @param task Task object containing updated details.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Task> updateTask(Task task) {
        try {
            validateTaskId(task.getId());
            task.validate();

            Task existingTask = taskRepository.getTaskById(task.getId());
            if (existingTask == null)
                return OperationResponse.failure("Task not found.");

            Task updatedTask = taskRepository.updateTask(task);
            return OperationResponse.success("Task updated successfully", updatedTask);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to update task.");
        }
    }

    /**
     * Deletes a task by its ID.
     *
     * @param taskId The ID of the task to delete.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Void> deleteTask(int taskId) {
        try {
            validateTaskId(taskId);

            Task existingTask = taskRepository.getTaskById(taskId);
            if (existingTask == null)
                return OperationResponse.failure("Task not found.");

            taskRepository.deleteTask(taskId);
            return OperationResponse.success("Task deleted successfully.");
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to delete task.");
        }
    }

    /**
     * Retrieves all tasks with their associated task events.
     *
     * @return OperationResponse containing the list of tasks with associated task events or failure message.
     */
    public OperationResponse<List<Task>> getAllTasksWithTaskEvents() {
        try {
            List<Task> tasks = taskRepository.getAllTasks();
            if (tasks.isEmpty())
                return OperationResponse.failure("No tasks found.");

            for (Task task : tasks) {
                List<TaskEvent> taskEvents = taskEventRepository.getAllTaskEventsByTaskId(task.getId());
                task.setTaskEvents(taskEvents);
            }

            return OperationResponse.success("Tasks with events retrieved successfully", tasks);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to retrieve tasks with events.");
        }
    }

    /**
     * Retrieves a task by its ID along with its associated task events.
     *
     * @param taskId The ID of the task to retrieve.
     * @return OperationResponse containing the task with associated task events or failure message.
     */
    public OperationResponse<Task> getTaskByIdWithTaskEvents(int taskId) {
        try {
            validateTaskId(taskId);

            Task task = taskRepository.getTaskById(taskId);
            if (task == null)
                return OperationResponse.failure("Task not found.");

            List<TaskEvent> taskEvents = taskEventRepository.getAllTaskEventsByTaskId(taskId);
            task.setTaskEvents(taskEvents);

            return OperationResponse.success("Task with events retrieved successfully.", task);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to retrieve task with events.");
        }
    }
}
