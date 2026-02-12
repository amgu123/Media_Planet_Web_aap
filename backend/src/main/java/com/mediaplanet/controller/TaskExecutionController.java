package com.mediaplanet.controller;

import com.mediaplanet.entity.TaskExecution;
import com.mediaplanet.service.TaskExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task-executions")
@CrossOrigin(origins = "*")
public class TaskExecutionController {

    @Autowired
    private TaskExecutionService taskExecutionService;

    @GetMapping
    public ResponseEntity<List<TaskExecution>> getAllTaskExecutions() {
        return ResponseEntity.ok(taskExecutionService.getAllTaskExecutions());
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<TaskExecution>> getTaskExecutionsByChannel(@PathVariable Long channelId) {
        return ResponseEntity.ok(taskExecutionService.getTaskExecutionsByChannel(channelId));
    }

    @PostMapping("/{channelId}/{taskType}/start")
    public ResponseEntity<TaskExecution> startTask(@PathVariable Long channelId, @PathVariable String taskType) {
        return ResponseEntity.ok(taskExecutionService.startTask(channelId, taskType));
    }

    @PostMapping("/{channelId}/{taskType}/stop")
    public ResponseEntity<TaskExecution> stopTask(@PathVariable Long channelId, @PathVariable String taskType) {
        return ResponseEntity.ok(taskExecutionService.stopTask(channelId, taskType));
    }

    @GetMapping("/{channelId}/{taskType}/logs")
    public ResponseEntity<Map<String, String>> getTaskLogs(@PathVariable Long channelId,
            @PathVariable String taskType) {
        String logs = taskExecutionService.getTaskLogs(channelId, taskType);
        return ResponseEntity.ok(Map.of("logs", logs));
    }
}
