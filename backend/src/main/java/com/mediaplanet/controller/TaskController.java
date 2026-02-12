package com.mediaplanet.controller;

import com.mediaplanet.entity.Task;
import com.mediaplanet.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Task>> getTasksByChannel(@PathVariable Long channelId) {
        return ResponseEntity.ok(taskService.getTasksByChannel(channelId));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody String status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}
