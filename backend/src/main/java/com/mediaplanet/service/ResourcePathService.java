package com.mediaplanet.service;

import com.mediaplanet.entity.JobMachine;
import com.mediaplanet.entity.ResourcePath;
import com.mediaplanet.repository.JobMachineRepository;
import com.mediaplanet.repository.ResourcePathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourcePathService {

    @Autowired
    private ResourcePathRepository resourcePathRepository;

    @Autowired
    private JobMachineRepository jobMachineRepository;

    public List<ResourcePath> getAllResourcePaths() {
        return resourcePathRepository.findAll();
    }

    public ResourcePath getResourcePathById(Long id) {
        return resourcePathRepository.findById(id).orElse(null);
    }

    public ResourcePath createResourcePath(ResourcePath resourcePath) {
        return resourcePathRepository.save(resourcePath);
    }

    public ResourcePath updateResourcePath(Long id, ResourcePath resourcePathDetails) {
        ResourcePath resourcePath = getResourcePathById(id);
        resourcePath.setJobMachine(resourcePathDetails.getJobMachine());
        resourcePath.setValue(resourcePathDetails.getValue());
        resourcePath.setDescription(resourcePathDetails.getDescription());
        resourcePath.setStatus(resourcePathDetails.getStatus());
        return resourcePathRepository.save(resourcePath);
    }

    public void deleteResourcePath(Long id) {
        ResourcePath resourcePath = getResourcePathById(id);
        resourcePathRepository.delete(resourcePath);
    }

    public List<ResourcePath> getActiveResourcePaths() {
        return resourcePathRepository.findByStatus(true);
    }

    public List<ResourcePath> getResourcePathsByJobMachine(Long jobMachineId) {
        return resourcePathRepository.findByJobMachineId(jobMachineId);
    }
}
