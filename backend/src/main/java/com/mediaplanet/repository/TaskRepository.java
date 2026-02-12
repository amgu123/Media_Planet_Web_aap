package com.mediaplanet.repository;

import com.mediaplanet.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByChannelId(Long channelId);

    List<Task> findByStatus(String status);

    List<Task> findByChannelIdAndStatus(Long channelId, String status);
}
