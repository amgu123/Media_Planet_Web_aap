package com.mediaplanet.repository;

import com.mediaplanet.entity.TaskExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskExecutionRepository extends JpaRepository<TaskExecution, Long> {
    List<TaskExecution> findByChannelId(Long channelId);

    Optional<TaskExecution> findByChannelIdAndTaskType(Long channelId, String taskType);
}
