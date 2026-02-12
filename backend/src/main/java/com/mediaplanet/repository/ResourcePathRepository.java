package com.mediaplanet.repository;

import com.mediaplanet.entity.ResourcePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourcePathRepository extends JpaRepository<ResourcePath, Long> {

    List<ResourcePath> findByStatus(Boolean status);

    List<ResourcePath> findByJobMachineId(Long jobMachineId);
}
