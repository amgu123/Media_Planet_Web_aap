package com.mediaplanet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_executions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "task_type", nullable = false, length = 20)
    private String taskType; // AD, NEWS, OCR

    @Column(name = "status", nullable = false, length = 20)
    private String status; // RUNNING, STOPPED, FAILED

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "stop_time")
    private LocalDateTime stopTime;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
