package com.mediaplanet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "task_type", length = 20)
    private String taskType; // AD, NEWS, OCR

    @Column(name = "status", nullable = false, length = 20)
    private String status; // PENDING, RUNNING, COMPLETED, FAILED

    @Column(name = "data_date")
    private LocalDate dataDate;

    @Column(name = "date_time")
    private String dateTime;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
