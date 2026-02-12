package com.mediaplanet.worker;

import com.mediaplanet.entity.Channel;
import com.mediaplanet.repository.ChannelRepository;
import com.mediaplanet.repository.TaskRepository;
import com.mediaplanet.service.TaskExecutionService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class WorkerManager {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskExecutionService taskExecutionService;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    private final Map<Long, ScheduledFuture<?>> activeWorkers = new HashMap<>();

    /**
     * Periodically syncs workers with active channels.
     * Runs every 30 seconds to pick up new channels or stop workers for inactive
     * ones.
     */
    @PostConstruct
    public void init() {
        // Run sync initially and then every 30 seconds
        taskScheduler.scheduleWithFixedDelay(this::syncWorkers, Duration.ofSeconds(30));
    }

    public void syncWorkers() {
        log.debug("Syncing channel workers...");
        List<Channel> channels = channelRepository.findAll();

        for (Channel channel : channels) {
            boolean isActive = Boolean.TRUE.equals(channel.getStatus());
            Long channelId = channel.getId();

            if (isActive && !activeWorkers.containsKey(channelId)) {
                startWorker(channel);
            } else if (!isActive && activeWorkers.containsKey(channelId)) {
                stopWorker(channelId);
            }
        }

        // Clean up workers for channels that might have been deleted from DB
        activeWorkers.keySet().removeIf(id -> {
            boolean exists = channels.stream().anyMatch(c -> c.getId().equals(id));
            if (!exists) {
                log.info("Stopping worker for deleted channel ID: {}", id);
                return true;
            }
            return false;
        });
    }

    private void startWorker(Channel channel) {
        log.info("Starting worker for channel: {}", channel.getChannelName());
        TaskWorker worker = new TaskWorker(
                channel.getId(),
                channel.getChannelName(),
                taskRepository,
                taskExecutionService);

        // Schedule the worker to run every 10 seconds (fixed delay)
        ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(worker, Duration.ofSeconds(10));
        activeWorkers.put(channel.getId(), future);
    }

    private void stopWorker(Long channelId) {
        log.info("Stopping worker for channel ID: {}", channelId);
        ScheduledFuture<?> future = activeWorkers.remove(channelId);
        if (future != null) {
            future.cancel(false);
        }
    }
}
