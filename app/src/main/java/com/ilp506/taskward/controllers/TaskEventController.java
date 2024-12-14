package com.ilp506.taskward.controllers;

import android.content.Context;

import com.ilp506.taskward.data.enums.TaskEventStatusEnum;
import com.ilp506.taskward.data.models.Task;
import com.ilp506.taskward.data.models.TaskEvent;
import com.ilp506.taskward.data.repositories.TaskEventRepository;
import com.ilp506.taskward.data.repositories.TaskRepository;
import com.ilp506.taskward.exceptions.handlers.ExceptionHandler;
import com.ilp506.taskward.services.PointService;
import com.ilp506.taskward.utils.OperationResponse;
import com.ilp506.taskward.utils.TaskScheduler;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class responsible for managing TaskEvent operations.
 * This class interacts with the TaskEventRepository to perform database operations
 * while handling errors and returning structured responses.
 */
public class TaskEventController {
    private final ExceptionHandler exceptionHandler;
    private final TaskEventRepository taskEventRepository;
    private final TaskRepository taskRepository;
    private final PointService pointService;

    /**
     * Constructs a TaskEventController with repository instances.
     *
     * @param context The application context used to initialize the repositories.
     */
    public TaskEventController(Context context) {
        this.exceptionHandler = ExceptionHandler.getInstance();
        this.taskEventRepository = new TaskEventRepository(context);
        this.taskRepository = new TaskRepository(context);
        this.pointService = new PointService(context);
    }

    /**
     * Creates a new TaskEvent and returns the created instance.
     *
     * @param taskEvent The TaskEvent instance to be created.
     * @return OperationResponse containing the created TaskEvent or failure message.
     */
    public OperationResponse<TaskEvent> createTaskEvent(TaskEvent taskEvent) {
        try {
            taskEvent.validate();
            TaskEvent createdTaskEvent = taskEventRepository.createTaskEvent(taskEvent);

            return OperationResponse.success("Task event created successfully", createdTaskEvent);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to create task event.");
        }
    }

    /**
     * Retrieves all TaskEvents.
     *
     * @return OperationResponse containing the list of TaskEvents or failure message.
     */
    public OperationResponse<List<TaskEvent>> getAllTaskEvents() {
        try {
            List<TaskEvent> taskEvents = taskEventRepository.getAllTaskEvents();
            if (taskEvents.isEmpty())
                return OperationResponse.failure("No task events found.");

            return OperationResponse.success("Task events retrieved successfully", taskEvents);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to retrieve task events.");
        }
    }

    /**
     * Retrieves a TaskEvent by its ID.
     *
     * @param taskEventId The ID of the TaskEvent to retrieve.
     * @return OperationResponse containing the TaskEvent or failure message.
     */
    public OperationResponse<TaskEvent> getTaskEventById(int taskEventId) {
        try {
            TaskEvent taskEvent = taskEventRepository.getTaskEventById(taskEventId);
            if (taskEvent == null)
                return OperationResponse.failure("Task event not found.");

            return OperationResponse.success("Task event retrieved successfully", taskEvent);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to retrieve task event.");
        }
    }

    /**
     * Updates an existing TaskEvent.
     *
     * @param taskEvent The TaskEvent instance containing updated details.
     * @return OperationResponse containing the updated TaskEvent or failure message.
     */
    public OperationResponse<TaskEvent> updateTaskEvent(TaskEvent taskEvent) {
        try {
            taskEvent.validate();

            TaskEvent existingTaskEvent = taskEventRepository.getTaskEventById(taskEvent.getId());
            if (existingTaskEvent == null)
                return OperationResponse.failure("Task event not found.");

            TaskEvent updatedTaskEvent = taskEventRepository.updateTaskEvent(taskEvent);
            return OperationResponse.success("Task event updated successfully", updatedTaskEvent);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to update task event.");
        }
    }

    /**
     * Deletes a TaskEvent by its ID.
     *
     * @param taskEventId The ID of the TaskEvent to delete.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Void> deleteTaskEvent(int taskEventId) {
        try {
            TaskEvent existingTaskEvent = taskEventRepository.getTaskEventById(taskEventId);
            if (existingTaskEvent == null)
                return OperationResponse.failure("Task event not found.");

            taskEventRepository.deleteTaskEvent(taskEventId);
            return OperationResponse.success("Task event deleted successfully.");
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to delete task event.");
        }
    }

    /**
     * Completes a TaskEvent and schedules the next instance if applicable.
     *
     * @param taskEventId The ID of the TaskEvent to complete.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Void> completeTaskEvent(int taskEventId) {
        try {
            TaskEvent event = taskEventRepository.getTaskEventById(taskEventId);
            if (event == null)
                return OperationResponse.failure("Task event not found.");

            if (event.getStatus() != TaskEventStatusEnum.SCHEDULED)
                return OperationResponse.failure("Task event is not scheduled.");

            event.setStatus(TaskEventStatusEnum.COMPLETED);
            event.setCompletedDate(LocalDateTime.now());
            taskEventRepository.updateTaskEvent(event);

            boolean pointsUpdated = pointService.addPoints(event.getUserId(), event.getPointsEarned());
            if (!pointsUpdated)
                return OperationResponse.failure("Failed to update user points.");

            Task task = taskRepository.getTaskById(event.getTaskId());
            if (task != null) {
                TaskEvent nextEvent = TaskScheduler.generateNextTaskEvent(task, event);
                if (nextEvent != null) taskEventRepository.createTaskEvent(nextEvent);
                // TODO implement logic for completed Task
            }

            return OperationResponse.success("Task event completed successfully.");
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to complete task event.");
        }
    }

    /**
     * Reverts the completion of a TaskEvent.
     *
     * @param taskEventId The ID of the TaskEvent to revert.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Void> revertTaskEventCompletion(int taskEventId) {
        try {
            TaskEvent event = taskEventRepository.getTaskEventById(taskEventId);
            if (event == null)
                return OperationResponse.failure("Task event not found.");

            if (event.getStatus() != TaskEventStatusEnum.COMPLETED)
                return OperationResponse.failure("Task event is not marked as completed.");

            event.setStatus(TaskEventStatusEnum.SCHEDULED);
            event.setCompletedDate(null);
            taskEventRepository.updateTaskEvent(event);

            boolean pointsUpdated = pointService.deductPoints(event.getUserId(), event.getPointsEarned());
            if (!pointsUpdated)
                return OperationResponse.failure("Failed to update user points.");

            Task task = taskRepository.getTaskById(event.getTaskId());
            if (task != null) {
                TaskEvent nextEvent = taskEventRepository.getNextTaskEvent(event);
                if (nextEvent != null && nextEvent.getStatus() == TaskEventStatusEnum.SCHEDULED) {
                    taskEventRepository.deleteTaskEvent(nextEvent.getId());
                }
            }

            return OperationResponse.success("Task event completion reverted successfully.");
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to revert task event completion.");
        }
    }

    /**
     * Processes expired TaskEvents and generates the next instances if applicable.
     *
     * @return OperationResponse indicating the result of the operation.
     */
    public OperationResponse<Void> checkAndGenerateExpiredEvents() {
        try {
            List<TaskEvent> pendingEvents = taskEventRepository.getAllTaskEvents();
            LocalDateTime now = LocalDateTime.now();

            for (TaskEvent event : pendingEvents) {
                if (event.getScheduledDate().isBefore(now) && event.getStatus() == TaskEventStatusEnum.SCHEDULED) {
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

            return OperationResponse.success("Expired events processed successfully.");
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to process expired task events.");
        }
    }
}
