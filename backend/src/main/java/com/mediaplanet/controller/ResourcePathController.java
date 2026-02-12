package com.mediaplanet.controller;

import com.mediaplanet.entity.ResourcePath;
import com.mediaplanet.service.ResourcePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource-paths")
@CrossOrigin(origins = "*")
public class ResourcePathController {

    @Autowired
    private ResourcePathService resourcePathService;

    @GetMapping
    public ResponseEntity<List<ResourcePath>> getAllResourcePaths() {
        return ResponseEntity.ok(resourcePathService.getAllResourcePaths());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourcePath> getResourcePathById(@PathVariable Long id) {
        return ResponseEntity.ok(resourcePathService.getResourcePathById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ResourcePath>> getActiveResourcePaths() {
        return ResponseEntity.ok(resourcePathService.getActiveResourcePaths());
    }

    @GetMapping("/job-machine/{jobMachineId}")
    public ResponseEntity<List<ResourcePath>> getResourcePathsByJobMachine(@PathVariable Long jobMachineId) {
        return ResponseEntity.ok(resourcePathService.getResourcePathsByJobMachine(jobMachineId));
    }

    @PostMapping
    public ResponseEntity<ResourcePath> createResourcePath(@RequestBody ResourcePath resourcePath) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resourcePathService.createResourcePath(resourcePath));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourcePath> updateResourcePath(@PathVariable Long id,
            @RequestBody ResourcePath resourcePath) {
        return ResponseEntity.ok(resourcePathService.updateResourcePath(id, resourcePath));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourcePath(@PathVariable Long id) {
        resourcePathService.deleteResourcePath(id);
        return ResponseEntity.noContent().build();
    }
}
