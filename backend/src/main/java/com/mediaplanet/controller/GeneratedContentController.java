package com.mediaplanet.controller;

import com.mediaplanet.entity.GeneratedContent;
import com.mediaplanet.service.GeneratedContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/generated-content")
@CrossOrigin(origins = "*")
public class GeneratedContentController {

    @Autowired
    private GeneratedContentService service;

    @GetMapping
    public ResponseEntity<Page<GeneratedContent>> getFilteredContent(
            @RequestParam(required = false) Long channelId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity
                .ok(service.getFilteredContent(channelId, startDate, endDate, startTime, endTime, pageRequest));
    }
}
