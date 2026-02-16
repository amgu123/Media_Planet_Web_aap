package com.mediaplanet.scheduler;

import com.mediaplanet.entity.Channel;
import com.mediaplanet.entity.Task;
import com.mediaplanet.repository.ChannelRepository;
import com.mediaplanet.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DailyTaskScheduler {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChannelRepository channelRepository;

    /**
     * Runs every day at 02:00 AM
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void generateDailyTasks() {
        log.info("Starting scheduled task generation for current day...");

        LocalDate today = LocalDate.now();
        List<Channel> channels = channelRepository.findAll();

        List<Task> tasksToSave = new ArrayList<>();

        for (Channel channel : channels) {
            if (channel.getStatus() != null && !channel.getStatus()) {
                continue; // Skip inactive channels
            }

            log.info("Generating tasks for channel: {}", channel.getChannelName());

            if (Boolean.TRUE.equals(channel.getAdDetection())) {
                generateTasksForType(channel, today, "AD", tasksToSave);
            }
            if (Boolean.TRUE.equals(channel.getNewsDetection())) {
                generateTasksForType(channel, today, "NEWS", tasksToSave);
            }
            if (Boolean.TRUE.equals(channel.getOcr())) {
                generateTasksForType(channel, today, "OCR", tasksToSave);
            }
        }

        if (!tasksToSave.isEmpty()) {
            taskRepository.saveAll(tasksToSave);
            log.info("Successfully generated and saved {} tasks for {}", tasksToSave.size(), today);
        } else {
            log.warn("No active channels or detections found. No tasks generated.");
        }
    }

    private void generateTasksForType(Channel channel, LocalDate date, String type, List<Task> tasksToSave) {
        log.info("Generating 24 tasks for channel: {} type: {}", channel.getChannelName(), type);
        for (int hour = 9; hour < 18; hour++) {
            String dateTimeStr = String.format("%02d00", hour);

            Task task = new Task();
            task.setChannel(channel);
            task.setTaskType(type);
            task.setDataDate(date);
            task.setDateTime(dateTimeStr);
            task.setStatus("PENDING");

            tasksToSave.add(task);
        }
    }
}
