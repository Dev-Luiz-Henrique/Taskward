package com.ilp506.taskward.controllers;

import android.content.Context;

import com.ilp506.taskward.data.enums.TaskEventStatusEnum;
import com.ilp506.taskward.data.models.Task;
import com.ilp506.taskward.data.models.TaskEvent;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.data.repositories.TaskEventRepository;
import com.ilp506.taskward.data.repositories.TaskRepository;
import com.ilp506.taskward.data.repositories.UserRepository;
import com.ilp506.taskward.exceptions.DatabaseOperationException;
import com.ilp506.taskward.exceptions.ExceptionHandler;
import com.ilp506.taskward.utils.OperationResponse;
import com.ilp506.taskward.utils.TaskScheduler;

import java.sql.Timestamp;
import java.util.List;

public class TaskEventController {
    private final TaskEventRepository taskEventRepository;
    private final TaskRepository taskRepository;
    private final UserRepository  userRepository;

    /**
     * Constructs a TaskEventController with a TaskEventRepository instance.
     *
     * @param context The application context used to initialize the TaskEventRepository.
     */
    public TaskEventController(Context context) {
        this.taskEventRepository = new TaskEventRepository(context);
        this.taskRepository = new TaskRepository(context);
        this.userRepository = new UserRepository(context);
    }

    /**
     * Creates a new task event and schedules the next event if applicable.
     *
     * @param taskEvent The TaskEvent instance to be created.
     * @return OperationResponse containing the created task event or failure message.
     */
    public OperationResponse<TaskEvent> createTaskEvent(TaskEvent taskEvent) {
        try {
            taskEvent.validate();

            TaskEvent createdTaskEvent = taskEventRepository.createTaskEvent(taskEvent);
            return OperationResponse.success("Task event created successfully", createdTaskEvent);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid task event data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while creating task event in the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while creating task event");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while creating task event");
        }
    }

    /**
     * Retrieves all task events.
     *
     * @return OperationResponse containing the list of task events or failure message.
     */
    public OperationResponse<List<TaskEvent>> getAllTaskEvents() {
        try {
            List<TaskEvent> taskEvents = taskEventRepository.getAllTaskEvents();
            if (taskEvents.isEmpty())
                return OperationResponse.failure("No task events found.");

            return OperationResponse.success("Task events retrieved successfully", taskEvents);
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving task events from the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving task events");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while retrieving task events");
        }
    }

    /**
     * Retrieves a task event by its ID.
     *
     * @param taskEventId The ID of the task event to retrieve.
     * @return OperationResponse containing the task event or failure message.
     */
    public OperationResponse<TaskEvent> getTaskEventById(int taskEventId) {
        try {
            TaskEvent taskEvent = taskEventRepository.getTaskEventById(taskEventId);
            if (taskEvent == null)
                return OperationResponse.failure("Task event not found");

            return OperationResponse.success("Task event retrieved successfully", taskEvent);
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving task event from the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving task event");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while retrieving task event");
        }
    }

    /**
     * Updates an existing task event.
     *
     * @param taskEvent The TaskEvent instance containing updated details.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<TaskEvent> updateTaskEvent(TaskEvent taskEvent) {
        try {
            TaskEvent existingTaskEvent = taskEventRepository.getTaskEventById(taskEvent.getId());
            if (existingTaskEvent == null)
                return OperationResponse.failure("Task event not found");

            taskEvent.validate();

            TaskEvent updatedTaskEvent = taskEventRepository.updateTaskEvent(taskEvent);
            return OperationResponse.success("Task event updated successfully", updatedTaskEvent);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid task event data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while updating task event in the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while updating task event");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while updating task event");
        }
    }

    /**
     * Deletes a task event by its ID.
     *
     * @param taskEventId The ID of the task event to delete.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Void> deleteTaskEvent(int taskEventId) {
        try {
            TaskEvent existingTaskEvent = taskEventRepository.getTaskEventById(taskEventId);
            if (existingTaskEvent == null)
                return OperationResponse.failure("Task event not found");

            taskEventRepository.deleteTaskEvent(taskEventId);

            return OperationResponse.success("Task event deleted successfully");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while deleting task event from the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while deleting task event");
        }
    }

    /**
     * Completes a TaskEvent and generates the next instance, if applicable.
     *
     * @param taskEventId The ID of the TaskEvent to complete.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Void> completeTaskEvent(int taskEventId) {
        try {
            TaskEvent event = taskEventRepository.getTaskEventById(taskEventId);
            if (event == null)
                return OperationResponse.failure("Task event not found");

            event.setStatus(TaskEventStatusEnum.COMPLETED);
            event.setCompletedDate(new Timestamp(System.currentTimeMillis()));
            taskEventRepository.updateTaskEvent(event);

            User user = userRepository.getUserById(event.getUserId());
            if (user != null) {
                user.setPoints(user.getPoints() + event.getPointsEarned());
                userRepository.updateUser(user);
            }

            Task task = taskRepository.getTaskById(event.getTaskId());
            if (task != null) {
                TaskEvent nextEvent = TaskScheduler.generateNextTaskEvent(task, event);
                if (nextEvent != null) taskEventRepository.createTaskEvent(nextEvent);
                // TODO implement logic for completed Task
            }

            return OperationResponse.success("Task event completed successfully");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while completing task event in the database");
        } catch (RuntimeException e){
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while completing task event");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while completing task event");
        }
    }

    /**
     * Checks and processes expired TaskEvents, generating the next instances if necessary.
     *
     * @return OperationResponse indicating the result of the operation.
     */
    public OperationResponse<Void> checkAndGenerateExpiredEvents() {
        try {
            List<TaskEvent> pendingEvents = taskEventRepository.getAllTaskEvents();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (TaskEvent event : pendingEvents) {
                if (event.getScheduledDate().before(now) && event.getStatus() == TaskEventStatusEnum.SCHEDULED) {

                    event.setStatus(TaskEventStatusEnum.EXPIRED);
                    taskEventRepository.updateTaskEvent(event);

                    Task task = taskRepository.getTaskById(event.getTaskId());
                    if (task != null) {
                        TaskEvent nextEvent = TaskScheduler.generateNextTaskEvent(task, event);
                        if (nextEvent != null) taskEventRepository.createTaskEvent(nextEvent);
                        // TODO implement logic for completed Task
                    }
                }
            }

            return OperationResponse.success("Expired events processed successfully");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while processing expired task events");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while processing expired task events");
        }
    }
}
