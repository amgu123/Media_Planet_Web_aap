package com.mediaplanet.worker;

import com.mediaplanet.entity.Channel;
import com.mediaplanet.entity.GeneratedContent;
import com.mediaplanet.entity.Task;
import com.mediaplanet.repository.ChannelRepository;
import com.mediaplanet.repository.GeneratedContentRepository;
import com.mediaplanet.repository.TranscriptRepository;
import com.mediaplanet.repository.TaskRepository;
import com.mediaplanet.service.AppConfigService;
import com.mediaplanet.service.TaskExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class TaskWorker implements Runnable {

    private final Long channelId;
    private final String channelName;
    private final String taskType;
    private final TaskRepository taskRepository;
    private final ChannelRepository channelRepository;
    private final GeneratedContentRepository generatedContentRepository;
    private final TranscriptRepository transcriptRepository;
    private final TaskExecutionService taskExecutionService;
    private final AppConfigService appConfigService;
    private final RestTemplate restTemplate;

    @Override
    public void run() {
        try {
            processChannelTasks();
        } catch (Exception e) {
            log.error("Fatal error in worker for channel {} type {}: {}", channelName, taskType, e.getMessage());
        }
    }

    private void processChannelTasks() {
        log.info("Channel {}: Processing tasks of type {}", channelName, taskType);
        List<Task> pendingTasks = taskRepository.findByChannelIdAndStatusAndTaskType(channelId, "PENDING", taskType);

        if (pendingTasks.isEmpty()) {
            log.info("No task found -------------------- Channel {}: Processing tasks of type {}", channelName,
                    taskType);

            return;
        }

        log.debug("Channel {}: Found {} pending tasks.", channelName, pendingTasks.size());

        for (Task task : pendingTasks) {
            // Check if worker should still be running
            if (!isWorkerRunning()) {
                log.info("Channel {}: Stop signal detected. Terminating task processing loop for type {}.", channelName,
                        taskType);
                break;
            }

            try {
                processSingleTask(task);
            } catch (Exception e) {
                log.error("Channel {}: Error processing task ID {}: {}", channelName, task.getId(), e.getMessage());
                task.setStatus("FAILED");
                taskRepository.save(task);
            }
        }
    }

    private boolean isWorkerRunning() {
        return channelRepository.findById(channelId)
                .map(channel -> {
                    switch (taskType.toUpperCase()) {
                        case "AD":
                            return Boolean.TRUE.equals(channel.getAdWorkerRunning());
                        case "NEWS":
                            return Boolean.TRUE.equals(channel.getNewsWorkerRunning());
                        case "OCR":
                            return Boolean.TRUE.equals(channel.getOcrWorkerRunning());
                        default:
                            return false;
                    }
                })
                .orElse(false);
    }

    private void processSingleTask(Task task) {
        log.info("Channel {}: Processing task ID {} type {}", channelName, task.getId(), task.getTaskType());

        // 1. Update Task status to RUNNING
        task.setStatus("RUNNING");
        taskRepository.save(task);

        // 2. Update/Create TaskExecution entry
        taskExecutionService.startTask(channelId, task.getTaskType());

        // 3. Call external API
        try {
            makeApiCall(task);

            log.info("Channel {}: API call successful for task ID {}", channelName, task.getId());

            // 4. Update status to COMPLETED
            task.setStatus("COMPLETED");
            taskRepository.save(task);

            // 5. Update TaskExecution
            taskExecutionService.stopTask(channelId, task.getTaskType());

        } catch (Exception e) {
            log.error("Channel {}: API call failed for task ID {}: {}", channelName, task.getId(), e.getMessage());
            task.setStatus("FAILED");
            taskRepository.save(task);
            taskExecutionService.stopTask(channelId, task.getTaskType());
        }
    }

    private void makeApiCall(Task task) {

        // get the channel langauge from the channel table
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        String languageName = channel.getLanguage().getLanguageName().toLowerCase();

        String apiUrl = "";
        if (languageName.equals("english")) {
            apiUrl = appConfigService.getByKey("ENG_LANG_API_HOST")
                    .map(config -> config.getConfigValue())
                    .orElseThrow(() -> new RuntimeException("DETECTION_API_URL not configured in app_configs"));
        } else {
            apiUrl = appConfigService.getByKey("MULTI_LANG_API_HOST")
                    .map(config -> config.getConfigValue())
                    .orElseThrow(() -> new RuntimeException("DETECTION_API_URL not configured in app_configs"));
        }

        // Format dates and times
        String formattedDate = task.getDataDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // Remove colons if present
        String formattedTime = task.getDateTime().replace(":", "");

        // Build Payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("channel", Collections.singletonList(channelName));
        payload.put("dates", Collections.singletonList(formattedDate));
        payload.put("times", Collections.singletonList(formattedTime));
        payload.put("language", languageName != null ? languageName.toLowerCase() : "unknown");

        log.info("Channel {}: Sending request to {}: {}", channelName, apiUrl, payload);

        // API Call
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(apiUrl, payload, Map.class);

        log.info("Channel {}: Received response: OK", channelName);

        if (response == null || !"batch_complete".equals(response.get("status"))) {
            throw new RuntimeException("API response status was not batch_complete: " + response);
        }

        // Handle Response Results
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        if (results != null && !results.isEmpty()) {
            for (Map<String, Object> result : results) {
                saveTranscript(task, result);
            }
        }
    }

    private void saveTranscript(Task task, Map<String, Object> result) {
        try {
            Object transcriptContent = result.get("transcript_content");
            String fileName = (String) result.get("file");

            if (transcriptContent != null) {
                GeneratedContent gc = new GeneratedContent();
                gc.setChannel(task.getChannel());
                gc.setTaskType(task.getTaskType());
                gc.setFileName(fileName);
                gc.setDataDate(task.getDataDate());

                GeneratedContent savedGc = generatedContentRepository.save(gc);
                log.info("Channel {}: Saved metadata for file {} in database.", channelName, fileName);

                // Save individual transcript segments
                if (transcriptContent instanceof List) {
                    List<?> list = (List<?>) transcriptContent;
                    for (Object obj : list) {
                        if (obj instanceof java.util.Map) {
                            java.util.Map<String, String> segment = (java.util.Map<String, String>) obj;
                            com.mediaplanet.entity.Transcript transcript = new com.mediaplanet.entity.Transcript();
                            transcript.setGeneratedContent(savedGc);
                            transcript.setText(segment.get("text"));
                            transcript.setTimeStamp(segment.get("time_stamp"));
                            transcriptRepository.save(transcript);
                        }
                    }
                    log.info("Channel {}: Saved {} segments for file {} in database.", channelName, list.size(),
                            fileName);
                }
            }
        } catch (Exception e) {
            log.error("Channel {}: Failed to save transcript: {}", channelName, e.getMessage());
        }
    }
}
