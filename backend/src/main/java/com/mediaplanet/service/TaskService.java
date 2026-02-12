package com.mediaplanet.service;

import com.mediaplanet.entity.Channel;
import com.mediaplanet.entity.Task;
import com.mediaplanet.repository.ChannelRepository;
import com.mediaplanet.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChannelRepository channelRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByChannel(Long channelId) {
        return taskRepository.findByChannelId(channelId);
    }

    public Task createTask(Task task) {
        if (task.getChannel() != null && task.getChannel().getId() != null) {
            Channel channel = channelRepository.findById(task.getChannel().getId())
                    .orElseThrow(() -> new RuntimeException("Channel not found"));
            task.setChannel(channel);
        }
        return taskRepository.save(task);
    }

    public Task updateTaskStatus(Long id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
