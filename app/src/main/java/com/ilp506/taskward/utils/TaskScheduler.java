package com.ilp506.taskward.utils;

import com.ilp506.taskward.data.enums.TaskEventStatusEnum;
import com.ilp506.taskward.data.models.Task;
import com.ilp506.taskward.data.models.TaskEvent;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for generating task event schedules.
 */
public class TaskScheduler {

    /**
     * Generates the next scheduled task event for a given task and its last event.
     *
     * @param task The task to generate the next event for.
     * @param lastEvent The last scheduled event (can be null if no previous event exists).
     * @return TaskEvent The next scheduled task event or null if the task's end date is exceeded.
     */
    public static TaskEvent generateNextTaskEvent(Task task, TaskEvent lastEvent) {
        if (task == null)
            throw new IllegalArgumentException("Task cannot be null");
        if (task.getStartDate() == null)
            throw new IllegalArgumentException("Task must have a start date");
        if (task.getFrequency() == null)
            throw new IllegalArgumentException("Task must have a frequency");

        LocalDateTime lastScheduledDate = lastEvent != null ? lastEvent.getScheduledDate() : task.getStartDate();
        LocalDateTime nextScheduledDate;

        switch (task.getFrequency()) {
            case DAILY:
                nextScheduledDate = lastScheduledDate.plusDays(task.getFrequencyInterval());
                break;
            case WEEKLY:
                nextScheduledDate = lastScheduledDate.plusWeeks(task.getFrequencyInterval());
                break;
            case MONTHLY:
                nextScheduledDate = lastScheduledDate.plusMonths(task.getFrequencyInterval());
                break;
            case YEARLY:
                nextScheduledDate = lastScheduledDate.plusYears(task.getFrequencyInterval());
                break;
            default:
                throw new IllegalArgumentException("Unsupported frequency: " + task.getFrequency());
        }

        if (task.getEndDate() != null && nextScheduledDate.isAfter(task.getEndDate()))
            return null;

        TaskEvent newEvent = new TaskEvent();
        newEvent.setTaskId(task.getId());
        newEvent.setUserId(1); // TODO Replace with actual user ID
        newEvent.setPointsEarned(task.getPointsReward());
        newEvent.setScheduledDate(nextScheduledDate);
        newEvent.setStatus(TaskEventStatusEnum.SCHEDULED);

        return newEvent;
    }
}
