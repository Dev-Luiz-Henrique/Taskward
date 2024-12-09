package com.ilp506.taskward.controllers;

import android.content.Context;

import com.ilp506.taskward.data.models.Task;
import com.ilp506.taskward.data.models.TaskEvent;
import com.ilp506.taskward.data.repositories.TaskEventRepository;
import com.ilp506.taskward.data.repositories.TaskRepository;
import com.ilp506.taskward.exceptions.DatabaseOperationException;
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
    private final TaskRepository taskRepository;
    private final TaskEventRepository taskEventRepository;

    /**
     * Constructs a TaskController with a TaskRepository instance.
     *
     * @param context The application context used to initialize the TaskRepository.
     */
    public TaskController(Context context) {
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
     * Creates a new task.
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
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid task data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while creating task in the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while creating task");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while creating task");
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
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving tasks from the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving tasks");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while retrieving tasks");
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
                return OperationResponse.failure("Task not found");

            return OperationResponse.success("Task retrieved successfully", task);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid task ID provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving task from the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving task");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while retrieving task");
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
            Task existingTask = taskRepository.getTaskById(task.getId());
            if (existingTask == null)
                return OperationResponse.failure("Task not found");

            task.validate();

            Task updatedTask = taskRepository.updateTask(task);
            return OperationResponse.success("Task updated successfully", updatedTask);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid task data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while updating task in the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while updating task");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while updating task");
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
                return OperationResponse.failure("Task not found");

            taskRepository.deleteTask(taskId);

            return OperationResponse.success("Task deleted successfully");
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid task ID provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while deleting task from the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while deleting task");
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
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving tasks with events from the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving tasks with events");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while retrieving tasks with events");
        }
    }

    /**
     * Retrieves a task by its ID and its associated task events.
     *
     * @param taskId The ID of the task to retrieve.
     * @return OperationResponse containing the task with associated task events or failure message.
     */
    public OperationResponse<Task> getTaskByIdWithTaskEvents(int taskId) {
        try {
            validateTaskId(taskId);
            Task task = taskRepository.getTaskById(taskId);
            if (task == null)
                return OperationResponse.failure("Task not found");

            List<TaskEvent> taskEvents = taskEventRepository.getAllTaskEventsByTaskId(taskId);
            task.setTaskEvents(taskEvents);

            return OperationResponse.success("Task with events retrieved successfully", task);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid task ID provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving task with events from the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving task with events");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while retrieving task with events");
        }
    }
}
