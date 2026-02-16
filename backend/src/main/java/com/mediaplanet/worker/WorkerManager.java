package com.mediaplanet.worker;

import com.mediaplanet.entity.Channel;
import com.mediaplanet.repository.ChannelRepository;
import com.mediaplanet.repository.GeneratedContentRepository;
import com.mediaplanet.repository.TranscriptRepository;
import com.mediaplanet.repository.TaskRepository;
import com.mediaplanet.service.AppConfigService;
import com.mediaplanet.service.TaskExecutionService;
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
    private GeneratedContentRepository generatedContentRepository;

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Autowired
    private TaskExecutionService taskExecutionService;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private org.springframework.web.client.RestTemplate restTemplate;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    private final Map<String, ScheduledFuture<?>> activeWorkers = new HashMap<>();

    public void syncWorkers() {
        log.debug("Syncing channel workers...");
        List<Channel> channels = channelRepository.findAll();

        for (Channel channel : channels) {

            syncWorkerType(channel, "AD", channel.getAdWorkerRunning());
            syncWorkerType(channel, "NEWS", channel.getNewsWorkerRunning());
            syncWorkerType(channel, "OCR", channel.getOcrWorkerRunning());
        }

        // Clean up workers for channels that might have been deleted from DB or are no
        // longer in the list
        activeWorkers.keySet().removeIf(key -> {
            String[] parts = key.split(":");
            Long id = Long.parseLong(parts[0]);
            boolean exists = channels.stream().anyMatch(c -> c.getId().equals(id));
            if (!exists) {
                log.info("Stopping worker for deleted channel ID: {} key: {}", id, key);
                ScheduledFuture<?> future = activeWorkers.get(key);
                if (future != null)
                    future.cancel(false);
                return true;
            }
            return false;
        });
    }

    private void syncWorkerType(Channel channel, String type, Boolean shouldBeRunning) {
        String key = channel.getId() + ":" + type;
        boolean isRunning = activeWorkers.containsKey(key);

        if (Boolean.TRUE.equals(shouldBeRunning) && !isRunning) {
            startWorker(channel, type);
        } else if (!Boolean.TRUE.equals(shouldBeRunning) && isRunning) {
            stopWorker(channel.getId(), type);
        }
    }

    public void startWorker(Channel channel, String taskType) {
        String key = channel.getId() + ":" + taskType;
        if (activeWorkers.containsKey(key)) {
            log.warn("Worker already running for channel: {} type: {}", channel.getChannelName(), taskType);
            return;
        }

        log.info("Starting worker for channel: {} type: {}", channel.getChannelName(), taskType);

        TaskWorker worker = new TaskWorker(
                channel.getId(),
                channel.getChannelName(),
                taskType,
                taskRepository,
                channelRepository,
                generatedContentRepository,
                transcriptRepository,
                taskExecutionService,
                appConfigService,
                restTemplate);

        ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(worker, Duration.ofSeconds(10));
        activeWorkers.put(key, future);
    }

    public void stopWorker(Long channelId, String taskType) {
        String key = channelId + ":" + taskType;
        log.info("Stopping worker for channel ID: {} type: {}", channelId, taskType);
        ScheduledFuture<?> future = activeWorkers.remove(key);
        if (future != null) {
            future.cancel(false);
        }
    }
}
