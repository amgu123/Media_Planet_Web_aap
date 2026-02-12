package com.mediaplanet.controller;

import com.mediaplanet.entity.Channel;
import com.mediaplanet.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/channels")
@CrossOrigin(origins = "*")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @GetMapping
    public ResponseEntity<List<Channel>> getAllChannels() {
        return ResponseEntity.ok(channelService.getAllChannels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Channel> getChannelById(@PathVariable Long id) {
        return ResponseEntity.ok(channelService.getChannelById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Channel>> getActiveChannels() {
        return ResponseEntity.ok(channelService.getActiveChannels());
    }

    @PostMapping
    public ResponseEntity<Channel> createChannel(@RequestBody Channel channel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createChannel(channel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Channel> updateChannel(@PathVariable Long id, @RequestBody Channel channel) {
        return ResponseEntity.ok(channelService.updateChannel(id, channel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable Long id) {
        channelService.deleteChannel(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/workers/status")
    public ResponseEntity<Channel> updateWorkerStatus(
            @PathVariable Long id,
            @RequestParam String type,
            @RequestParam boolean running) {
        return ResponseEntity.ok(channelService.updateWorkerStatus(id, type, running));
    }
}
