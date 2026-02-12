package com.mediaplanet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resource_paths")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ResourcePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_machine_id", nullable = false)
    private JobMachine jobMachine;

    @Column(name = "value", length = 500)
    private String value;

    @Column(name = "description", length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean status = true;

    @Column(name = "priority", length = 50)
    private String priority; // Primary, Secondary

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
