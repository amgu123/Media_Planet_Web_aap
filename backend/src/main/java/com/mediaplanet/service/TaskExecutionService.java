package com.mediaplanet.service;

import com.mediaplanet.entity.Channel;
import com.mediaplanet.entity.TaskExecution;
import com.mediaplanet.repository.ChannelRepository;
import com.mediaplanet.repository.TaskExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskExecutionService {

    @Autowired
    private TaskExecutionRepository taskExecutionRepository;

    @Autowired
    private ChannelRepository channelRepository;

    public List<TaskExecution> getAllTaskExecutions() {
        return taskExecutionRepository.findAll();
    }

    public List<TaskExecution> getTaskExecutionsByChannel(Long channelId) {
        return taskExecutionRepository.findByChannelId(channelId);
    }

    public TaskExecution startTask(Long channelId, String taskType) {
        if (channelId == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        Optional<TaskExecution> existingTask = taskExecutionRepository.findByChannelIdAndTaskType(channelId, taskType);

        TaskExecution task;
        if (existingTask.isPresent()) {
            task = existingTask.get();
        } else {
            task = new TaskExecution();
            task.setChannel(channel);
            task.setTaskType(taskType);
        }

        task.setStatus("RUNNING");
        task.setStartTime(LocalDateTime.now());
        task.setStopTime(null);

        return taskExecutionRepository.save(task);
    }

    public TaskExecution stopTask(Long channelId, String taskType) {
        if (channelId == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }
        TaskExecution task = taskExecutionRepository.findByChannelIdAndTaskType(channelId, taskType)
                .orElseThrow(() -> new RuntimeException("Task not found for this channel"));

        task.setStatus("STOPPED");
        task.setStopTime(LocalDateTime.now());

        return taskExecutionRepository.save(task);
    }

    public String getTaskLogs(Long channelId, String taskType) {
        // Mocking logs for now
        return String.format("[%s] INFO: Service starting for channel %d...\n" +
                "[%s] DEBUG: Fetching source path...\n" +
                "[%s] INFO: Analyzing stream for %s...\n" +
                "[%s] INFO: Processing frames...\n",
                LocalDateTime.now().minusMinutes(5), channelId,
                LocalDateTime.now().minusMinutes(4),
                LocalDateTime.now().minusMinutes(3), taskType,
                LocalDateTime.now().minusMinutes(2));
    }
}
