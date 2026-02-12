package com.mediaplanet.controller;

import com.mediaplanet.entity.AppConfig;
import com.mediaplanet.service.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/configs")
@CrossOrigin(origins = "*")
public class AppConfigController {

    @Autowired
    private AppConfigService appConfigService;

    @GetMapping
    public List<AppConfig> getAll() {
        return appConfigService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppConfig> getById(@PathVariable Long id) {
        return appConfigService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<AppConfig> getByKey(@PathVariable String key) {
        return appConfigService.getByKey(key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AppConfig create(@RequestBody AppConfig config) {
        return appConfigService.save(config);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppConfig> update(@PathVariable Long id, @RequestBody AppConfig config) {
        try {
            return ResponseEntity.ok(appConfigService.update(id, config));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appConfigService.delete(id);
        return ResponseEntity.ok().build();
    }
}
