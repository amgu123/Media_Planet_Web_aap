package com.mediaplanet.controller;

import com.mediaplanet.entity.JobMachine;
import com.mediaplanet.service.JobMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-machines")
@CrossOrigin(origins = "*")
public class JobMachineController {

    @Autowired
    private JobMachineService jobMachineService;

    @GetMapping
    public ResponseEntity<List<JobMachine>> getAllJobMachines() {
        return ResponseEntity.ok(jobMachineService.getAllJobMachines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobMachine> getJobMachineById(@PathVariable Long id) {
        return ResponseEntity.ok(jobMachineService.getJobMachineById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<JobMachine>> getActiveJobMachines() {
        return ResponseEntity.ok(jobMachineService.getActiveJobMachines());
    }

    @PostMapping
    public ResponseEntity<JobMachine> createJobMachine(@RequestBody JobMachine jobMachine) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobMachineService.createJobMachine(jobMachine));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobMachine> updateJobMachine(@PathVariable Long id, @RequestBody JobMachine jobMachine) {
        return ResponseEntity.ok(jobMachineService.updateJobMachine(id, jobMachine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobMachine(@PathVariable Long id) {
        jobMachineService.deleteJobMachine(id);
        return ResponseEntity.noContent().build();
    }
}
