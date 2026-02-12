package com.mediaplanet.service;

import com.mediaplanet.entity.AppConfig;
import com.mediaplanet.repository.AppConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppConfigService {

    @Autowired
    private AppConfigRepository appConfigRepository;

    public List<AppConfig> getAll() {
        return appConfigRepository.findAll();
    }

    public Optional<AppConfig> getById(Long id) {
        if (id == null)
            return Optional.empty();
        return appConfigRepository.findById(id);
    }

    public Optional<AppConfig> getByKey(String key) {
        if (key == null)
            return Optional.empty();
        return appConfigRepository.findByConfigKey(key);
    }

    public AppConfig save(AppConfig config) {
        if (config == null)
            throw new IllegalArgumentException("Config cannot be null");
        return appConfigRepository.save(config);
    }

    public void delete(Long id) {
        if (id != null) {
            appConfigRepository.deleteById(id);
        }
    }

    public AppConfig update(Long id, AppConfig config) {
        if (id == null || config == null)
            throw new IllegalArgumentException("Id and Config cannot be null");
        return appConfigRepository.findById(id)
                .map(existing -> {
                    existing.setConfigKey(config.getConfigKey());
                    existing.setConfigValue(config.getConfigValue());
                    existing.setDescription(config.getDescription());
                    return appConfigRepository.save(existing);
                }).orElseThrow(() -> new RuntimeException("Config not found with id " + id));
    }
}
