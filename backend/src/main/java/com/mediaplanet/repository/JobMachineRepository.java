package com.mediaplanet.repository;

import com.mediaplanet.entity.JobMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobMachineRepository extends JpaRepository<JobMachine, Long> {

    List<JobMachine> findByStatus(Boolean status);

    List<JobMachine> findByMachineNameContainingIgnoreCase(String name);
}
