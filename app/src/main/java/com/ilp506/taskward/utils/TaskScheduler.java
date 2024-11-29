package com.ilp506.taskward.utils;

import com.ilp506.taskward.data.enums.TaskEventStatusEnum;
import com.ilp506.taskward.data.models.Task;
import com.ilp506.taskward.data.models.TaskEvent;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class TaskScheduler {

    /**
     * Generates the next scheduled task event for a given task and its last event.
     *
     * @param task      The task to generate the next event for.
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

        Calendar calendar = Calendar.getInstance();
        if (lastEvent != null)
            calendar.setTime(lastEvent.getScheduledDate());
        else
            calendar.setTime(task.getStartDate());

        switch (task.getFrequency()) {
            case DAILY:
                calendar.add(Calendar.DAY_OF_YEAR, task.getFrequencyInterval());
                break;
            case WEEKLY:
                calendar.add(Calendar.WEEK_OF_YEAR, task.getFrequencyInterval());
                break;
            case MONTHLY:
                calendar.add(Calendar.MONTH, task.getFrequencyInterval());
                break;
            case YEARLY:
                calendar.add(Calendar.YEAR, task.getFrequencyInterval());
                break;
            default:
                throw new IllegalArgumentException("Unsupported frequency: " + task.getFrequency());
        }

        Date nextDate = calendar.getTime();
        if (task.getEndDate() != null && nextDate.after(task.getEndDate()))
            return null;

        TaskEvent newEvent = new TaskEvent();
        newEvent.setTaskId(task.getId());
        newEvent.setScheduledDate(new Timestamp(nextDate.getTime()));
        newEvent.setStatus(TaskEventStatusEnum.SCHEDULED);
        return newEvent;
    }
}
