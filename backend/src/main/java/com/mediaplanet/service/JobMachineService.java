package com.mediaplanet.service;

import com.mediaplanet.entity.JobMachine;
import com.mediaplanet.repository.JobMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobMachineService {

    @Autowired
    private JobMachineRepository jobMachineRepository;

    public List<JobMachine> getAllJobMachines() {
        return jobMachineRepository.findAll();
    }

    public JobMachine getJobMachineById(Long id) {
        return jobMachineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobMachine not found with id: " + id));
    }

    public JobMachine createJobMachine(JobMachine jobMachine) {
        return jobMachineRepository.save(jobMachine);
    }

    public JobMachine updateJobMachine(Long id, JobMachine jobMachineDetails) {
        JobMachine jobMachine = getJobMachineById(id);
        jobMachine.setMachineName(jobMachineDetails.getMachineName());
        jobMachine.setValue(jobMachineDetails.getValue());
        jobMachine.setDescription(jobMachineDetails.getDescription());
        jobMachine.setStatus(jobMachineDetails.getStatus());
        return jobMachineRepository.save(jobMachine);
    }

    public void deleteJobMachine(Long id) {
        JobMachine jobMachine = getJobMachineById(id);
        jobMachineRepository.delete(jobMachine);
    }

    public List<JobMachine> getActiveJobMachines() {
        return jobMachineRepository.findByStatus(true);
    }
}
