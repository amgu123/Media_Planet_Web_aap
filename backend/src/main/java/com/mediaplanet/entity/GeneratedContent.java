package com.mediaplanet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "generated_contents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "task_type", nullable = false, length = 20)
    private String taskType; // AD, NEWS, OCR

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;
}
