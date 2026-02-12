package com.mediaplanet.worker;

import com.mediaplanet.entity.Task;
import com.mediaplanet.entity.TaskExecution;
import com.mediaplanet.repository.TaskRepository;
import com.mediaplanet.service.TaskExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TaskWorker implements Runnable {

    private final Long channelId;
    private final String channelName;
    private final TaskRepository taskRepository;
    private final TaskExecutionService taskExecutionService;

    @Override
    public void run() {
        try {
            processChannelTasks();
        } catch (Exception e) {
            log.error("Fatal error in worker for channel {}: {}", channelName, e.getMessage());
        }
    }

    private void processChannelTasks() {
        List<Task> pendingTasks = taskRepository.findByChannelIdAndStatus(channelId, "PENDING");

        if (pendingTasks.isEmpty()) {
            return;
        }

        log.debug("Channel {}: Found {} pending tasks.", channelName, pendingTasks.size());

        for (Task task : pendingTasks) {
            try {
                processSingleTask(task);
            } catch (Exception e) {
                log.error("Channel {}: Error processing task ID {}: {}", channelName, task.getId(), e.getMessage());
                task.setStatus("FAILED");
                taskRepository.save(task);
            }
        }
    }

    private void processSingleTask(Task task) throws InterruptedException {
        log.info("Channel {}: Processing task ID {} type {}", channelName, task.getId(), task.getTaskType());

        // 1. Update Task status to RUNNING
        task.setStatus("RUNNING");
        taskRepository.save(task);

        // 2. Update/Create TaskExecution entry
        taskExecutionService.startTask(channelId, task.getTaskType());

        // 3. Call external API (Placeholder)
        try {
            // Simulating API call
            Thread.sleep(2000);

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
}
